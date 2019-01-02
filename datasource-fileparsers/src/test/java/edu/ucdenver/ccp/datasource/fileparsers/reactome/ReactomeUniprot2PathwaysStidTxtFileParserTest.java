package edu.ucdenver.ccp.datasource.fileparsers.reactome;

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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ReactomeReactionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

public class ReactomeUniprot2PathwaysStidTxtFileParserTest extends RecordReaderTester {
	private final static String SAMPLE_DATA_FILE_NAME = "uniprot2reactome.txt";

	private final static String SAMPLE_ID_LIST_FILE_NAME = "id-list";

	private final static String BAD_RESOURCE_ID_SAMPLE_DATA_FILE_NAME = "uniprot2reactome-bad-resource-id.txt";

	@Test(expected = NoSuchElementException.class)
	public void testParserExceptionOnNoMoreElements() throws Exception {
		ReactomeUniprot2PathwayStidTxtFileParser parser = makeParser(makeTestFile(SAMPLE_DATA_FILE_NAME));
		int i = 0;

		try {
			for (; i < 3; i++) {
				parser.next();
			}
		} catch (NoSuchElementException nse) {
			fail("The parser has failed to extract all available records.");
		}

		assertEquals(3, i);
		parser.next();
	}

	@Test
	public void testParserHasNext() throws Exception {
		ReactomeUniprot2PathwayStidTxtFileParser parser = makeParser(makeTestFile(SAMPLE_DATA_FILE_NAME));
		int i = 0;

		for (; i < 3; i++) {
			assertTrue(parser.hasNext());
			parser.next();
		}

		assertEquals(3, i);
		assertFalse(parser.hasNext());
	}

	@Test
	public void testPositiveRecordValidation() throws Exception {
		ReactomeUniprot2PathwayStidTxtFileParser parser = makeParser(makeTestFile(SAMPLE_DATA_FILE_NAME));
		validateRecord1(parser.next());
		validateRecord2(parser.next());
		validateRecord3(parser.next());
	}

	@Ignore("Results in uniprot swissprot file being downloaded")
	@Test
	public void testParser_withTaxon() throws Exception {
		ReactomeUniprot2PathwayStidTxtFileParser parser = new ReactomeUniprot2PathwayStidTxtFileParser(sampleInputFile,
				CharacterEncoding.US_ASCII, new File(
						"src/test/resources/edu/ucdenver/ccp/datasource/fileparsers/reactome/id-list"),
				CollectionsUtil.createSet(new NcbiTaxonomyID(9606)), null, false);
		validateRecord2(parser.next());
	}

	/* ---------------------------------------------------------------------- */

	@Override
	protected String getSampleFileName() {
		return SAMPLE_DATA_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return makeParser(makeTestFile(SAMPLE_DATA_FILE_NAME));
	}

	private ReactomeUniprot2PathwayStidTxtFileParser makeParser(File f) throws IOException {
		return new ReactomeUniprot2PathwayStidTxtFileParser(f, CharacterEncoding.US_ASCII, null, null, null, false);
	}

	private void validateRecord(ReactomeUniprot2PathwayStidTxtFileData record, UniProtID expectedUniprotId,
			ReactomeReactionID expectedReactionId, String expectedReactionName, URL expectedReactionUrl) {
		assertEquals(expectedUniprotId, record.getUniprotID());
		assertEquals(expectedReactionId, record.getReactionID());
		assertEquals(expectedReactionName, record.getReactionName());
		assertEquals(expectedReactionUrl, record.getReactionURL());
	}

	private void validateRecord1(ReactomeUniprot2PathwayStidTxtFileData record) throws MalformedURLException,
			URISyntaxException {
		validateRecord(record, new UniProtID("A0A023GQ97"), new ReactomeReactionID("R-DME-451307"),
				"Activation of Na-permeable Kainate Receptors", new URI(
						"http://www.reactome.org/PathwayBrowser/#R-DME-451307").toURL());
	}

	private void validateRecord2(ReactomeUniprot2PathwayStidTxtFileData record) throws MalformedURLException,
			URISyntaxException {
		validateRecord(record, new UniProtID("P08908"), new ReactomeReactionID("R-FOO-14797"),
				"The name of the reaction", new URI(
						"http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_14797").toURL());
	}

	private void validateRecord3(ReactomeUniprot2PathwayStidTxtFileData record) throws MalformedURLException,
			URISyntaxException {
		validateRecord(record, new UniProtID("A0JMA1"), new ReactomeReactionID("R-XTR-380270"),
				"Recruitment of mitotic centrosome proteins and complexes", new URI(
						"http://www.reactome.org/PathwayBrowser/#R-XTR-380270").toURL());
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P08195REACT_15518> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.reactome.org/ReactomePathway2UniProtRecord> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P08195REACT_15518> <http://www.reactome.org/isLinkedToReactomeReactionICE> <http://www.reactome.org/REACT_15518_ICE> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P08195REACT_15518> <http://www.reactome.org/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P08195_ICE> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P08908REACT_14797> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.reactome.org/ReactomePathway2UniProtRecord> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P08908REACT_14797> <http://www.reactome.org/isLinkedToReactomeReactionICE> <http://www.reactome.org/REACT_14797_ICE> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P08908REACT_14797> <http://www.reactome.org/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P08908_ICE> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P28222REACT_14797> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.reactome.org/ReactomePathway2UniProtRecord> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P28222REACT_14797> <http://www.reactome.org/isLinkedToReactomeReactionICE> <http://www.reactome.org/REACT_14797_ICE> .",
						"<http://www.reactome.org/REACTOME_UNIPROT2PATHWAY_RECORD_P28222REACT_14797> <http://www.reactome.org/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P28222_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap
				.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "reactome-pathway2uniprot.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("reactome-pathway2uniprot.nt", 9);
		counts.put("kabob-meta-reactome-pathway2uniprot.nt", 6);
		return counts;
	}

}
