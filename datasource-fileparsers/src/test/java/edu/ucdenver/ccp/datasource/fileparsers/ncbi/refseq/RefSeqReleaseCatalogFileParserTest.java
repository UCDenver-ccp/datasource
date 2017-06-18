package edu.ucdenver.ccp.datasource.fileparsers.ncbi.refseq;

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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RefSeqReleaseCatalogFileParserTest extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "RefSeq-release51.catalog";
	}

	@Override
	protected RefSeqReleaseCatalogFileParser initSampleRecordReader() throws IOException {
		return new RefSeqReleaseCatalogFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		RefSeqReleaseCatalogFileParser parser = initSampleRecordReader();
		
		if (parser.hasNext()) {
			RefSeqReleaseCatalogFileData record = parser.next();
			assertEquals(new NcbiTaxonomyID(7), record.getTaxId());
			assertEquals(record.getMoleculeType(),"RNA");
			assertFalse(record.isPredicted());
		} else {
			fail("Parser should have returned a record here.");
		}
		
		if (parser.hasNext()) {
			RefSeqReleaseCatalogFileData record = parser.next();
			assertEquals(new NcbiTaxonomyID(9), record.getTaxId());
			assertEquals(record.getMoleculeType(),"Genomic");
			assertFalse(record.isPredicted());
		} else {
			fail("Parser should have returned a record here.");
		}
		
		if (parser.hasNext()) {
			RefSeqReleaseCatalogFileData record = parser.next();
			assertEquals(new NcbiTaxonomyID(9), record.getTaxId());
			assertEquals(record.getMoleculeType(),"Protein");
			assertTrue(record.isPredicted());
		} else {
			fail("Parser should have returned a record here.");
		}
		
		assertFalse(parser.hasNext());
		
	}

}
