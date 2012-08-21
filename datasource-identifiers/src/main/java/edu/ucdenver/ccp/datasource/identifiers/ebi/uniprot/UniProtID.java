package edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class UniProtID extends DataSourceIdentifier<String> {

	public UniProtID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.UNIPROT;
	}

	/**
	 * See http://www.uniprot.org/manual/accession_numbers
	 */
	@Override
	public String validate(String uniprotID) throws IllegalArgumentException {
		String validUniProtFormatRegex_1 = "[A-NR-Z][0-9][A-Z][A-Z0-9][A-Z0-9][0-9]";
		String validUniProtFormatRegex_2 = "[OPQ][0-9][A-Z0-9][A-Z0-9][A-Z0-9][0-9]";
		if (uniprotID != null
				&& (uniprotID.matches(validUniProtFormatRegex_1) || uniprotID.matches(validUniProtFormatRegex_2)))
			return uniprotID;
		throw new IllegalArgumentException(getInvalidGeneIDErrorMessage(uniprotID));
	}

	private static String getInvalidGeneIDErrorMessage(String uniprotID) {
		return String.format("Invalid UniProt ID: %s. This ID does not comply with the specifications "
				+ "for UniProt accession numbers as defined here: http://www.uniprot.org/manual/accession_numbers",
				uniprotID);
	}
}
