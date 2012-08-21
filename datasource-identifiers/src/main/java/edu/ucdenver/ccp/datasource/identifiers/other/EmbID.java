/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class EmbID extends StringDataSourceIdentifier{

	/**
	 * @param resourceID
	 */
	public EmbID(String resourceID) {
		super(resourceID);
	}

	/* (non-Javadoc)
	 * @see edu.ucdenver.ccp.bio.entity.identifiers.DataElementIdentifier#getNamespace()
	 */
	@Override
	public DataSource getDataSource() {
		return DataSource.EMB;
	}
	
}
