package edu.ucdenver.ccp.fileparsers.geneontology;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * Looks like a MGI gene ID, but this type of ID is used by MGI to represent articles and internal
 * publications.
 * 
 * @author bill
 * 
 */
public class MgiReferenceID extends StringDataSourceIdentifier {

	public MgiReferenceID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MGI_REFERENCE;
	}

}
