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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * This class is used to parse the Kegg genome file
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGenomeFileParser extends MultiLineFileRecordReader<KeggGenomeFileData> {

	public KeggGenomeFileParser(File file, CharacterEncoding encoding) throws IOException {
		super((file.getName().endsWith(".gz") ? new GZIPInputStream(new FileInputStream(file)) : new FileInputStream(
				file)), encoding, null);
	}

	@Override
	protected void initialize() throws IOException {
		line = readLine();
		super.initialize();
	}

	@Override
	protected MultiLineBuffer compileMultiLineBuffer() throws IOException {
		if (line == null)
			return null;
		MultiLineBuffer multiLineBuffer = new MultiLineBuffer();
		do {
			multiLineBuffer.add(line);
			line = readLine();
		} while (line != null && !line.getText().startsWith("///"));
		line = readLine();
		return multiLineBuffer;
	}

	@Override
	protected KeggGenomeFileData parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer) {
		return KeggGenomeFileData.parseKeggGenomeFileRecord(multiLineBuffer);
	}

	/**
	 * Returns a mapping from NCBI taxonomy ID to the Kegg three-letter internal
	 * species code
	 * 
	 * @param genomeFile
	 * @return
	 */
	public static Map<NcbiTaxonomyID, Set<String>> getTaxonomyID2KeggSpeciesCodeMap(File genomeFile,
			CharacterEncoding encoding) throws IOException {
		Map<NcbiTaxonomyID, Set<String>> ncbiTaxonomyID2KeggThreeLetterCodeMap = new HashMap<NcbiTaxonomyID, Set<String>>();

		KeggGenomeFileParser parser = null;
		try {
			parser = new KeggGenomeFileParser(genomeFile, encoding);
			while (parser.hasNext()) {
				KeggGenomeFileData dataRecord = parser.next();
				NcbiTaxonomyID ncbiTaxonomyID = dataRecord.getNcbiTaxonomyID();
				String threeLetterCode = dataRecord.getKeggSpeciesCode();

				if (!ncbiTaxonomyID2KeggThreeLetterCodeMap.containsKey(ncbiTaxonomyID)) {
					Set<String> speciesCodes = new HashSet<String>();
					speciesCodes.add(threeLetterCode);
					ncbiTaxonomyID2KeggThreeLetterCodeMap.put(ncbiTaxonomyID, speciesCodes);
				} else {
					ncbiTaxonomyID2KeggThreeLetterCodeMap.get(ncbiTaxonomyID).add(threeLetterCode);
				}
			}
			return ncbiTaxonomyID2KeggThreeLetterCodeMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

}
