package edu.ucdenver.ccp.datasource.fileparsers.ncbi.homologene;

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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HomologeneGroupID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class HomoloGeneDataFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_HOMOLOGENE_DATA_FILE_NAME = "homologene.data";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_HOMOLOGENE_DATA_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new HomoloGeneDataFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			HomoloGeneDataFileParser parser = new HomoloGeneDataFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/*
				 * 3 [tab] 6239 [tab] 181758 [tab] acdh-7 [tab] 17570075 [tab] NP_510789.1
				 */
				HomoloGeneDataFileData record = parser.next();
				assertEquals(new HomologeneGroupID(3), record.getHomologeneGroupID());
				assertEquals(new NcbiTaxonomyID(6239), record.getTaxonomyID());
				assertEquals(new NcbiGeneId(181758), record.getEntrezGeneID());
				assertEquals(new String("acdh-7"), record.getGeneSymbol());
				assertEquals(new GiNumberID(17570075), record.getProteinGI());
				assertEquals(new RefSeqID("NP_510789.1"), record.getProteinAccession());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 5 [tab] 9606 [tab] 37 [tab] ACADVL [tab] 4557235 [tab] NP_000009.1
				 */
				HomoloGeneDataFileData record = parser.next();
				assertEquals(new HomologeneGroupID(5), record.getHomologeneGroupID());
				assertEquals(new NcbiTaxonomyID(9606), record.getTaxonomyID());
				assertEquals(new NcbiGeneId(37), record.getEntrezGeneID());
				assertEquals(new String("ACADVL"), record.getGeneSymbol());
				assertEquals(new GiNumberID(4557235), record.getProteinGI());
				assertEquals(new RefSeqID("NP_000009.1"), record.getProteinAccession());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 5 [tab] 9615 [tab] 489463 [tab] ACADVL [tab] 73955386 [tab] XP_546581.2
				 */
				HomoloGeneDataFileData record = parser.next();
				assertEquals(new HomologeneGroupID(5), record.getHomologeneGroupID());
				assertEquals(new NcbiTaxonomyID(9615), record.getTaxonomyID());
				assertEquals(new NcbiGeneId(489463), record.getEntrezGeneID());
				assertEquals(new String("ACADVL"), record.getGeneSymbol());
				assertEquals(new GiNumberID(73955386), record.getProteinGI());
				assertEquals(new RefSeqID("XP_546581.2"), record.getProteinAccession());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 121481 [tab] 10090 [tab] 665823 [tab] EG665823 [tab] 94386017 [tab] XP_984804.1
				 */
				HomoloGeneDataFileData record = parser.next();
				assertEquals(new HomologeneGroupID(121481), record.getHomologeneGroupID());
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonomyID());
				assertEquals(new NcbiGeneId(665823), record.getEntrezGeneID());
				assertEquals(new String("EG665823"), record.getGeneSymbol());
				assertEquals(new GiNumberID(94386017), record.getProteinGI());
				assertEquals(new RefSeqID("XP_984804.1"), record.getProteinAccession());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	@Test
	public void testGetEntrezGeneID2HomologeneGroupIDMap() throws Exception {
		Map<NcbiGeneId, HomologeneGroupID> entrezGeneID2HomologeneGroupIDMap = HomoloGeneDataFileParser
				.getEntrezGeneID2HomologeneGroupIDMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<NcbiGeneId, HomologeneGroupID> expectedEntrezGeneID2HomologeneGroupIDMap = new HashMap<NcbiGeneId, HomologeneGroupID>();
		expectedEntrezGeneID2HomologeneGroupIDMap.put(new NcbiGeneId(181758), new HomologeneGroupID(3));
		expectedEntrezGeneID2HomologeneGroupIDMap.put(new NcbiGeneId(37), new HomologeneGroupID(5));
		expectedEntrezGeneID2HomologeneGroupIDMap.put(new NcbiGeneId(489463), new HomologeneGroupID(5));
		expectedEntrezGeneID2HomologeneGroupIDMap.put(new NcbiGeneId(665823), new HomologeneGroupID(121481));

		/* Maps should be identical */
		assertEquals(expectedEntrezGeneID2HomologeneGroupIDMap, entrezGeneID2HomologeneGroupIDMap);

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_3EG_181758> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/homologene/HomologeneRecord> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_3EG_181758> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToHomologeneGroupICE> <http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_3_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_3EG_181758> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_181758_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_3EG_181758> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToGiNumberICE> <http://www.ncbi.nlm.nih.gov/genbank/GI_17570075_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_3EG_181758> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NP_510789.1_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_37> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/homologene/HomologeneRecord> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_37> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToHomologeneGroupICE> <http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_37> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_37_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_37> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToGiNumberICE> <http://www.ncbi.nlm.nih.gov/genbank/GI_4557235_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_37> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NP_000009.1_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_489463> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/homologene/HomologeneRecord> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_489463> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToHomologeneGroupICE> <http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_489463> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_489463_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_489463> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToGiNumberICE> <http://www.ncbi.nlm.nih.gov/genbank/GI_73955386_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_5EG_489463> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/XP_546581.2_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_121481EG_665823> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/homologene/HomologeneRecord> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_121481EG_665823> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToHomologeneGroupICE> <http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_121481_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_121481EG_665823> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_665823_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_121481EG_665823> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToGiNumberICE> <http://www.ncbi.nlm.nih.gov/genbank/GI_94386017_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/homologene/HOMOLOGENE_121481EG_665823> <http://www.ncbi.nlm.nih.gov/homologene/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/XP_984804.1_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "homologene-all.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("homologene-all.nt", 20);
		counts.put("kabob-meta-homologene-all.nt", 6);
		return counts;
	}

}
