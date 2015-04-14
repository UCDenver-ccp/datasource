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
public enum IAO {

	MENTIONS("mentions"),
	//DENOTES("denotes");
	DENOTES("IAO_0000219"),
	INFORMATION_CONTENT_ENITITY("IAO_0000030");

	private final String termName;

	private IAO(String termName) {
		this.termName = termName;
	}

	public URI uri() {
		return new URIImpl(RdfUtil.createUri(RdfNamespace.IAO, termName).toString());
	}

}
