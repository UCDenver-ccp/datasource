package edu.ucdenver.ccp.datasource.identifiers.sgd;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class SgdID extends StringDataSourceIdentifier {

	public SgdID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.SGD;
	}

}
