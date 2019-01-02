package edu.ucdenver.ccp.datasource.fileparsers.kegg;

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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggPathwayID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

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
			expectedIds.add(new NcbiGeneId(9081));
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
			expectedIds.add(new NcbiGeneId(8351));
			expectedIds.add(new OmimID(602811));
			expectedIds.add(new HgncID("4767"));
			expectedIds.add(new HprdID("11905"));
			expectedIds.add(new EnsemblGeneID("ENSG00000197409"));
			expectedIds.add(new UniProtID("P68431"));
			assertEquals(expectedIds, record.getDbLinks());
			assertEquals(CollectionsUtil.createSet(new KeggPathwayID("05322")), record.getPathwayIds());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			KeggGenesFileData record = parser.next();
			assertEquals(new KeggGeneID("25796"), record.getKeggGeneID());
			Set<DataSourceIdentifier<?>> expectedIds = new HashSet<DataSourceIdentifier<?>>();
			expectedIds.add(new GiNumberID(6912586));
			expectedIds.add(new NcbiGeneId(25796));
			expectedIds.add(new OmimID(604951));
			expectedIds.add(new HgncID("8903"));
			expectedIds.add(new HprdID("12001"));
			expectedIds.add(new EnsemblGeneID("ENSG00000130313"));
			expectedIds.add(new UniProtID("O95336"));
			expectedIds.add(new UniProtID("D3DUQ8"));
			assertEquals(expectedIds, record.getDbLinks());
			assertEquals(CollectionsUtil.createSet(new KeggPathwayID("00030"), new KeggPathwayID("01100")),
					record.getPathwayIds());
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

}
