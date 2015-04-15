/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.datasource.rdfizer.rdf.RdfNamespace;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum RO {

	LOCATED_IN("located_in"),
	PART_OF("part_of"),
	HAS_PART("has_part"),
	HAS_PARTICIPANT("has_participant");

	private final String termName;

	private RO(String termName) {
		this.termName = termName;
	}

	public URI uri() {
		return new URIImpl(RdfUtil.createUri(RdfNamespace.RO, termName).toString());
	}

}
