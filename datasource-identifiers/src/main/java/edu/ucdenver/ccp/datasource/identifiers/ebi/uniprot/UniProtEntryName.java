package edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

public class UniProtEntryName extends DataSourceIdentifier<String> {

	public UniProtEntryName(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.UNIPROT;
	}

	@Override
	public String validate(String entryName) throws IllegalArgumentException {
		if (entryName != null && entryName.contains("_"))
			return entryName;
		throw new IllegalArgumentException(String.format("Invalid UniProt Entry Name detected: %s", entryName));
	}

}
