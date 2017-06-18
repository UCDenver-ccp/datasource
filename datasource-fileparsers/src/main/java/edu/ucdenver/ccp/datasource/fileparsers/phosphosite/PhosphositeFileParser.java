package edu.ucdenver.ccp.datasource.fileparsers.phosphosite;

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
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;



/**
 * This class represents data available in Phosphosite Database
 * 
 * header = PROTEIN, ACC_ID, GENE, HU_CHR_LOC, MOD_TYPE, MOD_RSD, SITE_GRP_ID, ORGANISM, MW_kD, 
 * DOMAIN, SITE_+/-7_AA, LT_LIT, MS_LIT, MS_CST, CST_CAT# (tab delimited)
 * 
 * @author Heather Underwood
 * 
 */
public class PhosphositeFileParser extends SingleLineFileRecordReader<PhosphositeFileData> {
	private static Logger logger = Logger.getLogger(PhosphositeFileParser.class);

	private final static String HEADER = "PROTEIN\tACC_ID\tGENE\tHU_CHR_LOC\tMOD_TYPE\tMOD_RSD\tSITE_GRP_ID\tORGANISM\tMW_kD\tDOMAIN\tSITE_+/-7_AA\tLT_LIT\tMS_LIT\tMS_CST\tCST_CAT#";

	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	
	public PhosphositeFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public PhosphositeFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

//	@Override
//	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
//			throws IOException {
//		return new StreamLineReader(new GZIPInputStream(new FileInputStream(acetylationFile)), encoding, skipLinePrefix);
//	}

	@Override
	protected String getFileHeader() throws IOException {
		//File number
		readLine();
		//File description
		readLine();
		//Blank line
		readLine();
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected PhosphositeFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t",-1);
		
		String proteinName = toks[0];
		UniProtID phosphositeAccessionId = new UniProtID(toks[1]);
		String gene = toks[2];
		
		String chromosomeLocation = null;
		if (!toks[3].isEmpty()) {
			chromosomeLocation = toks[3];
		}
		
		String modificationType = toks[4];
		String modifiedResidue = toks[5];
		Integer siteGroupId = Integer.parseInt(toks[6]);
		String organism = toks[7];
		Double molWeight = Double.parseDouble(toks[8]);
		
		//Will return an empty set if domain is blank
		Set<String> domain = getDomain(toks[9]);
		String sequence = toks[10];
		
		Integer LTLitRefs = null;
		if (!toks[11].isEmpty()) {
			LTLitRefs = Integer.parseInt(toks[11]);
		}
				
		Integer MSLitRefs = null;
		if (!toks[12].isEmpty()) {
			MSLitRefs = Integer.parseInt(toks[12]);
		}
		
		Integer MSCSTRefs = null;
		if (!toks[13].isEmpty()) {
			MSCSTRefs = Integer.parseInt(toks[13]);
		}
		
		//Will return an empty set if catalog num is blank
		Set<String> CSTCatalogNum = getCSTCatalogNum(toks[13]);

		return new PhosphositeFileData(proteinName, phosphositeAccessionId, gene, chromosomeLocation, modificationType, modifiedResidue,
				siteGroupId, organism, molWeight, sequence, LTLitRefs, MSLitRefs, MSCSTRefs,
				CSTCatalogNum, domain, line.getByteOffset(),line.getLineNumber());
	}

	/**
	 * @param string
	 * @return
	 */
	private Set<String> getDomain(String str) {
		Set<String> domain = new HashSet<String>();
		if (!str.isEmpty()) {
			for (String temp : str.split(";")) {
				domain.add(new String(temp));
			}
		}
		return domain;
	}
	
	private Set<String> getCSTCatalogNum(String str) {
		Set<String> catnum = new HashSet<String>();
		if(!str.isEmpty()) {
			for(String temp: str.split(";")) {
				catnum.add(new String(temp));
			}
		}
		return catnum;
	}
}