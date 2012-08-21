package edu.ucdenver.ccp.datasource.identifiers.ebi.interpro;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PfamID extends StringDataSourceIdentifier {

	public PfamID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PFAM;
	}

}
