package edu.ucdenver.ccp.fileparsers.ebi.interpro;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

public class InterProProtein2IprDatFileParserTestFn extends DefaultTestCase {

	/**
	 * This functional test downloads the real protein2ipr.dat file from the InterPro FTP site. It
	 * checks that the download succeeded and that it's been unzipped, then proceeds to parse the
	 * entire file to make sure the file format has not changed unexpected.
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void testDownloadAndParseOfRealFile() throws Exception {
		File workDirectory = folder.newFolder("work");
		InterProProtein2IprDatFileParser parser = new InterProProtein2IprDatFileParser(workDirectory, true);
		File expectedDownloadedFile = FileUtil.appendPathElementsToDirectory(workDirectory,
				StringUtil.removeSuffix(InterProProtein2IprDatFileParser.FTP_FILE_NAME,".gz"));
		assertTrue(String.format("file must exist"), expectedDownloadedFile.exists());
		assertTrue(String.format("file must not be empty"), expectedDownloadedFile.length() > 0);
		while (parser.hasNext())
			parser.next();
	}
}
