package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.other.AlfredId;
import edu.ucdenver.ccp.datasource.identifiers.other.CtdId;
import edu.ucdenver.ccp.datasource.identifiers.other.GenAtlasId;
import edu.ucdenver.ccp.datasource.identifiers.other.GeneCardId;
import edu.ucdenver.ccp.datasource.identifiers.other.HugeId;
import edu.ucdenver.ccp.datasource.identifiers.other.HumanCycGeneId;
import edu.ucdenver.ccp.datasource.identifiers.other.ModBaseId;
import edu.ucdenver.ccp.datasource.identifiers.other.MutDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.UcscGenomeBrowserId;

@Ignore("file header in test file no longer matches file downloaded from PharmGkb. Code has been updated but test has not.")
public class PharmGkbGeneFileParserTest extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "genes.tsv";
	}

	@Override
	protected RecordReader<PharmGkbGeneFileRecord> initSampleRecordReader() throws IOException {
		return new PharmGkbGeneFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		RecordReader<PharmGkbGeneFileRecord> reader = initSampleRecordReader();
		PharmGkbGeneFileRecord r = reader.next();
		assertEquals("PA100", r.getAccessionId().getDataElement());
		assertEquals(995, r.getEntrezGeneIds().iterator().next().getDataElement().intValue());
		assertEquals("ENSG00000158402", r.getEnsemblGeneId().getDataElement());
		assertEquals("cell division cycle 25 homolog C (S. pombe)", r.getName());
		assertEquals("CDC25C", r.getSymbol());

		Set<String> expectedAlternativeNames = new HashSet<String>();
		expectedAlternativeNames.add(new String("M-phase inducer phosphatase 3"));
		expectedAlternativeNames.add(new String("OTTHUMP00000159490"));
		expectedAlternativeNames.add(new String("OTTHUMP00000224012"));
		expectedAlternativeNames.add(new String("OTTHUMP00000224013"));
		expectedAlternativeNames.add(new String("OTTHUMP00000224016"));
		expectedAlternativeNames.add(new String("cell division cycle 25C"));
		expectedAlternativeNames.add(new String("dual specificity phosphatase CDC25C"));
		expectedAlternativeNames.add(new String("m-phase inducer phosphatase 3"));
		expectedAlternativeNames.add(new String("mitosis inducer CDC25"));
		expectedAlternativeNames.add(new String("phosphotyrosine phosphatase"));
		expectedAlternativeNames.add(new String("protein phosphatase 1, regulatory subunit 60"));

		assertEquals(expectedAlternativeNames, new HashSet<String>(r.getAlternativeNames()));

		Set<String> expectedAlternativeSymbols = new HashSet<String>();
		expectedAlternativeSymbols.add(new String("CDC25"));
		expectedAlternativeSymbols.add(new String("PPP1R60"));

		assertEquals(expectedAlternativeSymbols, new HashSet<String>(r.getAlternativeSymbols()));

		assertFalse(r.isVip());
		assertFalse(r.hasVariantAnnotation());

		Set<DataSourceIdentifier<?>> expectedCrossReferences = new HashSet<DataSourceIdentifier<?>>();
		expectedCrossReferences.add(new HumanCycGeneId("HS08286"));
		expectedCrossReferences.add(new AlfredId("LO016398B"));
		expectedCrossReferences.add(new CtdId("995"));
		expectedCrossReferences.add(new EnsemblGeneID("ENSG00000158402"));
		expectedCrossReferences.add(new EntrezGeneID("995"));
		expectedCrossReferences.add(new GenAtlasId("CDC25C"));
		expectedCrossReferences.add(new GeneCardId("CDC25C"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0000079"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0000087"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0004725"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0005622"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0005634"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0006470"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0007049"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0007088"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0007089"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0008283"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0016787"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0051301"));
		expectedCrossReferences.add(new HgncID("1727"));
		expectedCrossReferences.add(new HugeId("CDC25C"));
		expectedCrossReferences.add(new ModBaseId("P30307"));
		expectedCrossReferences.add(new MutDbId("CDC25C"));
		expectedCrossReferences.add(new OmimID("157680"));
		expectedCrossReferences.add(new RefSeqID("AC_000048"));
		expectedCrossReferences.add(new RefSeqID("AC_000137"));
		expectedCrossReferences.add(new RefSeqID("NC_000005"));
		expectedCrossReferences.add(new RefSeqID("NT_034772"));
		expectedCrossReferences.add(new RefSeqID("NW_001838952"));
		expectedCrossReferences.add(new RefSeqID("NW_922784"));
		expectedCrossReferences.add(new RefSeqID("NP_001781"));
		expectedCrossReferences.add(new RefSeqID("NP_073720"));
		expectedCrossReferences.add(new RefSeqID("NM_001790"));
		expectedCrossReferences.add(new RefSeqID("NM_022809"));
		expectedCrossReferences.add(new UcscGenomeBrowserId("NM_001790"));
		expectedCrossReferences.add(new UniProtID("P30307"));

		assertEquals(expectedCrossReferences, new HashSet<DataSourceIdentifier<?>>(r.getCrossReferences()));

		r = reader.next();
		assertEquals("PA101", r.getAccessionId().getDataElement());
		assertEquals(1017, r.getEntrezGeneIds().iterator().next().getDataElement().intValue());

		assertFalse(reader.hasNext());
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/pharmgkb/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "PA100_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/pharmgkb/PharmGkbGeneIce1> .",
						NS + "PA100_ICE> <http://www.pharmgkb.org/hasPharmGkbID> \"PA100\"@en .",
						NS
								+ "PA100_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.pharmgkb.org/PA100> .",
						NS
								+ "PA100_ICE> <http://www.pharmgkb.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_995_ICE> .",
						NS
								+ "PA100_ICE> <http://www.pharmgkb.org/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSG00000158402_ICE> .",
						NS
								+ "PA100_ICE> <http://www.pharmgkb.org/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P30307_ICE> .",
						NS
								+ "PA128394563_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/pharmgkb/PharmGkbGeneIce1> .",
						NS + "PA128394563_ICE> <http://www.pharmgkb.org/hasPharmGkbID> \"PA128394563\"@en .",
						NS
								+ "PA128394563_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.pharmgkb.org/PA128394563> .",
						NS
								+ "PA128394563_ICE> <http://www.pharmgkb.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_9881_ICE> .");
		Map<File, List<String>> file2ExpectedLinesMap = new HashMap<File, List<String>>();
		file2ExpectedLinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "pharmgkb-genes.nt"), lines);
		return file2ExpectedLinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("pharmgkb-genes.nt", 10);
		counts.put("kabob-meta-pharmgkb-genes.nt", 6);
		return counts;
	}

}
