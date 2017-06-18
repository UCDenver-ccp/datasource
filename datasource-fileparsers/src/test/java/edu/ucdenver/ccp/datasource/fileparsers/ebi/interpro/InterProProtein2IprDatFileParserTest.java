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
import static org.junit.Assert.fail;

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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PfamID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TigrFamsID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class InterProProtein2IprDatFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_INPUT_FILE_NAME = "InterPro_protein2ipr.dat";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_INPUT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new InterProProtein2IprDatFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			InterProProtein2IprDatFileParser parser = new InterProProtein2IprDatFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* A0A000 IPR004839 Aminotransferase, class I and II PF00155 36 381 */
				InterProProtein2IprDatFileData record1 = parser.next();
				assertEquals(new UniProtID("A0A000"), record1.getUniProtID());
				assertEquals(new InterProID("IPR004839"), record1.getInterProID());
				assertEquals(new String("Aminotransferase, class I and II"), record1.getInterProName());
				assertEquals(String.format("External reference ID not as expected."), new PfamID("PF00155"),
						record1.getExternalReference());
				assertEquals(String.format("Sequence start position not as expected"), new Integer(36),
						record1.getSequenceStartPosition());
				assertEquals(String.format("Sequence end position not as expected."), new Integer(381),
						record1.getSequenceEndPosition());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * A0A000 IPR010961 Tetrapyrrole biosynthesis, 5-aminolevulinic acid synthase
				 * TIGR01821 1 393
				 */
				InterProProtein2IprDatFileData record2 = parser.next();
				assertEquals(new UniProtID("A0A000"), record2.getUniProtID());
				assertEquals(new InterProID("IPR010961"), record2.getInterProID());
				assertEquals(new String("Tetrapyrrole biosynthesis, 5-aminolevulinic acid synthase"),
						record2.getInterProName());
				assertEquals(String.format("External reference ID not as expected."), new TigrFamsID("TIGR01821"),
						record2.getExternalReference());
				assertEquals(String.format("Sequence start position not as expected"), new Integer(1),
						record2.getSequenceStartPosition());
				assertEquals(String.format("Sequence end position not as expected."), new Integer(393),
						record2.getSequenceEndPosition());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* A0A003 IPR001509 NAD-dependent epimerase/dehydratase PF01370 15 249 */
				InterProProtein2IprDatFileData record3 = parser.next();
				assertEquals(new UniProtID("A0A003"), record3.getUniProtID());
				assertEquals(new InterProID("IPR001509"), record3.getInterProID());
				assertEquals(new String("NAD-dependent epimerase/dehydratase"), record3.getInterProName());
				assertEquals(String.format("External reference ID not as expected."), new PfamID("PF01370"),
						record3.getExternalReference());
				assertEquals(String.format("Sequence start position not as expected"), new Integer(15),
						record3.getSequenceStartPosition());
				assertEquals(String.format("Sequence end position not as expected."), new Integer(249),
						record3.getSequenceEndPosition());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR004839A0A000PF0015536381> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ebi.ac.uk/interpro/InterPro_ProteinToIpr_Record> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR004839A0A000PF0015536381> <http://www.ebi.ac.uk/interpro/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR004839_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR004839A0A000PF0015536381> <http://www.ebi.ac.uk/interpro/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/A0A000_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR004839A0A000PF0015536381> <http://www.ebi.ac.uk/interpro/hasRecordedSequenceStartPosition> \"36\"^^<http://www.w3.org/2001/XMLSchema#int> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR004839A0A000PF0015536381> <http://www.ebi.ac.uk/interpro/hasRecordedSequenceEndPosition> \"381\"^^<http://www.w3.org/2001/XMLSchema#int> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR004839A0A000PF0015536381> <http://www.ebi.ac.uk/interpro/isLinkedToPfamICE> <http://pfam.sanger.ac.uk/PF00155_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR010961A0A000TIGR018211393> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ebi.ac.uk/interpro/InterPro_ProteinToIpr_Record> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR010961A0A000TIGR018211393> <http://www.ebi.ac.uk/interpro/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR010961_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR010961A0A000TIGR018211393> <http://www.ebi.ac.uk/interpro/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/A0A000_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR010961A0A000TIGR018211393> <http://www.ebi.ac.uk/interpro/hasRecordedSequenceStartPosition> \"1\"^^<http://www.w3.org/2001/XMLSchema#int> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR010961A0A000TIGR018211393> <http://www.ebi.ac.uk/interpro/hasRecordedSequenceEndPosition> \"393\"^^<http://www.w3.org/2001/XMLSchema#int> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR010961A0A000TIGR018211393> <http://www.ebi.ac.uk/interpro/isLinkedToTigrFamsICE> <http://cmr.jcvi.org/tigr-scripts/CMR/TIGR01821_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR001509A0A003PF0137015249> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ebi.ac.uk/interpro/InterPro_ProteinToIpr_Record> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR001509A0A003PF0137015249> <http://www.ebi.ac.uk/interpro/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR001509_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR001509A0A003PF0137015249> <http://www.ebi.ac.uk/interpro/isLinkedToUniProtICE> <http://purl.uniprot.org/uniprot/A0A003_ICE> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR001509A0A003PF0137015249> <http://www.ebi.ac.uk/interpro/hasRecordedSequenceStartPosition> \"15\"^^<http://www.w3.org/2001/XMLSchema#int> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR001509A0A003PF0137015249> <http://www.ebi.ac.uk/interpro/hasRecordedSequenceEndPosition> \"249\"^^<http://www.w3.org/2001/XMLSchema#int> .",
						"<http://www.ebi.ac.uk/interpro/INTERPRO_PROTEIN_TO_IPR_RECORD_IPR001509A0A003PF0137015249> <http://www.ebi.ac.uk/interpro/isLinkedToPfamICE> <http://pfam.sanger.ac.uk/PF01370_ICE> .");
		Map<File, List<String>> file2ExpectedLinesMap = new HashMap<File, List<String>>();
		file2ExpectedLinesMap.put(
				FileUtil.appendPathElementsToDirectory(outputDirectory, "interpro-UniprotID2InterProID.nt"), lines);
		return file2ExpectedLinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("interpro-UniprotID2InterProID.nt", 18);
		counts.put("kabob-meta-interpro-UniprotID2InterProID.nt", 6);
		return counts;
	}
}
