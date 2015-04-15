package edu.ucdenver.ccp.datasource.fileparsers.obo;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.geneontology.oboedit.dataadapter.DefaultOBOParser;
import org.geneontology.oboedit.dataadapter.OBOParseEngine;
import org.geneontology.oboedit.dataadapter.OBOParseException;
import org.geneontology.oboedit.datamodel.LinkedObject;
import org.geneontology.oboedit.datamodel.OBOClass;
import org.geneontology.oboedit.datamodel.OBOSession;
import org.geneontology.oboedit.datamodel.Synonym;
import org.geneontology.oboedit.datamodel.impl.OBORestrictionImpl;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.collections.IteratorUtil;
import edu.ucdenver.ccp.common.collections.LegacyCollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineIterator;
import edu.ucdenver.ccp.datasource.identifiers.obo.OntologyID;

/**
 * Utility class for dealing with OBO files.
 * 
 * @author Bill Baumgartner
 * @param <T>
 * 
 */
public class OboUtil<T extends OntologyID> {
	private static final Logger logger = Logger.getLogger(OboUtil.class);

	public enum ObsoleteTermHandling {
		INCLUDE_OBSOLETE_TERMS,
		EXCLUDE_OBSOLETE_TERMS
	}

	public enum SynonymType {
		RELATED(0),
		EXACT(1),
		NARROW(2),
		BROAD(3);

		private final int scope;

		private SynonymType(int scope) {
			this.scope = scope;
		}

		public int scope() {
			return scope;
		}

		public static SynonymType getTypeFromScope(int scope) {
			for (SynonymType type : SynonymType.values()) {
				if (type.scope() == scope) {
					return type;
				}
			}
			throw new IllegalArgumentException("Unknown scope ID: " + scope);
		}
	}

	private OBOSession session;

	public OboUtil(File oboFile, CharacterEncoding encoding) throws IOException {
		session = initSession(oboFile, encoding);
	}

	public Iterator<OBOClass> getClassIterator() {
		return IteratorUtil.consolidate(
				LegacyCollectionsUtil.checkIterator(session.getTerms().iterator(), OBOClass.class),
				LegacyCollectionsUtil.checkIterator(session.getObsoleteTerms().iterator(), OBOClass.class));
	}

	public Iterator<OBOClass> getClassIterator(ObsoleteTermHandling obsoleteHandling) {
		if (obsoleteHandling.equals(ObsoleteTermHandling.EXCLUDE_OBSOLETE_TERMS)) {
			return LegacyCollectionsUtil.checkIterator(session.getTerms().iterator(), OBOClass.class);
		}
		return IteratorUtil.consolidate(
				LegacyCollectionsUtil.checkIterator(session.getTerms().iterator(), OBOClass.class),
				LegacyCollectionsUtil.checkIterator(session.getObsoleteTerms().iterator(), OBOClass.class));
	}

	/**
	 * This method taken directly from the OBO-Edit FAQ webpage:
	 * http://wiki.geneontology.org/index.php/OBO-Edit:_OBO_Parser_-_Getting_Started
	 * 
	 * @param path
	 * @return an OBOSession object containing the contents of the input obo file
	 * @throws OBOParseException
	 * @throws IOException
	 */
	private OBOSession initSession(File oboFile, CharacterEncoding encoding) throws IOException {
		File modifiedOboFile = removeConsiderTags(oboFile, encoding);
		DefaultOBOParser parser = new DefaultOBOParser();
		OBOParseEngine engine = new OBOParseEngine(parser);
		/*
		 * OBOParseEngine can parse several files at once and create one munged-together ontology,
		 * so we need to provide a Collection to the setPaths() method
		 */
		engine.setPaths(CollectionsUtil.createList(modifiedOboFile.getAbsolutePath()));
		try {
			engine.parse();
		} catch (OBOParseException e) {
			throw new IOException(e);
		}
		return parser.getSession();
	}

	/**
	 * There appears to be an issue with unresolved identifiers in consider: tags. This method
	 * simply creates a copy of the obo file with the consider tags removed.
	 * 
	 * @param oboFile
	 * @return
	 * @throws IOException
	 */
	private static File removeConsiderTags(File oboFile, CharacterEncoding encoding) throws IOException {
		Pattern synPattern = Pattern.compile("(synonym: \".*?\") (\\w+:\\d+)( \\[\\])");
		Matcher m;
		File modifiedOboFile = new File(oboFile.getParentFile(), oboFile.getName() + ".mod");
		BufferedWriter writer = null;
		try {
			writer = FileWriterUtil.initBufferedWriter(modifiedOboFile, encoding, WriteMode.OVERWRITE,
					FileSuffixEnforcement.OFF);
			for (StreamLineIterator lineIter = new StreamLineIterator(oboFile, encoding); lineIter.hasNext();) {
				boolean writeLine = true;
				Line line = lineIter.next();
				String lineText = line.getText();
				if (lineText.startsWith("consider:") || lineText.startsWith("replaced_by:")) {
					writeLine = false;
				} else {
					m = synPattern.matcher(lineText);
					if (m.find()) {
						/*
						 * this is used to process the OBI file b/c it has synonm lines like the
						 * following that OBOEdit complains about: synonym: "antibody" OBI:9991118
						 * []
						 */

						lineText = m.group(1) + " EXACT" + m.group(3);
					}
				}

				if (writeLine) {
					writer.write(lineText);
					writer.newLine();
				}
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return modifiedOboFile;
	}

	/**
	 * Returns a mapping from term name/synonym/id string to an Ontology ID
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Set<T>> getTermName2IdMap(String idPrefix) {
		Map<String, OBOClass> termID2OboClassMap = getTermID2OboClassMap();
		Map<String, Set<T>> synonym2NameMap = new HashMap<String, Set<T>>();
		for (Entry<String, OBOClass> entry : termID2OboClassMap.entrySet()) {
			String termID = entry.getKey();
			if (termID.startsWith(idPrefix)) {
				T ontologyID = (T) OntologyIdResolver.resolveOntologyID(termID);
				OBOClass oboClass = entry.getValue();
				String termName = oboClass.getName().toLowerCase();

				addTermIdPairToMap(synonym2NameMap, ontologyID, termName);

				Set<Synonym> synonyms = oboClass.getSynonyms();
				for (Synonym synonym : synonyms) {
					String synonymText = synonym.getText();
					addTermIdPairToMap(synonym2NameMap, ontologyID, synonymText);
				}

				synonym2NameMap.put(termID.toLowerCase(), CollectionsUtil.createSet(ontologyID));
			}
		}

		return synonym2NameMap;
	}

	/**
	 * Addes a synonym/ontology ID pair to the input map. Throws a RuntimeException if the synonym
	 * already exists in the map.
	 * 
	 * @param synonym2NameMap
	 * @param ontologyID
	 * @param termName
	 */
	@SuppressWarnings("unchecked")
	private void addTermIdPairToMap(Map<String, Set<T>> synonym2NameMap, T ontologyID, String termName) {
		if (!synonym2NameMap.containsKey(termName))
			synonym2NameMap.put(termName, CollectionsUtil.createSet(ontologyID));
		else {
			if (!synonym2NameMap.get(termName).contains(ontologyID)) {
				synonym2NameMap.get(termName).add(ontologyID);
				logger.warn(String.format("Duplicate term name detected: '%s' maps to multiple term ids: %s", termName,
						synonym2NameMap.get(termName).toString()));
			}
		}
	}

	/**
	 * Returns a mapping from term ID string to OBOClass object for each class in the ontology. This
	 * method makes a point to include obsolete classes. It also removes "meta" terms such as
	 * obo:object[obo:object].
	 * 
	 * @return
	 */
	private Map<String, OBOClass> getTermID2OboClassMap() {
		@SuppressWarnings("unchecked")
		Map<String, OBOClass> termID2OboClassMap = session.getAllTermsHash();
		@SuppressWarnings("unchecked")
		Set<OBOClass> obsoleteTerms = session.getObsoleteTerms();
		for (OBOClass obsoleteTerm : obsoleteTerms) {
			termID2OboClassMap.put(obsoleteTerm.getID(), obsoleteTerm);
		}

		List<String> termIDsToRemove = new ArrayList<String>();
		/*
		 * Check for invalid term ids.The OBOEdit parser can make terms such as:
		 * "obo:object[obo:object]  isRoot:true  isObsolete:false"
		 */
		for (String termID : termID2OboClassMap.keySet()) {
			if (!isValidOntologyID(termID)) {
				termIDsToRemove.add(termID);
			}
		}

		for (String termID : termIDsToRemove) {
			termID2OboClassMap.remove(termID);
		}
		return termID2OboClassMap;
	}

	private static boolean isValidOntologyID(String termID) {
		Pattern termIDPattern = Pattern.compile("^[A-Z]+:\\d+$");
		Matcher m = termIDPattern.matcher(termID);
		/*
		 * Ensure we are returning a true ontology term. The OBOEdit parser can make terms such as:
		 * "obo:object[obo:object]  isRoot:true  isObsolete:false"
		 */
		return m.find();
	}

	public OBOSession getSession() {
		return session;
	}

	public static Set<OBOClass> getAncestors(OBOClass cls) {
		Set<OBOClass> ancestors = new HashSet<OBOClass>();
		getAncestors(cls, ancestors);
		return ancestors;
	}

	public static Set<OBOClass> getDescendents(OBOClass cls) {
		Set<OBOClass> ancestors = new HashSet<OBOClass>();
		getDescendents(cls, ancestors);
		return ancestors;
	}

	/**
	 * climbs the hierarchy using is-a and part-of relations only to return ancestors
	 * 
	 * @param childClass
	 * @param ancestors
	 */
	private static void getAncestors(OBOClass childClass, Set<OBOClass> ancestors) {
		Set parents = childClass.getParents();
		boolean childIsDescendant = false;
		for (Object parent : parents) {
			if (parent instanceof OBORestrictionImpl) {
				OBORestrictionImpl parentImpl = (OBORestrictionImpl) parent;
				String edgeType = parentImpl.getType().getName();
				if (edgeType.equals("is_a")) { // not using edges that equal part_of || edgeType.equals("part_of")) {
					LinkedObject linkedObject = parentImpl.getParent();
					if (linkedObject instanceof OBOClass) {
						OBOClass parentClass = (OBOClass) linkedObject;
						ancestors.add(parentClass);
						if (!parentClass.isRoot()) {
							getAncestors(parentClass, ancestors);
						}
					}
				}
			}
		}
	}

	private static void getDescendents(OBOClass parentClass, Set<OBOClass> descendents) {
		for (Object child : parentClass.getChildren()) {
			if (child instanceof OBORestrictionImpl) {
				OBORestrictionImpl childImpl = (OBORestrictionImpl) child;
				String edgeType = childImpl.getType().getName();
				if (edgeType.equals("is_a")) { // not using edges that equal part_of || edgeType.equals("part_of")) {
					LinkedObject linkedObject = childImpl.getChild();
					if (linkedObject instanceof OBOClass) {
						OBOClass childClass = (OBOClass) linkedObject;
						descendents.add(childClass);
						getDescendents(childClass, descendents);
					}
				}
			}
		}
	}
	
	public boolean isDescendant(OBOClass childClass, OBOClass parentClass) {
		Set parents = childClass.getParents();
		boolean childIsDescendant = false;
		for (Object parent : parents) {
			if (!childIsDescendant) {
				if (parent instanceof OBORestrictionImpl) {
					OBORestrictionImpl parentImpl = (OBORestrictionImpl) parent;
					LinkedObject linkedObject = parentImpl.getParent();
					if (linkedObject instanceof OBOClass) {
						OBOClass childsParentClass = (OBOClass) linkedObject;
						// Only using the is_a relationships
						if(parentImpl.toString().contains("OBO_REL:is_a")) {
							if (childsParentClass.getID().equals(parentClass.getID()))
								childIsDescendant = true;
							else if (childsParentClass.isRoot())
								childIsDescendant = false;
							else
								childIsDescendant = isDescendant(childsParentClass, parentClass);
						}
					}
				}
			}
		}
		return childIsDescendant;
	}

	
	public boolean isDescendent(String childId, String parentId) {
		OBOClass childClass = session.getTerm(childId);
		OBOClass parentClass = session.getTerm(parentId);
		if (childClass == null) {
			if (childId.equals("continuant") || childId.equals("syntactic context")) {
				logger.debug("Null child class: \"" + childId + "\"");
			} else {
				logger.warn("Null child class: \"" + childId + "\"");
			}
			return false;
		}
		if (parentClass == null) {
			logger.warn("Null parent class: \"" + parentId + "\"");
			return false;
		}
		return isDescendant(childClass, parentClass);
	}

	/**
	 * 
	 * @param ontologyId
	 * @return boolean if ID is obsolete or not
	 */
	public boolean isObsolete(String ontologyId) {
		@SuppressWarnings("unchecked")
		Set<String> obsoleteTerms = session.getObsoleteTerms();
		OBOClass annotation = session.getTerm(ontologyId);

		return obsoleteTerms.contains(annotation);
	}

	public boolean isInOntology(String ontologyId) {
		return session.containsObject(ontologyId);
	}
	
	
	public static void main(String[] args) throws IOException {
		
	}

}
