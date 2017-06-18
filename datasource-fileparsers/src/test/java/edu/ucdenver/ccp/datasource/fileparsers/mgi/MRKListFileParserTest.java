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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKListFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_MRKLIST_FILE_NAME = "MRK_List2.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_MRKLIST_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MRKListFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		MRKListFileParser parser = new MRKListFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

		if (parser.hasNext()) {
			/*
			 * MGI:1920971 4 21.70 41452042 41464366 - 1110017D15Rik O RIKEN cDNA 1110017D15 gene
			 * Gene protein coding gene Smrp1|Cbe1
			 */
			MRKListFileData record1 = parser.next();
			assertEquals(new MgiGeneID("MGI:1920971"), record1.getMgiAccessionID());
			assertEquals(new String("4"), record1.getChromosome());
			assertEquals(new String("21.70"), record1.getcM_Position());
			assertEquals(new Integer(41452042), record1.getGenomeCoordinateStart());
			assertEquals(new Integer(41464366), record1.getGenomeCoordinateEnd());
			assertEquals("-", record1.getStrand());
			assertEquals(new String("1110017D15Rik"), record1.getMarkerSymbol());
			assertEquals("O", record1.getStatus());
			assertEquals(new String("RIKEN cDNA 1110017D15 gene"), record1.getMarkerName());
			assertEquals(MgiGeneType.GENE, record1.getMarkerType());
			assertEquals("protein coding gene", record1.getFeatureType());
			assertEquals(CollectionsUtil.createSet(new String("Smrp1"), new String("Cbe1")),
					record1.getMarkerSynonyms());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			/*
			 * MGI:1337005 11 syntenic 03.MMHAP34FRA.seq O DNA segment, 03.MMHAP34FRA.seq DNA
			 * Segment DNA segment
			 */
			MRKListFileData record2 = parser.next();
			assertEquals(new MgiGeneID("MGI:1337005"), record2.getMgiAccessionID());
			assertEquals(new String("11"), record2.getChromosome());
			assertEquals(new String("syntenic"), record2.getcM_Position());
			assertNull(record2.getGenomeCoordinateStart());
			assertNull(record2.getGenomeCoordinateEnd());
			assertNull(record2.getStrand());
			assertEquals(new String("03.MMHAP34FRA.seq"), record2.getMarkerSymbol());
			assertEquals("O", record2.getStatus());
			assertEquals(new String("DNA segment, 03.MMHAP34FRA.seq"), record2.getMarkerName());
			assertEquals("DNA segment", record2.getFeatureType());
			assertEquals(MgiGeneType.DNA_SEGMENT, record2.getMarkerType());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/mgi/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "MGI_0123456_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/mgi/MgiGeneIce1> .",
						NS + "MGI_0123456_ICE> <http://www.informatics.jax.org/hasMgiGeneID> \"MGI:0123456\"@en .",
						NS
								+ "MGI_0123456_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.informatics.jax.org/MGI_0123456> .",
						NS
								+ "MGI_0123456_ICE> <http://www.informatics.jax.org/hasGeneSymbol> \"02.MHPA34FRA.seq\"@en .",
						NS
								+ "MGI_0123456_ICE> <http://www.informatics.jax.org/hasGeneName> \"DNA segment, 02.MHPA34FRA.seq\"@en .",
						NS + "MGI_0123456_ICE> <http://www.informatics.jax.org/hasChromosomeNumber> \"8\"@en .",
						NS
								+ "MGI_1357979_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/mgi/MgiGeneIce1> .",
						NS + "MGI_1357979_ICE> <http://www.informatics.jax.org/hasMgiGeneID> \"MGI:1357979\"@en .",
						NS
								+ "MGI_1357979_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.informatics.jax.org/MGI_1357979> .",
						NS + "MGI_1357979_ICE> <http://www.informatics.jax.org/hasGeneSymbol> \"03C82F\"@en .",
						NS
								+ "MGI_1357979_ICE> <http://www.informatics.jax.org/hasGeneName> \"DNA segment, 03C82F (Research Genetics)\"@en .",
						NS + "MGI_1357979_ICE> <http://www.informatics.jax.org/hasChromosomeNumber> \"2\"@en .",
						NS
								+ "MGI_1010101_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/mgi/MgiGeneIce1> .",
						NS + "MGI_1010101_ICE> <http://www.informatics.jax.org/hasMgiGeneID> \"MGI:1010101\"@en .",
						NS
								+ "MGI_1010101_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.informatics.jax.org/MGI_1010101> .",
						NS + "MGI_1010101_ICE> <http://www.informatics.jax.org/hasGeneSymbol> \"03C82R\"@en .",
						NS
								+ "MGI_1010101_ICE> <http://www.informatics.jax.org/hasGeneName> \"DNA segment, 03C82R (Research Genetics)\"@en .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-gene.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-gene.nt", 17);
		counts.put("kabob-meta-mgi-gene.nt", 6);
		return counts;
	}

}
