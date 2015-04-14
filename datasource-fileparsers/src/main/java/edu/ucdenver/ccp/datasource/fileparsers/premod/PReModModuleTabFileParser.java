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
package edu.ucdenver.ccp.fileparsers.premod;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

/**
 * This class is used to parse the PreMod mouse_module_tab.txt file
 * 
 * @author Bill Baumgartner
 * 
 */
public class PReModModuleTabFileParser extends SingleLineFileRecordReader<PReModModuleTabFileData> {
	
	private static final String HEADER = "Name\tChromosome\tLength\tScore\tUpstream Entrez Gene Id\tUpstream Gene Name\tUpstream Gene Position\tDownstream Entrez Gene Id\tDownstream Gene Name\tDownstream Gene Position\tTag Matrices";
	
	
	private static Logger logger = Logger.getLogger(PReModModuleTabFileParser.class);

	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	
	public PReModModuleTabFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	protected PReModModuleTabFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}
	
//	/**
//	 * During the initialization we want to skip the header, determine the character offsets for the
//	 * column and load the first record.
//	 */
//	protected void initialize() throws IOException {
//		/* skip the first line, it is a listing of column headings */
//		Line line = readLine();
//		if (line.getText().startsWith("mod")) {
//			String errorMessage = "Expected header line, but observed: " + line.toString();
//			logger.error(errorMessage);
//			throw new IOException(errorMessage);
//		}
//		super.initialize();
//	}
	
	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}
	
	@Override
	protected PReModModuleTabFileData parseRecordFromLine(Line line) {
		return PReModModuleTabFileData.parseModuleTabLine(line);
	}

}
