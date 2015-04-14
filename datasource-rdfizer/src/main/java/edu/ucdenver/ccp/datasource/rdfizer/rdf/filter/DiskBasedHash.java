/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf.filter;

import java.io.IOException;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public interface DiskBasedHash {

	
	public void add(Object o) throws IOException;
	
	public boolean contains(Object o);
	
	public void shutdown() throws IOException;
}
