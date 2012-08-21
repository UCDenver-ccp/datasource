package edu.ucdenver.ccp.datasource.identifiers.obo;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class GeneOntologyID extends OntologyID {

	public static final String GENE_ONTOLOGY_ID_PREFIX = "GO:";

	public GeneOntologyID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.GO;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.matches(String.format("%s\\d+", GENE_ONTOLOGY_ID_PREFIX)))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid GO Ontology Term ID detected: %s", resourceID));
	}
}
