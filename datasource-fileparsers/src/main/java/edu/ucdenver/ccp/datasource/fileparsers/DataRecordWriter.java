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
 */
package edu.ucdenver.ccp.datasource.fileparsers;

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
