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
package edu.ucdenver.ccp.fileparsers.ncbi.omim;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileArchiveUtil;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

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
	public static Map<OmimID, OmimRecordTitle> getOmimID2NameMap(File omimTxtFile, CharacterEncoding encoding)
			throws IOException {
		Map<OmimID, OmimRecordTitle> omimID2NameMap = new HashMap<OmimID, OmimRecordTitle>();
		OmimTxtFileParser parser = null;
		try {
			parser = new OmimTxtFileParser(omimTxtFile, encoding);
			while (parser.hasNext()) {
				OmimTxtFileData dataRecord = parser.next();
				OmimID mimNumber = dataRecord.getMimNumber();
				OmimRecordTitle title = dataRecord.getTitle();
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
