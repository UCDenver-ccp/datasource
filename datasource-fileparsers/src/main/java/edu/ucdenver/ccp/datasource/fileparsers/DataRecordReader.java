package edu.ucdenver.ccp.datasource.fileparsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;

public abstract class DataRecordReader<T extends DataRecord> extends RecordReader<T> {

	private static Logger logger = Logger.getLogger(FileRecordReader.class);

	private final StreamLineReader reader;

	public DataRecordReader(InputStream inputStream, CharacterEncoding encoding, String skipLinePrefix) throws IOException {
		logger.info(String.format("Reading records from stream."));
		reader = new StreamLineReader(inputStream, encoding, skipLinePrefix);
		initialize();
	}

	public DataRecordReader(File inputFile, CharacterEncoding encoding, String skipLinePrefix) throws IOException {
		FileUtil.validateFile(inputFile);
		logger.info(String.format("Reading records from file: %s", inputFile.getAbsolutePath()));
		reader = new StreamLineReader(inputFile, encoding, skipLinePrefix);
		initialize();
	}
	
	protected Line readLine() throws IOException {
		return reader.readLine();
	}

	@Override
	public void close() throws IOException {
		if (reader != null)
			reader.close();
	}
}
