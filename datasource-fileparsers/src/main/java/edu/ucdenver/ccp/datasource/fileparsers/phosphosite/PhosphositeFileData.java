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
