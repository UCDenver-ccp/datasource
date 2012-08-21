package edu.ucdenver.ccp.datasource.identifiers.ebi.intact;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class IntActID extends StringDataSourceIdentifier{

	public IntActID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.INTACT;
	}


}
