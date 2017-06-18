package edu.ucdenver.ccp.datasource.fileparsers.phosphosite;

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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;


/**
 * This class represents data available in the Acetylation Site Dataset files available here:
 * http://www.phosphosite.org/downloads/Acetylation_site_dataset.gz
 * 
 * @author Heather Underwood
 * 
 */
@Record(dataSource = DataSource.PHOSPHOSITE,schemaVersion = "1", comment = "", label="Acetylation_Site_List record")
public class PhosphositeFileData extends SingleLineFileRecord {

	@RecordField
	private final String proteinName;
	@RecordField
	private final UniProtID phosphositeAccessionId;
	@RecordField
	private final String gene;
	@RecordField
	private final String chromosomeLocation;
	@RecordField
	private final String modificationType;
	@RecordField
	private final String modifiedResidue;
	@RecordField
	private final Integer siteGroupId;
	@RecordField
	private final String organism;
	@RecordField
	private final Double molWeight;
	@RecordField
	private final Set<String> domain;
	@RecordField
	private final String sequence;
	@RecordField
	private final Integer LTLitRefs;
	@RecordField
	private final Integer MSLitRefs;
	@RecordField
	private final Integer MSCSTRefs;
	@RecordField
	private final Set<String> CSTCatalogNum;
	
	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param proteinName
	 * @param phosphositeAccessionID
	 * @param gene
	 * @param chromosomeLocation
	 * @param modificationType
	 * @param modifiedResidue
	 * @param siteGroupId
	 * @param organism
	 * @param molWeight
	 * @param domain
	 * @param sequence
	 * @param LTLitRefs
	 * @param MSLitRefs
	 * @param MSCSTRefs
	 * @param CSTCatalogNum
	 */
	public PhosphositeFileData(String proteinName, UniProtID phosphositeAccessionId, String gene,
			String chromosomeLocation, String modificationType, String modifiedResidue, Integer siteGroupId,
			String organism, Double molWeight, String sequence, Integer LTLitRefs, Integer MSLitRefs, Integer MSCSTRefs,
			Set<String> CSTCatalogNum, Set<String> domain, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.proteinName = proteinName;
		this.phosphositeAccessionId = phosphositeAccessionId;
		this.gene = gene;
		this.chromosomeLocation = chromosomeLocation;
		this.modificationType = modificationType;
		this.modifiedResidue = modifiedResidue;
		this.siteGroupId = siteGroupId;
		this.organism = organism;
		this.molWeight = molWeight;
		this.domain = domain;
		this.sequence = sequence;
		this.LTLitRefs = LTLitRefs;
		this.MSLitRefs = MSLitRefs;
		this.MSCSTRefs = MSCSTRefs;
		this.CSTCatalogNum = CSTCatalogNum;
	}
	
	
	/**
	 * @return the protein name
	 */
	public String getProteinName() {
		return proteinName;
	}

	/**
	 * @return the protein accession ID
	 */
	public UniProtID getAccessionId() {
		return phosphositeAccessionId;
	}

	/**
	 * @return the gene
	 */
	public String getGene() {
		return gene;
	}

	/**
	 * @return the chromosome location
	 */
	public String getChromosomeLocation() {
		return chromosomeLocation;
	}

	/**
	 * @return the modification type (e.g., acetylation, phosphorylation, etc.)
	 */
	public String getModificationType() {
		return modificationType;
	}

	/**
	 * @return the modified residue
	 */
	public String getModifiedResidue() {
		return modifiedResidue;
	}

	/**
	 * @return the site group id
	 */
	public Integer getSiteGroupId() {
		return siteGroupId;
	}

	/**
	 * @return the organism
	 */
	public String getOrganism() {
		return organism;
	}

	/**
	 * @return the molecular weight
	 */
	public Double getMolWeight() {
		return molWeight;
	}

	/**
	 * @return the domain
	 */
	public Set<String> getDomain() {
		return domain;
	}

	/**
	 * @return the sequence (SITE_+/-7_AA)
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @return the number of lit references for low threshold experiments
	 */
	public Integer getLTLitRefs() {
		if (LTLitRefs == null)
			return -1;
		else return LTLitRefs;
	}
	
	/**
	 * @return the number of lit references for mass spec experiments
	 */
	public Integer getMSLitRefs() {
		if(MSLitRefs == null)
			return -1;
		else return MSLitRefs;
	}
	
	/**
	 * @return the number of mass spec experiments at cell signaling technologies (CST)
	 */
	public Integer getMSCSTRefs() {
		if (MSCSTRefs == null)
			return -1;
		else return MSCSTRefs;
	}
	
	/**
	 * @return the CST Catlog # if available 
	 */
	public Set<String> getCSTCatalogNum() {
		return CSTCatalogNum;
	}

}
