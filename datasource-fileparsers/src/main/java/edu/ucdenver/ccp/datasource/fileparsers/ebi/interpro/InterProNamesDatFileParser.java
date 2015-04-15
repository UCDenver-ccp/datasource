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
package edu.ucdenver.ccp.fileparsers.ebi.interpro;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse the InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class InterProNamesDatFileParser extends SingleLineFileRecordReader<InterProNamesDatFileData> {

	public static final String FTP_FILE_NAME = "names.dat";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.INTERPRO_HOST, path = FtpHost.INTERPRO_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File interProNamesDatFile;

	public InterProNamesDatFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public InterProNamesDatFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(interProNamesDatFile, encoding, skipLinePrefix);
	}

	@Override
	protected InterProNamesDatFileData parseRecordFromLine(Line line) {
		return InterProNamesDatFileData.parseInterProNamesDatLine(line);
	}

	/**
	 * Returns a mapping from InterPro ID to InterPro Name.
	 * 
	 * @param interProNamesDatFile
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getInterProID2NameMap(File interProNamesDatFile, CharacterEncoding encoding)
			throws IOException {
		Map<String, String> interProID2NameMap = new HashMap<String, String>();
		InterProNamesDatFileParser parser = new InterProNamesDatFileParser(interProNamesDatFile, encoding);

		while (parser.hasNext()) {
			InterProNamesDatFileData dataRecord = parser.next();
			InterProID interProID = dataRecord.getInterProID();
			InterProName interProName = dataRecord.getInterProName();
			interProID2NameMap.put(interProID.getDataElement(), interProName.getDataElement());
		}

		parser.close();

		return interProID2NameMap;
	}

}
