package edu.ucdenver.ccp.datasource.fileparsers.ncbi.omim;

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
import java.util.HashMap;
import java.util.Map;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;

/**
 * 
 * ftp://ftp.ncbi.nih.gov/repository/OMIM/ARCHIVE/omim.txt.Z
 * 
 * NOTE: current OMIM information is available at omim.org and requires a strict
 * non-redistribution/non-derivative license.
 * 
 * @author Bill Baumgartner
 * 
 */
public class OmimTxtFileParser extends MultiLineFileRecordReader<OmimTxtFileData> {

	public static final String FTP_FILE_NAME = "omim.txt.Z";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	public static final String RECORD_TAG = "*RECORD*";
	public static final String THE_END_TAG = "*THEEND*";

	/*
	 * This is an archived vesion of OMIM last updated on Feb 22, 2011. Newer versions of OMIM are
	 * available from omim.org but require a license
	 */
	@FtpDownload(server = FtpHost.OMIM_HOST, path = FtpHost.OMIM_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY, decompress = true)
	private File omimTxtFile;

	public OmimTxtFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public OmimTxtFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(omimTxtFile, encoding, skipLinePrefix);
	}

	@Override
	protected MultiLineBuffer compileMultiLineBuffer() throws IOException {
		if (line == null)
			return null;
		MultiLineBuffer multiLineBuffer = new MultiLineBuffer();
		do {
			multiLineBuffer.add(line);
			line = readLine();
		} while (line != null && !line.getText().startsWith(RECORD_TAG) && !line.getText().startsWith(THE_END_TAG));
		line = readLine();
		return multiLineBuffer;
	}

	@Override
	protected OmimTxtFileData parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer) {
		return OmimTxtFileData.parseOmimTxtFileRecord(multiLineBuffer);
	}

	@Override
	protected void initialize() throws IOException {
		while ((line = readLine()) != null) {
			if (line.getText().startsWith(RECORD_TAG)) {
				break;
			}
		}
		line = readLine();
		super.initialize();
	}

	/**
	 * Returns a mapping from uniprot ID to full gene name
	 * 
	 * @param uniprotDatFileName
	 * @param taxonomyID
	 * @return
	 */
	public static Map<OmimID, String> getOmimID2NameMap(File omimTxtFile, CharacterEncoding encoding)
			throws IOException {
		Map<OmimID, String> omimID2NameMap = new HashMap<OmimID, String>();
		OmimTxtFileParser parser = null;
		try {
			parser = new OmimTxtFileParser(omimTxtFile, encoding);
			while (parser.hasNext()) {
				OmimTxtFileData dataRecord = parser.next();
				OmimID mimNumber = dataRecord.getMimNumber();
				String title = dataRecord.getTitle();
				omimID2NameMap.put(mimNumber, title);
			}
			return omimID2NameMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

}
