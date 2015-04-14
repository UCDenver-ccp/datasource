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

import java.util.Set;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.other.VegaID;
import edu.ucdenver.ccp.fileparsers.field.ChromosomeNumber;

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
	private final ChromosomeNumber chromosome;
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
	 */
	public MRKSequenceFileData(MgiGeneID mgiAccessionID, String markerSymbol, String status,
			MgiGeneType markerType, String markerName, String cM_Position, ChromosomeNumber chromosome,
			Integer genomeCoordinateStart, Integer genomeCoordinateEnd, String strand,
			Set<DataSourceIdentifier<?>> genBankAccessionIDs, Set<RefSeqID> refseqTranscriptIds,
			Set<VegaID> vegaTranscriptIds, Set<EnsemblGeneID> ensemblTranscriptId, Set<UniProtID> uniprotIds,
			Set<UniProtID> tremblIds, Set<VegaID> vegaProteinIds, Set<EnsemblGeneID> ensemblProteinIds,
			Set<RefSeqID> refseqProteinIds, Set<UniGeneID> unigeneIds, long byteOffset, long lineNumber) {
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
	}

	
}
