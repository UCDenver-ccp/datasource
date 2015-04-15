/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.filter;

import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class NoOpDuplicateStatementFilter implements DuplicateStatementFilter {

//	@Override
//	public boolean alreadyObservedFieldUri(URIImpl uri) {
//		return false;
//	}

	@Override
	public boolean alreadyObservedRecordUri(URIImpl uri) {
		return false;
	}

	@Override
	public boolean alreadyObservedStatement(Statement stmt) {
		return false;
	}

	@Override
	public boolean isLeakProof() {
		return false;
	}

	@Override
	public void shutdown() {
		// do nothing
	}

	@Override
	public void logRecordUri(URIImpl subRecordUri) {
		// do nothing
	}


}
