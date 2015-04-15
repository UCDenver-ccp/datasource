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
 * This class represents data available in the Regulatory Phosphosite Dataset files available here:
 * http://www.phosphosite.org/downloads/Regulatory_sites.gz
 * 
 * @author Heather Underwood
 * 
 */
@Record(dataSource = DataSource.PHOSPHOSITE,schemaVersion = "1", comment = "", label="Regulatory Phosphosite record")
public class RegulatoryPhosphositeFileData extends SingleLineFileRecord {

	@RecordField
	private final String proteinName;
	@RecordField
	private final String proteinType;
	@RecordField
	private final UniProtID accessionId;
	@RecordField
	private final String gene_id;
	@RecordField
	private final String gene;
	@RecordField
	private final String chromosomeLocation;
	@RecordField
	private final String organism;
	@RecordField
	private final String modifiedResidue;
	@RecordField
	private final Integer siteGroupId;
	@RecordField
	private final String sequence;
	@RecordField
	private final Set<String> domain;
	@RecordField
	private final Set<String> on_function;
	@RecordField
	private final Set<String> on_process;
	@RecordField
	private final Set<String> on_prot_interact;
	@RecordField
	private final Set<String> on_other_interact;
	@RecordField
	private final Integer pmids;
	@RecordField
	private final Integer LTLitRefs;
	@RecordField
	private final Integer MSLitRefs;
	@RecordField
	private final Integer MSCSTRefs;
	@RecordField
	private final String notes;
	
	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param proteinName
	 * @param proteinType
	 * @param accessionID
	 * @param gene_id
	 * @param gene
	 * @param chromosomeLocation
	 * @param organism
	 * @param modifiedResidue
	 * @param siteGroupId
	 * @param sequence
	 * @param domain
	 * @param on_function
	 * @param on_process
	 * @param on_prot_interact
	 * @param on_other_interact
	 * @param pmids
	 * @param LTLitRefs
	 * @param MSLitRefs
	 * @param MSCSTRefs
	 * @param notes
	 */
	public RegulatoryPhosphositeFileData(String proteinName, String proteinType, UniProtID accessionId, String gene_id, String gene,
			String chromosomeLocation, String organism, String modifiedResidue, Integer siteGroupId,
			String sequence, Set<String> domain, Set<String> on_function, Set<String> on_process, Set<String> on_prot_interact,
			Set<String> on_other_interact, Integer pmids, Integer LTLitRefs, Integer MSLitRefs, Integer MSCSTRefs,
			String notes,  long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.proteinName = proteinName;
		this.proteinType = proteinType;
		this.accessionId = accessionId;
		this.gene_id = gene_id;
		this.gene = gene;
		this.chromosomeLocation = chromosomeLocation;
		this.organism = organism;
		this.modifiedResidue = modifiedResidue;
		this.siteGroupId = siteGroupId;
		this.sequence = sequence;
		this.domain = domain;
		this.on_function = on_function;
		this.on_process = on_process;
		this.on_prot_interact = on_prot_interact;
		this.on_other_interact = on_other_interact;
		this.pmids = pmids;
		this.LTLitRefs = LTLitRefs;
		this.MSLitRefs = MSLitRefs;
		this.MSCSTRefs = MSCSTRefs;
		this.notes = notes;
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
	 * @return the organism
	 */
	public String getOrganism() {
		return organism;
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
	 * @return the sequence (SITE_+/-7_AA)
	 */
	public String getSequence() {
		return sequence;
	}
	
	/**
	 * @return the domain
	 */
	public Set<String> getDomain() {
		return domain;
	}
	
	/**
	 * @return the on_function
	 */
	public Set<String> getOnFunction() {
		return on_function;
	}
	
	/**
	 * @return the on_process
	 */
	public Set<String> getOnProcess() {
		return on_process;
	}
	
	/**
	 * @return the on_prot_interact
	 */
	public Set<String> getOnProtInteract() {
		return on_prot_interact;
	}
	
	/**
	 * @return the on_other_interact
	 */
	public Set<String> getOnOtherInteract() {
		return on_other_interact;
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
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

}
