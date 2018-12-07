package edu.ucdenver.ccp.datasource.identifiers.factories;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblProteinID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblTranscriptID;

public class EnsemblIdentifierFactory {

	public static DataSourceIdentifier<?> createEnsemblIdentifier(String id) {
		if (id.matches("ENS[^\\d]*P\\d+")) {
			return new EnsemblProteinID(id);
		}
		if (id.matches("ENS[^\\d]*T\\d+")) {
			return new EnsemblTranscriptID(id);
		}
		if (id.matches("ENS[^\\d]*G\\d+")) {
			return new EnsemblGeneID(id);
		}
		throw new IllegalArgumentException("Unknown Ensembl identifier format: " + id);
	}
}
