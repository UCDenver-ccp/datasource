package edu.ucdenver.ccp.datasource.identifiers.ensembl;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class EnsemblGeneID  extends DataSourceIdentifier<String>{

	public EnsemblGeneID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.ENSEMBL;
	}

	@Override
	public String validate(String ensemblID) throws IllegalArgumentException {
		return ensemblID;
	}
	

}
