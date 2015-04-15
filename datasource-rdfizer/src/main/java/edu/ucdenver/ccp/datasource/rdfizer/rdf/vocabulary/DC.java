/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.datasource.rdfizer.rdf.RdfNamespace;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil;

/**
 * Enum representing Dublin Core concepts
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum DC {

	ABSTRACT("abstract"),
	CREATED("created"),
	CREATOR("creator"),
	CONTRIBUTOR("contributor"),
	DATE("date"),
	EXTENT("extent"),
	HAS_VERSION("hasVersion"),
	IDENTIFIER("identifier"),
	IS_PART_OF("isPartOf"),
	ISSUED("issued"),
	LANGUAGE("language"),
	MEDIUM("medium"),
	MODIFIED("modified"),
	PUBLISHED("published"),
	PUBLISHER("publisher"),
	RELATION("relation"),
	RIGHTS("rights"),
	SOURCE("source"),
	TITLE("title"),
	TYPE("type");

	private final String termName;

	private DC(String termName) {
		this.termName = termName;
	}

	public URI uri() {
		return new URIImpl(RdfUtil.createUri(RdfNamespace.DC, termName).toString());
	}

}
