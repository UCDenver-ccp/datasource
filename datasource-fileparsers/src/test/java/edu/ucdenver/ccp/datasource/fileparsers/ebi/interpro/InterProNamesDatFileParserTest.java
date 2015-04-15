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
package edu.ucdenver.ccp.fileparsers.ebi.interpro;

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

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class InterProNamesDatFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_INTERPRONAMESDAT_FILE_NAME = "InterPro_names.dat";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_INTERPRONAMESDAT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new InterProNamesDatFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testGetInterProID2NameMap() throws Exception {
		Map<String, String> interProID2NameMap = InterProNamesDatFileParser.getInterProID2NameMap(sampleInputFile,
				CharacterEncoding.US_ASCII);

		Map<String, String> expectedInterProID2NameMap = new HashMap<String, String>();
		expectedInterProID2NameMap.put("IPR000005", "Helix-turn-helix, AraC type");
		expectedInterProID2NameMap.put("IPR000003", "Retinoid X receptor");
		expectedInterProID2NameMap.put("IPR000001", "Kringle");

		assertEquals(expectedInterProID2NameMap, interProID2NameMap);

	}

	@Test
	public void testParser() {
		try {
			InterProNamesDatFileParser parser = new InterProNamesDatFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* IPR000001 Kringle */
				InterProNamesDatFileData record1 = parser.next();
				assertEquals("IPR000001", record1.getInterProID().toString());
				assertEquals("Kringle", record1.getInterProName().toString());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* IPR000003 Retinoid X receptor */
				InterProNamesDatFileData record2 = parser.next();
				assertEquals("IPR000003", record2.getInterProID().toString());
				assertEquals("Retinoid X receptor", record2.getInterProName().toString());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* IPR000005 Helix-turn-helix, AraC type */
				InterProNamesDatFileData record3 = parser.next();
				assertEquals("IPR000005", record3.getInterProID().toString());
				assertEquals("Helix-turn-helix, AraC type", record3.getInterProName().toString());
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
		Map<File, List<String>> expectedOutputFile2LinesMap = new HashMap<File, List<String>>();
		File expectedOutputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, "interpro-Id2Name.nt");
		expectedOutputFile2LinesMap.put(expectedOutputFile, getExpectedLines());

		return expectedOutputFile2LinesMap;
	}

	private List<String> getExpectedLines() {
		final String NS = "<http://kabob.ucdenver.edu/ice/interpro/";
		return CollectionsUtil
				.createList(
						NS
								+ "IPR000001_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/interpro/InterProIce1> .",
						NS + "IPR000001_ICE> <http://www.ebi.ac.uk/interpro/hasInterProName> \"Kringle\"@en .",
						NS + "IPR000001_ICE> <http://www.ebi.ac.uk/interpro/hasInterProID> \"IPR000001\"@en .",
						NS
								+ "IPR000001_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ebi.ac.uk/interpro/IPR000001> .",
						NS
								+ "IPR000003_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/interpro/InterProIce1> .",
						NS
								+ "IPR000003_ICE> <http://www.ebi.ac.uk/interpro/hasInterProName> \"Retinoid X receptor\"@en .",
						NS + "IPR000003_ICE> <http://www.ebi.ac.uk/interpro/hasInterProID> \"IPR000003\"@en .",
						NS
								+ "IPR000003_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ebi.ac.uk/interpro/IPR000003> .",
						NS
								+ "IPR000005_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/interpro/InterProIce1> .",
						NS
								+ "IPR000005_ICE> <http://www.ebi.ac.uk/interpro/hasInterProName> \"Helix-turn-helix, AraC type\"@en .",
						NS + "IPR000005_ICE> <http://www.ebi.ac.uk/interpro/hasInterProID> \"IPR000005\"@en .",
						NS
								+ "IPR000005_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ebi.ac.uk/interpro/IPR000005> .");
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("interpro-Id2Name.nt", 12);
		counts.put("kabob-meta-interpro-Id2Name.nt", 6);
		return counts;
	}

}
