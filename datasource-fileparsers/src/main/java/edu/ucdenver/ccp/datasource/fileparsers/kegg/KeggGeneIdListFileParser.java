
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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggGeneID;

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
