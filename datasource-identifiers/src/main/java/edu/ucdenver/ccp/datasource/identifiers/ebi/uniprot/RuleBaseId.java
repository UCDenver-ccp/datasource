package edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * RuleBase is used as the main tool in the sequence database group at the EBI to apply automatic
 * annotation to unknown sequences. These IDs are seen in Trembl entries.
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RuleBaseId extends StringDataSourceIdentifier {

	public RuleBaseId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.RULEBASE;
	}

}
