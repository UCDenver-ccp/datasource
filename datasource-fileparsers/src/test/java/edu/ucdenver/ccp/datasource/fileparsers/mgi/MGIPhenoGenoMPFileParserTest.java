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
import java.util.Arrays;
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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MammalianPhenotypeID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class MGIPhenoGenoMPFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "MGI_PhenoGenoMP.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MGIPhenoGenoMPFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			MGIPhenoGenoMPFileParser parser = new MGIPhenoGenoMPFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/*
				 * Ptk2<tm1Imeg>/Ptk2<tm1Imeg> involves: C57BL/6 CBA MP:0005221 7478517,7566154
				 * MGI:95481
				 */
				MGIPhenoGenoMPFileData record = parser.next();
				assertEquals("Ptk2<tm1Imeg>/Ptk2<tm1Imeg>", record.getAllelicComposition());
				assertEquals("involves: C57BL/6 * CBA", record.getGeneticBackground());
				assertEquals(new MammalianPhenotypeID("MP:0005221"), record.getMammalianPhenotypeID());
				assertEquals(createSet(new PubMedID(7478517), new PubMedID(7566154)), record.getPubmedIDs());
				assertEquals(createSet(new MgiGeneID("MGI:95481")), record.getMgiAccessionIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr> involves: 129/Sv C57BL/6 CD-1
				 * MP:0001672 9226455 MGI:1347476,MGI:95841
				 */
				MGIPhenoGenoMPFileData record = parser.next();
				assertEquals("Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>", record.getAllelicComposition());
				assertEquals("involves: 129/Sv * C57BL/6 * CD-1", record.getGeneticBackground());
				assertEquals(new MammalianPhenotypeID("MP:0001672"), record.getMammalianPhenotypeID());
				assertEquals(createSet(new PubMedID(9226455)), record.getPubmedIDs());
				assertEquals(createSet(new MgiGeneID("MGI:1347476"), new MgiGeneID("MGI:95841")),
						record.getMgiAccessionIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	private <E extends Object> Set<E> createSet(E... objects) {
		return new HashSet<E>(Arrays.asList(objects));
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0005221_MGI_95481> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiPhenoGenoRecord> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0005221_MGI_95481> <http://www.informatics.jax.org/isLinkedToMammalianPhenotypeICE> <http://www.informatics.jax.org/MP_0005221_ICE> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0005221_MGI_95481> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_95481_ICE> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0005221_MGI_95481> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_7566154_ICE> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0005221_MGI_95481> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_7478517_ICE> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0001672_MGI_95841_MGI_1347476> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiPhenoGenoRecord> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0001672_MGI_95841_MGI_1347476> <http://www.informatics.jax.org/isLinkedToMammalianPhenotypeICE> <http://www.informatics.jax.org/MP_0001672_ICE> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0001672_MGI_95841_MGI_1347476> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_95841_ICE> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0001672_MGI_95841_MGI_1347476> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1347476_ICE> .",
						"<http://www.informatics.jax.org/MGI_PHENO_GENO_FILE_RECORD_MP_0001672_MGI_95841_MGI_1347476> <http://www.informatics.jax.org/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_9226455_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-phenogeno.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-phenogeno.nt", 10);
		counts.put("kabob-meta-mgi-phenogeno.nt", 6);
		return counts;
	}

}
