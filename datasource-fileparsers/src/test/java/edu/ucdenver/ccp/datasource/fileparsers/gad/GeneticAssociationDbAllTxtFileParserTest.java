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
package edu.ucdenver.ccp.fileparsers.gad;

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
import edu.ucdenver.ccp.datasource.identifiers.gad.GadID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

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
				assertEquals(new GadBroadPhenotype("leukemia"), record1.getBroadPhenotype());
				assertEquals(new GadDiseaseClass("CANCER"), record1.getDiseaseClass());
				assertEquals(String.format("Incorrect EntrezGeneID"), new EntrezGeneID(3105), record1.getEntrezGeneID());
				assertEquals(String.format("Incorrect UniGeneID"), new UniGeneID("Hs.181244"),
						record1.getUnigeneAccessionID());
				assertEquals(String.format("Incorrect RefSeqID"), new RefSeqID("NM_002116.5"), record1.getNucleotideID());
				assertEquals(String.format("Incorrect OmimID"), new OmimID(142800), record1.getOmimID());
				assertEquals(String.format("Incorrect PubMedID"), new PubMedID(16120569), record1.getPubmedID());
				assertEquals(String.format("Incorrect Association Status"), new GadAssociationStatus("UNSPECIFIED"),
						record1.getAssociationStatus());

			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				GeneticAssociationDbAllTxtFileData record2 = parser.next();
				assertEquals(new GadID(125159), record2.getGadId());
				assertEquals(false, record2.hasAssociation());
				assertEquals(new HgncGeneSymbolID("HLA-A"), record2.getGeneSymbol());
				assertEquals(new GadBroadPhenotype("leukemia"), record2.getBroadPhenotype());
				assertEquals(new GadDiseaseClass("CANCER"), record2.getDiseaseClass());
				assertEquals(String.format("Incorrect EntrezGeneID"), new EntrezGeneID(3105), record2.getEntrezGeneID());
				assertEquals(String.format("Incorrect UniGeneID"), new UniGeneID("Hs.181244"),
						record2.getUnigeneAccessionID());
				assertEquals(String.format("Incorrect RefSeqID"), new RefSeqID("NM_002116.5"), record2.getNucleotideID());
				assertEquals(String.format("Incorrect OmimID"), new OmimID(142800), record2.getOmimID());
				assertEquals(String.format("Incorrect PubMedID"), new PubMedID(16143070), record2.getPubmedID());
				assertEquals(String.format("Incorrect Association Status"), new GadAssociationStatus("UNSPECIFIED"),
						record2.getAssociationStatus());

			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				GeneticAssociationDbAllTxtFileData record3 = parser.next();
				assertEquals(new GadID(125160), record3.getGadId());
				assertEquals(true, record3.hasAssociation());
				assertEquals(new HgncGeneSymbolID("HLA-A"), record3.getGeneSymbol());
				assertEquals(new GadBroadPhenotype("alopecia areata"), record3.getBroadPhenotype());
				assertEquals(new GadDiseaseClass("IMMUNE"), record3.getDiseaseClass());
				assertEquals(String.format("Incorrect EntrezGeneID"), new EntrezGeneID(3105), record3.getEntrezGeneID());
				assertEquals(String.format("Incorrect UniGeneID"), new UniGeneID("Hs.181244"),
						record3.getUnigeneAccessionID());
				assertEquals(String.format("Incorrect RefSeqID"), new RefSeqID("NM_002116.5"), record3.getNucleotideID());
				assertEquals(String.format("Incorrect OmimID"), new OmimID(142800), record3.getOmimID());
				assertEquals(String.format("Incorrect PubMedID"), new PubMedID(16185849), record3.getPubmedID());
				assertEquals(String.format("Incorrect Association Status"), new GadAssociationStatus("YES"),
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
