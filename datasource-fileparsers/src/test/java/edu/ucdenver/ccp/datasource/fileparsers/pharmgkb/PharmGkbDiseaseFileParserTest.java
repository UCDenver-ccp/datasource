package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MeshID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SnoMedCtId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UmlsId;

public class PharmGkbDiseaseFileParserTest extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "diseases.tsv";
	}

	@Override
	protected RecordReader<PharmGkbDiseaseFileRecord> initSampleRecordReader() throws IOException {
		return new PharmGkbDiseaseFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		RecordReader<PharmGkbDiseaseFileRecord> reader = initSampleRecordReader();
		PharmGkbDiseaseFileRecord r = reader.next();
		assertEquals("PA128406954", r.getAccessionId().getId());
		String r1 = r.getName();
		assertEquals("Weight gain", r1);
		String r2 = r.getAlternativeNames().iterator().next();
		assertTrue(r.getAlternativeNames().size() == 1 && r2.equals("Weight increased"));
		assertEquals(0, r.getCrossReferences().size());
		// MeSH:D015430(Weight Gain),SnoMedCT:161831008(Weight increasing),SnoMedCT:262286000(Weight
		// increased),SnoMedCT:8943002(Weight gain finding),UMLS:C0043094(C0043094)
		Set<DataSourceIdentifier<?>> expectedExternalVocab = new HashSet<DataSourceIdentifier<?>>();
		expectedExternalVocab.add(new MeshID("D015430"));
		expectedExternalVocab.add(new SnoMedCtId("161831008"));
		expectedExternalVocab.add(new SnoMedCtId("262286000"));
		expectedExternalVocab.add(new SnoMedCtId("8943002"));
		expectedExternalVocab.add(new UmlsId("C0043094"));
		assertEquals(expectedExternalVocab, new HashSet<DataSourceIdentifier<?>>(r.getExternalVocabulary()));

		r = reader.next();
		assertEquals("PA128406957", r.getAccessionId().getId());
		String r3 = r.getName();
		assertEquals("5-fluorouracil associated toxicity", r3);
		assertEquals(0, r.getAlternativeNames().size());
		assertEquals(0, r.getCrossReferences().size());
		assertEquals(0, r.getExternalVocabulary().size());

		// this record checks that the parse succeeds when there is a comma in a value that is in a
		// comma-delimited field, e.g. SnoMedCT:88619007(Vascular resistance, function)
		r = reader.next();
		assertEquals("PA143488504", r.getAccessionId().getId());
		expectedExternalVocab = new HashSet<DataSourceIdentifier<?>>();
		expectedExternalVocab.add(new MeshID("D014655"));
		expectedExternalVocab.add(new SnoMedCtId("88619007"));
		expectedExternalVocab.add(new UmlsId("C0042380"));
		assertEquals(expectedExternalVocab, new HashSet<DataSourceIdentifier<?>>(r.getExternalVocabulary()));
		

		assertFalse(reader.hasNext());
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/pharmgkb/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "PA128406954_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/pharmgkb/PharmGkbDiseaseIce1> .",
						NS + "PA128406954_ICE> <http://www.pharmgkb.org/hasPharmGkbID> \"PA128406954\"@en .",
						NS
								+ "PA128406954_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.pharmgkb.org/PA128406954> .", // IS_ABOUT
						NS + "PA128406954_ICE> <http://www.pharmgkb.org/hasName> \"Weight gain\"@en .",
						NS + "PA128406954_ICE> <http://www.pharmgkb.org/hasAlternativeName> \"Weight increased\"@en .",
						NS
								+ "PA128406954_ICE> <http://www.pharmgkb.org/isLinkedToMeshICE> <http://www.nlm.nih.gov/mesh/D015430_ICE> .",
						NS
								+ "PA128406957_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/pharmgkb/PharmGkbDiseaseIce1> .",
						NS + "PA128406957_ICE> <http://www.pharmgkb.org/hasPharmGkbID> \"PA128406957\"@en .",
						NS
								+ "PA128406957_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.pharmgkb.org/PA128406957> .",
						NS
								+ "PA128406957_ICE> <http://www.pharmgkb.org/hasName> \"5-fluorouracil associated toxicity\"@en .");
		Map<File, List<String>> file2ExpectedLinesMap = new HashMap<File, List<String>>();
		file2ExpectedLinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "pharmgkb-diseases.nt"),
				lines);
		return file2ExpectedLinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("pharmgkb-diseases.nt", 10);
		counts.put("kabob-meta-pharmgkb-diseases.nt", 6);
		return counts;
	}

}
