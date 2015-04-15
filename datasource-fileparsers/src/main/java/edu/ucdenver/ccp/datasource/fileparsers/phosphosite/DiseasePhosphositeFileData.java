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
 * This class represents data available in the Disease Phosphosite Dataset files available here:
 * http://www.phosphosite.org/downloads/Disease-associated_sites.gz
 * 
 * @author Heather Underwood
 * 
 */
@Record(dataSource = DataSource.PHOSPHOSITE,schemaVersion = "1", comment = "", label="Disease Phosphosite record")
public class DiseasePhosphositeFileData extends SingleLineFileRecord {

	@RecordField
	private final String disease;
	@RecordField
	private final String alteration;
	@RecordField
	private final String proteinName;
	@RecordField
	private final UniProtID accessionId;
	@RecordField
	private final String gene_id;
	@RecordField
	private final String gene;
	@RecordField
	private final String chromosomeLocation;
	@RecordField
	private final Double molWeight;
	@RecordField
	private final String organism;
	@RecordField
	private final Integer siteGroupId;
	@RecordField
	private final String modifiedResidue;
	@RecordField
	private final Set<String> domain;
	@RecordField
	private final String sequence;
	@RecordField
	private final Integer pmids;
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
	 * @param disease
	 * @param alteration
	 * @param proteinName
	 * @param accessionID
	 * @param gene_id
	 * @param gene
	 * @param chromosomeLocation
	 * @param molWeight
	 * @param siteGroupId
	 * @param organism
	 * @param modifiedResidue
	 * @param domain
	 * @param sequence
	 * @param pmids
	 * @param LTLitRefs
	 * @param MSLitRefs
	 * @param MSCSTRefs
	 * @param CSTCatalogNum
	 */
	public DiseasePhosphositeFileData(String disease, String alteration, String proteinName, UniProtID accessionId, String gene_id, String gene,
			String chromosomeLocation, Double molWeight,String organism,Integer siteGroupId,String modifiedResidue, Set<String> domain,
			String sequence, Integer pmids, Integer LTLitRefs, Integer MSLitRefs, Integer MSCSTRefs,
			Set<String> CSTCatalogNum,  long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.disease = disease;
		this.alteration = alteration;
		this.proteinName = proteinName;
		this.accessionId = accessionId;
		this.gene_id = gene_id;
		this.gene = gene;
		this.chromosomeLocation = chromosomeLocation;
		this.modifiedResidue = modifiedResidue;
		this.siteGroupId = siteGroupId;
		this.organism = organism;
		this.molWeight = molWeight;
		this.domain = domain;
		this.sequence = sequence;
		this.pmids = pmids;
		this.LTLitRefs = LTLitRefs;
		this.MSLitRefs = MSLitRefs;
		this.MSCSTRefs = MSCSTRefs;
		this.CSTCatalogNum = CSTCatalogNum;
	}
	
	/**
	 * @return the disease name
	 */
	public String getDiseaseName() {
		return disease;
	}


	/**
	 * @return the alteration
	 */
	public String getAlteration() {
		return alteration;
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
		return accessionId;
	}

	/**
	 * @return the gene ID
	 */
	public String getGeneId() {
		return gene_id;
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
	 * @return the PMIDs
	 */
	public Integer getPMIDs() {
		return pmids;
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
