/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.dip;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class DipProcessingStatusId extends StringDataSourceIdentifier {
	
	/**
	 * @param resourceID
	 */
	public DipProcessingStatusId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DIP;
	}

}
