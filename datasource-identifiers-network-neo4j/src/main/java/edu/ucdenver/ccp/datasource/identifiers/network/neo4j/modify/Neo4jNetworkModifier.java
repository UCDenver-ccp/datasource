/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.identifiers.network.NetworkInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkSettings;
import edu.ucdenver.ccp.wrapper.neo4j.Neo4jUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jNetworkModifier implements NetworkInserter {

	private static final Logger logger = Logger.getLogger(Neo4jNetworkModifier.class);

	public static final String ID_STORAGE_DELIMITER = StringConstants.SPACE;

	protected final Neo4jNetworkSettings networkSettings;
	private GraphDatabaseService graphDb;
	private boolean isActive = false;

	public Neo4jNetworkModifier(Neo4jNetworkSettings networkSettings) {
		this.networkSettings = networkSettings;
		this.setActive(false);
	}

	@Override
	public void activate() {
		if (!isActive()) {
			setGraphDb(new EmbeddedGraphDatabase(networkSettings.getNeo4jGraphSettings().getGraphDirectory()
					.getAbsolutePath()));
			setActive(true);
		}
	}

	@Override
	public void shutdown() {
		if (isActive()) {
			Neo4jUtil.logGraphStatistics(getGraphDb(), logger);
			getGraphDb().shutdown();
		}
		setActive(false);
	}

	/**
	 * @return the isActive
	 */
	@Override
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	private void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the graphDb
	 */
	public GraphDatabaseService getGraphDb() {
		return graphDb;
	}

	/**
	 * @param graphDb
	 *            the graphDb to set
	 */
	private void setGraphDb(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}

}
