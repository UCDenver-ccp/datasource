package edu.ucdenver.ccp.datasource.identifiers.transfac;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class TransfacMatrixID extends StringDataSourceIdentifier {

	public TransfacMatrixID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.TRANSFAC;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.startsWith("M") && resourceID.substring(1).matches(RegExPatterns.HAS_NUMBERS_ONLY))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid Transfac Matrix ID detected: %s", resourceID));
	}

}
