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
package edu.ucdenver.ccp.fileparsers.transfac;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;

/**
 * This class is used to parse the Transfac matrix.dat file, available only via registration (and
 * potential payment) with www.gene-regulation.com
 * 
 * @author Bill Baumgartner
 * 
 */
public class TransfacMatrixDatFileParser extends MultiLineFileRecordReader<TransfacMatrixDatFileData> {

	public static final String MATRIX_DAT_FILE_NAME = "matrix.dat";
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TransfacMatrixDatFileParser.class);

	public TransfacMatrixDatFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	/**
	 * During the initialization we want to skip the header, determine the character offsets for the
	 * column and load the first record.
	 */
	protected void initialize() throws IOException {
		/* Read until line starts with AC */
		line = readLine();
		while (!line.getText().startsWith("AC"))
			line = readLine();
		
		super.initialize();
	}

	@Override
	protected MultiLineBuffer compileMultiLineBuffer() throws IOException {
		if (line == null)
			return null;
		MultiLineBuffer transfacDataRecordBuffer = new MultiLineBuffer();
		do {
			transfacDataRecordBuffer.add(line);
			line = readLine();
		} while (line != null && !line.getText().startsWith("//"));
		line = readLine();
		
		return transfacDataRecordBuffer;
	}

	@Override
	protected TransfacMatrixDatFileData parseRecordFromMultipleLines(MultiLineBuffer transfacDataRecordBuffer) {
		return TransfacMatrixDatFileData.parseTransfacMatrixDataRecord(transfacDataRecordBuffer);
	}

}
