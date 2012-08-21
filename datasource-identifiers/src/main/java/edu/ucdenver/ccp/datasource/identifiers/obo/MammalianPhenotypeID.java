package edu.ucdenver.ccp.datasource.identifiers.obo;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class MammalianPhenotypeID extends StringDataSourceIdentifier {

	public MammalianPhenotypeID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MAMMALIAN_PHENOTYPE;
	}

}
