package edu.ucdenver.ccp.datasource.fileparsers.pro;

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
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ProteinOntologyId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * File parser for Protein Ongology promapping.txt file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
public class ProMappingFileParser extends SingleLineFileRecordReader<ProMappingRecord> {

	public static final String IS_A_RELATION = "is_a";
	public static final String EXACT_RELATION = "exact";

	public static final CharacterEncoding FILE_ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = "ftp.pir.georgetown.edu", path = "databases/ontology/pro_obo/PRO_mappings/", filename = "promapping.txt", filetype = FileType.ASCII)
	private File mappingFile;

	/**
	 * Constructor.
	 * 
	 * @param dataFile
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public ProMappingFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding);
	}

	/**
	 * Default constructor
	 * 
	 * @param workDirectory
	 * @param clean
	 * @throws IOException
	 */
	public ProMappingFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, FILE_ENCODING, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mappingFile, encoding, skipLinePrefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.fileparsers.SingleLineFileRecordReader#parseRecordFromLine
	 * (edu.ucdenver. ccp.common.file.reader.LineReader.Line)
	 */
	@Override
	protected ProMappingRecord parseRecordFromLine(Line line) {
		String text = line.getText();
		ProMappingRecord r = null;

		if (text.startsWith("PR:")) {

			String[] tokens = text.split("\t");
			ProteinOntologyId fromId = new ProteinOntologyId(tokens[0].trim());
			DataSourceIdentifier<?> targetId = resolveId(tokens[1].trim());
			String mappingType = tokens[2].trim();

			r = new ProMappingRecord(fromId, targetId, mappingType, line.getByteOffset(), line.getLineNumber());
		}

		return r;
	}

	private DataSourceIdentifier<?> resolveId(String idStr) {
		try {
			if (idStr.startsWith("MGI:")) {
				return new MgiGeneID(idStr);
			}
			if (idStr.startsWith("RGD:")) {
				return new RgdID(StringUtil.removePrefix(idStr, "RGD:"));
			}
			if (idStr.startsWith("HGNC:")) {
				return new HgncID(idStr);
			}
			if (idStr.startsWith("UniProtKB:")) {
				return new UniProtID(StringUtil.removePrefix(idStr, "UniProtKB:"));
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, null, e.getMessage());
		}
		return new UnknownDataSourceIdentifier(idStr);
	}
}
