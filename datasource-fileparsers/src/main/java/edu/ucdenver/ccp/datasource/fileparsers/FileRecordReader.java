/*
 Copyright (c) 2012, Regents of the University of Colorado
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
    list of conditions and the following disclaimer.
   
 * Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.
   
 * Neither the name of the University of Colorado nor the names of its 
    contributors may be used to endorse or promote products derived from this 
    software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */package edu.ucdenver.ccp.datasource.fileparsers;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;

/**
 * Abstract class for reading data records from a file.
 * 
 * @author Bill Baumgartner
 * 
 */
public abstract class FileRecordReader<T extends FileRecord> extends RecordReader<T> {

	private static Logger logger = Logger.getLogger(FileRecordReader.class);

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
	}

	

}
