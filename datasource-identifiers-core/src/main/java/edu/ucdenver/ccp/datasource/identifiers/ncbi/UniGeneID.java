package edu.ucdenver.ccp.datasource.identifiers.ncbi;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class UniGeneID extends StringDataSourceIdentifier {

	private static final String ID_PREFIX = "UNIGENE_";
	
	public UniGeneID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.UNIGENE;
	}

	@Override
	public String toString() {
		return ID_PREFIX + getDataElement().toString();
	}

}
