package edu.ucdenver.ccp.datasource.fileparsers.hprd;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class HprdIdMappingsTxtFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_HprdIdMappingsTxt_FILE_NAME = "HPRD_ID_MAPPINGS.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_HprdIdMappingsTxt_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new HprdIdMappingsTxtFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			HprdIdMappingsTxtFileParser parser = new HprdIdMappingsTxtFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* 00001 ALDH1A1 NM_000689.3 NP_000680.2 216 0 P00352 Aldehyde dehydrogenase 1 */
				HprdIdMappingsTxtFileData record = parser.next();
				assertEquals(new HprdID("00001"), record.getHprdID());
				assertEquals("ALDH1A1", record.getGeneSymbol());
				assertEquals(new RefSeqID("NM_000689.3"), record.getNucleotideAccession());
				assertEquals(new RefSeqID("NP_000680.2"), record.getProteinAccession());
				assertEquals(new NcbiGeneId(216), record.getEntrezGeneID());
				assertNull(record.getOmimID());
				assertEquals(CollectionsUtil.createList(new UniProtID("P00352")), record.getSwissProtIDs());
				assertEquals("Aldehyde dehydrogenase 1", record.getMainName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 19429 FXYD8 NM_001099278.1 NP_001092748.1 406875 - P58550 FXYD domain containing
				 * ion transport regulator 8
				 */
				HprdIdMappingsTxtFileData record = parser.next();
				assertEquals(new HprdID("19429"), record.getHprdID());
				assertEquals("FXYD8", record.getGeneSymbol());
				assertEquals(new RefSeqID("NM_001099278.1"), record.getNucleotideAccession());
				assertEquals(new RefSeqID("NP_001092748.1"), record.getProteinAccession());
				assertEquals(new NcbiGeneId(406875), record.getEntrezGeneID());
				assertNull(record.getOmimID());
				assertEquals(CollectionsUtil.createList(new UniProtID("P58550")), record.getSwissProtIDs());
				assertEquals("FXYD domain containing ion transport regulator 8", record.getMainName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* 19398 - NM_001039496.1 NP_001034585.1 25858 - - DKFZP566E164 protein */
				HprdIdMappingsTxtFileData record = parser.next();
				assertEquals(new HprdID("19398"), record.getHprdID());
				assertNull(record.getGeneSymbol());
				assertEquals(new RefSeqID("NM_001039496.1"), record.getNucleotideAccession());
				assertEquals(new RefSeqID("NP_001034585.1"), record.getProteinAccession());
				assertEquals(new NcbiGeneId(25858), record.getEntrezGeneID());
				assertNull(record.getOmimID());
				assertEquals(new ArrayList<UniProtID>(), record.getSwissProtIDs());
				assertEquals("DKFZP566E164 protein", record.getMainName());

			} else {
				fail("Parser should have returned a record here.");
			}
			
			if (parser.hasNext()) {
				/* 01828	TGFB2	M19154.1	AAA50404.1	7042	190220	P61812,Q59EG9	TGF beta 2 */
				HprdIdMappingsTxtFileData record = parser.next();
				assertEquals(new HprdID("01828"), record.getHprdID());
				assertEquals("TGFB2", record.getGeneSymbol());
				assertEquals(new GenBankID("M19154.1"), record.getNucleotideAccession());
				assertEquals(new GenBankID("AAA50404.1"), record.getProteinAccession());
				assertEquals(new NcbiGeneId(7042), record.getEntrezGeneID());
				assertEquals(new OmimID(190220), record.getOmimID());
				assertEquals(CollectionsUtil.createList(new UniProtID("P61812"), new UniProtID("Q59EG9")), record.getSwissProtIDs());
				assertEquals("TGF beta 2", record.getMainName());

			} else {
				fail("Parser should have returned a record here.");
			}
			

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/hprd/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "HPRD_00001_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/hprd/HprdIce1> .",
						NS
								+ "HPRD_00001_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.hprd.org/HPRD_00001> .",
						NS
								+ "HPRD_00001_ICE> <http://www.hprd.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NM_000689.3_ICE> .",
						NS
								+ "HPRD_00001_ICE> <http://www.hprd.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NP_000680.2_ICE> .",
						NS
								+ "HPRD_00001_ICE> <http://www.hprd.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_216_ICE> .",
						NS
								+ "HPRD_00001_ICE> <http://www.hprd.org/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P00352_ICE> .",
						NS
								+ "HPRD_19429_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/hprd/HprdIce1> .",
						NS
								+ "HPRD_19429_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.hprd.org/HPRD_19429> .",
						NS
								+ "HPRD_19429_ICE> <http://www.hprd.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NM_001099278.1_ICE> .",
						NS
								+ "HPRD_19429_ICE> <http://www.hprd.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NP_001092748.1_ICE> .",
						NS
								+ "HPRD_19429_ICE> <http://www.hprd.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_406875_ICE> .",
						NS
								+ "HPRD_19429_ICE> <http://www.hprd.org/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P58550_ICE> .",
						NS
								+ "HPRD_19398_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/hprd/HprdIce1> .",
						NS
								+ "HPRD_19398_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.hprd.org/HPRD_19398> .",
						NS
								+ "HPRD_19398_ICE> <http://www.hprd.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NM_001039496.1_ICE> .",
						NS
								+ "HPRD_19398_ICE> <http://www.hprd.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NP_001034585.1_ICE> .",
						NS
								+ "HPRD_19398_ICE> <http://www.hprd.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_25858_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "hprd-idmappings.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("hprd-idmappings.nt", 17);
		counts.put("kabob-meta-hprd-idmappings.nt", 6);
		return counts;
	}
}
