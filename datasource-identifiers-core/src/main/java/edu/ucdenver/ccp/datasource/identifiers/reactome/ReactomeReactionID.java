package edu.ucdenver.ccp.datasource.identifiers.reactome;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class ReactomeReactionID extends StringDataSourceIdentifier {

	public ReactomeReactionID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.REACTOME;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.startsWith("REACT_") && resourceID.substring(6).matches(RegExPatterns.HAS_NUMBERS_ONLY))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid Reactome Reaction ID detected: %s", resourceID));
	}

}
