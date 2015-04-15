package edu.ucdenver.ccp.fileparsers.ncbi.gene;

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
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

public class EntrezGene2PubmedFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "EntrezGene_gene2pubmed";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected EntrezGene2PubmedFileParser initSampleRecordReader() throws IOException {
		return new EntrezGene2PubmedFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws Exception {
		EntrezGene2PubmedFileParser parser = initSampleRecordReader();

		// 9 1246500 9873079
		if (parser.hasNext())
			checkGene2PubmedRecord(parser.next(), "9", "1246500", "9873079");
		else
			fail("Parser should have returned the first record");

		// 10 1234567 9999999
		if (parser.hasNext())
			checkGene2PubmedRecord(parser.next(), "10", "1234567", "9999999");
		else
			fail("Parser should have returned the second record");

		assertFalse(parser.hasNext());
	}

	private void checkGene2PubmedRecord(EntrezGene2PubmedFileData record, String expectedTaxonomyId,
			String expectedEntrezGeneID, String expectedPubMedId) {
		assertEquals(String.format("Taxonomy ID not as expected."), new NcbiTaxonomyID(expectedTaxonomyId),
				record.getTaxonomyID());
		assertEquals(String.format("EntrezGeneID not as expected."), new EntrezGeneID(expectedEntrezGeneID),
				record.getEntrezGeneID());
		assertEquals(String.format("PubMed ID not as expected."), new PubMedID(expectedPubMedId), record.getPubmedID());

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil.createList("");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "entrezgene-pubmed.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("entrezgene-pubmed.nt", 8);
		counts.put("kabob-meta-entrezgene-pubmed.nt", 6);
		return counts;
	}
}
