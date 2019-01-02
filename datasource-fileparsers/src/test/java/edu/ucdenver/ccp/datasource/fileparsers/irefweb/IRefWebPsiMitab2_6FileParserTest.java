package edu.ucdenver.ccp.datasource.fileparsers.irefweb;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BindInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebCrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebCrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIcrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIcrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebRigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebRogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class IRefWebPsiMitab2_6FileParserTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "IRefWeb_10090.mitab.MMDDYYYY.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected IRefWebPsiMitab2_6FileParser initSampleRecordReader() throws IOException {
		return new IRefWebPsiMitab2_6FileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		IRefWebPsiMitab2_6FileParser parser = initSampleRecordReader();
		if (parser.hasNext()) {
			/*
			 * uniprotkb:P38276 uniprotkb:P38276
			 * uniprotkb:P38276|refseq:NP_009695|entrezgene/locuslink
			 * :852434|rogid:UsO9ZYVJXLI50JBd/g0C1NtSeXI559292|irogid:16835195
			 * uniprotkb:P38276|refseq
			 * :NP_009695|entrezgene/locuslink:852434|rogid
			 * :UsO9ZYVJXLI50JBd/g0C1NtSeXI559292 |irogid:16835195
			 * uniprotkb:YBY7_YEAST
			 * |entrezgene/locuslink:YBR137W|crogid:UsO9ZYVJXLI50JBd
			 * /g0C1NtSeXI559292|icrogid:16835195
			 * uniprotkb:YBY7_YEAST|entrezgene/locuslink:YBR137W|crogid
			 * :UsO9ZYVJXLI50JBd/g0C1NtSeXI559292|icrogid:16835195 MI:0018(2
			 * hybrid) - pubmed:10655498 taxid:559292(Saccharomyces cerevisiae
			 * S288c) taxid:559292(Saccharomyces cerevisiae S288c) -
			 * MI:0000(BIND_Translation)
			 * BIND_Translation:1261|rigid:+++94o2VtVJcuk6jD3H2JZXaVYc
			 * |irigid:617101|edgetype:X lpr:4518|hpr:5191|np:2 none
			 * MI:0000(unspecified) MI:0000(unspecified) MI:0000(unspecified)
			 * MI:0000(unspecified) MI:0326(protein) MI:0326(protein) - - - - -
			 * - - - 2010/05/18 2010/05/18
			 * rogid:UsO9ZYVJXLI50JBd/g0C1NtSeXI559292
			 * rogid:UsO9ZYVJXLI50JBd/g0C1NtSeXI559292
			 * rigid:+++94o2VtVJcuk6jD3H2JZXaVYc false refseq:NP_009695
			 * refseq:NP_009695 refseq:NP_009695 refseq:NP_009695 P P 16835195
			 * 16835195 617101 UsO9ZYVJXLI50JBd/g0C1NtSeXI559292
			 * UsO9ZYVJXLI50JBd/g0C1NtSeXI559292 +++94o2VtVJcuk6jD3H2JZXaVYc
			 * 16835195 16835195 617101 - X 2
			 */
			parser.next();

		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {

			/*
			 * rogid:Ivetsb7L/rt8ds+TyhtJZKxTtVE9796 uniprotkb:P05132
			 * PDB:1YDT_I|PDB:1YDR_I|PDB:1YDS_I
			 * |PDB:1FMO_I|PDB:1STC_I|rogid:Ivetsb7L
			 * /rt8ds+TyhtJZKxTtVE9796|irogid:9981084 uniprotkb:
			 * P05132|refseq:NP_032880
			 * |entrezgene/locuslink:18747|rogid:HdW51RuiujpUxo0Fu8TbWz3Yk8c10090
			 * |irogid:2201887
			 * rogid:Ivetsb7L/rt8ds+TyhtJZKxTtVE9796|crogid:Ivetsb7L
			 * /rt8ds+TyhtJZKxTtVE9796 |icrogid:9981084|-
			 * uniprotkb:KAPCA_MOUSE|entrezgene
			 * /locuslink:Prkaca|crogid:HdW51RuiujpUxo0Fu8TbWz3Yk8c10090
			 * |icrogid:2201887 MI:0114(three-dimensional-structure) -
			 * pubmed:1862342 taxid:9796(Equus caballus) taxid:10090(Mus
			 * musculus) - MI:0462(bind)
			 * bind:76262|rigid:++f9f/9TQhDLvdrGu56SalIhHSA
			 * |irigid:617146|edgetype:X lpr:1|hpr:6|np:6 none
			 * MI:0000(unspecified) MI:0000(unspecified) MI:0000(unspecified)
			 * MI:0000(unspecified) MI:0326(protein) MI:0326(protein) - - - - -
			 * - - - 2010/05/18 2010/05/18 rogid:Ivetsb7L/rt8ds+TyhtJZKxTtVE9796
			 * rogid:HdW51RuiujpUxo0Fu8TbWz3Yk8c10090
			 * rigid:++f9f/9TQhDLvdrGu56SalIhHSA false GenBank:"1FMO_I"
			 * GenBank:NP_032880 PDB:1FMO_I refseq:NP_032880 PT P 9981084
			 * 2201887 617146 Ivetsb7L/rt8ds+TyhtJZKxTtVE9796
			 * HdW51RuiujpUxo0Fu8TbWz3Yk8c10090 ++f9f/9TQhDLvdrGu56SalIhHSA
			 * 9981084 2201887 617146 - X 2
			 */

			IRefWebPsiMitab2_6FileData record = parser.next();
			assertEquals(new IRefWebRogId("Ivetsb7L/rt8ds+TyhtJZKxTtVE9796"), record.getInteractorA().getUniqueId());
			assertEquals(new UniProtID("P05132"), record.getInteractorB().getUniqueId());

			Set<DataSourceIdentifier<?>> expectedAltIdsA = new HashSet<DataSourceIdentifier<?>>();
			expectedAltIdsA.add(new PdbID("1YDT_I"));
			expectedAltIdsA.add(new PdbID("1YDR_I"));
			expectedAltIdsA.add(new PdbID("1YDS_I"));
			expectedAltIdsA.add(new PdbID("1FMO_I"));
			expectedAltIdsA.add(new PdbID("1STC_I"));
			expectedAltIdsA.add(new IRefWebRogId("Ivetsb7L/rt8ds+TyhtJZKxTtVE9796"));
			expectedAltIdsA.add(new IRefWebIrogId("9981084"));
			assertEquals(expectedAltIdsA, record.getInteractorA().getAlternateIds());

			Set<DataSourceIdentifier<?>> expectedAltIdsB = new HashSet<DataSourceIdentifier<?>>();
			expectedAltIdsB.add(new UniProtID("P05132"));
			expectedAltIdsB.add(new RefSeqID("NP_032880"));
			expectedAltIdsB.add(new NcbiGeneId("18747"));
			expectedAltIdsB.add(new IRefWebRogId("HdW51RuiujpUxo0Fu8TbWz3Yk8c10090"));
			expectedAltIdsB.add(new IRefWebIrogId("2201887"));
			assertEquals(expectedAltIdsB, record.getInteractorB().getAlternateIds());

			Set<DataSourceIdentifier<?>> expectedAliasesA = new HashSet<DataSourceIdentifier<?>>();
			expectedAliasesA.add(new IRefWebRogId("Ivetsb7L/rt8ds+TyhtJZKxTtVE9796"));
			expectedAliasesA.add(new IRefWebCrogId("Ivetsb7L/rt8ds+TyhtJZKxTtVE9796"));
			expectedAliasesA.add(new IRefWebIcrogId("9981084"));
			assertEquals(expectedAliasesA, record.getInteractorA().getAliasIds());

			Set<DataSourceIdentifier<?>> expectedAliasesB = new HashSet<DataSourceIdentifier<?>>();
			expectedAliasesB.add(new UniProtEntryName("KAPCA_MOUSE"));
			expectedAliasesB.add(new IRefWebCrogId("HdW51RuiujpUxo0Fu8TbWz3Yk8c10090"));
			expectedAliasesB.add(new IRefWebIcrogId("2201887"));
			assertEquals(expectedAliasesB, record.getInteractorB().getAliasIds());

			assertEquals(CollectionsUtil.createSet("rogid:Ivetsb7L/rt8ds+TyhtJZKxTtVE9796",
					"crogid:Ivetsb7L/rt8ds+TyhtJZKxTtVE9796", "icrogid:9981084"), record.getInteractorA()
					.getAliasSymbols());

			Set<String> expectedAliasBSymbols = CollectionsUtil.createSet("crogid:HdW51RuiujpUxo0Fu8TbWz3Yk8c10090",
					"entrezgene/locuslink:Prkaca", "icrogid:2201887", "uniprotkb:KAPCA_MOUSE");
			assertEquals(expectedAliasBSymbols, record.getInteractorB().getAliasSymbols());

			assertEquals(new IRefWebInteractionDetectionMethod(new MolecularInteractionOntologyTermID("MI:0114"),
					"three-dimensional-structure"), record.getInteraction().getDetectionMethod());

			assertNull(record.getInteraction().getAuthor());

			assertEquals(CollectionsUtil.createSet(new PubMedID(1862342)), record.getInteraction().getPmids());

			assertEquals(new IRefWebInteractorOrganism(new NcbiTaxonomyID(9796), "Equus caballus"), record
					.getInteractorA().getNcbiTaxonomyId());
			assertEquals(new IRefWebInteractorOrganism(new NcbiTaxonomyID(10090), "Mus musculus"), record
					.getInteractorB().getNcbiTaxonomyId());

			assertNull(record.getInteraction().getInteractionType());

			assertEquals(
					new IRefWebInteractionSourceDatabase(new MolecularInteractionOntologyTermID("MI:0462"), "bind"),
					record.getSourceDb());

			Set<DataSourceIdentifier<?>> expectedInteractionDbIds = new HashSet<DataSourceIdentifier<?>>();
			expectedInteractionDbIds.add(new BindInteractionID("76262"));
			expectedInteractionDbIds.add(new IRefWebRigId("++f9f/9TQhDLvdrGu56SalIhHSA"));
			expectedInteractionDbIds.add(new IRefWebIrigId("617146"));
			assertEquals(expectedInteractionDbIds, record.getInteraction().getInteractionDbIds());

			Set<String> expectedConfidences = CollectionsUtil.createSet("lpr:1", "hpr:6", "np:6");
			assertEquals(expectedConfidences, record.getInteraction().getConfidence());

			assertEquals("none", record.getInteraction().getExpansion());

			assertEquals(new IRefWebInteractorBiologicalRole(new MolecularInteractionOntologyTermID("MI:0000"),
					"unspecified"), record.getInteractorA().getBiologicalRole());
			assertEquals(new IRefWebInteractorBiologicalRole(new MolecularInteractionOntologyTermID("MI:0000"),
					"unspecified"), record.getInteractorB().getBiologicalRole());
			assertEquals(new IRefWebInteractorExperimentalRole(new MolecularInteractionOntologyTermID("MI:0000"),
					"unspecified"), record.getInteractorA().getExperimentalRole());
			assertEquals(new IRefWebInteractorExperimentalRole(new MolecularInteractionOntologyTermID("MI:0000"),
					"unspecified"), record.getInteractorB().getExperimentalRole());

			assertEquals(new IRefWebInteractorType(new MolecularInteractionOntologyTermID("MI:0326"), "protein"),
					record.getInteractorA().getInteractorType());
			assertEquals(new IRefWebInteractorType(new MolecularInteractionOntologyTermID("MI:0326"), "protein"),
					record.getInteractorB().getInteractorType());

			assertNull(record.getInteraction().getHostOrgTaxonomyId());

			assertEquals("2010/05/18", record.getCreationDate());
			assertEquals("2010/05/18", record.getUpdateDate());

			assertEquals(new IRefWebRogId("Ivetsb7L/rt8ds+TyhtJZKxTtVE9796"), record.getInteractorA().getChecksum());
			assertEquals(new IRefWebRogId("HdW51RuiujpUxo0Fu8TbWz3Yk8c10090"), record.getInteractorB().getChecksum());
			assertEquals(new IRefWebRigId("++f9f/9TQhDLvdrGu56SalIhHSA"), record.getInteraction().getChecksumInteraction());

			assertFalse(record.getInteraction().isNegative());

			assertEquals(new ProbableErrorDataSourceIdentifier("GenBank:\"1FMO_I\"", null,
					"Input is not a known accession pattern: GenBank:\"1FMO_I\""), record.getInteractorA()
					.getOriginalReference());
			assertEquals(new RefSeqID("NP_032880"), record.getInteractorB().getOriginalReference());
			assertEquals(new PdbID("1FMO_I"), record.getInteractorA().getFinalReference());
			assertEquals(new RefSeqID("NP_032880"), record.getInteractorB().getFinalReference());

			assertEquals("PT", record.getInteractorA().getMappingScore());
			assertEquals("P", record.getInteractorB().getMappingScore());

			assertEquals(new IRefWebIrogId("9981084"), record.getInteractorA().getIrogid());
			assertEquals(new IRefWebIrogId("2201887"), record.getInteractorB().getIrogid());
			assertEquals(new IRefWebIrigId("617146"), record.getInteraction().getIrigid());

			assertEquals(new IRefWebCrogId("Ivetsb7L/rt8ds+TyhtJZKxTtVE9796"), record.getInteractorA().getCrogid());
			assertEquals(new IRefWebCrogId("HdW51RuiujpUxo0Fu8TbWz3Yk8c10090"), record.getInteractorB().getCrogid());
			assertEquals(new IRefWebCrigId("++f9f/9TQhDLvdrGu56SalIhHSA"), record.getInteraction().getCrigid());

			assertEquals(new IRefWebIcrogId("9981084"), record.getInteractorA().getIcrogid());
			assertEquals(new IRefWebIcrogId("2201887"), record.getInteractorB().getIcrogid());
			assertEquals(new IRefWebIcrigId("617146"), record.getInteraction().getIcrigid());

			assertNull(record.getInteraction().getImexId());

			assertEquals("X", record.getInteraction().getEdgeType());

			assertEquals(2, record.getInteraction().getNumParticipants());

		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {

			/*
				 */
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

}
