package edu.ucdenver.ccp.datasource.identifiers.ncbi.snp;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * Reference SNP ID
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SnpRsId extends DataSourceIdentifier<String> {

	public SnpRsId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DBSNP;
	}

	/**
	 * See http://www.uniprot.org/manual/accession_numbers
	 */
	@Override
	public String validate(String snpId) throws IllegalArgumentException {
		if (snpId != null && (snpId.matches("rs\\d+")))
			return snpId;
		throw new IllegalArgumentException(getInvalidGeneIDErrorMessage(snpId));
	}

	private static String getInvalidGeneIDErrorMessage(String snpId) {
		return String.format("Invalid reference SNP ID: %s.", snpId);
	}
}
