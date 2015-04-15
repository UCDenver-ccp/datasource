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
package edu.ucdenver.ccp.fileparsers.ebi.interpro;

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
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PfamID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.TigrFamsID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;

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
				assertEquals(new InterProName("Aminotransferase, class I and II"), record1.getInterProName());
				assertEquals(String.format("External reference ID not as expected."), new PfamID("PF00155"),
						record1.getExternalReference());
				assertEquals(String.format("Sequence start position not as expected"), new SequencePosition(36),
						record1.getSequenceStartPosition());
				assertEquals(String.format("Sequence end position not as expected."), new SequencePosition(381),
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
				assertEquals(new InterProName("Tetrapyrrole biosynthesis, 5-aminolevulinic acid synthase"),
						record2.getInterProName());
				assertEquals(String.format("External reference ID not as expected."), new TigrFamsID("TIGR01821"),
						record2.getExternalReference());
				assertEquals(String.format("Sequence start position not as expected"), new SequencePosition(1),
						record2.getSequenceStartPosition());
				assertEquals(String.format("Sequence end position not as expected."), new SequencePosition(393),
						record2.getSequenceEndPosition());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* A0A003 IPR001509 NAD-dependent epimerase/dehydratase PF01370 15 249 */
				InterProProtein2IprDatFileData record3 = parser.next();
				assertEquals(new UniProtID("A0A003"), record3.getUniProtID());
				assertEquals(new InterProID("IPR001509"), record3.getInterProID());
				assertEquals(new InterProName("NAD-dependent epimerase/dehydratase"), record3.getInterProName());
				assertEquals(String.format("External reference ID not as expected."), new PfamID("PF01370"),
						record3.getExternalReference());
				assertEquals(String.format("Sequence start position not as expected"), new SequencePosition(15),
						record3.getSequenceStartPosition());
				assertEquals(String.format("Sequence end position not as expected."), new SequencePosition(249),
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
