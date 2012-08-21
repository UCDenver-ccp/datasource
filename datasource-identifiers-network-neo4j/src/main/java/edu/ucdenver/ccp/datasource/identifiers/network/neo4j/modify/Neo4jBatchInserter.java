/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.modify;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.index.BatchInserterIndex;
import org.neo4j.graphdb.index.BatchInserterIndexProvider;
import org.neo4j.index.impl.lucene.LuceneBatchInserterIndexProvider;
import org.neo4j.kernel.impl.batchinsert.BatchInserter;

import edu.ucdenver.ccp.datasource.identifiers.network.NetworkInserter;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.Neo4jNetworkSettings;
import edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index.NetworkIndexUtil;
import edu.ucdenver.ccp.wrapper.neo4j.Neo4jUtil;

/**
 * This class performs the core methods stipulated by the {@link NetworkInserter} interface using a
 * Neo4j backend.
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public abstract class Neo4jBatchInserter implements NetworkInserter {

	private static final Logger logger = Logger.getLogger(Neo4jBatchInserter.class);

	private final Neo4jNetworkSettings networkSettings;

	private BatchInserter inserter;
	private BatchInserterIndex index;
	private BatchInserterIndexProvider indexProvider;
	private boolean isActive = false;

	public Neo4jBatchInserter(Neo4jNetworkSettings networkSettings) {
		this.networkSettings = networkSettings;
		this.setActive(false);
	}

	@Override
	public void activate() {
		if (!isActive()) {
			setInserter(NetworkIndexUtil.initializeBatchInserter(getNetworkSettings()));
			setIndexProvider(new LuceneBatchInserterIndexProvider(getInserter()));
			setIndex(getBatchInserterIndex());
			setActive(true);
		}
	}

	/**
	 * @return the {@link BatchInserterIndex} implementation to use
	 */
	protected abstract BatchInserterIndex getBatchInserterIndex();

	@Override
	public void shutdown() {
		if (isActive)
			Neo4jUtil.shutdownAndLog(getInserter(), getIndexProvider(), getNetworkSettings().getNeo4jGraphSettings()
					.getGraphDirectory(), logger);
		setActive(false);
	}

	// public long getNodeCount() {
	// long count = 0;
	// for (Iterator<Node> nodeIter = getInserter().getGraphDbService().getAllNodes().iterator();
	// nodeIter.hasNext();)
	// count++;
	// return count;
	// }

	/**
	 * @return the inserter
	 */
	public BatchInserter getInserter() {
		return inserter;
	}

	/**
	 * @param inserter
	 *            the inserter to set
	 */
	private void setInserter(BatchInserter inserter) {
		this.inserter = inserter;
	}

	/**
	 * @return the index
	 */
	public BatchInserterIndex getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	private void setIndex(BatchInserterIndex index) {
		this.index = index;
	}

	/**
	 * @return the indexProvider
	 */
	public BatchInserterIndexProvider getIndexProvider() {
		return indexProvider;
	}

	/**
	 * @param indexProvider
	 *            the indexProvider to set
	 */
	private void setIndexProvider(BatchInserterIndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	/**
	 * @return the isActive
	 */
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
	 * @return the networkSettings
	 */
	public Neo4jNetworkSettings getNetworkSettings() {
		return networkSettings;
	}

}
