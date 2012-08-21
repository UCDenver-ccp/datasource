package edu.ucdenver.ccp.datasource.identifiers.ncbi.gene;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class GiNumberID extends IntegerDataSourceIdentifier {

	public GiNumberID(Integer resourceID) {
		super(resourceID);
	}

	public GiNumberID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.GENBANK;
	}
}
