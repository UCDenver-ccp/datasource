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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;

/**
 * Representation of data from KEGG aaa_gene_map.tab file format.
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGeneMapTabFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(KeggGeneMapTabFileData.class);

	public static final String RECORD_NAME_PREFIX = "KEGG_GENE2PATHWAY_RECORD_";
	private final EntrezGeneID geneID;

	private final Set<KeggPathwayID> keggPathwayIDs;

	public KeggGeneMapTabFileData(EntrezGeneID geneID, Set<KeggPathwayID> keggPathwayIDs, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.geneID = geneID;
		this.keggPathwayIDs = keggPathwayIDs;
	}

	public EntrezGeneID getGeneID() {
		return geneID;
	}

	public Set<KeggPathwayID> getKeggPathwayIDs() {
		return keggPathwayIDs;
	}

	public static KeggGeneMapTabFileData parseKeggGeneMapTabLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			EntrezGeneID geneID = new EntrezGeneID(toks[0]);
			Set<KeggPathwayID> keggPathwayIDs = new HashSet<KeggPathwayID>();
			for (String tok : toks[1].split(" "))
				keggPathwayIDs.add(new KeggPathwayID(tok));
			return new KeggGeneMapTabFileData(geneID, keggPathwayIDs, line.getByteOffset(), line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
