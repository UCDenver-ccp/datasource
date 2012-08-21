package edu.ucdenver.ccp.datasource.identifiers.rgd;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class RgdID extends IntegerDataSourceIdentifier {

	public RgdID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.RGD;
	}

}
