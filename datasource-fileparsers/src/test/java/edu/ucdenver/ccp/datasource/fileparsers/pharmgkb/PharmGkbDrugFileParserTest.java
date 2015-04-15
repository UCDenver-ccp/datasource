package edu.ucdenver.ccp.fileparsers.pharmgkb;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;

@Ignore("file header in test file no longer matches file downloaded from PharmGkb. Code has been updated but test has not.")
public class PharmGkbDrugFileParserTest<T> extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "drugs.tsv";
	}

	@Override
	protected RecordReader<PharmGkbDrugFileRecord> initSampleRecordReader() throws IOException {
		return new PharmGkbDrugFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		RecordReader<PharmGkbDrugFileRecord> reader = initSampleRecordReader();
		PharmGkbDrugFileRecord r = reader.next();
		assertEquals("PA164738432", r.getAccessionId().getDataElement());
		assertEquals("AZD1152", r.getName());
		assertEquals(0, r.getGenericNames().size());
		assertEquals(0, r.getTradeNames().size());
		assertEquals(0, r.getBrandMixtures().size());
		assertEquals(String.format("type should equal 'Drug/Small Molecule'"), "Drug/Small Molecule", r.getType());
		assertEquals(0, r.getCrossReferences().size());
		assertNull(r.getUrl());
		assertNull(r.getSmiles());
		assertNull(r.getExternalVocabulary());

		r = reader.next();
		assertEquals("PA164740891", r.getAccessionId().getDataElement());
		assertEquals("zanamivir", r.getName());
		assertEquals(5, r.getGenericNames().size());
		assertEquals(1, r.getTradeNames().size());
		assertEquals(0, r.getBrandMixtures().size());
		assertEquals(String.format("type should equal 'Drug/Small Molecule'"), "Drug/Small Molecule", r.getType());
		assertEquals(5, r.getCrossReferences().size());
		assertEquals("http://en.wikipedia.org/wiki/Zanamivir", r.getUrl());
		assertNull(r.getSmiles());
		assertEquals("ATC:J05AH(Neuraminidase inhibitors)", r.getExternalVocabulary());

		assertFalse(reader.hasNext());
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("pharmgkb-drugs.nt", 19);
		counts.put("kabob-meta-pharmgkb-drugs.nt", 6);
		return counts;
	}

}
