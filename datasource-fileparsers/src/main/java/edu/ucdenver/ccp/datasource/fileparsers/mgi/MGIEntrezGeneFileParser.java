package edu.ucdenver.ccp.datasource.fileparsers.mgi;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;

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
	public static Map<NcbiGeneId, Set<MgiGeneID>> getEntrezGene2MgiIDMap(File mgiEntrezGeneRPTFile, CharacterEncoding encoding)
			throws IOException {
		Map<NcbiGeneId, Set<MgiGeneID>> entrezGene2MgiIDMap = new HashMap<NcbiGeneId, Set<MgiGeneID>>();

		MGIEntrezGeneFileParser parser = null;
		try {
			parser = new MGIEntrezGeneFileParser(mgiEntrezGeneRPTFile, encoding);
			while (parser.hasNext()) {
				MGIEntrezGeneFileData dataRecord = parser.next();
				NcbiGeneId entrezGeneID = dataRecord.getEntrezGeneID();
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
