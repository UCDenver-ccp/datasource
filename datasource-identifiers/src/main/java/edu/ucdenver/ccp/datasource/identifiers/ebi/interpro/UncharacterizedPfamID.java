package edu.ucdenver.ccp.datasource.identifiers.ebi.interpro;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class UncharacterizedPfamID extends StringDataSourceIdentifier {

	public UncharacterizedPfamID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.UNCHAR_PFAM;
	}

}
