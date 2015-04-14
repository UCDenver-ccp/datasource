/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.rdfizer.rdf.RdfNamespace;
import edu.ucdenver.ccp.rdfizer.rdf.RdfUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum RDFS {

	LABEL("label"),
	SUBCLASS_OF("subClassOf"),
	COMMENT("comment");
	

	private final String termName;

	private RDFS(String termName) {
		this.termName = termName;
	}

	public URI uri() {
		return new URIImpl(RdfUtil.createUri(RdfNamespace.RDFS, termName).toString());
	}

}
