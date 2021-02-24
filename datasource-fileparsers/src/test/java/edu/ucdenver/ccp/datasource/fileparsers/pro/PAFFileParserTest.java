package edu.ucdenver.ccp.datasource.fileparsers.pro;

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
import java.util.NoSuchElementException;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.obo.ProteinOntologyId;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PfamID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class PAFFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_PAF_FILE_NAME = "PAF.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_PAF_FILE_NAME;
	}

	@Override
	protected PAFFileParser initSampleRecordReader() throws IOException {
		return new PAFFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		PAFFileParser parser = initSampleRecordReader();

		parser.next();
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

		if (parser.hasNext()) {
			validateRecord3(parser.next());
		} else {
			fail("Parser should have returned a record here.");
		}
		while(parser.hasNext())
			parser.next();

		try {
			parser.next();
			fail("Should have thrown a NoSuchElementException.");
		} catch (NoSuchElementException nsee) {
			// do nothing, exception expected
		}

	}

	private void validateRecord(PAFRecord record, ProteinOntologyId expectedPrId, String expectedName,
			String expectedRelation, DataSourceIdentifier<?> expectedTargetId) {
		assertEquals(expectedPrId, record.getProteinOntologyId());
		assertEquals(expectedName, record.getProteinOntologyName());
		assertEquals(expectedRelation, record.getRelationName());
		assertEquals(expectedTargetId, record.getRelationOntologyId());
	}

	private void validateRecord1(PAFRecord record) {
		validateRecord(record, new ProteinOntologyId("PR:000000002"), "E3 ubiquitin ligase SFC complex Skp1 subunit", "has_part", new PfamID("PF01466"));
	}

	private void validateRecord2(PAFRecord record) {
		validateRecord(record, new ProteinOntologyId("PR:000000002"), "E3 ubiquitin ligase SFC complex Skp1 subunit", "has_part", new PfamID("PF03931"));
	}

	private void validateRecord3(PAFRecord record) {
		validateRecord(record, new ProteinOntologyId("PR:000000002"), "E3 ubiquitin ligase SFC complex Skp1 subunit", "part_of", new GeneOntologyID("GO:0019005"));
	}

}
