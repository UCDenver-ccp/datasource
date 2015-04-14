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
package edu.ucdenver.ccp.fileparsers.mgi;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse MGI_EntrezGene.rpt files
 * 
 * @author Bill Baumgartner
 * 
 */
public class MGIEntrezGeneFileParser extends SingleLineFileRecordReader<MGIEntrezGeneFileData> {

	public static final String FTP_FILE_NAME = "MGI_EntrezGene.rpt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File mgiEntrezGeneRptFile;

	public MGIEntrezGeneFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public MGIEntrezGeneFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mgiEntrezGeneRptFile, encoding, skipLinePrefix);
	}

	@Override
	protected MGIEntrezGeneFileData parseRecordFromLine(Line line) {
		return MGIEntrezGeneFileData.parseMGIEntrezGeneLine(line);
	}

	/**
	 * Returns a mapping between EntrezGene ID and corresponding MGI ID(s)
	 * 
	 * @return
	 */
	public static Map<EntrezGeneID, Set<MgiGeneID>> getEntrezGene2MgiIDMap(File mgiEntrezGeneRPTFile, CharacterEncoding encoding)
			throws IOException {
		Map<EntrezGeneID, Set<MgiGeneID>> entrezGene2MgiIDMap = new HashMap<EntrezGeneID, Set<MgiGeneID>>();

		MGIEntrezGeneFileParser parser = null;
		try {
			parser = new MGIEntrezGeneFileParser(mgiEntrezGeneRPTFile, encoding);
			while (parser.hasNext()) {
				MGIEntrezGeneFileData dataRecord = parser.next();
				EntrezGeneID entrezGeneID = dataRecord.getEntrezGeneID();
				MgiGeneID mgiID = dataRecord.getMgiAccessionID();

				if (entrezGeneID != null & mgiID != null) {
					if (entrezGene2MgiIDMap.containsKey(entrezGeneID)) {
						entrezGene2MgiIDMap.get(entrezGeneID).add(mgiID);
					} else {
						Set<MgiGeneID> mgiIDs = new HashSet<MgiGeneID>();
						mgiIDs.add(mgiID);
						entrezGene2MgiIDMap.put(entrezGeneID, mgiIDs);
					}
				}
			}
			return entrezGene2MgiIDMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

}
