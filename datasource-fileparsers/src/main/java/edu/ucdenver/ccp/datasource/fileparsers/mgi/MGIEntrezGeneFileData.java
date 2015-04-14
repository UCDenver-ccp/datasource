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
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.fileparsers.field.ChromosomeNumber;

/**
 * Data representation of contents of MGI_EntrezGene.rpt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.MGI, label="MGIEntrezGene record")
public class MGIEntrezGeneFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "MGI_ENTREZGENE_FILE_RECORD_";
	@RecordField
	private final MgiGeneID mgiAccessionID;
	@RecordField
	private final String markerSymbol;
	@RecordField
	private final String status;
	@RecordField
	private final String markerName;
	@RecordField
	private final String cM_Position;
	@RecordField
	private final ChromosomeNumber chromosome;
	@RecordField
	private final MgiGeneType type;
	@RecordField
	private final Set<MgiGeneID> secondaryAccessionIDs;
	@RecordField
	private final EntrezGeneID entrezGeneID;
	@RecordField
	private final Set<String> synonyms;

	public MGIEntrezGeneFileData(MgiGeneID mgiAccessionID, String markerSymbol, String status,
			String markerName, String cMPosition, ChromosomeNumber chromosome, MgiGeneType type,
			Set<MgiGeneID> secondaryAccessionIDs, EntrezGeneID entrezGeneID, Set<String> synonyms,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mgiAccessionID = mgiAccessionID;
		this.markerSymbol = markerSymbol;
		this.status = status;
		this.markerName = markerName;
		this.cM_Position = cMPosition;
		this.chromosome = chromosome;
		this.type = type;
		this.secondaryAccessionIDs = secondaryAccessionIDs;
		this.entrezGeneID = entrezGeneID;
		this.synonyms = synonyms;
	}

	public MgiGeneID getMgiAccessionID() {
		return mgiAccessionID;
	}

	public String getMarkerSymbol() {
		return markerSymbol;
	}

	public String getStatus() {
		return status;
	}

	public String getMarkerName() {
		return markerName;
	}

	public String getcM_Position() {
		return cM_Position;
	}

	public ChromosomeNumber getChromosome() {
		return chromosome;
	}

	public MgiGeneType getType() {
		return type;
	}

	public Set<MgiGeneID> getSecondaryAccessionIDs() {
		return secondaryAccessionIDs;
	}

	public EntrezGeneID getEntrezGeneID() {
		return entrezGeneID;
	}

	public Set<String> getSynonyms() {
		return synonyms;
	}

	public static MGIEntrezGeneFileData parseMGIEntrezGeneLine(Line line) {
		String[] toks = line.getText().split("\\t");

		MgiGeneID mgiAccessionID = new MgiGeneID(toks[0]);
		String markerSymbol = new String(toks[1]);
		String status = toks[2];
		String markerName = new String(toks[3]);
		String cM_Position = toks[4].trim();
		ChromosomeNumber chromosome = null;
		if (!toks[5].equals("UN"))
			chromosome = new ChromosomeNumber(toks[5]);
		MgiGeneType type = MgiGeneType.getValue(toks[6]);
		Set<MgiGeneID> secondaryAccIDs = new HashSet<MgiGeneID>();
		if (toks.length > 7) {
			String[] secondaryAccessionIDs = toks[7].split("\\|");
			for (String secondaryID : secondaryAccessionIDs) {
				if (secondaryID.trim().length() > 0) {
					secondaryAccIDs.add(new MgiGeneID(secondaryID));
				}
			}
		}
		String entrezGeneIDStr = null;
		EntrezGeneID entrezGeneID = null;
		if (toks.length > 8) {
			entrezGeneIDStr = toks[8];
			if (entrezGeneIDStr.trim().length() > 0)
				entrezGeneID = new EntrezGeneID(toks[8].trim());
		}
		Set<String> syns = new HashSet<String>();
		if (toks.length > 9) {
			String[] synonyms = toks[9].split("\\|");
			for (String synonym : synonyms) {
				if (synonym.trim().length() > 0) {
					syns.add(new String(synonym));
				}
			}
		}

		return new MGIEntrezGeneFileData(mgiAccessionID, markerSymbol, status, markerName, cM_Position, chromosome,
				type, secondaryAccIDs, entrezGeneID, syns, line.getByteOffset(), line.getLineNumber());
	}

}
