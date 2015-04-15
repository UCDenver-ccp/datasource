package edu.ucdenver.ccp.fileparsers.ebi.goa;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class GoRefID extends StringDataSourceIdentifier {

	public GoRefID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.GO_REFERENCE;
	}

}
