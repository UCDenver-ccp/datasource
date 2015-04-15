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
package edu.ucdenver.ccp.fileparsers.rgd;

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
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;

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
