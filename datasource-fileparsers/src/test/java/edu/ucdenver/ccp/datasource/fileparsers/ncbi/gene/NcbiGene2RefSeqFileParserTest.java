package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
@Ignore("originally written for the gene2accession file, but this class was revised to parse only the gene2refseq file. Tests seem to pass but that might not be a good thing.")
public class NcbiGene2RefSeqFileParserTest extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "EntrezGene_gene2accession";
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new NcbiGene2RefseqFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			NcbiGene2RefseqFileParser parser = new NcbiGene2RefseqFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			// /* Test calling next() before hasNext() */
			// assertNull(parser.next());

			if (parser.hasNext()) {
				/*
				 * 10090 16822 PROVISIONAL NM_010696.3 118130099 NP_034826.2 31543115 AC_000033.1
				 * 83274085 36458438 36503503 + Alternate assembly (based on Celera)
				 */
				NcbiGene2RefseqFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new NcbiGeneId(16822), record.getGeneID());
				assertEquals("PROVISIONAL", record.getStatus());
				assertEquals(new RefSeqID("NM_010696.3"), record.getRNA_nucleotide_accession_dot_version());
				assertEquals(new GiNumberID(118130099), record.getRNA_nucleotide_gi());
				assertEquals(new RefSeqID("NP_034826.2"), record.getProtein_accession_dot_version());
				assertEquals(new GiNumberID(31543115), record.getProtein_gi());
				assertEquals(new RefSeqID("AC_000033.1"), record.getGenomic_nucleotide_accession_dot_version());
				assertEquals(new GiNumberID(83274085), record.getGenomic_nucleotide_gi());
				assertEquals((Integer) 36458438, record.getStart_position_on_the_genomic_accession());
				assertEquals((Integer) 36503503, record.getEnd_position_on_the_genomic_accession());
				assertEquals('+', record.getOrientation());
				assertEquals("Alternate assembly (based on Celera)", record.getAssembly());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 10090 16825 - - - - - AC108484.25 31076542 69411 76442 - -
				 */
				NcbiGene2RefseqFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new NcbiGeneId(16825), record.getGeneID());
				assertNull(record.getStatus());
				assertNull(record.getRNA_nucleotide_accession_dot_version());
				assertNull(record.getRNA_nucleotide_gi());
				assertNull(record.getProtein_accession_dot_version());
				assertNull(record.getProtein_gi());
				assertNull(record.getGenomic_nucleotide_accession_dot_version());
				assertEquals(new GiNumberID(31076542), record.getGenomic_nucleotide_gi());
				assertEquals((Integer) 69411, record.getStart_position_on_the_genomic_accession());
				assertEquals((Integer) 76442, record.getEnd_position_on_the_genomic_accession());
				assertEquals('-', record.getOrientation());
				assertNull(record.getAssembly());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 10090 16825 - - - AAC40064.1 2827901 AF024524.1 2827900 - - ? -
				 */
				NcbiGene2RefseqFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new NcbiGeneId(16825), record.getGeneID());
				assertNull(record.getStatus());
				assertNull(record.getRNA_nucleotide_accession_dot_version());
				assertNull(record.getRNA_nucleotide_gi());
				assertNull(record.getProtein_accession_dot_version());
				assertEquals(new GiNumberID(2827901), record.getProtein_gi());
				assertNull(record.getGenomic_nucleotide_accession_dot_version());
				assertEquals(new GiNumberID(2827900), record.getGenomic_nucleotide_gi());
				assertNull(record.getStart_position_on_the_genomic_accession());
				assertNull(record.getEnd_position_on_the_genomic_accession());
				assertEquals('?', record.getOrientation());
				assertNull(record.getAssembly());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	@Test
	public void testGetProteinGiID2EntrezGeneIDMap() throws Exception {
		Map<GiNumberID, Set<NcbiGeneId>> proteinAccession2EntrezGeneIDMap = NcbiGene2RefseqFileParser
				.getProteinGiID2EntrezGeneIDMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090));

		Map<GiNumberID, Set<NcbiGeneId>> expectedProteinAccession2EntrezGeneIDMap = new HashMap<GiNumberID, Set<NcbiGeneId>>();
		Set<NcbiGeneId> entrezGenes1 = new HashSet<NcbiGeneId>();
		entrezGenes1.add(new NcbiGeneId(16822));
		expectedProteinAccession2EntrezGeneIDMap.put(new GiNumberID(31543115), entrezGenes1);
		Set<NcbiGeneId> entrezGenes2 = new HashSet<NcbiGeneId>();
		entrezGenes2.add(new NcbiGeneId(16825));
		expectedProteinAccession2EntrezGeneIDMap.put(new GiNumberID(2827901), entrezGenes2);

		/* Maps should be identical */
		assertEquals(expectedProteinAccession2EntrezGeneIDMap, proteinAccession2EntrezGeneIDMap);
	}

	@Test
	public void testGetProteinAccessionID2EntrezGeneIDMap() throws Exception {
		Map<RefSeqID, Set<NcbiGeneId>> proteinAccession2EntrezGeneIDMap = NcbiGene2RefseqFileParser
				.getProteinAccessionID2EntrezGeneIDMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(
						10090));

		Map<RefSeqID, Set<NcbiGeneId>> expectedProteinAccession2EntrezGeneIDMap = new HashMap<RefSeqID, Set<NcbiGeneId>>();
		Set<NcbiGeneId> entrezGenes1 = new HashSet<NcbiGeneId>();
		entrezGenes1.add(new NcbiGeneId(16822));
		expectedProteinAccession2EntrezGeneIDMap.put(new RefSeqID("NP_034826"), entrezGenes1);

		/* Maps should be identical */
		assertEquals(expectedProteinAccession2EntrezGeneIDMap, proteinAccession2EntrezGeneIDMap);
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/gene/EntrezGene2AccessionRecord> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.informatics.jax.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_16822_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToRnaNucleotideAccession> <http://www.ncbi.nlm.nih.gov/refseq/NM_010696.3_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToRnaNucleotideGi> <http://www.ncbi.nlm.nih.gov/genbank/GI_118130099_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToProteinAccession> <http://www.ncbi.nlm.nih.gov/refseq/NP_034826.2_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToProteinGi> <http://www.ncbi.nlm.nih.gov/genbank/GI_31543115_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToGenomicNucleotideAccession> <http://www.ncbi.nlm.nih.gov/refseq/AC_000033.1_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16822> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToGenomicNucleotideGi> <http://www.ncbi.nlm.nih.gov/genbank/GI_83274085_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/gene/EntrezGene2AccessionRecord> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.informatics.jax.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_16825_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToGenomicNucleotideAccession> <http://www.ncbi.nlm.nih.gov/refseq/AC108484.25_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToGenomicNucleotideGi> <http://www.ncbi.nlm.nih.gov/genbank/GI_31076542_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/gene/EntrezGene2AccessionRecord> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.informatics.jax.org/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_16825_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToProteinAccession> <http://www.ncbi.nlm.nih.gov/refseq/AAC40064.1_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToProteinGi> <http://www.ncbi.nlm.nih.gov/genbank/GI_2827901_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToGenomicNucleotideAccession> <http://www.ncbi.nlm.nih.gov/refseq/AF024524.1_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/ENTREZ_GENE2ACCESSION_RECORD_EG_16825> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToGenomicNucleotideGi> <http://www.ncbi.nlm.nih.gov/genbank/GI_2827900_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "entrezgene-2refseqOrAccession.nt"),
				lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("entrezgene-2refseqOrAccession.nt", 18);
		counts.put("kabob-meta-entrezgene-2refseqOrAccession.nt", 6);
		return counts;
	}

}
