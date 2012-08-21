package edu.ucdenver.ccp.datasource.identifiers.obo;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class MolecularInteractionOntologyTermID extends OntologyID {

	public static final String MOLECULAR_INTERACTION_ONTOLOGY_ID_PREFIX = "MI:";
	
	
	public MolecularInteractionOntologyTermID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MI_ONTOLOGY;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.matches(MOLECULAR_INTERACTION_ONTOLOGY_ID_PREFIX + "\\d+"))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid MI Ontology Term ID detected: %s", resourceID));
	}

}
