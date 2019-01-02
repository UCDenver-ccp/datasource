package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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
import static org.junit.Assert.assertTrue;
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
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class NcbiGeneInfoFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_ENTREZGENEINFO_FILE_NAME = "EntrezGene_gene_info";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_ENTREZGENEINFO_FILE_NAME;
	}

	@Override
	protected NcbiGeneInfoFileParser initSampleRecordReader() throws IOException {
		return new NcbiGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testTaxonAwareParser_10090() throws IOException {
		NcbiGeneInfoFileParser rr = new NcbiGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII,
				CollectionsUtil.createSet(new NcbiTaxonomyID(10090)));
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertFalse(rr.hasNext());

	}

	@Test
	public void testTaxonAwareParser_9606() throws IOException {
		NcbiGeneInfoFileParser rr = new NcbiGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII,
				CollectionsUtil.createSet(new NcbiTaxonomyID(9606)));
		assertTrue(rr.hasNext());
		rr.next();
		assertFalse(rr.hasNext());
	}

	@Test
	public void testTaxonAwareParser_null() throws IOException {
		NcbiGeneInfoFileParser rr = new NcbiGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII, null);
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertFalse(rr.hasNext());

	}

	@Test
	public void testTaxonAwareParser_empty() throws IOException {
		NcbiGeneInfoFileParser rr = new NcbiGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII,
				new HashSet<NcbiTaxonomyID>());
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertTrue(rr.hasNext());
		rr.next();
		assertFalse(rr.hasNext());

	}

	@Test
	public void testParser() {
		try {
			NcbiGeneInfoFileParser parser = new NcbiGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/*
				 * 10090 12780 Abcc2 - AI173996|Abc30|Cmoat|Mrp2|cMRP
				 * MGI:1352447|Ensembl:ENSMUSG00000025194 19 19 C3|19 43.0 cM ATP-binding cassette,
				 * sub-family C (CFTR/MRP), member 2 protein-coding Abcc2 ATP-binding cassette,
				 * sub-family C (CFTR/MRP), member 2 O ATP-binding cassette, sub-family C, member
				 * 2|canalicular multispecific organic anion transporter|multidrug resistance
				 * protein 2 20080827 regulatory:enhancer|regulatory:silencer
				 */
				NcbiGeneInfoFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new NcbiGeneId(12780), record.getGeneID());
				assertEquals(new String("Abcc2"), record.getSymbol());
				assertEquals(null, record.getLocusTag());
				Set<String> expectedSynonyms = new HashSet<String>();
				expectedSynonyms.add(new String("AI173996"));
				expectedSynonyms.add(new String("Abc30"));
				expectedSynonyms.add(new String("Cmoat"));
				expectedSynonyms.add(new String("Mrp2"));
				expectedSynonyms.add(new String("cMRP"));
				assertEquals(expectedSynonyms, record.getSynonyms());
				Set<DataSourceIdentifier<?>> expectedDBXrefs = new HashSet<DataSourceIdentifier<?>>();
				expectedDBXrefs.add(new MgiGeneID("MGI:1352447"));
				expectedDBXrefs.add(new EnsemblGeneID("ENSMUSG00000025194"));
				assertEquals(expectedDBXrefs, record.getDbXrefs());
				assertEquals(new String("19"), record.getChromosome());
				assertEquals("19 C3|19 43.0 cM", record.getMapLocation());
				assertEquals("ATP-binding cassette, sub-family C (CFTR/MRP), member 2", record.getDescription());
				assertEquals("protein-coding", record.getTypeOfGene());
				assertEquals(new String("Abcc2"), record.getSymbolFromNomenclatureAuthority());
				assertEquals(new String("ATP-binding cassette, sub-family C (CFTR/MRP), member 2"),
						record.getFullNameFromNomenclatureAuthority());
				assertEquals("O", record.getNomenclatureStatus());
				Set<String> expectedOtherDesignations = new HashSet<String>();
				expectedOtherDesignations.add(new String("ATP-binding cassette, sub-family C, member 2"));
				expectedOtherDesignations.add(new String(
						"canalicular multispecific organic anion transporter"));
				expectedOtherDesignations.add(new String("multidrug resistance protein 2"));
				assertEquals(expectedOtherDesignations, record.getOtherDesignations());
				assertEquals("20080827", record.getModificationDate());
				assertEquals(CollectionsUtil.createSet("regulatory:enhancer","regulatory:silencer"), record.getFeatureTypes());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 10090 11308 Abi1 - E3B1|MGC6064|NAP1|Ssh3bp1
				 * MGI:104913|Ensembl:ENSMUSG00000058835 2 2 A3|2 15.0 cM abl-interactor 1
				 * protein-coding Abi1 abl-interactor 1 O abelson interactor 1|ablphilin-1|eps8
				 * binding protein|spectrin SH3 domain binding protein 1 20080817
				 */
				NcbiGeneInfoFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new NcbiGeneId(11308), record.getGeneID());
				assertEquals(new String("Abi1"), record.getSymbol());
				assertEquals(null, record.getLocusTag());
				Set<String> expectedSynonyms = new HashSet<String>();
				expectedSynonyms.add(new String("E3B1"));
				expectedSynonyms.add(new String("MGC6064"));
				expectedSynonyms.add(new String("NAP1"));
				expectedSynonyms.add(new String("Ssh3bp1"));
				assertEquals(expectedSynonyms, record.getSynonyms());
				Set<DataSourceIdentifier<?>> expectedDBXrefs = new HashSet<DataSourceIdentifier<?>>();
				expectedDBXrefs.add(new MgiGeneID("MGI:104913"));
				expectedDBXrefs.add(new EnsemblGeneID("ENSMUSG00000058835"));
				assertEquals(expectedDBXrefs, record.getDbXrefs());
				assertEquals("2", record.getChromosome());
				assertEquals("2 A3|2 15.0 cM", record.getMapLocation());
				assertEquals("abl-interactor 1", record.getDescription());
				assertEquals("protein-coding", record.getTypeOfGene());
				assertEquals(new String("Abi1"), record.getSymbolFromNomenclatureAuthority());
				assertEquals(new String("abl-interactor 1"), record.getFullNameFromNomenclatureAuthority());
				assertEquals("O", record.getNomenclatureStatus());
				Set<String> expectedOtherDesignations = new HashSet<String>();
				expectedOtherDesignations.add(new String("abelson interactor 1"));
				expectedOtherDesignations.add(new String("ablphilin-1"));
				expectedOtherDesignations.add(new String("eps8 binding protein"));
				expectedOtherDesignations.add(new String("spectrin SH3 domain binding protein 1"));
				assertEquals(expectedOtherDesignations, record.getOtherDesignations());
				assertEquals("20080817", record.getModificationDate());
				assertEmpty(record.getFeatureTypes());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 10090 11434 Acr - AI323726|MGC124043 MGI:87884|Ensembl:ENSMUSG00000022622 15 15
				 * E-F|15 48.6 cM acrosin prepropeptide protein-coding Acr acrosin prepropeptide O
				 * acrosin|preproacrosin 20080831
				 */
				NcbiGeneInfoFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new NcbiGeneId(11434), record.getGeneID());
				assertEquals(new String("Acr"), record.getSymbol());
				assertEquals(null, record.getLocusTag());
				Set<String> expectedSynonyms = new HashSet<String>();
				expectedSynonyms.add(new String("AI323726"));
				expectedSynonyms.add(new String("MGC124043"));
				assertEquals(expectedSynonyms, record.getSynonyms());
				Set<DataSourceIdentifier<?>> expectedDBXrefs = new HashSet<DataSourceIdentifier<?>>();
				expectedDBXrefs.add(new MgiGeneID("MGI:87884"));
				expectedDBXrefs.add(new EnsemblGeneID("ENSMUSG00000022622"));
				assertEquals(expectedDBXrefs, record.getDbXrefs());
				assertEquals("15", record.getChromosome());
				assertEquals("15 E-F|15 48.6 cM", record.getMapLocation());
				assertEquals("acrosin prepropeptide", record.getDescription());
				assertEquals("protein-coding", record.getTypeOfGene());
				assertEquals(new String("Acr"), record.getSymbolFromNomenclatureAuthority());
				assertEquals(new String("acrosin prepropeptide"),
						record.getFullNameFromNomenclatureAuthority());
				assertEquals("O", record.getNomenclatureStatus());
				Set<String> expectedOtherDesignations = new HashSet<String>();
				expectedOtherDesignations.add(new String("acrosin"));
				expectedOtherDesignations.add(new String("preproacrosin"));
				assertEquals(expectedOtherDesignations, record.getOtherDesignations());
				assertEquals("20080831", record.getModificationDate());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertTrue(parser.hasNext());
			parser.next();
			assertTrue(parser.hasNext());
			parser.next();
			assertTrue(parser.hasNext());
			parser.next();
			assertTrue(parser.hasNext());
			parser.next();
			assertTrue(parser.hasNext());
			parser.next();
			assertTrue(parser.hasNext());
			parser.next();
			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	@Test
	public void testGetGeneSymbol2EntrezGeneIDMap() throws Exception {
		boolean toLowerCase = false;
		Map<String, Set<NcbiGeneId>> geneSymbol2EntrezGeneIDMap = NcbiGeneInfoFileParser
				.getGeneSymbol2EntrezGeneIDMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090),
						toLowerCase);

		Set<NcbiGeneId> abcc2Set = new HashSet<NcbiGeneId>();
		abcc2Set.add(new NcbiGeneId(12780));
		Set<NcbiGeneId> abi1Set = new HashSet<NcbiGeneId>();
		abi1Set.add(new NcbiGeneId(11308));
		Set<NcbiGeneId> acrSet = new HashSet<NcbiGeneId>();
		acrSet.add(new NcbiGeneId(11434));
		Set<NcbiGeneId> rdxSet = new HashSet<NcbiGeneId>();
		rdxSet.add(new NcbiGeneId(19684));
		Set<NcbiGeneId> ezrSet = new HashSet<NcbiGeneId>();
		ezrSet.add(new NcbiGeneId(22350));
		Set<NcbiGeneId> enahSet = new HashSet<NcbiGeneId>();
		enahSet.add(new NcbiGeneId(13800));
		Set<NcbiGeneId> zp2Set = new HashSet<NcbiGeneId>();
		zp2Set.add(new NcbiGeneId(22787));
		Set<NcbiGeneId> zp3Set = new HashSet<NcbiGeneId>();
		zp3Set.add(new NcbiGeneId(22788));

		Map<String, Set<NcbiGeneId>> expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<NcbiGeneId>>();
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Abcc2"), abcc2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Abi1"), abi1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Acr"), acrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Rdx"), rdxSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Ezr"), ezrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Enah"), enahSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Zp2"), zp2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Zp3"), zp3Set);

		/* Maps should be identical */
		assertEquals(expectedGeneSymbol2EntrezGeneIDMap, geneSymbol2EntrezGeneIDMap);

		/* Now test when toLowerCase = true */
		toLowerCase = true;
		geneSymbol2EntrezGeneIDMap = NcbiGeneInfoFileParser.getGeneSymbol2EntrezGeneIDMap(sampleInputFile,
				CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090), toLowerCase);
		expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<NcbiGeneId>>();
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("abcc2"), abcc2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("abi1"), abi1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("acr"), acrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("rdx"), rdxSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("ezr"), ezrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("enah"), enahSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("zp2"), zp2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("zp3"), zp3Set);

		/* Maps should be identical with lowercase keys */
		assertEquals(expectedGeneSymbol2EntrezGeneIDMap, geneSymbol2EntrezGeneIDMap);

		/* Test taxon id filter */
		geneSymbol2EntrezGeneIDMap = NcbiGeneInfoFileParser.getGeneSymbol2EntrezGeneIDMap(sampleInputFile,
				CharacterEncoding.US_ASCII, new NcbiTaxonomyID(12345), toLowerCase);
		assertEquals(0, geneSymbol2EntrezGeneIDMap.size());

	}

	@Test
	public void testGetGeneSymbol2EntrezGeneIDMap_withSynonyms() throws Exception {
		boolean toLowerCase = false;
		Map<String, Set<NcbiGeneId>> geneSymbol2EntrezGeneIDMap = NcbiGeneInfoFileParser
				.getGeneSymbol2EntrezGeneIDMap_withSynonyms(sampleInputFile, CharacterEncoding.US_ASCII,
						new NcbiTaxonomyID(10090), toLowerCase);

		Set<NcbiGeneId> abcc2Set = new HashSet<NcbiGeneId>();
		abcc2Set.add(new NcbiGeneId(12780));
		Set<NcbiGeneId> aI173996Set = new HashSet<NcbiGeneId>();
		aI173996Set.add(new NcbiGeneId(12780));
		Set<NcbiGeneId> abc30Set = new HashSet<NcbiGeneId>();
		abc30Set.add(new NcbiGeneId(12780));
		Set<NcbiGeneId> cmoatSet = new HashSet<NcbiGeneId>();
		cmoatSet.add(new NcbiGeneId(12780));
		Set<NcbiGeneId> mrp2Set = new HashSet<NcbiGeneId>();
		mrp2Set.add(new NcbiGeneId(12780));
		Set<NcbiGeneId> cMRPSet = new HashSet<NcbiGeneId>();
		cMRPSet.add(new NcbiGeneId(12780));

		Set<NcbiGeneId> abi1Set = new HashSet<NcbiGeneId>();
		abi1Set.add(new NcbiGeneId(11308));
		Set<NcbiGeneId> e3B1Set = new HashSet<NcbiGeneId>();
		e3B1Set.add(new NcbiGeneId(11308));
		Set<NcbiGeneId> mGC6064Set = new HashSet<NcbiGeneId>();
		mGC6064Set.add(new NcbiGeneId(11308));
		Set<NcbiGeneId> nAP1Set = new HashSet<NcbiGeneId>();
		nAP1Set.add(new NcbiGeneId(11308));
		Set<NcbiGeneId> ssh3bp1Set = new HashSet<NcbiGeneId>();
		ssh3bp1Set.add(new NcbiGeneId(11308));

		Set<NcbiGeneId> acrSet = new HashSet<NcbiGeneId>();
		acrSet.add(new NcbiGeneId(11434));
		Set<NcbiGeneId> aI323726Set = new HashSet<NcbiGeneId>();
		aI323726Set.add(new NcbiGeneId(11434));
		Set<NcbiGeneId> mGC124043Set = new HashSet<NcbiGeneId>();
		mGC124043Set.add(new NcbiGeneId(11434));

		Set<NcbiGeneId> rdxSet = new HashSet<NcbiGeneId>();
		rdxSet.add(new NcbiGeneId(19684));

		Set<NcbiGeneId> aA516625Set = new HashSet<NcbiGeneId>();
		aA516625Set.add(new NcbiGeneId(19684));

		Set<NcbiGeneId> ezrSet = new HashSet<NcbiGeneId>();
		ezrSet.add(new NcbiGeneId(22350));
		Set<NcbiGeneId> aW146364Set = new HashSet<NcbiGeneId>();
		aW146364Set.add(new NcbiGeneId(22350));
		Set<NcbiGeneId> mGC107499Set = new HashSet<NcbiGeneId>();
		mGC107499Set.add(new NcbiGeneId(22350));
		Set<NcbiGeneId> r75297Set = new HashSet<NcbiGeneId>();
		r75297Set.add(new NcbiGeneId(22350));
		Set<NcbiGeneId> vil2Set = new HashSet<NcbiGeneId>();
		vil2Set.add(new NcbiGeneId(22350));
		Set<NcbiGeneId> p81Set = new HashSet<NcbiGeneId>();
		p81Set.add(new NcbiGeneId(22350));

		Set<NcbiGeneId> enahSet = new HashSet<NcbiGeneId>();
		enahSet.add(new NcbiGeneId(13800));
		Set<NcbiGeneId> aI464316Set = new HashSet<NcbiGeneId>();
		aI464316Set.add(new NcbiGeneId(13800));
		Set<NcbiGeneId> aW045240Set = new HashSet<NcbiGeneId>();
		aW045240Set.add(new NcbiGeneId(13800));
		Set<NcbiGeneId> menaSet = new HashSet<NcbiGeneId>();
		menaSet.add(new NcbiGeneId(13800));
		Set<NcbiGeneId> ndpp1Set = new HashSet<NcbiGeneId>();
		ndpp1Set.add(new NcbiGeneId(13800));
		Set<NcbiGeneId> wBP8Set = new HashSet<NcbiGeneId>();
		wBP8Set.add(new NcbiGeneId(13800));

		Set<NcbiGeneId> zp2Set = new HashSet<NcbiGeneId>();
		zp2Set.add(new NcbiGeneId(22787));
		Set<NcbiGeneId> ZpDash2Set = new HashSet<NcbiGeneId>();
		ZpDash2Set.add(new NcbiGeneId(22787));

		Set<NcbiGeneId> zp3Set = new HashSet<NcbiGeneId>();
		zp3Set.add(new NcbiGeneId(22788));
		Set<NcbiGeneId> ZpDash3Set = new HashSet<NcbiGeneId>();
		ZpDash3Set.add(new NcbiGeneId(22788));

		Map<String, Set<NcbiGeneId>> expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<NcbiGeneId>>();
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Abcc2"), abcc2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("AI173996"), aI173996Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Abc30"), abc30Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Cmoat"), cmoatSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Mrp2"), mrp2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("cMRP"), cMRPSet);

		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Abi1"), abi1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("E3B1"), e3B1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("MGC6064"), mGC6064Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("NAP1"), nAP1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Ssh3bp1"), ssh3bp1Set);

		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Acr"), acrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("AI323726"), aI323726Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("MGC124043"), mGC124043Set);

		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Rdx"), rdxSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("AA516625"), aA516625Set);

		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Ezr"), ezrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("AW146364"), aW146364Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("MGC107499"), mGC107499Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("R75297"), r75297Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Vil2"), vil2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("p81"), p81Set);

		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Enah"), enahSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("AI464316"), aI464316Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("AW045240"), aW045240Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Mena"), menaSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Ndpp1"), ndpp1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("WBP8"), wBP8Set);

		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Zp2"), zp2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Zp-2"), ZpDash2Set);

		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Zp3"), zp3Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("Zp-3"), ZpDash3Set);

		/* Maps should be identical */
		assertEquals(expectedGeneSymbol2EntrezGeneIDMap, geneSymbol2EntrezGeneIDMap);

		/* Now test with toLowerCase = true */
		toLowerCase = true;
		geneSymbol2EntrezGeneIDMap = NcbiGeneInfoFileParser.getGeneSymbol2EntrezGeneIDMap_withSynonyms(
				sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090), toLowerCase);
		expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<NcbiGeneId>>();
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("abcc2"), abcc2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("ai173996"), aI173996Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("abc30"), abc30Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("cmoat"), cmoatSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("mrp2"), mrp2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("cmrp"), cMRPSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("abi1"), abi1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("e3b1"), e3B1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("mgc6064"), mGC6064Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("nap1"), nAP1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("ssh3bp1"), ssh3bp1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("acr"), acrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("ai323726"), aI323726Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("mgc124043"), mGC124043Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("rdx"), rdxSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("aa516625"), aA516625Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("ezr"), ezrSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("aw146364"), aW146364Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("mgc107499"), mGC107499Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("r75297"), r75297Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("vil2"), vil2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("p81"), p81Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("enah"), enahSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("ai464316"), aI464316Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("aw045240"), aW045240Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("mena"), menaSet);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("ndpp1"), ndpp1Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("wbp8"), wBP8Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("zp2"), zp2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("zp-2"), ZpDash2Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("zp3"), zp3Set);
		expectedGeneSymbol2EntrezGeneIDMap.put(new String("zp-3"), ZpDash3Set);

		/* Maps should be identical with lowercase keys */
		assertEquals(expectedGeneSymbol2EntrezGeneIDMap, geneSymbol2EntrezGeneIDMap);

	}

	@Override
	@Test
	public void testIteratorPattern() throws IOException {
		NcbiGeneInfoFileParser parser = new NcbiGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
		assertTrue(parser.hasNext());
		assertTrue(parser.hasNext());
		assertTrue(parser.hasNext());
		assertTrue(parser.hasNext());
		assertTrue(parser.hasNext());
		assertTrue(parser.hasNext());
		assertTrue(parser.hasNext());
		assertTrue(parser.hasNext());
		assertTrue("Repeated calls to parser.hasNext() should not advance the iterator.", parser.hasNext());
	}

	@Test
	public void testGetEntrezGeneIDAsString2GeneSymbolMap() throws Exception {
		Map<NcbiGeneId, String> expectedGeneID2GeneNameMap = new HashMap<NcbiGeneId, String>();
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(12780), new String("Abcc2"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(11308), new String("Abi1"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(11434), new String("Acr"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(19684), new String("Rdx"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(22350), new String("Ezr"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(13800), new String("Enah"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(22787), new String("Zp2"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(22788), new String("Zp3"));

		assertEquals("Maps should match exactly", expectedGeneID2GeneNameMap,
				NcbiGeneInfoFileParser.getEntrezGeneID2GeneSymbolMap(sampleInputFile, CharacterEncoding.US_ASCII,
						new NcbiTaxonomyID(10090)));
	}

	@Test
	public void testGetEntrezGeneIDAsString2GeneNameMap() throws Exception {
		Map<NcbiGeneId, String> expectedGeneID2GeneNameMap = new HashMap<NcbiGeneId, String>();
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(12780), new String(
				"ATP-binding cassette, sub-family C (CFTR/MRP), member 2"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(11308), new String("abl-interactor 1"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(11434), new String("acrosin prepropeptide"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(19684), new String("radixin"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(22350), new String("ezrin"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(13800), new String("enabled homolog (Drosophila)"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(22787), new String("zona pellucida glycoprotein 2"));
		expectedGeneID2GeneNameMap.put(new NcbiGeneId(22788), new String("zona pellucida glycoprotein 3"));

		Map<NcbiGeneId, String> entrezGeneID2GeneNameMap = NcbiGeneInfoFileParser
				.getEntrezGeneID2GeneNameMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090));

		assertEquals("Maps should match exactly", expectedGeneID2GeneNameMap, entrezGeneID2GeneNameMap);
	}

	@Test
	public void testGetEntrezGeneID2TaxonomyIDMap() throws Exception {
		Set<NcbiGeneId> geneIDs2Include = CollectionsUtil.createSet(new NcbiGeneId(11308), new NcbiGeneId(22350),
				new NcbiGeneId(22787));
		Map<NcbiGeneId, NcbiTaxonomyID> expectedEntrezGeneID2TaxonomyIDMap = new HashMap<NcbiGeneId, NcbiTaxonomyID>();
		expectedEntrezGeneID2TaxonomyIDMap.put(new NcbiGeneId(11308), new NcbiTaxonomyID(10090));
		expectedEntrezGeneID2TaxonomyIDMap.put(new NcbiGeneId(22350), new NcbiTaxonomyID(10090));
		expectedEntrezGeneID2TaxonomyIDMap.put(new NcbiGeneId(22787), new NcbiTaxonomyID(10090));
		Map<NcbiGeneId, NcbiTaxonomyID> entrezGeneID2TaxonomyIDMap = NcbiGeneInfoFileParser
				.getEntrezGeneID2TaxonomyIDMap(sampleInputFile, CharacterEncoding.US_ASCII, geneIDs2Include);
		assertEquals(String.format("Map should have 3 entries."), expectedEntrezGeneID2TaxonomyIDMap,
				entrezGeneID2TaxonomyIDMap);
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/eg/";
		List<String> lines = CollectionsUtil
				.createList(
						NS
								+ "EG_12780_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_12780\"@en .",
						NS
								+ "EG_12780_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_12780> .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Abcc2\"@en .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Cmoat\"@en .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"cMRP\"@en .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Abc30\"@en .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"AI173996\"@en .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Mrp2\"@en .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1352447_ICE> .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000025194_ICE> .",
						NS + "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"19\"@en .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Abcc2\"@en .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"ATP-binding cassette, sub-family C (CFTR/MRP), member 2\"@en .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"ATP-binding cassette, sub-family C, member 2\"@en .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"multidrug resistance protein 2\"@en .",
						NS
								+ "EG_12780_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"canalicular multispecific organic anion transporter\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_11308\"@en .",
						NS
								+ "EG_11308_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_11308> .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Abi1\"@en .",
						NS + "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"E3B1\"@en .",
						NS + "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"MGC6064\"@en .",
						NS + "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"NAP1\"@en .",
						NS + "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Ssh3bp1\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_104913_ICE> .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000058835_ICE> .",
						NS + "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"2\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Abi1\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"abl-interactor 1\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"eps8 binding protein\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"spectrin SH3 domain binding protein 1\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"ablphilin-1\"@en .",
						NS
								+ "EG_11308_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"abelson interactor 1\"@en .",
						NS
								+ "EG_11434_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_11434\"@en .",
						NS
								+ "EG_11434_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_11434> .",
						NS
								+ "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Acr\"@en .",
						NS + "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"MGC124043\"@en .",
						NS + "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"AI323726\"@en .",
						NS
								+ "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_87884_ICE> .",
						NS
								+ "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000022622_ICE> .",
						NS + "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"15\"@en .",
						NS
								+ "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Acr\"@en .",
						NS
								+ "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"acrosin prepropeptide\"@en .",
						NS
								+ "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"preproacrosin\"@en .",
						NS + "EG_11434_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"acrosin\"@en .",
						NS
								+ "EG_19684_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_19684\"@en .",
						NS
								+ "EG_19684_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_19684> .",
						NS
								+ "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Rdx\"@en .",
						NS + "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"AA516625\"@en .",
						NS
								+ "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_97887_ICE> .",
						NS
								+ "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000032050_ICE> .",
						NS + "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"9\"@en .",
						NS
								+ "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Rdx\"@en .",
						NS
								+ "EG_19684_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"radixin\"@en .",
						NS
								+ "EG_22350_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_22350\"@en .",
						NS
								+ "EG_22350_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_22350> .",
						NS
								+ "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Ezr\"@en .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"MGC107499\"@en .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Vil2\"@en .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"p81\"@en .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"AW146364\"@en .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"R75297\"@en .",
						NS
								+ "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_98931_ICE> .",
						NS
								+ "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000052397_ICE> .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"17\"@en .",
						NS
								+ "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Ezr\"@en .",
						NS
								+ "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"ezrin\"@en .",
						NS
								+ "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"OTTMUSP00000019758\"@en .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"cytovillin\"@en .",
						NS + "EG_22350_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"villin 2\"@en .",
						NS
								+ "EG_13800_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_13800\"@en .",
						NS
								+ "EG_13800_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_13800> .",
						NS
								+ "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Enah\"@en .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"AW045240\"@en .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Ndpp1\"@en .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"WBP8\"@en .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"AI464316\"@en .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Mena\"@en .",
						NS
								+ "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_108360_ICE> .",
						NS
								+ "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000022995_ICE> .",
						NS + "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"1\"@en .",
						NS
								+ "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Enah\"@en .",
						NS
								+ "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"enabled homolog (Drosophila)\"@en .",
						NS
								+ "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"enabled homolog\"@en .",
						NS
								+ "EG_13800_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"NPC derived proline rich protein 1\"@en .",
						NS
								+ "EG_22787_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_22787\"@en .",
						NS
								+ "EG_22787_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_22787> .",
						NS
								+ "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Zp2\"@en .",
						NS + "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Zp-2\"@en .",
						NS
								+ "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_99214_ICE> .",
						NS
								+ "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000030911_ICE> .",
						NS + "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"7\"@en .",
						NS
								+ "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Zp2\"@en .",
						NS
								+ "EG_22787_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"zona pellucida glycoprotein 2\"@en .",
						NS
								+ "EG_22788_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/eg/EntrezGeneIce1> .",
						NS + "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasEntrezGeneID> \"EG_22788\"@en .",
						NS
								+ "EG_22788_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.ncbi.nlm.nih.gov/gene/EG_22788> .",
						NS
								+ "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToNcbiTaxonomyICE> <http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10090_ICE> .",
						NS + "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbol> \"Zp3\"@en .",
						NS + "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasSynonym> \"Zp-3\"@en .",
						NS
								+ "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_99215_ICE> .",
						NS
								+ "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000004948_ICE> .",
						NS + "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasChromosomeNumber> \"5\"@en .",
						NS
								+ "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneSymbolFromNomenclatureAuthority> \"Zp3\"@en .",
						NS
								+ "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasGeneFullNameFromNomenclatureAuthority> \"zona pellucida glycoprotein 3\"@en .",
						NS
								+ "EG_22788_ICE> <http://www.ncbi.nlm.nih.gov/gene/hasOtherDesignation> \"OTTMUSP00000030819\"@en .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "entrezgene-info.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("entrezgene-info.nt", 119);
		counts.put("kabob-meta-entrezgene-info.nt", 6);
		return counts;
	}
}
