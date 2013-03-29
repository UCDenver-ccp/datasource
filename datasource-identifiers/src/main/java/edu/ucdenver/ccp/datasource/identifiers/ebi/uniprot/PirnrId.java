package edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * PIRSR (site rule) and PIRNR (name rule) are annotation rules based on PIRSF (protein families
 * with full-length sequence similarity). These rules are manually curated to accurately and
 * automatically annotate protein sequences. PIRSRs annotate sequence features for sites, such as
 * catalytic site, and binding site. PIRNRs annotations may include protein name, keywords, and
 * general annotation, such as function, subcellular location, and pathway.
 * 
 * For more information please go to http://pir.georgetown.edu/pirsf/.
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class PirnrId extends StringDataSourceIdentifier {

	public PirnrId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.UNIPROT_PREDICTION;
	}

}
