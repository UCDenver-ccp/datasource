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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.download.HttpURL;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;



/**
 * This class represents data available in the Disease dataset files available here:
 * http://www.phosphosite.org/downloads/Disease-associated_sites.gz
 * 
 * header = DISEASE, ALTERATION, PROTEIN, ACC_ID, GENE_ID, GENE, HU_CHR_LOC, MW_kD, ORGANISM, SITE_GRP_ID, MOD_RSD, 
 * DOMAIN, SITE_+/-7_AA, PMIDS, LT_LIT, MS_LIT, MS_CST, CST_CAT# (tab delimited)
 * 
 * @author Heather Underwood
 * 
 */
public class DiseasePhosphositeFileParser extends SingleLineFileRecordReader<DiseasePhosphositeFileData> {
	private static Logger logger = Logger.getLogger(DiseasePhosphositeFileParser.class);

	private final static String HEADER = "DISEASE\tALTERATION\tPROTEIN\tACC_ID\tGENE_ID\tGENE\tHU_CHR_LOC\tMW_kD\tORGANISM\tSITE_GRP_ID\tMOD_RSD\tDOMAIN\tSITE_+/-7_AA\tPMIDs\tLT_LIT\tMS_LIT\tMS_CST\tCST_CAT#";

	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	
	@HttpDownload(url = HttpURL.PHOSPHOSITE_DISEASE)
	private File diseaseFile;
	
	public DiseasePhosphositeFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public DiseasePhosphositeFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(diseaseFile)), encoding, skipLinePrefix);
	}

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
	protected DiseasePhosphositeFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t",-1);
		
		String disease = toks[0];
		String alteration = toks[1];
		String proteinName = toks[3];
		UniProtID accessionId = new UniProtID(toks[4]);
		String geneId = toks[5];
		String gene = toks[6];
		
		String chromosomeLocation = null;
		if (!toks[7].isEmpty()) {
			chromosomeLocation = toks[7];
		}
		Double molWeight = Double.parseDouble(toks[8]);
		String organism = toks[9];
		Integer siteGroupId = Integer.parseInt(toks[10]);
		String modifiedResidue = toks[11];
		
		//Will return an empty set if domain is blank
		Set<String> domain = getDomain(toks[12]);
		String sequence = toks[13];
		Integer pmids = Integer.parseInt(toks[14]);
		
		Integer LTLitRefs = null;
		if (!toks[15].isEmpty()) {
			LTLitRefs = Integer.parseInt(toks[15]);
		}
				
		Integer MSLitRefs = null;
		if (!toks[16].isEmpty()) {
			MSLitRefs = Integer.parseInt(toks[16]);
		}
		
		Integer MSCSTRefs = null;
		if (!toks[17].isEmpty()) {
			MSCSTRefs = Integer.parseInt(toks[17]);
		}
		
		//Will return an empty set if catalog num is blank
		Set<String> CSTCatalogNum = getCSTCatalogNum(toks[18]);

		return new DiseasePhosphositeFileData(disease, alteration, proteinName, accessionId, geneId, gene, chromosomeLocation,
				molWeight, organism, siteGroupId, modifiedResidue, domain, sequence, pmids, LTLitRefs, MSLitRefs, MSCSTRefs,
				CSTCatalogNum, line.getByteOffset(),line.getLineNumber());
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