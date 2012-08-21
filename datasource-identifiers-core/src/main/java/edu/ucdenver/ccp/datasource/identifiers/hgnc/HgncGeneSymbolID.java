package edu.ucdenver.ccp.datasource.identifiers.hgnc;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * This symbol is a unique identifier for human genes within HGNC
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public class HgncGeneSymbolID extends DataSourceIdentifier<String> {

	public HgncGeneSymbolID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.HGNC;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		return resourceID;
	}

}
