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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse the Kegg genome file
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGenomeFileParser extends MultiLineFileRecordReader<KeggGenomeFileData> {

	public static final String FTP_FILE_NAME = "genome";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.KEGG_GENENOME_HOST, path = FtpHost.KEGG_GENENOME_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File keggGenomeFile;

	public KeggGenomeFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public KeggGenomeFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(keggGenomeFile, encoding, skipLinePrefix);
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
	 * Returns a mapping from NCBI taxonomy ID to the Kegg three-letter internal species code
	 * 
	 * @param genomeFile
	 * @return
	 */
	public static Map<NcbiTaxonomyID, Set<KeggSpeciesCode>> getTaxonomyID2KeggSpeciesCodeMap(File genomeFile,
			CharacterEncoding encoding) throws IOException {
		Map<NcbiTaxonomyID, Set<KeggSpeciesCode>> ncbiTaxonomyID2KeggThreeLetterCodeMap = new HashMap<NcbiTaxonomyID, Set<KeggSpeciesCode>>();

		KeggGenomeFileParser parser = null;
		try {
			parser = new KeggGenomeFileParser(genomeFile, encoding);
			while (parser.hasNext()) {
				KeggGenomeFileData dataRecord = parser.next();
				NcbiTaxonomyID ncbiTaxonomyID = dataRecord.getNcbiTaxonomyID();
				KeggSpeciesCode threeLetterCode = dataRecord.getThreeLetterCode();

				if (!ncbiTaxonomyID2KeggThreeLetterCodeMap.containsKey(ncbiTaxonomyID)) {
					Set<KeggSpeciesCode> speciesCodes = new HashSet<KeggSpeciesCode>();
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
