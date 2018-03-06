package edu.ucdenver.ccp.datasource.fileparsers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.ExcelFileLineReader;
import edu.ucdenver.ccp.common.file.reader.Line;

/**
 * Abstract class for reading data records from a file.
 * 
 * @author Bill Baumgartner
 * 
 */
public abstract class ExcelFileRecordReader<T extends FileRecord> extends FileRecordReader<T> {

	private static Logger logger = Logger.getLogger(ExcelFileRecordReader.class);

	private ExcelFileLineReader reader;
	protected Line line;
	
	/**
	 * @param stream
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public ExcelFileRecordReader(InputStream stream, CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		super(stream, encoding, skipLinePrefix);
		setReader(new ExcelFileLineReader(stream, skipLinePrefix));
		initialize();
	}

	/**
	 * This constructor takes as input a reference to the file to parse including its character
	 * encoding and line prefix that indicates a line to ignore.
	 * 
	 * @param dataFile
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public ExcelFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix) throws IOException {
		super(dataFile, encoding, skipLinePrefix);
		setReader(new ExcelFileLineReader(dataFile, skipLinePrefix));
		initialize();
	}

	/**
	 * This constructor is designed to be used in conjunction with the FTPDownload annotation. It
	 * allows the user to specify a working directory where file(s) will be downloaded. If this
	 * constructor is implemented by a subclass of FileRecordReader, then the
	 * initializeFileLineReaderFromDownload() method MUST be overriden, else an
	 * UnsupportedOperationException will be thrown.
	 * 
	 * @param workDirectory
	 * @param encoding
	 * @param skipLinePrefix
	 * @param ftpUsername
	 * @param ftpPassword
	 * @param clean
	 * @throws IOException
	 */
	public ExcelFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix, String ftpUsername,
			String ftpPassword, boolean clean) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
		setReader(initializeLineReaderFromDownload(encoding, skipLinePrefix));
		initialize();
	}

	/**
	 * This method is to be overriden by any subclass implementations of FileRecordReader that use
	 * the FileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
	 * String ftpUsername, String ftpPassword, boolean clean) constructor. In fact, it MUST be
	 * overriden by any of such subclasses.
	 * 
	 * @param encoding
	 * @param skipLinePrefix
	 * @return
	 * @throws IOException
	 */
	protected ExcelFileLineReader initializeLineReaderFromDownload(@SuppressWarnings("unused") CharacterEncoding encoding,
			@SuppressWarnings("unused") String skipLinePrefix) throws IOException {
		throw new UnsupportedOperationException(String.format(
				"The initializeFileLineReaderFromDownload( method must be overriden by the %s subclass "
						+ "of FileRecordReader in order to use the constructor which automatically downloads "
						+ "the data file.", getClass().getName()));
	}

	/**
	 * Read next line
	 * 
	 * @return the next line of text from this file, or null if end of file is encountered before
	 *         even one byte is read.
	 */
	protected Line readLine() throws IOException {
		return reader.readLine();
	}
	
	protected void advanceReader() throws IOException {
		line = readLine();
	}

	
	protected void setReader(ExcelFileLineReader reader) {
		this.reader = reader;
	}
	
	/**
	 * 
	 */
	protected void initialize() throws IOException {
		String fileHeader = getFileHeader();
		validateFileHeader(fileHeader);
		line = readLine();
		// optionally override to provide functionality, such as skipping lines at the beginning of
		// the file being parsed.
	}

	/**
	 * @return the file header for the file being read. Returns null by default. This method should
	 *         be overriden for files that contain headers.
	 * @throws IOException
	 */
	protected String getFileHeader() throws IOException {
		// optionally override to retrieve the header for a data source file
		return null;
	}

	/**
	 * 
	 * @param fileHeader
	 * @throws IOException
	 * @throws IllegalStateException
	 *             if the file head does not match the expected file header. This method provides an
	 *             initial sanity check that the file being read matches what is expected by the
	 *             code that is reading it.
	 */
	protected void validateFileHeader(String fileHeader) throws IOException {
		if (fileHeader == null && getExpectedFileHeader() == null) {
			return;
		}
		if (fileHeader == null && getExpectedFileHeader() != null) {
			throw new IllegalStateException(
					"File header inconsistency! Code changes likely required. The file header is null but the expected file header is not.");
		}
		if (fileHeader != null && getExpectedFileHeader() == null) {
			throw new IllegalStateException(
					"File header inconsistency! Code changes likely required. The expected file header is null, but the file being read has a header: "
							+ fileHeader);
		}
		if (!fileHeader.equals(getExpectedFileHeader())) {
			String msg = "";
			if (fileHeader.length() > getExpectedFileHeader().length()) {
				msg = "File header is longer than expected. (" + fileHeader.length() + ">"
						+ getExpectedFileHeader().length() + ")";
			} else if (fileHeader.length() < getExpectedFileHeader().toCharArray().length) {
				msg = "File header is shorter than expected. (" + fileHeader.length() + "<"
						+ getExpectedFileHeader().length() + ")";
			} else {
				int cIndex = 0;
				for (; cIndex < fileHeader.toCharArray().length; cIndex++) {
					if (fileHeader.toCharArray()[cIndex] != getExpectedFileHeader().toCharArray()[cIndex]) {
						break;
					}
				}
				int windowStart = (cIndex > 10) ? (cIndex - 10) : 0;
				int windowEnd = (cIndex < (fileHeader.length() - 10)) ? (cIndex + 10) : fileHeader.length();
				msg = "File header is the expected length (" + fileHeader.length()
						+ "), however there is a difference at character: " + cIndex + ". Expected: ..."
						+ getExpectedFileHeader().substring(windowStart, windowEnd) + "... but was, ..."
						+ fileHeader.subSequence(windowStart, windowEnd) + "...";
			}

			// int windowEndIndex = (cIndex+10 < fileHeader.length()) ? cIndex+10 :
			// fileHeader.length();
			throw new IllegalStateException("File header inconsistency! Code changes likely required. " + msg
					+ " \nFILE_HEADER: " + fileHeader.replaceAll(" ", "[S]").replaceAll("\\t", "[T]").replaceAll("\\n", "[N]") + "\nEXPECTED   : " + getExpectedFileHeader().replaceAll(" ", "[S]").replaceAll("\\t", "[T]").replaceAll("\\n", "[N]"));
			// + "\nLook for mismatch around character index "
			// + cIndex
			// + "; header: "
			// + fileHeader.substring(cIndex, windowEndIndex)
			// + " expected: "
			// + getExpectedFileHeader().substring(cIndex, windowEndIndex));
		}
	}

	protected String getExpectedFileHeader() throws IOException {
		// optionally override if this file has a header that can be validated
		return null;
	}

	@Override
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
		}
	}
	
	@Override
	public boolean hasNext() {
		return line != null;
	}

	protected void setNextLine(Line line) {
		this.line = line;
	}

	
	
	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		T recordToReturn = parseRecordFromLine(line);

		try {
			line = readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return recordToReturn;
	}

	protected abstract T parseRecordFromLine(Line line);

}
