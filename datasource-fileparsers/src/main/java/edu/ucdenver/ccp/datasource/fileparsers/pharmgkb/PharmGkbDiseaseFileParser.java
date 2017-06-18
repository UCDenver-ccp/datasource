package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

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

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

public class PharmGkbDiseaseFileParser extends SingleLineFileRecordReader<PharmGkbDiseaseFileRecord> {

	private static final String HEADER = "PharmGKB Accession Id\tName\tAlternate Names\tCross-references\tExternal Vocabulary";

	public static final String PHARMGKB_DISEASE_RECORD_ID_PREFIX = "PHARMGKB_DISEASE_RECORD_";

	private static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	@HttpDownload(url = "https://api.pharmgkb.org/v1/download/file/data/phenotypes.zip", fileName = "phenotypes.zip", targetFileName = "phenotypes.tsv", decompress=true)
	private File pharmGkbDiseasesFile;

	public PharmGkbDiseaseFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	public PharmGkbDiseaseFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(pharmGkbDiseasesFile, encoding, skipLinePrefix);
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
	protected PharmGkbDiseaseFileRecord parseRecordFromLine(Line line) {
		String[] toks = line.getText().split(RegExPatterns.TAB);
		String pharmGkbAccessionId = toks[0];
		String name = toks[1];
		String alternativeNames = toks.length > 2 ? toks[2] : "";
		String crossReferences = toks.length > 3 ? toks[3] : "";
		String externalVocabulary = toks.length > 4 ? toks[4] : "";
		return new PharmGkbDiseaseFileRecord(pharmGkbAccessionId, name, alternativeNames, crossReferences, externalVocabulary,
				line.getByteOffset(), line.getLineNumber());
	}
	
}
