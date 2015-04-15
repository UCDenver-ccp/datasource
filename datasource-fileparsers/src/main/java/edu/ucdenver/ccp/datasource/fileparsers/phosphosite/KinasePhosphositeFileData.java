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
package edu.ucdenver.ccp.fileparsers.phosphosite;

import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;


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
