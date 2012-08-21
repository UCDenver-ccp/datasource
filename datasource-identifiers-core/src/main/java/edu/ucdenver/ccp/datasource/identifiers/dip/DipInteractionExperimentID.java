package edu.ucdenver.ccp.datasource.identifiers.dip;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

public class DipInteractionExperimentID extends StringDataSourceIdentifier {

	public DipInteractionExperimentID(PubMedID pmid, MolecularInteractionOntologyTermID detectionMethod) {
		super(((pmid != null) ? pmid.toString() : "UNKNOWN") + detectionMethod.toString());
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DIP;
	}

}
