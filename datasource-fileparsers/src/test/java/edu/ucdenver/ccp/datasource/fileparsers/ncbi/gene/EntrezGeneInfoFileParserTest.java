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
package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

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
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class EntrezGeneInfoFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_ENTREZGENEINFO_FILE_NAME = "EntrezGene_gene_info";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_ENTREZGENEINFO_FILE_NAME;
	}

	@Override
	protected EntrezGeneInfoFileParser initSampleRecordReader() throws IOException {
		return new EntrezGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testTaxonAwareParser_10090() throws IOException {
		EntrezGeneInfoFileParser rr = new EntrezGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII,
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
		EntrezGeneInfoFileParser rr = new EntrezGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII,
				CollectionsUtil.createSet(new NcbiTaxonomyID(9606)));
		assertTrue(rr.hasNext());
		rr.next();
		assertFalse(rr.hasNext());
	}

	@Test
	public void testTaxonAwareParser_null() throws IOException {
		EntrezGeneInfoFileParser rr = new EntrezGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII, null);
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
		EntrezGeneInfoFileParser rr = new EntrezGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII,
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
			EntrezGeneInfoFileParser parser = new EntrezGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/*
				 * 10090 12780 Abcc2 - AI173996|Abc30|Cmoat|Mrp2|cMRP
				 * MGI:1352447|Ensembl:ENSMUSG00000025194 19 19 C3|19 43.0 cM ATP-binding cassette,
				 * sub-family C (CFTR/MRP), member 2 protein-coding Abcc2 ATP-binding cassette,
				 * sub-family C (CFTR/MRP), member 2 O ATP-binding cassette, sub-family C, member
				 * 2|canalicular multispecific organic anion transporter|multidrug resistance
				 * protein 2 20080827
				 */
				EntrezGeneInfoFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new EntrezGeneID(12780), record.getGeneID());
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
				EntrezGeneInfoFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new EntrezGeneID(11308), record.getGeneID());
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
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * 10090 11434 Acr - AI323726|MGC124043 MGI:87884|Ensembl:ENSMUSG00000022622 15 15
				 * E-F|15 48.6 cM acrosin prepropeptide protein-coding Acr acrosin prepropeptide O
				 * acrosin|preproacrosin 20080831
				 */
				EntrezGeneInfoFileData record = parser.next();
				assertEquals(new NcbiTaxonomyID(10090), record.getTaxonID());
				assertEquals(new EntrezGeneID(11434), record.getGeneID());
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
		Map<String, Set<EntrezGeneID>> geneSymbol2EntrezGeneIDMap = EntrezGeneInfoFileParser
				.getGeneSymbol2EntrezGeneIDMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090),
						toLowerCase);

		Set<EntrezGeneID> abcc2Set = new HashSet<EntrezGeneID>();
		abcc2Set.add(new EntrezGeneID(12780));
		Set<EntrezGeneID> abi1Set = new HashSet<EntrezGeneID>();
		abi1Set.add(new EntrezGeneID(11308));
		Set<EntrezGeneID> acrSet = new HashSet<EntrezGeneID>();
		acrSet.add(new EntrezGeneID(11434));
		Set<EntrezGeneID> rdxSet = new HashSet<EntrezGeneID>();
		rdxSet.add(new EntrezGeneID(19684));
		Set<EntrezGeneID> ezrSet = new HashSet<EntrezGeneID>();
		ezrSet.add(new EntrezGeneID(22350));
		Set<EntrezGeneID> enahSet = new HashSet<EntrezGeneID>();
		enahSet.add(new EntrezGeneID(13800));
		Set<EntrezGeneID> zp2Set = new HashSet<EntrezGeneID>();
		zp2Set.add(new EntrezGeneID(22787));
		Set<EntrezGeneID> zp3Set = new HashSet<EntrezGeneID>();
		zp3Set.add(new EntrezGeneID(22788));

		Map<String, Set<EntrezGeneID>> expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<EntrezGeneID>>();
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
		geneSymbol2EntrezGeneIDMap = EntrezGeneInfoFileParser.getGeneSymbol2EntrezGeneIDMap(sampleInputFile,
				CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090), toLowerCase);
		expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<EntrezGeneID>>();
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
		geneSymbol2EntrezGeneIDMap = EntrezGeneInfoFileParser.getGeneSymbol2EntrezGeneIDMap(sampleInputFile,
				CharacterEncoding.US_ASCII, new NcbiTaxonomyID(12345), toLowerCase);
		assertEquals(0, geneSymbol2EntrezGeneIDMap.size());

	}

	@Test
	public void testGetGeneSymbol2EntrezGeneIDMap_withSynonyms() throws Exception {
		boolean toLowerCase = false;
		Map<String, Set<EntrezGeneID>> geneSymbol2EntrezGeneIDMap = EntrezGeneInfoFileParser
				.getGeneSymbol2EntrezGeneIDMap_withSynonyms(sampleInputFile, CharacterEncoding.US_ASCII,
						new NcbiTaxonomyID(10090), toLowerCase);

		Set<EntrezGeneID> abcc2Set = new HashSet<EntrezGeneID>();
		abcc2Set.add(new EntrezGeneID(12780));
		Set<EntrezGeneID> aI173996Set = new HashSet<EntrezGeneID>();
		aI173996Set.add(new EntrezGeneID(12780));
		Set<EntrezGeneID> abc30Set = new HashSet<EntrezGeneID>();
		abc30Set.add(new EntrezGeneID(12780));
		Set<EntrezGeneID> cmoatSet = new HashSet<EntrezGeneID>();
		cmoatSet.add(new EntrezGeneID(12780));
		Set<EntrezGeneID> mrp2Set = new HashSet<EntrezGeneID>();
		mrp2Set.add(new EntrezGeneID(12780));
		Set<EntrezGeneID> cMRPSet = new HashSet<EntrezGeneID>();
		cMRPSet.add(new EntrezGeneID(12780));

		Set<EntrezGeneID> abi1Set = new HashSet<EntrezGeneID>();
		abi1Set.add(new EntrezGeneID(11308));
		Set<EntrezGeneID> e3B1Set = new HashSet<EntrezGeneID>();
		e3B1Set.add(new EntrezGeneID(11308));
		Set<EntrezGeneID> mGC6064Set = new HashSet<EntrezGeneID>();
		mGC6064Set.add(new EntrezGeneID(11308));
		Set<EntrezGeneID> nAP1Set = new HashSet<EntrezGeneID>();
		nAP1Set.add(new EntrezGeneID(11308));
		Set<EntrezGeneID> ssh3bp1Set = new HashSet<EntrezGeneID>();
		ssh3bp1Set.add(new EntrezGeneID(11308));

		Set<EntrezGeneID> acrSet = new HashSet<EntrezGeneID>();
		acrSet.add(new EntrezGeneID(11434));
		Set<EntrezGeneID> aI323726Set = new HashSet<EntrezGeneID>();
		aI323726Set.add(new EntrezGeneID(11434));
		Set<EntrezGeneID> mGC124043Set = new HashSet<EntrezGeneID>();
		mGC124043Set.add(new EntrezGeneID(11434));

		Set<EntrezGeneID> rdxSet = new HashSet<EntrezGeneID>();
		rdxSet.add(new EntrezGeneID(19684));

		Set<EntrezGeneID> aA516625Set = new HashSet<EntrezGeneID>();
		aA516625Set.add(new EntrezGeneID(19684));

		Set<EntrezGeneID> ezrSet = new HashSet<EntrezGeneID>();
		ezrSet.add(new EntrezGeneID(22350));
		Set<EntrezGeneID> aW146364Set = new HashSet<EntrezGeneID>();
		aW146364Set.add(new EntrezGeneID(22350));
		Set<EntrezGeneID> mGC107499Set = new HashSet<EntrezGeneID>();
		mGC107499Set.add(new EntrezGeneID(22350));
		Set<EntrezGeneID> r75297Set = new HashSet<EntrezGeneID>();
		r75297Set.add(new EntrezGeneID(22350));
		Set<EntrezGeneID> vil2Set = new HashSet<EntrezGeneID>();
		vil2Set.add(new EntrezGeneID(22350));
		Set<EntrezGeneID> p81Set = new HashSet<EntrezGeneID>();
		p81Set.add(new EntrezGeneID(22350));

		Set<EntrezGeneID> enahSet = new HashSet<EntrezGeneID>();
		enahSet.add(new EntrezGeneID(13800));
		Set<EntrezGeneID> aI464316Set = new HashSet<EntrezGeneID>();
		aI464316Set.add(new EntrezGeneID(13800));
		Set<EntrezGeneID> aW045240Set = new HashSet<EntrezGeneID>();
		aW045240Set.add(new EntrezGeneID(13800));
		Set<EntrezGeneID> menaSet = new HashSet<EntrezGeneID>();
		menaSet.add(new EntrezGeneID(13800));
		Set<EntrezGeneID> ndpp1Set = new HashSet<EntrezGeneID>();
		ndpp1Set.add(new EntrezGeneID(13800));
		Set<EntrezGeneID> wBP8Set = new HashSet<EntrezGeneID>();
		wBP8Set.add(new EntrezGeneID(13800));

		Set<EntrezGeneID> zp2Set = new HashSet<EntrezGeneID>();
		zp2Set.add(new EntrezGeneID(22787));
		Set<EntrezGeneID> ZpDash2Set = new HashSet<EntrezGeneID>();
		ZpDash2Set.add(new EntrezGeneID(22787));

		Set<EntrezGeneID> zp3Set = new HashSet<EntrezGeneID>();
		zp3Set.add(new EntrezGeneID(22788));
		Set<EntrezGeneID> ZpDash3Set = new HashSet<EntrezGeneID>();
		ZpDash3Set.add(new EntrezGeneID(22788));

		Map<String, Set<EntrezGeneID>> expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<EntrezGeneID>>();
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
		geneSymbol2EntrezGeneIDMap = EntrezGeneInfoFileParser.getGeneSymbol2EntrezGeneIDMap_withSynonyms(
				sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090), toLowerCase);
		expectedGeneSymbol2EntrezGeneIDMap = new HashMap<String, Set<EntrezGeneID>>();
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
		EntrezGeneInfoFileParser parser = new EntrezGeneInfoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
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
		Map<EntrezGeneID, String> expectedGeneID2GeneNameMap = new HashMap<EntrezGeneID, String>();
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(12780), new String("Abcc2"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(11308), new String("Abi1"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(11434), new String("Acr"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(19684), new String("Rdx"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(22350), new String("Ezr"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(13800), new String("Enah"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(22787), new String("Zp2"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(22788), new String("Zp3"));

		assertEquals("Maps should match exactly", expectedGeneID2GeneNameMap,
				EntrezGeneInfoFileParser.getEntrezGeneID2GeneSymbolMap(sampleInputFile, CharacterEncoding.US_ASCII,
						new NcbiTaxonomyID(10090)));
	}

	@Test
	public void testGetEntrezGeneIDAsString2GeneNameMap() throws Exception {
		Map<EntrezGeneID, String> expectedGeneID2GeneNameMap = new HashMap<EntrezGeneID, String>();
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(12780), new String(
				"ATP-binding cassette, sub-family C (CFTR/MRP), member 2"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(11308), new String("abl-interactor 1"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(11434), new String("acrosin prepropeptide"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(19684), new String("radixin"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(22350), new String("ezrin"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(13800), new String("enabled homolog (Drosophila)"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(22787), new String("zona pellucida glycoprotein 2"));
		expectedGeneID2GeneNameMap.put(new EntrezGeneID(22788), new String("zona pellucida glycoprotein 3"));

		Map<EntrezGeneID, String> entrezGeneID2GeneNameMap = EntrezGeneInfoFileParser
				.getEntrezGeneID2GeneNameMap(sampleInputFile, CharacterEncoding.US_ASCII, new NcbiTaxonomyID(10090));

		assertEquals("Maps should match exactly", expectedGeneID2GeneNameMap, entrezGeneID2GeneNameMap);
	}

	@Test
	public void testGetEntrezGeneID2TaxonomyIDMap() throws Exception {
		Set<EntrezGeneID> geneIDs2Include = CollectionsUtil.createSet(new EntrezGeneID(11308), new EntrezGeneID(22350),
				new EntrezGeneID(22787));
		Map<EntrezGeneID, NcbiTaxonomyID> expectedEntrezGeneID2TaxonomyIDMap = new HashMap<EntrezGeneID, NcbiTaxonomyID>();
		expectedEntrezGeneID2TaxonomyIDMap.put(new EntrezGeneID(11308), new NcbiTaxonomyID(10090));
		expectedEntrezGeneID2TaxonomyIDMap.put(new EntrezGeneID(22350), new NcbiTaxonomyID(10090));
		expectedEntrezGeneID2TaxonomyIDMap.put(new EntrezGeneID(22787), new NcbiTaxonomyID(10090));
		Map<EntrezGeneID, NcbiTaxonomyID> entrezGeneID2TaxonomyIDMap = EntrezGeneInfoFileParser
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
