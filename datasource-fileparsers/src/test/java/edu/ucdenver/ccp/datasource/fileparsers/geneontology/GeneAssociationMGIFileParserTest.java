package edu.ucdenver.ccp.datasource.fileparsers.geneontology;

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
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class GeneAssociationMGIFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_GENEASSOCIATIONMGI_FILE_NAME = "gene_association.mgi";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_GENEASSOCIATIONMGI_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new GeneAssociationFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			GeneAssociationFileParser parser = new GeneAssociationFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/*
				 * MGI MGI:2685383 A630055G03Rik GO:0003677 MGI:MGI:1354194 IEA SP_KW:KW-0238 F
				 * RIKEN cDNA A630055G03 gene LOC381034 gene taxon:10090 20081012 UniProtKB
				 */

				checkRecord(parser.next(), new String("MGI"), new MgiGeneID("MGI:2685383"), new String(
						"A630055G03Rik"), null, new GeneOntologyID("GO:0003677"),
						CollectionsUtil.createSet(new MgiReferenceID("MGI:MGI:1354194")), new HashSet<PubMedID>(),
						new String("IEA"), "SP_KW:KW-0238", GeneOntologyDomain.MOLECULAR_FUNCTION,
						new String("RIKEN cDNA A630055G03 gene"),
						CollectionsUtil.createSet(new String("LOC381034")), MarkerType.GENE,
						new NcbiTaxonomyID(10090), "20081012", "UniProtKB");
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * MGI MGI:1345167 Aadat GO:0005739 MGI:MGI:2682130|PMID:14651853 IDA C aminoadipate
				 * aminotransferase Kat2|KATII|mKat-2 gene taxon:10090 20071029 MGI
				 */
				checkRecord(parser.next(), new String("MGI"), new MgiGeneID("MGI:1345167"), new String(
						"Aadat"), null, new GeneOntologyID("GO:0005739"), CollectionsUtil.createSet(new MgiReferenceID(
						"MGI:MGI:2682130")), CollectionsUtil.createSet(new PubMedID(14651853)),
						new String("IDA"), null, GeneOntologyDomain.CELLULAR_COMPONENT,
						new String("aminoadipate aminotransferase"), CollectionsUtil.createSet(
								new String("Kat2"), new String("KATII"), new String(
										"mKat-2")), MarkerType.GENE, new NcbiTaxonomyID(10090), "20071029",
						"MGI");
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * MGI MGI:1098687 Aak1 NOT c MGI:MGI:2152098 IEA
				 * InterPro:IPR000719,InterPro:IPR017441,InterPro:IPR017442 F AP2 associated kinase
				 * 1 D6Ertd245e gene taxon:10090 20081012 UniProtKB
				 */

				checkRecord(parser.next(), new String("MGI"), new MgiGeneID("MGI:1098687"), new String(
						"Aak1"), "NOT", new GeneOntologyID("GO:0005524"),
						CollectionsUtil.createSet(new MgiReferenceID("MGI:MGI:2152098")), new HashSet<PubMedID>(),
						new String("IEA"),
						"InterPro:IPR000719,InterPro:IPR017441,InterPro:IPR017442",
						GeneOntologyDomain.MOLECULAR_FUNCTION, new String("AP2 associated kinase 1"),
						CollectionsUtil.createSet(new String("D6Ertd245e")), MarkerType.GENE,
						new NcbiTaxonomyID(10090), "20081012", "UniProtKB");
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	private void checkRecord(GeneAssociationFileData record, String databaseDesignation,
			DataSourceIdentifier<?> geneID, String geneSymbol, String qualifier,
			GeneOntologyID goTermID, Set<MgiReferenceID> referenceAccessionIDs, Set<PubMedID> referencePMIDs,
			String goEvidenceCode, String inferredFrom, GeneOntologyDomain ontology,
			String markerName, Set<String> markerSynonyms, MarkerType markerType,
			NcbiTaxonomyID taxonID, String modificationDate, String assignedBy) {
		assertEquals(databaseDesignation, record.getDatabaseDesignation());
		assertEquals(geneID, record.getGeneID());
		assertEquals(geneSymbol, record.getGeneSymbol());
		assertEquals(qualifier, record.getAnnotationQualifier());
		assertEquals(goTermID, record.getGoTermID());
		assertEquals(referenceAccessionIDs, record.getReferenceAccessionIDs());
		assertEquals(goEvidenceCode, record.getGoEvidenceCode());
		assertEquals(inferredFrom, record.getInferredFrom());
		assertEquals(ontology, record.getOntology());
		assertEquals(markerName, record.getMarkerName());
		assertEquals(markerSynonyms, record.getMarkerSynonyms());
		assertEquals(markerType, record.getMarkerType());
		assertEquals(taxonID, record.getTaxonID());
		assertEquals(modificationDate, record.getModificationDate());
		assertEquals(assignedBy, record.getAssignedBy());
		assertEquals(referencePMIDs, record.getReferencePMIDs());
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_2685383GO_0003677IEA> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.geneontology.org/GeneAssociationRecord> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_2685383GO_0003677IEA> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_2685383_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_2685383GO_0003677IEA> <http://www.informatics.jax.org/isLinkedToGeneOntologyICE> <http://purl.org/obo/owl/GO#GO_0003677_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_2685383GO_0003677IEA> <http://www.informatics.jax.org/hasGoEvidencCode> <http://www.geneontology.org/GO.evidence.shtml#IEA_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_2685383GO_0003677IEA> <http://www.informatics.jax.org/hasAnnotationAssigner> \"UniProtKB\"@en .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1345167GO_0005739IDA> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.geneontology.org/GeneAssociationRecord> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1345167GO_0005739IDA> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1345167_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1345167GO_0005739IDA> <http://www.informatics.jax.org/isLinkedToGeneOntologyICE> <http://purl.org/obo/owl/GO#GO_0005739_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1345167GO_0005739IDA> <http://www.informatics.jax.org/hasGoEvidencCode> <http://www.geneontology.org/GO.evidence.shtml#IDA_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1345167GO_0005739IDA> <http://www.informatics.jax.org/hasDatabaseReferenceSupportingAnnotation> <http://www.ncbi.nlm.nih.gov/pubmed/PubMed_14651853_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1345167GO_0005739IDA> <http://www.informatics.jax.org/hasAnnotationAssigner> \"MGI\"@en .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1098687GO_0005524IEA> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.geneontology.org/GeneAssociationRecord> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1098687GO_0005524IEA> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1098687_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1098687GO_0005524IEA> <http://www.informatics.jax.org/isLinkedToGeneOntologyICE> <http://purl.org/obo/owl/GO#GO_0005524_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1098687GO_0005524IEA> <http://www.informatics.jax.org/hasGoEvidencCode> <http://www.geneontology.org/GO.evidence.shtml#IEA_ICE> .",
						"<http://purl.org/obo/owl/GO#GENE_ASSOCIATION_RECORD_MGI_1098687GO_0005524IEA> <http://www.informatics.jax.org/hasAnnotationAssigner> \"UniProtKB\"@en .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "gene-association--.mgi.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("gene-association-.nt", 16);
		counts.put("kabob-meta-gene-association-.nt", 6);
		return counts;
	}

}
