/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.modify;


/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class IdentifierEdge {

	private final IdentifierNode fromIdNode;
	private final IdentifierNode toIdNode;
	private final EdgeType edgeType;

	public IdentifierEdge(IdentifierNode fromIdNode, IdentifierNode toIdNode, EdgeType edgeType) {
		this.fromIdNode = fromIdNode;
		this.toIdNode = toIdNode;
		this.edgeType = edgeType;
	}

	/**
	 * @return the fromIdNode
	 */
	public IdentifierNode getFromIdNode() {
		return fromIdNode;
	}

	/**
	 * @return the toIdNode
	 */
	public IdentifierNode getToIdNode() {
		return toIdNode;
	}

	/**
	 * @return the edgeType
	 */
	public EdgeType getEdgeType() {
		return edgeType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IdentifierEdge [fromIdNode=" + fromIdNode + ", toIdNode=" + toIdNode + ", edgeType=" + edgeType + "]";
	}
	
	
	
}
