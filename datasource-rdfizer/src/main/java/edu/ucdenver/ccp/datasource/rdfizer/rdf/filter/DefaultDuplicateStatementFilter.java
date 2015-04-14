/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf.filter;

import java.io.File;
import java.io.IOException;

/**
 * Uses the JDBMS cache to store previously observed field values on disk
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class DefaultDuplicateStatementFilter extends DuplicateFieldValueFilter {

	/**
	 * @param cacheFilePrefix
	 *            the file prefix where the JDBMS will catch information
	 * 
	 * @throws IOException
	 */
	public DefaultDuplicateStatementFilter(File cacheFilePrefix) throws IOException {
		super(new Jdbm2Cache(cacheFilePrefix));

	}

	/**
	 * The underlying implementation for this filter is disk-based, so it should always be
	 * leak-proof.
	 */
	@Override
	public boolean isLeakProof() {
		return true;
	}

}
