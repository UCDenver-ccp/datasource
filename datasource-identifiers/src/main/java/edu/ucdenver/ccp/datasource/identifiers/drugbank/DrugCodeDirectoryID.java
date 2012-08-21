package edu.ucdenver.ccp.datasource.identifiers.drugbank;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class DrugCodeDirectoryID extends StringDataSourceIdentifier {

	public DrugCodeDirectoryID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DRUGCODEDIRECTORY;
	}

}
