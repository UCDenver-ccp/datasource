package edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot;

import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class UniProtIsoformID extends StringDataSourceIdentifier {

	public UniProtIsoformID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.UNIPROT;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		String[] toks = resourceID.split(StringConstants.HYPHEN_MINUS);
		IllegalArgumentException invalidUniProtException = null;
		if (toks.length == 2) {
			if (StringUtil.isNonNegativeInteger(toks[1]))
				try {
					new UniProtID(toks[0]);
				} catch (IllegalArgumentException iae) {
					invalidUniProtException = iae;
				}
			if (invalidUniProtException == null)
				return resourceID;
		}
		String errorMessage = String.format("Invalid UniProt Isoform ID: %s. This ID must consist of a "
				+ "valid UniProt ID followed by a hyphen and then an integer.", resourceID);
		if (invalidUniProtException == null)
			throw new IllegalArgumentException(errorMessage);
		throw new IllegalArgumentException(errorMessage, invalidUniProtException);

	}

}
