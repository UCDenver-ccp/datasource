package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.impl;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2018 Regents of the University of Colorado
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileReaderUtil;
import edu.ucdenver.ccp.common.io.ClassPathUtil;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtXmlFileParserTest;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtXmlFileRecordReader;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfRecordWriter;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.RdfFormat;

public class UniProtXmlRdfGenTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private File outputDirectory;
	private File inputDirectory;
	private File inputFile;
	private File outputFile;
	private static final String SAMPLE_INPUT_FILE_NAME = "sprot_small.xml";

	@Before
	public void setUp() throws Exception {
		outputDirectory = folder.newFolder("output");
		inputDirectory = folder.newFolder("input");
		inputFile = new File(inputDirectory, SAMPLE_INPUT_FILE_NAME);
		outputFile = new File(outputDirectory, "uniprot-UniProtXmlFileRecordReader.0-0.nt");
		ClassPathUtil.copyClasspathResourceToDirectory(UniProtXmlFileParserTest.class, SAMPLE_INPUT_FILE_NAME,
				inputDirectory);
		assertTrue(inputFile.exists());
	}

	@Test
	public void test() throws IOException {
		UniProtXmlFileRecordReader rr = new UniProtXmlFileRecordReader(inputFile, null);
		RdfRecordWriter<UniProtXmlFileRecordReader> recordWriter = new RdfRecordWriter<UniProtXmlFileRecordReader>(
				outputDirectory, RdfFormat.NTRIPLES);
		long createdTimeInMillis20101217 = new GregorianCalendar(2010, 11, 17).getTimeInMillis();
		recordWriter.processRecordReader(rr, createdTimeInMillis20101217, Collections.emptySet());

		assertTrue("Output file should have been created.", outputFile.exists());

		List<String> lines = FileReaderUtil.loadLinesFromFile(outputFile, CharacterEncoding.UTF_8);
		for (String line : lines) {
			System.out.println(line);
		}

		Set<String> uniqueLines = new HashSet<String>(
				FileReaderUtil.loadLinesFromFile(outputFile, CharacterEncoding.UTF_8));
		Set<String> incorrectLines = CollectionsUtil.createSet(
				"<http://ccp.ucdenver.edu/kabob/ice/R_ovAB6mfXcONmclwbDLTTRY8gRAc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://ccp.ucdenver.edu/obo/ext/IAO_EXT_0001168> .",
				"<http://ccp.ucdenver.edu/kabob/ice/R_ovAB6mfXcONmclwbDLTTRY8gRAc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://ccp.ucdenver.edu/obo/ext/IAO_EXT_0001169> .");

		incorrectLines.removeAll(uniqueLines);
		assertFalse(incorrectLines.isEmpty());
		
		// List<String> expectedLines =
		// getExpectedLines(RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis20101217));
		// assertTrue("N-Triple Lines should be as expected.",
		// FileComparisonUtil.hasExpectedLines(outputFile,
		// CharacterEncoding.UTF_8, expectedLines, null, LineOrder.ANY_ORDER,
		// ColumnOrder.AS_IN_FILE));
	}

}
