/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.kegg;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class KeggCompoundID extends StringDataSourceIdentifier {

	/**
	 * @param resourceID
	 */
	public KeggCompoundID(String resourceID) {
		super(resourceID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		return DataSource.KEGG;
	}

}
