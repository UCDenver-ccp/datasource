package edu.ucdenver.ccp.fileparsers.premod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;

/**
 * http://genomequebec.mcgill.ca/PReMod/data/module/human/1/human_module_tab.txt.gz
 * 
 * @author bill
 * 
 */
public class HumanPReModModuleTabFileParser extends PReModModuleTabFileParser {

	@HttpDownload(url = "http://genomequebec.mcgill.ca/PReMod/data/module/human/1/human_module_tab.txt.gz")
	private File premodModuleTabFile;

	public HumanPReModModuleTabFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, clean);
	}

	@Override
	public String getDataSpecificKey() {
		return "human";
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(premodModuleTabFile)), encoding, skipLinePrefix);
	}

}
