package edu.ucdenver.ccp.fileparsers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;

public abstract class DataRecordWriter<T extends DataRecord> {

	private final List<String> headerLines;
	protected final BufferedWriter writer;

	public DataRecordWriter(File outputFile, CharacterEncoding encoding, WriteMode writeMode, FileSuffixEnforcement suffixEnforcementPolicy)
			throws FileNotFoundException {
		this.headerLines = new ArrayList<String>();
		this.writer = FileWriterUtil.initBufferedWriter(outputFile, encoding, writeMode, suffixEnforcementPolicy);

	}

	public DataRecordWriter(File outputFile, CharacterEncoding encoding, List<String> headerLines)
			throws FileNotFoundException {
		this.headerLines = headerLines;
		this.writer = FileWriterUtil.initBufferedWriter(outputFile, encoding, WriteMode.OVERWRITE, FileSuffixEnforcement.ON);
	}

	public abstract String getStorageLine(T data);

	public void write(List<T> data) throws IOException {
		FileWriterUtil.printLines(headerLines, writer);
		for (T lineData : data) {
			write(lineData);
		}
	}

	public void write(T data) throws IOException {
		writer.write(getStorageLine(data));
		writer.newLine();
	}

	public void close() throws IOException {
		writer.close();
	}

}
