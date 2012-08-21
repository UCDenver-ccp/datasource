package edu.ucdenver.ccp.datasource.identifiers.network.neo4j;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNode;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index.Neo4jIdentifierNetworkIndexUtil;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify.Neo4jIdentifierNodeUtil;

public class IdentifierNetworkNodeValidator {
	private static final Logger logger = Logger.getLogger(IdentifierNetworkNodeValidator.class);

	public static Collection<String> validateGraph(File graphDirectory,
			Collection<? extends IdentifierNode> expectedIdNodes) {
		Collection<String> errorMessages = new ArrayList<String>();
		EmbeddedGraphDatabase graphDb = new EmbeddedGraphDatabase(graphDirectory.getAbsolutePath());
		errorMessages.addAll(validateIdentifierNodes(graphDb, expectedIdNodes));
		errorMessages.addAll(validateIdentifierNodesIndex(graphDb, expectedIdNodes));
		graphDb.shutdown();
		return errorMessages;
	}

	private static Collection<? extends String> validateIdentifierNodesIndex(EmbeddedGraphDatabase graphDb,
			Collection<? extends IdentifierNode> expectedIdNodes) {
		Collection<String> errorMessages = new ArrayList<String>();
		if (expectedIdNodes != null) {
			Index<Node> idNodeIndex = Neo4jIdentifierNetworkIndexUtil.getIdNodeIndex(graphDb);
			for (IdentifierNode idNode : expectedIdNodes) {
				for (Entry<DataSource, Set<DataSourceIdentifier<?>>> entry : idNode.getIds().entrySet()) {
					for (DataSourceIdentifier<?> id : entry.getValue()) {
						logger.debug("looking for id node: " + id.toString());
						if (!idIsAHit(idNode, id, graphDb, idNodeIndex)) {
							logger.debug("did not find gene: " + id.toString());
							errorMessages.add("IdNode index missing gene: " + idNode.toString());
						}
					}
				}
			}
		}
		return errorMessages;
	}

	private static boolean idIsAHit(IdentifierNode idNode, DataSourceIdentifier<?> id, EmbeddedGraphDatabase graphDb,
			Index<Node> idNodeIndex) {
		System.out.println("Query using: " + id.getDataSource().name() + " " + id.toString());
		IndexHits<Node> queryResults = Neo4jIdentifierNetworkIndexUtil.queryIdNodeIndex(idNodeIndex, id);
		try {
			for (Iterator<Node> nodeIter = queryResults.iterator(); nodeIter.hasNext();) {
				Node node = nodeIter.next();
				System.out.println("Matching " + idNode.toString() + " with " + node.toString());
				if (propertiesMatch(idNode, node)) {
					return true;
				}
			}
			System.out.println("IsAHit = false");
			return false;
		} finally {
			queryResults.close();
		}
	}

	public static boolean propertiesMatch(IdentifierNode idNode, Node node) {
		boolean checked = false;
		for (String propertyKey : node.getPropertyKeys()) {
			checked = true;
			if (propertyKey.equals(Neo4jIdentifierNodeUtil.ID_TYPE_KEY)) {
				String idType = node.getProperty(propertyKey).toString();
				if (!idType.equals(idNode.getIdType())) {
					System.out.println("id types differ");
					return false;
				}
			} else if (DataSource.isDataSource(propertyKey)) {
				DataSource idType = DataSource.valueOf(propertyKey);
				String nodeIds = (String) node.getProperty(propertyKey);
				if (!idNode.getIds().containsKey(idType)) {
					System.out.println("Node missing property: " + idType.name());
					return false;
				}
				Set<DataSourceIdentifier<?>> set = idNode.getIds().get(idType);
				String ids = CollectionsUtil.createDelimitedString(set, StringConstants.SPACE);
				Set<String> nodeIdsSet = CollectionsUtil.array2Set(nodeIds.split(StringConstants.SPACE));
				Set<String> idsSet = CollectionsUtil.array2Set(ids.split(StringConstants.SPACE));
				if (!nodeIdsSet.equals(idsSet)) {
					System.out.println("Id sets not equal: " + nodeIdsSet.toString() + " -- " + idsSet.toString());
					return false;
				}
			} else {
				throw new RuntimeException("Unknown property key: " + propertyKey);
			}
		}
		for (Entry<DataSource, Set<DataSourceIdentifier<?>>> entry : idNode.getIds().entrySet()) {
			DataSource idType = entry.getKey();
			if (!node.hasProperty(idType.name())) {
				System.out.println("Node missing property2: " + idType.name());
				return false;
			}
			String nodeIds = (String) node.getProperty(idType.name());
			String ids = CollectionsUtil.createDelimitedString(entry.getValue(), StringConstants.SPACE);
			Set<String> nodeIdsSet = CollectionsUtil.array2Set(nodeIds.split(StringConstants.SPACE));
			Set<String> idsSet = CollectionsUtil.array2Set(ids.split(StringConstants.SPACE));
			if (!nodeIdsSet.equals(idsSet)) {
				System.out.println("Id sets not equal2: " + nodeIdsSet.toString() + " -- " + idsSet.toString());
				return false;
			}
		}
		System.out.println("REturning checked..." + checked);
		return checked;
	}

	private static Map<String, Object> getNodeProperties(Node node) {
		Map<String, Object> properties = new HashMap<String, Object>();
		for (String key : node.getPropertyKeys())
			properties.put(key, node.getProperty(key));
		return properties;
	}

	private static String getPropertiesStr(Map<String, Object> properties) {
		List<String> propertyKeys = Collections.list(Collections.enumeration(properties.keySet()));
		Collections.sort(propertyKeys);
		StringBuilder propertiesStr = new StringBuilder();
		for (String propertyKey : propertyKeys) {
			propertiesStr.append(" ");
			propertiesStr.append(propertyKey);
			propertiesStr.append(":");
			propertiesStr.append(properties.get(propertyKey).toString());
		}
		return propertiesStr.toString();
	}

	private static Collection<String> validateIdentifierNodes(EmbeddedGraphDatabase graphDb,
			Collection<? extends IdentifierNode> expectedGeneNodes) {
		/* +1 accounts for the root node in neo4j graphs */
		assertEquals("Graph should have the expected number of nodes.", expectedGeneNodes.size() + 1, CollectionsUtil
				.createList(graphDb.getAllNodes().iterator()).size());
		Collection<String> errorMessages = new ArrayList<String>();
		Set<String> expectedIdNodeStrs = createIdNodeStrs(expectedGeneNodes);
		for (Node node : graphDb.getAllNodes()) {
			if (node.getId() != 0) { // ignore the root node
				String idNodeStr = createIdNodeStr(node);
				if (expectedIdNodeStrs.contains(idNodeStr))
					expectedIdNodeStrs.remove(idNodeStr);
				else
					errorMessages.add(String.format("Unexpected ID node observed in graph: %s", idNodeStr));
			}
		}
		for (String expectedIdNodeStr : expectedIdNodeStrs)
			errorMessages.add(String.format("Expected gene node missing from graph: %s", expectedIdNodeStr));
		return errorMessages;
	}

	private static String createIdNodeStr(Node idNode) {
		return createIDsStr(idNode);
	}

	public static String createIDsStr(Node idNode) {
		List<String> idsList = new ArrayList<String>();
		for (String idType : idNode.getPropertyKeys()) {
			if (idNode.hasProperty(idType)) {
				String idStr = idNode.getProperty(idType).toString();
				idsList.add(idType + " -- " + sortSpaceDelimitedList(idStr));
			}
		}
		Collections.sort(idsList);
		return idsList.toString();
	}

	/**
	 * Takes a space-delimited string as input and returns a space delimited string whose members
	 * have been sorted
	 * 
	 * @param stringToSort
	 * @return
	 */
	private static String sortSpaceDelimitedList(String stringToSort) {
		String[] ids = stringToSort.split(StringConstants.SPACE);
		List<String> sortedIdList = new ArrayList<String>();
		for (String id : ids)
			sortedIdList.add(id);
		Collections.sort(sortedIdList);
		return CollectionsUtil.createDelimitedString(sortedIdList, StringConstants.SPACE);
	}

	public static String createIdNodeStr(IdentifierNode idNode) {
		List<String> idsList = new ArrayList<String>();
		for (Entry<DataSource, Set<DataSourceIdentifier<?>>> entry : idNode.getIds().entrySet()) {
			idsList.add(entry.getKey().name() + " -- "
					+ sortSpaceDelimitedList(entry.getValue().toString().replaceAll("[\\]\\[,]", "")));
		}
		idsList.add(Neo4jIdentifierNodeUtil.ID_TYPE_KEY + " -- " + idNode.getIdType());
		Collections.sort(idsList);
		return idsList.toString();
	}

	private static Set<String> createIdNodeStrs(Collection<? extends IdentifierNode> expectedIdNodes) {
		Set<String> idNodeStrs = new HashSet<String>();
		if (expectedIdNodes != null) {
			for (IdentifierNode idNode : expectedIdNodes) {
				idNodeStrs.add(createIdNodeStr(idNode));
			}
		}
		return idNodeStrs;
	}

}
