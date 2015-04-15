/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
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
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.reactome.ReactomeReactionID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
@Ignore("Needs to be updated to reflect change in downloaded file format. (2 columns added, 2 columns swapped positions)")
public class ReactomeUniprot2PathwaysStidTxtFileParserTest extends RecordReaderTester {

	private final static String SAMPLE_DATA_FILE_NAME = "Reactome_uniprot_2_pathways.stid.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_DATA_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new ReactomeUniprot2PathwayStidTxtFileParser(sampleInputFile, CharacterEncoding.US_ASCII, null, null);
	}

	@Test
	public void testParser() throws Exception {
		try {
			ReactomeUniprot2PathwayStidTxtFileParser parser = new ReactomeUniprot2PathwayStidTxtFileParser(
					sampleInputFile, CharacterEncoding.US_ASCII, null, null);

			if (parser.hasNext()) {
				/*
				 * P08195 [tab] REACT_15518 [tab] Transmembrane transport of
				 * small molecules [tab]
				 * http://www.reactome.org/cgi-bin/eventbrowser_st_id
				 * ?ST_ID=REACT_15518
				 */
				validateRecord1(parser.next());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * P08908 [tab] REACT_14797 [tab] Signaling by GPCR [tab]
				 * http://
				 * www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_14797
				 */
				validateRecord2(parser.next());
			} else {
				fail("Parser should have returned a record here.");
			}

			/*
			 * P28222 [tab] REACT_14797 [tab] Signaling by GPCR [tab]
			 * http://www.
			 * reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_14797
			 */
			validateRecord3(parser.next());

			assertFalse(parser.hasNext());

			try {
				parser.next();
				fail("Should have thrown a NoSuchElementException.");
			} catch (NoSuchElementException nsee) {
				// do nothing, exception expected
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	@Test
	public void testParser_withTaxon() throws Exception {
		ReactomeUniprot2PathwayStidTxtFileParser parser = new ReactomeUniprot2PathwayStidTxtFileParser(sampleInputFile,
				CharacterEncoding.US_ASCII,
				new File("src/test/resources/edu/ucdenver/ccp/fileparsers/reactome/id-list"),
				CollectionsUtil.createSet(new NcbiTaxonomyID(9606)));

		if (parser.hasNext()) {
			/*
			 * P08908 [tab] REACT_14797 [tab] Signaling by GPCR [tab]
			 * http://www.
			 * reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_14797
			 */
			validateRecord2(parser.next());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
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
		validateRecord(record, new UniProtID("P08195"), new ReactomeReactionID("REACT_15518"),
				"Transmembrane transport of small molecules", new URI(
						"http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_15518").toURL());
	}

	private void validateRecord2(ReactomeUniprot2PathwayStidTxtFileData record) throws MalformedURLException,
			URISyntaxException {
		validateRecord(record, new UniProtID("P08908"), new ReactomeReactionID("REACT_14797"), "Signaling by GPCR",
				new URI("http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_14797").toURL());
	}

	private void validateRecord3(ReactomeUniprot2PathwayStidTxtFileData record) throws MalformedURLException,
			URISyntaxException {
		validateRecord(record, new UniProtID("P28222"), new ReactomeReactionID("REACT_14797"), "Signaling by GPCR",
				new URI("http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_14797").toURL());
	}

	@Test
	public void testGetInternal2ExternalGeneIDMap() throws Exception {
		Map<ReactomeReactionID, String> reactomeReactionID2NameMap = ReactomeUniprot2PathwayStidTxtFileParser
				.createReactomeReactionID2NameMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<ReactomeReactionID, String> expectedMap = new HashMap<ReactomeReactionID, String>();
		expectedMap.put(new ReactomeReactionID("REACT_15518"), "Transmembrane transport of small molecules");
		expectedMap.put(new ReactomeReactionID("REACT_14797"), "Signaling by GPCR");

		assertEquals(expectedMap, reactomeReactionID2NameMap);
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
