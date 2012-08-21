/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.EdgeType;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNodeMerger;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkSettings;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index.Neo4jIdentifierNetworkIndexUtil;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.skos.Neo4jSkosMatchRelationshipType;
import edu.ucdenver.ccp.wrapper.neo4j.Neo4jUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jIdentifierNodeMerger extends Neo4jNetworkModifier implements IdentifierNodeMerger {

	private static final Logger logger = Logger.getLogger(Neo4jIdentifierNodeMerger.class);
	// private Map<Long, Long> nodeIdToMergedNodeIdMap;

	/**
	 * The number of merges that should be included in each Neo4j transaction
	 */
	private static final int TRANSACTION_LIMIT = 1000;

	private Set<Long> alreadyMergedNodeIds;

	/**
	 * @param networkSettings
	 */
	public Neo4jIdentifierNodeMerger(Neo4jNetworkSettings networkSettings) {
		super(networkSettings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.datasource.identifier.network.skos.IdentifierNodeMerger#mergeNodesConnectedBy
	 * (edu.ucdenver.ccp.datasource.identifier.network.edge.EdgeType)
	 */
	@Override
	public void mergeNodesConnectedBy(EdgeType edgeType) {
		// nodeIdToMergedNodeIdMap = new HashMap<Long, Long>();
		alreadyMergedNodeIds = new HashSet<Long>();
		Transaction tx = getGraphDb().beginTx();
		try {
			/*
			 * Checks each node to see if it has a relationship of the specified type. Nodes that
			 * have already been merged are skipped. TODO: This method should accept any edge type
			 * and not be constrained to skos edge types as it is now.
			 */
			int count = 0;
			for (Node node : GlobalGraphOperations.at(getGraphDb()).getAllNodes()) {
				if (count++ % 1000 == 0) {
					logger.info("Merging nodes progress: " + (count - 1));
				}
				if (!alreadyMergedNodeIds.contains(node.getId())) {
					if (count % TRANSACTION_LIMIT == 0) {
						tx.success();
						tx.finish();
						tx = getGraphDb().beginTx();
					}
					logger.debug("Starting new merge group for node: " + node.getId() + " -- "
							+ Neo4jUtil.getProperties(node));
					mergeIdentifierNodes(node, CollectionsUtil.createSet(node.getId()),
							Neo4jSkosMatchRelationshipType.valueOf(edgeType.name()));
				}
			}
			tx.success();
		} finally {
			logger.info("Merging of nodes connected by relationship type: " + edgeType.name() + " -- COMPLETE.");
			tx.finish();
		}

	}

	/**
	 * This recursive method essentially does a depth-first traversal over the relationships of the
	 * specified {@link RelationshipType}. It returns when it reaches the end of a series of
	 * relationships, or if the only paths left form a cycle. The traversal path is tracked using
	 * the alreadyTraversedNodeIds set. Nodes are merged starting from the end of the chain of
	 * relationships and working towards the initial node used in the first call to
	 * mergeIdentifierNodes.
	 * 
	 * @param node
	 * @param alreadyTraversedNodeIds
	 * @param relationshipType
	 */
	private void mergeIdentifierNodes(Node node, Set<Long> alreadyTraversedNodeIds, RelationshipType relationshipType) {
		logger.debug("Node " + node.getId() + " -- " + Neo4jUtil.getProperties(node) + " has "
				+ CollectionsUtil.createList(node.getRelationships(relationshipType).iterator()).size()
				+ " relationships of type " + relationshipType.name());
		logger.debug("Already traversed nodes = " + alreadyTraversedNodeIds.toString());
		for (Relationship relationship : node.getRelationships(relationshipType)) {
			Node otherNode = relationship.getOtherNode(node);
			logger.debug("Following relationship from node " + node.getId() + " to node: " + otherNode.getId());
			if (alreadyTraversedNodeIds.contains(otherNode.getId())) {
				logger.debug("Already visited node " + otherNode.getId() + " ... continuing merge traversal.");
				continue;
			}
			alreadyTraversedNodeIds.add(otherNode.getId());
			mergeIdentifierNodes(otherNode, alreadyTraversedNodeIds, relationshipType);
			mergeIdentifierNodes(node, otherNode);
			relationship.delete();
			alreadyMergedNodeIds.add(otherNode.getId());
			logger.debug("Deleting node: " + otherNode.getId());
			Neo4jIdentifierNetworkIndexUtil.getIdNodeIndex(getGraphDb()).remove(otherNode);
			otherNode.delete();
		}
	}

	/**
	 * @param startNode
	 * @param endNode
	 */
	private void mergeIdentifierNodes(Node node1, Node node2) {
		/*
		 * It's possible at this point that the nodes connected by skos:exactMatch edges have been
		 * collapsed already, so here we check the nodeIdToMergedNodeIdMap to see if either of the
		 * input nodes have a new mapping (i.e. they've been merged and since deleted from the
		 * graph).
		 */
		Node nodeToMerge1 = node1;
		Node nodeToMerge2 = node2;
		// if (nodeIdToMergedNodeIdMap.containsKey(node1.getId())) {
		// nodeToMerge1 = getGraphDb().getNodeById(nodeIdToMergedNodeIdMap.get(node1.getId()));
		// }
		// if (nodeIdToMergedNodeIdMap.containsKey(node2.getId())) {
		// nodeToMerge2 = getGraphDb().getNodeById(nodeIdToMergedNodeIdMap.get(node2.getId()));
		// }
		mergeIdentifierNodeProperties(nodeToMerge1, nodeToMerge2);
		mergeNodeEdges(nodeToMerge1, nodeToMerge2);
	}

	/**
	 * Merges the identifier properties from the nodeId to the mergedNodeId
	 * 
	 * @param mergedNodeId
	 *            target node
	 * @param nodeId
	 *            source node
	 */
	private void mergeIdentifierNodeProperties(Node node1, Node node2) {
		Map<String, Object> updatedProperties = Neo4jUtil.getProperties(node1);
		Map<String, Object> propertiesToMerge = Neo4jUtil.getProperties(node2);
		logger.debug("Merging node: " + node2.getId() + " into " + node1.getId() + "; " + node1.getId()
				+ " Properties = " + updatedProperties.toString() + " -- " + node2.getId() + " Properties = "
				+ propertiesToMerge.toString());
		for (Entry<String, Object> entry : propertiesToMerge.entrySet()) {
			if (entry.getKey().equals(Neo4jIdentifierNodeUtil.ID_TYPE_KEY)) {
				String idType1 = entry.getValue().toString();
				String idType2 = updatedProperties.get(Neo4jIdentifierNodeUtil.ID_TYPE_KEY).toString();
				if (!idType1.equals(idType2)) {
					logger.warn("Merging identifier nodes with different ID types: Node1: "
							+ updatedProperties.toString() + " Node2: " + propertiesToMerge.toString());
				}
			} else {
				if (updatedProperties.containsKey(entry.getKey())) {
					String idType = entry.getKey();
					Set<String> updatedIds = getIds(updatedProperties, DataSource.valueOf(idType));
					Set<String> idsToMerge = getIds(propertiesToMerge, DataSource.valueOf(idType));
					// /* updated the node index */
					// Set<String> idsToIndex = new HashSet<String>(idsToMerge);
					// idsToIndex.removeAll(updatedIds);
					// for (String idToIndex : idsToIndex) {
					// Neo4jIdentifierNetworkIndexUtil.indexIdNode(getGraphDb(), node1, idType,
					// idToIndex);
					// }
					updatedIds.addAll(idsToMerge);
					String idStorageStr = CollectionsUtil.createDelimitedString(updatedIds,
							Neo4jIdentifierNodeUtil.ID_STORAGE_DELIMITER);
					updatedProperties.put(idType, idStorageStr);
				} else {
					updatedProperties.put(entry.getKey(), entry.getValue());
					// /* updated the node index */
					// for (String idToIndex : entry.getValue().toString()
					// .split(Neo4jIdentifierNodeUtil.ID_STORAGE_DELIMITER_REGEX)) {
					// Neo4jIdentifierNetworkIndexUtil.indexIdNode(getGraphDb(), node1,
					// entry.getKey(), idToIndex);
					// }
				}
			}
		}
		logger.debug("Setting " + node1 + " properties to MERGED PROPERTIES: " + updatedProperties.toString());

		for (Entry<String, Object> entry : updatedProperties.entrySet()) {
			Neo4jIdentifierNetworkIndexUtil.indexIdNode(getGraphDb(), node1, entry.getKey(), entry.getValue()
					.toString());
		}

		Neo4jUtil.setProperties(node1, updatedProperties);
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
		return new HashSet<String>(Arrays.asList(idStr.split(ID_STORAGE_DELIMITER)));
	}

	/**
	 * This method iterates over all edges associated with the input {@link Node} and creates new
	 * edges with the same type and properties but with node2 swapped out for node1
	 * 
	 * Note that edge types should be mutually exclusive. If there are multiple edges between nodes
	 * being merged, then information will be lost during the merge. This information loss happens
	 * when a second edge between node1 and node2 is deleted but no reference to the edge is made on
	 * the merged node.
	 * 
	 * @param mergedNodeId
	 * @param nodeId
	 */
	private void mergeNodeEdges(Node node1, Node node2) {
		logger.debug("Migrating relationships from node: " + node2.getId() + " to node: " + node1.getId());
		for (Relationship relationship : node2.getRelationships()) {
			Node otherNode = relationship.getOtherNode(node2);
			if (otherNode.getId() != node1.getId()) {
				Map<String, Object> relationshipProperties = Neo4jUtil.getProperties(relationship);
				RelationshipType type = relationship.getType();
				if (!relationshipAlreadyExists(type, node1, otherNode, relationshipProperties)) {
					logger.debug("Creating relationship (" + type.name() + ") from node: " + node1.getId()
							+ " to node " + otherNode.getId());
					Relationship r = node1.createRelationshipTo(otherNode, type);
					Neo4jUtil.setProperties(r, relationshipProperties);
					Neo4jIdentifierNetworkIndexUtil.indexEdgeByType(getGraphDb(), r);
				}
				logger.debug("Deleting relationship (" + type.name() + ") from " + relationship.getStartNode().getId()
						+ " to " + relationship.getEndNode().getId());
				relationship.delete();
			}
		}
	}

	/**
	 * @param type
	 * @param node
	 * @param otherNode
	 * @param expectedProperties
	 * @return true if the exact relationship already exists, false otherwise
	 */
	private boolean relationshipAlreadyExists(RelationshipType type, Node node, Node otherNode,
			Map<String, Object> expectedProperties) {
		for (Relationship r : node.getRelationships(type)) {
			/* relationship nodes must be the same */
			if (r.getOtherNode(node).getId() == otherNode.getId()) {
				Map<String, Object> relationshipProperties = Neo4jUtil.getProperties(r);
				/* relationship properties must be the same */
				for (Entry<String, Object> entry : relationshipProperties.entrySet()) {
					if (expectedProperties.containsKey(entry.getKey())) {
						Object expectedValue = expectedProperties.get(entry.getKey());
						if (!entry.getValue().equals(expectedValue)) {
							return false;
						}
					} else {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
