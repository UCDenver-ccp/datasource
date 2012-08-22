package edu.ucdenver.ccp.fileparsers.obo;

import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.datasource.identifiers.obo.OntologyID;

public class OntologyIdResolver {

	public static OntologyID resolveOntologyID(String ontologyIdStr) {
		if (ontologyIdStr.startsWith(GeneOntologyID.GENE_ONTOLOGY_ID_PREFIX))
			return new GeneOntologyID(ontologyIdStr);
		if (ontologyIdStr.startsWith(MolecularInteractionOntologyTermID.MOLECULAR_INTERACTION_ONTOLOGY_ID_PREFIX))
			return new MolecularInteractionOntologyTermID(ontologyIdStr);
		throw new IllegalArgumentException(String.format("Cannot resolve unknown ontology ID type: %s", ontologyIdStr));
	}

}
