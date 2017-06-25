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

import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VegaID;
import lombok.Data;

/**
 * Data representation of contents of MRK_SwissProt.rpt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.MGI,schemaVersion = "2", comment = "Version 2 of this schema added columns for strand, genome coordinate start/end, and a number of the identifier mapping fields.",label="MRKSequence record")
@Data
public class MRKSequenceFileData extends SingleLineFileRecord {

	@RecordField
	private final MgiGeneID mgiAccessionID;
	@RecordField
	private final String markerSymbol;
	@RecordField
	private final String status;
	@RecordField
	private final MgiGeneType markerType;
	@RecordField
	private final String markerName;
	@RecordField
	private final String cM_Position;
	@RecordField
	private final String chromosome;
	@RecordField
	private final Integer genomeCoordinateStart;
	@RecordField
	private final Integer genomeCoordinateEnd;
	@RecordField
	private final String strand;
	@RecordField
	private final Set<DataSourceIdentifier<?>> genBankAccessionIDs;
	@RecordField
	private final Set<RefSeqID> refseqTranscriptIds;
	@RecordField
	private final Set<VegaID> vegaTranscriptIds;
	@RecordField
	private final Set<EnsemblGeneID> ensemblTranscriptId;
	@RecordField
	private final Set<UniProtID> uniprotIds;
	@RecordField
	private final Set<UniProtID> tremblIds;
	@RecordField
	private final Set<VegaID> vegaProteinIds;
	@RecordField
	private final Set<EnsemblGeneID> ensemblProteinIds;
	@RecordField
	private final Set<RefSeqID> refseqProteinIds;
	@RecordField
	private final Set<UniGeneID> unigeneIds;
	@RecordField
	private final String featureType;
	

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param mgiAccessionID
	 * @param markerSymbol
	 * @param status
	 * @param markerType
	 * @param markerName
	 * @param cM_Position
	 * @param chromosome
	 * @param genomeCoordinateStart
	 * @param genomeCoordinateEnd
	 * @param strand
	 * @param genBankAccessionIDs
	 * @param refseqTranscriptIds
	 * @param vegaTranscriptIds
	 * @param ensemblTranscriptId
	 * @param uniprotIds
	 * @param tremblId
	 * @param vegaProteinIds
	 * @param ensemblProteinIds
	 * @param refseqProteinIds
	 * @param featureType 
	 */
	public MRKSequenceFileData(MgiGeneID mgiAccessionID, String markerSymbol, String status,
			MgiGeneType markerType, String markerName, String cM_Position, String chromosome,
			Integer genomeCoordinateStart, Integer genomeCoordinateEnd, String strand,
			Set<DataSourceIdentifier<?>> genBankAccessionIDs, Set<RefSeqID> refseqTranscriptIds,
			Set<VegaID> vegaTranscriptIds, Set<EnsemblGeneID> ensemblTranscriptId, Set<UniProtID> uniprotIds,
			Set<UniProtID> tremblIds, Set<VegaID> vegaProteinIds, Set<EnsemblGeneID> ensemblProteinIds,
			Set<RefSeqID> refseqProteinIds, Set<UniGeneID> unigeneIds, String featureType, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mgiAccessionID = mgiAccessionID;
		this.markerSymbol = markerSymbol;
		this.status = status;
		this.markerType = markerType;
		this.markerName = markerName;
		this.cM_Position = cM_Position;
		this.chromosome = chromosome;
		this.genomeCoordinateStart = genomeCoordinateStart;
		this.genomeCoordinateEnd = genomeCoordinateEnd;
		this.strand = strand;
		this.genBankAccessionIDs = genBankAccessionIDs;
		this.refseqTranscriptIds = refseqTranscriptIds;
		this.vegaTranscriptIds = vegaTranscriptIds;
		this.ensemblTranscriptId = ensemblTranscriptId;
		this.uniprotIds = uniprotIds;
		this.tremblIds = tremblIds;
		this.vegaProteinIds = vegaProteinIds;
		this.ensemblProteinIds = ensemblProteinIds;
		this.refseqProteinIds = refseqProteinIds;
		this.unigeneIds = unigeneIds;
		this.featureType = featureType;
	}

	
}
