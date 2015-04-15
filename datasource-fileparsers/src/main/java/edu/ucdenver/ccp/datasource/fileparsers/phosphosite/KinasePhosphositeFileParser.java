/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.phosphosite;

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
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.fileparsers.download.HttpURL;



/**
 * This class represents data available in the Kinase dataset files available here:
 * http://www.phosphosite.org/downloads/Kinase_Substrate_Dataset.gz
 * 
 * header = KINASE,	KIN_ACC_ID,	GENE, HU_CHR_LOC, KIN_ORGANISM, SUBSTRATE, SUB_GENE_ID, SUB_ACC_ID, SUB_GENE,
 * SUB_ORGANISM, SUB_MOD_RSD, SITE_GRP_ID, SITE_+/-7_AA, IN_VIVO_RXN, IN_VITRO_RXN, CST_CAT# (tab delimited)
 * 
 * @author Heather Underwood
 * 
 */
public class KinasePhosphositeFileParser extends SingleLineFileRecordReader<KinasePhosphositeFileData> {
	private static Logger logger = Logger.getLogger(KinasePhosphositeFileParser.class);

	private final static String HEADER = "KINASE\tKIN_ACC_ID\tGENE\tHU_CHR_LOC\tKIN_ORGANISM\tSUBSTRATE\tSUB_GENE_ID\tSUB_ACC_ID\tSUB_GENE\tSUB_ORGANISM\tSUB_MOD_RSD\tSITE_GRP_ID\tSITE_+/-7_AA\tIN_VIVO_RXN\tIN_VITRO_RXN\tCST_CAT#";

	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	
	@HttpDownload(url = HttpURL.PHOSPHOSITE_KINASE)
	private File kinaseFile;
	
	public KinasePhosphositeFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public KinasePhosphositeFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(kinaseFile)), encoding, skipLinePrefix);
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
	protected KinasePhosphositeFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t",-1);
		
		String kinase = toks[0];
		UniProtID accessionId = new UniProtID(toks[1]);
		String gene = toks[2];
		String chromosomeLocation = null;
		if (!toks[3].isEmpty()) {
			chromosomeLocation = toks[3];
		}

		String organism = toks[4];
		String substrate = toks[5];
		Integer sub_geneId = Integer.parseInt(toks[6]);
		UniProtID sub_accessionId = new UniProtID(toks[7]);
		String sub_gene = toks[8];
		String sub_organism = toks[9];
		String sub_modifiedResidue = toks[10];
		Integer siteGroupId = Integer.parseInt(toks[11]);
		String sequence = toks[12];
		
		Boolean in_vivo_rxn = true;
		if (toks[13] == null) {
			in_vivo_rxn = false;
		}
		
		Boolean in_vitro_rxn = true;
		if (toks[14] == null) {
			in_vitro_rxn = false;
		}
		
		//Will return an empty set if catalog num is blank
		Set<String> CSTCatalogNum = getCSTCatalogNum(toks[15]);

		return new KinasePhosphositeFileData(kinase,accessionId, gene, chromosomeLocation,
				organism, substrate, sub_geneId, sub_accessionId, sub_gene, sub_organism, sub_modifiedResidue, siteGroupId, 
				sequence, in_vivo_rxn, in_vitro_rxn, CSTCatalogNum, line.getByteOffset(),line.getLineNumber());
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