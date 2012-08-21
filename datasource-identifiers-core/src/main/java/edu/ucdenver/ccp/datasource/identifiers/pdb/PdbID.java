package edu.ucdenver.ccp.datasource.identifiers.pdb;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PdbID extends StringDataSourceIdentifier {

	public PdbID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PDB;
	}

}
