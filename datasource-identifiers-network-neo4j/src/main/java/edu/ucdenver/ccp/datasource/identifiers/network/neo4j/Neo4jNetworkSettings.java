/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j;

import java.io.File;

import edu.ucdenver.ccp.datasource.identifiers.network.NetworkSettings;
import edu.ucdenver.ccp.wrapper.neo4j.DefaultGraphSettings;
import edu.ucdenver.ccp.wrapper.neo4j.GraphSettings;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jNetworkSettings extends NetworkSettings {
	private final GraphSettings neo4jGraphSettings;

	/**
	 * @param graphDirectory the directory where the graph itself will be stored (Neo4j stores
	 * graphs in a directory)
	 * 
	 * @param dataDirectory
	 * @param taxonomyId
	 * @param networkDataSources
	 */
	public Neo4jNetworkSettings(File graphDirectory, File dataDirectory) {
		super(dataDirectory);
		neo4jGraphSettings = new DefaultGraphSettings(graphDirectory);
	}

	/**
	 * @return the neo4jGraphSettings
	 */
	public GraphSettings getNeo4jGraphSettings() {
		return neo4jGraphSettings;
	}

}
