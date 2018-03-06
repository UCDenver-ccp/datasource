package edu.ucdenver.ccp.datasource.fileparsers.manuscript_data.santos2016;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2018 Regents of the University of Colorado
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

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.ExcelFileLineReader;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.ExcelFileRecordReader;

public class Santos2016SupTableS2RecordReader extends ExcelFileRecordReader<Santos2016SupTableS2FileData> {

	// http://www.nature.com/nrd/journal/vaop/ncurrent/extref/nrd.2016.230-s2.xlsx
	@HttpDownload(url = "http://www.nature.com/nrd/journal/vaop/ncurrent/extref/nrd.2016.230-s2.xlsx", fileName = "nrd.2016.230-s2.xlsx")
	private File xlsxFile;

	public Santos2016SupTableS2RecordReader(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	public Santos2016SupTableS2RecordReader(File workDirectory, CharacterEncoding encoding, boolean clean)
			throws IOException {
		super(workDirectory, encoding, null, null, null, clean);
	}

	public Santos2016SupTableS2RecordReader(InputStream stream, CharacterEncoding encoding) throws IOException {
		super(stream, encoding, null);
	}

	@Override
	protected ExcelFileLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new ExcelFileLineReader(xlsxFile, null);
	}

	@Override
	protected Santos2016SupTableS2FileData parseRecordFromLine(Line line) {
		return Santos2016SupTableS2FileData.parseSupTableS2Line(line);
	}

}
