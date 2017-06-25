package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.goa;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.test.TestUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.GpAssociationGoaUniprotFileData;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.GpAssociationGoaUniprotFileParser;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IpiID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

//@Ignore("file header in test file no longer matches file downloaded. Code has been updated but test has not.")
public class GpAssociationGoaUniprotFileParserTest extends RecordReaderTester {

	private File sampleGpAssociationGoaUniprotFile;
	private File idListDirectory;

	@Override
	protected String getSampleFileName() {
		return null;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		File baseSourceFileDirectory = null;
		boolean cleanIdListFiles = false;
		return new GpAssociationGoaUniprotFileParser(sampleGpAssociationGoaUniprotFile, CharacterEncoding.US_ASCII,
				null, null, baseSourceFileDirectory, cleanIdListFiles);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		initializeSampleGpAssociationGoaUniprotFile(getSampleGpAssociationGoaUniProtFileLines());
		idListDirectory = folder.newFolder("id-lists");
		File idList9606 = new File(idListDirectory, "UNIPROT.ixJNsZVcfKFuXrgmTw2kKOorPq4.utf8");
		FileWriterUtil.printLines(CollectionsUtil.createList("A0A000"), idList9606, CharacterEncoding.UTF_8);
		File idList10090 = new File(idListDirectory, "UNIPROT.dZoQfILgUb0wGOCiZE6bDGREJY4.utf8");
		FileWriterUtil.printLines(CollectionsUtil.createList("A0A123"), idList10090, CharacterEncoding.UTF_8);
		File intactIdList9606 = new File(idListDirectory, "INTACT.ixJNsZVcfKFuXrgmTw2kKOorPq4.utf8");
		FileWriterUtil.printLines(new ArrayList<String>(), intactIdList9606, CharacterEncoding.UTF_8);
		File intactIdList10090 = new File(idListDirectory, "INTACT.dZoQfILgUb0wGOCiZE6bDGREJY4.utf8");
		FileWriterUtil.printLines(new ArrayList<String>(), intactIdList10090, CharacterEncoding.UTF_8);
	}

	private List<String> getSampleGpAssociationGoaUniProtFileLines() {
		return CollectionsUtil
				.createList("IPI\tIPI00197411\t\tGO:0000900\tPMID:12933792\tIDA\t\t\t20061230\tHGNC\t\t",
						"IPI\tIPI00693437\tcolocalizes_with\tGO:0005657\tPMID:10490843\tISS\tUniProtKB:P41182\t\t20070622\tUniProtKB\t\t");
	}

	private List<String> getMoreSampleGpAssociationGoaUniProtFileLines() {
		return CollectionsUtil
				.createList(
						"UniProtKB\tA0A000\tenables\tGO:0003824\tGO_REF:0000002\tECO:0000256\tInterPro:IPR015421|InterPro:IPR015422\t\t20140118\tInterPro\t\t",
						"UniProtKB\tO15519:PRO_0000004678\tenables\tGO:0005515\tPMID:23541952\tECO:0000353\tUniProtKB:Q92851-4\t\t20140120\tIntAct\t\t");
	}

	private void initializeSampleGpAssociationGoaUniprotFile(List<String> dataLines) throws IOException {
		sampleGpAssociationGoaUniprotFile = folder.newFile("gp_association.goa_uniprot");
		ArrayList<String> lines = new ArrayList<String>(
				CollectionsUtil
						.createList("!gpa-version: 1.1\n"
								+ "!\n"
								+ "!This file contains all GO annotations for proteins in the UniProt KnowledgeBase (UniProtKB).\n"
								+ "!If a particular protein accession is not annotated with GO, then it will not appear in this file.\n"
								+ "!\n" + "!Columns:\n" + "!\n"
								+ "!   name                  required? cardinality   GAF column #\n"
								+ "!   DB                    required  1             1\n"
								+ "!   DB_Object_ID          required  1             2 / 17\n"
								+ "!   Qualifier             required  1 or greater  4\n"
								+ "!   GO ID                 required  1             5\n"
								+ "!   DB:Reference(s)       required  1 or greater  6\n"
								+ "!   ECO evidence code     required  1             7 (GO evidence code)\n"
								+ "!   With                  optional  0 or greater  8\n"
								+ "!   Interacting taxon ID  optional  0 or 1        13\n"
								+ "!   Date                  required  1             14\n"
								+ "!   Assigned_by           required  1             15\n"
								+ "!   Annotation Extension  optional  0 or greater  16\n"
								+ "!   Annotation Properties optional  0 or 1        n/a\n" + "!\n"
								+ "!Generated: 2014-01-21 00:14\n" + "!"));// ,
																			// "IPI\tIPI00197411\t\tGO:0000900\tPMID:12933792\tIDA\t\t\t20061230\tHGNC\t\t",
		// "IPI\tIPI00693437\tcolocalizes_with\tGO:0005657\tPMID:10490843\tISS\tUniProtKB:P41182\t\t20070622\tUniProtKB\t\t");
		lines.addAll(dataLines);
		FileWriterUtil.printLines(lines, sampleGpAssociationGoaUniprotFile, CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
	}

	@Test
	public void testParser() throws Exception {
		GpAssociationGoaUniprotFileParser parser = new GpAssociationGoaUniprotFileParser(
				sampleGpAssociationGoaUniprotFile, CharacterEncoding.US_ASCII, null, null, null, false);
		assertTrue(String.format("should have the first of two records."), parser.hasNext());
		GpAssociationGoaUniprotFileData dataRecord = parser.next();
		TestUtil.conductBeanComparison(getExpectedRecord1(), dataRecord);
		// assertTrue("This data record has a PubMed ID reference.",
		// dataRecord.hasPubmedReference());
		assertEquals("The pubmed id reference is 12933792", new PubMedID(12933792), dataRecord.getDbReference());
		assertTrue(String.format("should have the second of two records."), parser.hasNext());
		TestUtil.conductBeanComparison(getExpectedRecord2(), parser.next());
		assertFalse(String.format("should only have 2 records"), parser.hasNext());

	}

	@Ignore("parser has been deprecated and source file is no longer available")
	@Test
	public void testParser_withTaxon() throws Exception {
		initializeSampleGpAssociationGoaUniprotFile(getMoreSampleGpAssociationGoaUniProtFileLines());
		GpAssociationGoaUniprotFileParser parser = new GpAssociationGoaUniprotFileParser(
				sampleGpAssociationGoaUniprotFile, CharacterEncoding.US_ASCII, idListDirectory,
				CollectionsUtil.createSet(new NcbiTaxonomyID(9606)), null, false);
		assertTrue(String.format("should have the first of one record."), parser.hasNext());
		parser.next();
		assertFalse(String.format("should only have 1 record; the other record is unparseable"), parser.hasNext());

	}

	@Ignore("parser has been deprecated and source file is no longer available")
	@Test
	public void testParser_withTaxon_nomatches() throws Exception {
		initializeSampleGpAssociationGoaUniprotFile(getMoreSampleGpAssociationGoaUniProtFileLines());
		GpAssociationGoaUniprotFileParser parser = new GpAssociationGoaUniprotFileParser(
				sampleGpAssociationGoaUniprotFile, CharacterEncoding.US_ASCII, idListDirectory,
				CollectionsUtil.createSet(new NcbiTaxonomyID(10090)), null, false);
		assertFalse(String.format("should only have 0 records"), parser.hasNext());

	}

	private List<String> getLinesThatFailedInRealDataParser() {
		return CollectionsUtil
				.createList("UniProtKB\tC0LGT6\t\tGO:0005515\tPMID:18158241\tIPI\tUniProtKB:Q87Y16\ttaxon:323\t20090924\tUniProtKB\t\t");
	}

	@Test
	public void testFailedParseInRealDataFile() throws Exception {
		initializeSampleGpAssociationGoaUniprotFile(getLinesThatFailedInRealDataParser());
		GpAssociationGoaUniprotFileParser parser = (GpAssociationGoaUniprotFileParser) initSampleRecordReader();
		assertTrue(String.format("should have the first of one record."), parser.hasNext());
		GpAssociationGoaUniprotFileData dataRecord = parser.next();
		TestUtil.conductBeanComparison(getExpectedRecordFromFailedRealParse1(), dataRecord);
		// assertTrue("This data record has a PubMed ID reference.",
		// dataRecord.hasPubmedReference());
		assertEquals("The pubmed id reference is 18158241", new PubMedID(18158241), dataRecord.getDbReference());
		assertFalse(String.format("should only have 1 record"), parser.hasNext());
	}

	private GpAssociationGoaUniprotFileData getExpectedRecordFromFailedRealParse1() {
		return new GpAssociationGoaUniprotFileData(new String("UniProtKB"), new UniProtID("C0LGT6"), null,
				new GeneOntologyID("GO:0005515"), new PubMedID("PMID:18158241"), new String("IPI"), "UniProtKB:Q87Y16",
				new NcbiTaxonomyID(323), "20090924", "UniProtKB", null, null, -1, 23);
	}

	private GpAssociationGoaUniprotFileData getExpectedRecord1() {
		return new GpAssociationGoaUniprotFileData(new String("IPI"), new IpiID("IPI00197411"), null,
				new GeneOntologyID("GO:0000900"), new PubMedID("PMID:12933792"), new String("IDA"), null, null,
				"20061230", "HGNC", null, null, -1, 23);
	}

	private GpAssociationGoaUniprotFileData getExpectedRecord2() {
		return new GpAssociationGoaUniprotFileData(new String("IPI"), new IpiID("IPI00693437"), "colocalizes_with",
				new GeneOntologyID("GO:0005657"), new PubMedID("PMID:10490843"), new String("ISS"), "UniProtKB:P41182",
				null, "20070622", "UniProtKB", null, null, -1, 24);
	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00197411IDAGO_0000900PubMed_12933792> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/goa/GOARecord1> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00197411IDAGO_0000900PubMed_12933792> <http://kabob.ucdenver.edu/iao/goa/isLinkedToIPIGeneICE> <http://kabob.ucdenver.edu/iao/ipi/IPI00197411_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00197411IDAGO_0000900PubMed_12933792> <http://kabob.ucdenver.edu/iao/goa/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0000900_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00197411IDAGO_0000900PubMed_12933792> <http://kabob.ucdenver.edu/iao/goa/hasGoEvidencCode> <http://kabob.ucdenver.edu/iao/go_evidence/IDA_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00197411IDAGO_0000900PubMed_12933792> <http://kabob.ucdenver.edu/iao/goa/hasDatabaseReferenceSupportingAnnotation> <http://kabob.ucdenver.edu/iao/pm/PubMed_12933792_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00197411IDAGO_0000900PubMed_12933792> <http://kabob.ucdenver.edu/iao/goa/hasAnnotationAssigner> \"HGNC\"@en .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00693437ISSGO_0005657PubMed_10490843> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/goa/GOARecord1> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00693437ISSGO_0005657PubMed_10490843> <http://kabob.ucdenver.edu/iao/goa/isLinkedToIPIGeneICE> <http://kabob.ucdenver.edu/iao/ipi/IPI00693437_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00693437ISSGO_0005657PubMed_10490843> <http://kabob.ucdenver.edu/iao/goa/isLinkedToGeneOntologyICE> <http://kabob.ucdenver.edu/iao/go/GO_0005657_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00693437ISSGO_0005657PubMed_10490843> <http://kabob.ucdenver.edu/iao/goa/hasGoEvidencCode> <http://kabob.ucdenver.edu/iao/go_evidence/ISS_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00693437ISSGO_0005657PubMed_10490843> <http://kabob.ucdenver.edu/iao/goa/hasDatabaseReferenceSupportingAnnotation> <http://kabob.ucdenver.edu/iao/pm/PubMed_10490843_ICE> .",
						"<http://kabob.ucdenver.edu/iao/goa/GOA_RECORD_IPI00693437ISSGO_0005657PubMed_10490843> <http://kabob.ucdenver.edu/iao/goa/hasAnnotationAssigner> \"UniProtKB\"@en .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "goa-all.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("goa-all.nt", 12);
		counts.put("kabob-meta-goa-all.nt", 6);
		return counts;
	}

}
