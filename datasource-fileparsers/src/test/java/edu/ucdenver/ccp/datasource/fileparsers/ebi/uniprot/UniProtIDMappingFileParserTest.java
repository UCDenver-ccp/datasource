package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IpiID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

@Ignore("sample file needs to be updated to match current format: remove IPI Ids is one of the necessary changes")
public class UniProtIDMappingFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_INPUT_FILE_NAME = "UniProt_idmapping_selected.tab";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_INPUT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new UniProtIDMappingFileRecordReader(sampleInputFile, CharacterEncoding.US_ASCII, null);
	}

	@Test
	public void testParser() {
		try {
			UniProtIDMappingFileRecordReader parser = new UniProtIDMappingFileRecordReader(sampleInputFile,
					CharacterEncoding.US_ASCII, null);

			if (parser.hasNext()) {
				validateRecord(parser.next(), new UniProtEntryName("104K_THEAN"), new NcbiTaxonomyID(5874),
						new UniProtID("Q4U9M9"), new HashSet<NcbiGeneId>(),
						CollectionsUtil.createSet(new EmblID("CR940353")),
						CollectionsUtil.createSet(new IpiID("IPI00759832")));
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				validateRecord(parser.next(), new UniProtEntryName("104K_THEPA"), new NcbiTaxonomyID(5875),
						new UniProtID("P15711"), CollectionsUtil.createSet(new NcbiGeneId(3500484)),
						CollectionsUtil.createSet(new EmblID("M29954"), new EmblID("AAGK01000004")),
						new HashSet<IpiID>());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				validateRecord(parser.next(), new UniProtEntryName("108_SOLLC"), new NcbiTaxonomyID(4081),
						new UniProtID("Q43495"), new HashSet<NcbiGeneId>(),
						CollectionsUtil.createSet(new EmblID("Z14088")), new HashSet<IpiID>());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}
	}

	private void validateRecord(UniProtIDMappingFileData record, UniProtEntryName uniProtEntryName,
			NcbiTaxonomyID ncbiTaxonomyID, UniProtID uniProtID, Set<NcbiGeneId> entrezGeneIDs, Set<EmblID> emblIDs,
			Set<IpiID> ipiIds) {
		assertEquals(uniProtEntryName, record.getUniProtEntryName());
		assertEquals(ncbiTaxonomyID, record.getTaxonomyID());
		assertEquals(uniProtID, record.getUniProtAccessionID());
		assertEquals(entrezGeneIDs, record.getEntrezGeneIDs());
	}

//	@Test
//	public void testGetUniProtID2EntrezGeneIDMap() throws Exception {
//		Map<UniProtID, Set<EntrezGeneID>> uniprot2entrezGeneIDMap = UniProtIDMappingFileRecordReader
//				.getUniProtIDToEntrezGeneIDsMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(5875));
//
//		Map<UniProtID, Set<EntrezGeneID>> expectedUniprot2entrezGeneIDMap = new HashMap<UniProtID, Set<EntrezGeneID>>();
//		Set<EntrezGeneID> egSet = CollectionsUtil.createSet(new EntrezGeneID(3500484));
//		expectedUniprot2entrezGeneIDMap.put(new UniProtID("P15711"), egSet);
//		assertEquals(expectedUniprot2entrezGeneIDMap, uniprot2entrezGeneIDMap);
//	}
//
//	@Test
//	public void testGetEntrezGeneID2UniProtIDMap() throws Exception {
//		Map<EntrezGeneID, Set<UniProtID>> entrezGeneID2UniProtIDMap = UniProtIDMappingFileRecordReader
//				.getEntrezGeneID2UniProtIDsMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(5875));
//
//		Map<EntrezGeneID, Set<UniProtID>> expectedEntrezGeneID2UniProtIDMap = new HashMap<EntrezGeneID, Set<UniProtID>>();
//		Set<UniProtID> unIdSet = CollectionsUtil.createSet(new UniProtID("P15711"));
//		expectedEntrezGeneID2UniProtIDMap.put(new EntrezGeneID(3500484), unIdSet);
//
//		assertEquals(expectedEntrezGeneID2UniProtIDMap, entrezGeneID2UniProtIDMap);
//	}
//
//	@Test
//	public void testGetEmblID2EntrezGeneIDMap() throws Exception {
//		Map<EmblID, Set<EntrezGeneID>> embl2entrezGeneIDMap = UniProtIDMappingFileRecordReader.getEmblToEntrezGeneIDsMap(
//				sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(5875));
//
//		Map<EmblID, Set<EntrezGeneID>> expectedEmbl2entrezGeneIDMap = new HashMap<EmblID, Set<EntrezGeneID>>();
//		expectedEmbl2entrezGeneIDMap.put(new EmblID("M29954"), CollectionsUtil.createSet(new EntrezGeneID("3500484")));
//		expectedEmbl2entrezGeneIDMap.put(new EmblID("AAGK01000004"),
//				CollectionsUtil.createSet(new EntrezGeneID("3500484")));
//
//		assertEquals(expectedEmbl2entrezGeneIDMap, embl2entrezGeneIDMap);
//	}
//
//	@Test
//	public void testGetEmblID2UniProtIDMap() throws Exception {
//		Map<EmblID, Set<UniProtID>> embl2UniProtIDMap = UniProtIDMappingFileRecordReader.getEmbl2UniProtIDsMap(
//				sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(5875));
//
//		Map<EmblID, Set<UniProtID>> expectedEmbl2UniProtIDMap = new HashMap<EmblID, Set<UniProtID>>();
//		expectedEmbl2UniProtIDMap.put(new EmblID("M29954"), CollectionsUtil.createSet(new UniProtID("P15711")));
//		expectedEmbl2UniProtIDMap.put(new EmblID("AAGK01000004"), CollectionsUtil.createSet(new UniProtID("P15711")));
//
//		assertEquals(expectedEmbl2UniProtIDMap, embl2UniProtIDMap);
//	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();

		List<String> lines = CollectionsUtil
				.createList(
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_Q4U9M9> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.uniprot.org/uniprot/UniProt_IdMapping_Record> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_Q4U9M9> <http://purl.uniprot.org/uniprot/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/Q4U9M9_ICE> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_Q4U9M9> <http://purl.uniprot.org/uniprot/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/CR940353_ICE> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_P15711> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.uniprot.org/uniprot/UniProt_IdMapping_Record> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_P15711> <http://purl.uniprot.org/uniprot/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/P15711_ICE> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_P15711> <http://purl.uniprot.org/uniprot/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_3500484_ICE> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_P15711> <http://purl.uniprot.org/uniprot/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AAGK01000004_ICE> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_P15711> <http://purl.uniprot.org/uniprot/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/M29954_ICE> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_Q43495> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.uniprot.org/uniprot/UniProt_IdMapping_Record> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_Q43495> <http://purl.uniprot.org/uniprot/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/Q43495_ICE> .",
						"<http://purl.uniprot.org/uniprot/UNIPROT_IDMAPPING_RECORD_Q43495> <http://purl.uniprot.org/uniprot/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/Z14088_ICE> .");
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "uniprot-idmappings.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("uniprot-idmappings.nt", 11);
		counts.put("kabob-meta-uniprot-idmappings.nt", 6);
		return counts;
	}

}
