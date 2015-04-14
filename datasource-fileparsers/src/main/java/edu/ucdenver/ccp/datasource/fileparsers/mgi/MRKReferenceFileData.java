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
package edu.ucdenver.ccp.fileparsers.mgi;

import java.util.HashSet;
import java.util.Set;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * Data representation of contents of MRK_Reference.rpt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.MGI,label="MRKReference record")
public class MRKReferenceFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "MRD_REFERENCE_FILE_RECORD_";
	@RecordField
	private final MgiGeneID mgiAccessionID;
	@RecordField
	private final String markerSymbol;
	@RecordField
	private final String markerName;
	@RecordField
	private final Set<String> markerSynonyms;
	@RecordField
	private final Set<PubMedID> pubmedIDs;

	public MRKReferenceFileData(MgiGeneID mgiAccessionID, String markerSymbol, String markerName,
			Set<String> markerSynonyms, Set<PubMedID> pubMedIDs, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mgiAccessionID = mgiAccessionID;
		this.markerSymbol = markerSymbol;
		this.markerName = markerName;
		this.markerSynonyms = markerSynonyms;
		this.pubmedIDs = pubMedIDs;
	}

	public MgiGeneID getMgiAccessionID() {
		return mgiAccessionID;
	}

	public String getMarkerSymbol() {
		return markerSymbol;
	}

	public String getMarkerName() {
		return markerName;
	}

	public Set<String> getMarkerSynonyms() {
		return markerSynonyms;
	}

	public Set<PubMedID> getPubMedIDs() {
		return pubmedIDs;
	}

	public static MRKReferenceFileData parseMRKReferenceLine(Line line) {
		String[] toks = line.getText().split("\\t");

		MgiGeneID mgiAccessionID = new MgiGeneID(toks[0]);
		String markerSymbol = new String(toks[1]);
		String markerName = new String(toks[2]);
		String[] synonyms = toks[3].split("\\|");
		Set<String> markerSynonyms = new HashSet<String>();
		for (String synonym : synonyms) {
			if (synonym.trim().length() > 0) {
				markerSynonyms.add(new String(synonym));
			}
		}
		String[] pmids = toks[4].split("\\|");
		Set<PubMedID> pubMedIDs = new HashSet<PubMedID>();
		for (String pmid : pmids) {
			if (pmid.trim().length() > 0) {
				pubMedIDs.add(new PubMedID(pmid));
			}
		}

		return new MRKReferenceFileData(mgiAccessionID, markerSymbol, markerName, markerSynonyms, pubMedIDs,
				line.getByteOffset(), line.getLineNumber());

	}

}
