package edu.ucdenver.ccp.datasource.identifiers.ebi.interpro;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PirSfID extends StringDataSourceIdentifier {

	public PirSfID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PIRSF;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.startsWith("PIRSF")
				&& resourceID.substring("PIRSF".length()).matches(RegExPatterns.HAS_NUMBERS_ONLY))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid PIR ID detected: %s.", (resourceID)));

	}

}
