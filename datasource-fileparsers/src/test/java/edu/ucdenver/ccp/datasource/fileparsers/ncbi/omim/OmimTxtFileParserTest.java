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
package edu.ucdenver.ccp.fileparsers.ncbi.omim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class OmimTxtFileParserTest extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "OMIM_omim.txt";
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new OmimTxtFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	// private final String REAL_OMIM_TXT_FILE =
	// "data/test/edu.uchsc.ccp.hanalyzer.parser.datafile/omim.txt";

	@Test
	public void testParser() {
		try {
			OmimTxtFileParser parser = new OmimTxtFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				OmimTxtFileData record1 = parser.next();
				assertEquals(new OmimID(100050), record1.getMimNumber());
				assertEquals(new OmimRecordTitle("AARSKOG SYNDROME"), record1.getTitle());
				assertEquals(new HashSet<OmimRecordTitle>(), record1.getAlternativeTitles());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				OmimTxtFileData record2 = parser.next();
				assertEquals(new OmimID(100070), record2.getMimNumber());
				assertEquals(new OmimRecordTitle("AORTIC ANEURYSM, ABDOMINAL"), record2.getTitle());
				assertEquals(CollectionsUtil.createSet(new OmimRecordTitle("AAA"), new OmimRecordTitle("AAA1"),
						new OmimRecordTitle("ANEURYSM, ABDOMINAL AORTIC"), new OmimRecordTitle(
								"ABDOMINAL AORTIC ANEURYSM ARTERIOMEGALY, INCLUDED"), new OmimRecordTitle(
								"ANEURYSMS, PERIPHERAL, INCLUDED")), record2.getAlternativeTitles());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				OmimTxtFileData record3 = parser.next();
				assertEquals(new OmimID(100100), record3.getMimNumber());
				assertEquals(new OmimRecordTitle(
						"ABDOMINAL MUSCLES, ABSENCE OF, WITH URINARY TRACT ABNORMALITY AND CRYPTORCHIDISM"), record3
						.getTitle());
				assertEquals(CollectionsUtil.createSet(new OmimRecordTitle("PRUNE BELLY SYNDROME"),
						new OmimRecordTitle("EAGLE-BARRETT SYNDROME")), record3.getAlternativeTitles());
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
	public void testGetOmimID2NameMap() throws Exception {
		Map<OmimID, OmimRecordTitle> omimID2NameMap = OmimTxtFileParser.getOmimID2NameMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<OmimID, OmimRecordTitle> expectedInterProID2NameMap = new HashMap<OmimID, OmimRecordTitle>();
		expectedInterProID2NameMap.put(new OmimID(100050), new OmimRecordTitle("AARSKOG SYNDROME"));
		expectedInterProID2NameMap.put(new OmimID(100070), new OmimRecordTitle("AORTIC ANEURYSM, ABDOMINAL"));
		expectedInterProID2NameMap.put(new OmimID(100100), new OmimRecordTitle("ABDOMINAL MUSCLES, ABSENCE OF, WITH URINARY TRACT ABNORMALITY AND CRYPTORCHIDISM"));

		assertEquals(expectedInterProID2NameMap, omimID2NameMap);
	}

}
