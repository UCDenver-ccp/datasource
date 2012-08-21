/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.FileArchiveUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.identifiers.network.NetworkBuilder;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Neo4jNetworkBuilder extends NetworkBuilder {

	private static final Logger logger = Logger.getLogger(Neo4jNetworkBuilder.class);

	/**
	 * @param networkSettings
	 */
	public Neo4jNetworkBuilder(Neo4jNetworkSettings networkSettings) {
		super(networkSettings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.identifier.network.NetworkBuilder#backupGraph()
	 */
	@Override
	public void backupGraph(String backupFileNamePrefix) throws IOException {
		File graphDirectory = ((Neo4jNetworkSettings) getNetworkSettings()).getNeo4jGraphSettings().getGraphDirectory();
		File backupDirectory = ((Neo4jNetworkSettings) getNetworkSettings()).getDataDirectory();
		File backupTarFile = new File(backupDirectory, "backup-" + backupFileNamePrefix + ".tar");
		logger.info("Backing up Neo4j graph directory to: " + backupTarFile.getAbsolutePath());
		FileArchiveUtil.packTarFile(graphDirectory, backupTarFile, FileArchiveUtil.IncludeBaseDirectoryInPackage.YES);
		logger.info("Compressing : " + backupTarFile.getAbsolutePath());
		FileArchiveUtil.gzipFile(backupTarFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.identifier.network.NetworkBuilder#cleanNetwork()
	 */
	@Override
	public void cleanNetwork() {
		File graphDirectory = ((Neo4jNetworkSettings) getNetworkSettings()).getNeo4jGraphSettings().getGraphDirectory();
		logger.info(String.format("Cleaning graph directory: %s", graphDirectory.getAbsolutePath()));
		try {
			FileUtil.cleanDirectory(graphDirectory);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

}
