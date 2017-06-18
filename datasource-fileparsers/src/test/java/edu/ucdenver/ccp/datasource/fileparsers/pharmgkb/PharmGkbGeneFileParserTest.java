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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AlfredId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CtdId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GenAtlasId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneCardId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HugeId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HumanCycGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ModBaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MutDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

//@Ignore("file header in test file no longer matches file downloaded from PharmGkb. Code has been updated but test has not.")
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
		assertEquals("PA100", r.getAccessionId().getId());
		assertEquals(1, r.getEntrezGeneIds().iterator().next().getId().intValue());
		assertEquals("ENSG00000121410", r.getEnsemblGeneId().getId());
		assertEquals("alpha-1-B glycoprotein", r.getName());
		assertEquals("A1BG", r.getSymbol());

		Set<String> expectedAlternativeNames = new HashSet<String>();
		assertEquals(expectedAlternativeNames, new HashSet<String>(r.getAlternativeNames()));

		Set<String> expectedAlternativeSymbols = new HashSet<String>();
		assertEquals(expectedAlternativeSymbols, new HashSet<String>(r.getAlternativeSymbols()));

		assertFalse(r.isVip());
		assertFalse(r.hasVariantAnnotation());

		Set<DataSourceIdentifier<?>> expectedCrossReferences = new HashSet<DataSourceIdentifier<?>>();
		expectedCrossReferences.add(new NcbiGeneId(1));
		expectedCrossReferences.add(new OmimID("138670"));
		expectedCrossReferences.add(new UcscGenomeBrowserId("NM_130786"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0000004"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0005554"));
		expectedCrossReferences.add(new GeneOntologyID("GO:0005576"));
		expectedCrossReferences.add(new RefSeqID("NM_130786"));
		expectedCrossReferences.add(new RefSeqID("NP_570602"));
		expectedCrossReferences.add(new RefSeqID("AC_000062"));
		expectedCrossReferences.add(new RefSeqID("AC_000151"));
		expectedCrossReferences.add(new RefSeqID("NC_000019"));
		expectedCrossReferences.add(new RefSeqID("NT_011109"));
		expectedCrossReferences.add(new RefSeqID("NW_001838501"));
		expectedCrossReferences.add(new RefSeqID("NW_927284"));
		expectedCrossReferences.add(new UniProtID("P04217"));
		expectedCrossReferences.add(new EnsemblGeneID("ENSG00000121410"));
		expectedCrossReferences.add(new GenAtlasId("A1BG"));
		expectedCrossReferences.add(new GeneCardId("A1BG"));
		expectedCrossReferences.add(new MutDbId("A1BG"));
		expectedCrossReferences.add(new AlfredId("LO098153A"));
		expectedCrossReferences.add(new HugeId("A1BG"));
		expectedCrossReferences.add(new CtdId("1"));
		expectedCrossReferences.add(new ModBaseId("P04217"));
		expectedCrossReferences.add(new HumanCycGeneId("HS04495"));
		expectedCrossReferences.add(new HgncID("5"));
		assertEquals(expectedCrossReferences, new HashSet<DataSourceIdentifier<?>>(r.getCrossReferences()));

		assertEquals("chr19", r.getChromosome());
		assertEquals(58855172, r.getChromosomalStart().intValue());
		assertEquals(58874865, r.getChromosomalEnd().intValue());
		
		r = reader.next();
		assertEquals("PA12345678", r.getAccessionId().getId());
		assertEquals(45345, r.getEntrezGeneIds().iterator().next().getId().intValue());

		assertFalse(reader.hasNext());
	}

//	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
//		final String NS = "<http://kabob.ucdenver.edu/ice/pharmgkb/";
//		List<String> lines = CollectionsUtil
//				.createList(
//						NS
//								+ "PA100_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/pharmgkb/PharmGkbGeneIce1> .",
//						NS + "PA100_ICE> <http://www.pharmgkb.org/hasPharmGkbID> \"PA100\"@en .",
//						NS
//								+ "PA100_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.pharmgkb.org/PA100> .",
//						NS
//								+ "PA100_ICE> <http://www.pharmgkb.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_995_ICE> .",
//						NS
//								+ "PA100_ICE> <http://www.pharmgkb.org/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSG00000158402_ICE> .",
//						NS
//								+ "PA100_ICE> <http://www.pharmgkb.org/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P30307_ICE> .",
//						NS
//								+ "PA128394563_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/pharmgkb/PharmGkbGeneIce1> .",
//						NS + "PA128394563_ICE> <http://www.pharmgkb.org/hasPharmGkbID> \"PA128394563\"@en .",
//						NS
//								+ "PA128394563_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.pharmgkb.org/PA128394563> .",
//						NS
//								+ "PA128394563_ICE> <http://www.pharmgkb.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_9881_ICE> .");
//		Map<File, List<String>> file2ExpectedLinesMap = new HashMap<File, List<String>>();
//		file2ExpectedLinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "pharmgkb-genes.nt"), lines);
//		return file2ExpectedLinesMap;
//	}
//
//	protected Map<String, Integer> getExpectedFileStatementCounts() {
//		Map<String, Integer> counts = new HashMap<String, Integer>();
//		counts.put("pharmgkb-genes.nt", 10);
//		counts.put("kabob-meta-pharmgkb-genes.nt", 6);
//		return counts;
//	}

}
