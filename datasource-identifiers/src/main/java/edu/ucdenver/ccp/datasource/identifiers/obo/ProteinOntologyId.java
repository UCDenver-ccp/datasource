package edu.ucdenver.ccp.datasource.identifiers.obo;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class ProteinOntologyId extends StringDataSourceIdentifier {

	public ProteinOntologyId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PR;
	}

}
