package edu.ucdenver.ccp.datasource.fileparsers.snomed;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.snomed.SnomedRf2DescriptionFileRecord.DescriptionType;
import lombok.Data;

/**
 * Parses the snomed distribution files and creates an obo ontology of is_a relations
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SnoMedOboGenerator {

	private static final Logger logger = Logger.getLogger(SnoMedOboGenerator.class);

	public static void generateObo(File snomedConceptFile, File snomedDescriptionFile, File snomedRelationFile,
			File outputOboFile, Set<String> rootNodesToInclude) throws IOException {
		Map<String, Term> conceptIdToTermMap = new HashMap<String, Term>();
		initConceptTerms(conceptIdToTermMap, snomedConceptFile);
		addNamesAndSynonymsToTerms(conceptIdToTermMap, snomedDescriptionFile);
		addRelationsBetweenTerms(conceptIdToTermMap, snomedRelationFile);
		outputTermsToFile(conceptIdToTermMap, outputOboFile, rootNodesToInclude);
	}

	/**
	 * @param conceptIdToTermMap
	 * @param snomedRelationFile
	 * @throws IOException
	 */
	private static void addRelationsBetweenTerms(Map<String, Term> conceptIdToTermMap, File snomedRelationFile)
			throws IOException {
		for (SnomedRf2RelationshipFileRecordReader rr = new SnomedRf2RelationshipFileRecordReader(snomedRelationFile); rr
				.hasNext();) {
			SnomedRf2RelationshipFileRecord record = rr.next();

			String sourceConceptId = record.getSourceConceptId();
			String destinationConceptId = record.getDestinationConceptId();
			String relationshipTypeId = record.getTypeId();

			if (record.isActive() && conceptIdToTermMap.containsKey(sourceConceptId)
					&& conceptIdToTermMap.containsKey(destinationConceptId)) {
				if (relationshipTypeId.equals("116680003")) { // then it's an is_a
					conceptIdToTermMap.get(sourceConceptId).addParent(conceptIdToTermMap.get(destinationConceptId));
				}
			}
		}
	}

	/**
	 * @param conceptIdToTermMap
	 * @param snomedDescriptionFile
	 * @throws IOException
	 */
	private static void addNamesAndSynonymsToTerms(Map<String, Term> conceptIdToTermMap, File snomedDescriptionFile)
			throws IOException {
		for (SnomedRf2DescriptionFileRecordReader rr = new SnomedRf2DescriptionFileRecordReader(snomedDescriptionFile); rr
				.hasNext();) {
			SnomedRf2DescriptionFileRecord record = rr.next();

			String conceptId = record.getConceptId();
			DescriptionType type = record.getType();
			String termDescription = record.getTerm();

			if (record.isActive()) {
				if (conceptIdToTermMap.containsKey(conceptId)) {
					if (type.equals(DescriptionType.FULLY_SPECIFIED_NAME)) {
						conceptIdToTermMap.get(conceptId).setName(termDescription);
					} else if (type.equals(DescriptionType.SYNONYM)) {
						conceptIdToTermMap.get(conceptId).addSynonym(termDescription);
					} else {
						throw new IllegalStateException("Code does not handle description of type: " + type.name());
					}
				} else {
					logger.warn("Description: Concept identifier not present in conceptIdToTermMap: " + conceptId);
				}
			}
		}
	}

	/**
	 * @param conceptIdToTermMap
	 * @param snomedConceptFile
	 * @throws IOException
	 */
	private static void initConceptTerms(Map<String, Term> conceptIdToTermMap, File snomedConceptFile)
			throws IOException {
		for (SnomedRf2ConceptFileRecordReader rr = new SnomedRf2ConceptFileRecordReader(snomedConceptFile); rr
				.hasNext();) {
			SnomedRf2ConceptFileRecord record = rr.next();

			String conceptId = record.getConceptId();
			String effectiveTime = record.getEffectiveTime();
			boolean isActive = record.isActive();
			if (isActive) {
				if (!conceptIdToTermMap.containsKey(conceptId)) {
					conceptIdToTermMap.put(conceptId, new Term("SNOMED:" + conceptId, effectiveTime));
				} else {
					logger.warn("Duplicate concept id detected: " + conceptId);
				}
			} else {
				// if the concept id is not active and the effective date is after the effective
				// date for a term already in the hash with the same concept id, then we need to
				// remove the term already in the hash b/c it was inactivated
				if (conceptIdToTermMap.containsKey(conceptId)) {
					Term t = conceptIdToTermMap.get(conceptId);
					if (t.hasEffectiveDateEqualToOrBefore(record.getEffectiveTime())) {
						conceptIdToTermMap.remove(conceptId);
					}
				}
			}
		}

	}

	/**
	 * @param conceptIdToTermMap
	 * @param outputOboFile
	 * @throws IOException
	 */
	private static void outputTermsToFile(Map<String, Term> conceptIdToTermMap, File outputOboFile,
			Set<String> rootNodesToInclude) throws IOException {
		BufferedWriter writer = FileWriterUtil.initBufferedWriter(outputOboFile, CharacterEncoding.UTF_8,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		// System.out.println("Sample Term ID: " +
		// conceptIdToTermMap.entrySet().iterator().next().getKey());
		try {
			for (Term term : conceptIdToTermMap.values()) {
				// System.out.println("Term ID: " + term);
				System.out.println(isChildOfRootNode(term, rootNodesToInclude, conceptIdToTermMap));
				if (rootNodesToInclude == null || isChildOfRootNode(term, rootNodesToInclude, conceptIdToTermMap)
						|| term.getId().equals("SNOMED:138875005")) {
					writer.write(term.toOboString());
				}
			}
		} finally {
			writer.close();
		}

	}

	/**
	 * @param id
	 * @param rootNodesToInclude
	 * @param conceptIdToTermMap
	 * @return
	 */
	private static boolean isChildOfRootNode(Term term, Set<String> rootNodesToInclude,
			Map<String, Term> conceptIdToTermMap) {
		String id = StringUtil.removePrefix(term.getId(), "SNOMED:");
		if (rootNodesToInclude.contains(id)) {
			System.out.println("is root node!!!! " + id);
			return true;
		}
		if (term.getParentTerms() != null) {
			for (Term parentTerm : term.getParentTerms()) {
				if (isChildOfRootNode(parentTerm, rootNodesToInclude, conceptIdToTermMap)) {
					return true;
				}
			}
		}

		return false;
	}

	@Data
	public static class Term {
		private final String id;
		private String name;
		private Set<String> synonyms;
		private Set<Term> parentTerms;
		private final String effectiveDate;

		public void addSynonym(String syn) {
			if (synonyms == null) {
				synonyms = new HashSet<String>();
			}
			synonyms.add(syn);
		}

		public void addParent(Term parentTerm) {
			if (parentTerms == null) {
				parentTerms = new HashSet<Term>();
			}
			parentTerms.add(parentTerm);
		}

		public boolean hasEffectiveDateEqualToOrBefore(String dateStr) {
			Integer other = Integer.parseInt(dateStr);
			Integer mine = Integer.parseInt(this.getEffectiveDate());

			return mine <= other;
		}

		// public boolean hasEffectiveDateEqualOrLaterThan(Term t) {
		// Integer other = Integer.parseInt(t.getEffectiveDate());
		// Integer mine = Integer.parseInt(this.getEffectiveDate());
		//
		// return mine >= other;
		// }

		/**
		 * example OBO term:
		 * 
		 * <pre>
		 * [Term]
		 * id: CL:0000000
		 * name: cell
		 * namespace: cell
		 * def: "A material entity of anatomical origin (part of or deriving from an organism) that has as its parts a maximally connected cell compartment surrounded by a plasma membrane." [CARO:mah]
		 * comment: The definition of cell is intended to represent all cells, and thus a cell is defined as a material entity and not an anatomical structure, which implies that it is part of an organism (or the entirety of one).
		 * synonym: "primary cell culture cell" EXACT []
		 * synonym: "primary cell line cell" RELATED []
		 * subset: ubprop:upper_level
		 * xref: XAO:0003012
		 * is_a: GO:0005575 ! cellular_component
		 * equivalent_to: GO:0005623 ! cell
		 * </pre>
		 * 
		 * @return
		 */
		public String toOboString() {
			StringBuilder builder = new StringBuilder();
			builder.append("\n[Term]\n");
			builder.append("id: " + id + "\n");
			builder.append("name: " + name + "\n");
			if (synonyms != null) {
				for (String syn : synonyms) {
					if (syn.contains("\"")) {
						syn = syn.replaceAll("\"", "");
					}
					builder.append("synonym: \"" + syn + "\" EXACT []\n");
				}
			}
			if (parentTerms != null) {
				for (Term parentTerm : parentTerms) {
					builder.append("is_a: " + parentTerm.getId() + " ! " + parentTerm.getName() + "\n");
				}
			}
			return builder.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Term other = (Term) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		File snomedDirectory = new File(
				"/Users/bill/Documents/snomed/SnomedCT_Release_INT_20130731/RF2Release/Full/Terminology");
		File snomedConceptFile = new File(snomedDirectory, "sct2_Concept_Full_INT_20130731.txt.activeOnly");
		File snomedDescriptionFile = new File(snomedDirectory, "sct2_Description_Full-en_INT_20130731.txt.activeOnly");
		File snomedRelationFile = new File(snomedDirectory, "sct2_Relationship_Full_INT_20130731.txt.activeOnly");
		File outputOboFile = new File(snomedDirectory, "snomed-restricted.obo");
		Set<String> rootNodesToInclude = CollectionsUtil.createSet("123037004", "404684003");
		try {
			generateObo(snomedConceptFile, snomedDescriptionFile, snomedRelationFile, outputOboFile, rootNodesToInclude);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
