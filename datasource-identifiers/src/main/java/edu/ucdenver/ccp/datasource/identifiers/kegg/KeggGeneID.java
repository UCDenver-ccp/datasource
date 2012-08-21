package edu.ucdenver.ccp.datasource.identifiers.kegg;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class KeggGeneID extends StringDataSourceIdentifier {

	public KeggGeneID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.KEGG;
	}

}
