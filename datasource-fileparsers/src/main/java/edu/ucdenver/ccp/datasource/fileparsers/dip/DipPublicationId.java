/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.dip;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * @author Center for Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class DipPublicationId extends StringDataSourceIdentifier {

	/**
	 * @param resourceID
	 */
	public DipPublicationId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DIP;
	}

}
