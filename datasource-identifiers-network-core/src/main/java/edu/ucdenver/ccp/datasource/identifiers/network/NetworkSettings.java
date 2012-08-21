package edu.ucdenver.ccp.datasource.identifiers.network;

import java.io.File;

/**
 * Container to store settings used during network construction
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class NetworkSettings {

	private final File dataDirectory;

	/**
	 * 
	 * @param dataDirectory
	 *            the directory where input data for this network is/should be stored
	 */
	public NetworkSettings(File dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	/**
	 * @return the directory where input data for this network is/should be stored
	 */
	public File getDataDirectory() {
		return dataDirectory;
	}

}
