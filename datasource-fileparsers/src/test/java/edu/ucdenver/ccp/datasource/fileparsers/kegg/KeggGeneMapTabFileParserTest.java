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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggPathwayID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGeneMapTabFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_KEGGMMUGENEMAPTAB_FILE_NAME = "KEGG_mmu_gene_map.tab";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_KEGGMMUGENEMAPTAB_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new KeggGeneMapTabFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			KeggGeneMapTabFileParser parser = new KeggGeneMapTabFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* 100039790 04020 04514 04612 04660 04940 05310 05320 05322 05330 05332 */
				KeggGeneMapTabFileData record1 = parser.next();
				assertEquals(new NcbiGeneId("100039790"), record1.getGeneID());
				assertEquals(10, record1.getKeggPathwayIDs().size());
				Set<KeggPathwayID> pathwayIDs = new HashSet<KeggPathwayID>();
				pathwayIDs.add(new KeggPathwayID("04020"));
				pathwayIDs.add(new KeggPathwayID("04514"));
				pathwayIDs.add(new KeggPathwayID("04612"));
				pathwayIDs.add(new KeggPathwayID("04660"));
				pathwayIDs.add(new KeggPathwayID("04940"));
				pathwayIDs.add(new KeggPathwayID("05310"));
				pathwayIDs.add(new KeggPathwayID("05320"));
				pathwayIDs.add(new KeggPathwayID("05322"));
				pathwayIDs.add(new KeggPathwayID("05330"));
				pathwayIDs.add(new KeggPathwayID("05332"));
				assertEquals(pathwayIDs, record1.getKeggPathwayIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* 100040390 04120 05020 */
				KeggGeneMapTabFileData record2 = parser.next();
				assertEquals(new NcbiGeneId("100040390"), record2.getGeneID());
				assertEquals(2, record2.getKeggPathwayIDs().size());
				Set<KeggPathwayID> pathwayIDs = new HashSet<KeggPathwayID>();
				pathwayIDs.add(new KeggPathwayID("04120"));
				pathwayIDs.add(new KeggPathwayID("05020"));
				assertEquals(pathwayIDs, record2.getKeggPathwayIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* 100041140 03410 */
				KeggGeneMapTabFileData record3 = parser.next();
				assertEquals(new NcbiGeneId("100041140"), record3.getGeneID());
				assertEquals(1, record3.getKeggPathwayIDs().size());
				Set<KeggPathwayID> pathwayIDs = new HashSet<KeggPathwayID>();
				pathwayIDs.add(new KeggPathwayID("03410"));
				assertEquals(pathwayIDs, record3.getKeggPathwayIDs());
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
	public void testGetGeneID2KeggPathwayIDMap() throws Exception {
		Map<NcbiGeneId, Set<KeggPathwayID>> geneID2KeggPathwayIDMap = KeggGeneMapTabFileParser
				.getGeneID2KeggPathwayIDMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<NcbiGeneId, Set<KeggPathwayID>> expectedMap = new HashMap<NcbiGeneId, Set<KeggPathwayID>>();
		expectedMap.put(new NcbiGeneId("100039790"), CollectionsUtil.createSet(new KeggPathwayID("04020"),
				new KeggPathwayID("04514"), new KeggPathwayID("04612"), new KeggPathwayID("04660"), new KeggPathwayID(
						"04940"), new KeggPathwayID("05310"), new KeggPathwayID("05320"), new KeggPathwayID("05322"),
				new KeggPathwayID("05330"), new KeggPathwayID("05332")));
		expectedMap.put(new NcbiGeneId("100040390"),
				CollectionsUtil.createSet(new KeggPathwayID("04120"), new KeggPathwayID("05020")));
		expectedMap.put(new NcbiGeneId("100041140"), CollectionsUtil.createSet(new KeggPathwayID("03410")));

		assertEquals(expectedMap, geneID2KeggPathwayIDMap);

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.genome.jp/kegg/KeggGene2PathwayRecord> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_100039790_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_04612_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_04514_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_05310_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_05330_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_05322_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_04020_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_05332_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_05320_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_04940_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100039790> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_04660_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100040390> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.genome.jp/kegg/KeggGene2PathwayRecord> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100040390> <http://www.genome.jp/kegg/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_100040390_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100040390> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_05020_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100040390> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_04120_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100041140> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.genome.jp/kegg/KeggGene2PathwayRecord> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100041140> <http://www.genome.jp/kegg/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_100041140_ICE> .",
						"<http://www.genome.jp/kegg/KEGG_GENE2PATHWAY_RECORD_EG_100041140> <http://www.genome.jp/kegg/isLinkedToKeggPathwayICE> <http://www.genome.jp/kegg/KEGG_03410_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "kegg-gene2pathway-KEGG.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("kegg-gene2pathway.nt", 19);
		counts.put("kabob-meta-kegg-gene2pathway.nt", 6);
		return counts;
	}

}
