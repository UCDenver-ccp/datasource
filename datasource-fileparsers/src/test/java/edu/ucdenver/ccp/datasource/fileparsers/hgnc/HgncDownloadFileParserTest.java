package edu.ucdenver.ccp.datasource.fileparsers.hgnc;

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
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.GeneFamilyTagDescriptionPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.LocusSpecificDatabaseNameLinkPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.SpecialistDbIdLinkPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileParser.WithdrawnRecordTreatment;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CosmicId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

public class HgncDownloadFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_HGNC_FILE_NAME = "hgnc_download.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_HGNC_FILE_NAME;
	}

	@Override
	protected HgncDownloadFileParser initSampleRecordReader() throws IOException {
		return new HgncDownloadFileParser(sampleInputFile, CharacterEncoding.US_ASCII, WithdrawnRecordTreatment.INCLUDE);
	}

	@Test
	public void testParser() throws Exception {
		HgncDownloadFileParser parser = initSampleRecordReader();

		if (parser.hasNext()) {
			HgncDownloadFileData dataRecord = parser.next();
			assertEquals(String.format("Returned incorrect HGNC ID."), new HgncID("HGNC:37133"), dataRecord.getHgncID());
			assertEquals(String.format("Returned incorrect gene symbol."), new HgncGeneSymbolID("A1BG-AS1"),
					dataRecord.getHgncGeneSymbol());
			assertEquals(new String("A1BG antisense RNA 1"), dataRecord.getHgncGeneName());
			assertEquals(new String("Approved"), dataRecord.getStatus());
			assertEquals(new String("RNA, long non-coding"), dataRecord.getLocusType());
			assertEquals(new String("non-coding RNA"), dataRecord.getLocusGroup());
			assertEquals(CollectionsUtil.createSet(new String("NCRNA00181"), new String("A1BGAS"),
					new String("A1BG-AS")), dataRecord.getPreviousSymbols());
			assertEquals(CollectionsUtil.createSet(new String("non-protein coding RNA 181"),
					new String("A1BG antisense RNA (non-protein coding)"), new String(
							"A1BG antisense RNA 1 (non-protein coding)")), dataRecord.getPreviousNames());
			assertEquals(CollectionsUtil.createSet(new String("FLJ23569")), dataRecord.getSynonyms());
			assertEmpty(dataRecord.getNameSynonyms());
			assertEquals(new String("19q13.4"), dataRecord.getChromosome());
			assertEquals("2009-07-20", dataRecord.getDateApproved());
			assertEquals("2012-10-12", dataRecord.getDateModified());
			assertEquals("2010-11-25", dataRecord.getDateSymbolChanged());
			assertEquals("2012-08-15", dataRecord.getDateNameChanged());
			assertEquals(CollectionsUtil.createSet(NucleotideAccessionResolver.resolveNucleotideAccession("BC040926", null)),
					dataRecord.getAccessionNumbers());
			assertEmpty(dataRecord.getEcNumbers());
			assertEquals(new NcbiGeneId(503538), dataRecord.getEntrezGeneID());
			assertNull(dataRecord.getEnsemblGeneID());
			assertNull(dataRecord.getMgiIDs());
			assertEmpty(dataRecord.getSpecialistDatabaseIdLinkPairings());
			assertEmpty(dataRecord.getPubmedIDs());
			assertEquals(CollectionsUtil.createSet(new RefSeqID("NR_015380")), dataRecord.getRefseqIDs());
			assertEquals(CollectionsUtil.createSet(new GeneFamilyTagDescriptionPair("LNCRNA", null),
					new GeneFamilyTagDescriptionPair("ANTISENSE", "ncRNAs / Long non-coding RNAs, antisense")),
					dataRecord.getGeneFamilyTagDescriptionPairings());
			assertEquals("Standard", dataRecord.getRecordType());
			assertEmpty(dataRecord.getPrimaryIds());
			assertEmpty(dataRecord.getSecondaryIds());
			assertEmpty(dataRecord.getCcdsIDs());
			assertEmpty(dataRecord.getVegaIDs());
			assertEmpty(dataRecord.getLocusSpecificDatabaseNameLinkPairings());
			assertEquals(new NcbiGeneId(503538), dataRecord.getSuppliedEntrezGeneId());
			assertEmpty(dataRecord.getSuppliedOmimIds());
			assertEquals(new RefSeqID("NR_015380"), dataRecord.getSuppliedRefseqId());
			assertEmpty(dataRecord.getSuppliedUniprotIds());
			assertNull(dataRecord.getSuppliedEnsemblId());
			assertEquals(new UcscGenomeBrowserId("uc002qsg.3"), dataRecord.getSuppliedUcscId());
			assertEmpty(dataRecord.getSuppliedMgiIds());
			assertEmpty(dataRecord.getSuppliedRgdIds());
		} else {
			fail("Parser should have returned the first record.");
		}

		if (parser.hasNext()) {
			HgncDownloadFileData dataRecord = parser.next();
			assertEquals(String.format("Returned incorrect HGNC ID."), new HgncID("HGNC:24086"), dataRecord.getHgncID());
			assertEquals(String.format("Returned incorrect gene symbol."), new HgncGeneSymbolID("A1CF"),
					dataRecord.getHgncGeneSymbol());
			assertEquals(new String("APOBEC1 complementation factor"), dataRecord.getHgncGeneName());
			assertEquals(new String("Approved"), dataRecord.getStatus());
			assertEquals(new String("gene with protein product"), dataRecord.getLocusType());
			assertEquals(new String("protein-coding gene"), dataRecord.getLocusGroup());
			assertEmpty(dataRecord.getPreviousSymbols());
			assertEmpty(dataRecord.getPreviousNames());
			assertEquals(CollectionsUtil.createSet(new String("ACF"), new String("ASP"),
					new String("ACF64"), new String("ACF65"), new String("APOBEC1CF")),
					dataRecord.getSynonyms());
			assertEmpty(dataRecord.getNameSynonyms());
			assertEquals(new String("10q21.1"), dataRecord.getChromosome());
			assertEquals("2007-11-23", dataRecord.getDateApproved());
			assertEquals("2011-07-21", dataRecord.getDateModified());
			assertNull(dataRecord.getDateSymbolChanged());
			assertNull(dataRecord.getDateNameChanged());
			assertEquals(CollectionsUtil.createSet(NucleotideAccessionResolver.resolveNucleotideAccession("AF271790", null)),
					dataRecord.getAccessionNumbers());
			assertEmpty(dataRecord.getEcNumbers());
			assertEquals(new NcbiGeneId(29974), dataRecord.getEntrezGeneID());
			assertEquals(new EnsemblGeneID("ENSG00000148584"), dataRecord.getEnsemblGeneID());
			assertEquals(CollectionsUtil.createSet(new MgiGeneID("MGI:1917115")), dataRecord.getMgiIDs());
			assertEquals(CollectionsUtil.createSet(new SpecialistDbIdLinkPair(new CosmicId("A1CF"),
					"http://www.sanger.ac.uk/perl/genetics/CGP/cosmic?action=gene&amp;ln=A1CF")),
					dataRecord.getSpecialistDatabaseIdLinkPairings());
			assertEquals(CollectionsUtil.createSet(new PubMedID(11815617), new PubMedID(11072063)),
					dataRecord.getPubmedIDs());
			assertEquals(CollectionsUtil.createSet(new RefSeqID("NM_014576")), dataRecord.getRefseqIDs());
			assertEmpty(dataRecord.getGeneFamilyTagDescriptionPairings());
			assertEquals("Standard", dataRecord.getRecordType());
			assertEmpty(dataRecord.getPrimaryIds());
			assertEmpty(dataRecord.getSecondaryIds());
			assertEquals(CollectionsUtil.createSet(new CcdsId("CCDS7241.1"), new CcdsId("CCDS7242.1"), new CcdsId(
					"CCDS7243.1")), dataRecord.getCcdsIDs());
			assertEquals(CollectionsUtil.createSet(new VegaID("OTTHUMG00000018240")), dataRecord.getVegaIDs());
			assertEquals(CollectionsUtil.createSet(new LocusSpecificDatabaseNameLinkPair("Androgen Receptor",
					"http://androgendb.mcgill.ca/"), new LocusSpecificDatabaseNameLinkPair(
					"Mental Retardation database", "http://grenada.lumc.nl/LOVD2/MR/home.php?select_db=AR"),
					new LocusSpecificDatabaseNameLinkPair(
							"ALSOD, the Amyotrophic Lateral Sclerosis Online Genetic Database",
							"http://alsod.iop.kcl.ac.uk/")), dataRecord.getLocusSpecificDatabaseNameLinkPairings());
			assertEquals(new NcbiGeneId(29974), dataRecord.getSuppliedEntrezGeneId());
			assertEmpty(dataRecord.getSuppliedOmimIds());
			assertEquals(new RefSeqID("NM_001198818"), dataRecord.getSuppliedRefseqId());
			assertEquals(CollectionsUtil.createSet(new UniProtID("Q9NQ94")), dataRecord.getSuppliedUniprotIds());
			assertEquals(new EnsemblGeneID("ENSG00000148584"), dataRecord.getSuppliedEnsemblId());
			assertEquals(new UcscGenomeBrowserId("uc001jjj.3"), dataRecord.getSuppliedUcscId());
			assertEquals(CollectionsUtil.createSet(new MgiGeneID("MGI:1917115")), dataRecord.getSuppliedMgiIds());
			assertEquals(CollectionsUtil.createSet(new RgdID("619834")), dataRecord.getSuppliedRgdIds());
		} else {
			fail("Parser should have returned the first record.");
		}

		assertFalse(parser.hasNext());
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
