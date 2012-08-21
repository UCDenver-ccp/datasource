package edu.ucdenver.ccp.datasource.identifiers.ncbi.omim;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class OmimID extends IntegerDataSourceIdentifier{

	public OmimID(Integer resourceID) {
		super(resourceID);
	}
	
	public OmimID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.OMIM;
	}
}
