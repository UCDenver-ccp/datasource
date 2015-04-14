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
package edu.ucdenver.ccp.fileparsers.pro;

import java.io.File;
import java.io.IOException;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.obo.ProteinOntologyId;

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
	 * edu.ucdenver.ccp.fileparsers.SingleLineFileRecordReader#parseRecordFromLine(edu.ucdenver.
	 * ccp.common.file.reader.LineReader.Line)
	 */
	@Override
	protected ProMappingRecord parseRecordFromLine(Line line) {
		String text = line.getText();
		ProMappingRecord r = null;

		if (text.startsWith("PR:")) {

			String[] tokens = text.split("\t");
			ProteinOntologyId fromId = (ProteinOntologyId) DataSourceIdResolver.resolveId(tokens[0].trim());
			if (tokens[1].trim().startsWith("UniProtKB_VAR"))
				return null;

			DataSourceIdentifier targetId = DataSourceIdResolver.resolveId(tokens[1].trim());
			String mappingType = tokens[2].trim();

			r = new ProMappingRecord(fromId, targetId, mappingType, line.getByteOffset(), line.getLineNumber());
		}

		return r;
	}
}
