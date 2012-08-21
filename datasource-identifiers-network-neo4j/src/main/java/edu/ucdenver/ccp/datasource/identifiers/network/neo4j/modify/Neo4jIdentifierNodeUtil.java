/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.neo4j.graphdb.Node;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.network.modify.IdentifierNode;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jIdentifierNodeUtil {

	public static final String ID_TYPE_KEY = "IdType";
	public static final String ID_STORAGE_DELIMITER = StringConstants.SPACE;
	public static final String ID_STORAGE_DELIMITER_REGEX = RegExPatterns.SPACE;

	public static Map<String, Object> getNodeProperties(IdentifierNode idNode) {
		/* extract the id_type */
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put(ID_TYPE_KEY, idNode.getIdType());
		/* extract the datasource identifiers */
		for (Entry<DataSource, Set<DataSourceIdentifier<?>>> entry : idNode.getIds().entrySet()) {
			String idStr = CollectionsUtil.createDelimitedString(entry.getValue(), ID_STORAGE_DELIMITER);
			propertiesMap.put(entry.getKey().name(), idStr);
		}
		return propertiesMap;
	}

	/**
	 * @param node
	 * @return the identifier type for the input {@link Node}
	 */
	public static String getIdType(Node node) {
		return node.getProperty(ID_TYPE_KEY).toString();
	}

	/**
	 * Compiles all {@link DataSourceIdentifier} object represented by the input {@link Node} into a
	 * single set.
	 * 
	 * @param node
	 * @return
	 */
	public static Map<DataSource, Set<DataSourceIdentifier<?>>> getDataSourceIdentifiers(Node node) {
		Map<DataSource, Set<DataSourceIdentifier<?>>> dataSourceToIdsMap = new HashMap<DataSource, Set<DataSourceIdentifier<?>>>();
		for (String propertyKey : node.getPropertyKeys()) {
			Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
			if (!propertyKey.equals(ID_TYPE_KEY)) {
				DataSource ds = DataSource.valueOf(propertyKey);
				for (String idStr : node.getProperty(propertyKey).toString().split(ID_STORAGE_DELIMITER_REGEX)) {
					ids.add(DataSourceIdResolver.resolveId(ds, idStr));
				}
				dataSourceToIdsMap.put(ds, ids);
			}
		}
		return dataSourceToIdsMap;
	}

}
