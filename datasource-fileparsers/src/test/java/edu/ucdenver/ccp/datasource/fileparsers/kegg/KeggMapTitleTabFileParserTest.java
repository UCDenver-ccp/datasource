/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.kegg;

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
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;

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
				assertEquals(new KeggPathwayName("Glycolysis / Gluconeogenesis"), record1.getKeggPathwayName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* 00020 Citrate cycle (TCA cycle) */
				KeggMapTitleTabFileData record2 = parser.next();
				assertEquals(new KeggPathwayID("00020"), record2.getKeggPathwayID());
				assertEquals(new KeggPathwayName("Citrate cycle (TCA cycle)"), record2.getKeggPathwayName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* 00030 Pentose phosphate pathway */
				KeggMapTitleTabFileData record3 = parser.next();
				assertEquals(new KeggPathwayID("00030"), record3.getKeggPathwayID());
				assertEquals(new KeggPathwayName("Pentose phosphate pathway"), record3.getKeggPathwayName());
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
