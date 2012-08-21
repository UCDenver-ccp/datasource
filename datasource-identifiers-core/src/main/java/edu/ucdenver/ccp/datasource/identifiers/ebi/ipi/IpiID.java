package edu.ucdenver.ccp.datasource.identifiers.ebi.ipi;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class IpiID extends StringDataSourceIdentifier {

	public IpiID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.IPI;
	}

}
