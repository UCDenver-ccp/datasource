/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.search.Query;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.BatchInserterIndex;
import org.neo4j.graphdb.index.BatchInserterIndexProvider;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.RelationshipIndex;
import org.neo4j.index.impl.lucene.LuceneBatchInserterIndexProvider;
import org.neo4j.kernel.impl.batchinsert.BatchInserter;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.network.index.DatasourceIdentifierNetworkIndex;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.EdgeType;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNode;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkSettings;
import edu.ucdenver.ccp.wrapper.neo4j.index.Neo4jIndexUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jIdentifierNetworkIndexUtil {
	
	private static final Logger logger = Logger.getLogger(Neo4jIdentifierNetworkIndexUtil.class);

	public static final String EDGE_INDEX_TYPE_PROPERTY_NAME = "type";
	
	public static final String NODE_INDEX_TYPE_PROPERTY_NAME = "idType";

	public static final Map<String, String> IDENTIFIER_NODE_INDEX_PROPERTIES = Neo4jIndexUtil
			.getLuceneIndexPropertiesMap(Neo4jIndexUtil.IndexType.FULLTEXT, IdIndexAnalyzer.class);

	public static final Map<String, String> IDENTIFIER_EDGE_INDEX_PROPERTIES = Neo4jIndexUtil
			.getLuceneIndexPropertiesMap(Neo4jIndexUtil.IndexType.EXACT, IdIndexAnalyzer.class);

	/**
	 * Returns the batch-loading index used to catalog data source identifier nodes in the network
	 * 
	 * @param indexProvider
	 * @return the batch-loading index used to catalog data source identifier nodes in the network
	 */
	public static BatchInserterIndex getBatchInserterNodeIndex(BatchInserterIndexProvider indexProvider) {
		return indexProvider.nodeIndex(DatasourceIdentifierNetworkIndex.IDENTIFIER_NODE.name(),
				IDENTIFIER_NODE_INDEX_PROPERTIES);
	}

	/**
	 * 
	 * @param networkSettings
	 *            used to indicate where the network lives on disk
	 * @return the batch-loading index used to catalog data source identifier nodes in the network
	 */
	public static BatchInserterIndex getBatchInserterNodeIndex(Neo4jNetworkSettings networkSettings) {
		BatchInserter inserter = NetworkIndexUtil.initializeBatchInserter(networkSettings);
		BatchInserterIndexProvider indexProvider = new LuceneBatchInserterIndexProvider(inserter);
		return getBatchInserterNodeIndex(indexProvider);
	}

	/**
	 * Searches the input {@link BatchInserterIndex} for all identifier values in the input
	 * {@link IdentifierNode}.
	 * 
	 * @param idNodeIndex
	 * @param idNode
	 * @return a collection of Neo4j node identifiers for nodes that share IDs with the input
	 *         {@link IdentifierNode}
	 */
	public static Collection<Long> queryForIdentifierNode(BatchInserterIndexProvider indexProvider,
			IdentifierNode idNode) {
		BatchInserterIndex idNodeIndex = getBatchInserterNodeIndex(indexProvider);
		Collection<Long> nodeIdHits = new ArrayList<Long>();
		for (Entry<DataSource, Set<DataSourceIdentifier<?>>> entry : idNode.getIds().entrySet()) {
			DataSource idSource = entry.getKey();
			for (DataSourceIdentifier<?> id : entry.getValue()) {
				IndexHits<Long> hits = idNodeIndex.query(NetworkQueryUtil.createIdentifierIndexQuery(idSource.name(),
						id.getDataElement().toString()));
				nodeIdHits.addAll(CollectionsUtil.createList(hits.iterator()));
			}
		}
		return nodeIdHits;
	}

	/**
	 * Returns the batch-loading index used to catalog data source identifier nodes in the network
	 * 
	 * @param indexProvider
	 * @return the batch-loading index used to catalog data source identifier nodes in the network
	 */
	public static BatchInserterIndex getBatchInserterEdgeIndex(BatchInserterIndexProvider indexProvider) {
		return indexProvider.relationshipIndex(DatasourceIdentifierNetworkIndex.IDENTIFIER_EDGE.name(),
				IDENTIFIER_EDGE_INDEX_PROPERTIES);
	}

	/**
	 * 
	 * @param networkSettings
	 *            used to indicate where the network lives on disk
	 * @return the batch-loading index used to catalog data source identifier nodes in the network
	 */
	public static BatchInserterIndex getBatchInserterEdgeIndex(Neo4jNetworkSettings networkSettings) {
		BatchInserter inserter = NetworkIndexUtil.initializeBatchInserter(networkSettings);
		BatchInserterIndexProvider indexProvider = new LuceneBatchInserterIndexProvider(inserter);
		return getBatchInserterEdgeIndex(indexProvider);
	}

	/**
	 * @param graphDb
	 * @return
	 */
	public static Index<Node> getIdNodeIndex(GraphDatabaseService graphDb) {
		return graphDb.index().forNodes(DatasourceIdentifierNetworkIndex.IDENTIFIER_NODE.name(),
				IDENTIFIER_NODE_INDEX_PROPERTIES);
	}

	/**
	 * @param idNodeIndex
	 * @param id
	 * @return
	 */
	public static IndexHits<Node> queryIdNodeIndex(Index<Node> idNodeIndex, DataSourceIdentifier<?> id) {
		Query query = NetworkQueryUtil.createIdentifierIndexQuery(id.getDataSource().name(), id.getDataElement()
				.toString());
		return idNodeIndex.query(query);
	}

	/**
	 * @param graphDb
	 * @return
	 */
	public static RelationshipIndex getIdEdgeIndex(GraphDatabaseService graphDb) {
		return graphDb.index().forRelationships(DatasourceIdentifierNetworkIndex.IDENTIFIER_EDGE.name(),
				IDENTIFIER_EDGE_INDEX_PROPERTIES);
	}

	public static void indexEdgeByType(GraphDatabaseService graphDb, Relationship r) {
		getIdEdgeIndex(graphDb).add(r, EDGE_INDEX_TYPE_PROPERTY_NAME, r.getType().name());
	}

	public static IndexHits<Relationship> queryIdEdgeIndexByType(GraphDatabaseService graphDb, EdgeType edgeType) {
		Query query = NetworkQueryUtil.createIdentifierIndexQuery(EDGE_INDEX_TYPE_PROPERTY_NAME, edgeType.name());
		return getIdEdgeIndex(graphDb).query(query);
	}

	/**
	 * Replaces all colons with underscores. Colon is a reserved character in the Lucene query
	 * syntax and replacing it just makes things easier.
	 * 
	 * @param id
	 * @return
	 */
	public static String indexReadyIdStr(DataSourceIdentifier<?> id) {
		String idStr = id.toString();
		return indexReadyIdStr(idStr);
	}

	public static String indexReadyIdStr(String idStr) {
		return idStr.replaceAll(":", "_");
	}

	public static void indexIdNode(GraphDatabaseService graphDb, Node node, String idSource, String idStr) {
		logger.debug("Indexing node "+node.getId()+" key/value: " + idSource + "/" + idStr);
		getIdNodeIndex(graphDb).add(node, idSource, indexReadyIdStr(idStr));
	}
}
