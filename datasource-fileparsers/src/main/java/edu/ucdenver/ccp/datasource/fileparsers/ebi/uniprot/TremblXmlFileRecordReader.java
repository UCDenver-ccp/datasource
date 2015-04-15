package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

public class TremblXmlFileRecordReader extends UniProtXmlFileRecordReader {

	public static final String FTP_FILE_NAME = "uniprot_trembl.xml.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.UNIPROT_KNOWLEDGEBASE_HOST, path = FtpHost.UNIPROT_KNOWLEDGEBASE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File tremblKbFile;

//	public TremblXmlFileRecordReader(File workDirectory, boolean clean) throws IOException {
//		super(workDirectory, clean);
//	}
//
//	public TremblXmlFileRecordReader(File uniprotXmlFile) throws IOException {
//		super(uniprotXmlFile);
//	}

	public TremblXmlFileRecordReader(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(workDirectory, clean, taxonIds);
	}

	public TremblXmlFileRecordReader(File uniprotXmlFile, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(uniprotXmlFile, taxonIds);
	}

	@Override
	protected InputStream initializeInputStreamFromDownload() throws IOException {
		return new GZIPInputStream(new FileInputStream(tremblKbFile));
	}

}
