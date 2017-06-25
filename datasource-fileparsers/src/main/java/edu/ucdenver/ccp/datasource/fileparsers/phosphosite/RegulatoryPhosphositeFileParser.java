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
 * This class represents data available in the Regulatory Phosphosite Dataset files available here:
 * http://www.phosphosite.org/downloads/Regulatory_sites.gz
 * 
 * header = PROTEIN	PROT_TYPE	ACC_ID	GENE_ID	GENE	HU_CHR_LOC	ORGANISM	MOD_RSD	SITE_GRP_ID	SITE_+/-7_AA	
 * DOMAIN	ON_FUNCTION	ON_PROCESS	ON_PROT_INTERACT	ON_OTHER_INTERACT	PMIDs	LT_LIT	MS_LIT	MS_CST	NOTES (tab delimited)
 * 
 * @author Heather Underwood
 * 
 */
public class RegulatoryPhosphositeFileParser extends SingleLineFileRecordReader<RegulatoryPhosphositeFileData> {
	private static Logger logger = Logger.getLogger(RegulatoryPhosphositeFileParser.class);

	private final static String HEADER = "PROTEIN\tPROT_TYPE\tACC_ID\tGENE_ID\tGENE\tHU_CHR_LOC\tORGANISM\tMOD_RSD\tSITE_GRP_ID\tSITE_+/-7_AA\tDOMAIN\tON_FUNCTION\tON_PROCESS\tON_PROT_INTERACT\tON_OTHER_INTERACT\tPMIDs\tLT_LIT\tMS_LIT\tMS_CST\tNOTES";

	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	
	@HttpDownload(url = HttpURL.PHOSPHOSITE_REGULATORY)
	private File regulatoryFile;
	
	public RegulatoryPhosphositeFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public RegulatoryPhosphositeFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(regulatoryFile)), encoding, skipLinePrefix);
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
	protected RegulatoryPhosphositeFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t",-1);
		
		String proteinName = toks[0];
		String proteinType = toks[1];
		UniProtID accessionId = new UniProtID(toks[2]);
		String geneId = toks[3];
		String gene = toks[4];
		
		String chromosomeLocation = null;
		if (!toks[5].isEmpty()) {
			chromosomeLocation = toks[5];
		}
		
		String organism = toks[6];
		String modifiedResidue = toks[7];
		Integer siteGroupId = Integer.parseInt(toks[8]);
		String sequence = toks[9];
		//Will return an empty set if domain is blank
		Set<String> domain = getDomain(toks[10]);
		Set<String> on_function = getOnFunction(toks[11]);
		Set<String> on_process = getOnProcess(toks[12]);
		Set<String> on_prot_interact = getOnProtInteract(toks[13]);
		Set<String> on_other_interact = getOnOtherInteract(toks[14]);
				
		Integer pmids = Integer.parseInt(toks[15]);
		
		Integer LTLitRefs = null;
		if (!toks[15].isEmpty()) {
			LTLitRefs = Integer.parseInt(toks[16]);
		}
				
		Integer MSLitRefs = null;
		if (!toks[16].isEmpty()) {
			MSLitRefs = Integer.parseInt(toks[17]);
		}
		
		Integer MSCSTRefs = null;
		if (!toks[17].isEmpty()) {
			MSCSTRefs = Integer.parseInt(toks[18]);
		}
	
		String notes = toks[19];

		return new RegulatoryPhosphositeFileData(proteinName, proteinType, accessionId, geneId, gene, chromosomeLocation,
			 organism, modifiedResidue, siteGroupId, sequence, domain, on_function, on_process, on_prot_interact,
			 on_other_interact, pmids, LTLitRefs, MSLitRefs, MSCSTRefs, notes, line.getByteOffset(),line.getLineNumber());
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
	
	/**
	 * @param string
	 * @return
	 */
	private Set<String> getOnFunction(String str) {
		Set<String> on_function = new HashSet<String>();
		if (!str.isEmpty()) {
			for (String temp : str.split(";")) {
				on_function.add(new String(temp));
			}
		}
		return on_function;
	}
	
	
	/**
	 * @param string
	 * @return
	 */
	private Set<String> getOnProcess(String str) {
		Set<String> on_process = new HashSet<String>();
		if (!str.isEmpty()) {
			for (String temp : str.split(";")) {
				on_process.add(new String(temp));
			}
		}
		return on_process;
	}
	

	/**
	 * @param string
	 * @return
	 */
	private Set<String> getOnProtInteract(String str) {
		Set<String> on_prot_interact = new HashSet<String>();
		if (!str.isEmpty()) {
			for (String temp : str.split(";")) {
				on_prot_interact.add(new String(temp));
			}
		}
		return on_prot_interact;
	}
	

	/**
	 * @param string
	 * @return
	 */
	private Set<String> getOnOtherInteract(String str) {
		Set<String> on_other_interact = new HashSet<String>();
		if (!str.isEmpty()) {
			for (String temp : str.split(";")) {
				on_other_interact.add(new String(temp));
			}
		}
		return on_other_interact;
	}
}