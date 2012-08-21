package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PubChemBioAssayId extends StringDataSourceIdentifier {

	public PubChemBioAssayId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PUBCHEM_BIOASSAY;
		
	}

}
