package edu.ucdenver.ccp.datasource.identifiers.kegg;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class KeggPathwayID extends StringDataSourceIdentifier {

	public static final String ID_PREFIX = "KEGG_PATHWAY_";
	
	public KeggPathwayID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.KEGG;
	}

	@Override
	public String getStringRepresentation() {
		return ID_PREFIX + getDataElement().toString();
	}
	
}
