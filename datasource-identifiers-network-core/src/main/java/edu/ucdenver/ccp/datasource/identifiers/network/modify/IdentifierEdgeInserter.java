/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.modify;

import edu.ucdenver.ccp.datasource.identifiers.network.NetworkInserter;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public interface IdentifierEdgeInserter extends NetworkInserter {

	/**
	 * @param edge
	 */
	public void insertEdge(IdentifierEdge edge);

}
