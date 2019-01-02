package edu.ucdenver.ccp.datasource.fileparsers.premod;

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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PreModID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacMatrixID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class PReModModuleTabFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_PREMOD_MODULETAB_FILE_NAME = "PreMod_organism_module_tab.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_PREMOD_MODULETAB_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new PReModModuleTabFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			PReModModuleTabFileParser parser = new PReModModuleTabFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/**
				 * <pre>
				 * mod000001    1   406 13.632706   497097  Xkr4    470403  18777   Lypla1  -1607998    M00192 (GR) M00750 (HMG IY) M00447 (AR) M00223 (STATx)
				 * </pre>
				 */
				PReModModuleTabFileData record1 = parser.next();
				assertEquals(new PreModID("mod000001"), record1.getPremodID());
				assertEquals(new String("1"), record1.getChromosome());
				assertEquals(406, record1.getLength());
				assertEquals(13.632706d, record1.getScore(), 0.00001);
				assertEquals(new NcbiGeneId(497097), record1.getUpstreamEntrezGeneID());
				assertEquals(new String("Xkr4"), record1.getUpstreamGeneName());
				assertEquals(470403, record1.getUpstreamGenePosition());
				assertEquals(new NcbiGeneId(18777), record1.getDownstreamEntrezGeneID());
				assertEquals(new String("Lypla1"), record1.getDownstreamGeneName());
				assertEquals(-1607998, record1.getDownstreamGenePosition());
				Set<TransfacMatrixID> tagMatrices = new HashSet<TransfacMatrixID>();
				tagMatrices.add(new TransfacMatrixID("M00192"));
				tagMatrices.add(new TransfacMatrixID("M00750"));
				tagMatrices.add(new TransfacMatrixID("M00447"));
				tagMatrices.add(new TransfacMatrixID("M00223"));
				assertEquals(tagMatrices, record1.getTagMatrices());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/**
				 * <pre>
				 * mod000002   1   893 24.409269   497097  Xkr4    456205  18777   Lypla1  -1593800    M00528 (PPAR)   M00216 (TATA)   M00720 (CAC-binding protein)    M00655 (PEA3)   M00319 (MEF-3)
				 * </pre>
				 */
				PReModModuleTabFileData record2 = parser.next();
				assertEquals(new PreModID("mod000002"), record2.getPremodID());
				assertEquals(new String("1"), record2.getChromosome());
				assertEquals(893, record2.getLength());
				assertEquals(24.409269f, record2.getScore(), 0.00000001);
				assertEquals(new NcbiGeneId(497097), record2.getUpstreamEntrezGeneID());
				assertEquals(new String("Xkr4"), record2.getUpstreamGeneName());
				assertEquals(456205, record2.getUpstreamGenePosition());
				assertEquals(new NcbiGeneId(18777), record2.getDownstreamEntrezGeneID());
				assertEquals(new String("Lypla1"), record2.getDownstreamGeneName());
				assertEquals(-1593800, record2.getDownstreamGenePosition());
				Set<TransfacMatrixID> tagMatrices = new HashSet<TransfacMatrixID>();
				tagMatrices.add(new TransfacMatrixID("M00528"));
				tagMatrices.add(new TransfacMatrixID("M00216"));
				tagMatrices.add(new TransfacMatrixID("M00720"));
				tagMatrices.add(new TransfacMatrixID("M00655"));
				tagMatrices.add(new TransfacMatrixID("M00319"));
				assertEquals(tagMatrices, record2.getTagMatrices());
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
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/premod/PremodIce1> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/hasPreModID> \"mod000001\"@en .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://genomequebec.mcgill.ca/PReMod/mod000001> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/hasChromosomeNumber> \"1\"@en .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/hasPreModScore> \"13.632706\"^^<http://www.w3.org/2001/XMLSchema#float> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToUpstreamGeneICE> <http://kabob.ucdenver.edu/iao/eg/EG_497097_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToDownstreamGeneICE> <http://kabob.ucdenver.edu/iao/eg/EG_18777_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00192_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00447_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00750_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000001_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00223_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/premod/PremodIce1> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/hasPreModID> \"mod000002\"@en .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://genomequebec.mcgill.ca/PReMod/mod000002> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/hasChromosomeNumber> \"1\"@en .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/hasPreModScore> \"24.40927\"^^<http://www.w3.org/2001/XMLSchema#float> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToUpstreamGeneICE> <http://kabob.ucdenver.edu/iao/eg/EG_497097_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToDownstreamGeneICE> <http://kabob.ucdenver.edu/iao/eg/EG_18777_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00216_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00720_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00528_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00319_ICE> .",
						"<http://kabob.ucdenver.edu/iao/premod/mod000002_ICE> <http://kabob.ucdenver.edu/iao/premod/isLinkedToTransfacMatrixICE> <http://kabob.ucdenver.edu/iao/transfac/M00655_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "premod-all.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("premod-all.nt", 23);
		counts.put("kabob-meta-premod-all.nt", 6);
		return counts;
	}

}
