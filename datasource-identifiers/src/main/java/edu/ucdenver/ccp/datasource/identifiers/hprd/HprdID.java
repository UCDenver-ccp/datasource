package edu.ucdenver.ccp.datasource.identifiers.hprd;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class HprdID extends DataSourceIdentifier<String> {

	private static final String ID_PREFIX = "HPRD_";
	private static final String ID_PREFIX_REGEX = "HPRD_?:?";

	public HprdID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.HPRD;
	}

	@Override
	public String validate(String hprdID) throws IllegalArgumentException {
		if (hprdID != null) {
			String id = hprdID;
			if (StringUtil.startsWithRegex(id, ID_PREFIX_REGEX)) {
				id = StringUtil.removePrefixRegex(id, ID_PREFIX_REGEX);
				return "HPRD:" + id;
			}
			if (id.length() == 5 && id.matches(RegExPatterns.HAS_NUMBERS_ONLY)) {
				return "HPRD:" + id;
			}
		}

		throw new IllegalArgumentException(String.format("Invalid HPRD ID detected: %s", hprdID));
	}

	@Override
	public String toString() {
		return ID_PREFIX + getDataElement().toString();
	}

}
