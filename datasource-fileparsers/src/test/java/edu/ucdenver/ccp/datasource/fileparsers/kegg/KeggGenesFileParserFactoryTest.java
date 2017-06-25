package edu.ucdenver.ccp.datasource.fileparsers.kegg;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2016 Regents of the University of Colorado
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

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.io.ClassPathUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

public class KeggGenesFileParserFactoryTest extends RecordReaderTester {

	private final static String SAMPLE_KEGG_GENES_FILE_NAME = "h.sapiens";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_KEGG_GENES_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		/* make a copy of the sample input file to mimic multiple input files */
		File duplicate = new File(sampleInputFile.getParentFile(), "copy.copy");
		FileUtil.copy(sampleInputFile, duplicate);
		Set<File> genesFilesToProcess = CollectionsUtil.createSet(sampleInputFile, duplicate);
		return KeggGenesFileParserFactory.buildAggregateRecordReader(genesFilesToProcess);
	}

	@Test
	public void testParser() throws IOException {
		int count = 0;
		for (RecordReader<?> rr = initSampleRecordReader(); rr.hasNext();) {
			rr.next();
			count++;
		}
		assertEquals(6, count);
	}

	@Test
	public void testGetAbbreviatedSpeciesNames() throws IOException {
		File genomeFile = ClassPathUtil.copyClasspathResourceToFile(KeggGenesFileParserFactoryTest.class,
				"KEGG_genome", new File(outputDirectory, "genome"));
		Set<NcbiTaxonomyID> taxonIds = CollectionsUtil.createSet(new NcbiTaxonomyID(9544));

		Set<String> abbreviatedSpeciesNamesForTaxonIds = KeggGenesFileParserFactory
				.getAbbreviatedSpeciesNamesForTaxonIds(taxonIds, genomeFile);

		Set<String> expectedAbbrevSpeciesNames = CollectionsUtil.createSet("M.mulatta");

		assertEquals(expectedAbbrevSpeciesNames, abbreviatedSpeciesNamesForTaxonIds);
	}
}
