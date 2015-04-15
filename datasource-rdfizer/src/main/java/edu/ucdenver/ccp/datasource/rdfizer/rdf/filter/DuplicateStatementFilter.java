/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.filter;

import java.io.IOException;

import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public interface DuplicateStatementFilter {

	/**
	 * @param stmt
	 * @return
	 */
//	public boolean alreadyObservedFieldUri(URIImpl fieldUri);

	public boolean alreadyObservedRecordUri(URIImpl recordUri);
	
	public boolean alreadyObservedStatement(Statement stmt);

	/**
	 * @return true if the filter is 100% sure no duplicate triples leaked through, false otherwise
	 */
	public boolean isLeakProof();

	public void shutdown() throws IOException;

	/**
	 * @param subRecordUri
	 * @throws  
	 */
	public void logRecordUri(URIImpl subRecordUri);

}
