package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;

@Ignore("file header in test file no longer matches file downloaded from PharmGkb. Code has been updated but test has not.")
public class PharmGkbDrugFileParserTest<T> extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "drugs.tsv";
	}

	@Override
	protected RecordReader<PharmGkbDrugFileRecord> initSampleRecordReader() throws IOException {
		return new PharmGkbDrugFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		RecordReader<PharmGkbDrugFileRecord> reader = initSampleRecordReader();
		PharmGkbDrugFileRecord r = reader.next();
		assertEquals("PA164738432", r.getAccessionId().getId());
		assertEquals("AZD1152", r.getName());
		assertEquals(0, r.getGenericNames().size());
		assertEquals(0, r.getTradeNames().size());
		assertEquals(0, r.getBrandMixtures().size());
		assertEquals(String.format("type should equal 'Drug/Small Molecule'"), "Drug/Small Molecule", r.getType());
		assertEquals(0, r.getCrossReferences().size());
		assertNull(r.getUrl());
		assertNull(r.getSmiles());
		assertNull(r.getExternalVocabulary());

		r = reader.next();
		assertEquals("PA164740891", r.getAccessionId().getId());
		assertEquals("zanamivir", r.getName());
		assertEquals(5, r.getGenericNames().size());
		assertEquals(1, r.getTradeNames().size());
		assertEquals(0, r.getBrandMixtures().size());
		assertEquals(String.format("type should equal 'Drug/Small Molecule'"), "Drug/Small Molecule", r.getType());
		assertEquals(5, r.getCrossReferences().size());
		assertEquals("http://en.wikipedia.org/wiki/Zanamivir", r.getUrl());
		assertNull(r.getSmiles());
		assertEquals("ATC:J05AH(Neuraminidase inhibitors)", r.getExternalVocabulary());

		assertFalse(reader.hasNext());
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("pharmgkb-drugs.nt", 19);
		counts.put("kabob-meta-pharmgkb-drugs.nt", 6);
		return counts;
	}

}
