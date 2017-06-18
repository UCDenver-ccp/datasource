package edu.ucdenver.ccp.datasource.fileparsers.mgi;

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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;

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
