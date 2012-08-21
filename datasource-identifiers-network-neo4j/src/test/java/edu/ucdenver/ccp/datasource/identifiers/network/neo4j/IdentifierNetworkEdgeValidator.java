package edu.ucdenver.ccp.datasource.identifiers.network.neo4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.EdgeType;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierEdge;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index.Neo4jIdentifierNetworkIndexUtil;

public class IdentifierNetworkEdgeValidator {
	private static final Logger logger = Logger.getLogger(IdentifierNetworkEdgeValidator.class);

	public static Collection<String> validateGraph(File graphDirectory,
			Collection<? extends IdentifierEdge> expectedEdges) {
		Collection<String> errorMessages = new ArrayList<String>();
		EmbeddedGraphDatabase graphDb = new EmbeddedGraphDatabase(graphDirectory.getAbsolutePath());
		errorMessages.addAll(validateEdges(graphDb, expectedEdges));
		errorMessages.addAll(validateEdgesIndex(graphDb, expectedEdges));
		graphDb.shutdown();
		return errorMessages;
	}

	/**
	 * @param graphDb
	 * @param expectedEdges
	 * @return
	 */
	private static Collection<? extends String> validateEdgesIndex(EmbeddedGraphDatabase graphDb,
			Collection<? extends IdentifierEdge> expectedEdges) {
		Collection<String> errorMessages = new ArrayList<String>();
		if (expectedEdges != null) {
			for (IdentifierEdge idEdge : expectedEdges) {
				if (!edgeIsAHit(idEdge, graphDb)) {
					logger.debug("did not find expected edge: " + idEdge.toString());
					errorMessages.add("IdEdge index missing edge: " + idEdge.toString());
				}
			}
		}
		return errorMessages;
	}

	private static boolean edgeIsAHit(IdentifierEdge idEdge, EmbeddedGraphDatabase graphDb) {
		EdgeType edgeType = idEdge.getEdgeType();
		IndexHits<Relationship> queryResults = Neo4jIdentifierNetworkIndexUtil
				.queryIdEdgeIndexByType(graphDb, edgeType);// queryIdEdgeIndexByType(graphDb,
		// idEdge.getEdgeType());
		try {
			for (Iterator<Relationship> edgeIter = queryResults.iterator(); edgeIter.hasNext();) {
				Relationship r = edgeIter.next();
				if (propertiesMatch(idEdge, r))
					return true;
			}
			return false;
		} finally {
			queryResults.close();
		}
	}

	/**
	 * @param idEdge
	 * @param r
	 * @return
	 */
	private static boolean propertiesMatch(IdentifierEdge idEdge, Relationship r) {
		return IdentifierNetworkNodeValidator.propertiesMatch(idEdge.getFromIdNode(), r.getStartNode())
				&& IdentifierNetworkNodeValidator.propertiesMatch(idEdge.getToIdNode(), r.getEndNode())
				&& r.getType().name().equals(idEdge.getEdgeType().name());
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

	private static Collection<String> validateEdges(EmbeddedGraphDatabase graphDb,
			Collection<? extends IdentifierEdge> expectedEdges) {
		if (expectedEdges == null)
			expectedEdges = new ArrayList<IdentifierEdge>();

		Collection<String> errorMessages = new ArrayList<String>();
		Set<String> alreadySeenEdgeStrs = new HashSet<String>();
		Set<String> expectedEdgeStrs = createEdgeStrs(expectedEdges);
		for (Node node : graphDb.getAllNodes()) {
			for (Relationship relationship : node.getRelationships()) {
				String edgeStr = createEdgeStr(relationship);
				if (!alreadySeenEdgeStrs.contains(edgeStr)) {
					alreadySeenEdgeStrs.add(edgeStr);
					if (expectedEdgeStrs.contains(edgeStr))
						expectedEdgeStrs.remove(edgeStr);
					else
						errorMessages.add(String.format("Unexpected edge observed in graph: %s", edgeStr));
				}
			}
		}
		for (String expectedEdgeStr : expectedEdgeStrs)
			errorMessages.add(String.format("Expected edge missing from graph: %s", expectedEdgeStr));
		return errorMessages;

	}

	private static String createEdgeStr(Relationship relationship) {
		String startNodeID = NodeIdExtractor.extractNodeIDStr(relationship.getStartNode());
		String endNodeID = NodeIdExtractor.extractNodeIDStr(relationship.getEndNode());
		String properties = PropertyExtractor.extractPropertiesStr(relationship);
		RelationshipType type = relationship.getType();
		// TODO: commenting out below will probably cause some hanalyzer-related unit tests to fail
		// if (GgpNetworkValidator.isGeneNode(relationship.getStartNode())
		// && GgpNetworkValidator.isGeneNode(relationship.getEndNode()))
		// return orderEdgeStr(startNodeID, endNodeID, type, properties, false);
		// else
		return orderEdgeStr(startNodeID, endNodeID, type, properties, true);

	}

	protected static class PropertyExtractor {
		public static String extractPropertiesStr(IdentifierEdge edge) {
			// List<String> propertiesStrs = new ArrayList<String>();
			// for (String propertyKey : edge.getProperties().keySet()) {
			// List<String> propertyValues =
			// Collections.list(Collections.enumeration(edge.getProperties().get(
			// propertyKey)));
			// Collections.sort(propertyValues);
			// propertiesStrs.add(propertyKey + " -- " + propertyValues.toString());
			// }
			// Collections.sort(propertiesStrs);
			// if (propertiesStrs.size() > 0)
			// return propertiesStrs.toString();
			// else
			return "";
		}

		public static String extractPropertiesStr(Relationship relationship) {
			List<String> propertiesStrs = new ArrayList<String>();
			for (String propertyKey : relationship.getPropertyKeys()) {
				List<String> propertyValues = Arrays.asList(((String) relationship.getProperty(propertyKey))
						.split(StringConstants.SPACE));
				Collections.sort(propertyValues);
				propertiesStrs.add(propertyKey + " -- " + propertyValues.toString());
			}
			Collections.sort(propertiesStrs);
			if (propertiesStrs.size() > 0)
				return propertiesStrs.toString();
			return "";
		}

	}

	private static String orderEdgeStr(String startNodeID, String endNodeID, RelationshipType type, String properties,
			boolean isOrderedRelationship) {
		// logger.info(String.format("end node id: '%s' properties='%s'", endNodeID, properties));
		if (isOrderedRelationship)
			return (startNodeID + getEdgeTypeStr(type.name()) + endNodeID + properties).trim();
		List<String> idStrs = new ArrayList<String>();
		idStrs.add(startNodeID);
		idStrs.add(endNodeID);
		Collections.sort(idStrs);
		return (idStrs.get(0) + getEdgeTypeStr(type.name()) + idStrs.get(1) + properties).trim();
	}

	private static Set<String> createEdgeStrs(Collection<? extends IdentifierEdge> expectedEdges) {
		Set<String> edgeStrs = new HashSet<String>();
		for (IdentifierEdge edge : expectedEdges)
			edgeStrs.add(createIdentifierEdgeStr(edge));
		edgeStrs.remove("");
		return edgeStrs;
	}

	public static String getEdgeTypeStr(String edgeType) {
		return " --" + edgeType + "--> ";
	}

	/**
	 * @param edge
	 * @return
	 */
	private static String createIdentifierEdgeStr(IdentifierEdge edge) {
		String idsStr1 = IdentifierNetworkNodeValidator.createIdNodeStr(edge.getFromIdNode());
		String idsStr2 = IdentifierNetworkNodeValidator.createIdNodeStr(edge.getToIdNode());
		return idsStr1 + getEdgeTypeStr(edge.getEdgeType().name()) + idsStr2
				+ PropertyExtractor.extractPropertiesStr(edge);
	}

	private static class NodeIdExtractor {
		public static String extractNodeIDStr(Node node) {
			return IdentifierNetworkNodeValidator.createIDsStr(node);
		}
	}
}
