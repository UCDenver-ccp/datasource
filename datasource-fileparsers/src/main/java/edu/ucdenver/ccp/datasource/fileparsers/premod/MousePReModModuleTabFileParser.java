package edu.ucdenver.ccp.fileparsers.premod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;

/**
 * http://genomequebec.mcgill.ca/PReMod/data/module/mouse/1/mouse_module_tab.txt.gz
 * 
 * @author bill
 * 
 */
public class MousePReModModuleTabFileParser extends PReModModuleTabFileParser {

	@HttpDownload(url = "http://genomequebec.mcgill.ca/PReMod/data/module/mouse/1/mouse_module_tab.txt.gz")
	private File premodModuleTabFile;

	public MousePReModModuleTabFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, clean);
	}

	@Override
	public String getDataSpecificKey() {
		return "mouse";
	}
	
	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(premodModuleTabFile)), encoding, skipLinePrefix);
	}

}
