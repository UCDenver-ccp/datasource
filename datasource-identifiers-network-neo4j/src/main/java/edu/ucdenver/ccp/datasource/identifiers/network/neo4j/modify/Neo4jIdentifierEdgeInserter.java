/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.BatchInserterIndex;
import org.neo4j.helpers.collection.MapUtil;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.reflection.PrivateAccessor;
import edu.ucdenver.ccp.datasource.identifiers.network.index.IndexOperation;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierEdge;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierEdgeInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNode;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkSettings;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index.Neo4jIdentifierNetworkIndexUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jIdentifierEdgeInserter extends Neo4jBatchInserter implements IdentifierEdgeInserter {

	private static final Logger logger = Logger.getLogger(Neo4jIdentifierEdgeInserter.class);

	private static final String ENUM_VALUE_OF_METHOD_NAME = "valueOf";
	private final Class<? extends RelationshipType> relationshipTypeEnumClass;

	/**
	 * @param networkSettings
	 */
	public Neo4jIdentifierEdgeInserter(Neo4jNetworkSettings networkSettings,
			Class<? extends RelationshipType> relationshipTypeEnumClass) {
		super(networkSettings);
		this.relationshipTypeEnumClass = relationshipTypeEnumClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.identifier.network.impl.neo4j.skos.Neo4jBatchInserter#
	 * getBatchInserterIndex()
	 */
	@Override
	protected BatchInserterIndex getBatchInserterIndex() {
		return Neo4jIdentifierNetworkIndexUtil.getBatchInserterEdgeIndex(getIndexProvider());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.datasource.identifier.network.skos.IdentifierEdgeInserter#insertEdge(edu
	 * .ucdenver.ccp.datasource.identifier.network.skos.IdentifierEdge)
	 */
	@Override
	public void insertEdge(IdentifierEdge edge) {
		logger.debug("Inserting edge: " + edge.toString());
		long fromNodeId = getNetworkNodeId(edge.getFromIdNode());
		long toNodeId = getNetworkNodeId(edge.getToIdNode());
		RelationshipType edgeType = null;
		try {
			edgeType = (RelationshipType) PrivateAccessor.invokeStaticPrivateMethod(relationshipTypeEnumClass,
					ENUM_VALUE_OF_METHOD_NAME, edge.getEdgeType().name());
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		Map<String, Object> storageProperties = null;
		Map<String, Object> indexProperties = MapUtil.map(
				Neo4jIdentifierNetworkIndexUtil.EDGE_INDEX_TYPE_PROPERTY_NAME, edgeType.name());
		long relationshipId = getInserter().createRelationship(fromNodeId, toNodeId, edgeType, storageProperties);
		getIndex().add(relationshipId, indexProperties);
	}

	/**
	 * Assumes that the input {@link IdentifierNode} maps to a single node in the network
	 * 
	 * @param fromIdNode
	 * @return the node id for the network node that maps to the input {@link IdentifierNode}
	 */
	private long getNetworkNodeId(IdentifierNode idNode) {
		Collection<Long> nodeIds = Neo4jIdentifierNetworkIndexUtil.queryForIdentifierNode(getIndexProvider(), idNode);
		logger.debug("# query hits for: " + idNode + " = " + nodeIds.size());
		if (nodeIds.isEmpty()) {
			return createNewIdentifierNode(idNode);
		}
		if (nodeIds.size() == 1)
			return CollectionsUtil.getSingleElement(nodeIds);
		throw new IllegalArgumentException("Input node maps to multiple network nodes. -- " + idNode.toString());
	}

	/**
	 * @param idNode
	 * @return
	 */
	private long createNewIdentifierNode(IdentifierNode idNode) {
		return Neo4jIdentifierNodeInserter.insertNewIdentifierNode(getInserter(), getIndexProvider(), idNode,
				IndexOperation.FLUSH);
	}

}
