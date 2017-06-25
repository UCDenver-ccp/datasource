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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;

/**
 * This class is used to parse the MRK_List#.sql.rpt files available here:
 * ftp://ftp.informatics.jax.org/pub/reports/index.html
 * 
 * header = MGI Accession ID Chr cM Position genome coordinate start genome coordinate end strand
 * Marker Symbol Status Marker Name Marker Type Feature Type Marker Synonyms (pipe-separated)
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKListFileParser extends SingleLineFileRecordReader<MRKListFileData> {
	private static Logger logger = Logger.getLogger(MRKListFileParser.class);

	private final static String HEADER = "MGI Accession ID\tChr\tcM Position\tgenome coordinate start\tgenome coordinate end\tstrand\tMarker Symbol\tStatus\tMarker Name\tMarker Type\tFeature Type\tMarker Synonyms (pipe-separated)";

	public static final String FTP_FILE_NAME = "MRK_List2.rpt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File mrkListRptFile;

	public MRKListFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public MRKListFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mrkListRptFile, encoding, skipLinePrefix);
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected MRKListFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t",-1);
		MgiGeneID mgiAccessionID = new MgiGeneID(toks[0]);
		String chromosome = null;
		if (!toks[1].equals("UN")) {
			chromosome = new String(toks[1]);
		}
		String cM_Position = new String(toks[2]);

		Integer genomeCoordinateStart = null;
		if (!toks[3].isEmpty()) {
			genomeCoordinateStart = Integer.parseInt(toks[3]);
		}
		Integer genomeCoordinateEnd = null;
		if (!toks[4].isEmpty()) {
			genomeCoordinateEnd = Integer.parseInt(toks[4]);
		}
		String strand = null;
		if (!toks[5].isEmpty()) {
			strand = toks[5];
		}
		String markerSymbol = new String(toks[6]);
		String status = toks[7];
		String markerName = new String(toks[8]);
		MgiGeneType type = MgiGeneType.getValue(toks[9]);
		String featureType = toks[10];
		Set<String> markerSynonyms = getMarkerSynonyms(toks[11]);

		return new MRKListFileData(mgiAccessionID, chromosome, cM_Position, genomeCoordinateStart, genomeCoordinateEnd,
				strand, markerSymbol, status, markerName, type, featureType, markerSynonyms, line.getByteOffset(),
				line.getLineNumber());
	}

	/**
	 * @param string
	 * @return
	 */
	private Set<String> getMarkerSynonyms(String synStr) {
		Set<String> synonyms = new HashSet<String>();
		if (!synStr.isEmpty()) {
			for (String syn : synStr.split(RegExPatterns.PIPE)) {
				synonyms.add(new String(syn));
			}
		}
		return synonyms;
	}

	/**
	 * Returns a mapping from MGI ID to gene symbol
	 * 
	 * @param mrkListFilename
	 * @return
	 */
	public static Map<MgiGeneID, String> getMgiAccessionID2SymbolMap(File mrkListFile,
			CharacterEncoding encoding) throws IOException {
		Map<MgiGeneID, String> mgiAccessionID2SymbolMap = new HashMap<MgiGeneID, String>();
		MRKListFileParser parser = new MRKListFileParser(mrkListFile, encoding);
		while (parser.hasNext()) {
			MRKListFileData dataRecord = parser.next();
			MgiGeneID mgiID = dataRecord.getMgiAccessionID();
			String symbol = dataRecord.getMarkerSymbol();
			if (!mgiAccessionID2SymbolMap.containsKey(mgiID)) {
				mgiAccessionID2SymbolMap.put(mgiID, symbol);
			} else {
				logger.warn("MGI Accession with multiple symbols detected: " + mgiID);
			}
		}
		return mgiAccessionID2SymbolMap;

	}

	/**
	 * Returns a mapping from MGI ID to gene name
	 * 
	 * @param mrkListFilename
	 * @return
	 */
	public static Map<MgiGeneID, String> getMgiAccessionID2NameMap(File mrkListFile,
			CharacterEncoding encoding) throws IOException {
		Map<MgiGeneID, String> mgiAccessionID2NameMap = new HashMap<MgiGeneID, String>();
		MRKListFileParser parser = new MRKListFileParser(mrkListFile, encoding);
		while (parser.hasNext()) {
			MRKListFileData dataRecord = parser.next();
			MgiGeneID mgiID = dataRecord.getMgiAccessionID();
			String name = dataRecord.getMarkerName();
			if (!mgiAccessionID2NameMap.containsKey(mgiID)) {
				mgiAccessionID2NameMap.put(mgiID, name);
			} else {
				logger.warn("MGI Accession with multiple names detected: " + mgiID);
			}
		}
		return mgiAccessionID2NameMap;

	}

}
