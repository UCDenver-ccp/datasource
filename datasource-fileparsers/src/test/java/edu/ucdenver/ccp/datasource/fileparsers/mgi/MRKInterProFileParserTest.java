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
package edu.ucdenver.ccp.fileparsers.mgi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKInterProFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_MRKINTERPRO_FILE_NAME = "MRK_InterPro.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_MRKINTERPRO_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MRKInterProFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			MRKInterProFileParser parser = new MRKInterProFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/* MGI:1212121 0600034P09Rik IPR012345 IPR001010 IPR01111 */
				MRKInterProFileData record1 = parser.next();
				assertEquals(new MgiGeneID("MGI:1212121"), record1.getMgiAccessionID());
				assertEquals(new String("0600034P09Rik"), record1.getMarkerSymbol());
				assertEquals(3, record1.getInterProAccessionIDs().size());
				Set<InterProID> interProIDs = new HashSet<InterProID>();
				interProIDs.add(new InterProID("IPR012345"));
				interProIDs.add(new InterProID("IPR001010"));
				interProIDs.add(new InterProID("IPR01111"));
				assertEquals(interProIDs, record1.getInterProAccessionIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* MGI:1313131 0600034P10Rik IPR002222 IPR003333 IPR04444 IPR015555 IPR016666 */
				MRKInterProFileData record2 = parser.next();
				assertEquals(new MgiGeneID("MGI:1313131"), record2.getMgiAccessionID());
				assertEquals(new String("0600034P10Rik"), record2.getMarkerSymbol());
				assertEquals(5, record2.getInterProAccessionIDs().size());
				Set<InterProID> interProIDs = new HashSet<InterProID>();
				interProIDs.add(new InterProID("IPR002222"));
				interProIDs.add(new InterProID("IPR003333"));
				interProIDs.add(new InterProID("IPR04444"));
				interProIDs.add(new InterProID("IPR015555"));
				interProIDs.add(new InterProID("IPR016666"));
				assertEquals(interProIDs, record2.getInterProAccessionIDs());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/* MGI:1414141 0600034P11Rik IPR007890 */
				MRKInterProFileData record3 = parser.next();
				assertEquals(new MgiGeneID("MGI:1414141"), record3.getMgiAccessionID());
				assertEquals(new String("0600034P11Rik"), record3.getMarkerSymbol());
				assertEquals(1, record3.getInterProAccessionIDs().size());
				Set<InterProID> interProIDs = new HashSet<InterProID>();
				interProIDs.add(new InterProID("IPR007890"));
				assertEquals(interProIDs, record3.getInterProAccessionIDs());
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
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1212121> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiInterProRecord> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1212121> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1212121_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1212121> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR001010_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1212121> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR012345_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1212121> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR01111_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1313131> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiInterProRecord> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1313131> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1313131_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1313131> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR015555_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1313131> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR003333_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1313131> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR002222_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1313131> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR04444_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1313131> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR016666_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1414141> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiInterProRecord> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1414141> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1414141_ICE> .",
						"<http://www.informatics.jax.org/MRK_INTERPRO_FILE_RECORD_MGI_1414141> <http://www.informatics.jax.org/isLinkedToInterProICE> <http://www.ebi.ac.uk/interpro/IPR007890_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-interpro.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-interpro.nt", 15);
		counts.put("kabob-meta-mgi-interpro.nt", 6);
		return counts;
	}

}
