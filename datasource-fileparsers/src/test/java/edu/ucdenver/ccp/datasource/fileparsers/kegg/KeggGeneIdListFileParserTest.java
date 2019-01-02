package edu.ucdenver.ccp.datasource.fileparsers.kegg;

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
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGeneIdListFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_KEGG_GENE_ID_LIST_FILE_NAME = "KEGG_aae_ncbi-geneid.list";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_KEGG_GENE_ID_LIST_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new KeggGeneIdListFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			KeggGeneIdListFileParser parser = new KeggGeneIdListFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* aae:aq_001 ncbi-geneid:1192533 */
				KeggGeneIdListFileData record1 = parser.next();
				assertEquals(new KeggGeneID("aq_001"), record1.getKeggGeneID());
				assertEquals(new NcbiGeneId("1192533"), record1.getExternalGeneID());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* aae:aq_005 ncbi-geneid:1192534 */
				KeggGeneIdListFileData record2 = parser.next();
				assertEquals(new KeggGeneID("aq_005"), record2.getKeggGeneID());
				assertEquals(new NcbiGeneId("1192534"), record2.getExternalGeneID());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* aae:aq_008 ncbi-geneid:1192535 */
				KeggGeneIdListFileData record3 = parser.next();
				assertEquals(new KeggGeneID("aq_008"), record3.getKeggGeneID());
				assertEquals(new NcbiGeneId("1192535"), record3.getExternalGeneID());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* aae:aq_008 ncbi-geneid:1192535 */
				KeggGeneIdListFileData record3 = parser.next();
				assertEquals(new KeggGeneID("aq_008"), record3.getKeggGeneID());
				assertEquals(new NcbiGeneId("1234567"), record3.getExternalGeneID());
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
	public void testGetInternal2ExternalGeneIDMap() throws Exception {
		Map<KeggGeneID, Set<DataSourceIdentifier<?>>> keggInternal2ExternalGeneIDMap = KeggGeneIdListFileParser
				.getInternal2ExternalGeneIDMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<KeggGeneID, Set<NcbiGeneId>> expectedMap = new HashMap<KeggGeneID, Set<NcbiGeneId>>();
		expectedMap.put(new KeggGeneID("aq_001"), CollectionsUtil.createSet(new NcbiGeneId("1192533")));
		expectedMap.put(new KeggGeneID("aq_005"), CollectionsUtil.createSet(new NcbiGeneId("1192534")));
		expectedMap.put(new KeggGeneID("aq_008"),
				CollectionsUtil.createSet(new NcbiGeneId("1192535"), new NcbiGeneId("1234567")));

		assertEquals(expectedMap, keggInternal2ExternalGeneIDMap);
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/kegg/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "KEGG_GENE_aq_001_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/kegg/KeggGeneIce1> .",
						NS + "KEGG_GENE_aq_001_ICE> <http://www.genome.jp/kegg/hasKeggGeneID> \"aq_001\"@en .",
						NS
								+ "KEGG_GENE_aq_001_ICE> <http://www.genome.jp/kegg/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_1192533_ICE> .",
						NS
								+ "KEGG_GENE_aq_005_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/kegg/KeggGeneIce1> .",
						NS + "KEGG_GENE_aq_005_ICE> <http://www.genome.jp/kegg/hasKeggGeneID> \"aq_005\"@en .",
						NS
								+ "KEGG_GENE_aq_005_ICE> <http://www.genome.jp/kegg/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_1192534_ICE> .",
						NS
								+ "KEGG_GENE_aq_008_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/kegg/KeggGeneIce1> .",
						NS + "KEGG_GENE_aq_008_ICE> <http://www.genome.jp/kegg/hasKeggGeneID> \"aq_008\"@en .",
						NS
								+ "KEGG_GENE_aq_008_ICE> <http://www.genome.jp/kegg/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_1192535_ICE> .",
						NS
								+ "KEGG_GENE_aq_008_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/kegg/KeggGeneIce1> .",
						NS + "KEGG_GENE_aq_008_ICE> <http://www.genome.jp/kegg/hasKeggGeneID> \"aq_008\"@en .",
						NS
								+ "KEGG_GENE_aq_008_ICE> <http://www.genome.jp/kegg/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_1234567_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "kegg-genelist-KEGG.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("kegg-genelist.nt", 12);
		counts.put("kabob-meta-kegg-genelist.nt", 6);
		return counts;
	}

}
