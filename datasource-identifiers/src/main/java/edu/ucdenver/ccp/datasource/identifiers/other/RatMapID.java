package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class RatMapID extends IntegerDataSourceIdentifier {

	public RatMapID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.RATMAP;
	}

}
