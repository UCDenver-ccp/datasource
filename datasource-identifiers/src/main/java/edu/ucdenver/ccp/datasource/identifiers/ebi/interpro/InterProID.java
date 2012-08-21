package edu.ucdenver.ccp.datasource.identifiers.ebi.interpro;

import org.apache.commons.lang.math.NumberUtils;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class InterProID extends DataSourceIdentifier<String> {

	public InterProID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.INTERPRO;
	}

	/**
	 * InterPro IDs start with "IPR" and are followed by a series of numbers, e.g IPR000001
	 */
	@Override
	public String validate(String interProID) throws IllegalArgumentException {
		if (interProID != null && interProID.startsWith("IPR") && NumberUtils.isDigits(interProID.substring(3)))
			return interProID;
		throw new IllegalArgumentException(String.format("Invalid InterPro ID detected: %s", interProID));
	}

}
