package edu.ucdenver.ccp.identifier.publication;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PubMedCentralID extends StringDataSourceIdentifier {

	public PubMedCentralID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PMC;
	}

}
