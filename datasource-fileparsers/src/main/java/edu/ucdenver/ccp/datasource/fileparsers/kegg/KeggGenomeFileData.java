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
package edu.ucdenver.ccp.fileparsers.kegg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader.MultiLineBuffer;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * Incomplete representation of data from KEGG genome file. Only the three-letter entry code and
 * NCBI taxonomy ID have been represented.
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGenomeFileData extends MultiLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_";

	private static final Logger logger = Logger.getLogger(KeggGenomeFileData.class);

	private static final String NAME = "NAME";
	private static final String ENTRY = "ENTRY";
	private static final String TAXONOMY = "TAXONOMY";

	private static final String GENOME_ID_T3 = "T3"; // meta genome identifier

	private final KeggSpeciesCode keggSpeciesCode;

	private final NcbiTaxonomyID ncbiTaxonomyID;

	public KeggGenomeFileData(KeggSpeciesCode threeLetterCode, NcbiTaxonomyID ncbiTaxonomyID, long byteOffset) {
		super(byteOffset);
		this.keggSpeciesCode = threeLetterCode;
		this.ncbiTaxonomyID = ncbiTaxonomyID;
	}

	public KeggSpeciesCode getThreeLetterCode() {
		return keggSpeciesCode;
	}

	public NcbiTaxonomyID getNcbiTaxonomyID() {
		return ncbiTaxonomyID;
	}

	public static KeggGenomeFileData parseKeggGenomeFileRecord(MultiLineBuffer multiLineBuffer) {
		// TODO: review how to handle multiple taxonomy ID values in Kegg entries classified as
		// 'Meta Genome'
		try {
			BufferedReader br = new BufferedReader(new StringReader(multiLineBuffer.toString()));
			String line;
			KeggSpeciesCode entry = null;
			NcbiTaxonomyID ncbiTaxonomyID = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(NAME)) {
					entry = getEntryFromLine(line);
				} else if (line.startsWith(TAXONOMY)) {
					ncbiTaxonomyID = getNcbiTaxonomyIDFromLine(line);
					break;
				} else if (line.startsWith(ENTRY)) {
					String testLine = line.replace(ENTRY, "").trim();
					if (testLine.startsWith(GENOME_ID_T3)) {
						logger.warn("Ignoring meta genome entry : " + multiLineBuffer.toString());
						return null;
					}
				}
			}
			return new KeggGenomeFileData(entry, ncbiTaxonomyID, multiLineBuffer.getByteOffset());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the three-letter entry code from an ENTRY line
	 * 
	 * @param line
	 * @return
	 */
	private static KeggSpeciesCode getEntryFromLine(String line) {
		line = line.replaceFirst(NAME, "").trim();
		// checkEntryForThreeCharacters(line.trim());
		return new KeggSpeciesCode(line.split(",")[0]);
	}

	/**
	 * Logs an error if the input String is not 3 characters long -- NOTE: some are 4 characters
	 * long
	 * 
	 * @param entryStr
	 */
	private static void checkEntryForThreeCharacters(String entryStr) {
		if (entryStr.length() != 3) {
			// logger.error("Invalid Entry String (should be three characters long): " + entryStr);
		}
	}

	/**
	 * Returns the NCBI taxonomy ID from a TAXONOMY line
	 * 
	 * @param line
	 * @return
	 */
	private static NcbiTaxonomyID getNcbiTaxonomyIDFromLine(String line) {
		line = line.replaceAll(TAXONOMY + "\\s+", "");
		String ids = line.substring(line.indexOf(":") + 1).trim();
		String firstId = ids.split(" ")[0];
		return new NcbiTaxonomyID(firstId);
	}

}
