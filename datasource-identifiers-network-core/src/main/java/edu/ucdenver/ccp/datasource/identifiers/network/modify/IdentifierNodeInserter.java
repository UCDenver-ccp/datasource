/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.modify;

import edu.ucdenver.ccp.datasource.identifiers.network.NetworkInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.index.IndexOperation;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public interface IdentifierNodeInserter extends NetworkInserter {

	/**
	 * Inserts the input {@link IdentifierNode} into the network. The network is searched for all
	 * identifiers in the input {@link IdentifierNode}. If no identifiers are found, a new node is
	 * created in the network. If one identifier is found then all identifiers in the input
	 * {@link IdentifierNode} are added to the existing network node. If multiple identifiers are
	 * found covering multiple nodes then the nodes are merged.
	 * 
	 * @param idNode
	 */
	public void insertIdentifierNode(IdentifierNode idNode, IndexOperation indexOperationAfterStorage);
}
