package edu.ucdenver.ccp.rdfizer.rdf;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileComparisonUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.ColumnOrder;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.LineOrder;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.fileparsers.hprd.HprdIdMappingsTxtFileParser;
import edu.ucdenver.ccp.rdfizer.rdf.RdfUtil.RdfFormat;

/**
 * this class is used to test specific bugs found in the RDF generation process
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SpecificRdfRecordWriterImplTest extends DefaultTestCase {

	private File outputDirectory;

	@Before
	public void setUp() throws Exception {
		outputDirectory = folder.newFolder("output");
	}

	/**
	 * This test created after it was discovered that some Genbank identifiers are truncated in
	 * their RDF representation. For example, M19154.1 shows up as GENBANK_M_ICE in the RDF.
	 * 
	 * @throws IOException
	 */
	@Ignore
	@Test
	public void testGenbankIdRdf() throws IOException {
		File f = populateSampleHprdIdMappingsFile();
		String expectedOutputFileName = "hprd-HprdIdMappingsTxtFileParser.0-0.nt";
		HprdIdMappingsTxtFileParser p = new HprdIdMappingsTxtFileParser(f, CharacterEncoding.UTF_8);
		RdfRecordWriterImpl<HprdIdMappingsTxtFileParser> recordWriter = new RdfRecordWriterImpl<HprdIdMappingsTxtFileParser>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(p, createdTimeInMillis20101217);

		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		assertTrue("Output file should have been created.", outputFile.exists());

		List<String> expectedLines = getExpectedHprdLines();
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	private File populateSampleHprdIdMappingsFile() throws IOException {
		File f = folder.newFile("hprd-input.txt");
		List<String> lines = CollectionsUtil
				.createList("01828\tTGFB2\tM19154.1\tAAA50404.1\t7042\t190220\tP61812,Q59EG9\tTGF beta 2");
		FileWriterUtil.printLines(lines, f, CharacterEncoding.UTF_8, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		return f;
	}

	private List<String> getExpectedHprdLines() {

		return CollectionsUtil.createList(

		);
	}

}
