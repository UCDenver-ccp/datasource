package edu.ucdenver.ccp.datasource.fileparsers.hgnc;

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

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioparadigmsSlcId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CosmicId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HomeoDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HordeId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HumanCellDifferentiationMoleculeDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HumanKZNFGeneCatalogDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ImgtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IntermediateFilamentDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IupharId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.LncRnaDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.LocusSpecificMutationDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MamitTrnaDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MeropsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MiRBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OrphanetId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PseudogeneOrgId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RnaCentralId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SnoRnaBaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * See https://www.genenames.org/help/statistics-downloads for field definitions
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Record(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD, dataSource = DataSource.HGNC, schemaVersion = "2")
public class HgncDownloadFileData extends SingleLineFileRecord {

	// fields:
	// hgnc_id
	// symbol
	// name
	// locus_group
	// locus_type
	// status
	// location
	// location_sortable
	// alias_symbol
	// alias_name
	// prev_symbol
	// prev_name
	// gene_family
	// gene_family_id
	// date_approved_reserved
	// date_symbol_changed
	// date_name_changed
	// date_modified
	// entrez_id
	// ensembl_gene_id
	// vega_id
	// ucsc_id
	// ena
	// refseq_accession
	// ccds_id
	// uniprot_ids
	// pubmed_id
	// mgd_id
	// rgd_id
	// lsdb
	// cosmic
	// omim_id
	// mirbase
	// homeodb
	// snornabase
	// bioparadigms_slc
	// orphanet
	// pseudogene.org
	// horde_id
	// merops
	// imgt
	// iuphar
	// kznf_gene_catalog
	// mamit-trnadb
	// cd
	// lncrnadb
	// enzyme_id
	// intermediate_filament_db
	// rna_central_ids

	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE)
	private final HgncID hgncID;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_GENE_SYMBOL_FIELD_VALUE)
	private final HgncGeneSymbolID hgncGeneSymbol;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_GENE_NAME_FIELD_VALUE)
	private final String hgncGeneName;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___LOCUS_GROUP_FIELD_VALUE)
	private final String locusGroup;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___LOCUS_TYPE_FIELD_VALUE)
	private final String locusType;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___STATUS_FIELD_VALUE)
	private final String status;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___CYTOGENIC_LOCATION_FIELD_VALUE)
	private final String cytogenicLocation;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SORTABLE_CYTOGENIC_LOCATION_FIELD_VALUE)
	private final String cytogenicLocationSortable;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ALIAS_SYMBOL_FIELD_VALUE)
	private final Set<String> aliasSymbols;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ALIAS_NAME_FIELD_VALUE)
	private final Set<String> aliasNames;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PREVIOUS_SYMBOL_FIELD_VALUE)
	private final Set<String> previousSymbols;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PREVIOUS_NAME_FIELD_VALUE)
	private final Set<String> previousNames;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___GENE_FAMILY_FIELD_VALUE)
	private final String geneFamily;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___GENE_FAMILY_IDENTIFIER_FIELD_VALUE)
	private final String geneFamilyId;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_APPROVED_FIELD_VALUE)
	private final String dateApproved;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_SYMBOL_CHANGED_FIELD_VALUE)
	private final String dateSymbolChanged;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_NAME_CHANGED_FIELD_VALUE)
	private final String dateNameChanged;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_MODIFIED_FIELD_VALUE)
	private final String dateModified;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ENTREZ_GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId entrezGeneID;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ENSEMBL_GENE_IDENTIFIER_FIELD_VALUE)
	private final EnsemblGeneID ensemblGeneID;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___VEGA_IDENTIFIER_FIELD_VALUE)
	private final Set<VegaID> vegaIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___UCSC_GENOME_BROWSER_IDENTIFIER_FIELD_VALUE)
	private final UcscGenomeBrowserId ucscId;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___NUCLEOTIDE_ACCESSION_NUMBERS_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> accessionNumbers;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___REFSEQ_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> refseqIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___CCDS_IDENTIFIER_FIELD_VALUE)
	private final Set<CcdsId> ccdsIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___UNIPROT_IDENTIFIER_FIELD_VALUE)
	private final Set<UniProtID> uniprotIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
	private final Set<PubMedID> pubmedIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___MGI_IDENTIFIER_FIELD_VALUE)
	private final Set<MgiGeneID> mgiIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___RGD_IDENTIFIER_FIELD_VALUE)
	private final Set<RgdID> rgdIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___LOCUS_SPECIFIC_MUTATION_DATABASE_IDENTIFIER_FIELD_VALUE)
	private final Set<LocusSpecificMutationDbId> lsdbIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___COSMIC_IDENTIFIER_FIELD_VALUE)
	private final Set<CosmicId> cosmicIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___OMIM_IDENTIFIER_FIELD_VALUE)
	private final Set<OmimID> omimIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___MIRBASE_IDENTIFIER_FIELD_VALUE)
	private final Set<MiRBaseID> mirBaseIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HOMEOBOX_DATABASE_IDENTIFIER_FIELD_VALUE)
	private final Set<HomeoDbId> homeoBoxIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SNORNABASE_IDENTIFIER_FIELD_VALUE)
	private final Set<SnoRnaBaseId> snoRnaBaseIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___BIOPARADIGMS_SLC_TABLE_IDENTIFIER_FIELD_VALUE)
	private final Set<BioparadigmsSlcId> bioparadigmsSlcIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ORPHANET_IDENTIFIER_FIELD_VALUE)
	private final Set<OrphanetId> orphanetIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PSEUDOGENEORG_IDENTIFIER_FIELD_VALUE)
	private final Set<PseudogeneOrgId> pseudogeneOrgDbIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HORDE_IDENTIFIER_FIELD_VALUE)
	private final Set<HordeId> hordeIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___MEROPS_IDENTIFIER_FIELD_VALUE)
	private final Set<MeropsId> meropsIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___IMGT_IDENTIFIER_FIELD_VALUE)
	private final Set<ImgtID> imgtIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___IUPHAR_IDENTIFIER_FIELD_VALUE)
	private final Set<IupharId> iupharIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HUMAN_KZNF_GENE_CATALOG_IDENTIFIER_FIELD_VALUE)
	private final Set<HumanKZNFGeneCatalogDbId> kznfIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___MAMIT_TRNA_DATABASE_IDENTIFIER_FIELD_VALUE)
	private final Set<MamitTrnaDbId> mamitTrnaDbIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HUMAN_CELL_DIFFERENTIATION_MOLECULAR_DATABASE_IDENTIFIER_FIELD_VALUE)
	private final Set<HumanCellDifferentiationMoleculeDbId> humanCellDifferentiationMoleculeDbIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___INCRNA_DATABASE_IDENTIFIER_FIELD_VALUE)
	private final Set<LncRnaDbId> incRnaDbIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ENZYME_COMMISSION_NUMBERS_FIELD_VALUE)
	private final Set<EnzymeCommissionID> ecNumbers;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___INTERMEDIATE_FILAMENT_DATABASE_IDENTIFIER_FIELD_VALUE)
	private final Set<IntermediateFilamentDbId> intermediateFilamentDbIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___RNACENTRAL_IDENTIFIER_FIELD_VALUE)
	private final Set<RnaCentralId> rnaCentralIds;

	public HgncDownloadFileData(HgncID hgncID, HgncGeneSymbolID hgncGeneSymbol, String hgncGeneName, String locusGroup,
			String locusType, String status, String cytogenicLocation, String cytogenicLocationSortable,
			Set<String> aliasSymbols, Set<String> aliasNames, Set<String> previousSymbols, Set<String> previousNames,
			String geneFamily, String geneFamilyId, String dateApproved, String dateSymbolChanged,
			String dateNameChanged, String dateModified, NcbiGeneId entrezGeneID, EnsemblGeneID ensemblGeneID,
			Set<VegaID> vegaIDs, UcscGenomeBrowserId ucscId, Set<DataSourceIdentifier<?>> accessionNumbers,
			Set<DataSourceIdentifier<?>> refseqIDs, Set<CcdsId> ccdsIDs, Set<UniProtID> uniprotIds,
			Set<PubMedID> pubmedIDs, Set<MgiGeneID> mgiIDs, Set<RgdID> rgdIds, Set<LocusSpecificMutationDbId> lsdbIds,
			Set<CosmicId> cosmicIds, Set<OmimID> omimIds, Set<MiRBaseID> mirBaseIds, Set<HomeoDbId> homeoBoxIds,
			Set<SnoRnaBaseId> snoRnaBaseIds, Set<BioparadigmsSlcId> bioparadigmsSlcIds, Set<OrphanetId> orphanetIds,
			Set<PseudogeneOrgId> pseudogeneOrgDbIds, Set<HordeId> hordeIds, Set<MeropsId> meropsIds,
			Set<ImgtID> imgtIds, Set<IupharId> iupharIds, Set<HumanKZNFGeneCatalogDbId> kznfIds,
			Set<MamitTrnaDbId> mamitTrnaDbIds,
			Set<HumanCellDifferentiationMoleculeDbId> humanCellDifferentiationMoleculeDbIds,
			Set<LncRnaDbId> incRnaDbIds, Set<EnzymeCommissionID> ecNumbers,
			Set<IntermediateFilamentDbId> intermediateFilamentDbIds, Set<RnaCentralId> rnaCentralIds, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.hgncID = hgncID;
		this.hgncGeneSymbol = hgncGeneSymbol;
		this.hgncGeneName = hgncGeneName;
		this.locusGroup = locusGroup;
		this.locusType = locusType;
		this.status = status;
		this.cytogenicLocation = cytogenicLocation;
		this.cytogenicLocationSortable = cytogenicLocationSortable;
		this.aliasSymbols = aliasSymbols;
		this.aliasNames = aliasNames;
		this.previousSymbols = previousSymbols;
		this.previousNames = previousNames;
		this.geneFamily = geneFamily;
		this.geneFamilyId = geneFamilyId;
		this.dateApproved = dateApproved;
		this.dateSymbolChanged = dateSymbolChanged;
		this.dateNameChanged = dateNameChanged;
		this.dateModified = dateModified;
		this.entrezGeneID = entrezGeneID;
		this.ensemblGeneID = ensemblGeneID;
		this.vegaIDs = vegaIDs;
		this.ucscId = ucscId;
		this.accessionNumbers = accessionNumbers;
		this.refseqIDs = refseqIDs;
		this.ccdsIDs = ccdsIDs;
		this.uniprotIds = uniprotIds;
		this.pubmedIDs = pubmedIDs;
		this.mgiIDs = mgiIDs;
		this.rgdIds = rgdIds;
		this.lsdbIds = lsdbIds;
		this.cosmicIds = cosmicIds;
		this.omimIds = omimIds;
		this.mirBaseIds = mirBaseIds;
		this.homeoBoxIds = homeoBoxIds;
		this.snoRnaBaseIds = snoRnaBaseIds;
		this.bioparadigmsSlcIds = bioparadigmsSlcIds;
		this.orphanetIds = orphanetIds;
		this.pseudogeneOrgDbIds = pseudogeneOrgDbIds;
		this.hordeIds = hordeIds;
		this.meropsIds = meropsIds;
		this.imgtIds = imgtIds;
		this.iupharIds = iupharIds;
		this.kznfIds = kznfIds;
		this.mamitTrnaDbIds = mamitTrnaDbIds;
		this.humanCellDifferentiationMoleculeDbIds = humanCellDifferentiationMoleculeDbIds;
		this.incRnaDbIds = incRnaDbIds;
		this.ecNumbers = ecNumbers;
		this.intermediateFilamentDbIds = intermediateFilamentDbIds;
		this.rnaCentralIds = rnaCentralIds;
	}

}
