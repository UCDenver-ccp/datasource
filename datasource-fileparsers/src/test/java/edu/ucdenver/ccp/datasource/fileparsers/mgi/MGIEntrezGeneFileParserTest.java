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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;

public class MGIEntrezGeneFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "MGI_EntrezGene.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MGIEntrezGeneFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws Exception {
		MGIEntrezGeneFileParser parser = new MGIEntrezGeneFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

		// MGI:87853 a O nonagouti 89.00 2 Gene 50518 agouti|As|agouti signal protein|ASP
		if (parser.hasNext())
			checkMgiEntrezGeneRecord(parser.next(), "MGI:87853", "a", "O", "nonagouti", "89.00", 2, MgiGeneType.GENE,
					new HashSet<String>(), 50518,
					CollectionsUtil.createSet("agouti", "As", "agouti signal protein", "ASP"));
		else
			fail("Parser should have returned the first record");

		// MGI:87854 Pzp O pregnancy zone protein 62.00 6 Gene MGI:2141514 11287
		if (parser.hasNext())
			checkMgiEntrezGeneRecord(parser.next(), "MGI:87854", "Pzp", "O", "pregnancy zone protein", "62.00", 6,
					MgiGeneType.GENE, CollectionsUtil.createSet("MGI:2141514"), 11287, new HashSet<String>());
		else
			fail("Parser should have returned the second record");

		// MGI:87859 Abl1 O c-abl oncogene 1, receptor tyrosine kinase 21.00 2 Gene
		// MGI:2138905|MGI:2443781 11350 c-Abl
		if (parser.hasNext())
			checkMgiEntrezGeneRecord(parser.next(), "MGI:87859", "Abl1", "O",
					"c-abl oncogene 1, receptor tyrosine kinase", "21.00", 2, MgiGeneType.GENE,
					CollectionsUtil.createSet("MGI:2138905", "MGI:2443781"), 11350, CollectionsUtil.createSet("c-Abl"));
		else
			fail("Parser should have returned the third record");

		assertFalse(parser.hasNext());
	}

	private void checkMgiEntrezGeneRecord(MGIEntrezGeneFileData record, String expectedMgiID, String expectedSymbol,
			String expectedStatus, String expectedMarkerName, String expectedCmPosition, int expectedChromosome,
			MgiGeneType expectedGeneType, Set<String> expectedSecondaryIDs, int expectedEntrezGeneID,
			Set<String> expectedSynoyms) {
		assertEquals(String.format("Primary MGI ID not as expected"), new MgiGeneID(expectedMgiID),
				record.getMgiAccessionID());
		assertEquals(String.format("Marker symbol not as expected"), new String(expectedSymbol),
				record.getMarkerSymbol());
		assertEquals(String.format("Status not as expected"), expectedStatus, record.getStatus());
		assertEquals(String.format("Marker name not as expected"), new String(expectedMarkerName),
				record.getMarkerName());
		assertEquals(String.format("Chromosome not as expected"), Integer.toString(expectedChromosome),
				record.getChromosome());
		assertEquals(String.format("cM position not as expected"), expectedCmPosition, record.getcM_Position());
		assertEquals(String.format("Entrez Gene Id not as expected"), new NcbiGeneId(expectedEntrezGeneID),
				record.getEntrezGeneID());
		Set<MgiGeneID> secondaryIDs = new HashSet<MgiGeneID>();
		for (String secondaryID : expectedSecondaryIDs)
			secondaryIDs.add(new MgiGeneID(secondaryID));
		assertEquals(String.format("Secondary accession IDs not as expected"), secondaryIDs,
				record.getSecondaryAccessionIDs());
		Set<String> synonyms = new HashSet<String>();
		for (String syn : expectedSynoyms)
			synonyms.add(new String(syn));
		assertEquals(String.format("Synonyms not as expected"), synonyms, record.getSynonyms());
		assertEquals(String.format("Gene type not as expected"), expectedGeneType, record.getType());
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87853> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEntrezGeneRecord> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87853> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_87853_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87853> <http://www.informatics.jax.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_50518_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87854> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEntrezGeneRecord> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87854> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_87854_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87854> <http://www.informatics.jax.org/hasSecondaryMgiGeneID> <http://www.informatics.jax.org/MGI_2141514_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87854> <http://www.informatics.jax.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_11287_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87859> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEntrezGeneRecord> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87859> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_87859_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87859> <http://www.informatics.jax.org/hasSecondaryMgiGeneID> <http://www.informatics.jax.org/MGI_2138905_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87859> <http://www.informatics.jax.org/hasSecondaryMgiGeneID> <http://www.informatics.jax.org/MGI_2443781_ICE> .",
						"<http://www.informatics.jax.org/MGI_ENTREZGENE_FILE_RECORD_MGI_87859> <http://www.informatics.jax.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_11350_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-entrezgene.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-entrezgene.nt", 12);
		counts.put("kabob-meta-mgi-entrezgene.nt", 6);
		return counts;
	}

}
