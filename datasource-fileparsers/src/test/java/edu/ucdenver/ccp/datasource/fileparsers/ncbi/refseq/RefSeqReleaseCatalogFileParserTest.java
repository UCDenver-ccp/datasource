/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ncbi.refseq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RefSeqReleaseCatalogFileParserTest extends RecordReaderTester {

	@Override
	protected String getSampleFileName() {
		return "RefSeq-release51.catalog";
	}

	@Override
	protected RefSeqReleaseCatalogFileParser initSampleRecordReader() throws IOException {
		return new RefSeqReleaseCatalogFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() throws IOException {
		RefSeqReleaseCatalogFileParser parser = initSampleRecordReader();
		
		if (parser.hasNext()) {
			RefSeqReleaseCatalogFileData record = parser.next();
			assertEquals(new NcbiTaxonomyID(7), record.getTaxId());
			assertEquals(record.getMoleculeType(),"RNA");
			assertFalse(record.isPredicted());
		} else {
			fail("Parser should have returned a record here.");
		}
		
		if (parser.hasNext()) {
			RefSeqReleaseCatalogFileData record = parser.next();
			assertEquals(new NcbiTaxonomyID(9), record.getTaxId());
			assertEquals(record.getMoleculeType(),"Genomic");
			assertFalse(record.isPredicted());
		} else {
			fail("Parser should have returned a record here.");
		}
		
		if (parser.hasNext()) {
			RefSeqReleaseCatalogFileData record = parser.next();
			assertEquals(new NcbiTaxonomyID(9), record.getTaxId());
			assertEquals(record.getMoleculeType(),"Protein");
			assertTrue(record.isPredicted());
		} else {
			fail("Parser should have returned a record here.");
		}
		
		assertFalse(parser.hasNext());
		
	}

}
