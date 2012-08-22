package edu.ucdenver.ccp.fileparsers;

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
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

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
