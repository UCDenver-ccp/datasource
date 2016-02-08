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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileComparisonUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.ColumnOrder;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.LineOrder;
import edu.ucdenver.ccp.common.file.FileReaderUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.fileparsers.pro.ProMappingFileParser;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.RdfFormat;

/**
 * Testing using the protein ontology mapping file b/c it's a simple format and
 * it has unknown and potentially erroneous data source identifiers.
 */
public class RdfRecordWriterImplErroneousAndUnknownIdentifierTest extends DefaultTestCase {

	private File proMappingTxtFile_unknownIdentifier;
	private File outputDirectory;
	private final String expectedOutputFileName = "pr-ProMappingFileParser.0-0.nt";

	@Before
	public void setUp() throws Exception {
		outputDirectory = folder.newFolder("output");
		proMappingTxtFile_unknownIdentifier = folder.newFile("promapping.txt");
		populateProMappingTxtFile_unknownIdentifier();
	}

	/**
	 * PR:000000005 HGNC:11773 is_a <br>
	 * PR:000000005 UniProtKB_VAR:VAR_022359 is_a // unknown identifier type<br>
	 * PR:000000006 UniProtKB:PABCDE exact // invalid UniProt ID<br>
	 */
	private void populateProMappingTxtFile_unknownIdentifier() throws IOException {
		List<String> lines = CollectionsUtil.createList("PR:000000005\tHGNC:11773\tis_a",
				"PR:000000005\tUniProtKB_VAR:VAR_022359\tis_a", "PR:000000006\tUniProtKB:PABCDE\texact");
		FileWriterUtil.printLines(lines, proMappingTxtFile_unknownIdentifier, CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
	}

	@Test
	public void testWriteRdf_unknown_and_erroneous_identifiers() throws IOException {
		ProMappingFileParser parser = new ProMappingFileParser(proMappingTxtFile_unknownIdentifier,
				CharacterEncoding.US_ASCII);
		RdfRecordWriterImpl<ProMappingFileParser> recordWriter = new RdfRecordWriterImpl<ProMappingFileParser>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217);

		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		System.err.println("dir contents: " + Arrays.toString(outputDirectory.list()));
		assertTrue("Output file should have been created.", outputFile.exists());

		List<String> linesFromFile = FileReaderUtil.loadLinesFromFile(outputFile, CharacterEncoding.UTF_8);
		for (String l : linesFromFile) {
			System.err.println(l);
		}

		List<String> expectedLines = getExpectedLines();
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	private List<String> getExpectedLines() {

		return CollectionsUtil
				.createList(

						"<http://kabob.ucdenver.edu/iao/pr/prDataSource20101217> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/pr/prDataSource> .",
						"<http://kabob.ucdenver.edu/iao/pr/prProMappingRecordDataSet20101217> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/prProMappingRecordSchema1> .",
						"<http://kabob.ucdenver.edu/iao/pr/prDataSource20101217> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/prProMappingRecordDataSet20101217> .",
						"<http://kabob.ucdenver.edu/iao/pr/prProMappingRecordDataSet20101217> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/DataSet> .",
						"<http://kabob.ucdenver.edu/iao/pr/prProMappingRecordDataSet20101217> <http://kabob.ucdenver.edu/iao/hasCreationDate> \"2010-12-17T00:00:00.000-07:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime> .",
						"<http://kabob.ucdenver.edu/iao/pr/prProMappingRecordDataSet20101217> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_WZhKO4jkiAbN2hT_3flYHMxyvEc> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_WZhKO4jkiAbN2hT_3flYHMxyvEc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_WZhKO4jkiAbN2hT_3flYHMxyvEc> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecordSchema1> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_WZhKO4jkiAbN2hT_3flYHMxyvEc> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_2897ALlg_3c5NaoTPgKcA2iK3LE> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_2897ALlg_3c5NaoTPgKcA2iK3LE> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord_mappingTypeDataField1> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_2897ALlg_3c5NaoTPgKcA2iK3LE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_2897ALlg_3c5NaoTPgKcA2iK3LE> <http://purl.obolibrary.org/obo/IAO_0000219> \"is_a\"@en .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_WZhKO4jkiAbN2hT_3flYHMxyvEc> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_GWKfNoArBFkAsDcNi36qnBAsgzQ> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_GWKfNoArBFkAsDcNi36qnBAsgzQ> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord_proteinOntologyIdDataField1> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_GWKfNoArBFkAsDcNi36qnBAsgzQ> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_GWKfNoArBFkAsDcNi36qnBAsgzQ> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/pr/PR_000000005_ICE> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_WZhKO4jkiAbN2hT_3flYHMxyvEc> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_kULVK1FZ1tqZ3xW2uncIWRC_QMY> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_kULVK1FZ1tqZ3xW2uncIWRC_QMY> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord_targetRecordIdDataField1> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_kULVK1FZ1tqZ3xW2uncIWRC_QMY> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_kULVK1FZ1tqZ3xW2uncIWRC_QMY> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/hgnc/HGNC_11773_ICE> .",
						"<http://kabob.ucdenver.edu/iao/pr/prProMappingRecordDataSet20101217> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_X2n4OD0lGbx7Kz9nOlpuf-0b9x8> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_X2n4OD0lGbx7Kz9nOlpuf-0b9x8> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_X2n4OD0lGbx7Kz9nOlpuf-0b9x8> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecordSchema1> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_X2n4OD0lGbx7Kz9nOlpuf-0b9x8> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_2897ALlg_3c5NaoTPgKcA2iK3LE> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_X2n4OD0lGbx7Kz9nOlpuf-0b9x8> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_GWKfNoArBFkAsDcNi36qnBAsgzQ> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_X2n4OD0lGbx7Kz9nOlpuf-0b9x8> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_PgXUJOe0DcW7eLkxbVbZS3Jy2OQ> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_PgXUJOe0DcW7eLkxbVbZS3Jy2OQ> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord_targetRecordIdDataField1> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_PgXUJOe0DcW7eLkxbVbZS3Jy2OQ> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_PgXUJOe0DcW7eLkxbVbZS3Jy2OQ> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/kabob/R_NonNormalizedIdentifierRecord_H0tEwGtJ3UasTlh7kGGk6r42NMo> .",
						"<http://kabob.ucdenver.edu/iao/kabob/R_NonNormalizedIdentifierRecord_H0tEwGtJ3UasTlh7kGGk6r42NMo> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/kabob/NonNormalizedIdentifierRecord> .",
						"<http://kabob.ucdenver.edu/iao/kabob/R_NonNormalizedIdentifierRecord_H0tEwGtJ3UasTlh7kGGk6r42NMo> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/kabob/NonNormalizedIdentifierRecordSchema1> .",
						"<http://kabob.ucdenver.edu/iao/kabob/R_NonNormalizedIdentifierRecord_H0tEwGtJ3UasTlh7kGGk6r42NMo> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/kabob/F_NonNormalizedIdentifierRecord_identifier_H0tEwGtJ3UasTlh7kGGk6r42NMo> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_NonNormalizedIdentifierRecord_identifier_H0tEwGtJ3UasTlh7kGGk6r42NMo> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/kabob/NonNormalizedIdentifierRecord_identifierDataField1> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_NonNormalizedIdentifierRecord_identifier_H0tEwGtJ3UasTlh7kGGk6r42NMo> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_NonNormalizedIdentifierRecord_identifier_H0tEwGtJ3UasTlh7kGGk6r42NMo> <http://purl.obolibrary.org/obo/IAO_0000219> \"UniProtKB_VAR:VAR_022359\"@en .",
						"<http://kabob.ucdenver.edu/iao/pr/prProMappingRecordDataSet20101217> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_7sl9N3o2lQUc26DjJRSaO8R2Y7s> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_7sl9N3o2lQUc26DjJRSaO8R2Y7s> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_7sl9N3o2lQUc26DjJRSaO8R2Y7s> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecordSchema1> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_7sl9N3o2lQUc26DjJRSaO8R2Y7s> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_H1hCp5e8_r2TTrw-uWcH1m5JKXM> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_H1hCp5e8_r2TTrw-uWcH1m5JKXM> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord_mappingTypeDataField1> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_H1hCp5e8_r2TTrw-uWcH1m5JKXM> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_mappingType_H1hCp5e8_r2TTrw-uWcH1m5JKXM> <http://purl.obolibrary.org/obo/IAO_0000219> \"exact\"@en .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_7sl9N3o2lQUc26DjJRSaO8R2Y7s> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_S1fonzU5XtSRL8_MlMV4e_N20QM> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_S1fonzU5XtSRL8_MlMV4e_N20QM> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord_proteinOntologyIdDataField1> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_S1fonzU5XtSRL8_MlMV4e_N20QM> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_proteinOntologyId_S1fonzU5XtSRL8_MlMV4e_N20QM> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/pr/PR_000000006_ICE> .",
						"<http://kabob.ucdenver.edu/iao/pr/R_ProMappingRecord_7sl9N3o2lQUc26DjJRSaO8R2Y7s> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_NqfHfOrmMhiavPrX11WCiU0Kg5I> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_NqfHfOrmMhiavPrX11WCiU0Kg5I> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/pr/ProMappingRecord_targetRecordIdDataField1> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_NqfHfOrmMhiavPrX11WCiU0Kg5I> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/pr/F_ProMappingRecord_targetRecordId_NqfHfOrmMhiavPrX11WCiU0Kg5I> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/kabob/R_ErroneousIdentifierRecord_E34PZ6CBFbVnAiHThhjCwDlF_BM> .",
						"<http://kabob.ucdenver.edu/iao/kabob/R_ErroneousIdentifierRecord_E34PZ6CBFbVnAiHThhjCwDlF_BM> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/kabob/ErroneousIdentifierRecord> .",
						"<http://kabob.ucdenver.edu/iao/kabob/R_ErroneousIdentifierRecord_E34PZ6CBFbVnAiHThhjCwDlF_BM> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/kabob/ErroneousIdentifierRecordSchema1> .",
						"<http://kabob.ucdenver.edu/iao/kabob/R_ErroneousIdentifierRecord_E34PZ6CBFbVnAiHThhjCwDlF_BM> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_comment_ofxXBdY4IPpk1fxDieB7Gu34gAY> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_comment_ofxXBdY4IPpk1fxDieB7Gu34gAY> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/kabob/ErroneousIdentifierRecord_commentDataField1> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_comment_ofxXBdY4IPpk1fxDieB7Gu34gAY> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_comment_ofxXBdY4IPpk1fxDieB7Gu34gAY> <http://purl.obolibrary.org/obo/IAO_0000219> \"Invalid UniProt ID: PABCDE. This ID does not comply with the specifications for UniProt accession numbers as defined here: http://www.uniprot.org/manual/accession_numbers\"@en .",
						"<http://kabob.ucdenver.edu/iao/kabob/R_ErroneousIdentifierRecord_E34PZ6CBFbVnAiHThhjCwDlF_BM> <http://purl.obolibrary.org/obo/BFO_0000051> <http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_identifier_eH5KzArIhTUYaJC-91OzTVN2OdU> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_identifier_eH5KzArIhTUYaJC-91OzTVN2OdU> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/kabob/ErroneousIdentifierRecord_identifierDataField1> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_identifier_eH5KzArIhTUYaJC-91OzTVN2OdU> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
						"<http://kabob.ucdenver.edu/iao/kabob/F_ErroneousIdentifierRecord_identifier_eH5KzArIhTUYaJC-91OzTVN2OdU> <http://purl.obolibrary.org/obo/IAO_0000219> \"UniProtKB:PABCDE\"@en .");
	}

}
