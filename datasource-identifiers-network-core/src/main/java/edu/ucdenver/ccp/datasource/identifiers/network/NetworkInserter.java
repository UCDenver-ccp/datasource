/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public interface NetworkInserter {
	
	public boolean isActive();
	
	public void activate();
	
	public void shutdown();
	
//	public long getNodeCount();
	
//public void flushNodeIndex();
}
