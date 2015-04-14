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
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggGeneID;

/**
 * This class is used to parse the Kegg AAA_geneIDType.list files where AAA is a three-letter code
 * used by Kegg internally to identify species.
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGeneIdListFileParser extends SingleLineFileRecordReader<KeggGeneIdListFileData> {

	/**
	 * The species is inferred from the input file name. Assuming the file came directly from the
	 * Kegg FTP site this is a safe assumption.
	 */
	private final String speciesKey;

	public KeggGeneIdListFileParser(File file, CharacterEncoding encoding) throws IOException {
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
	protected KeggGeneIdListFileData parseRecordFromLine(Line line) {
		return KeggGeneIdListFileData.parseKeggGeneIDListLine(line);
	}

	/**
	 * Returns a mapping from the Kegg internal gene ID to an external gene ID as defined in the
	 * .list file.
	 * 
	 * @param geneIdListFile
	 * @return
	 */
	public static Map<KeggGeneID, Set<DataSourceIdentifier<?>>> getInternal2ExternalGeneIDMap(File geneIdListFile,
			CharacterEncoding encoding) throws IOException {
		Map<KeggGeneID, Set<DataSourceIdentifier<?>>> keggInternal2ExternalGeneIDMap = new HashMap<KeggGeneID, Set<DataSourceIdentifier<?>>>();

		KeggGeneIdListFileParser parser = null;
		try {
			parser = new KeggGeneIdListFileParser(geneIdListFile, encoding);
			while (parser.hasNext()) {
				KeggGeneIdListFileData dataRecord = parser.next();
				KeggGeneID keggInternalGeneID = dataRecord.getKeggGeneID();
				DataSourceIdentifier<?> externalGeneID = dataRecord.getExternalGeneID();

				if (!keggInternal2ExternalGeneIDMap.containsKey(keggInternalGeneID)) {
					Set<DataSourceIdentifier<?>> externalGeneIDs = new HashSet<DataSourceIdentifier<?>>();
					externalGeneIDs.add(externalGeneID);
					keggInternal2ExternalGeneIDMap.put(keggInternalGeneID, externalGeneIDs);
				} else {
					keggInternal2ExternalGeneIDMap.get(keggInternalGeneID).add(externalGeneID);
				}
			}
			return keggInternal2ExternalGeneIDMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

}
