package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

public class UniProtDatFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_INPUT_FILE_NAME = "UniProt_uniprot_sprot.dat";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_INPUT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new SparseUniProtDatFileRecordReader(sampleInputFile, CharacterEncoding.US_ASCII, null);
	}

	@Test
	public void testParser() throws IOException {
		assertTrue(sampleInputFile.exists());
		SparseUniProtDatFileRecordReader parser = new SparseUniProtDatFileRecordReader(sampleInputFile,
				CharacterEncoding.US_ASCII, null);

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());

	}
	
	
	@Test
	public void testParser_withTaxons() throws IOException {
		assertTrue(sampleInputFile.exists());
		SparseUniProtDatFileRecordReader parser = new SparseUniProtDatFileRecordReader(sampleInputFile,
				CharacterEncoding.US_ASCII, CollectionsUtil.createSet(new NcbiTaxonomyID(54321)));

		if (parser.hasNext()) {
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());

	}

}
