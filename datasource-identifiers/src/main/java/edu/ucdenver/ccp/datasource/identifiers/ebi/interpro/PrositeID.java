package edu.ucdenver.ccp.datasource.identifiers.ebi.interpro;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PrositeID extends StringDataSourceIdentifier {

	public PrositeID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PROSITE;
	}

}
