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

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is deprecated. The MRK_InterPro.rpt file is no long available on the MGI FTP site
 * 
 * This class is used to parse MGI MRK_InterPro.rpt files
 * 
 * @author Bill Baumgartner
 * 
 */
@Deprecated
public class MRKInterProFileParser extends SingleLineFileRecordReader<MRKInterProFileData> {

	public static final String FTP_FILE_NAME = "MRK_InterPro.rpt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File mrkInterProRptFile;

	public MRKInterProFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public MRKInterProFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mrkInterProRptFile, encoding, skipLinePrefix);
	}

	@Override
	protected MRKInterProFileData parseRecordFromLine(Line line) {
		return MRKInterProFileData.parseMRKInterProLine(line);
	}

}
