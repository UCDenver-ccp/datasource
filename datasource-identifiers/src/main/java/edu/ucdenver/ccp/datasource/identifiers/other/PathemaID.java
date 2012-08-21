package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PathemaID extends StringDataSourceIdentifier {

	public PathemaID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PATHEMA;
	}

}
