package edu.ucdenver.ccp.datasource.fileparsers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

public class SingleLineDataRecordReaderTest extends DefaultTestCase {

	private File testFile;

	@Before
	public void setUp() throws Exception {
		populateTestFile();
	}

	private void populateTestFile() throws IOException {
		testFile = folder.newFile("testFile.ascii");
		List<String> lines = CollectionsUtil.createList("line 1", "line 2", "line 3");
		FileWriterUtil.printLines(lines, testFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testRecordReader() throws Exception {
		SampleRecordReader reader = new SampleRecordReader(testFile);
		assertTrue(reader.hasNext());
		assertEquals("line 1", reader.next().getText());
		assertTrue(reader.hasNext());
		assertEquals("line 2", reader.next().getText());
		assertTrue(reader.hasNext());
		assertEquals("line 3", reader.next().getText());
		assertFalse(reader.hasNext());
	}

	/**
	 * Simple instantiation of the SingleLineDataRecordReader
	 * 
	 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
	 * 
	 */
	private static class SampleRecordReader extends SingleLineFileRecordReader<SampleDataRecord> {

		public SampleRecordReader(File dataFile) throws IOException {
			super(dataFile, CharacterEncoding.US_ASCII, null);
		}

		@Override
		protected SampleDataRecord parseRecordFromLine(Line line) {
			return new SampleDataRecord(line.getText(), line.getByteOffset(), line.getLineNumber());
		}

	}

	private static class SampleDataRecord extends SingleLineFileRecord {

		private final String text;

		public SampleDataRecord(String text, long byteOffset, long lineNumber) {
			super(byteOffset, lineNumber);
			this.text = text;
		}

		public String getText() {
			return text;
		}

	}

}
