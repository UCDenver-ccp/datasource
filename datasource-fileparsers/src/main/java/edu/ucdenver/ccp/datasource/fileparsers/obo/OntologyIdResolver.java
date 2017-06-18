package edu.ucdenver.ccp.datasource.fileparsers.obo;

import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.OntologyID;

public class OntologyIdResolver {

	public static OntologyID resolveOntologyID(String ontologyIdStr) {
		if (ontologyIdStr.startsWith(GeneOntologyID.GENE_ONTOLOGY_ID_PREFIX))
			return new GeneOntologyID(ontologyIdStr);
		if (ontologyIdStr.startsWith(MolecularInteractionOntologyTermID.MOLECULAR_INTERACTION_ONTOLOGY_ID_PREFIX))
			return new MolecularInteractionOntologyTermID(ontologyIdStr);
		throw new IllegalArgumentException(String.format("Cannot resolve unknown ontology ID type: %s", ontologyIdStr));
	}

}
