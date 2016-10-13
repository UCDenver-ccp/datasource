package edu.ucdenver.ccp.datasource.fileparsers.rgd;

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

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.format.gaf2.Gaf2FileRecordReader;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RgdRatGeneRdoAnnotationFileRecordReader extends Gaf2FileRecordReader<RgdAnnotationGaf2FileRecord> {

	@FtpDownload(server = "ftp.rgd.mcw.edu", path = "pub/data_release/annotated_rgd_objects_by_ontology/", filename = "rattus_genes_rdo", filetype = FileType.ASCII)
	private File annotationFile;

	/**
	 * @param file
	 * @param encoding
	 * @param idResolverClass
	 * @throws IOException
	 */
	public RgdRatGeneRdoAnnotationFileRecordReader(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null, RgdAnnotationFileIdResolver.class);
	}

	/**
	 * Default constructor
	 * 
	 * @param workDirectory
	 * @param clean
	 * @throws IOException
	 */
	public RgdRatGeneRdoAnnotationFileRecordReader(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, CharacterEncoding.US_ASCII, clean, null, RgdAnnotationFileIdResolver.class);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(annotationFile, encoding, skipLinePrefix);
	}

	@Override
	protected RgdAnnotationGaf2FileRecord parseRecordFromLine(Line line) {
		return new RgdAnnotationGaf2FileRecord(Gaf2FileRecordReader.parseGaf2FileLine(line));
	}

}
