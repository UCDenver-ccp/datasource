package edu.ucdenver.ccp.datasource.fileparsers.kegg;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;

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
	public static Map<KeggPathwayID, String> createKeggPathwayID2TitleMap(File keggMapTitleTabFile,
			CharacterEncoding encoding) throws IOException {
		Map<KeggPathwayID, String> keggPathwayID2NameMap = new HashMap<KeggPathwayID, String>();

		KeggMapTitleTabFileParser parser = null;
		try {
			parser = new KeggMapTitleTabFileParser(keggMapTitleTabFile, encoding);
			while (parser.hasNext()) {
				KeggMapTitleTabFileData dataRecord = parser.next();
				KeggPathwayID keggPathwayID = dataRecord.getKeggPathwayID();
				String keggPathwayName = dataRecord.getKeggPathwayName();

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
