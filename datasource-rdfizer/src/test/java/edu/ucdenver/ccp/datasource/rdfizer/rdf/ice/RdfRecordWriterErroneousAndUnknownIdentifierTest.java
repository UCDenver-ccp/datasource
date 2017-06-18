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
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.vocabulary.DCTERMS;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;

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
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.pro.ProMappingFileParser;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.RdfFormat;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.RO;

/**
 * Testing using the protein ontology mapping file b/c it's a simple format and
 * it has unknown and potentially erroneous data source identifiers.
 */
public class RdfRecordWriterErroneousAndUnknownIdentifierTest extends DefaultTestCase {

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
		RdfRecordWriter<ProMappingFileParser> recordWriter = new RdfRecordWriter<ProMappingFileParser>(outputDirectory,
				RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(parser, createdTimeInMillis20101217, Collections.emptySet());

		File outputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, expectedOutputFileName);
		assertTrue("Output file should have been created.", outputFile.exists());

		List<String> expectedLines = getExpectedLines(RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis20101217));
		assertTrue("N-Triple Lines should be as expected.", FileComparisonUtil.hasExpectedLines(outputFile,
				CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE));
	}

	private List<String> getExpectedLines(String timestamp) {

		String recordHash1 = "XNtdzDn7n4FX2h_2IvZbmIgMADk";
		String fieldHash_1_1 = "fL2YEdeVCVPT-TpnzPUjxPQ0Op8"; // PR:000000005
		String fieldHash_1_2 = "Yx1D_QkJseyfomgZpcEUoW5t_3c"; // HGNC:11773
		String fieldHash_1_3 = "DMHorv2gNNZbf9mxOuF5KtY9kDw"; // is_a

		String recordHash2 = "LvRLq5aOGkx7-WkQJxcIk5V8daU";
		String fieldHash_2_1 = fieldHash_1_1; // PR:000000005
		String fieldHash_2_2 = "3c8k63TsxT7FPzTfo9m8foFL4dI"; // UniProtKB_VAR:VAR_022359
		String fieldHash_2_3 = fieldHash_1_3; // is_a

		String recordHash3 = "u1x8JAp3svavYRurYCqcH12cyYc";
		String fieldHash_3_1 = "P0JJEE-FidLu4jXZMtR9_C4Ik78"; // PR:000000006
		String fieldHash_3_2 = "u4hQlNNMmKwnmi0jfXIXKxJJp-o"; // UniProtKB:PABCDE
		String fieldHash_3_3 = "C2ciNC0aev2gZkUKJOlmf8l8yoA"; // exact

		String errorMessage = "\"Invalid UniProt ID: PABCDE. This ID does not comply with the specifications for UniProt accession numbers as defined here: http://www.uniprot.org/manual/accession_numbers\"@en";

		return CollectionsUtil.createList(
				/* @formatter:off */
						
						"<http://ccp.ucdenver.edu/obo/ext/RS_PR_20101217> <" + RDF.TYPE + "> <" + CcpExtensionOntology.RECORD_SET.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/RS_PR_20101217> <" + DCTERMS.DATE + "> " + timestamp + " .",
						
						/* record 1:   PR:000000005\tHGNC:11773\tis_a" */
						"<http://ccp.ucdenver.edu/obo/ext/RS_PR_20101217> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD.uri() + "> .",

						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___PRO_IDENTIFIER_FIELD_VALUE.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/PR_000000005> .",
						"<http://ccp.ucdenver.edu/obo/ext/PR_000000005> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.PROTEIN_ONTOLOGY_CONCEPT_IDENTIFIER.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_1 + "> <" + RDFS.LABEL + "> \"PR:000000005\"@en .",
						
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_2 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___TARGET_RECORD_IDENTIFIER_FIELD_VALUE.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_2 + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/HGNC_11773> .",
						"<http://ccp.ucdenver.edu/obo/ext/HGNC_11773> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.HGNC_GENE_IDENTIFIER.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_2 + "> <" + RDFS.LABEL + "> \"HGNC:11773\"@en .",
						
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash1 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_3 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_3 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___MAPPING_TYPE_FIELD_VALUE.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_1_3 + "> <" + RDFS.LABEL + "> \"is_a\"@en .",

						/* record 2: "PR:000000005\tUniProtKB_VAR:VAR_022359\tis_a", */
						"<http://ccp.ucdenver.edu/obo/ext/RS_PR_20101217> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD.uri() + "> .",

						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_1 + "> .",
						
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_2 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___TARGET_RECORD_IDENTIFIER_FIELD_VALUE.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.IDENTIFIER_OF_UNKNOWN_ORIGIN.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_2 + "> <" + RDFS.LABEL + "> \"UniProtKB_VAR:VAR_022359\"@en .",
						
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash2 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_2_3 + "> .",
						
						/* record 3: "PR:000000006\tUniProtKB:PABCDE\texact */
						"<http://ccp.ucdenver.edu/obo/ext/RS_PR_20101217> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD.uri() + "> .",

						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___PRO_IDENTIFIER_FIELD_VALUE.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> <" + RDF.TYPE + "> <http://ccp.ucdenver.edu/obo/ext/PR_000000006> .",
						"<http://ccp.ucdenver.edu/obo/ext/PR_000000006> <" + RDFS.SUBCLASSOF + "> <" + CcpExtensionOntology.PROTEIN_ONTOLOGY_CONCEPT_IDENTIFIER.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_1 + "> <" + RDFS.LABEL + "> \"PR:000000006\"@en .",
						
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___TARGET_RECORD_IDENTIFIER_FIELD_VALUE.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.INVALID_IDENTIFIER.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> <" + RDFS.LABEL + "> \"UniProtKB:PABCDE\"@en .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_2 + "> <" + RDFS.COMMENT + "> " + errorMessage + " .",
						
						"<http://ccp.ucdenver.edu/obo/ext/R_" + recordHash3 + "> <" + RO.HAS_PART.uri() + "> <http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_3 + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_3 + "> <" + RDF.TYPE + "> <" + CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___MAPPING_TYPE_FIELD_VALUE.uri() + "> .",
						"<http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_3_3 + "> <" + RDFS.LABEL + "> \"exact\"@en ."

						/* @formatter:on */
		);
	}

}
