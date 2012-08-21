package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class ChemicalAbstractsServiceId extends StringDataSourceIdentifier {

	public ChemicalAbstractsServiceId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.CHEMICAL_ABSTRACTS_SERVICE;
	}

}
