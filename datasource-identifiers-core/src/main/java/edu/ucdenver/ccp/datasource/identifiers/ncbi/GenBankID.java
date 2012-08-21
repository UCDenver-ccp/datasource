package edu.ucdenver.ccp.datasource.identifiers.ncbi;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class GenBankID extends StringDataSourceIdentifier{

	public GenBankID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.GENBANK;
	}

}
