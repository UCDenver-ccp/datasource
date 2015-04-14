/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.rdfizer.rdf.filter.DuplicateStatementFilter;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SimpleDuplicateTripleFilter implements DuplicateStatementFilter {

	private Set<Statement> alreadyObservedStmts;

	public SimpleDuplicateTripleFilter() {
		this.alreadyObservedStmts = new HashSet<Statement>();
	}

	@Override
	public boolean alreadyObservedStatement(Statement stmt) {
		if (alreadyObservedStmts.contains(stmt)) {
			return true;
		}
		alreadyObservedStmts.add(stmt);
		return false;
	}

	@Override
	public boolean isLeakProof() {
		return true;
	}

	@Override
	public void shutdown() throws IOException {
		// do nothing
	}

	@Override
	public boolean alreadyObservedRecordUri(URIImpl recordUri) {
		return false;
	}

	@Override
	public void logRecordUri(URIImpl subRecordUri) {
		// do nothing
	}

}
