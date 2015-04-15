package edu.ucdenver.ccp.datasource.fileparsers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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
import java.io.InputStream;
import java.util.NoSuchElementException;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;

public abstract class SingleLineFileRecordReader<T extends SingleLineFileRecord> extends LineFileRecordReader<T> {

	public SingleLineFileRecordReader(InputStream stream, CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		super(stream, encoding, skipLinePrefix);
	}

	public SingleLineFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		super(dataFile, encoding, skipLinePrefix);
	}
	
	
	public SingleLineFileRecordReader(File dataFile, CharacterEncoding encoding)
			throws IOException {
		super(dataFile, encoding, null);
	}

	public SingleLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			String ftpUsername, String ftpPassword, boolean clean) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
	}

	public SingleLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			 boolean clean) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, null, null, clean);
	}
	
	/**
	 * Overriding to determine initial status of {@link #hasNext()}
	 */
	@Override
	protected void initialize() throws IOException {
		super.initialize();
		line = readLine();
	}

	@Override
	public boolean hasNext() {
		return line != null;
	}

	protected void setNextLine(Line line) {
		this.line = line;
	}

	
	
	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		T recordToReturn = parseRecordFromLine(line);

		try {
			line = readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return recordToReturn;
	}

	protected abstract T parseRecordFromLine(Line line);
}
