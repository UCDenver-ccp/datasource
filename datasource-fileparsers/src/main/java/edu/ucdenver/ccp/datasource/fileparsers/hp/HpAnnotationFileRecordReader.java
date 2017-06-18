package edu.ucdenver.ccp.datasource.fileparsers.hp;

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

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HumanPhenotypeID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;

public class HpAnnotationFileRecordReader extends SingleLineFileRecordReader<HpAnnotationFileRecord> {

	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	public static final String COMMENT_PREFIX = null;

	/**
	 * Constructor.
	 * 
	 * @param dataFile
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public HpAnnotationFileRecordReader(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, COMMENT_PREFIX);
	}

	public HpAnnotationFileRecordReader(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, COMMENT_PREFIX, null, null, clean);
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return "#Format: entrez-gene-id<tab>entrez-gene-symbol<tab>HPO-Term-Name<tab>HPO-Term-ID";
	}

	
	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.fileparsers.SingleLineFileRecordReader#parseRecordFromLine
	 * (edu.ucdenver. ccp.common.file.reader.LineReader.Line)
	 */
	@Override
	protected HpAnnotationFileRecord parseRecordFromLine(Line line) {
		String text = line.getText();
		String[] toks = text.split("\\t", -1);

		NcbiGeneId egId = new NcbiGeneId(toks[0]);
		String geneSymbol = toks[1];
		String hpoTermName = toks[2];
		HumanPhenotypeID hpoTermId = new HumanPhenotypeID(toks[3]);

		HpAnnotationFileRecord r = new HpAnnotationFileRecord(egId, geneSymbol, hpoTermName, hpoTermId,
				line.getByteOffset(), line.getLineNumber());

		return r;
	}
}
