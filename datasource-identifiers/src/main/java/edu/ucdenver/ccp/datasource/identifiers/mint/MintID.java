package edu.ucdenver.ccp.datasource.identifiers.mint;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class MintID extends StringDataSourceIdentifier {

	public MintID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MINT;
	}

}
