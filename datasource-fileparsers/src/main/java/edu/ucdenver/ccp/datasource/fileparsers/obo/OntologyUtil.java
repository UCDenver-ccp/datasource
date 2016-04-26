package edu.ucdenver.ccp.datasource.fileparsers.obo;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2016 Regents of the University of Colorado
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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tools.ant.util.StringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import owltools.graph.OWLGraphWrapper;

public class OntologyUtil {

	private static final Logger logger = Logger.getLogger(OntologyUtil.class);
	private static final String EXACT_SYN_PROP = "<http://www.geneontology.org/formats/oboInOwl#hasExactSynonym>";
	private static final String EXACT_SYN_PROP_ALT = "<http://purl.obolibrary.org/obo/exact_synonym>";
	private static final String RELATED_SYN_PROP = "<http://www.geneontology.org/formats/oboInOwl#hasRelatedSynonym>";
	private static final String RELATED_SYN_PROP_ALT = "<http://purl.obolibrary.org/obo/related_synonym>";
	private static final String NARROW_SYN_PROP = "<http://www.geneontology.org/formats/oboInOwl#hasNarrowSynonym>";
	private static final String NARROW_SYN_PROP_ALT = "<http://purl.obolibrary.org/obo/narrow_synonym>";
	private static final String BROAD_SYN_PROP = "<http://www.geneontology.org/formats/oboInOwl#hasBroadSynonym>";
	private static final String BROAD_SYN_PROP_ALT = "<http://purl.obolibrary.org/obo/broad_synonym>";
	private final OWLGraphWrapper graph;
	private OWLOntology ont;

	public enum SynonymType {
		RELATED(0), EXACT(1), NARROW(2), BROAD(3), ALL(-1);

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

	public OntologyUtil(File ontologyFile) throws OWLOntologyCreationException {
		OWLOntologyManager inputOntologyManager = OWLManager.createOWLOntologyManager();
		ont = inputOntologyManager.loadOntologyFromOntologyDocument(ontologyFile);
		graph = new OWLGraphWrapper(ont);
	}

	public Set<OWLClass> getAncestors(OWLClass cls) {
		return graph.getAncestorsThroughIsA(cls);
	}

	public Set<OWLClass> getDescendents(OWLClass cls) {
		return graph.getDescendantsThroughIsA(cls);
	}

	public boolean isDescendent(OWLClass possibleChild, OWLClass possibleParent) {
		return getDescendents(possibleParent).contains(possibleChild);
	}

	/**
	 * @return an iterator of all classes in the ontology (does not include
	 *         obsolete classes).
	 */
	public Iterator<OWLClass> getClassIterator() {
		return graph.getAllOWLClasses().iterator();
	}

	public void close() throws IOException {
		graph.close();
	}

	public OWLClass getOWLClassFromId(String id) {
		return graph.getOWLClassByIdentifier(id);
	}

	public boolean isObsolete(OWLClass cls) {
		Set<OWLAnnotation> annotations = cls.getAnnotations(ont);
		for (OWLAnnotation annotation : annotations) {
			if (annotation.isDeprecatedIRIAnnotation()) {
				return true;
			}
		}
		return false;
	}

	public String getLabel(OWLClass cls) {
		Set<OWLAnnotation> annotations = cls.getAnnotations(ont);
		for (OWLAnnotation annotation : annotations) {
			if (annotation.getProperty().isLabel()) {
				String s = annotation.getValue().toString();
				s = StringUtils.removePrefix(s, "\"");
				s = StringUtils.removeSuffix(s, "\"^^xsd:string");
				return s;
			}
		}

		return null;
	}

	public Set<String> getSynonyms(OWLClass cls, SynonymType synType) {
		Set<String> synonyms = new HashSet<String>();
		Set<OWLAnnotation> annotations = cls.getAnnotations(ont);
		for (OWLAnnotation annotation : annotations) {
			String property = annotation.getProperty().toString();
			if ((synType == SynonymType.EXACT || synType == SynonymType.ALL)
					&& (property.equalsIgnoreCase(EXACT_SYN_PROP) || property.equalsIgnoreCase(EXACT_SYN_PROP_ALT))) {
				String s = annotation.getValue().toString();
				s = StringUtils.removePrefix(s, "\"");
				s = StringUtils.removeSuffix(s, "\"^^xsd:string");
				s = StringUtils.removePrefix(s, "\\\"");
				s = StringUtils.removeSuffix(s, "\\\" []");
				synonyms.add(s);
			} else if ((synType == SynonymType.RELATED || synType == SynonymType.ALL)
					&& (property.equalsIgnoreCase(RELATED_SYN_PROP) || property.equalsIgnoreCase(RELATED_SYN_PROP_ALT))) {
				String s = annotation.getValue().toString();
				s = StringUtils.removePrefix(s, "\"");
				s = StringUtils.removeSuffix(s, "\"^^xsd:string");
				s = StringUtils.removePrefix(s, "\\\"");
				s = StringUtils.removeSuffix(s, "\\\" []");
				synonyms.add(s);
			} else if ((synType == SynonymType.BROAD || synType == SynonymType.ALL)
					&& (property.equalsIgnoreCase(BROAD_SYN_PROP) || property.equalsIgnoreCase(BROAD_SYN_PROP_ALT))) {
				String s = annotation.getValue().toString();
				s = StringUtils.removePrefix(s, "\"");
				s = StringUtils.removeSuffix(s, "\"^^xsd:string");
				s = StringUtils.removePrefix(s, "\\\"");
				s = StringUtils.removeSuffix(s, "\\\" []");
				synonyms.add(s);
			} else if ((synType == SynonymType.NARROW || synType == SynonymType.ALL)
					&& (property.equalsIgnoreCase(NARROW_SYN_PROP) || property.equalsIgnoreCase(NARROW_SYN_PROP_ALT))) {
				String s = annotation.getValue().toString();
				s = StringUtils.removePrefix(s, "\"");
				s = StringUtils.removeSuffix(s, "\"^^xsd:string");
				s = StringUtils.removePrefix(s, "\\\"");
				s = StringUtils.removeSuffix(s, "\\\" []");
				synonyms.add(s);
			}

			if (property.contains("ynonym")
					&& !(property.equalsIgnoreCase(BROAD_SYN_PROP) || property.equalsIgnoreCase(BROAD_SYN_PROP_ALT)
							|| property.equalsIgnoreCase(EXACT_SYN_PROP) || property.equalsIgnoreCase(EXACT_SYN_PROP_ALT)
							|| property.equalsIgnoreCase(NARROW_SYN_PROP) || property.equalsIgnoreCase(NARROW_SYN_PROP_ALT)
							|| property.equalsIgnoreCase(RELATED_SYN_PROP) || property.equalsIgnoreCase(RELATED_SYN_PROP_ALT))) {
				logger.error("Unhandled synonym type: " + annotation.getProperty());
			}

			// System.out.println("PROP: " + annotation.getProperty());
			// System.out.println("VALUE: " + annotation.getValue());
		}
		return synonyms;

	}

	public String getNamespace(OWLClass cls) {
		Set<OWLAnnotation> annotations = cls.getAnnotations(ont);
		for (OWLAnnotation annotation : annotations) {
			if (annotation.getProperty().toString()
					.equalsIgnoreCase("<http://www.geneontology.org/formats/oboInOwl#hasOBONamespace>")) {
				String s = annotation.getValue().toString();
				s = StringUtils.removePrefix(s, "\"");
				s = StringUtils.removeSuffix(s, "\"^^xsd:string");
				return s;
			}
		}

		return null;
	}

}
