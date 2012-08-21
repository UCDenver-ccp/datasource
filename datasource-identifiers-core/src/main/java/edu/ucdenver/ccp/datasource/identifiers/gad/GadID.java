package edu.ucdenver.ccp.datasource.identifiers.gad;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class GadID extends IntegerDataSourceIdentifier {

	public GadID(Integer resourceID) {
		super(resourceID);
	}

	public GadID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.GAD;
	}

}
