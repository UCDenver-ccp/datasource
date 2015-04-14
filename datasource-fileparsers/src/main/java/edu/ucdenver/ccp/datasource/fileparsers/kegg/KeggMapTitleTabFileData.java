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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;

/**
 * Representation of data from KEGG map_title.tab file
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggMapTitleTabFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "KEGG_MAP_TITLE_RECORD_";
	private static final Logger logger = Logger.getLogger(KeggMapTitleTabFileData.class);

	private final KeggPathwayID keggPathwayID;

	private final KeggPathwayName keggPathwayName;

	public KeggMapTitleTabFileData(KeggPathwayID keggPathwayID, KeggPathwayName keggPathwayName, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.keggPathwayID = keggPathwayID;
		this.keggPathwayName = keggPathwayName;
	}

	public KeggPathwayID getKeggPathwayID() {
		return keggPathwayID;
	}

	public KeggPathwayName getKeggPathwayName() {
		return keggPathwayName;
	}

	public static KeggMapTitleTabFileData parseKeggMapTitleTabLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			KeggPathwayID keggPathwayID = new KeggPathwayID(toks[0]);
			KeggPathwayName keggPathwayName = new KeggPathwayName(toks[1]);
			return new KeggMapTitleTabFileData(keggPathwayID, keggPathwayName, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
