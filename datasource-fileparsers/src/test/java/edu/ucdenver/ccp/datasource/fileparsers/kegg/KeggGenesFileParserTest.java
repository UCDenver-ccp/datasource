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
package edu.ucdenver.ccp.fileparsers.kegg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.hprd.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggGeneID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGenesFileParserTest extends RecordReaderTester {

	private final static String SAMPLE_KEGG_GENES_FILE_NAME = "h.sapiens";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_KEGG_GENES_FILE_NAME;
	}

	@Override
	protected KeggGenesFileParser initSampleRecordReader() throws IOException {
		return new KeggGenesFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		validateAllRecords(initSampleRecordReader());
	}

	private void validateAllRecords(KeggGenesFileParser parser) {

		if (parser.hasNext()) {
			KeggGenesFileData record = parser.next();
			assertEquals(new KeggGeneID("9081"), record.getKeggGeneID());
			Set<DataSourceIdentifier<?>> expectedIds = new HashSet<DataSourceIdentifier<?>>();
			expectedIds.add(new GiNumberID(22507417));
			expectedIds.add(new EntrezGeneID(9081));
			expectedIds.add(new OmimID(400019));
			expectedIds.add(new HgncID("14024"));
			expectedIds.add(new HprdID("02460"));
			expectedIds.add(new EnsemblGeneID("ENSG00000169789"));
			expectedIds.add(new UniProtID("O14603"));
			assertEquals(expectedIds, record.getDbLinks());
			assertEquals(new HashSet<KeggPathwayID>(), record.getPathwayIds());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			KeggGenesFileData record = parser.next();
			assertEquals(new KeggGeneID("8351"), record.getKeggGeneID());
			Set<DataSourceIdentifier<?>> expectedIds = new HashSet<DataSourceIdentifier<?>>();
			expectedIds.add(new GiNumberID(21071021));
			expectedIds.add(new EntrezGeneID(8351));
			expectedIds.add(new OmimID(602811));
			expectedIds.add(new HgncID("4767"));
			expectedIds.add(new HprdID("11905"));
			expectedIds.add(new EnsemblGeneID("ENSG00000197409"));
			expectedIds.add(new UniProtID("P68431"));
			assertEquals(expectedIds, record.getDbLinks());
			assertEquals(CollectionsUtil.createSet(new KeggPathwayID("hsa05322")), record.getPathwayIds());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			KeggGenesFileData record = parser.next();
			assertEquals(new KeggGeneID("25796"), record.getKeggGeneID());
			Set<DataSourceIdentifier<?>> expectedIds = new HashSet<DataSourceIdentifier<?>>();
			expectedIds.add(new GiNumberID(6912586));
			expectedIds.add(new EntrezGeneID(25796));
			expectedIds.add(new OmimID(604951));
			expectedIds.add(new HgncID("8903"));
			expectedIds.add(new HprdID("12001"));
			expectedIds.add(new EnsemblGeneID("ENSG00000130313"));
			expectedIds.add(new UniProtID("O95336"));
			expectedIds.add(new UniProtID("D3DUQ8"));
			assertEquals(expectedIds, record.getDbLinks());
			assertEquals(CollectionsUtil.createSet(new KeggPathwayID("hsa00030"), new KeggPathwayID("hsa01100")),
					record.getPathwayIds());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

}
