/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.skos;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.network.index.IndexOperation;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierEdge;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNode;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.IdentifierNetworkEdgeValidator;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.IdentifierNetworkNodeValidator;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkBuilder;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.NetworkBuilderTester;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify.Neo4jIdentifierEdgeInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify.Neo4jIdentifierNodeInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify.Neo4jIdentifierNodeMerger;
import edu.ucdenver.ccp.datasource.identifiers.network.skos.SkosMatchNetworkBuilder;
import edu.ucdenver.ccp.datasource.identifiers.network.skos.SkosMatchNetworkBuilder.SkosMatchEdgeType;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SkosMatchNetworkBuilderTest extends NetworkBuilderTester {

	private static final Logger logger = Logger.getLogger(SkosMatchNetworkBuilderTest.class);

	private SkosMatchNetworkBuilder skosNetworkBuilder;
	private Neo4jIdentifierNodeInserter idNodeInserter;
	private Neo4jIdentifierEdgeInserter idEdgeInserter;
	private Neo4jIdentifierNodeMerger idNodeMerger;

	private static final String ID_TYPE = "DNAIdentifier";

	private final EntrezGeneID ID_A_1 = new EntrezGeneID(1234);
	private final IdentifierNode ID_NODE_A_1 = new IdentifierNode(ID_A_1, ID_TYPE);

	private final EntrezGeneID ID_B_1 = new EntrezGeneID(2222);
	private final HgncGeneSymbolID ID_B_2 = new HgncGeneSymbolID("2222");
	private final EntrezGeneID ID_B_3 = new EntrezGeneID(3333);
	private final EntrezGeneID ID_B_4 = new EntrezGeneID(4444);
	private final IdentifierNode ID_NODE_B_1 = new IdentifierNode(CollectionsUtil.createList(ID_B_1), ID_TYPE);
	private final IdentifierNode ID_NODE_B_2 = new IdentifierNode(CollectionsUtil.createList(ID_B_2), ID_TYPE);
	private final IdentifierNode ID_NODE_B_3 = new IdentifierNode(CollectionsUtil.createList(ID_B_3), ID_TYPE);
	private final IdentifierNode ID_NODE_B_4 = new IdentifierNode(CollectionsUtil.createList(ID_B_4), ID_TYPE);
	private final IdentifierNode ID_NODE_B_12 = new IdentifierNode(CollectionsUtil.createList(ID_B_1, ID_B_2), ID_TYPE);
	private final IdentifierNode ID_NODE_B_1234 = new IdentifierNode(CollectionsUtil.createList(ID_B_1, ID_B_2, ID_B_3,
			ID_B_4), ID_TYPE);
	private final IdentifierEdge ID_EDGE_B_12_EXACT = new IdentifierEdge(ID_NODE_B_1, ID_NODE_B_2,
			SkosMatchEdgeType.SKOS_EXACT_MATCH);
	private final IdentifierEdge ID_EDGE_B_12_CLOSE = new IdentifierEdge(ID_NODE_B_1, ID_NODE_B_2,
			SkosMatchEdgeType.SKOS_CLOSE_MATCH);
	private final IdentifierEdge ID_EDGE_B_12_RELATED = new IdentifierEdge(ID_NODE_B_1, ID_NODE_B_2,
			SkosMatchEdgeType.SKOS_RELATED_MATCH);

	@Before
	@Override
	public void setUp() throws IOException {
		super.setUp();
		new Neo4jNetworkBuilder(getNetworkSettings()).cleanNetwork();
		idNodeInserter = new Neo4jIdentifierNodeInserter(getNetworkSettings());
		idEdgeInserter = new Neo4jIdentifierEdgeInserter(getNetworkSettings(), Neo4jSkosMatchRelationshipType.class);
		idNodeMerger = new Neo4jIdentifierNodeMerger(getNetworkSettings());
		skosNetworkBuilder = new SkosMatchNetworkBuilder(getNetworkSettings(), idNodeInserter, idEdgeInserter,
				idNodeMerger);
	}

	/**
	 * Simplest test - this tests that a new ID node can be input into an empty graph
	 */
	@Test
	public void testInsertIdNode_GraphIsEmpty() {
		skosNetworkBuilder.insertIdentifierNode(ID_A_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.close();
		validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_A_1));
		validateGraphForExpectedIdentifierEdges(null);
	}

	/**
	 * Simplest test - this tests that inserting a duplicate ID node does not result in a duplicate
	 * node in the graph
	 */
	@Test
	public void testInsertDuplicateIdNode() {
		skosNetworkBuilder.insertIdentifierNode(ID_A_1, ID_TYPE, IndexOperation.FLUSH);
		skosNetworkBuilder.insertIdentifierNode(ID_A_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.close();
		validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_A_1));
		validateGraphForExpectedIdentifierEdges(null);
	}

	/**
	 * Still very simple - this tests that a new ID node is created when an "exact" match is
	 * inserted, and when neither of the IDs in the exact match are already present in the graph.
	 */
	@Test
	public void testInsertExactMatch_GraphIsEmpty() {
		skosNetworkBuilder.insertIdentifierNode(ID_B_2, ID_TYPE, IndexOperation.NONE);
		/*
		 * ID_B_2 is already present so insertExactMatch should add ID_B_1 to the already present
		 * node
		 */
		skosNetworkBuilder.insertExactMatch(ID_B_1, ID_B_2, ID_TYPE);
		skosNetworkBuilder.close();
		validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_B_1, ID_NODE_B_2));
		validateGraphForExpectedIdentifierEdges(CollectionsUtil.createList(ID_EDGE_B_12_EXACT));
	}

	// @Test
	// public void testInsertCloseMatch_GraphIsEmpty() {
	// skosNetworkBuilder.insertCloseMatch(ID_B_1, ID_B_2);
	// skosNetworkBuilder.close();
	// validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_B_1,
	// ID_NODE_B_2));
	// validateGraphForExpectedIdentifierEdges(CollectionsUtil.createList(ID_EDGE_B_12_CLOSE));
	// }
	//
	// @Test
	// public void testInsertRelatedMatch_GraphIsEmpty() {
	// skosNetworkBuilder.insertRelatedMatch(ID_B_1, ID_B_2);
	// skosNetworkBuilder.close();
	// validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_B_1,
	// ID_NODE_B_2));
	// validateGraphForExpectedIdentifierEdges(CollectionsUtil.createList(ID_EDGE_B_12_RELATED));
	// }

	/**
	 * This tests that an "exact" match adds the missing node and connects it to an already present
	 * ID node
	 */
	@Test
	public void testInsertExactMatch_GraphHasOneIdAlready() {
		skosNetworkBuilder.insertIdentifierNode(ID_B_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertExactMatch(ID_B_1, ID_B_2, ID_TYPE);
		skosNetworkBuilder.close();
		validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_B_1, ID_NODE_B_2));
		validateGraphForExpectedIdentifierEdges(CollectionsUtil.createList(ID_EDGE_B_12_EXACT));
	}

	/**
	 * This tests that an "exact" match results in the connecting of two nodes already present in
	 * the graph
	 */
	@Test
	public void testInsertExactMatch_GraphHasBothIdsAlready() {
		skosNetworkBuilder.insertIdentifierNode(ID_A_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_2, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertExactMatch(ID_B_1, ID_B_2, ID_TYPE);
		skosNetworkBuilder.close();
		validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_A_1, ID_NODE_B_1, ID_NODE_B_2));
		validateGraphForExpectedIdentifierEdges(CollectionsUtil.createList(ID_EDGE_B_12_EXACT));
	}

	/**
	 * This tests that an "exact" match results in the connecting of two nodes already present in
	 * the graph
	 */
	@Test
	public void testMerge_GraphHasBothIdsAlready() {
		skosNetworkBuilder.insertIdentifierNode(ID_A_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_2, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertExactMatch(ID_B_1, ID_B_2, ID_TYPE);
		skosNetworkBuilder.mergeExactMatches();
		skosNetworkBuilder.close();
		printNodes();
		validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_A_1, ID_NODE_B_12));
		validateGraphForExpectedIdentifierEdges(null);
	}

	/**
	 * This tests that an "exact" match results in the connecting of two nodes already present in
	 * the graph
	 */
	@Test
	public void testMerge_GraphHasBothIdsAlready_MultipleIdsOfAGivenType() {
		skosNetworkBuilder.insertIdentifierNode(ID_A_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_1, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_2, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_3, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertIdentifierNode(ID_B_4, ID_TYPE, IndexOperation.NONE);
		skosNetworkBuilder.insertExactMatch(ID_B_1, ID_B_2, ID_TYPE);
		skosNetworkBuilder.insertExactMatch(ID_B_1, ID_B_3, ID_TYPE);
		skosNetworkBuilder.insertExactMatch(ID_B_2, ID_B_4, ID_TYPE);
		skosNetworkBuilder.mergeExactMatches();
		skosNetworkBuilder.close();
		printNodes();
		validateGraphForExpectedIdentifierNodes(CollectionsUtil.createList(ID_NODE_A_1, ID_NODE_B_1234));
		validateGraphForExpectedIdentifierEdges(null);
	}

	/**
	 * 
	 */
	private void printNodes() {
		GraphDatabaseService graphDb = getGraph();
		Transaction tx = graphDb.beginTx();
		try {
			for (Node node : graphDb.getAllNodes()) {
				logger.debug("NODE: " + IdentifierNetworkNodeValidator.createIDsStr(node));
			}
			tx.success();
		} finally {
			tx.finish();
			graphDb.shutdown();
		}
	}

	/**
	 * Checks the graph for expected nodes
	 * 
	 * @param expectedTerms
	 */
	private void validateGraphForExpectedIdentifierNodes(Collection<IdentifierNode> expectedIdNodes) {
		Collection<String> errorMessages = IdentifierNetworkNodeValidator.validateGraph(getGraphDirectory(),
				expectedIdNodes);
		if (!errorMessages.isEmpty())
			fail("Graph not as expected:\n" + errorMessages);
	}

	/**
	 * Checks the graph for expected edges
	 * 
	 * @param expectedTerms
	 */
	private void validateGraphForExpectedIdentifierEdges(Collection<IdentifierEdge> expectedIdEdges) {
		Collection<String> errorMessages = IdentifierNetworkEdgeValidator.validateGraph(getGraphDirectory(),
				expectedIdEdges);
		if (!errorMessages.isEmpty())
			fail("Graph not as expected:\n" + errorMessages);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.identifier.network.impl.neo4j.NetworkBuilderTester#
	 * getNetworkTaxonomyId()
	 */
	@Override
	protected NcbiTaxonomyID getNetworkTaxonomyId() {
		return null;
	}

}
