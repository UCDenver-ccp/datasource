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
package edu.ucdenver.ccp.fileparsers.ncbi.gene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse the InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class EntrezGene2PubmedFileParser extends SingleLineFileRecordReader<EntrezGene2PubmedFileData> {

	public static final String FTP_FILE_NAME = "gene2pubmed.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	private static final String COMMENT_INDICATOR = StringConstants.POUND_SIGN;

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File gene2PubmedFile;
	
	public EntrezGene2PubmedFileParser(File gene2PubmedFile, CharacterEncoding encoding) throws IOException {
		super(gene2PubmedFile, encoding, COMMENT_INDICATOR);
	}

	public EntrezGene2PubmedFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, COMMENT_INDICATOR, null, null, clean);
	}
	
	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(gene2PubmedFile)), encoding, skipLinePrefix);
	}
	
	@Override
	protected EntrezGene2PubmedFileData parseRecordFromLine(Line line) {
		return EntrezGene2PubmedFileData.parseGene2PubmedLine(line);
	}

}
