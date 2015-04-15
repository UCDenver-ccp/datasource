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
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader.MultiLineBuffer;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggGeneID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;

/**
 * Data structure used to represent KEGG genes file downloaded as part of genes.tar.gz - one of the
 * bulk download files from KEGG Incomplete implementation.
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGenesFileData extends MultiLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "KEGG_SPECIES_CODE_2_NCBI_TAXONOMYID_RECORD_";

	private static final Logger logger = Logger.getLogger(KeggGenesFileData.class);

	private static final String ENTRY = "ENTRY";
	private static final String NAME = "NAME";
	private static final String DEFINITION = "DEFINITION";
	private static final String POSITION = "POSITION";
	private static final String DBLINKS = "DBLINKS";
	private static final String AASEQ = "AASEQ";
	private static final String NTSEQ = "NTSEQ";
	private static final String ORTHOLOGY = "ORTHOLOGY";
	private static final String PATHWAY = "PATHWAY";
	private static final String CLASS = "CLASS";
	private static final String MOTIF = "MOTIF";
	private static final String STRUCTURE = "STRUCTURE";
	private static final String SLASHES = "///";

	private final KeggGeneID keggGeneID;
	// private final String geneType;
	// private final String speciesAbbrev;
	// private final Set<String> names;
	// private final String definition;
	// private final String position;
	private final Set<DataSourceIdentifier<?>> dbLinks;
	// private final int aaSeqLength;
	// private final String aaSeq;
	// private final int ntSeqLength;
	// private final String ntSeq;
	private final Set<KeggPathwayID> pathwayIds;

	public KeggGenesFileData(KeggGeneID keggGeneId, Set<DataSourceIdentifier<?>> dbIds, Set<KeggPathwayID> pathwayIds,
			long byteOffset) {
		super(byteOffset);
		this.keggGeneID = keggGeneId;
		this.dbLinks = dbIds;
		this.pathwayIds = pathwayIds;
	}

	public static KeggGenesFileData parseKeggGenesFileRecord(MultiLineBuffer multiLineBuffer) {
		try {
			BufferedReader br = new BufferedReader(new StringReader(multiLineBuffer.toString()));
			String line;
			boolean inDbLinks = false;
			boolean inPathwayIds = false;
			KeggGeneID keggGeneId = null;
			Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
			Set<KeggPathwayID> pathwayIds = new HashSet<KeggPathwayID>();
			while ((line = br.readLine()) != null) {
				if (line.startsWith(ENTRY)) {
					keggGeneId = getKeggGeneIdFromLine(line);
				} else if (StringUtil.startsWithRegex(line, "[A-Z]+") && !line.startsWith(PATHWAY)
						&& !line.startsWith(DBLINKS)) {
					inDbLinks = false;
					inPathwayIds = false;
				} else if (line.startsWith(PATHWAY) || inPathwayIds) {
					inPathwayIds = true;
					inDbLinks = false;
					pathwayIds.add(getKeggPathwayId(line));
				} else if (line.startsWith(DBLINKS) || inDbLinks) {
					inDbLinks = true;
					inPathwayIds = false;
					ids.addAll(getDbLink(line));
				} else {
					inDbLinks = false;
					inPathwayIds = false;
				}
			}
			return new KeggGenesFileData(keggGeneId, ids, pathwayIds, multiLineBuffer.getByteOffset());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * @param line
	 * @return
	 */
	private static Set<DataSourceIdentifier<?>> getDbLink(String line) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		if (line.startsWith(DBLINKS))
			line = StringUtil.removePrefix(line, DBLINKS);
		String[] toks = line.trim().split("\\s+");
		String databaseName = toks[0].replaceAll(":", "");
		for (int i = 1; i < toks.length; i++) {
			DataSourceIdentifier<?> id = DataSourceIdResolver.resolveId(databaseName, toks[i]);
			if (id != null)
				ids.add(id);
		}
		ids.remove(null);
		return ids;
	}

	/**
	 * @param line
	 * @return
	 */
	private static KeggPathwayID getKeggPathwayId(String line) {
		String[] toks = line.trim().split("\\s+");
		if (line.startsWith(PATHWAY))
			return new KeggPathwayID(toks[1]);
		return new KeggPathwayID(toks[0]);
	}

	/**
	 * @param line
	 * @return
	 */
	private static KeggGeneID getKeggGeneIdFromLine(String line) {
		String[] toks = line.split("\\s+");
		return new KeggGeneID(toks[1]);
	}

	/**
	 * @return the keggGeneID
	 */
	public KeggGeneID getKeggGeneID() {
		return keggGeneID;
	}

	/**
	 * @return the dbLinks
	 */
	public Set<DataSourceIdentifier<?>> getDbLinks() {
		return dbLinks;
	}

	/**
	 * @return the pathwayIds
	 */
	public Set<KeggPathwayID> getPathwayIds() {
		return pathwayIds;
	}

}
