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
package edu.ucdenver.ccp.fileparsers.mgi;

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
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKReferenceFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_MRKREFERENCE_FILE_NAME = "MRK_Reference.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_MRKREFERENCE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MRKReferenceFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			MRKReferenceFileParser parser = new MRKReferenceFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* MGI:121212 06100L10Rik RIKEN cDNA 06100L10 gene 1111111|2222222 */
				MRKReferenceFileData record1 = parser.next();
				assertEquals(new MgiGeneID("MGI:121212"), record1.getMgiAccessionID());
				assertEquals(new String("06100L10Rik"), record1.getMarkerSymbol());
				assertEquals(new String("RIKEN cDNA 06100L10 gene"), record1.getMarkerName());
				assertEquals(0, record1.getMarkerSynonyms().size());
				Set<String> synonyms = new HashSet<String>();
				assertEquals(0, record1.getMarkerSynonyms().size());
				assertEquals(synonyms, record1.getMarkerSynonyms());
				assertEquals(2, record1.getPubMedIDs().size());
				Set<PubMedID> pmids = new HashSet<PubMedID>();
				pmids.add(new PubMedID("1111111"));
				pmids.add(new PubMedID("2222222"));
				assertEquals(pmids, record1.getPubMedIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * MGI:131313 06100L11Rik RIKEN cDNA 06100L11 gene p23|p33|HSP34|HSP35
				 * 56565656|89898989|64646464
				 */
				MRKReferenceFileData record2 = parser.next();
				assertEquals(new MgiGeneID("MGI:131313"), record2.getMgiAccessionID());
				assertEquals(new String("06100L11Rik"), record2.getMarkerSymbol());
				assertEquals(new String("RIKEN cDNA 06100L11 gene"), record2.getMarkerName());
				assertEquals(4, record2.getMarkerSynonyms().size());
				Set<String> synonyms = new HashSet<String>();
				synonyms.add(new String("p23"));
				synonyms.add(new String("p33"));
				synonyms.add(new String("HSP34"));
				synonyms.add(new String("HSP35"));
				assertEquals(synonyms, record2.getMarkerSynonyms());
				assertEquals(3, record2.getPubMedIDs().size());
				Set<PubMedID> pmids = new HashSet<PubMedID>();
				pmids.add(new PubMedID("56565656"));
				pmids.add(new PubMedID("89898989"));
				pmids.add(new PubMedID("64646464"));
				assertEquals(pmids, record2.getPubMedIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_121212> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiReferenceRecord> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_121212> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_121212_ICE> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_121212> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_1111111_ICE> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_121212> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_2222222_ICE> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_131313> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiReferenceRecord> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_131313> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_131313_ICE> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_131313> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_89898989_ICE> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_131313> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_56565656_ICE> .",
						"<http://www.informatics.jax.org/MRD_REFERENCE_FILE_RECORD_MGI_131313> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_64646464_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-reference.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-reference.nt", 9);
		counts.put("kabob-meta-mgi-reference.nt", 6);
		return counts;
	}

}
