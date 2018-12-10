package edu.ucdenver.ccp.datasource.identifiers.factories;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblProteinID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblTranscriptID;

public class EnsemblIdentifierFactoryTest {

	@Test
	public void test() {
		DataSourceIdentifier<?> id = EnsemblIdentifierFactory.createEnsemblIdentifier("ENSG000000001234");
		assertEquals(EnsemblGeneID.class, id.getClass());
		id = EnsemblIdentifierFactory.createEnsemblIdentifier("ENSGALG00000004143");
		assertEquals(EnsemblGeneID.class, id.getClass());
		id = EnsemblIdentifierFactory.createEnsemblIdentifier("ENSGALT00000044506");
		assertEquals(EnsemblTranscriptID.class, id.getClass());
		id = EnsemblIdentifierFactory.createEnsemblIdentifier("ENSGALP00000039133");
		assertEquals(EnsemblProteinID.class, id.getClass());
	}

}
