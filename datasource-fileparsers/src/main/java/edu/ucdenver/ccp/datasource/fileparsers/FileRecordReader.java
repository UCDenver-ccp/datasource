/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;

/**
 * Abstract class for reading data records from a file.
 * 
 * @author Bill Baumgartner
 * 
 */
public abstract class FileRecordReader<T extends FileRecord> extends RecordReader<T> {

	private static Logger logger = Logger.getLogger(FileRecordReader.class);

	private final StreamLineReader reader;

	/**
	 * This constructor takes as input a reference to the file to parse including its character
	 * encoding and line prefix that indicates a line to ignore.
	 * 
	 * @param dataFile
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public FileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix) throws IOException {
		FileUtil.validateFile(dataFile);
		logger.info(String.format("Reading records from file: %s", dataFile.getAbsolutePath()));
		reader = new StreamLineReader(dataFile, encoding, skipLinePrefix);
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
	public FileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix, String ftpUsername,
			String ftpPassword, boolean clean) throws IOException {
		FileUtil.validateDirectory(workDirectory);
		try {
			DownloadUtil.download(this, workDirectory, ftpUsername, ftpPassword, clean);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}
		reader = initializeLineReaderFromDownload(encoding, skipLinePrefix);
		initialize();
	}

	/**
	 * This method is to be overriden by any subclass implementations of FileRecordReader that use
	 * the FileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix, String
	 * ftpUsername, String ftpPassword, boolean clean) constructor. In fact, it MUST be overriden by
	 * any of such subclasses.
	 * 
	 * @param encoding
	 * @param skipLinePrefix
	 * @return
	 * @throws IOException
	 */
	protected StreamLineReader initializeLineReaderFromDownload(
			@SuppressWarnings("unused") CharacterEncoding encoding, @SuppressWarnings("unused") String skipLinePrefix)
			throws IOException {
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

	protected void initialize() throws IOException {
		// optionally override to provide functionality, such as skipping lines at the beginning of
		// the file being parsed.
	}

	@Override
	public void close() throws IOException {
		if (reader != null)
			reader.close();
	}

}
