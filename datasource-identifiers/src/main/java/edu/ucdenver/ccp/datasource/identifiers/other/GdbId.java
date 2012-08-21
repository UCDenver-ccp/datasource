package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class GdbId extends StringDataSourceIdentifier {

	public GdbId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.GDB;
	}

}
