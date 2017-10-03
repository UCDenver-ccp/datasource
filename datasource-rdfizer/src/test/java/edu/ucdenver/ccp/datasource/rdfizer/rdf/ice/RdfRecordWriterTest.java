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
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.model.vocabulary.DCTERMS;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.download.DownloadMetadata;
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
import edu.ucdenver.ccp.common.file.reader.StreamLineIterator;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf.GoaFileIdResolver;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf.GoaGaf2FileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.IdResolver;
import edu.ucdenver.ccp.datasource.identifiers.Identifier;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.RdfFormat;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.IAO;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.RO;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class RdfRecordWriterTest extends DefaultTestCase {

	private File geneId2NameDatFile;
	private File outputDirectory;
	private final String expectedOutputFileName = "ncbi_gene-GeneId2NameDatFileParser.0-0.nt";

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
		RdfRecordWriter<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriter<GeneId2NameDatFileParser>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217, Collections.emptySet());

		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		assertTrue("Output file should have been created.", outputFile.exists());

		List<String> expectedLines = getExpectedLines(RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis20101217));
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	@Test
	public void testWriteRdf_Compressed() throws IOException {
		boolean compress = true;
		GeneId2NameDatFileParser parser = new GeneId2NameDatFileParser(geneId2NameDatFile);
		RdfRecordWriter<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriter<GeneId2NameDatFileParser>(
				outputDirectory, RdfFormat.NTRIPLES, compress, -1, 0, new SimpleDuplicateTripleFilter());
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217, Collections.emptySet());

		File zippedOutputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName + ".gz");
		assertTrue("Output file should have been created.", zippedOutputFile.exists());

		// for (String line : FileReaderUtil.loadLinesFromFile(outputFile,
		// CharacterEncoding.UTF_8))
		// {
		// System.out.println("TRIPLE: " + line);
		// }
		File unzippedOutputFile = FileArchiveUtil.gunzipFile(zippedOutputFile);
		List<String> expectedLines = getExpectedLines(RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis20101217));
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(unzippedOutputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	/**
	 * Tests that setting the output record limit works as expected by capping
	 * the number of records output
	 * 
	 * @throws IOException
	 */
	@Test
	public void testWriteRdf_withOutputRecordLimit() throws IOException {
		GeneId2NameDatFileParser parser = new GeneId2NameDatFileParser(geneId2NameDatFile);
		RdfRecordWriter<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriter<GeneId2NameDatFileParser>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217, 1, Collections.emptySet());
		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		assertTrue("Output file should have been created.", outputFile.exists());
		List<String> expectedLines = getExpectedLines(RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis20101217))
				.subList(0, 30);
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	@Test
	public void testWriteRdf_WithDownloadMetadata() throws IOException {
		GeneId2NameDatFileParser parser = new GeneId2NameDatFileParser(geneId2NameDatFile);
		RdfRecordWriter<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriter<GeneId2NameDatFileParser>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();

		Calendar downloadDate = new GregorianCalendar(2010, 01, 25);
		File downloadedFile = folder.newFile("datasource.txt");
		long fileSizeInBytes = 1234567;
		Calendar lastModifiedDate = new GregorianCalendar(2010, 01, 21);
		URL downloadUrl = new URL("http://path/datasource.txt");

		DownloadMetadata dmd = new DownloadMetadata(downloadDate, downloadedFile, fileSizeInBytes, lastModifiedDate,
				downloadUrl);

		recordWriter.processRecordReader(parser, createdTimeInMillis20101217, CollectionsUtil.createSet(dmd));

		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		assertTrue("Output file should have been created.", outputFile.exists());

		List<String> expectedLines = getExpectedLines(RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis20101217));
		expectedLines.addAll(getExpectedMetadatalines(dmd));
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	private List<String> getExpectedMetadatalines(DownloadMetadata dmd) {
		URIImpl metadataUri = RdfRecordWriter.computeDownloadMetadataUri(dmd);
		/* @formatter:off */
		return CollectionsUtil.createList(
				"<http://ccp.ucdenver.edu/obo/ext/RS_NCBI_GENE_20101217> <" + DC.SOURCE + "> <"	+ metadataUri + "> .",
				"<" + metadataUri + "> <" + RDF.TYPE + "> <" + IAO.DOCUMENT.uri() + "> .",
				"<" + metadataUri + "> <" + CcpExtensionOntology.DOWNLOAD_DATE.uri() + "> "	+ RdfUtil.getDateLiteral(dmd.getDownloadDate().getTimeInMillis()) + " .",
				"<" + metadataUri + "> <" + CcpExtensionOntology.LAST_MODIFIED_DATE.uri() + "> " + RdfUtil.getDateLiteral(dmd.getFileLastModifiedDate().getTimeInMillis()) + " .",
				"<" + metadataUri + "> <" + CcpExtensionOntology.FILE_SIZE_BYTES.uri() + "> " + RdfUtil.createLiteral(dmd.getFileSizeInBytes()) + " .",
				"<" + metadataUri + "> <" + DC.SOURCE + "> <" + dmd.getDownloadUrl() + "> .");
		/* @formatter:on */
	}

	private List<String> getExpectedLines(String timestamp) {

		String recordHash1 = "_9p0Fjl7pa_ERTwAARalvsLvvhQ";
		String fieldHash_1_1 = "NS-OtpvOXf5NeNtm9_Cd8Y0G8wg"; // gene id 111
		String fieldHash_1_2 = "scQJXp5c3X-vDdO0bSHh_TbCQDk"; // abc-1
		String fieldHash_1_3 = "kOC8ypUOhEI93ThryXub9fYhbOs"; // chromosome 1
		String fieldHash_1_4_a = "xQxjFUZBqv84m0ELm85GVvEJkm0"; // 456
		String fieldHash_1_4_b = "Jm8VskpdUozjgEZgZkIc_IkoWMk"; // 567
		String fieldHash_1_4_c = "LCLRscp_Lu0cLcxHuuXuQWWaAcI"; // 678

		String recordHash2 = "eqCFP9-6LOi3eJp_Lc4U64q9cT0";
		String fieldHash_2_1 = "IRoWImnh1QgF69F77631RMJNddo"; // gene id 222
		String fieldHash_2_2 = "eRxH4V6FzXuKZzidqIqzMrX1WXo"; // def-2
		String fieldHash_2_3 = "Oxh57HmyrQlBkAJxvaPHw8iwUFg"; // chromosome 2
		String fieldHash_2_4_a = "TGJIE7iq7ewko_R0AkI39fnhuIE"; // 555
		String fieldHash_2_4_b = fieldHash_1_4_b; // 567

		String recordHash3 = "N23bJTw4db6V75tGAzNQbl8K20U";
		String fieldHash_3_1 = "Tca_p2Ap2x6DYaS00JfS2SlIHTo"; // gene id 333
		String fieldHash_3_2 = "7jHxWqndrdNh3_aQN1gLMjZfg7U"; // xyz-9
		String fieldHash_3_3 = "ceveSMS1JUCof0z1LlXPIGZTBeU"; // chromosome 3

		/* @formatter:off */
		return CollectionsUtil.createList(
				"<http://ccp.ucdenver.edu/obo/ext/RS_NCBI_GENE_20101217> <" + RDF.TYPE + "> <" + CcpExtensionOntology.RECORD_SET.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/RS_NCBI_GENE_20101217> <" + DCTERMS.DATE + "> " + timestamp + " .",
				
				/* record 1:   111 ABC-1 1 456,567,678 */
				
				"<http://ccp.ucdenver.edu/obo/ext/RS_NCBI_GENE_20101217> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NCBI_GENE_INFO_RECORD.uri() + "> .",

				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.DNA_IDENTIFIER_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_111> .",
				"<http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_111> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.NCBI_GENE_IDENTIFIER.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> <" + RDFS.LABEL + "> \"111\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_2 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_2 + "> <" + RDFS.LABEL + "> \"ABC-1\"@en .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NAME_FIELD_VALUE.uri() + "> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_3 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_3 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NCBI_GENE_INFO_RECORD___CHROMOSOME_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_3 + "> <" + RDFS.LABEL + "> \"1\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_a + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_a + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.INTERACTOR_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_a + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_456> .",
				"<http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_456> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.NCBI_GENE_IDENTIFIER.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_a + "> <" + RDFS.LABEL + "> \"456\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_b + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_b + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.INTERACTOR_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_b + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_567> .",
				"<http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_567> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.NCBI_GENE_IDENTIFIER.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_b + "> <" + RDFS.LABEL + "> \"567\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_c + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_c + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.INTERACTOR_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_c + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_678> .",
				"<http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_678> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.NCBI_GENE_IDENTIFIER.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_4_c + "> <" + RDFS.LABEL + "> \"678\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				/* record 2:   222 DEF-2 2 555,567 */
				"<http://ccp.ucdenver.edu/obo/ext/RS_NCBI_GENE_20101217> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NCBI_GENE_INFO_RECORD.uri() + "> .",

				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_1 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_1 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.DNA_IDENTIFIER_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_1 + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_222> .",
				"<http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_222> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.NCBI_GENE_IDENTIFIER.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_1 + "> <" + RDFS.LABEL + "> \"222\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_2 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_2 + "> <" + RDFS.LABEL + "> \"DEF-2\"@en .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NAME_FIELD_VALUE.uri() + "> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_3 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_3 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NCBI_GENE_INFO_RECORD___CHROMOSOME_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_3 + "> <" + RDFS.LABEL + "> \"2\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_4_a + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_4_a + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.INTERACTOR_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_4_a + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_555> .",
				"<http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_555> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.NCBI_GENE_IDENTIFIER.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_4_a + "> <" + RDFS.LABEL + "> \"555\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_4_b + "> .",
				
				/* record 3:   333 XYZ-9 3 [] */
				"<http://ccp.ucdenver.edu/obo/ext/RS_NCBI_GENE_20101217> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NCBI_GENE_INFO_RECORD.uri() + "> .",

				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.DNA_IDENTIFIER_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_333> .",
				"<http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_333> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.NCBI_GENE_IDENTIFIER.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> <" + RDFS.LABEL + "> \"333\"^^<http://www.w3.org/2001/XMLSchema#integer> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> <" + RDFS.LABEL + "> \"XYZ-9\"@en .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NAME_FIELD_VALUE.uri() + "> .",
				
				"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_3 + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_3 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.NCBI_GENE_INFO_RECORD___CHROMOSOME_FIELD_VALUE.uri() + "> .",
				"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_3 + "> <" + RDFS.LABEL + "> \"3\"^^<http://www.w3.org/2001/XMLSchema#integer> ."
				);
		/* @formatter:on */

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

	@Record(dataSource = DataSource.NCBI_GENE, ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD)
	@Data
	@EqualsAndHashCode(callSuper = false)
	private static class GeneId2NameDatFileData extends SingleLineFileRecord {

		@RecordField(ontClass = CcpExtensionOntology.DNA_IDENTIFIER_FIELD_VALUE)
		private final GeneID geneID;
		@RecordField(ontClass = CcpExtensionOntology.NAME_FIELD_VALUE)
		private final String geneName;
		@RecordField(ontClass = CcpExtensionOntology.INTERACTOR_FIELD_VALUE)
		private final Set<GeneID> interactingGeneIDs;
		@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___CHROMOSOME_FIELD_VALUE)
		private final Integer chromosome;

		public GeneId2NameDatFileData(GeneID geneID, String geneName, Integer chromosome, Set<GeneID> homologousGeneIDs,
				long byteOffset, long lineNumber) {
			super(byteOffset, lineNumber);
			this.geneID = geneID;
			this.geneName = geneName;
			this.chromosome = chromosome;
			this.interactingGeneIDs = homologousGeneIDs;
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

	@Identifier(ontClass = CcpExtensionOntology.NCBI_GENE_IDENTIFIER)
	private static class GeneID extends DataSourceIdentifier<Integer> {

		public GeneID(Integer resourceID) {
			super(resourceID, DataSource.NCBI_GENE);
		}

		@Override
		public Integer validate(Integer resourceID) throws IllegalArgumentException {
			return resourceID;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof GeneID) {
				GeneID geneID = (GeneID) obj;
				return geneID.getId().equals(getId());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return getId().hashCode();
		}

	}

	
//	@Test
//	public void testWriteUniProtRdf() throws IOException {
//		Class<? extends IdResolver> ID_RESOLVER_CLASS = GoaFileIdResolver.class;
//		GoaGaf2FileRecordReader rr = new GoaGaf2FileRecordReader(
//				new File(
//						"/Users/bill/Dropbox/work/eclipse/eclipse-projects/kabob.bill.git.0/test/rules_tests/build_test/test_triples/raw/sample.goa.gaf"),
//				CharacterEncoding.UTF_8, null, ID_RESOLVER_CLASS);
//		// UniProtXmlFileRecordReader rr = new UniProtXmlFileRecordReader(new
//		// File("/Users/bill/Downloads/P37173.xml"), null);
//		// UniProtXmlFileRecordReader rr = new UniProtXmlFileRecordReader(new
//		// File("/Users/bill/Desktop/tgfr2.uniprot.xml"), null);
//		// UniProtXmlFileRecordReader rr = new UniProtXmlFileRecordReader(new
//		// File("/Users/bill/Desktop/14-3-3.uniprot.xml"), null);
//		RdfRecordWriter<GeneId2NameDatFileParser> recordWriter = new RdfRecordWriter<GeneId2NameDatFileParser>(
//				new File("/tmp"), RdfFormat.NTRIPLES);
//		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
//		recordWriter.processRecordReader(rr, createdTimeInMillis20101217, Collections.emptySet());
//
//		
//		File f = new File("/tmp/goa-GoaGaf2FileRecordReader.0-0.nt");
//		cljFile(f);
//		
//		// File outputFile =
//		// FileUtil.appendPathElementsToDirectory(outputDirectory,
//		// expectedOutputFileName);
//		// assertTrue("Output file should have been created.",
//		// outputFile.exists());
//		//
//		// List<String> expectedLines =
//		// getExpectedLines(RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis20101217));
//		// assertTrue("N-Triple Lines should be as expected.",
//		// FileComparisonUtil.hasExpectedLines(outputFile,
//		// CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER,
//		// ColumnOrder.AS_IN_FILE));
//	}
//	
//	
//	private static void cljFile(File inputFile) throws IOException {
//		// order matters so use linkedhashmap
//		Map<String, String> regex2replaceMap = new LinkedHashMap<String, String>();
//		regex2replaceMap.put("<http://ccp.ucdenver.edu/obo/ext/", "ccp/");
//		regex2replaceMap.put("<http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf/");
//		regex2replaceMap.put("<http://purl.obolibrary.org/obo/", "obo/");
//		regex2replaceMap.put("<http://www.w3.org/2000/01/rdf-schema#", "rdfs/");
//		regex2replaceMap.put("\"([^\"]+)\"\\^\\^<http://www.w3.org/2001/XMLSchema#boolean> \\.", "$1)");
//		regex2replaceMap.put("\"(\\d+)\"\\^\\^<http://www.w3.org/2001/XMLSchema#integer> \\.", "$1)");
//		regex2replaceMap.put("\"([^\"]+)\"@en \\.", "[\"$1\" \"en\"])");
//		regex2replaceMap.put("\"([^\"]+)\" \\.", "[\"$1\"])");
//		regex2replaceMap.put("> \\.", ")");
//		regex2replaceMap.put(">", "");
//		regex2replaceMap.put("^ccp", "(ccp");
//		for (StreamLineIterator lineIter = new StreamLineIterator(inputFile, CharacterEncoding.UTF_8); lineIter.hasNext();) {
//			String line = lineIter.next().getText();
//			for (Entry<String, String> entry : regex2replaceMap.entrySet()) {
//				Pattern p = Pattern.compile(entry.getKey());
//				Matcher m = p.matcher(line);
//				line = m.replaceAll(entry.getValue());
//			}
//			System.out.println(line);
//			
//		}
//	}
	

}
