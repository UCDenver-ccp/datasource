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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;

/**
 * This class represents data available in the MRK_List#.sql.rpt files available here:
 * ftp://ftp.informatics.jax.org/pub/reports/index.html
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.MGI,schemaVersion = "2", comment = "Version 2 of this record schema includes new fields now part of the source data file.", label="MRK_List record")
public class MRKListFileData extends SingleLineFileRecord {

	@RecordField
	private final MgiGeneID mgiAccessionID;
	@RecordField
	private final String chromosome;
	@RecordField
	private final String cM_Position;
	@RecordField
	private final Integer genomeCoordinateStart;
	@RecordField
	private final Integer genomeCoordinateEnd;
	@RecordField
	private final String strand;
	@RecordField
	private final String markerSymbol;
	@RecordField
	private final String status;
	@RecordField
	private final String markerName;
	@RecordField
	private final MgiGeneType markerType;
	@RecordField
	private final String featureType;
	@RecordField
	private final Set<String> markerSynonyms;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param mgiAccessionID
	 * @param chromosome
	 * @param cM_Position
	 * @param genomeCoordinateStart
	 * @param genomeCoordinateEnd
	 * @param strand
	 * @param markerSymbol
	 * @param status
	 * @param markerName
	 * @param markerType
	 * @param featureType
	 * @param markerSynonyms
	 */
	public MRKListFileData(MgiGeneID mgiAccessionID, String chromosome,
			String cM_Position, Integer genomeCoordinateStart, Integer genomeCoordinateEnd, String strand,
			String markerSymbol, String status, String markerName, MgiGeneType markerType,
			String featureType, Set<String> markerSynonyms,long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mgiAccessionID = mgiAccessionID;
		this.chromosome = chromosome;
		this.cM_Position = cM_Position;
		this.genomeCoordinateStart = genomeCoordinateStart;
		this.genomeCoordinateEnd = genomeCoordinateEnd;
		this.strand = strand;
		this.markerSymbol = markerSymbol;
		this.status = status;
		this.markerName = markerName;
		this.markerType = markerType;
		this.featureType = featureType;
		this.markerSynonyms = markerSynonyms;
	}

	/**
	 * @return the mgiAccessionID
	 */
	public MgiGeneID getMgiAccessionID() {
		return mgiAccessionID;
	}

	/**
	 * @return the chromosome
	 */
	public String getChromosome() {
		return chromosome;
	}

	/**
	 * @return the cM_Position
	 */
	public String getcM_Position() {
		return cM_Position;
	}

	/**
	 * @return the genomeCoordinateStart
	 */
	public Integer getGenomeCoordinateStart() {
		return genomeCoordinateStart;
	}

	/**
	 * @return the genomeCoordinateEnd
	 */
	public Integer getGenomeCoordinateEnd() {
		return genomeCoordinateEnd;
	}

	/**
	 * @return the strand
	 */
	public String getStrand() {
		return strand;
	}

	/**
	 * @return the markerSymbol
	 */
	public String getMarkerSymbol() {
		return markerSymbol;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the markerName
	 */
	public String getMarkerName() {
		return markerName;
	}

	/**
	 * @return the markerType
	 */
	public MgiGeneType getMarkerType() {
		return markerType;
	}

	/**
	 * @return the featureType
	 */
	public String getFeatureType() {
		return featureType;
	}

	/**
	 * @return the markerSynonyms
	 */
	public Set<String> getMarkerSynonyms() {
		return markerSynonyms;
	}

}
