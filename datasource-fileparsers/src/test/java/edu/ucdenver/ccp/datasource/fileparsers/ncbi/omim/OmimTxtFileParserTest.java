package edu.ucdenver.ccp.datasource.fileparsers.ncbi.omim;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;

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
				assertEquals(new String("AARSKOG SYNDROME"), record1.getTitle());
				assertEquals(new HashSet<String>(), record1.getAlternativeTitles());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				OmimTxtFileData record2 = parser.next();
				assertEquals(new OmimID(100070), record2.getMimNumber());
				assertEquals(new String("AORTIC ANEURYSM, ABDOMINAL"), record2.getTitle());
				assertEquals(CollectionsUtil.createSet(new String("AAA"), new String("AAA1"),
						new String("ANEURYSM, ABDOMINAL AORTIC"), new String(
								"ABDOMINAL AORTIC ANEURYSM ARTERIOMEGALY, INCLUDED"), new String(
								"ANEURYSMS, PERIPHERAL, INCLUDED")), record2.getAlternativeTitles());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				OmimTxtFileData record3 = parser.next();
				assertEquals(new OmimID(100100), record3.getMimNumber());
				assertEquals(new String(
						"ABDOMINAL MUSCLES, ABSENCE OF, WITH URINARY TRACT ABNORMALITY AND CRYPTORCHIDISM"), record3
						.getTitle());
				assertEquals(CollectionsUtil.createSet(new String("PRUNE BELLY SYNDROME"),
						new String("EAGLE-BARRETT SYNDROME")), record3.getAlternativeTitles());
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
		Map<OmimID, String> omimID2NameMap = OmimTxtFileParser.getOmimID2NameMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<OmimID, String> expectedInterProID2NameMap = new HashMap<OmimID, String>();
		expectedInterProID2NameMap.put(new OmimID(100050), new String("AARSKOG SYNDROME"));
		expectedInterProID2NameMap.put(new OmimID(100070), new String("AORTIC ANEURYSM, ABDOMINAL"));
		expectedInterProID2NameMap.put(new OmimID(100100), new String("ABDOMINAL MUSCLES, ABSENCE OF, WITH URINARY TRACT ABNORMALITY AND CRYPTORCHIDISM"));

		assertEquals(expectedInterProID2NameMap, omimID2NameMap);
	}

}
