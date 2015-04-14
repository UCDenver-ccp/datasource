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
package edu.ucdenver.ccp.fileparsers.kegg;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse the Kegg map_title.tab file
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggMapTitleTabFileParser extends SingleLineFileRecordReader<KeggMapTitleTabFileData> {
	private static final Logger logger = Logger.getLogger(KeggMapTitleTabFileParser.class);

	public static final String FTP_FILE_NAME = "map_title.tab";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	@FtpDownload(server = FtpHost.KEGG_MAPTITLETAB_HOST, path = FtpHost.KEGG_MAPTITLETAB_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File keggMapTitleTabFile;

	public KeggMapTitleTabFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public KeggMapTitleTabFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(keggMapTitleTabFile, encoding, skipLinePrefix);
	}

	@Override
	protected KeggMapTitleTabFileData parseRecordFromLine(Line line) {
		return KeggMapTitleTabFileData.parseKeggMapTitleTabLine(line);
	}

	/**
	 * Create a mapping between the KEGG pathway ID and the KEGG pathway name
	 */
	public static Map<KeggPathwayID, KeggPathwayName> createKeggPathwayID2TitleMap(File keggMapTitleTabFile,
			CharacterEncoding encoding) throws IOException {
		Map<KeggPathwayID, KeggPathwayName> keggPathwayID2NameMap = new HashMap<KeggPathwayID, KeggPathwayName>();

		KeggMapTitleTabFileParser parser = null;
		try {
			parser = new KeggMapTitleTabFileParser(keggMapTitleTabFile, encoding);
			while (parser.hasNext()) {
				KeggMapTitleTabFileData dataRecord = parser.next();
				KeggPathwayID keggPathwayID = dataRecord.getKeggPathwayID();
				KeggPathwayName keggPathwayName = dataRecord.getKeggPathwayName();

				if (!keggPathwayID2NameMap.containsKey(keggPathwayID)) {
					keggPathwayID2NameMap.put(keggPathwayID, keggPathwayName);
				} else {
					logger.error("Duplicate KEGG Pathway ID (" + keggPathwayID + ") discovered in: "
							+ keggMapTitleTabFile);
				}
			}
			return keggPathwayID2NameMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

}
