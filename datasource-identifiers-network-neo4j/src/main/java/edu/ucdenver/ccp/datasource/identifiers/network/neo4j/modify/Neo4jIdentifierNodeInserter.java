/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.index.BatchInserterIndex;
import org.neo4j.graphdb.index.BatchInserterIndexProvider;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.impl.batchinsert.BatchInserter;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.network.index.IndexOperation;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNode;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNodeInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkSettings;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index.Neo4jIdentifierNetworkIndexUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jIdentifierNodeInserter extends Neo4jBatchInserter implements IdentifierNodeInserter {

	private static final Logger logger = Logger.getLogger(Neo4jIdentifierNodeInserter.class);
	
	/**
	 * @param networkSettings
	 */
	public Neo4jIdentifierNodeInserter(Neo4jNetworkSettings networkSettings) {
		super(networkSettings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.identifier.network.impl.neo4j.skos.Neo4jNodeBatchInserter#
	 * getBatchInserterIndex()
	 */
	@Override
	protected BatchInserterIndex getBatchInserterIndex() {
		return Neo4jIdentifierNetworkIndexUtil.getBatchInserterNodeIndex(getIndexProvider());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.datasource.identifier.network.skos.IdentifierNodeInserter#insertIdentifierNode
	 * (edu.ucdenver.ccp.datasource.identifier.network.skos.IdentifierNode)
	 */
	@Override
	public void insertIdentifierNode(IdentifierNode idNode, IndexOperation indexOperationAfterInsertion) {
		Collection<Long> nodeIds = getNeo4jNodeIdsForIdNode(idNode);
		logger.debug("Inserting identifier node: " + idNode.toString() + " -- Node already present as: " + nodeIds.toString());
		if (nodeIds.isEmpty())
			insertNewIdentifierNode(getInserter(), getIndexProvider(), idNode, indexOperationAfterInsertion);
		else if (nodeIds.size() == 1)
			updateIdentifierNode(idNode, CollectionsUtil.getSingleElement(nodeIds), indexOperationAfterInsertion);
		else
			throw new UnsupportedOperationException(
					"Cannot merge nodes during batch insertion b/c we cannot delete nodes and edges at this time.");
		// mergeIdentifierNodes(idNode, nodeIds, indexOperationAfterInsertion);
	}

	// /**
	// * This method is to be used in cases where data source identifiers stored in the input
	// * {@link IdentifierNode} map to multiple nodes in the network. When this is the case, the
	// * multiple nodes should be merged into a single node and then merged with any other
	// identifiers
	// * in the {@link IdentifierNode}.
	// *
	// * By default the first nodeId encountered in the input nodeIdsToMerge collection is the node
	// * that is kept. The others are merged, then removed from the network.
	// *
	// * @param idNode
	// * @param nodeIds
	// * @param indexOperationAfterInsertion
	// */
	// private void mergeIdentifierNodes(IdentifierNode idNode, Collection<Long> nodeIdsToMerge,
	// IndexOperation indexOperationAfterInsertion) {
	// List<Long> nodeIds = new ArrayList<Long>(nodeIdsToMerge);
	// Long mergedNodeId = nodeIds.remove(0);
	// for (Long nodeId : nodeIds) {
	// mergeIdentifierNodeProperties(mergedNodeId, nodeId);
	// mergeNodeEdges(mergedNodeId, nodeId);
	// removeNode(nodeId);
	// }
	// updateIdentifierNode(idNode, mergedNodeId, indexOperationAfterInsertion);
	// }
	//
	// /**
	// * @param nodeId
	// */
	// private void removeNode(Long nodeId) {
	// getInserter().getGraphDbService().getNodeById(nodeId).delete();
	// }

	// /**
	// * This method iterates over all edges associated with the input nodeId and creates new edges
	// * with the same type and properties but with nodeId swapped out for the mergedNodeId
	// *
	// * @param mergedNodeId
	// * @param nodeId
	// */
	// private void mergeNodeEdges(Long mergedNodeId, Long nodeId) {
	// for (SimpleRelationship relationship : getInserter().getRelationships(nodeId)) {
	// long relationshipId = relationship.getId();
	// RelationshipType relationshipType = relationship.getType();
	// long startNodeId = relationship.getStartNode();
	// long endNodeId = relationship.getEndNode();
	// Map<String, Object> relationshipProperties =
	// getInserter().getRelationshipProperties(relationshipId);
	// if (startNodeId == nodeId)
	// getInserter().createRelationship(mergedNodeId, endNodeId, relationshipType,
	// relationshipProperties);
	// else
	// getInserter().createRelationship(startNodeId, mergedNodeId, relationshipType,
	// relationshipProperties);
	// getInserter().getGraphDbService().getRelationshipById(relationshipId).delete();
	// // TODO: Updated edges index?
	// }
	// }
	//
	// /**
	// * Merges the identifier properties from the nodeId to the mergedNodeId
	// *
	// * @param mergedNodeId
	// * target node
	// * @param nodeId
	// * source node
	// */
	// private void mergeIdentifierNodeProperties(Long mergedNodeId, Long nodeId) {
	// Map<String, Object> updatedProperties = new HashMap<String,
	// Object>(getInserter().getNodeProperties(
	// mergedNodeId));
	// Map<String, Object> propertiesToMerge = new HashMap<String,
	// Object>(getInserter().getNodeProperties(nodeId));
	// for (Entry<String, Object> entry : propertiesToMerge.entrySet()) {
	// if (updatedProperties.containsKey(entry.getKey())) {
	// String idType = entry.getKey();
	// Set<String> updatedIds = getIds(updatedProperties, DataSource.valueOf(idType));
	// Set<String> idsToMerge = getIds(propertiesToMerge, DataSource.valueOf(idType));
	// updatedIds.addAll(idsToMerge);
	// String idStorageStr = CollectionsUtil.createDelimitedString(updatedIds,
	// Neo4jBatchInserter.ID_STORAGE_DELIMITER);
	// updatedProperties.put(idType, idStorageStr);
	// } else
	// updatedProperties.put(entry.getKey(), entry.getValue());
	// }
	// }

	/**
	 * This method is to be used in cases when at least one of the data source identifiers stored in
	 * the input {@link IdentifierNode} are also part of one other node in the network. This is a
	 * simple case of them merge operation where only the missing identifiers in the input
	 * {@link IdentifierNode} need to be added to a pre-existing network node. For example, if the
	 * {@link IdentifierNode} represents ids A and B and there is already a node in the network that
	 * represents A, then B simply needs to be added to that pre-existing network node.
	 * 
	 * @param idNode
	 * @param singleElement
	 * @param indexOperationAfterInsertion
	 */
	private void updateIdentifierNode(IdentifierNode idNode, Long existingNodeId,
			IndexOperation indexOperationAfterInsertion) {
		Map<String, Object> updatedProperties = new HashMap<String, Object>(getInserter().getNodeProperties(
				existingNodeId));
		for (Entry<DataSource, Set<DataSourceIdentifier<?>>> entry : idNode.getIds().entrySet()) {
			DataSource idType = entry.getKey();
			if (updatedProperties.containsKey(idType.name())) {
				Set<String> alreadyStoredIds = getIds(updatedProperties, idType);
				Set<String> ids = addIdsFromGeneOrGeneProduct(idNode, idType, alreadyStoredIds);
				String idStr = CollectionsUtil.createDelimitedString(ids, Neo4jIdentifierNodeUtil.ID_STORAGE_DELIMITER);
				updatedProperties.put(idType.name(), idStr);
				for (DataSourceIdentifier<?> id : idNode.getIds().get(idType)) {
					if (!alreadyStoredIds.contains(id.toString()))
						getIndex().add(existingNodeId, MapUtil.map(idType.name(), indexReadyIdStr(id)));
				}
			} else {
				Set<String> ids = new HashSet<String>(CollectionsUtil.toString(idNode.getIds().get(idType)));
				String idStr = CollectionsUtil.createDelimitedString(ids, Neo4jIdentifierNodeUtil.ID_STORAGE_DELIMITER);
				updatedProperties.put(idType.name(), idStr);
				for (DataSourceIdentifier<?> id : idNode.getIds().get(idType)) {
					getIndex().add(existingNodeId, MapUtil.map(idType.name(), indexReadyIdStr(id)));
				}
			}
		}
		getInserter().setNodeProperties(existingNodeId, updatedProperties);
		if (indexOperationAfterInsertion.equals(IndexOperation.FLUSH))
			getIndex().flush();
	}

	/**
	 * Returns a {@link Set} containing unique identifiers stored in the input properties
	 * {@link Map}. Expected format of the input properties {@link Map} values is a space-delimited
	 * {@link String} containing the IDs for the {@link DataSource} key.
	 * 
	 * @param propertiesMap
	 * @param idSource
	 * @return Returns a {@link Set} containing unique identifiers stored in the input properties
	 *         {@link Map} that match the input {@link DataSource} key
	 */
	private static Set<String> getIds(Map<String, Object> propertiesMap, DataSource idSource) {
		String idStr = (String) propertiesMap.get(idSource.name());
		return new HashSet<String>(Arrays.asList(idStr.split(Neo4jIdentifierNodeUtil.ID_STORAGE_DELIMITER)));
	}

	/**
	 * For a given {@link DataSource} this method adds any IDs from that particular
	 * {@link DataSource} to the input {@link Set} of IDs and returns the union.
	 * 
	 * @param ggp
	 * @param idSource
	 * @param ids
	 * @return a {@link Set} containing the union of the contents of the input {@link Set} and IDs
	 *         from the input {@link GeneOrGeneProduct} of the specified {@link DataSource}
	 */
	private static Set<String> addIdsFromGeneOrGeneProduct(IdentifierNode idNode, DataSource idSource, Set<String> ids) {
		Set<String> idsToReturn = new HashSet<String>(ids);
		for (DataSourceIdentifier<?> id : idNode.getIds().get(idSource))
			idsToReturn.add(id.toString());
		return idsToReturn;
	}

	/**
	 * This method inserts a new node into the network to represent the input {@link IdentifierNode}
	 * This method assumes that the none of the data source identifiers stored in the
	 * {@link IdentifierNode} are currently represented in the network.
	 * 
	 * @param idNode
	 * @param indexOperationAfterInsertion
	 *            if {@link IndexOperation#FLUSH} then the index is flushed after insertion
	 */
	public static long insertNewIdentifierNode(BatchInserter inserter, BatchInserterIndexProvider indexProvider,
			IdentifierNode idNode, IndexOperation indexOperationAfterInsertion) {
		BatchInserterIndex index = Neo4jIdentifierNetworkIndexUtil.getBatchInserterNodeIndex(indexProvider);
		Map<String, Object> propertiesMap = Neo4jIdentifierNodeUtil.getNodeProperties(idNode);
		long nodeID = inserter.createNode(propertiesMap);

		/* index the node by id type */
		Map<String, Object> idTypeMap = MapUtil.map(Neo4jIdentifierNetworkIndexUtil.NODE_INDEX_TYPE_PROPERTY_NAME,
				idNode.getIdType());
		index.add(nodeID, idTypeMap);

		/* index the node by datasource identifier */
		for (Entry<DataSource, Set<DataSourceIdentifier<?>>> entry : idNode.getIds().entrySet()) {
			for (DataSourceIdentifier<?> id : entry.getValue()) {
				Map<String, Object> idMap = MapUtil.map(entry.getKey().name(), indexReadyIdStr(id));
				index.add(nodeID, idMap);
			}
		}
		if (indexOperationAfterInsertion.equals(IndexOperation.FLUSH)) {
			index.flush();
		}
		return nodeID;
	}

	/**
	 * Replaces all colons with underscores. Colon is a reserved character in the Lucene query
	 * syntax and replacing it just makes things easier.
	 * 
	 * @param id
	 * @return
	 */
	private static String indexReadyIdStr(DataSourceIdentifier<?> id) {
		String idStr = id.toString();
		return idStr.replaceAll(":", "_");
	}

	/**
	 * @param idNode
	 * @return a collection of Neo4j identifiers representing nodes that match data source
	 *         identifiers of the input {@link IdentifierNode}
	 */
	private Collection<Long> getNeo4jNodeIdsForIdNode(IdentifierNode idNode) {
		return Neo4jIdentifierNetworkIndexUtil.queryForIdentifierNode(getIndexProvider(), idNode);
	}

}
