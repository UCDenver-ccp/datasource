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
package edu.ucdenver.ccp.fileparsers.ncbi.taxonomy;

import java.io.File;
import java.io.IOException;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

/**
 * This class is used to parse the NCBI Taxonomy names.dmp file, located in taxdump.tar.gz here:
 * ftp://ftp.ncbi.nih.gov/pub/taxonomy/
 * 
 * @author Bill Baumgartner
 * 
 */
public class NcbiTaxonomyNamesDmpFileParser extends SingleLineFileRecordReader<NcbiTaxonomyNamesDmpFileData> {

	// need to add auto-untarring to download util to get the target file.
//	public static final String FTP_FILE_NAME = "taxdump.tar.gz";
//	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
//
//	@FtpDownload(server = FtpHost.NCBI_HOST, path = FtpHost.NCBI_TAXONOMY_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY, targetFileName = "names.dmp")
//	private File namesDmpFile;

	public NcbiTaxonomyNamesDmpFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

//	public NcbiTaxonomyNamesDmpFileParser(File workDirectory, boolean clean) throws IOException {
//		super(workDirectory, ENCODING, null, null, null, clean);
//	}
//
//	@Override
//	protected LineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
//			throws IOException {
//		return new LineReader(namesDmpFile, encoding, skipLinePrefix);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.fileparsers.SingleLineFileRecordReader#parseRecordFromLine(edu.ucdenver.
	 * ccp.common.file.reader.Line)
	 */
	@Override
	protected NcbiTaxonomyNamesDmpFileData parseRecordFromLine(Line line) {
		return NcbiTaxonomyNamesDmpFileData.parseNCBITaxonomyNamesDmpLine(line);
	}
}
