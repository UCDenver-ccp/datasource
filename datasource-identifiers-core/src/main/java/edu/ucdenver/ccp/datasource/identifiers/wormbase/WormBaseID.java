package edu.ucdenver.ccp.datasource.identifiers.wormbase;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class WormBaseID extends StringDataSourceIdentifier {

	public WormBaseID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.WORMBASE;
	}

}
