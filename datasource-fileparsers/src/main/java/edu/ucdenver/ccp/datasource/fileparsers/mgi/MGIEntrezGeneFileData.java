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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;

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
	private final String chromosome;
	@RecordField
	private final MgiGeneType type;
	@RecordField
	private final Set<MgiGeneID> secondaryAccessionIDs;
	@RecordField
	private final NcbiGeneId entrezGeneID;
	@RecordField
	private final Set<String> synonyms;

	public MGIEntrezGeneFileData(MgiGeneID mgiAccessionID, String markerSymbol, String status,
			String markerName, String cMPosition, String chromosome, MgiGeneType type,
			Set<MgiGeneID> secondaryAccessionIDs, NcbiGeneId entrezGeneID, Set<String> synonyms,
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

	public String getChromosome() {
		return chromosome;
	}

	public MgiGeneType getType() {
		return type;
	}

	public Set<MgiGeneID> getSecondaryAccessionIDs() {
		return secondaryAccessionIDs;
	}

	public NcbiGeneId getEntrezGeneID() {
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
		String chromosome = null;
		if (!toks[5].equals("UN"))
			chromosome = new String(toks[5]);
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
		NcbiGeneId entrezGeneID = null;
		if (toks.length > 8) {
			entrezGeneIDStr = toks[8];
			if (entrezGeneIDStr.trim().length() > 0)
				entrezGeneID = new NcbiGeneId(toks[8].trim());
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
