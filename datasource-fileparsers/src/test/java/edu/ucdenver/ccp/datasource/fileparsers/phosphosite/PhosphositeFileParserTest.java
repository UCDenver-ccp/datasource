package edu.ucdenver.ccp.datasource.fileparsers.phosphosite;

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

import java.io.IOException;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * 
 * @author Heather Underwood
 * 
 */
public class PhosphositeFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "acetylation_test.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new PhosphositeFileParser(sampleInputFile, CharacterEncoding.ISO_8859_1);
	}

	@Test
	public void testParser() throws IOException {
		PhosphositeFileParser parser = new PhosphositeFileParser(sampleInputFile, CharacterEncoding.ISO_8859_1);

		if (parser.hasNext()) {
			/*
			 * 010715
			 * Data extracted from PhosphoSitePlus(R), created by Cell Signaling Technology Inc. PhosphoSitePlus is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. Attribution must be given in written, oral and digital presentations to PhosphoSitePlus, www.phosphosite.org. Written documents should additionally cite Hornbeck PV, Kornhauser JM, Tkachev S, Zhang B, Skrzypek E, Murray B, Latham V, Sullivan M (2012) PhosphoSitePlus: a comprehensive resource for investigating the structure and function of experimentally determined post-translational modifications in man and mouse. Nucleic Acids Res. 40, D261–70.; www.phosphosite.org.
			 */
			PhosphositeFileData record1 = parser.next();
			
			assertEquals("14-3-3 beta", record1.getProteinName());
			assertEquals(new UniProtID("Q9CQV8"), record1.getAccessionId());
			assertEquals("Ywhab", record1.getGene());
			assertEquals(null, record1.getChromosomeLocation());
			assertEquals("ACETYLATION", record1.getModificationType());
			assertEquals("K5", record1.getModifiedResidue());
			assertEquals(new Integer(33347661), record1.getSiteGroupId());
			assertEquals("mouse", record1.getOrganism());
			assertEquals(new Double(28.09), record1.getMolWeight());
			assertEquals(CollectionsUtil.createSet(new String("14-3-3")), record1.getDomain());
			assertEquals("___MtMDksELVQkA", record1.getSequence());
			assertEquals(new Integer(-1), record1.getLTLitRefs());
			assertEquals(new Integer(1), record1.getMSLitRefs());
			assertEquals(new Integer(-1), record1.getMSCSTRefs());
			assertEquals(CollectionsUtil.createSet(), record1.getCSTCatalogNum());

		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			
			PhosphositeFileData record2 = parser.next();
			assertEquals("14-3-3 beta", record2.getProteinName());
			assertEquals(new UniProtID("P35213"), record2.getAccessionId());
			assertEquals("Ywhab", record2.getGene());
			assertEquals(null, record2.getChromosomeLocation());
			assertEquals("ACETYLATION", record2.getModificationType());
			assertEquals("K5", record2.getModifiedResidue());
			assertEquals(new Integer(33347661), record2.getSiteGroupId());
			assertEquals("rat", record2.getOrganism());
			assertEquals(new Double(28.05), record2.getMolWeight());
			assertEquals(CollectionsUtil.createSet(new String("14-3-3")), record2.getDomain());
			assertEquals("___MTMDkSELVQkA", record2.getSequence());
			assertEquals(new Integer(-1), record2.getLTLitRefs());
			assertEquals(new Integer(1), record2.getMSLitRefs());
			assertEquals(new Integer(-1), record2.getMSCSTRefs());
			assertEquals(CollectionsUtil.createSet(), record2.getCSTCatalogNum());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

	/*protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
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
	}*/

}
