package edu.ucdenver.ccp.datasource.fileparsers.cosmic;

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

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.identifiers.cosmic.CosmicGeneSymbolID;
//import edu.ucdenver.ccp.datasource.fileparsers.cosmic.CosmicCancerGeneCensusFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.GeneFamilyTagDescriptionPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.LocusSpecificDatabaseNameLinkPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.SpecialistDbIdLinkPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileParser.WithdrawnRecordTreatment;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.other.CosmicId;
import edu.ucdenver.ccp.datasource.identifiers.other.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.other.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

public class CosmicCancerGeneCensusFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_COSMIC_FILE_NAME = "cancer_gene_census.csv";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_COSMIC_FILE_NAME;
	}

	@Override
	protected CosmicCancerGeneCensusFileParser initSampleRecordReader() throws IOException {
		return new CosmicCancerGeneCensusFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}
	// "COSMIC ID\tCOSMIC Gene Name\tApproved Name\tEntrez Gene ID\tGenome Location\tChromosome Band\tIs Somatic?\tIs Germline?\tSomatic Tumour Types\tGermline Tumour Types\tCancer Syndrome\tTissue Types\tMolecular Genetics\tRole In Cancer\tMutation Types\tTranslocation Partner\tOther Syndromes\tOther Germline Mutations\tName Synonyms";

	// ATIC,5-aminoimidazole-4-carboxamide ribonucleotide formyltransferase/IMP cyclohydrolase,471,2:215312144-215349656,2q35,yes,,ALCL,,,L,Dom,,T,ALK,,,"471,IMPCHASE,PURH,P31939,AICARFT,ENSG00000138363,ATIC"
	// ATM,ataxia telangiectasia mutated,472,11:108227625-108365508,11q22.3,yes,yes,T-PLL,"leukaemia, lymphoma, medulloblastoma, glioma",ataxia-telangiectasia,"L, O",Rec,TSG,"D, Mis, N, F, S",,,,"ATM,TELO1,ATA,ATC,ATDC,MGC74674,Q13315,AT1,DKFZp781A0353,ATE,ATD,TEL1,ENSG00000149311,472"
	// ATP1A1,"ATPase, Na+/K+ transporting, alpha 1 polypeptide",476,1:116373512-116404444,1p21,yes,,adrenal aldosterone producing adenoma,,,E,Dom,oncogene,"Mis, O",,,,"P05023,ATP1A1,476,ENSG00000163399,MGC3285,MGC51750"
	// ATP2B3,"ATPase, Ca++ transporting, plasma membrane 3",492,X:153536248-153580003,Xq28,yes,,adrenal aldosterone producing adenoma,,,E,Dom,,O,,,,"492,PMCA3,ENSG00000067842,ATP2B3"
	@Test
	public void testParser() throws Exception {
		CosmicCancerGeneCensusFileParser parser = initSampleRecordReader();  // problem

		if (parser.hasNext()) {
			CosmicCancerGeneCensusFileData dataRecord = parser.next();
			assertEquals(String.format("Returned incorrect COSMIC gene symbol ID."), new CosmicGeneSymbolID("ABI1"), dataRecord.getCosmicGeneSymbolID());
			assertEquals(String.format("Returned incorrect COSMIC gene name."), new String("abl-interactor 1"),
					dataRecord.getCosmicGeneName());
			assertEquals(new EntrezGeneID(10006), dataRecord.getEntrezGeneID());
			assertEquals(new String("10:26748570-26860863"), dataRecord.getGenomeLocation());
			assertEquals(new String("10p11.2"), dataRecord.getChromosomeBand());
			assertEquals(new String("yes"), dataRecord.getIsSomatic());
			assertEquals(new String(""), dataRecord.getIsGermline());
			assertEquals(CollectionsUtil.createSet(new String("AML")), dataRecord.getSomaticTumourTypes());
			assertEmpty(dataRecord.getGermlineTumourTypes());
			assertEmpty(dataRecord.getCancerSyndrome());
			assertEquals(CollectionsUtil.createSet(new String("L")), dataRecord.getTissueTypes());
			assertEquals(new String("Dom"), dataRecord.getMolecularGenetics());
			assertEquals(CollectionsUtil.createSet(new String("TSG")), dataRecord.getRoleInCancer());
			assertEquals(CollectionsUtil.createSet(new String("T")), dataRecord.getMutationTypes());
			assertEquals(CollectionsUtil.createSet(new String("KMT2A")), dataRecord.getTranslocationPartner());
			assertEmpty(dataRecord.getOtherSyndromes());
			assertEmpty(dataRecord.getOtherGermlineMutations());
			assertEquals(CollectionsUtil.createSet(new String("ABI1"), new String("E3B1"), new String("ABI-1"), 
					new String("SSH3BP1"), new String("10006")), 
					dataRecord.getNameSynonyms());
		} else {
			fail("Parser should have returned the first record.");
		}

		if (parser.hasNext()) {
			CosmicCancerGeneCensusFileData dataRecord = parser.next();
			assertEquals(String.format("Returned incorrect COSMIC gene symbol ID."), new CosmicGeneSymbolID("ABL1"), dataRecord.getCosmicGeneSymbolID());
			assertEquals(String.format("Returned incorrect COSMIC gene name."), new String("v-abl Abelson murine leukemia viral oncogene homolog 1"),
					dataRecord.getCosmicGeneName());
			assertEquals(new EntrezGeneID(25), dataRecord.getEntrezGeneID());
			assertEquals(new String("9:130835447-130885683"), dataRecord.getGenomeLocation());
			assertEquals(new String("9q34.1"), dataRecord.getChromosomeBand());
			assertEquals(new String("yes"), dataRecord.getIsSomatic());
			assertEquals(new String(""), dataRecord.getIsGermline());
			assertEquals(CollectionsUtil.createSet(new String("CML"), new String("ALL"), new String("T-ALL")), dataRecord.getSomaticTumourTypes());
			assertEmpty(dataRecord.getGermlineTumourTypes());
			assertEmpty(dataRecord.getCancerSyndrome());
			assertEquals(CollectionsUtil.createSet(new String("L")), dataRecord.getTissueTypes());
			assertEquals(new String("Dom"), dataRecord.getMolecularGenetics());
			assertEquals(CollectionsUtil.createSet(new String("oncogene")), dataRecord.getRoleInCancer());
			assertEquals(CollectionsUtil.createSet(new String("T"), new String("Mis")), dataRecord.getMutationTypes());
			assertEquals(CollectionsUtil.createSet(new String("BCR"), new String("ETV6"), new String("NUP214")), dataRecord.getTranslocationPartner());
			assertEmpty(dataRecord.getOtherSyndromes());
			assertEmpty(dataRecord.getOtherGermlineMutations());
			assertEquals(CollectionsUtil.createSet(new String("ABL1"), new String("p150"), new String("ABL"), new String("c-ABL"), 
					new String("JTK7"), new String("bcr/abl"), new String("v-abl"), new String("P00519"), 
					new String("ENSG00000097007"), new String("25")), dataRecord.getNameSynonyms());
		} else {
			fail("Parser should have returned the second record.");
		}
		while (parser.hasNext()) {
			CosmicCancerGeneCensusFileData dataRecord = parser.next();
		}
		//assertFalse(parser.hasNext());
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		String NS = "<http://kabob.ucdenver.edu/iao/hgnc/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "HGNC_5_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/hgnc/HgncIce1> .",
						NS
								+ "HGNC_5_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.genenames.org/hgnc/HGNC_5> .",
						NS + "HGNC_5_ICE> <http://kabob.ucdenver.edu/iao/hgnc/hasHgncGeneSymbol> \"A1BG\"@en .",
						NS
								+ "HGNC_5_ICE> <http://kabob.ucdenver.edu/iao/hgnc/isLinkedToEntrezGeneICE> <http://kabob.ucdenver.edu/iao/eg/EG_1_ICE> .",
						NS
								+ "HGNC_5_ICE> <http://kabob.ucdenver.edu/iao/hgnc/isLinkedToEnsemblGeneICE> <http://kabob.ucdenver.edu/iao/ensembl/ENSG00000121410_ICE> .",
						NS
								+ "HGNC_5_ICE> <http://kabob.ucdenver.edu/iao/hgnc/isLinkedToRefSeqICE> <http://kabob.ucdenver.edu/iao/refseq/NM_130786_ICE> .",
						NS
								+ "HGNC_37133_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/hgnc/HgncIce1> .",
						NS
								+ "HGNC_37133_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.genenames.org/hgnc/HGNC_37133> .",
						NS + "HGNC_37133_ICE> <http://kabob.ucdenver.edu/iao/hgnc/hasHgncGeneSymbol> \"A1BGAS\"@en .",
						NS
								+ "HGNC_37133_ICE> <http://kabob.ucdenver.edu/iao/hgnc/isLinkedToEntrezGeneICE> <http://kabob.ucdenver.edu/iao/eg/EG_503538_ICE> .",
						NS
								+ "HGNC_37133_ICE> <http://kabob.ucdenver.edu/iao/hgnc/isLinkedToRefSeqICE> <http://kabob.ucdenver.edu/iao/refseq/NR_015380_ICE> .");
		File expectedOutputFile = FileUtil.appendPathElementsToDirectory(outputDirectory, "hgnc-idmappings.nt");
		Map<File, List<String>> map = new HashMap<File, List<String>>();
		map.put(expectedOutputFile, lines);
		return map;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("hgnc-idmappings.nt", 11);
		counts.put("kabob-meta-hgnc-idmappings.nt", 6);
		return counts;
	}

}
