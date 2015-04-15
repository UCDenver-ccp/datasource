package edu.ucdenver.ccp.datasource.fileparsers.snomed;

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
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

public class SnomedRf2ConceptFileRecordReader extends SingleLineFileRecordReader<SnomedRf2ConceptFileRecord> {

	private static final Logger logger = Logger.getLogger(SnomedRf2ConceptFileRecordReader.class);
	private static final String HEADER = "id\teffectiveTime\tactive\tmoduleId\tdefinitionStatusId";

	public static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;
	public static final String SKIP_LINE_PREFIX = null;

	public SnomedRf2ConceptFileRecordReader(File file) throws IOException {
		super(file, ENCODING, SKIP_LINE_PREFIX);
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected SnomedRf2ConceptFileRecord parseRecordFromLine(Line line) {
		return parseSnomedRf2ConceptFileLine(line);
	}

	/**
	 * @param line
	 * @return
	 */
	private SnomedRf2ConceptFileRecord parseSnomedRf2ConceptFileLine(Line line) {
		int index = 0;
		String[] columns = line.getText().split(RegExPatterns.TAB, -1);
		if (columns.length != 5) {
			logger.warn("Unexpected number of columns observed. Expected 5 but was " + columns.length + " on line: "
					+ line.getText());
			return null;
		}

		String conceptId = columns[index++];
		String effectiveTime = columns[index++];
		boolean isActive = (columns[index++].equals("1")) ? true : false;
		String moduleId = columns[index++];
		String definitionStatusId = columns[index++];

		return new SnomedRf2ConceptFileRecord(conceptId, effectiveTime, isActive, moduleId, definitionStatusId,
				line.getByteOffset(), line.getLineNumber());

	}

}
