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
package edu.ucdenver.ccp.fileparsers.hprd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.hprd.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

/**
 * This class is used to parse the InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class HprdIdMappingsTxtFileParser extends SingleLineFileRecordReader<HprdIdMappingsTxtFileData> {

	public static final String HPRD_ID_MAPPINGS_TXT_FILE_NAME = "HPRD_ID_MAPPINGS.txt";

	public HprdIdMappingsTxtFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	@Override
	protected HprdIdMappingsTxtFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		if (toks.length == 8) {
			HprdID hprdID = new HprdID(toks[0]);
			String geneSymbol = null;
			if (!toks[1].equals("-")) {
				geneSymbol = toks[1];
			}
			DataSourceIdentifier<?> nucleotideAccession = resolveAccession(toks[2]);
			DataSourceIdentifier<?> proteinAccession = resolveAccession(toks[3]);
			EntrezGeneID entrezGeneID = null;
			if (!toks[4].equals("-")) {
				entrezGeneID = new EntrezGeneID(toks[4]);
			}
			OmimID omimID = null;
			if (!toks[5].equals("-") && !toks[5].equals("0")) {
				omimID = new OmimID(toks[5]);
			}
			List<UniProtID> swissProtIDs = new ArrayList<UniProtID>();
			if (!toks[6].equals("-")) {
				for (String spIDStr : toks[6].split(",")) {
					swissProtIDs.add(new UniProtID(spIDStr));
				}
			}
			String mainName = toks[7];
			return new HprdIdMappingsTxtFileData(hprdID, geneSymbol, nucleotideAccession, proteinAccession,
					entrezGeneID, omimID, swissProtIDs, mainName, line.getByteOffset(), line.getLineNumber());
		}
		throw new IllegalArgumentException("Unexpected number of tokens (" + toks.length + ") on line: " + line);
	}

	private DataSourceIdentifier<?> resolveAccession(String acc) {
		try {
			return NucleotideAccessionResolver.resolveNucleotideAccession(acc);
		} catch (IllegalArgumentException e) {
			return ProteinAccessionResolver.resolveProteinAccession(acc);
		}
	}

}
