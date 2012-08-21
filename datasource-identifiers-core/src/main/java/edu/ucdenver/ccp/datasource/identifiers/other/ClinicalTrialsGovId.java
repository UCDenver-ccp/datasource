package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class ClinicalTrialsGovId extends StringDataSourceIdentifier {

	public ClinicalTrialsGovId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.CLINICAL_TRIALS_GOV;
	}

}
