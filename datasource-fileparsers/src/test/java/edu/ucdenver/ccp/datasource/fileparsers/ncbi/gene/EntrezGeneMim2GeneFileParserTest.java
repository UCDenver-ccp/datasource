package edu.ucdenver.ccp.fileparsers.ncbi.gene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

@Ignore("file format changed. Code has been updated but test would need to be revised")
public class EntrezGeneMim2GeneFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "EntrezGene_mim2gene";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new EntrezGeneMim2GeneFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws Exception {
		EntrezGeneMim2GeneFileParser parser = new EntrezGeneMim2GeneFileParser(sampleInputFile,
				CharacterEncoding.US_ASCII);

		// 100300 100188340 gene
		if (parser.hasNext())
			checkMim2GeneRecord(parser.next(), "100300", "100188340", "gene");
		else
			fail("Parser should have returned the first record");

		// 100640 216 gene
		if (parser.hasNext())
			checkMim2GeneRecord(parser.next(), "100640", "216", "gene");
		else
			fail("Parser should have returned the second record");

		assertFalse(parser.hasNext());
	}

	private void checkMim2GeneRecord(EntrezGeneMim2GeneFileData record, String expectedMimNumber,
			String expectedEntrezGeneID, String expectedAssociationType) {
		assertEquals(String.format("OmimID not as expected."), new OmimID(expectedMimNumber), record.getMimNumber());
		assertEquals(String.format("EntrezGeneID not as expected."), new EntrezGeneID(expectedEntrezGeneID),
				record.getEntrezGeneID());
		assertEquals(String.format("Omim association type not as expected."), expectedAssociationType,
				record.getAssociationType());

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/gene/EntrezGeneToOmimRecord> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_100188340_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToOmimICE> <http://www.ncbi.nlm.nih.gov/omim/OMIM_100300_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100300EG_100188340> <http://www.ncbi.nlm.nih.gov/gene/hasRecordedOmimAssociationType> \"gene\"@en .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.ncbi.nlm.nih.gov/gene/EntrezGeneToOmimRecord> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_216_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.ncbi.nlm.nih.gov/gene/isLinkedToOmimICE> <http://www.ncbi.nlm.nih.gov/omim/OMIM_100640_ICE> .",
						"<http://www.ncbi.nlm.nih.gov/gene/OMIM_TO_ENTREZGENE_RECORD_OMIM_100640EG_216> <http://www.ncbi.nlm.nih.gov/gene/hasRecordedOmimAssociationType> \"gene\"@en .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "entrezgene-omim.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("entrezgene-omim.nt", 8);
		counts.put("kabob-meta-entrezgene-omim.nt", 6);
		return counts;
	}
}
