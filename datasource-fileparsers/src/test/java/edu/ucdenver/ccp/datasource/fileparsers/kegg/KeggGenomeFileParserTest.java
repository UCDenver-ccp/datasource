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
package edu.ucdenver.ccp.fileparsers.kegg;

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
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

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
			assertEquals(new KeggSpeciesCode("hsa"), record1.getThreeLetterCode());
			assertEquals(new NcbiTaxonomyID(9606), record1.getNcbiTaxonomyID());
			position1 = record1.getByteOffset();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* ptr -- 9598 */
			KeggGenomeFileData record2 = parser.next();
			assertEquals(new KeggSpeciesCode("ptr"), record2.getThreeLetterCode());
			assertEquals(new NcbiTaxonomyID(9598), record2.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		long position3 = -1;
		if (parser.hasNext()) {
			/* mcc -- 9544 */
			KeggGenomeFileData record3 = parser.next();
			assertEquals(new KeggSpeciesCode("mcc"), record3.getThreeLetterCode());
			assertEquals(new NcbiTaxonomyID(9544), record3.getNcbiTaxonomyID());
			position3 = record3.getByteOffset();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* mcc2 -- 9544 */
			KeggGenomeFileData record4 = parser.next();
			assertEquals(new KeggSpeciesCode("mcc2"), record4.getThreeLetterCode());
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
			assertEquals(new KeggSpeciesCode("hsa"), record1.getThreeLetterCode());
			assertEquals(new NcbiTaxonomyID(9606), record1.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* ptr -- 9598 */
			KeggGenomeFileData record2 = parser.next();
			assertEquals(new KeggSpeciesCode("ptr"), record2.getThreeLetterCode());
			assertEquals(new NcbiTaxonomyID(9598), record2.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* mcc -- 9544 */
			KeggGenomeFileData record3 = parser.next();
			assertEquals(new KeggSpeciesCode("mcc"), record3.getThreeLetterCode());
			assertEquals(new NcbiTaxonomyID(9544), record3.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/* mcc2 -- 9544 */
			KeggGenomeFileData record4 = parser.next();
			assertEquals(new KeggSpeciesCode("mcc2"), record4.getThreeLetterCode());
			assertEquals(new NcbiTaxonomyID(9544), record4.getNcbiTaxonomyID());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

	@Test
	public void testGetTaxonomyID2KeggSpeciesCodeMap() throws Exception {
		Map<NcbiTaxonomyID, Set<KeggSpeciesCode>> taxonomyID2ThreeLetterCodeMap = KeggGenomeFileParser
				.getTaxonomyID2KeggSpeciesCodeMap(sampleInputFile, CharacterEncoding.US_ASCII);

		Map<NcbiTaxonomyID, Set<KeggSpeciesCode>> expectedMap = new HashMap<NcbiTaxonomyID, Set<KeggSpeciesCode>>();
		expectedMap.put(new NcbiTaxonomyID(9606), CollectionsUtil.createSet(new KeggSpeciesCode("hsa")));
		expectedMap.put(new NcbiTaxonomyID(9598), CollectionsUtil.createSet(new KeggSpeciesCode("ptr")));
		expectedMap.put(new NcbiTaxonomyID(9544), CollectionsUtil.createSet(new KeggSpeciesCode("mcc"),
				new KeggSpeciesCode("mcc2")));

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
