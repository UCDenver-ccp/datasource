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

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VegaID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
@Ignore("file header in test file no longer matches file downloaded from MGI. Code has been updated but test has not.")
public class MRKSequenceFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_MRKSEQUENCE_FILE_NAME = "MRK_Sequence.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_MRKSEQUENCE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MRKSequenceFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			MRKSequenceFileParser parser = new MRKSequenceFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				MRKSequenceFileData record1 = parser.next();
				assertEquals(new MgiGeneID("MGI:1915571"), record1.getMgiAccessionID());
				assertEquals(new String("12"), record1.getChromosome());
				assertEquals("39.81", record1.getCM_Position());
				assertEquals(new String("0610007P14Rik"), record1.getMarkerSymbol());
				assertEquals("O", record1.getStatus());
				assertEquals(MgiGeneType.GENE, record1.getMarkerType());
				assertEquals(new String("RIKEN cDNA 0610007P14 gene"), record1.getMarkerName());
				Set<DataSourceIdentifier<?>> expectedGenBankIds = new HashSet<DataSourceIdentifier<?>>();
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("AF270646", null));
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("AK002308", null));
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("AK004480", null));
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("AK152230", null));
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("AU019315", null));
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("BC004591", null));
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("BG066052", null));
				expectedGenBankIds.add(NucleotideAccessionResolver.resolveNucleotideAccession("C77855", null));
				assertEquals(expectedGenBankIds, record1.getGenBankAccessionIDs());
				
				Set<RefSeqID> expectedRefseqTranscriptIds = new HashSet<RefSeqID>();
				expectedRefseqTranscriptIds.add(new RefSeqID("NM_021446"));
				assertEquals(expectedRefseqTranscriptIds, record1.getRefseqTranscriptIds());
				
				Set<VegaID> expectedVegaTranscriptIds = new HashSet<VegaID>();
				expectedVegaTranscriptIds.add(new VegaID("OTTMUST00000081014"));
				expectedVegaTranscriptIds.add(new VegaID("OTTMUST00000081015"));
				expectedVegaTranscriptIds.add(new VegaID("OTTMUST00000081016"));
				expectedVegaTranscriptIds.add(new VegaID("OTTMUST00000081017"));
				expectedVegaTranscriptIds.add(new VegaID("OTTMUST00000081018"));
				assertEquals(expectedVegaTranscriptIds, record1.getVegaTranscriptIds());
				
				Set<EnsemblGeneID> expectedEnsemblTranscriptIds = new HashSet<EnsemblGeneID>();
				expectedEnsemblTranscriptIds.add(new EnsemblGeneID("ENSMUST00000021676"));
				expectedEnsemblTranscriptIds.add(new EnsemblGeneID("ENSMUST00000124311"));
				expectedEnsemblTranscriptIds.add(new EnsemblGeneID("ENSMUST00000131681"));
				expectedEnsemblTranscriptIds.add(new EnsemblGeneID("ENSMUST00000142331"));
				expectedEnsemblTranscriptIds.add(new EnsemblGeneID("ENSMUST00000148323"));
				assertEquals(expectedEnsemblTranscriptIds, record1.getEnsemblTranscriptId());
				
				assertEquals(CollectionsUtil.createSet(new UniProtID("Q9ERY9")), record1.getUniprotIds());
				assertEquals(CollectionsUtil.createSet(new UniProtID("D3YUR8")), record1.getTremblIds());
				
				Set<VegaID> expectedVegaProteinIds = new HashSet<VegaID>();
				expectedVegaProteinIds.add(new VegaID("OTTMUSP00000043235"));
				expectedVegaProteinIds.add(new VegaID("OTTMUSP00000043236"));
				assertEquals(expectedVegaProteinIds, record1.getVegaProteinIds());
				
				Set<EnsemblGeneID> expectedEnsemblProteinIds = new HashSet<EnsemblGeneID>();
				expectedEnsemblProteinIds.add(new EnsemblGeneID("ENSMUSP00000021676"));
				expectedEnsemblProteinIds.add(new EnsemblGeneID("ENSMUSP00000114987"));
				assertEquals(expectedEnsemblProteinIds, record1.getEnsemblProteinIds());
				
				assertEquals(CollectionsUtil.createSet(new RefSeqID("NP_067421")), record1.getRefseqProteinIds());
				
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				MRKSequenceFileData record2 = parser.next();
				assertEquals(new MgiGeneID("MGI:1341869"), record2.getMgiAccessionID());
			} else {
				fail("Parser should have returned a record here.");
			}
			if (parser.hasNext()) {
				MRKSequenceFileData record2 = parser.next();
				assertEquals(new MgiGeneID("MGI:1918911"), record2.getMgiAccessionID());
			} else {
				fail("Parser should have returned a record here.");
			}
			if (parser.hasNext()) {
				MRKSequenceFileData record2 = parser.next();
				assertEquals(new MgiGeneID("MGI:1923503"), record2.getMgiAccessionID());
			} else {
				fail("Parser should have returned a record here.");
			}
			if (parser.hasNext()) {
				MRKSequenceFileData record2 = parser.next();
				assertEquals(new MgiGeneID("MGI:1914085"), record2.getMgiAccessionID());
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
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_1212121> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiSequenceRecord> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_1212121> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_1212121_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_1212121> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/G48123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiSequenceRecord> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_2323232_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK002123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AI182123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK085123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AI042123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AI413123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_2323232> <http://www.informatics.jax.org/isLinkedToUniGeneICE> <http://www.ncbi.nlm.nih.gov/unigene/UNIGENE_439123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiSequenceRecord> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_3434343_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK003123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK009123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/BG076123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK160123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK002123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK013123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK008123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/BC066123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AA710123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_3434343> <http://www.informatics.jax.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/NM_025123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_4545454> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiSequenceRecord> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_4545454> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_4545454_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_4545454> <http://www.informatics.jax.org/isLinkedToGenBankICE> <http://www.ncbi.nlm.nih.gov/genbank/AK002123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_4545454> <http://www.informatics.jax.org/isLinkedToUniGeneICE> <http://www.ncbi.nlm.nih.gov/unigene/UNIGENE_396123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_4545454> <http://www.informatics.jax.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/XM_001479123_ICE> .",
						"<http://www.informatics.jax.org/MRK_SEQUENCE_FILE_RECORD_MGI_4545454> <http://www.informatics.jax.org/isLinkedToRefSeqICE> <http://www.ncbi.nlm.nih.gov/refseq/XM_921123_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-sequence.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-sequence.nt", 29);
		counts.put("kabob-meta-mgi-sequence.nt", 6);
		return counts;
	}

}
