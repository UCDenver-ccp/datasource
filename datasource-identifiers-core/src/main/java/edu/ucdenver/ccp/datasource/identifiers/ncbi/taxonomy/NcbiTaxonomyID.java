package edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy;

import java.util.Arrays;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

public class NcbiTaxonomyID extends IntegerDataSourceIdentifier {

	public static final NcbiTaxonomyID ALL = new NcbiTaxonomyID(Integer.MAX_VALUE);
	public static final NcbiTaxonomyID HOMO_SAPIENS = new NcbiTaxonomyID(9606);
	public static final NcbiTaxonomyID MUS_MUSCULUS = new NcbiTaxonomyID(10090);

	public NcbiTaxonomyID(Integer taxonomyID) {
		super(taxonomyID);
	}

	public NcbiTaxonomyID(String taxonomyID) {
		super(taxonomyID, Arrays.asList("TAXID:", "TAXON:"));
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.NCBI_TAXON;
	}
}
