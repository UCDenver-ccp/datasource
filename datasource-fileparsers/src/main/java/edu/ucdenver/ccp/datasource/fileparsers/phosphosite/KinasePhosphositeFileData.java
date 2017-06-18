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
 * This class represents data available in the Kinase Phosphosite Dataset files available here:
 * http://www.phosphosite.org/downloads/Kinase_Substrate_Dataset.gz
 * 
 * @author Heather Underwood
 * 
 */
@Record(dataSource = DataSource.PHOSPHOSITE,schemaVersion = "1", comment = "", label="Kinase Phosphosite record")
public class KinasePhosphositeFileData extends SingleLineFileRecord {

	@RecordField
	private final String kinase;
	@RecordField
	private final UniProtID accessionId;
	@RecordField
	private final String gene;
	@RecordField
	private final String chromosomeLocation;
	@RecordField
	private final String organism;
	@RecordField
	private final String substrate;
	@RecordField
	private final Integer sub_geneId;
	@RecordField
	private final UniProtID sub_accessionId;
	@RecordField
	private final String sub_gene;
	@RecordField
	private final String sub_organism;
	@RecordField
	private final String sub_modifiedResidue;
	@RecordField
	private final Integer siteGroupId;
	@RecordField
	private final String sequence;
	@RecordField
	private final Boolean in_vivo_rxn;
	@RecordField
	private final Boolean in_vitro_rxn;
	@RecordField
	private final Set<String> CSTCatalogNum;
	
	/**
	@param byteOffset
	@param lineNumber
	@param kinase;
	@param accessionId;
	@param gene;
	@param chromosomeLocation;
	@param organism;
	@param substrate;
	@param sub_geneId;
	@param sub_accessionId;
	@param sub_gene;
	@param sub_organism;
	@param sub_modifiedResidue;
	@param siteGroupId;
	@param sequence;
	@param in_vivo_rxn;
	@param in_vitro_rxn;
	@param CSTCatalogNum;
	 */
	public KinasePhosphositeFileData(String kinase, UniProtID accessionId, String gene,
			String chromosomeLocation, String organism, String substrate,Integer sub_geneId, UniProtID sub_accessionId, String sub_gene,
			String sub_organism, String sub_modifiedResidue, Integer siteGroupId, String sequence, Boolean in_vivo_rxn,
			Boolean in_vitro_rxn, Set<String> CSTCatalogNum,  long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.kinase = kinase;
		this.accessionId = accessionId;
		this.gene = gene;
		this.chromosomeLocation = chromosomeLocation;
		this.organism = organism;
		this.substrate = substrate;
		this.sub_geneId = sub_geneId;
		this.sub_accessionId = sub_accessionId;
		this.sub_gene = sub_gene;
		this.sub_organism = sub_organism;
		this.sub_modifiedResidue = sub_modifiedResidue;
		this.siteGroupId = siteGroupId;
		this.sequence = sequence;
		this.in_vivo_rxn = in_vivo_rxn;
		this.in_vitro_rxn = in_vitro_rxn;
		this.CSTCatalogNum = CSTCatalogNum;
	}
	
	/**
	 * @return the kinase name
	 */
	public String getKinaseName() {
		return kinase;
	}


	/**
	 * @return the accession ID
	 */
	public UniProtID getAccessionId() {
		return accessionId;
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
	 * @return the organism
	 */
	public String getOrganism() {
		return organism;
	}
	

	/**
	 * @return the substrate
	 */
	public String getSubstrate() {
		return substrate;
	}
	

	/**
	 * @return the sub gene id
	 */
	public Integer getSubGeneId() {
		return sub_geneId;
	}


	/**
	 * @return the sub accession id
	 */
	public UniProtID getSubAccessionId() {
		return sub_accessionId;
	}


	/**
	 * @return the sub gene
	 */
	public String getSubGene() {
		return sub_gene;
	}
	
	/**
	 * @return the sub organism
	 */
	public String getSubOrganism() {
		return sub_organism;
	}
	
	/**
	 * @return the sub modified residue
	 */
	public String getSubModifiedResidue() {
		return sub_modifiedResidue;
	}


	/**
	 * @return the site group id
	 */
	public Integer getSiteGroupId() {
		return siteGroupId;
	}
	
	/**
	 * @return the sequence (SITE_+/-7_AA)
	 */
	public String getSequence() {
		return sequence;
	}
	
	/**
	 * @return the in vivo reaction
	 */
	public Boolean getInVivoRxn() {
		return in_vivo_rxn;
	}
	
	/**
	 * @return the in vitro reaction
	 */
	public Boolean getInVitroRxn() {
		return in_vitro_rxn;
	}
	
	/**
	 * @return the CST Catlog # if available 
	 */
	public Set<String> getCSTCatalogNum() {
		return CSTCatalogNum;
	}

}
