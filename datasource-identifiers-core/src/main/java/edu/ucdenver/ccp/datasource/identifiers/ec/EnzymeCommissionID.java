package edu.ucdenver.ccp.datasource.identifiers.ec;

import org.apache.commons.lang.StringUtils;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class EnzymeCommissionID extends DataSourceIdentifier<String> {

	public EnzymeCommissionID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.ENZYME_COMMISSION;
	}

	@Override
	public String validate(String ecNumber) throws IllegalArgumentException {
		String validatedEcNumber = ecNumber;
		/* remove trailing periods and hyphens*/
		while (validatedEcNumber.endsWith(".") || validatedEcNumber.endsWith("-") || validatedEcNumber.endsWith(" "))
			validatedEcNumber = StringUtil.removeLastCharacter(validatedEcNumber);
		String ecNumberPatternStr = String
				.format("(EC\\s)?\\d+(\\.%s)?(\\.%s)?(\\.%s)?", RegExPatterns.IS_NUMBER_OR_HYPHEN,
						RegExPatterns.IS_NUMBER_OR_HYPHEN, RegExPatterns.IS_NUMBER_OR_HYPHEN);
		if (validatedEcNumber.matches(ecNumberPatternStr))
			return validatedEcNumber;
		throw new IllegalArgumentException(String.format("Invalid EC number: %s", ecNumber));
	}
}
