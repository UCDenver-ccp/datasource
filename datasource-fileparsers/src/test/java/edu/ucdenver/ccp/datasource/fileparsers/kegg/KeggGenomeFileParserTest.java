package edu.ucdenver.ccp.datasource.fileparsers.kegg;

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
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGenomeFileParserTest extends RecordReaderTester {

	private final static String SAMPLE_KEGG_GENOME_FILE_NAME = "KEGG_genome";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_KEGG_GENOME_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new KeggGenomeFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		KeggGenomeFileParser parser = new KeggGenomeFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
		validateAllRecords(parser);
		assertFalse(parser.hasNext());
	}

	@Test
	public void testParserFilePointer() throws IOException {
		KeggGenomeFileParser parser = new KeggGenomeFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

		long position1 = -1;
		if (parser.hasNext()) {
			/* hsa -- 9606 */
			KeggGenomeFileData record1 = parser.next();
			assertEquals(new String("hsa"), record1.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9606), record1.getNcbiTaxonomyID());
			assertEquals("H.sapiens", record1.getKeggSpeciesAbbreviatedName());
			position1 = record1.getByteOffset();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* ptr -- 9598 */
			KeggGenomeFileData record2 = parser.next();
			assertEquals(new String("ptr"), record2.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9598), record2.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		long position3 = -1;
		if (parser.hasNext()) {
			/* mcc -- 9544 */
			KeggGenomeFileData record3 = parser.next();
			assertEquals(new String("mcc"), record3.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9544), record3.getNcbiTaxonomyID());
			position3 = record3.getByteOffset();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* mcc2 -- 9544 */
			KeggGenomeFileData record4 = parser.next();
			assertEquals(new String("mcc2"), record4.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9544), record4.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());

		
	}

	private void validateAllRecords(KeggGenomeFileParser parser) {

		if (parser.hasNext()) {
			/* hsa -- 9606 */
			KeggGenomeFileData record1 = parser.next();
			assertEquals(new String("hsa"), record1.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9606), record1.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* ptr -- 9598 */
			KeggGenomeFileData record2 = parser.next();
			assertEquals(new String("ptr"), record2.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9598), record2.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* mcc -- 9544 */
			KeggGenomeFileData record3 = parser.next();
			assertEquals(new String("mcc"), record3.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9544), record3.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* mcc2 -- 9544 */
			KeggGenomeFileData record4 = parser.next();
			assertEquals(new String("mcc2"), record4.getKeggSpeciesCode());
			assertEquals(new NcbiTaxonomyID(9544), record4.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

	@Test
	public void testGetTaxonomyID2KeggSpeciesCodeMap() throws Exception {
		Map<NcbiTaxonomyID, Set<String>> taxonomyID2ThreeLetterCodeMap = KeggGenomeFileParser
				.getTaxonomyID2KeggSpeciesCodeMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<NcbiTaxonomyID, Set<String>> expectedMap = new HashMap<NcbiTaxonomyID, Set<String>>();
		expectedMap.put(new NcbiTaxonomyID(9606), CollectionsUtil.createSet(new String("hsa")));
		expectedMap.put(new NcbiTaxonomyID(9598), CollectionsUtil.createSet(new String("ptr")));
		expectedMap.put(new NcbiTaxonomyID(9544), CollectionsUtil.createSet(new String("mcc"),
				new String("mcc2")));

		assertEquals(expectedMap, taxonomyID2ThreeLetterCodeMap);
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_hsa> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/kegg/KeggSpeciesCode2NcbiTaxonRecord1> .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_hsa> <http://kabob.ucdenver.edu/iao/kegg/hasKeggSpeciesCode> \"hsa\"@en .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_hsa> <http://kabob.ucdenver.edu/iao/kegg/isLinkedToNcbiTaxonomyICE> <http://kabob.ucdenver.edu/iao/ncbi_taxon/NCBITaxon_9606_ICE> .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_ptr> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/kegg/KeggSpeciesCode2NcbiTaxonRecord1> .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_ptr> <http://kabob.ucdenver.edu/iao/kegg/hasKeggSpeciesCode> \"ptr\"@en .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_ptr> <http://kabob.ucdenver.edu/iao/kegg/isLinkedToNcbiTaxonomyICE> <http://kabob.ucdenver.edu/iao/ncbi_taxon/NCBITaxon_9598_ICE> .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_mcc> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/kegg/KeggSpeciesCode2NcbiTaxonRecord1> .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_mcc> <http://kabob.ucdenver.edu/iao/kegg/hasKeggSpeciesCode> \"mcc\"@en .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_mcc> <http://kabob.ucdenver.edu/iao/kegg/isLinkedToNcbiTaxonomyICE> <http://kabob.ucdenver.edu/iao/ncbi_taxon/NCBITaxon_9544_ICE> .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_mcc2> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/kegg/KeggSpeciesCode2NcbiTaxonRecord1> .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_mcc2> <http://kabob.ucdenver.edu/iao/kegg/hasKeggSpeciesCode> \"mcc2\"@en .",
						"<http://kabob.ucdenver.edu/iao/kegg/KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_mcc2> <http://kabob.ucdenver.edu/iao/kegg/isLinkedToNcbiTaxonomyICE> <http://kabob.ucdenver.edu/iao/ncbi_taxon/NCBITaxon_9544_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(
				FileUtil.appendPathElementsToDirectory(outputDirectory, "kegg-speciesCode2NcbiTaxonomyID.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("kegg-speciesCode2NcbiTaxonomyID.nt", 12);
		counts.put("kabob-meta-kegg-speciesCode2NcbiTaxonomyID.nt", 6);
		return counts;
	}
}
