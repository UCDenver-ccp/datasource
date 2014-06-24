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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringConstants;

public abstract class MultiLineFileRecordReader<T extends FileRecord> extends LineFileRecordReader<T> {

	protected Line line;
	protected MultiLineBuffer buffer;

	public MultiLineFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		super(dataFile, encoding, skipLinePrefix);
	}

	public MultiLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			String ftpUsername, String ftpPassword, boolean clean) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
	}

	public MultiLineFileRecordReader(InputStream stream, CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		super(stream, encoding, skipLinePrefix);
	}
	
	/**
	 * Overriding to determine initial status of {@link #hasNext()}
	 */
	@Override
	protected void initialize() throws IOException {
		super.initialize();
		buffer = compileMultiLineBuffer();
	}

	@Override
	public boolean hasNext() {
		return buffer != null;
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		T record = parseRecordFromMultipleLines(buffer);

		try {
			buffer = compileMultiLineBuffer();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return record;
	}

	protected abstract MultiLineBuffer compileMultiLineBuffer() throws IOException;

	protected abstract T parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer);

	public static class MultiLineBuffer {
		private List<Line> lines;

		public MultiLineBuffer() {
			lines = new ArrayList<Line>();
		}

		public void add(Line line) {
			lines.add(line);
		}

		public long getByteOffset() {
			if (lines.size() > 0)
				return lines.get(0).getByteOffset();
			throw new IllegalStateException("Cannot request byte offset from an empty MultiLineBuffer!");
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Line line : lines) {
				sb.append(line.getText() + StringConstants.NEW_LINE);
			}
			return sb.toString();
		}
	}

}
