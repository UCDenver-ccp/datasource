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
import java.util.ArrayList;
import java.util.Collection;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

public class PharmGkbDrugFileParser extends SingleLineFileRecordReader<PharmGkbDrugFileRecord> {

	private static final String HEADER = "PharmGKB Accession Id\tName\tGeneric Names\tTrade Names\tBrand Mixtures\tType\tCross-references\tSMILES\tInChI\tDosing Guideline\tExternal Vocabulary";

	private static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	@HttpDownload(url = "https://api.pharmgkb.org/v1/download/file/data/drugs.zip", fileName = "drugs.zip", targetFileName = "drugs.tsv", decompress = true)
	private File pharmGkbDrugsFile;

	public PharmGkbDrugFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	public PharmGkbDrugFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(pharmGkbDrugsFile, encoding, skipLinePrefix);
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
	protected PharmGkbDrugFileRecord parseRecordFromLine(Line line) {
		int index = 0;
		String[] toks = line.getText().split(RegExPatterns.TAB, -1);
		String pharmGkbAccessionId = toks[index++];
		String name = toks[index++];
		String genericNamesTok = toks[index++];
		String tradeNamesTok = toks[index++];
		String brandMixturesTok = toks[index++];
		String type = toks[index++];
		String crossReferencesTok = toks[index++];
		String smiles = toks[index++];
		String inChI = toks[index++];
		String dosingGuideline = toks[index++];
		String externalVocabulary = toks[index++];

		Collection<String> genericNames = StringUtil.delimitAndTrim(genericNamesTok, StringConstants.COMMA,
				StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);

		Collection<String> tradeNames = StringUtil.delimitAndTrim(tradeNamesTok, StringConstants.COMMA,
				StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);

		Collection<String> brandMixtures = StringUtil.delimitAndTrim(brandMixturesTok, StringConstants.COMMA,
				StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);

		Collection<DataSourceIdentifier<?>> crossReferences = new ArrayList<DataSourceIdentifier<?>>();
		String url = "";
		for (String idStr : StringUtil.delimitAndTrim(crossReferencesTok, StringConstants.COMMA,
				StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE)) {
			String databaseName = idStr.split(":")[0];
			String databaseIdentifierStr = idStr.substring(idStr.indexOf(':') + 1);
			if (databaseName.equalsIgnoreCase("url")) {
				url = databaseIdentifierStr;
			} else {
				crossReferences.add(DataSourceIdResolver.resolveId(databaseName, databaseIdentifierStr, idStr));
			}
		}

		// /*
		// * There are two lines in the drugs.tsv file that have a minor error that involves an
		// * inadvertent tab included as one of the alternative names. This results in the columns
		// to
		// * the right of "alternative names" being shifted by one. PharmGKB has been notified of
		// this
		// * error.
		// */
		// if (toks.length == 8) {
		// alternativeNames = alternativeNames + " " + toks[3];
		// type = toks[4];
		// drugBankId = toks[5];
		// smiles = toks[6];
		// meshIds = toks[7];
		// }

		return new PharmGkbDrugFileRecord(pharmGkbAccessionId, name, genericNames, tradeNames, brandMixtures, type,
				crossReferences, url, smiles, inChI, dosingGuideline, externalVocabulary, line.getByteOffset(),
				line.getLineNumber());
	}
	
}
