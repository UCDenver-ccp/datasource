package edu.ucdenver.ccp.datasource.fileparsers.gad;

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

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GadID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class GeneticAssociationDbAllTxtFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_GAD_ALLTXT_FILE_NAME = "GAD_all.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_GAD_ALLTXT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new GeneticAssociationDbAllTxtFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			GeneticAssociationDbAllTxtFileParser parser = new GeneticAssociationDbAllTxtFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				GeneticAssociationDbAllTxtFileData record1 = parser.next();
				assertEquals(new GadID(125158), record1.getGadId());
				assertEquals(false, record1.hasAssociation());
				assertEquals(new HgncGeneSymbolID("HLA-A"), record1.getGeneSymbol());
				assertEquals(new String("leukemia"), record1.getBroadPhenotype());
				assertEquals(new String("CANCER"), record1.getDiseaseClass());
				assertEquals(String.format("Incorrect EntrezGeneID"), new NcbiGeneId(3105), record1.getEntrezGeneID());
				assertEquals(String.format("Incorrect UniGeneID"), new UniGeneID("Hs.181244"),
						record1.getUnigeneAccessionID());
				assertEquals(String.format("Incorrect RefSeqID"), new RefSeqID("NM_002116.5"), record1.getNucleotideID());
				assertEquals(String.format("Incorrect OmimID"), new OmimID(142800), record1.getOmimID());
				assertEquals(String.format("Incorrect PubMedID"), new PubMedID(16120569), record1.getPubmedID());
				assertEquals(String.format("Incorrect Association Status"), new String("UNSPECIFIED"),
						record1.getAssociationStatus());

			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				GeneticAssociationDbAllTxtFileData record2 = parser.next();
				assertEquals(new GadID(125159), record2.getGadId());
				assertEquals(false, record2.hasAssociation());
				assertEquals(new HgncGeneSymbolID("HLA-A"), record2.getGeneSymbol());
				assertEquals(new String("leukemia"), record2.getBroadPhenotype());
				assertEquals(new String("CANCER"), record2.getDiseaseClass());
				assertEquals(String.format("Incorrect EntrezGeneID"), new NcbiGeneId(3105), record2.getEntrezGeneID());
				assertEquals(String.format("Incorrect UniGeneID"), new UniGeneID("Hs.181244"),
						record2.getUnigeneAccessionID());
				assertEquals(String.format("Incorrect RefSeqID"), new RefSeqID("NM_002116.5"), record2.getNucleotideID());
				assertEquals(String.format("Incorrect OmimID"), new OmimID(142800), record2.getOmimID());
				assertEquals(String.format("Incorrect PubMedID"), new PubMedID(16143070), record2.getPubmedID());
				assertEquals(String.format("Incorrect Association Status"), new String("UNSPECIFIED"),
						record2.getAssociationStatus());

			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				GeneticAssociationDbAllTxtFileData record3 = parser.next();
				assertEquals(new GadID(125160), record3.getGadId());
				assertEquals(true, record3.hasAssociation());
				assertEquals(new HgncGeneSymbolID("HLA-A"), record3.getGeneSymbol());
				assertEquals(new String("alopecia areata"), record3.getBroadPhenotype());
				assertEquals(new String("IMMUNE"), record3.getDiseaseClass());
				assertEquals(String.format("Incorrect EntrezGeneID"), new NcbiGeneId(3105), record3.getEntrezGeneID());
				assertEquals(String.format("Incorrect UniGeneID"), new UniGeneID("Hs.181244"),
						record3.getUnigeneAccessionID());
				assertEquals(String.format("Incorrect RefSeqID"), new RefSeqID("NM_002116.5"), record3.getNucleotideID());
				assertEquals(String.format("Incorrect OmimID"), new OmimID(142800), record3.getOmimID());
				assertEquals(String.format("Incorrect PubMedID"), new PubMedID(16185849), record3.getPubmedID());
				assertEquals(String.format("Incorrect Association Status"), new String("YES"),
						record3.getAssociationStatus());

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
		final String NS = "<http://kabob.ucdenver.edu/ice/gad/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "GAD_125158_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/gad/GadIce1> .",
						NS + "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/hasGadID> \"GAD_125158\"@en .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/hasBroadPhenotype> \"leukemia\"@en .",
						NS + "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/hasDiseaseClass> \"CANCER\"@en .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/isLinkedToHgncGeneSymbolICE> <http://www.genenames.org/hgnc/HLA-A_ICE> .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NM_002116.5_ICE> .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_3105_ICE> .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/isLinkedToUniGeneICE> <http://www.ncbi.nlm.nih.gov/unigene/UNIGENE_Hs.181244_ICE> .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/isLinkedToOmimICE> <http://www.ncbi.nlm.nih.gov/omim/OMIM_142800_ICE> .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/hasAssociationBtwnPhenotypeAndGene> \"UNSPECIFIED\"@en .",
						NS
								+ "GAD_125158_ICE> <http://geneticassociationdb.nih.gov/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_16120569_ICE> .",
						NS
								+ "GAD_125159_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/gad/GadIce1> .",
						NS + "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/hasGadID> \"GAD_125159\"@en .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/hasBroadPhenotype> \"leukemia\"@en .",
						NS + "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/hasDiseaseClass> \"CANCER\"@en .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/isLinkedToHgncGeneSymbolICE> <http://www.genenames.org/hgnc/HLA-A_ICE> .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NM_002116.5_ICE> .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_3105_ICE> .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/isLinkedToUniGeneICE> <http://www.ncbi.nlm.nih.gov/unigene/UNIGENE_Hs.181244_ICE> .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/isLinkedToOmimICE> <http://www.ncbi.nlm.nih.gov/omim/OMIM_142800_ICE> .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/hasAssociationBtwnPhenotypeAndGene> \"UNSPECIFIED\"@en .",
						NS
								+ "GAD_125159_ICE> <http://geneticassociationdb.nih.gov/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_16143070_ICE> .",
						NS
								+ "GAD_125160_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/gad/GadIce1> .",
						NS + "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/hasGadID> \"GAD_125160\"@en .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/hasBroadPhenotype> \"alopecia areata\"@en .",
						NS + "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/hasDiseaseClass> \"IMMUNE\"@en .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/isLinkedToHgncGeneSymbolICE> <http://www.genenames.org/hgnc/HLA-A_ICE> .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NM_002116.5_ICE> .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_3105_ICE> .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/isLinkedToUniGeneICE> <http://www.ncbi.nlm.nih.gov/unigene/UNIGENE_Hs.181244_ICE> .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/isLinkedToOmimICE> <http://www.ncbi.nlm.nih.gov/omim/OMIM_142800_ICE> .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/hasAssociationBtwnPhenotypeAndGene> \"YES\"@en .",
						NS
								+ "GAD_125160_ICE> <http://geneticassociationdb.nih.gov/isLinkedToPubMedICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_16185849_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "gad-all.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("gad-all.nt", 33);
		counts.put("kabob-meta-gad-all.nt", 6);
		return counts;
	}

}
