package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

public class UniProtXmlFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_INPUT_FILE_NAME = "sprot.xml";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_INPUT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new UniProtXmlFileRecordReader(sampleInputFile, null);
	}

	@Test
	public void testParser() throws IOException {
		assertTrue(sampleInputFile.exists());
		UniProtXmlFileRecordReader parser = new UniProtXmlFileRecordReader(sampleInputFile, null);

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());

	}

	@Test
	public void testParser_withTaxons() throws IOException {
		assertTrue(sampleInputFile.exists());
		UniProtXmlFileRecordReader parser = new UniProtXmlFileRecordReader(sampleInputFile,
				CollectionsUtil.createSet(new NcbiTaxonomyID(54321)));

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());

	}

}
