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
public enum RDFS {

	LABEL("label"),
	SUBCLASS_OF("subClassOf"),
	COMMENT("comment");
	

	private final String termName;

	private RDFS(String termName) {
		this.termName = termName;
	}

	public URI uri() {
		return new URIImpl(RdfUtil.createUri(DataSource.RDFS, termName).toString());
	}

}
