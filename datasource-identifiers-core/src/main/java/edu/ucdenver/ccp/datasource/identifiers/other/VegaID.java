package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class VegaID extends DataSourceIdentifier<String>{

	public VegaID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		return resourceID;
	}

}
