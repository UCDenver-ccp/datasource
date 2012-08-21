package edu.ucdenver.ccp.datasource.identifiers.drugbank;

import org.apache.commons.lang.math.NumberUtils;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

public class DrugBankID extends DataSourceIdentifier<String> {

	public DrugBankID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DRUGBANK;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && resourceID.startsWith("DB") && NumberUtils.isDigits(resourceID.substring(2)))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid DrugBank ID detected: %s", resourceID));
	}

}
