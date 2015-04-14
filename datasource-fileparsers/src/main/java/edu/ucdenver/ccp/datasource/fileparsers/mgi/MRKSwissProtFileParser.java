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
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse MGI MRK_SwissProt.rpt files
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKSwissProtFileParser extends SingleLineFileRecordReader<MRKSwissProtFileData> {

	public static final String FTP_FILE_NAME = "MRK_SwissProt.rpt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File mrkSwissProtRptFile;

	public MRKSwissProtFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public MRKSwissProtFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mrkSwissProtRptFile, encoding, skipLinePrefix);
	}

	@Override
	protected MRKSwissProtFileData parseRecordFromLine(Line line) {
		return MRKSwissProtFileData.parseMRKSwissProtLine(line);
	}

	/**
	 * Returns a mapping from SwissProt ID (key) to sets of MGI IDs (values)
	 * 
	 * @param mrkSwissProtFileName
	 * @return
	 */
	public static Map<UniProtID, Set<MgiGeneID>> getSwissProtID2MgiIDsMap(File mrkSwissProtFile, CharacterEncoding encoding)
			throws IOException {
		Map<UniProtID, Set<MgiGeneID>> swissProtID2MgiIDsMap = new HashMap<UniProtID, Set<MgiGeneID>>();
		MRKSwissProtFileParser parser = null;
		try {
			parser = new MRKSwissProtFileParser(mrkSwissProtFile, encoding);
			while (parser.hasNext()) {
				MRKSwissProtFileData dataRecord = parser.next();
				MgiGeneID mgiID = dataRecord.getMgiAccessionID();
				Set<UniProtID> swissProtIDs = dataRecord.getSwissProtAccessionIDs();

				for (UniProtID swissProtID : swissProtIDs) {
					if (swissProtID2MgiIDsMap.containsKey(swissProtID)) {
						swissProtID2MgiIDsMap.get(swissProtID).add(mgiID);
					} else {
						Set<MgiGeneID> mgiIDs = new HashSet<MgiGeneID>();
						mgiIDs.add(mgiID);
						swissProtID2MgiIDsMap.put(swissProtID, mgiIDs);
					}
				}
			}
			return swissProtID2MgiIDsMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

	public static Map<MgiGeneID, Set<UniProtID>> getMgiID2UniProtIDsMap(File mrkSwissProtTremblRptFile, CharacterEncoding encoding)
			throws IOException {
		Map<MgiGeneID, Set<UniProtID>> mgiID2SwissProtIDsMap = new HashMap<MgiGeneID, Set<UniProtID>>();
		MRKSwissProtFileParser parser = null;
		try {
			parser = new MRKSwissProtFileParser(mrkSwissProtTremblRptFile, encoding);

			while (parser.hasNext()) {
				MRKSwissProtFileData dataRecord = parser.next();
				MgiGeneID mgiID = dataRecord.getMgiAccessionID();
				Set<UniProtID> swissProtIDs = dataRecord.getSwissProtAccessionIDs();

				if (mgiID2SwissProtIDsMap.containsKey(mgiID)) {
					mgiID2SwissProtIDsMap.get(mgiID).addAll(swissProtIDs);
				} else {
					mgiID2SwissProtIDsMap.put(mgiID, swissProtIDs);
				}
			}
		} finally {
			if (parser != null)
				parser.close();
		}

		return mgiID2SwissProtIDsMap;
	}

}
