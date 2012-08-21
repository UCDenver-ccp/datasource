package edu.ucdenver.ccp.datasource.identifiers.ebi.interpro;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PrintsID extends StringDataSourceIdentifier {

	public PrintsID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PRINTS;
	}

}
