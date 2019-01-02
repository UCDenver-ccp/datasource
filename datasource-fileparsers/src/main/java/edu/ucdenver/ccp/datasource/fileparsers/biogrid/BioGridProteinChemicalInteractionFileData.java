package edu.ucdenver.ccp.datasource.fileparsers.biogrid;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2018 Regents of the University of Colorado
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AtcCode;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioGridChemicalId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioGridID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioGridInteractionId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioGridInteractorId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ChemSpiderId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ChemicalAbstractsServiceId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DrugBankID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.BioGridPublicationId;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Getter;
import lombok.ToString;

/**
 * Data representation of the contents of the BioPlex Interaction List file.
 * http://bioplex.hms.harvard.edu/data/BioPlex_interactionList_v4a.tsv
 * 
 */
@Record(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD, dataSource = DataSource.BIOGRID)
@ToString
@Getter
public class BioGridProteinChemicalInteractionFileData extends SingleLineFileRecord {

	// #BioGRID Chemical Interaction ID
	// BioGRID Gene ID
	// Entrez Gene ID
	// Systematic Name
	// Official Symbol
	// Synonyms
	// Organism ID
	// Organism
	// Action
	// Interaction Type
	// Author
	// Pubmed ID
	// BioGRID Publication ID
	// BioGRID Chemical ID
	// Chemical Name
	// Chemical Synonyms
	// Chemical Brands
	// Chemical Source
	// Chemical Source ID
	// Molecular Formula
	// Chemical Type
	// ATC Codes
	// CAS Number
	// Curated By
	// Method
	// Method Description
	// Related BioGRID Gene ID
	// Related Entrez Gene ID
	// Related Systematic Name
	// Related Official Symbol
	// Related Synonyms
	// Related Organism ID
	// Related Organism
	// Related Type
	// Notes

	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___BIOGRID_INTERACTION_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> biogridInteractionId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___BIOGRID_GENE_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> biogridGeneId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___NCBI_GENE_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> ncbiGeneId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___SYSTEMATIC_GENE_NAME_FIELD_VALUE)
	private final String systematicName;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___OFFICIAL_GENE_SYMBOL_FIELD_VALUE)
	private final String officialSymbol;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___GENE_SYNONYMS_FIELD_VALUE)
	private final List<String> synonyms;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___ORGANISM_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> organismId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___ORGANISM_FIELD_VALUE)
	private final String organism;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___ACTION_FIELD_VALUE)
	private final String action;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___INTERACTION_TYPE_FIELD_VALUE)
	private final String interactionType;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___AUTHOR_FIELD_VALUE)
	private final String author;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> pubmedId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___BIOGRID_PUBLICATION_IDENTIFIER_FIELD_VALUE)
	private final BioGridPublicationId biogridPublicationId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___BIOGRID_CHEMICAL_IDENTIFIER_FIELD_VALUE)
	private final BioGridChemicalId biogridChemicalId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CHEMICAL_NAME_FIELD_VALUE)
	private final String chemicalName;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CHEMICAL_SYNONYMS_FIELD_VALUE)
	private final List<String> chemicalSynonyms;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CHEMICAL_BRANDS_FIELD_VALUE)
	private final List<String> chemicalBrands;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CHEMICAL_SOURCE_FIELD_VALUE)
	private final String chemicalSource;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CHEMICAL_SOURCE_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> chemicalSourceId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___MOLECULAR_FORMULA_FIELD_VALUE)
	private final String molecularFormula;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CHEMICAL_TYPE_FIELD_VALUE)
	private final String chemicalType;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___ATC_CODES_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> atcCodes;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CAS_NUMBER_FIELD_VALUE)
	private final ChemicalAbstractsServiceId casNumber;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___CURATED_BY_FIELD_VALUE)
	private final String curatedBy;

	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___METHOD_FIELD_VALUE)
	private final String method;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___METHOD_DESCRIPTION_FIELD_VALUE)
	private final String methodDescription;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_BIOGRID_GENE_IDENTIFIER_FIELD_VALUE)
	private final BioGridID relatedBiogridGeneId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_NCBI_GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId relatedNcbiGeneId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_SYSTEMATIC_NAME_FIELD_VALUE)
	private final String relatedSystematicName;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_OFFICIAL_SYMBOL_FIELD_VALUE)
	private final String relatedOfficialSymbol;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_SYNONYMS_FIELD_VALUE)
	private final List<String> relatedSynonyms;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_ORGANISM_IDENTIFIER_FIELD_VALUE)
	private final NcbiTaxonomyID relatedOrganismId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_ORGANISM_NAME_FIELD_VALUE)
	private final String relatedOrganismName;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___RELATED_TYPE_FIELD_VALUE)
	private final String relatedType;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_CHEMICAL_INTERACTION_RECORD___NOTES_FIELD_VALUE)
	private final String notes;

	public BioGridProteinChemicalInteractionFileData(DataSourceIdentifier<?> biogridInteractionId,
			DataSourceIdentifier<?> biogridGeneId, DataSourceIdentifier<?> ncbiGeneId, String systematicName,
			String officialSymbol, List<String> synonyms, DataSourceIdentifier<?> organismId2, String organism,
			String action, String interactionType, String author, DataSourceIdentifier<?> pubmedId2,
			BioGridPublicationId biogridPublicationId, BioGridChemicalId biogridChemicalId, String chemicalName,
			List<String> chemicalSynonyms, List<String> chemicalBrands, String chemicalSource,
			DataSourceIdentifier<?> chemicalSourceId, String molecularFormula, String chemicalType,
			Set<DataSourceIdentifier<?>> atcCodes2, ChemicalAbstractsServiceId casNumber, String curatedBy,
			String method, String methodDescription, BioGridID relatedBioGridId, NcbiGeneId relatedNcbiGeneId,
			String relatedSystematicName, String relatedOfficialSymbol, List<String> relatedSynonyms,
			NcbiTaxonomyID relatedOrganismId, String relatedOrganismName, String relatedType, String notes,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.biogridInteractionId = biogridInteractionId;
		this.biogridGeneId = biogridGeneId;
		this.ncbiGeneId = ncbiGeneId;
		this.systematicName = systematicName;
		this.officialSymbol = officialSymbol;
		this.synonyms = synonyms;
		this.organismId = organismId2;
		this.organism = organism;
		this.action = action;
		this.interactionType = interactionType;
		this.author = author;
		this.pubmedId = pubmedId2;
		this.biogridPublicationId = biogridPublicationId;
		this.biogridChemicalId = biogridChemicalId;
		this.chemicalName = chemicalName;
		this.chemicalSynonyms = chemicalSynonyms;
		this.chemicalBrands = chemicalBrands;
		this.chemicalSource = chemicalSource;
		this.chemicalSourceId = chemicalSourceId;
		this.molecularFormula = molecularFormula;
		this.chemicalType = chemicalType;
		this.atcCodes = atcCodes2;
		this.casNumber = casNumber;
		this.curatedBy = curatedBy;
		this.method = method;
		this.methodDescription = methodDescription;
		this.relatedBiogridGeneId = relatedBioGridId;
		this.relatedNcbiGeneId = relatedNcbiGeneId;
		this.relatedOfficialSymbol = relatedOfficialSymbol;
		this.relatedOrganismName = relatedOrganismName;
		this.relatedOrganismId = relatedOrganismId;
		this.relatedSynonyms = relatedSynonyms;
		this.relatedSystematicName = relatedSystematicName;
		this.relatedType = relatedType;
		this.notes = notes;
	}

	public static BioGridProteinChemicalInteractionFileData parseLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;
		String tmpStr;
		BioGridInteractionId biogridInteractionId = new BioGridInteractionId(toks[index++]);
		DataSourceIdentifier<?> biogridGeneId = resolveId(DataSource.BIOGRID, toks[index++]);
		DataSourceIdentifier<?> ncbiGeneId = resolveId(DataSource.NCBI_GENE, toks[index++]);
		tmpStr = toks[index++];
		String systematicName = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String officialSymbol = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		List<String> synonyms = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		DataSourceIdentifier<?> organismId = resolveId(DataSource.NCBI_TAXON, toks[index++]);
		tmpStr = toks[index++];
		String organism = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String action = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String interactionType = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String author = (tmpStr.trim().equals("-")) ? null : tmpStr;
		DataSourceIdentifier<?> pubmedId = resolveId(DataSource.PM, toks[index++]);
		tmpStr = toks[index++];
		BioGridPublicationId biogridPublicationId = (tmpStr.trim().equals("-")) ? null
				: new BioGridPublicationId(tmpStr);
		tmpStr = toks[index++];
		BioGridChemicalId biogridChemicalId = (tmpStr.trim().equals("-")) ? null : new BioGridChemicalId(tmpStr);
		tmpStr = toks[index++];
		String chemicalName = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		List<String> chemicalSynonyms = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		List<String> chemicalBrands = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		String chemicalSource = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		System.out.println("tmp str: " + tmpStr + ";;" + " chemical source = " + chemicalSource);
		DataSourceIdentifier<?> chemicalId = (tmpStr.trim().equals("-")) ? null
				: resolveId((chemicalSource.equals("DRUGBANK") ? DataSource.DRUGBANK : (chemicalSource.equals("CHEMSPIDER") ? DataSource.CHEMSPIDER : null)), tmpStr);
		tmpStr = toks[index++];
		String molecularFormula = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String chemicalType = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		Set<DataSourceIdentifier<?>> atcCodes = (tmpStr.trim().equals("-")) ? null
				: resolveIds(DataSource.WHOCC, tmpStr);
		tmpStr = toks[index++];
		ChemicalAbstractsServiceId casNumber = (tmpStr.trim().equals("-")) ? null
				: new ChemicalAbstractsServiceId(tmpStr);
		tmpStr = toks[index++];
		String curatedBy = (tmpStr.trim().equals("-")) ? null : tmpStr;

		tmpStr = toks[index++];
		String method = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String methodDescription = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		BioGridID relatedBioGridId = (tmpStr.trim().equals("-")) ? null : new BioGridID(tmpStr);
		tmpStr = toks[index++];
		NcbiGeneId relatedNcbiGeneId = (tmpStr.trim().equals("-")) ? null : new NcbiGeneId(tmpStr);
		tmpStr = toks[index++];
		String relatedSystematicName = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String relatedOfficialSymbol = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		List<String> relatedSynonyms = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		NcbiTaxonomyID relatedOrganismId = (tmpStr.trim().equals("-")) ? null : new NcbiTaxonomyID(tmpStr);
		tmpStr = toks[index++];
		String relatedOrganismName = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String relatedType = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String notes = (tmpStr.trim().equals("-")) ? null : tmpStr;

		return new BioGridProteinChemicalInteractionFileData(biogridInteractionId, biogridGeneId, ncbiGeneId,
				systematicName, officialSymbol, synonyms, organismId, organism, action, interactionType, author,
				pubmedId, biogridPublicationId, biogridChemicalId, chemicalName, chemicalSynonyms, chemicalBrands,
				chemicalSource, chemicalId, molecularFormula, chemicalType, atcCodes, casNumber, curatedBy, method,
				methodDescription, relatedBioGridId, relatedNcbiGeneId, relatedSystematicName, relatedOfficialSymbol,
				relatedSynonyms, relatedOrganismId, relatedOrganismName, relatedType, notes, line.getByteOffset(),
				line.getLineNumber());
	}

	private static DataSourceIdentifier<?> resolveId(DataSource ds, String idStr) {
		try {
			switch (ds) {
			case BIOGRID:
				return new BioGridInteractorId(idStr);
			case CHEMSPIDER:
				return new ChemSpiderId(idStr);
			case DRUGBANK:
				return new DrugBankID(idStr);
			case NCBI_GENE:
				return new NcbiGeneId(idStr);
			case NCBI_TAXON:
				return new NcbiTaxonomyID(idStr);
			case PM:
				return new PubMedID(idStr);
			case UNIPROT:
				if (idStr.contains("-")) {
					return new UniProtIsoformID(idStr);
				} else {
					return new UniProtID(idStr);
				}
			default:
				throw new IllegalStateException("Cannot create identifier. Unhandled data source: " + ds.name());
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, ds.name(), e.getMessage());
		}
	}

	private static Set<DataSourceIdentifier<?>> resolveIds(DataSource ds, String idStr) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		for (String id : idStr.split("\\|")) {
			try {
				switch (ds) {
				case WHOCC:
					ids.add(new AtcCode(id));
					break;
				default:
					throw new IllegalStateException("Cannot create identifier. Unhandled data source: " + ds.name());
				}
			} catch (IllegalArgumentException e) {
				ids.add(new ProbableErrorDataSourceIdentifier(idStr, ds.name(), e.getMessage()));
			}
		}
		return ids;
	}

}
