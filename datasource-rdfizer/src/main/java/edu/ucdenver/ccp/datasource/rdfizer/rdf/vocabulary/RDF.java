/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum RDF {

	TYPE("type");

	private final String termName;

	private RDF(String termName) {
		this.termName = termName;
	}

	public URI uri() {
		return new URIImpl(RdfUtil.createUri(DataSource.RDF, termName).toString());
	}

}
