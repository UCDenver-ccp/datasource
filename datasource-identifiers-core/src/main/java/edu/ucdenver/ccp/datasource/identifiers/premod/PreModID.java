package edu.ucdenver.ccp.datasource.identifiers.premod;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PreModID extends StringDataSourceIdentifier {

	public PreModID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PREMOD;
	}

}
