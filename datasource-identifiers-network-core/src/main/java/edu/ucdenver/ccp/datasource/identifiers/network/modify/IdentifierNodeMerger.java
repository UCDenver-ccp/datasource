/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.modify;

import edu.ucdenver.ccp.datasource.identifiers.network.NetworkInserter;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public interface IdentifierNodeMerger extends NetworkInserter {

	/**
	 * @param skosExactMatch
	 */
	public void mergeNodesConnectedBy(EdgeType edgeType);

	
}
