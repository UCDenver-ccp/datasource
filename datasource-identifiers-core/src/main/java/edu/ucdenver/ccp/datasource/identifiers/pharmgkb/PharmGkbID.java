package edu.ucdenver.ccp.datasource.identifiers.pharmgkb;

import org.apache.commons.lang.math.NumberUtils;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class PharmGkbID extends DataSourceIdentifier<String> {

	public PharmGkbID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PHARMGKB;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && resourceID.startsWith("PA") && NumberUtils.isDigits(resourceID.substring(2)))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid PharmGKB ID detected: %s", resourceID));
	}

}
