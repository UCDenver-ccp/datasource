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
 *  Deprecated b/c the MRK_Ensembl_Pheno.rpt file is no longer available on the MGI FTP site
 *  
 * This class is used to parse MGI MRK_EnsemblPheno.rpt files
 * 
 * @author Bill Baumgartner
 * 
 */
@Deprecated
public class MRKEnsemblPhenoFileParser extends SingleLineFileRecordReader<MRKEnsemblPhenoFileData> {

	public static final String FTP_FILE_NAME = "MRK_Ensembl_Pheno.rpt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File mrkEnsemblPhenoRptFile;

	public MRKEnsemblPhenoFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public MRKEnsemblPhenoFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mrkEnsemblPhenoRptFile, encoding, skipLinePrefix);
	}

	@Override
	protected MRKEnsemblPhenoFileData parseRecordFromLine(Line line) {
		return MRKEnsemblPhenoFileData.parseMRKEnsemblPhenoLine(line);
	}

}
