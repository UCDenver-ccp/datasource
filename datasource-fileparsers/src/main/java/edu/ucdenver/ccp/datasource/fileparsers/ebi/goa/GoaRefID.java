package edu.ucdenver.ccp.fileparsers.ebi.goa;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class GoaRefID extends StringDataSourceIdentifier {

	public GoaRefID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.GOA_REFERENCE;
	}

}
