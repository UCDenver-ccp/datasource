package edu.ucdenver.ccp.datasource.fileparsers.rgd;

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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
@Ignore("need to update the sample file to include 8 new columns")
public class RgdGeneFileRecordReaderTest extends RecordReaderTester {

	private final static String SAMPLE_DATA_FILE_NAME = "RGD_GENES_RAT.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_DATA_FILE_NAME;
	}

	@Override
	protected RgdRatGeneFileRecordReader initSampleRecordReader() throws IOException {
		return new RgdRatGeneFileRecordReader(sampleInputFile);
	}

	@Test
	public void testParser() throws IOException, URISyntaxException {
		RgdRatGeneFileRecordReader parser = initSampleRecordReader();

		if (parser.hasNext()) {
			validateRecord1(parser.next());
		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {
			validateRecord2(parser.next());
		} else {
			fail("Parser should have returned a record here.");
		}

		validateRecord3(parser.next());
		assertFalse(parser.hasNext());

		try {
			parser.next();
			fail("Should have thrown a NoSuchElementException.");
		} catch (NoSuchElementException nsee) {
			// do nothing, exception expected
		}

	}

	private void validateRecord(RgdGeneFileRecord record, RgdID expectedRgdId, String expectedSymbol,
			String expectedName) {
		assertEquals(expectedRgdId, record.getGeneId());
		assertEquals(expectedSymbol, record.getGeneSymbol());
		assertEquals(expectedName, record.getGeneName());
	}

	private void validateRecord1(RgdGeneFileRecord record) throws MalformedURLException, URISyntaxException {
		validateRecord(record, new RgdID("1595728"), "2461a1a2", "class I gene fragment 2461");
	}

	private void validateRecord2(RgdGeneFileRecord record) throws MalformedURLException, URISyntaxException {
		validateRecord(record, new RgdID("1595729"), "2458a2", "class I gene fragment 2458");
	}

	private void validateRecord3(RgdGeneFileRecord record) throws MalformedURLException, URISyntaxException {
		validateRecord(record, new RgdID("1594425"), "2361ex4-5", "class I gene fragment 2361");
	}

}
