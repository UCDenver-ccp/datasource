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

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.fileparsers.field.ChromosomeNumber;

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
	private final ChromosomeNumber chromosome;
	@RecordField
	private final CmPosition cM_Position;
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
	public MRKListFileData(MgiGeneID mgiAccessionID, ChromosomeNumber chromosome,
			CmPosition cM_Position, Integer genomeCoordinateStart, Integer genomeCoordinateEnd, String strand,
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
	public ChromosomeNumber getChromosome() {
		return chromosome;
	}

	/**
	 * @return the cM_Position
	 */
	public CmPosition getcM_Position() {
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
