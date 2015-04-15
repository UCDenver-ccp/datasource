package edu.ucdenver.ccp.fileparsers.geneontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil.ObsoleteTermHandling;
import edu.ucdenver.ccp.datasource.fileparsers.obo.impl.GeneOntologyClassIterator;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;

public class GeneOntologyOboFileParserTest extends RecordReaderTester {
	private static final String SAMPLE_GENEONTOLOGY_OBO_FILE_NAME = "gene_ontology.obo";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_GENEONTOLOGY_OBO_FILE_NAME;
	}

	@Override
	protected GeneOntologyClassIterator initSampleRecordReader() throws IOException {
		try {
			return new GeneOntologyClassIterator(sampleInputFile, ObsoleteTermHandling.EXCLUDE_OBSOLETE_TERMS);
		} catch (OBOParseException e) {
			throw new IOException(e);
		}
	}

	@Test
	public void testParser() throws Exception {
		GeneOntologyClassIterator parser = initSampleRecordReader();

		Set<String> expectedGoIds = CollectionsUtil.createSet("GO:0000001", "GO:0000002", "GO:0000003");

		int count = 0;
		while (parser.hasNext()) {
			String idStr = parser.next().getOboClass().getID();
			if (idStr.startsWith("GO")) {
				assertTrue(String.format("GO Id (%s) was not removed from set as expected.", idStr),
						expectedGoIds.remove(idStr));
				count++;
			}
		}

		assertFalse(parser.hasNext());
		assertEquals("Three records should have been processed.", 3, count);
	}

}
