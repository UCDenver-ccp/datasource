package edu.ucdenver.ccp.datasource.identifiers.flybase;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class FlyBaseID extends StringDataSourceIdentifier {

	public FlyBaseID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.FLYBASE;
	}

}
