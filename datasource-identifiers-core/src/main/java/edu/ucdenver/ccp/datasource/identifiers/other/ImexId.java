package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * 
 * Represents an ID from the International Molecular Exchange Consortium
 * 
 * (http://www.imexconsortium.org/)
 * 
 * 
 * 
 * @author
 * 
 * 
 */

public class ImexId extends StringDataSourceIdentifier {

	public ImexId(String resourceID) {

		super(resourceID);

	}

	@Override
	public DataSource getDataSource() {

		return DataSource.IMEX;

	}

}
