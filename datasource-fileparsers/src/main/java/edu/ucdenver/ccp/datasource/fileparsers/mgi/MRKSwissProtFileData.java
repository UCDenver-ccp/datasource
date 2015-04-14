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
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.fileparsers.field.ChromosomeNumber;

/**
 * Data representation of contents of MRK_SwissProt.rpt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.MGI, label="MRKSwissProt record")
public class MRKSwissProtFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "MRK_SWISSPROT_RECORD_";
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
	private final Set<UniProtID> swissProtAccessionIDs;

	public MRKSwissProtFileData(MgiGeneID mgiAccessionID, String markerSymbol, String status,
			String markerName, String cMPosition, ChromosomeNumber chromosome,
			Set<UniProtID> swissProtAccessionIDs, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mgiAccessionID = mgiAccessionID;
		this.markerSymbol = markerSymbol;
		this.status = status;
		this.markerName = markerName;
		cM_Position = cMPosition;
		this.chromosome = chromosome;
		this.swissProtAccessionIDs = swissProtAccessionIDs;
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

	public Set<UniProtID> getSwissProtAccessionIDs() {
		return swissProtAccessionIDs;
	}

	public static MRKSwissProtFileData parseMRKSwissProtLine(Line line) {
		String[] toks = line.getText().split("\\t");

		MgiGeneID mgiAccessionID = new MgiGeneID(toks[0]);
		String markerSymbol = new String(toks[1]);
		String status = toks[2];
		String markerName = new String(toks[3]);
		String cM_Position = toks[4];
		ChromosomeNumber chromosome = null;
		if (!toks[5].equals("UN"))
			chromosome = new ChromosomeNumber(toks[5]);
		Set<UniProtID> swissProtAccessionIDs = new HashSet<UniProtID>();
		for (String swissProtIDStr : toks[6].split(" "))
			swissProtAccessionIDs.add(new UniProtID(swissProtIDStr));

		return new MRKSwissProtFileData(mgiAccessionID, markerSymbol, status, markerName, cM_Position, chromosome,
				swissProtAccessionIDs, line.getByteOffset(), line.getLineNumber());

	}

}
