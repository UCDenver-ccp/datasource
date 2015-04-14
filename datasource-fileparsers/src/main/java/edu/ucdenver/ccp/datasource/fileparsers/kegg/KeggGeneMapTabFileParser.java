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

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;

/**
 * This class is used to parse the Kegg aaa_gene_map.tab file format.
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGeneMapTabFileParser extends SingleLineFileRecordReader<KeggGeneMapTabFileData> {

	/**
	 * The species is inferred from the input file name. Assuming the file came directly from the
	 * Kegg FTP site this is a safe assumption.
	 */
	private final String speciesKey;
	
	public KeggGeneMapTabFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
		this.speciesKey = extractSpeciesKey(file.getName());
	}

	private String extractSpeciesKey(String fileName) {
		return fileName.substring(0, fileName.indexOf(StringConstants.UNDERSCORE));
	}

	@Override
	public String getDataSpecificKey() {
		return speciesKey;
	}
	
	@Override
	protected KeggGeneMapTabFileData parseRecordFromLine(Line line) {
		return KeggGeneMapTabFileData.parseKeggGeneMapTabLine(line);
	}

	/**
	 * Create a mapping between the Gene ID and the KEGG Pathway ID
	 * 
	 * @param keggGeneMapTabFile
	 */
	public static Map<EntrezGeneID, Set<KeggPathwayID>> getGeneID2KeggPathwayIDMap(File keggGeneMapTabFile,
			CharacterEncoding encoding) throws IOException {
		Map<EntrezGeneID, Set<KeggPathwayID>> geneID2KeggPathwayIDMap = new HashMap<EntrezGeneID, Set<KeggPathwayID>>();

		KeggGeneMapTabFileParser parser = null;
		try {
			parser = new KeggGeneMapTabFileParser(keggGeneMapTabFile, encoding);
			while (parser.hasNext()) {
				KeggGeneMapTabFileData dataRecord = parser.next();
				EntrezGeneID geneID = dataRecord.getGeneID();
				Set<KeggPathwayID> keggPathwayIDs = dataRecord.getKeggPathwayIDs();
				if (geneID2KeggPathwayIDMap.containsKey(geneID)) {
					geneID2KeggPathwayIDMap.get(geneID).addAll(keggPathwayIDs);
				} else {
					geneID2KeggPathwayIDMap.put(geneID, new HashSet<KeggPathwayID>(keggPathwayIDs));
				}
			}
			return geneID2KeggPathwayIDMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

}
