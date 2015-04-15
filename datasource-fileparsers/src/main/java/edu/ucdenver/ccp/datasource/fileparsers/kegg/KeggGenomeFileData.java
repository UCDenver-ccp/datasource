package edu.ucdenver.ccp.datasource.fileparsers.kegg;

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

	private final String keggSpeciesCode;

	private final NcbiTaxonomyID ncbiTaxonomyID;

	public KeggGenomeFileData(String threeLetterCode, NcbiTaxonomyID ncbiTaxonomyID, long byteOffset) {
		super(byteOffset);
		this.keggSpeciesCode = threeLetterCode;
		this.ncbiTaxonomyID = ncbiTaxonomyID;
	}

	public String getThreeLetterCode() {
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
			String entry = null;
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
	private static String getEntryFromLine(String line) {
		line = line.replaceFirst(NAME, "").trim();
		// checkEntryForThreeCharacters(line.trim());
		return new String(line.split(",")[0]);
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
