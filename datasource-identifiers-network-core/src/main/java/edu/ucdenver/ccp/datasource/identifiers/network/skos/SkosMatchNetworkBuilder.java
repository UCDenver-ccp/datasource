/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.skos;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.network.NetworkSettings;
import edu.ucdenver.ccp.datasource.identifiers.network.index.IndexOperation;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.EdgeType;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierEdge;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierEdgeInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNode;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNodeInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNodeMerger;

/**
 * We use the skos:exactMatch and skos:relatedMatch properties to link data source identifiers, as a
 * first step towards building bio-world. This class consumes XMatch triples, combining concepts
 * related by skos:exactMatch into a single node and retaining the skos:relatedMatch relations.
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SkosMatchNetworkBuilder {

	public enum SkosMatchEdgeType implements EdgeType {
		SKOS_EXACT_MATCH,
		SKOS_CLOSE_MATCH,
		SKOS_RELATED_MATCH
	}

	private final NetworkSettings networkSettings;
	private final IdentifierNodeInserter idNodeInserter;
	private final IdentifierEdgeInserter idEdgeInserter;
	private final IdentifierNodeMerger idNodeMerger;

	/**
	 * @param networkSettings
	 */
	public SkosMatchNetworkBuilder(NetworkSettings networkSettings, IdentifierNodeInserter idNodeInserter,
			IdentifierEdgeInserter idEdgeInserter, IdentifierNodeMerger idNodeMerger) {
		this.networkSettings = networkSettings;
		this.idNodeInserter = idNodeInserter;
		this.idEdgeInserter = idEdgeInserter;
		this.idNodeMerger = idNodeMerger;

	}

	public void close() {
		idNodeInserter.shutdown();
		idEdgeInserter.shutdown();
		idNodeMerger.shutdown();
	}

	/**
	 * Inserts the specified {@link DataSourceIdentifier} into the network as an individual new node
	 * 
	 * @param id
	 * @param idType
	 * @param indexOperationAfterInsert
	 */
	public void insertIdentifierNode(DataSourceIdentifier<?> id, String idType, IndexOperation indexOperationAfterInsert) {
		idEdgeInserter.shutdown();
		idNodeMerger.shutdown();
		idNodeInserter.activate();
		this.idNodeInserter.insertIdentifierNode(new IdentifierNode(id, idType), indexOperationAfterInsert);
	}

	/**
	 * Treats the input {@link DataSourceIdentifier} objects as if they are related with the
	 * skos:exactMatch predicate. If nodes already exist for both identifiers then the nodes are
	 * merged. If a node exists for only one of the identifiers then the other is added to that
	 * node. If neither identifier already has a node in the network then a new node is created.
	 * 
	 * @param id1
	 * @param id2
	 * @param idType
	 */
	public void insertExactMatch(DataSourceIdentifier<?> id1, DataSourceIdentifier<?> id2, String idType) {
		insertIdentifierEdge(id1, id2, SkosMatchEdgeType.SKOS_EXACT_MATCH, idType);
	}

	// /**
	// * The two input identifiers are considered a "close match". If nodes do not exist in the
	// * network for either of the two identifiers then nodes are generated. The two nodes are
	// * connected with an edge of type {@link SkosMatchEdgeType#SKOS_CLOSE_MATCH}.
	// *
	// * @param fromId
	// * @param toId
	// * @param idType
	// * @throws IllegalArgumentException
	// * if the two input identifiers reference the same network node and an edge cannot
	// * be created
	// */
	// public void insertCloseMatch(DataSourceIdentifier<?> fromId, DataSourceIdentifier<?> toId)
	// throws IllegalArgumentException {
	// insertIdentifierEdge(fromId, toId, SkosMatchEdgeType.SKOS_CLOSE_MATCH);
	// }
	//
	// /**
	// * The two input identifiers are considered a "related match". If nodes do not exist in the
	// * network for either of the two identifiers then nodes are generated. The two nodes are
	// * connected with an edge of type {@link SkosMatchEdgeType#SKOS_RELATED_MATCH}.
	// *
	// * @param fromId
	// * @param toId
	// * @param idType
	// * @throws IllegalArgumentException
	// * if the two input identifiers reference the same network node and an edge cannot
	// * be created
	// */
	// public void insertRelatedMatch(DataSourceIdentifier<?> fromId, DataSourceIdentifier<?> toId)
	// throws IllegalArgumentException {
	// insertIdentifierEdge(fromId, toId, SkosMatchEdgeType.SKOS_RELATED_MATCH);
	// }

	/**
	 * Creates a {@link IdentifierEdge} with the specified {@link SkosMatchEdgeType} and inserts it
	 * into the network
	 * 
	 * @param fromId
	 * @param toId
	 * @param edgeType
	 */
	private void insertIdentifierEdge(DataSourceIdentifier<?> fromId, DataSourceIdentifier<?> toId,
			SkosMatchEdgeType edgeType, String idType) {
		idNodeInserter.shutdown();
		idNodeMerger.shutdown();
		idEdgeInserter.activate();
		IdentifierNode fromIdNode = new IdentifierNode(fromId, idType);
		IdentifierNode toIdNode = new IdentifierNode(toId, idType);
		IdentifierEdge edge = new IdentifierEdge(fromIdNode, toIdNode, edgeType);
		this.idEdgeInserter.insertEdge(edge);
	}

	/**
	 * When this method is invoked, all nodes connected by SKOS_EXACT_MATCH relationships are merged
	 * into a single node
	 */
	public void mergeExactMatches() {
		idNodeInserter.shutdown();
		idEdgeInserter.shutdown();
		idNodeMerger.activate();
		idNodeMerger.mergeNodesConnectedBy(SkosMatchEdgeType.SKOS_EXACT_MATCH);
		idNodeMerger.shutdown();
	}
}
