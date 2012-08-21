package edu.ucdenver.ccp.datasource.identifiers.bind;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class BindInteractionID extends IntegerDataSourceIdentifier {

	public BindInteractionID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.BIND;
	}

}
