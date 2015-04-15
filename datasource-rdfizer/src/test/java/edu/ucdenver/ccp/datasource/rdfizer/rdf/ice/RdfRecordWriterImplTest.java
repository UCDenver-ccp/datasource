package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileArchiveUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.ColumnOrder;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.LineOrder;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.RdfFormat;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.RDFS;

public class RdfRecordWriterImplTest extends DefaultTestCase {

	private File geneId2NameDatFile;
	private File outputDirectory;
	private final String expectedOutputFileName = "eg-GeneId2NameDatFileParser.0-0.nt";

	@Before
	public void setUp() throws Exception {
		outputDirectory = folder.newFolder("output");
		geneId2NameDatFile = folder.newFile("geneId2Name.dat");
		populateGeneId2NameDatFile();
	}

	private final Integer geneID_1 = 111;
	private final String geneName_1 = "ABC-1";
	private final Integer chromosome_1 = 1;
	private final Set<Integer> homologousGeneIDs_1 = CollectionsUtil.createSet(456, 567, 678);
	private final Integer geneID_2 = 222;
	private final String geneName_2 = "DEF-2";
	private final Integer chromosome_2 = 2;
	private final Set<Integer> homologousGeneIDs_2 = CollectionsUtil.createSet(555, 567);
	private final Integer geneID_3 = 333;
	private final String geneName_3 = "XYZ-9";
	private final Integer chromosome_3 = 3;
	private final Set<Integer> homologousGeneIDs_3 = CollectionsUtil.createSet();

	private String createGeneId2NameDatLine(Integer geneID, String geneName, Integer chromosome,
			Set<Integer> homologousGeneIDs) {
		String line = geneID.toString() + "\t" + geneName + "\t" + chromosome + "\t";
		for (Integer homGeneID : homologousGeneIDs) {
			line += (homGeneID.toString() + "|");
		}
		if (homologousGeneIDs.size() > 0)
			line = StringUtil.removeLastCharacter(line);
		return line;
	}

	private void populateGeneId2NameDatFile() throws IOException {
		List<String> lines = CollectionsUtil.createList(
				createGeneId2NameDatLine(geneID_1, geneName_1, chromosome_1, homologousGeneIDs_1),
				createGeneId2NameDatLine(geneID_2, geneName_2, chromosome_2, homologousGeneIDs_2),
				createGeneId2NameDatLine(geneID_3, geneName_3, chromosome_3, homologousGeneIDs_3));
		FileWriterUtil.printLines(lines, geneId2NameDatFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);
	}

	@Test
	public void testWriteRdf() throws IOException {
		GeneId2NameDatFileParser parser = new GeneId2NameDatFileParser(geneId2NameDatFile);
		RdfRecordWriterImpl<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriterImpl<GeneId2NameDatFileParser>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217);

		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		System.out.println("dir contents: " + Arrays.toString(outputDirectory.list()));
		assertTrue("Output file should have been created.", outputFile.exists());

		List<String> expectedLines = getExpectedLines();
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	@Test
	public void testWriteRdf_Compressed() throws IOException {
		boolean compress = true;
		GeneId2NameDatFileParser parser = new GeneId2NameDatFileParser(geneId2NameDatFile);
		RdfRecordWriterImpl<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriterImpl<GeneId2NameDatFileParser>(
				outputDirectory, RdfFormat.NTRIPLES, compress, -1, 0, new SimpleDuplicateTripleFilter());
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217);

		File zippedOutputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName + ".gz");
		assertTrue("Output file should have been created.", zippedOutputFile.exists());

		// for (String line : FileReaderUtil.loadLinesFromFile(outputFile, CharacterEncoding.UTF_8))
		// {
		// System.out.println("TRIPLE: " + line);
		// }
		File unzippedOutputFile = FileArchiveUtil.gunzipFile(zippedOutputFile);
		List<String> expectedLines = getExpectedLines();
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(unzippedOutputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	/**
	 * Tests that setting the output record limit works as expected by capping the number of records
	 * output
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWriteRdf_withOutputRecordLimit() throws IOException {
		GeneId2NameDatFileParser parser = new GeneId2NameDatFileParser(geneId2NameDatFile);
		RdfRecordWriterImpl<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriterImpl<GeneId2NameDatFileParser>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217, 1);
		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		assertTrue("Output file should have been created.", outputFile.exists());
		List<String> expectedLines = getExpectedLines().subList(0, 32);
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	private List<String> getExpectedLines() {

		return CollectionsUtil
				.createList(

						"<http://kabob.ucdenver.edu/iao/eg/egDataSource20101217> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/eg/egDataSource> .",
						"<http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataDataSet20101217> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataSchema1> .",
						"<http://kabob.ucdenver.edu/iao/eg/egDataSource20101217> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataDataSet20101217> .",
						"<http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataDataSet20101217> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/DataSet> .",
						"<http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataDataSet20101217> <http://kabob.ucdenver.edu/iao/hasCreationDate> \"2010-12-17T00:00:00.000-07:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime> .",
						"<http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataDataSet20101217> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileDataSchema1> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_uMTGX3DgyrCZiUl_TdbT5kknbDc> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_uMTGX3DgyrCZiUl_TdbT5kknbDc> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_chromosomeDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_uMTGX3DgyrCZiUl_TdbT5kknbDc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_uMTGX3DgyrCZiUl_TdbT5kknbDc> <http://purl.obolibrary.org/obo/IAO_0000219> \"1\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_-CCwiNPQGZXqxzHwnRQPLcufczE> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_-CCwiNPQGZXqxzHwnRQPLcufczE> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_geneIDDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_-CCwiNPQGZXqxzHwnRQPLcufczE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_-CCwiNPQGZXqxzHwnRQPLcufczE> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_111_ICE> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_0Bh5BTPeMidQDIVhcwyulDl43_w> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_0Bh5BTPeMidQDIVhcwyulDl43_w> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_geneNameDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_0Bh5BTPeMidQDIVhcwyulDl43_w> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_0Bh5BTPeMidQDIVhcwyulDl43_w> <http://purl.obolibrary.org/obo/IAO_0000219> \"ABC-1\"@en .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_icg7DNHgWpSrcO1RMs6jGswLysE> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_icg7DNHgWpSrcO1RMs6jGswLysE> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_567_ICE> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_icg7DNHgWpSrcO1RMs6jGswLysE> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_homologousGeneIDsDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_icg7DNHgWpSrcO1RMs6jGswLysE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_Huvuy3PBpaT_1bkqnPJrUm56plE> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_Huvuy3PBpaT_1bkqnPJrUm56plE> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_456_ICE> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_Huvuy3PBpaT_1bkqnPJrUm56plE> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_homologousGeneIDsDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_Huvuy3PBpaT_1bkqnPJrUm56plE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_SLEJJYDvYbjNjgDeZpwYtYyDzDE> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_qfYm9hdvq_aJMz72kAWH5fEPvb4> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_qfYm9hdvq_aJMz72kAWH5fEPvb4> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_678_ICE> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_qfYm9hdvq_aJMz72kAWH5fEPvb4> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_homologousGeneIDsDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_qfYm9hdvq_aJMz72kAWH5fEPvb4> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataDataSet20101217> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileDataSchema1> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_Sq8QGIW4_EY29zLv9ndSuo0FXdM> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_Sq8QGIW4_EY29zLv9ndSuo0FXdM> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_chromosomeDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_Sq8QGIW4_EY29zLv9ndSuo0FXdM> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_Sq8QGIW4_EY29zLv9ndSuo0FXdM> <http://purl.obolibrary.org/obo/IAO_0000219> \"2\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_t7j3WFg6gDoRywM0JQ9U211X5OY> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_t7j3WFg6gDoRywM0JQ9U211X5OY> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_geneIDDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_t7j3WFg6gDoRywM0JQ9U211X5OY> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_t7j3WFg6gDoRywM0JQ9U211X5OY> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_222_ICE> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_zh3IaDv1xyvAghT9MjJxG41szZ4> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_zh3IaDv1xyvAghT9MjJxG41szZ4> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_geneNameDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_zh3IaDv1xyvAghT9MjJxG41szZ4> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_zh3IaDv1xyvAghT9MjJxG41szZ4> <http://purl.obolibrary.org/obo/IAO_0000219> \"DEF-2\"@en .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_icg7DNHgWpSrcO1RMs6jGswLysE> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_URI5a-9i5Ti9J5Qeet0-_rcjEvc> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_139oy219DqSRmT4r65iQ4QFbaMc> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_139oy219DqSRmT4r65iQ4QFbaMc> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_555_ICE> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_139oy219DqSRmT4r65iQ4QFbaMc> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_homologousGeneIDsDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_homologousGeneIDs_139oy219DqSRmT4r65iQ4QFbaMc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/egGeneId2NameDatFileDataDataSet20101217> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_FtYHBoyFbhuQktYWySdRhIQf6YQ> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_FtYHBoyFbhuQktYWySdRhIQf6YQ> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_FtYHBoyFbhuQktYWySdRhIQf6YQ> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileDataSchema1> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_FtYHBoyFbhuQktYWySdRhIQf6YQ> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_73htV8MhttNyomSqgl9a8326Trw> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_73htV8MhttNyomSqgl9a8326Trw> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_chromosomeDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_73htV8MhttNyomSqgl9a8326Trw> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_chromosome_73htV8MhttNyomSqgl9a8326Trw> <http://purl.obolibrary.org/obo/IAO_0000219> \"3\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_FtYHBoyFbhuQktYWySdRhIQf6YQ> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_xNM_WvK_FybEYs5f8Xi23ySoEOA> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_xNM_WvK_FybEYs5f8Xi23ySoEOA> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_geneIDDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_xNM_WvK_FybEYs5f8Xi23ySoEOA> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneID_xNM_WvK_FybEYs5f8Xi23ySoEOA> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_333_ICE> .",
						"<http://kabob.ucdenver.edu/iao/eg/R_GeneId2NameDatFileData_FtYHBoyFbhuQktYWySdRhIQf6YQ> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_8L2bT7V4aFD6tDh9E-14Yja2Cmc> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_8L2bT7V4aFD6tDh9E-14Yja2Cmc> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/eg/GeneId2NameDatFileData_geneNameDataField1> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_8L2bT7V4aFD6tDh9E-14Yja2Cmc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/eg/F_GeneId2NameDatFileData_geneName_8L2bT7V4aFD6tDh9E-14Yja2Cmc> <http://purl.obolibrary.org/obo/IAO_0000219> \"XYZ-9\"@en .");
	}

	private String createExpectedNTripleLine_GeneID2LiteralGeneID(Integer geneID) {
		return String.format("<http://kabob.ucdenver.edu/iao/eg/_%s> <%s> \"%s\"@en .", geneID.toString(),
				"http://kabob.ucdenver.edu/iao/eg/hasGeneID", geneID.toString());
	}

	private String createExpectedNTripleLine_GeneID2SOConstant(Integer geneID, String constantUri) {
		return String.format("<http://kabob.ucdenver.edu/iao/eg/_%s> <%s> <%s> .", geneID.toString(),
				RDFS.SUBCLASS_OF.uri(), constantUri);
	}

	// this test needs the expected lines in order to work
	// @Test
	// public void testWriteRdf_DataRecordHasASet() throws IOException {
	// GeneId2NameDatFileParser parser = new GeneId2NameDatFileParser(geneId2NameDatFile);
	// RdfRecordWriterImpl<GeneId2NameDatFileParser> recordWriter = new
	// RdfRecordWriterImpl<GeneId2NameDatFileParser>(
	// outputDirectory, RdfFormat.NTRIPLES);
	// long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
	// recordWriter
	// .setRdfSource(new BaseRdfSource(RdfNamespace.EG, createdTimeInMillis20101217));
	// recordWriter.processRecordReader(parser);
	//
	// File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory,
	// expectedOutputFileName);
	// assertTrue("Output file should have been created.", outputFile.exists());
	//
	// assertTrue("N-Triple Lines should be as expected.",
	// FileComparisonUtil.hasExpectedLines(outputFile,
	// CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	// }

	private String createExpectedNTripleLine_GeneID2Name(Integer geneID, String geneName) {
		return String.format("<http://kabob.ucdenver.edu/iao/eg/_%s> <%s> \"%s\"@en .", geneID.toString(),
				"http://kabob.ucdenver.edu/iao/eg/hasString", geneName);
	}

	private String createExpectedNTripleLine_GeneID2GeneID(Integer geneID, Integer homologousGeneID) {
		return String.format("<http://kabob.ucdenver.edu/iao/eg/_%s> <%s> <http://www.ncbi.nlm.nih.gov/gene/_%s> .",
				geneID.toString(), "http://kabob.ucdenver.edu/iao/homologene/hasHomolog", homologousGeneID.toString());
	}

	private static class GeneId2NameDatFileParser extends SingleLineFileRecordReader<GeneId2NameDatFileData> {

		public GeneId2NameDatFileParser(File dataFile) throws IOException {
			super(dataFile, CharacterEncoding.US_ASCII, null);
		}

		@Override
		protected GeneId2NameDatFileData parseRecordFromLine(Line line) {
			return GeneId2NameDatFileData.parseFromLine(line);
		}

	}

	@Record(dataSource = DataSource.EG)
	private static class GeneId2NameDatFileData extends SingleLineFileRecord {

		@RecordField
		private final GeneID geneID;
		@RecordField
		private final String geneName;
		@RecordField
		private final Set<GeneID> homologousGeneIDs;
		@RecordField
		private final Integer chromosome;

		public GeneId2NameDatFileData(GeneID geneID, String geneName, Integer chromosome,
				Set<GeneID> homologousGeneIDs, long byteOffset, long lineNumber) {
			super(byteOffset, lineNumber);
			this.geneID = geneID;
			this.geneName = geneName;
			this.chromosome = chromosome;
			this.homologousGeneIDs = homologousGeneIDs;
		}

		public GeneID getGeneID() {
			return geneID;
		}

		public String getString() {
			return geneName;
		}

		public Integer getChromosome() {
			return chromosome;
		}

		public Set<GeneID> getHomologousGeneIDs() {
			return homologousGeneIDs;
		}

		public static GeneId2NameDatFileData parseFromLine(Line line) {
			String[] toks = line.getText().split("\\t", -1);
			GeneID geneID = new GeneID(new Integer(toks[0]));
			String geneName = new String(toks[1]);
			Integer chromosome = new Integer(toks[2]);
			Set<GeneID> homologousGeneIDs = new HashSet<GeneID>();
			if (toks[3].length() > 0)
				for (Integer id : CollectionsUtil.parseInts(Arrays.asList(toks[3].split("\\|")))) {
					homologousGeneIDs.add(new GeneID(id));
				}
			return new GeneId2NameDatFileData(geneID, geneName, chromosome, homologousGeneIDs, line.getByteOffset(),
					line.getLineNumber());
		}

	}

	private static class GeneID extends DataSourceIdentifier<Integer> {

		public GeneID(Integer resourceID) {
			super(resourceID, DataSource.EG);
		}

		@Override
		public Integer validate(Integer resourceID) throws IllegalArgumentException {
			return resourceID;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof GeneID) {
				GeneID geneID = (GeneID) obj;
				return geneID.getDataElement().equals(getDataElement());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return getDataElement().hashCode();
		}

	}

	

}
