package edu.ucdenver.ccp.datasource.identifiers.ncbi.homologene;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class HomologeneGroupID extends IntegerDataSourceIdentifier {

	public HomologeneGroupID(Integer resourceID) {
		super(resourceID);
	}

	public HomologeneGroupID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.HOMOLOGENE;
	}
}
