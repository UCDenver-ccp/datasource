package edu.ucdenver.ccp.datasource.fileparsers.transfac;

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
