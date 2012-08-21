package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class DictyBaseID extends StringDataSourceIdentifier {

	public DictyBaseID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DICTYBASE;
	}

}
