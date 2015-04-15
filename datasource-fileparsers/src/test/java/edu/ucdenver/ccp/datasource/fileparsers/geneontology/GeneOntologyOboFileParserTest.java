package edu.ucdenver.ccp.datasource.fileparsers.geneontology;

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

import java.io.IOException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil.ObsoleteTermHandling;
import edu.ucdenver.ccp.datasource.fileparsers.obo.impl.GeneOntologyClassIterator;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;

public class GeneOntologyOboFileParserTest extends RecordReaderTester {
	private static final String SAMPLE_GENEONTOLOGY_OBO_FILE_NAME = "gene_ontology.obo";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_GENEONTOLOGY_OBO_FILE_NAME;
	}

	@Override
	protected GeneOntologyClassIterator initSampleRecordReader() throws IOException {
		try {
			return new GeneOntologyClassIterator(sampleInputFile, ObsoleteTermHandling.EXCLUDE_OBSOLETE_TERMS);
		} catch (OBOParseException e) {
			throw new IOException(e);
		}
	}

	@Test
	public void testParser() throws Exception {
		GeneOntologyClassIterator parser = initSampleRecordReader();

		Set<String> expectedGoIds = CollectionsUtil.createSet("GO:0000001", "GO:0000002", "GO:0000003");

		int count = 0;
		while (parser.hasNext()) {
			String idStr = parser.next().getOboClass().getID();
			if (idStr.startsWith("GO")) {
				assertTrue(String.format("GO Id (%s) was not removed from set as expected.", idStr),
						expectedGoIds.remove(idStr));
				count++;
			}
		}

		assertFalse(parser.hasNext());
		assertEquals("Three records should have been processed.", 3, count);
	}

}
