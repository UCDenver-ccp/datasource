package edu.ucdenver.ccp.identifier.publication;

import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class DOI extends StringDataSourceIdentifier {

	public DOI(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DOI;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.startsWith("DOI:"))
			resourceID = StringUtil.removePrefix(resourceID, "DOI:");
		return resourceID;
	}

}
