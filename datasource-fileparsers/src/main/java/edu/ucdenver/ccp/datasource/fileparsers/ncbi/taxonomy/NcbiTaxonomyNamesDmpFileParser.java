package edu.ucdenver.ccp.datasource.fileparsers.ncbi.taxonomy;

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
