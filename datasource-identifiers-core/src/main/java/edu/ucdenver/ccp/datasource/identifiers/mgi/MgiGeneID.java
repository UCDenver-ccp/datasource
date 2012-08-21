package edu.ucdenver.ccp.datasource.identifiers.mgi;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class MgiGeneID extends DataSourceIdentifier<String>{

	public MgiGeneID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MGI;
	}

	@Override
	public String validate(String mgiID) throws IllegalArgumentException {
		if (mgiID != null && mgiID.startsWith("MGI:") && mgiID.substring(4).matches(RegExPatterns.HAS_NUMBERS_ONLY))
			return mgiID;
		if (mgiID != null && mgiID.matches(RegExPatterns.HAS_NUMBERS_ONLY))
			return "MGI:" + mgiID;
		throw new IllegalArgumentException(String.format("Invalid MGI ID detected: %s", mgiID));
	}

}
