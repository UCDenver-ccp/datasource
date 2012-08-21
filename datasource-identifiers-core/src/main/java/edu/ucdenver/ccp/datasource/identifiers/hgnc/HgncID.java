package edu.ucdenver.ccp.datasource.identifiers.hgnc;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class HgncID extends DataSourceIdentifier<String>{

	public HgncID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.HGNC;
	}

	@Override
	public String validate(String geneID) throws IllegalArgumentException {
		if (geneID != null && geneID.startsWith("HGNC:") && geneID.substring(5).matches(RegExPatterns.HAS_NUMBERS_ONLY)) 
			return geneID;
		if (geneID != null && geneID.matches(RegExPatterns.HAS_NUMBERS_ONLY))
			return "HGNC:" + geneID;
		throw new IllegalArgumentException(getInvalidGeneIDErrorMessage(geneID == null ? null : geneID));
	}

	private static String getInvalidGeneIDErrorMessage(String geneIDStr) {
		return String.format("Invalid HGNC Gene ID: %s. Must start with HGNC_ and end with only numbers.", geneIDStr);
	}
	
}
