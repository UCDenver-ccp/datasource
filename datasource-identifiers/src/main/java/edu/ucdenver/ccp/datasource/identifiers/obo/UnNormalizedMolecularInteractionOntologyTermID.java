package edu.ucdenver.ccp.datasource.identifiers.obo;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class UnNormalizedMolecularInteractionOntologyTermID extends MolecularInteractionOntologyTermID {

	public UnNormalizedMolecularInteractionOntologyTermID(String termName) {
		super(termName);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MI_ONTOLOGY;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && !resourceID.isEmpty())
			return resourceID;
		throw new IllegalArgumentException(
				"Detected invalid un-normalized MI Ontology Term Name. It cannot be null or empty.");
	}

}
