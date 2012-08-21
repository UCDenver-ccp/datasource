package edu.ucdenver.ccp.identifier.publication;

import java.util.Arrays;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class PubMedID extends IntegerDataSourceIdentifier {

	public PubMedID(Integer pubmedID) {
		super(pubmedID);
	}

	/**
	 * Constructor that handles string argument that may start with "PMID:" prefix.
	 * 
	 * @param pubmedID
	 */
	public PubMedID(String pubmedID) {
		super(pubmedID, Arrays.asList("PMID:"));
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.PM;
	}
}
