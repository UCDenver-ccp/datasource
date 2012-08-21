package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index;

import org.apache.log4j.Logger;
import org.neo4j.kernel.impl.batchinsert.BatchInserter;

import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkSettings;
import edu.ucdenver.ccp.wrapper.neo4j.Neo4jUtil;

public class NetworkIndexUtil {

	/**
	 * Initializes a BatchInserter for the neo4j graph specified by the hanalyzer network properties
	 * 
	 * @param networkSettings
	 * @return
	 */
	public static BatchInserter initializeBatchInserter(Neo4jNetworkSettings networkSettings) {
		return Neo4jUtil.initializeBatchInserter(networkSettings.getNeo4jGraphSettings().getGraphDirectory(),
				networkSettings.getNeo4jGraphSettings().getNeo4jProperties());
	}

	// private static final String MASTER_EDGE_INDEX_NAME = "mainEdge";
	// private static final String TERMS_INDEX_NAME = "terms";
	// private static final String GENES_INDEX_NAME = "genes";
	// private static final String EDGES_INDEX_NAME = "edges";
	// private static final Logger logger = Logger.getLogger(NetworkIndexUtil.class);
	//
	//
	// public static BatchInserterIndex getTermsInserterIndex(BatchInserter inserter,
	// BatchInserterIndexProvider indexProvider) {
	// return indexProvider.nodeIndex(TERMS_INDEX_NAME,
	// MapUtil.stringMap("provider", "lucene", "type", "exact", "analyzer",
	// IdIndexAnalyzer.class.getName()));
	// }
	//
	// public static BatchInserterIndex getEdgesInserterIndex(BatchInserter inserter,
	// BatchInserterIndexProvider indexProvider) {
	// return indexProvider.relationshipIndex(EDGES_INDEX_NAME,
	// MapUtil.stringMap("provider", "lucene", "type", "exact", "analyzer",
	// IdIndexAnalyzer.class.getName()));
	// }
	//
	// public static BatchInserterIndex getMasterEdgesInserterIndex(BatchInserter inserter,
	// BatchInserterIndexProvider indexProvider) {
	// return indexProvider.relationshipIndex(MASTER_EDGE_INDEX_NAME,
	// MapUtil.stringMap("provider", "lucene", "type", "exact", "analyzer",
	// IdIndexAnalyzer.class.getName()));
	// }
	//
	// public static Index<Node> getTermsIndex(GraphDatabaseService graphDb) {
	// return graphDb.index().forNodes(TERMS_INDEX_NAME,
	// MapUtil.stringMap("provider", "lucene", "type", "exact", "analyzer",
	// IdIndexAnalyzer.class.getName()));
	// }
	//
	// public static RelationshipIndex getEdgesIndex(GraphDatabaseService graphDb) {
	// return graphDb.index().forRelationships(EDGES_INDEX_NAME,
	// MapUtil.stringMap("provider", "lucene", "type", "exact", "analyzer",
	// IdIndexAnalyzer.class.getName()));
	// }
	//
	// public static RelationshipIndex getMasterEdgeRelationshipIndex(GraphDatabaseService graphDb)
	// {
	// return graphDb.index().forRelationships(MASTER_EDGE_INDEX_NAME,
	// MapUtil.stringMap("provider", "lucene", "type", "exact", "analyzer",
	// IdIndexAnalyzer.class.getName()));
	// }

	public static void logGraphStatistics(Neo4jNetworkSettings networkSettings, Logger logger) {
		Neo4jUtil.logGraphStatistics(networkSettings.getNeo4jGraphSettings().getGraphDirectory(), logger);
	}

	// public static IndexHits<Node> queryGenesIndex(Index<Node> genesIndex, GeneIdType idType,
	// DataElementIdentifier<?> geneId) {
	// Query query = createIndexQuery(idType.name(), geneId.toString());
	// // logger.info("QUERY: " + query.toString());
	// return genesIndex.query(query);
	// }

	// public static IndexHits<Node> queryTermsIndexByTermId(Index<Node> termsIndex, String termId)
	// {
	// Query query = createIndexQuery(TermProperties.TERM_ID.name(), termId);
	// // logger.info("QUERY: " + query.toString());
	// return termsIndex.query(query);
	// }
	//
	// public static IndexHits<Node> queryTermsIndexByTermNamespace(Index<Node> termsIndex, String
	// namespace) {
	// Query query = createIndexQuery(TermProperties.TERM_NAMESPACE.name(), namespace);
	// // logger.info("QUERY: " + query.toString());
	// return termsIndex.query(query);
	// }

	//
	//
	// public static IndexHits<Long> queryTermsIndexForTermIdMatch(BatchInserterIndex termsIndex,
	// Term term) {
	// return termsIndex.query(createIndexQuery(TermProperties.TERM_ID.name(),
	// term.getTermId().toString()));
	// }
	//
	// public static IndexHits<Node> queryTermsIndexForTermIdMatch(Index<Node> termsIndex, Term
	// term) {
	// return termsIndex.query(createIndexQuery(TermProperties.TERM_ID.name(),
	// term.getTermId().toString()));
	// }
	//
	// /**
	// * Returns null if query fails to find a match
	// *
	// * @param termsIndex
	// * @param termId
	// * @return
	// */
	// public static Long queryTermsIndexForSingleTermMatch(BatchInserterIndex termsIndex, String
	// termId) {
	// IndexHits<Long> hits = termsIndex.query(createIndexQuery(TermProperties.TERM_ID.name(),
	// termId));
	// if (hits == null) {
	// logger.warn("Query failed for term ID: " + termId);
	// return null;
	// }
	// return hits.getSingle();
	// }
	//
	// public static IndexHits<Long> queryTermsIndexForNamespaceMatches(BatchInserterIndex
	// termsIndex, String namespace) {
	// return termsIndex.query(createIndexQuery(TermProperties.TERM_NAMESPACE.name(), namespace));
	// }
	//
	// public static IndexHits<Node> queryTermsIndexForNamespaceMatches(Index<Node> termsIndex,
	// String namespace) {
	// return termsIndex.query(TermProperties.TERM_NAMESPACE.name(), namespace);
	// }
	//
	// /**
	// * Returns the collection of all master edges in the graph
	// *
	// * @param graphDb
	// * @return
	// */
	// public static IndexHits<Relationship> getMasterRelationships(GraphDatabaseService graphDb) {
	// RelationshipIndex masterEdgeRelationshipIndex = getMasterEdgeRelationshipIndex(graphDb);
	// return masterEdgeRelationshipIndex.query(EdgeIndexProperties.EDGE_TYPE.name(),
	// HanalyzerEdgeType.MASTER.name());
	// }
	//
	// public static Iterator<Relationship> getMasterRelationshipIterator(GraphDatabaseService
	// graphDb) {
	// return getMasterRelationships(graphDb).iterator();
	// }
	//
	// public static IndexHits<Relationship> queryEdgesByDataSource(GraphDatabaseService graphDb,
	// String dataSourceName) {
	// RelationshipIndex edgesIndex = getEdgesIndex(graphDb);
	// Transaction tx = graphDb.beginTx();
	// try {
	// Query query = createIndexQuery(EdgeProperties.ASSERTING_RESOURCE.name(), dataSourceName);
	// logger.info("Searching for resource edges: " + dataSourceName + " query=" +
	// query.toString());
	// IndexHits<Relationship> query2 = edgesIndex.query(query);
	// tx.success();
	// return query2;
	// } finally {
	// tx.finish();
	// }
	// }
	//
	// // /**
	// // * Searches the index for a node that matches the input gene using the given GeneIdType.
	// // Returns
	// // * null if the node can't be found.
	// // *
	// // * @param gene
	// // * @param genesIndex
	// // * @param searchGeneIdType
	// // * @return
	// // */
	// // public static IndexHits<Long> searchGenesIndex(Gene gene, GeneIdType searchGeneIdType,
	// // BatchInserterIndex genesIndex) {
	// // if (gene.getGeneIds().containsKey(searchGeneIdType)) {
	// // for (DataElementIdentifier<?> id : gene.getGeneIds().get(searchGeneIdType)) {
	// // return genesIndex.query(searchGeneIdType.name(), String.format("%s", id.toString()));
	// // }
	// // }
	// // return null;
	// // }
	// //
	// // /**
	// // * Searches the index for a node that matches the input gene using the given GeneIdType.
	// // Returns
	// // * null if the node can't be found.
	// // *
	// // * @param gene
	// // * @param genesIndex
	// // * @param searchGeneIdType
	// // * @return
	// // */
	// // public static IndexHits<Long> searchGenesIndex(Gene gene, BatchInserterIndex genesIndex) {
	// // for (GeneIdType idType : gene.getGeneIds().keySet()) {
	// // for (DataElementIdentifier<?> id : gene.getGeneIds().get(idType)) {
	// // return genesIndex.query(idType.name(), String.format("%s", id.toString()));
	// // }
	// // }
	// // return null;
	// // }
	// //
	// // public static IndexHits<Long> searchTermsIndex(String termID, BatchInserterIndex
	// termsIndex)
	// // {
	// // return termsIndex.query(TermProperties.TERM_ID.name(), String.format("%s",
	// // termID.replaceAll(":", "_")));
	// // }
	// //
	// // public static Long searchForSingleTerm(String termID, BatchInserterIndex termsIndex) {
	// // IndexHits<Long> indexHits = searchTermsIndex(termID, termsIndex);
	// // if (indexHits.size() == 0) {
	// // logger.warn(String
	// //
	// .format("TermID %s does not exist in the graph (Most likely it has been deprecated by the source resource). A link has not been generated.",
	// // termID));
	// // return null;
	// // }
	// // return indexHits.getSingle();
	// // }
	// //
	// // public static IndexHits<Node> retrieveAllTermsInNamespace(String namespace, Index<Node>
	// // termsIndex) {
	// // return termsIndex.query(TermProperties.TERM_NAMESPACE.name(), namespace);
	// //
	// // }

}
