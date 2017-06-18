package edu.ucdenver.ccp.datasource.fileparsers.mgi;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKSwissProtFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_MRKSWISSPROT_FILE_NAME = "MRK_SwissProt.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_MRKSWISSPROT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MRKSwissProtFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			MRKSwissProtFileParser parser = new MRKSwissProtFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* MGI:012345 A2dc21 O rock protein, epsilon 1 63.9 2 Q12C456 P98765 */
				MRKSwissProtFileData record1 = parser.next();
				assertEquals(new MgiGeneID("MGI:012345"), record1.getMgiAccessionID());
				assertEquals(new String("2"), record1.getChromosome());
				assertEquals("63.9", record1.getcM_Position());
				assertEquals(new String("A2dc21"), record1.getMarkerSymbol());
				assertEquals("O", record1.getStatus());
				assertEquals(new String("rock protein, epsilon 1"), record1.getMarkerName());
				assertEquals(2, record1.getSwissProtAccessionIDs().size());
				Set<UniProtID> swissProtIDs = new HashSet<UniProtID>();
				swissProtIDs.add(new UniProtID("Q12C45"));
				swissProtIDs.add(new UniProtID("P98765"));
				assertEquals(swissProtIDs, record1.getSwissProtAccessionIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* MGI:12121212 Acdc22 O rockprotein, mu 1 syntenic 5 Q7890 */
				MRKSwissProtFileData record2 = parser.next();
				assertEquals(new MgiGeneID("MGI:12121212"), record2.getMgiAccessionID());
				assertEquals(new String("5"), record2.getChromosome());
				assertEquals("syntenic", record2.getcM_Position());
				assertEquals(new String("Acdc22"), record2.getMarkerSymbol());
				assertEquals("O", record2.getStatus());
				assertEquals(new String("rockprotein, mu 1"), record2.getMarkerName());
				assertEquals(1, record2.getSwissProtAccessionIDs().size());
				Set<UniProtID> swissProtIDs = new HashSet<UniProtID>();
				swissProtIDs.add(new UniProtID("Q78A90"));
				assertEquals(swissProtIDs, record2.getSwissProtAccessionIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* MGI:0995 Acdc23 O rock protein complex, sigma 1 syntenic 12 QWV15 P5656 */
				MRKSwissProtFileData record3 = parser.next();
				assertEquals(new MgiGeneID("MGI:0995"), record3.getMgiAccessionID());
				assertEquals(new String("12"), record3.getChromosome());
				assertEquals("syntenic", record3.getcM_Position());
				assertEquals(new String("Acdc23"), record3.getMarkerSymbol());
				assertEquals("O", record3.getStatus());
				assertEquals(new String("rock protein complex, sigma 1"), record3.getMarkerName());
				assertEquals(2, record3.getSwissProtAccessionIDs().size());
				Set<UniProtID> swissProtIDs = new HashSet<UniProtID>();
				swissProtIDs.add(new UniProtID("Q1V155"));
				swissProtIDs.add(new UniProtID("P56565"));
				assertEquals(swissProtIDs, record3.getSwissProtAccessionIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	@Test
	public void testGetSwissProtID2MgiIDsMap() throws Exception {
		Map<UniProtID, Set<MgiGeneID>> swissProtID2MgiIDMap = MRKSwissProtFileParser.getSwissProtID2MgiIDsMap(
				sampleInputFile, CharacterEncoding.US_ASCII);

		Map<UniProtID, Set<MgiGeneID>> expectedSwissProtID2MgiIDMap = new HashMap<UniProtID, Set<MgiGeneID>>();
		Set<MgiGeneID> mgiIDSet = new HashSet<MgiGeneID>();
		mgiIDSet.add(new MgiGeneID("MGI:012345"));
		expectedSwissProtID2MgiIDMap.put(new UniProtID("Q12C45"), mgiIDSet);
		mgiIDSet = new HashSet<MgiGeneID>();
		mgiIDSet.add(new MgiGeneID("MGI:012345"));
		expectedSwissProtID2MgiIDMap.put(new UniProtID("P98765"), mgiIDSet);

		mgiIDSet = new HashSet<MgiGeneID>();
		mgiIDSet.add(new MgiGeneID("MGI:12121212"));
		expectedSwissProtID2MgiIDMap.put(new UniProtID("Q78A90"), mgiIDSet);

		mgiIDSet = new HashSet<MgiGeneID>();
		mgiIDSet.add(new MgiGeneID("MGI:0995"));
		expectedSwissProtID2MgiIDMap.put(new UniProtID("Q1V155"), mgiIDSet);
		mgiIDSet = new HashSet<MgiGeneID>();
		mgiIDSet.add(new MgiGeneID("MGI:0995"));
		expectedSwissProtID2MgiIDMap.put(new UniProtID("P56565"), mgiIDSet);

		assertEquals(expectedSwissProtID2MgiIDMap, swissProtID2MgiIDMap);

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_012345> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/mgi/MgiSwissprotRecord1> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_012345> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToMgiGeneICE> <http://kabob.ucdenver.edu/iao/mgi/MGI_012345_ICE> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_012345> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToUniProtICE> <http://kabob.ucdenver.edu/iao/uniprot/Q12C45_ICE> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_012345> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToUniProtICE> <http://kabob.ucdenver.edu/iao/uniprot/P98765_ICE> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_12121212> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/mgi/MgiSwissprotRecord1> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_12121212> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToMgiGeneICE> <http://kabob.ucdenver.edu/iao/mgi/MGI_12121212_ICE> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_12121212> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToUniProtICE> <http://kabob.ucdenver.edu/iao/uniprot/Q78A90_ICE> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_0995> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/mgi/MgiSwissprotRecord1> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_0995> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToMgiGeneICE> <http://kabob.ucdenver.edu/iao/mgi/MGI_0995_ICE> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_0995> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToUniProtICE> <http://kabob.ucdenver.edu/iao/uniprot/Q1V155_ICE> .",
						"<http://kabob.ucdenver.edu/iao/mgi/MRK_SWISSPROT_RECORD_MGI_0995> <http://kabob.ucdenver.edu/iao/mgi/isLinkedToUniProtICE> <http://kabob.ucdenver.edu/iao/uniprot/P56565_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-swissprot.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-swissprot.nt", 11);
		counts.put("kabob-meta-mgi-swissprot.nt", 6);
		return counts;
	}
}
