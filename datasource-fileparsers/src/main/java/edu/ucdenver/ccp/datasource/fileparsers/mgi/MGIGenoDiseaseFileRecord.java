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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MammalianPhenotypeID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * Data representation of contents of MGI_Geno_Disease.rpt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.MGI, label = "MGIGenoDisease record")
@Data()
public class MGIGenoDiseaseFileRecord extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(MGIGenoDiseaseFileRecord.class);

	/*
	 * Allelic Composition Genetic Background Mammalian Phenotype ID PubMed ID MGI Marker Accession
	 * ID (comma-delimited)
	 */
	@RecordField
	private final String allelicComposition;
	@RecordField
	private final String alleleSymbols;
	@RecordField
	private final Set<MgiGeneID> mgiAlleleIDs;
	@RecordField
	private final String geneticBackground;
	@RecordField
	private final MammalianPhenotypeID mammalianPhenotypeID;
	@RecordField
	private final Set<PubMedID> pubmedIds;
	@RecordField
	private final Set<MgiGeneID> mgiAccessionIDs;
	@RecordField
	private final Set<OmimID> omimIds;

	public MGIGenoDiseaseFileRecord(String allelicComposition, String alleleSymbols, Set<MgiGeneID> mgiAlleleIds,
			String geneticBackground, MammalianPhenotypeID mammalianPhenotypeID, Set<PubMedID> pubmedIds,
			Set<MgiGeneID> mgiAccessionIDs, Set<OmimID> omimIds, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.allelicComposition = allelicComposition;
		this.alleleSymbols = alleleSymbols;
		this.mgiAlleleIDs = mgiAlleleIds;
		this.geneticBackground = geneticBackground;
		this.mammalianPhenotypeID = mammalianPhenotypeID;
		this.pubmedIds = pubmedIds;
		this.mgiAccessionIDs = mgiAccessionIDs;
		this.omimIds = omimIds;
	}

}
