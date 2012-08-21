/**
 * 
 */
package edu.ucdenver.ccp.datasource.download;

import java.io.File;
import java.io.IOException;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public interface FileDownloader {

	public File downloadFile(File storageDirectory) throws IOException;

	/**
	 * Creates a place-holder empty file of the same name as the one that will be downloaded.
	 * 
	 * @param storageDirectory
	 * @throws IOException 
	 */
	public void touchFile(File storageDirectory) throws IOException;
}
