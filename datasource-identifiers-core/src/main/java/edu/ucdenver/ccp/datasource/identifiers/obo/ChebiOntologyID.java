package edu.ucdenver.ccp.datasource.identifiers.obo;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class ChebiOntologyID extends StringDataSourceIdentifier {

	public ChebiOntologyID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
return DataSource.CHEBI;
	}

}
