package edu.ucdenver.ccp.datasource.fileparsers.vectorbase;

import java.io.File;
import java.io.IOException;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;

public class VectorBaseFastaFileRecordReader_aael_transcripts extends VectorBaseFastaFileRecordReader {

	private static final String DOWNLOAD_URL = "https://www.vectorbase.org/download/aedes-aegypti-liverpooltranscriptsaaegl33fagz";
	private static final String DOWNLOAD_FILE_NAME = "Aedes-aegypti-Liverpool_TRANSCRIPTS_AaegL3.3.fa.gz";
	private static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;

	@HttpDownload(url = DOWNLOAD_URL, fileName = DOWNLOAD_FILE_NAME)
	private File downloadedFile;

	public VectorBaseFastaFileRecordReader_aael_transcripts(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(downloadedFile, encoding, skipLinePrefix);
	}

}
