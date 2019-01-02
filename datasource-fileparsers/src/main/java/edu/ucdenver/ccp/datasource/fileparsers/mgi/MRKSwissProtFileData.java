package edu.ucdenver.ccp.datasource.fileparsers.mgi;

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

import java.util.HashSet;
import java.util.Set;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

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
	private final String chromosome;
	@RecordField
	private final Set<UniProtID> swissProtAccessionIDs;

	public MRKSwissProtFileData(MgiGeneID mgiAccessionID, String markerSymbol, String status,
			String markerName, String cMPosition, String chromosome,
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

	public String getChromosome() {
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
		String chromosome = null;
		if (!toks[5].equals("UN"))
			chromosome = new String(toks[5]);
		Set<UniProtID> swissProtAccessionIDs = new HashSet<UniProtID>();
		for (String swissProtIDStr : toks[6].split(" "))
			swissProtAccessionIDs.add(new UniProtID(swissProtIDStr));

		return new MRKSwissProtFileData(mgiAccessionID, markerSymbol, status, markerName, cM_Position, chromosome,
				swissProtAccessionIDs, line.getByteOffset(), line.getLineNumber());

	}

}
