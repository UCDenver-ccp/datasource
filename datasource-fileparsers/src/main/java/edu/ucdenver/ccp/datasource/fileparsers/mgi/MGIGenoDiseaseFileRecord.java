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
