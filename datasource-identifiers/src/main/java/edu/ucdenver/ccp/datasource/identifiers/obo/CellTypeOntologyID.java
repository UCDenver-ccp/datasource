package edu.ucdenver.ccp.datasource.identifiers.obo;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class CellTypeOntologyID extends StringDataSourceIdentifier {

	public CellTypeOntologyID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
return DataSource.CL;
	}

}
