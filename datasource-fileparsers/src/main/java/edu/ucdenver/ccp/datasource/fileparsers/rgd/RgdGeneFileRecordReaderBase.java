package edu.ucdenver.ccp.datasource.fileparsers.rgd;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import lombok.Getter;

/**
 * This class is used to parse the PreMod mouse_module_tab.txt file
 * 
 * @author Bill Baumgartner
 * 
 */
public class RgdGeneFileRecordReaderBase extends SingleLineFileRecordReader<RgdGeneFileRecord> {

	public static final String FTP_SERVER = "ftp.rgd.mcw.edu";
	public static final String FTP_PATH = "pub/data_release";

	public static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;
	public static final String SKIP_LINE_PREFIX = StringConstants.POUND_SIGN;

	private static Logger logger = Logger.getLogger(RgdGeneFileRecordReaderBase.class);

	@Getter
	private String oldReferenceAssemblyNumber;
	@Getter
	private String currentReferenceAssemblyNumber;
	
	public RgdGeneFileRecordReaderBase(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, SKIP_LINE_PREFIX);
	}

	protected RgdGeneFileRecordReaderBase(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, SKIP_LINE_PREFIX, null, null, clean);
	}

	@Override
	protected String getFileHeader() throws IOException {
		String headerLine = readLine().getText();
		Pattern assemblyPattern = Pattern.compile("CHROMOSOME_(\\d\\.\\d)\\sCHROMOSOME_(\\d\\.\\d)");
		Matcher m = assemblyPattern.matcher(headerLine);
		if (m.find()) {
			oldReferenceAssemblyNumber = m.group(1);
			currentReferenceAssemblyNumber = m.group(2);
		} else {
			throw new IOException("Could not extract assembly numbers from file header: " + headerLine);
		}
		return headerLine;
	}

	@Override
	protected RgdGeneFileRecord parseRecordFromLine(Line line) {
		return RgdGeneFileRecord.parseGeneFileLine(line, oldReferenceAssemblyNumber, currentReferenceAssemblyNumber);
	}

}
