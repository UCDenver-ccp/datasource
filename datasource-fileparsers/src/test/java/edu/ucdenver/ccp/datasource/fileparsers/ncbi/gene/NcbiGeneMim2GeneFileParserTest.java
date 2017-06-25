package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

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
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;

@Ignore("file format changed. Code has been updated but test would need to be revised")
public class NcbiGeneMim2GeneFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "EntrezGene_mim2gene";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new NcbiGeneMim2GeneFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws Exception {
		NcbiGeneMim2GeneFileParser parser = new NcbiGeneMim2GeneFileParser(sampleInputFile,
				CharacterEncoding.US_ASCII);

		// 100300 100188340 gene
		if (parser.hasNext())
			checkMim2GeneRecord(parser.next(), "100300", "100188340", "gene");
		else
			fail("Parser should have returned the first record");

		// 100640 216 gene
		if (parser.hasNext())
			checkMim2GeneRecord(parser.next(), "100640", "216", "gene");
		else
			fail("Parser should have returned the second record");

		assertFalse(parser.hasNext());
	}

	private void checkMim2GeneRecord(NcbiGeneMim2GeneFileData record, String expectedMimNumber,
			String expectedEntrezGeneID, String expectedAssociationType) {
		assertEquals(String.format("OmimID not as expected."), new OmimID(expectedMimNumber), record.getMimNumber());
		assertEquals(String.format("EntrezGeneID not as expected."), new NcbiGeneId(expectedEntrezGeneID),
				record.getEntrezGeneID());
		assertEquals(String.format("Omim association type not as expected."), expectedAssociationType,
				record.getAssociationType());

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/gene/EntrezGeneToOmimRecord> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_100188340_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToOmimICE> <http://www.ncbi.nlm.nih.gov/omim/OMIM_100300_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.ncbi.nlm.nih.gov/gene/hasRecordedOmimAssociationType> \"gene\"@en .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/gene/EntrezGeneToOmimRecord> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_216_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToOmimICE> <http://www.ncbi.nlm.nih.gov/omim/OMIM_100640_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.ncbi.nlm.nih.gov/gene/hasRecordedOmimAssociationType> \"gene\"@en .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "entrezgene-omim.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("entrezgene-omim.nt", 8);
		counts.put("kabob-meta-entrezgene-omim.nt", 6);
		return counts;
	}
}
