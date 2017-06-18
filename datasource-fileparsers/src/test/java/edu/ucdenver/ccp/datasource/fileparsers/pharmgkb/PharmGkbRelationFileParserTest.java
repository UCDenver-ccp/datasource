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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
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
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

public class PharmGkbRelationFileParserTest extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "relationships.tsv";
	}

	@Override
	protected RecordReader<PharmGkbRelationFileRecord> initSampleRecordReader() throws IOException {
		return new PharmGkbRelationFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		RecordReader<PharmGkbRelationFileRecord> reader = initSampleRecordReader();
		PharmGkbRelationFileRecord r = reader.next();
		assertEquals("PA356", CollectionsUtil.getSingleElement(r.getEntity1Id()).getId());
		assertEquals("TPMT", r.getEntity1Name());
		assertEquals("Gene", r.getEntity1Type());
		assertEquals("PA164754994", CollectionsUtil.getSingleElement(r.getEntity2Id()).getId());
		assertEquals("s-adenosylmethionine", r.getEntity2Name());
		assertEquals("Drug", r.getEntity2Type());
		assertEquals(CollectionsUtil.createSet("VariantAnnotation"), r.getEvidence());
		assertEquals("associated", r.getAssociation());
		assertEquals(CollectionsUtil.createSet(new PubMedID(16220112)), new HashSet<PubMedID>(r.getPmids()));

		r = reader.next();
		assertEquals("PA443560", CollectionsUtil.getSingleElement(r.getEntity1Id()).getId());
		assertEquals("Breast Neoplasms", r.getEntity1Name());
		assertEquals("Disease", r.getEntity1Type());
		// should be revised to look for two elements in the id collection
//		assertEquals("CYP2A6 *12A, CYP2A6 *9A", CollectionsUtil.getSingleElement(r.getEntity2Id()).getId());
		assertEquals("CYP2A6 *12A, CYP2A6 *9A", r.getEntity2Name());
		assertEquals("Haplotype", r.getEntity2Type());
		assertEquals(CollectionsUtil.createSet("VariantAnnotation"), r.getEvidence());
		assertEquals("associated", r.getAssociation());
		assertEquals(CollectionsUtil.createSet(new PubMedID(21975350)), new HashSet<PubMedID>(r.getPmids()));

		r = reader.next();
		assertEquals("PA443560", CollectionsUtil.getSingleElement(r.getEntity1Id()).getId());
		assertEquals("chr17:41209080 (hg19)", CollectionsUtil.getSingleElement(r.getEntity2Id()).getId());

		assertEquals(CollectionsUtil.createSet(new PubMedID(11896095), new PubMedID(7611277), new PubMedID(7837387),
				new PubMedID(7894492), new PubMedID(8533757), new PubMedID(8571953), new PubMedID(8898735),
				new PubMedID(9042929), new PubMedID(9150149), new PubMedID(9333265)),
				new HashSet<PubMedID>(r.getPmids()));

		r = reader.next();
		assertEquals(CollectionsUtil.createSet("ClinicalAnnotation", "VariantAnnotation"), r.getEvidence());

		assertFalse(reader.hasNext());
	}

	private boolean containsAll(Collection<DataSourceIdentifier> relations, List<String> ids) {
		Set<String> relationIds = new HashSet<String>(ids);
		for (DataSourceIdentifier relation : relations) {
			relationIds.remove(relation.getId().toString());
		}

		return relationIds.isEmpty();
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA267_Drug_PA165110729_RSID_rs1045642_RSID_rs1045642_RSID_rs2032582> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.pharmgkb.org/PharmGkb_Relationship_Record> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA267_Drug_PA165110729_RSID_rs1045642_RSID_rs1045642_RSID_rs2032582> <http://www.pharmgkb.org/isLinkedToEntity1ICE> <http://www.pharmgkb.org/PA267_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA267_Drug_PA165110729_RSID_rs1045642_RSID_rs1045642_RSID_rs2032582> <http://www.pharmgkb.org/isLinkedToEntity2ICE> <http://www.pharmgkb.org/PA165110729_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA267_Drug_PA165110729_RSID_rs1045642_RSID_rs1045642_RSID_rs2032582> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.ncbi.nlm.nih.gov/SNP/rs1045642_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA267_Drug_PA165110729_RSID_rs1045642_RSID_rs1045642_RSID_rs2032582> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.ncbi.nlm.nih.gov/SNP/rs1045642_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA267_Drug_PA165110729_RSID_rs1045642_RSID_rs1045642_RSID_rs2032582> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.ncbi.nlm.nih.gov/SNP/rs2032582_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Drug_PA164712423_Disease_PA443298_PMID_569544072_PMID_569544021_PMID_569543943> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.pharmgkb.org/PharmGkb_Relationship_Record> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Drug_PA164712423_Disease_PA443298_PMID_569544072_PMID_569544021_PMID_569543943> <http://www.pharmgkb.org/isLinkedToEntity1ICE> <http://www.pharmgkb.org/PA164712423_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Drug_PA164712423_Disease_PA443298_PMID_569544072_PMID_569544021_PMID_569543943> <http://www.pharmgkb.org/isLinkedToEntity2ICE> <http://www.pharmgkb.org/PA443298_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Drug_PA164712423_Disease_PA443298_PMID_569544072_PMID_569544021_PMID_569543943> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_569544072_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Drug_PA164712423_Disease_PA443298_PMID_569544072_PMID_569544021_PMID_569543943> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_569544021_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Drug_PA164712423_Disease_PA443298_PMID_569544072_PMID_569544021_PMID_569543943> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_569543943_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA55_Disease_PA446850_PMID_569543559_Pathway_PA165363284> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.pharmgkb.org/PharmGkb_Relationship_Record> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA55_Disease_PA446850_PMID_569543559_Pathway_PA165363284> <http://www.pharmgkb.org/isLinkedToEntity1ICE> <http://www.pharmgkb.org/PA55_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA55_Disease_PA446850_PMID_569543559_Pathway_PA165363284> <http://www.pharmgkb.org/isLinkedToEntity2ICE> <http://www.pharmgkb.org/PA446850_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA55_Disease_PA446850_PMID_569543559_Pathway_PA165363284> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_569543559_ICE> .",
						"<http://www.pharmgkb.org/PHARMGKB_RELATION_RECORD_Gene_PA55_Disease_PA446850_PMID_569543559_Pathway_PA165363284> <http://www.pharmgkb.org/isLinkedToProvenanceICE> <http://www.pharmgkb.org/PA165363284_ICE> .");
		Map<File, List<String>> file2ExpectedLinesMap = new HashMap<File, List<String>>();
		file2ExpectedLinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "pharmgkb-relations.nt"),
				lines);
		return file2ExpectedLinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("pharmgkb-relations.nt", 17);
		counts.put("kabob-meta-pharmgkb-relations.nt", 6);
		return counts;
	}

}
