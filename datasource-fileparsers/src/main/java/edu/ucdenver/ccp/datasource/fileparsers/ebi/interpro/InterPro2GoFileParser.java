/**
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

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * File parser for InterPro interpro2go file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
public class InterPro2GoFileParser extends SingleLineFileRecordReader<InterPro2GoFileRecord> {

	public static final String FTP_FILE_NAME = "interpro2go";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	public static final String COMMENT_PREFIX = "!";

	@FtpDownload(server = FtpHost.INTERPRO_HOST, path = FtpHost.INTERPRO_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File interpro2goFile;

	/**
	 * Constructor.
	 * 
	 * @param dataFile
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public InterPro2GoFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, COMMENT_PREFIX);
	}

	public InterPro2GoFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, COMMENT_PREFIX, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(interpro2goFile, encoding, skipLinePrefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.fileparsers.SingleLineFileRecordReader#parseRecordFromLine(edu.ucdenver.
	 * ccp.common.file.reader.LineReader.Line)
	 */
	@Override
	protected InterPro2GoFileRecord parseRecordFromLine(Line line) {
		String text = line.getText();
		InterPro2GoFileRecord r = null;

		if (text.startsWith("InterPro:")) {
			String interProId = text.split(" ")[0].split(":")[1].trim();
			String[] tokens = text.split(";");
			String goId = tokens[tokens.length - 1].trim();

			r = new InterPro2GoFileRecord(new InterProID(interProId), new GeneOntologyID(goId), line.getByteOffset(),
					line.getLineNumber());
		}

		return r;
	}
}
