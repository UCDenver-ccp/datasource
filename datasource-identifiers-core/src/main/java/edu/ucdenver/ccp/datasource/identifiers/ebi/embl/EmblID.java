package edu.ucdenver.ccp.datasource.identifiers.ebi.embl;

import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class EmblID extends DataSourceIdentifier<String>{

	public EmblID(String resourceID) {
		super(removeVersionIfPresent(resourceID));
	}

	
	/**
	 * EMBL IDs are sometimes listed as accession.version pairs, e.g. BAC39464.1.
	 * We currently ignore the version information because not all EMBL IDs
	 * are listed with one.  This method removes
	 * the sequence version from the EMBL ID if one is present.
	 * 
	 * @param resourceID
	 * @return
	 */
	private static String removeVersionIfPresent(String resourceID) {
		String versionRegex = "\\.\\d+";
		if (StringUtil.endsWithRegex(resourceID, versionRegex)) 
			return StringUtil.removeSuffixRegex(resourceID, versionRegex);
		return resourceID;
	}
	
	@Override
	public DataSource getDataSource() {
		return DataSource.EMBL;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && !resourceID.isEmpty())
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid Embl ID detected: %s", resourceID));
	}

}
