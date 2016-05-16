package edu.ucdenver.ccp.datasource.fileparsers.kegg;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;

public class KeggGenesFileParserFactoryTest extends RecordReaderTester {

	private final static String SAMPLE_KEGG_GENES_FILE_NAME = "h.sapiens";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_KEGG_GENES_FILE_NAME;
	}


	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		/* make a copy of the sample input file to mimic multiple input files*/
		File duplicate = new File(sampleInputFile.getParentFile(), "copy.copy");
		FileUtil.copy(sampleInputFile, duplicate);
		Set<File> genesFilesToProcess = CollectionsUtil.createSet(sampleInputFile, duplicate);
		return KeggGenesFileParserFactory.buildAggregateRecordReader(genesFilesToProcess);
	}

	
	@Test
	public void testParser() throws IOException {
		int count = 0;
		for(RecordReader<?> rr = initSampleRecordReader();rr.hasNext();) {
			rr.next();
			count++;
		}
		assertEquals(6, count);
	}
}
