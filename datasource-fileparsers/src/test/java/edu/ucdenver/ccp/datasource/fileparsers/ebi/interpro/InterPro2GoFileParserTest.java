package edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro;

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
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class InterPro2GoFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_INPUT_FILE_NAME = "interpro2go";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_INPUT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new InterPro2GoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		InterPro2GoFileParser parser = new InterPro2GoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

		assertNull(parser.next()); // for 'Elapsed:' line

		InterPro2GoFileRecord record = parser.next();
		assertEquals("IPR000005", record.getInterProId().getId());
		assertEquals("GO:0003700", record.getGoId().getId());

		record = parser.next();
		assertEquals("IPR000005", record.getInterProId().getId());
		assertEquals("GO:0043565", record.getGoId().getId());

		record = parser.next();
		assertEquals("IPR000005", record.getInterProId().getId());
		assertEquals("GO:0006355", record.getGoId().getId());

		record = parser.next();
		assertEquals("IPR000005", record.getInterProId().getId());
		assertEquals("GO:0005622", record.getGoId().getId());

		record = parser.next();
		assertEquals("IPR000006", record.getInterProId().getId());
		assertEquals("GO:0046872", record.getGoId().getId());

		record = parser.next();
		assertEquals("IPR000009", record.getInterProId().getId());
		assertEquals("GO:0008601", record.getGoId().getId());

		assertFalse(parser.hasNext());

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0003700> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/interpro/InterPro_TO_GO_Record1> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0003700> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToInterProICE> <http://kabob.ucdenver.edu/iao/interpro/IPR000005_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0003700> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0003700_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0043565> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/interpro/InterPro_TO_GO_Record1> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0043565> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToInterProICE> <http://kabob.ucdenver.edu/iao/interpro/IPR000005_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0043565> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0043565_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0006355> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/interpro/InterPro_TO_GO_Record1> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0006355> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToInterProICE> <http://kabob.ucdenver.edu/iao/interpro/IPR000005_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0006355> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0006355_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0005622> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/interpro/InterPro_TO_GO_Record1> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0005622> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToInterProICE> <http://kabob.ucdenver.edu/iao/interpro/IPR000005_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000005_GO_0005622> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0005622_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000006_GO_0046872> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/interpro/InterPro_TO_GO_Record1> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000006_GO_0046872> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToInterProICE> <http://kabob.ucdenver.edu/iao/interpro/IPR000006_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000006_GO_0046872> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0046872_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000009_GO_0008601> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/interpro/InterPro_TO_GO_Record1> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000009_GO_0008601> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToInterProICE> <http://kabob.ucdenver.edu/iao/interpro/IPR000009_ICE> .",
						"<http://kabob.ucdenver.edu/iao/interpro/INTERPRO_TO_GO_RECORD_IPR000009_GO_0008601> <http://kabob.ucdenver.edu/iao/interpro/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0008601_ICE> .");
		Map<File, List<String>> file2ExpectedLinesMap = new HashMap<File, List<String>>();
		file2ExpectedLinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "interpro-InterPro2Go.nt"),
				lines);
		return file2ExpectedLinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("interpro-InterPro2Go.nt", 18);
		counts.put("kabob-meta-interpro-InterPro2Go.nt", 6);
		return counts;
	}
}
