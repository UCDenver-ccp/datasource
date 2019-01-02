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

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggPathwayID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggMapTitleTabFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_KEGGMAPTITLETAB_FILE_NAME = "KEGG_map_title.tab";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_KEGGMAPTITLETAB_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new KeggMapTitleTabFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			KeggMapTitleTabFileParser parser = new KeggMapTitleTabFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* 00010 Glycolysis / Gluconeogenesis */
				KeggMapTitleTabFileData record1 = parser.next();
				assertEquals(new KeggPathwayID("00010"), record1.getKeggPathwayID());
				assertEquals(new String("Glycolysis / Gluconeogenesis"), record1.getKeggPathwayName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* 00020 Citrate cycle (TCA cycle) */
				KeggMapTitleTabFileData record2 = parser.next();
				assertEquals(new KeggPathwayID("00020"), record2.getKeggPathwayID());
				assertEquals(new String("Citrate cycle (TCA cycle)"), record2.getKeggPathwayName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* 00030 Pentose phosphate pathway */
				KeggMapTitleTabFileData record3 = parser.next();
				assertEquals(new KeggPathwayID("00030"), record3.getKeggPathwayID());
				assertEquals(new String("Pentose phosphate pathway"), record3.getKeggPathwayName());
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
		final String NS = "<http://kabob.ucdenver.edu/ice/kegg/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "KEGG_00010_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/kegg/KeggPathwayIce1> .",
						NS + "KEGG_00010_ICE> <http://www.genome.jp/kegg/hasKeggPathwayID> \"KEGG_00010\"@en .",
						NS
								+ "KEGG_00010_ICE> <http://www.genome.jp/kegg/hasKeggPathwayName> \"Glycolysis / Gluconeogenesis\"@en .",
						NS
								+ "KEGG_00020_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/kegg/KeggPathwayIce1> .",
						NS + "KEGG_00020_ICE> <http://www.genome.jp/kegg/hasKeggPathwayID> \"KEGG_00020\"@en .",
						NS
								+ "KEGG_00020_ICE> <http://www.genome.jp/kegg/hasKeggPathwayName> \"Citrate cycle (TCA cycle)\"@en .",
						NS
								+ "KEGG_00030_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/kegg/KeggPathwayIce1> .",
						NS + "KEGG_00030_ICE> <http://www.genome.jp/kegg/hasKeggPathwayID> \"KEGG_00030\"@en .",
						NS
								+ "KEGG_00030_ICE> <http://www.genome.jp/kegg/hasKeggPathwayName> \"Pentose phosphate pathway\"@en .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "kegg-pathway.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("kegg-pathway.nt", 9);
		counts.put("kabob-meta-kegg-pathway.nt", 6);
		return counts;
	}

}
