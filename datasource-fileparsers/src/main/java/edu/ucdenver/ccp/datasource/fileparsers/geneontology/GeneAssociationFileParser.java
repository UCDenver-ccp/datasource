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
package edu.ucdenver.ccp.datasource.fileparsers.geneontology;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;

/**
 * This class is used to parse Gene Ontology gene-association.xxxx files
 * 
 * @author Bill Baumgartner
 * 
 */
public class GeneAssociationFileParser extends SingleLineFileRecordReader<GeneAssociationFileData> {

	private final static String COMMENT_INDICATOR = StringConstants.EXCLAMATION_MARK;

	private static final String GENE_ASSOCIATION_FILE_PREFIX = "gene_association.";

	private final String speciesKey;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(GeneAssociationFileParser.class);

	public GeneAssociationFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, COMMENT_INDICATOR);
		this.speciesKey = extractSpeciesKey(file.getName());
	}

	private String extractSpeciesKey(String fileName) {
		if (!fileName.startsWith(GENE_ASSOCIATION_FILE_PREFIX))
			throw new RuntimeException(String.format("Expected file name to start with %s but instead observed: ",
					GENE_ASSOCIATION_FILE_PREFIX));
		return fileName.substring(fileName.lastIndexOf("."));
	}

	@Override
	public String getDataSpecificKey() {
		return speciesKey;
	}

	@Override
	protected GeneAssociationFileData parseRecordFromLine(Line line) {
		return GeneAssociationFileData.parseLineFromFile(line);
	}

	/**
	 * Returns a mapping from GO Term to the Mgi IDs that have been annotated to it
	 * 
	 * @param geneAssociationFile
	 * @return
	 */
	public static Map<GeneOntologyID, Set<DataSourceIdentifier<?>>> getGoTermID2GeneIDsMap(File geneAssociationFile,
			CharacterEncoding encoding, int maxGoTerm2GeneIDLinkThreshold) {
		Map<GeneOntologyID, Set<DataSourceIdentifier<?>>> goTermID2GeneIDsMap = new HashMap<GeneOntologyID, Set<DataSourceIdentifier<?>>>();
		GeneAssociationFileParser parser = null;

		try {
			parser = new GeneAssociationFileParser(geneAssociationFile, encoding);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe);
		}

		while (parser.hasNext()) {
			GeneAssociationFileData dataRecord = parser.next();
			GeneOntologyID goTermID = dataRecord.getGoTermID();
			DataSourceIdentifier<?> geneID = dataRecord.getGeneID();
			if (goTermID2GeneIDsMap.containsKey(goTermID)) {
				goTermID2GeneIDsMap.get(goTermID).add(geneID);
			} else {
				Set<DataSourceIdentifier<?>> geneIDs = new HashSet<DataSourceIdentifier<?>>();
				geneIDs.add(geneID);
				goTermID2GeneIDsMap.put(goTermID, geneIDs);
			}
		}

		/*
		 * filter out any terms that have greater than maxGoTerm2GeneIDLinkThreshold genes linked to
		 * them
		 */
		Set<GeneOntologyID> goTermIDs = goTermID2GeneIDsMap.keySet();
		Set<GeneOntologyID> goTermIDsToRemove = new HashSet<GeneOntologyID>();
		for (GeneOntologyID goTermID : goTermIDs) {
			Set<DataSourceIdentifier<?>> linkedGeneIDs = goTermID2GeneIDsMap.get(goTermID);
			if (linkedGeneIDs.size() > maxGoTerm2GeneIDLinkThreshold) {
				goTermIDsToRemove.add(goTermID);
			}
		}
		for (GeneOntologyID goTermID : goTermIDsToRemove) {
			goTermID2GeneIDsMap.remove(goTermID);
		}

		return goTermID2GeneIDsMap;
	}

}
