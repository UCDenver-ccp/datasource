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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *     *  HGNC ID  - A unique ID provided by the HGNC. In the HTML results page this ID links to the HGNC Symbol Report for that gene.
 * Approved Symbol - The official gene symbol that has been approved by the HGNC and is publicly available. Symbols are approved based on specific nomenclature guidelines. In the HTML results page this ID links to the HGNC Symbol Report for that gene.
 * Approved Name - The official gene name that has been approved by the HGNC and is publicly available. Names are approved based on specific nomenclature guidelines .
 * Status - Indicates whether the gene is classified as:
 *           o Approved - these genes have HGNC-approved gene symbols
 *           o Entry withdrawn - these previously approved genes are no longer thought to exist
 *           o Symbol withdrawn - a previously approved record that has since been merged into a another record
 * Locus Type - Specifies the type of locus described by the given entry:
 *           o complex locus constituent - transcriptional unit that is part of a named complex locus
 *           o endogenous retrovirus - integrated retroviral elements that are transmitted through the germline
 *           o fragile site - a heritable locus on a chromosome that is prone to DNA breakage
 *           o gene with protein product - protein-coding genes (the protein may be predicted and of unknown function)
 *           o gene with no protein product - long RNA-coding genes that do not fall into other specific categories
 *           o immunoglobulin gene - gene segments that undergo somatic recombination to form heavy or light chain immunoglobulin genes
 *           o immunoglobulin pseudogene - immunoglobulin gene segments that are inactivated due to frameshift mutations and/or stop codons in the open reading frame
 *           o phenotype only - mapped phenotypes
 *           o protocadherin - gene segments that constitute the three clustered protocadherins (alpha, beta and gamma)
 *           o pseudogene - genomic DNA sequences that are similar to protein-coding genes but do not encode a functional protein
 *           o RNA, pseudogene - pseudogene of a non-protein coding RNA
 *           o readthrough - a naturally occurring transcript containing coding sequence from two or more genes that can also be transcribed individually
 *           o region - extents of genomic sequence that contain one or more genes, also applied to non-gene areas that do not fall into other types
 *           o RNA, antisense - non-protein coding genes that encode RNA that is complementary to a messenger RNA
 *           o RNA, Cajal body-specific - non-protein coding genes that encode composite small nucleolar RNAs (snoRNAs) containing both C/D and H/ACA box domains
 *           o RNA, cluster - region containing a cluster of non-coding RNA genes
 *           o RNA, micro - non-protein coding genes that encode microRNAs (miRNAs)
 *           o RNA, RNase - non-protein coding genes that encode ribonuclease RNAs
 *           o RNA, ribosomal - non-protein coding genes that encode ribosomal RNAs (rRNAs)
 *           o RNA, small nuclear - non-protein coding genes that encode small nuclear RNAs
 *           o RNA, small nucleolar - non-protein coding genes that encode small nucleolar RNAs (snoRNAs) containing either C/D or H/ACA box domains
 *           o RNA, small cytoplasmic - non-protein coding genes that encode small cytoplasmic RNAs
 *           o RNA, telomerase - non-protein coding genes that encode a ribosomal component of telomerase
 *           o RNA, transfer - non-protein coding genes that encode transfer RNAs (tRNAs)
 *           o RNA, vault - non-protein coding genes that encode vault RNAs (vtRNAs)
 *           o T cell receptor gene - gene segments that undergo somatic recombination to form either alpha, beta, gamma or delta chain T cell receptor genes
 *           o T cell receptor pseudogene - T cell receptor gene segments that are inactivated due to frameshift mutations and/or stop codons in the open reading frame
 *           o transposable element - a segment of repetitive DNA that can move, or retrotranspose, to new sites within the genome
 *           o unknown - entries where the locus type is currently unknown
 *           o virus integration site - target sequence for the integration of viral DNA into the genome
 * Previous Symbols CD - Symbols previously approved by the HGNC for this gene
 * Previous Names QCD - Gene names previously approved by the HGNC for this gene
 * Aliases CD - Other symbols used to refer to this gene
 * Name Aliases QCD - Other names used to refer to this gene
 * Chromosome - Indicates the location of the gene or region on the chromosome
 * Date Approved - Date the gene symbol and name were approved by the HGNC
 * Date Modified - If applicable, the date the entry was modified by the HGNC
 * Date Symbol changed - If applicable, the date the gene symbol was last changed by the HGNC from a previously approved symbol. Many genes receive approved symbols and names which are viewed as temporary (eg C2orf#) or are non-ideal when considered in the light of subsequent information. In the case of individual genes a change to the name (and subsequently the symbol) is ony made if the original name is seriously misleading.
 * Date Name changed - If applicable, the date the gene name was last changed by the HGNC from a previously approved name
 * Accession Numbers CD - Accession numbers for each entry selected by the HGNC
 * Enzyme ID CD - Enzyme entries have Enzyme Commission (EC) numbers associated with them that indicate the hierarchical functional classes to which they belong
 * Entrez Gene ID - Entrez Gene at the NCBI provide curated sequence and descriptive information about genetic loci including official nomenclature, aliases, sequence accessions, phenotypes, EC numbers, MIM numbers, UniGene clusters, homology, map locations, and related web sites. In the HTML results page this ID links to the Entrez Gene page for that gene. Entrez Gene has replaced LocusLink.
 * CCDS ID The Consensus CDS (CCDS) project is a collaborative effort to identify a core set of human and mouse protein coding regions that are consistently annotated and of high quality. The long term goal is to support convergence towards a standard set of gene annotations.
 * VEGA IDs This contains curated VEGA gene IDs
 * Locus Specific Databases - This contains a list of links to databases or database entries pertinent to the gene
 * Mouse Genome Database ID - MGI identifier. In the HTML results page this ID links to the MGI Report for that gene.
 * Specialist Database Links CD - This column contains links to specialist databases with a particular interest in that symbol/gene (also see Specialist Database IDs).
 * Ensembl Gene ID - This column contains a manually curated Ensembl Gene ID
 * Specialist Database IDs CD - The Specialist Database Links column contains HTML links to the database in question. This column contains the database ID only. It is a comma delimited list with each position dedicated to a particular database:-
 *          1. miRBase the microRNA database
 *          2. HORDE ID Human Olfactory Receptor Data Exploratorium
 *          3. CD Human Cell Differentiation Antigens
 *          4. Rfam RNA families database of alignments and CMs
 *          5. snoRNABase database of human H/ACA and C/D box snoRNAs.
 *          6. KZNF Gene Catalog Human KZNF Gene Catalog
 *          7. Intermediate Filament DB Human Intermediate Filament Database
 *          8. IUPHAR Committee on Receptor Nomenclature and Drug Classification.(mapped)
 *          9. IMGT/GENE-DB the international ImMunoGeneTics information system for immunoglobulins (mapped)
 *         10. MEROPS the peptidase database
 *         11. COSMIC Catalogue Of Somatic Mutations In Cancer
 *         12. Orphanet portal for rare diseases and orphan drugs
 *         13. Pseudogene.org database of identified pseudogenes
 *         14. piRNABank database of piwi-interacting RNA clusters
 *       most of these IDs have undergone manual curation, however a few are mapped from regularly updated files kindly provided by the specialist database. When we add new databases these will be appended to the end of this list
 * Pubmed IDs CD - Identifier that links to published articles relevant to the entry in the NCBI's PubMed database .
 * RefSeq IDs CD - The Reference Sequence (RefSeq) identifier for that entry, provided by the NCBI. As we do not aim to curate all variants of a gene only one selected RefSeq is displayed per gene report. RefSeq aims to provide a comprehensive, integrated, non-redundant set of sequences, including genomic DNA, transcript (RNA), and protein products. RefSeq identifiers are designed to provide a stable reference for gene identification and characterization, mutation analysis, expression studies, polymorphism discovery, and comparative analyses. In the HTML results page this ID links to the RefSeq page for that entry.
 * Gene Family tag CD - Tag used to designate a gene family or group the gene has been assigned to, according to either sequence similarity or information from publications, specialist advisors for that family or other databases. Families/groups may be either structural or functional, therefore a gene may belong to more than one family/group. These tags are used to generate gene family or grouping specific pages at genenames.org and do not necessarily reflect an official nomenclature.
 * 
 * Mapped Data
 * 
 * Please note that mapped data are derived from external sources and as such are not subject to our strict checking and curation procedures. They should therefore be treated with some caution.
 * 
 * Mouse Genome Database ID (mapped data) - MGI identifier. In the HTML results page this ID links to the MGI Report for that gene.
 * Rat Genome Database ID (mapped data) - RGD identifier. In the HTML results page this ID links to the RGD Report for that gene.
 * GDB ID (mapped data) -(depreciated) GDB was a source of high quality mapping data which were made available both on-line as well as through numerous printed publications. What set GDB apart from other biological databases was its use of world-class leaders in human genetics to act as curators for the data. In order to ensure a high degree of quality, records within GDB were subjected to a process of peer-review, not unlike a traditional publication.
 * Entrez Gene ID (mapped data) - Entrez Gene at the NCBI provide curated sequence and descriptive information about genetic loci including official nomenclature, aliases, sequence accessions, phenotypes, EC numbers, MIM numbers, UniGene clusters, homology, map locations, and related web sites. In the HTML results page this ID links to the Entrez Gene page for that gene. Entrez Gene has replaced LocusLink.
 * OMIM ID (mapped data) - Identifier provided by Online Mendelian Inheritance in Man (OMIM) at the NCBI. This database is described as a catalog of human genes and genetic disorders containing textual information and refelinks to MEDLINE and sequence records in the Entrez system, and links to additional related resources at NCBI and elsewhere. In the HTML results page this ID links to the OMIM page for that entry.
 * RefSeq (mapped data) - The Reference Sequence (RefSeq) identifier for that entry, provided by the NCBI. As we do not aim to curate all variants of a gene only one mapped RefSeq is displayed per gene report. RefSeq aims to provide a comprehensive, integrated, non-redundant set of sequences, including genomic DNA, transcript (RNA), and protein products. RefSeq identifiers are designed to provide a stable reference for gene identification and characterization, mutation analysis, expression studies, polymorphism discovery, and comparative analyses. In the HTML results page this ID links to the RefSeq page for that entry.
 * UniProt ID (mapped data) - The UniProt identifier, provided by the EBI . The UniProt Protein Knowledgebase is described as a curated protein sequence database that provides a high level of annotation, a minimal level of redundancy and high level of integration with other databases. In the HTML results page this ID links to the UniProt page for that entry.
 * Ensembl Gene ID(mapped data) The Ensembl ID is derived from the current build of the Ensembl database and provided by the Ensembl team.
 * UCSC (mapped data) The UCSC ID is derived from the current build of the UCSC database
 * </pre>
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Record(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD, dataSource = DataSource.HGNC, schemaVersion = "2")
public class HgncDownloadFileData extends SingleLineFileRecord {

	private static final Logger logger = Logger.getLogger(HgncDownloadFileData.class);

	// 0 HGNC ID
	// 1 Approved Symbol
	// 2 Approved Name
	// 3 Status
	// 4 Locus Type
	// 5 Locus Group
	// 6 Previous Symbols
	// 7 Previous Names
	// 8 Synonyms
	// 9 Name Synonyms
	// 10 Chromosome
	// 11 Date Approved
	// 12 Date Modified
	// 13 Date Symbol Changed
	// 14 Date Name Changed
	// 15 Accession Numbers
	// 16 Enzyme IDs
	// 17 Entrez Gene ID
	// 18 Ensembl Gene ID
	// 19 Mouse Genome Database ID
	// 20 Specialist Database Links
	// 21 Specialist Database IDs
	// 22 Pubmed IDs
	// 23 RefSeq IDs
	// 24 Gene Family Tag
	// 25 Gene family description
	// 26 Record Type
	// 27 Primary IDs
	// 28 Secondary IDs
	// 29 CCDS IDs
	// 30 VEGA IDs
	// 31 Locus Specific Databases
	// 32 Entrez Gene ID (mapped data supplied by NCBI)
	// 33 OMIM ID (mapped data supplied by NCBI)
	// 34 RefSeq (mapped data supplied by NCBI)
	// 35 UniProt ID (mapped data supplied by UniProt)
	// 36 Ensembl ID (mapped data supplied by Ensembl)
	// 37 UCSC ID (mapped data supplied by UCSC)
	// 38 Mouse Genome Database ID (mapped data supplied by MGI)
	// 49 Rat Genome Database ID (mapped data supplied by RGD)

	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE)
	private final HgncID hgncID;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_GENE_SYMBOL_FIELD_VALUE)
	private final HgncGeneSymbolID hgncGeneSymbol;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_GENE_NAME_FIELD_VALUE)
	private final String hgncGeneName;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___STATUS_FIELD_VALUE)
	private final String status;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___LOCUS_TYPE_FIELD_VALUE)
	private final String locusType;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___LOCUS_GROUP_FIELD_VALUE)
	private final String locusGroup;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PREVIOUS_SYMBOL_FIELD_VALUE)
	private final Set<String> previousSymbols;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PREVIOUS_NAME_FIELD_VALUE)
	private final Set<String> previousNames;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SYNONYMS_FIELD_VALUE)
	private final Set<String> synonyms;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___NAME_SYNONYMS_FIELD_VALUE)
	private final Set<String> nameSynonyms;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___CHROMOSOME_FIELD_VALUE)
	private final String chromosome;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_APPROVED_FIELD_VALUE)
	private final String dateApproved;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_MODIFIED_FIELD_VALUE)
	private final String dateModified;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_SYMBOL_CHANGED_FIELD_VALUE)
	private final String dateSymbolChanged;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___DATE_NAME_CHANGED_FIELD_VALUE)
	private final String dateNameChanged;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ACCESSION_NUMBERS_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> accessionNumbers;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ENZYME_COMMISSION_NUMBERS_FIELD_VALUE)
	private final Set<EnzymeCommissionID> ecNumbers;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ENTREZ_GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId entrezGeneID;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___ENSEMBL_GENE_IDENTIFIER_FIELD_VALUE)
	private final EnsemblGeneID ensemblGeneID;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___MGI_IDENTIFIER_FIELD_VALUE)
	private final Set<MgiGeneID> mgiIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SPECIALIST_DATABASE_IDENTIFIER_LINK_PAIRINGS_FIELD_VALUE)
	private final Set<SpecialistDbIdLinkPair> specialistDatabaseIdLinkPairings;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
	private final Set<PubMedID> pubmedIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___REFSEQ_IDENTIFIER_FIELD_VALUE)
	private final Set<RefSeqID> refseqIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___GENE_FAMILY_TAG_DESCRIPTION_PAIRINGS_FIELD_VALUE)
	private final Set<GeneFamilyTagDescriptionPair> geneFamilyTagDescriptionPairings;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___RECORD_TYPE_FIELD_VALUE)
	private final String recordType;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___PRIMARY_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> primaryIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SECONDARY_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> secondaryIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___CCDS_IDENTIFIER_FIELD_VALUE)
	private final Set<CcdsId> ccdsIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___VEGA_IDENTIFIER_FIELD_VALUE)
	private final Set<VegaID> vegaIDs;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___LOCUS_SPECIFIC_DATABASE_NAME_LINK_PAIRINGS_FIELD_VALUE)
	private final Set<LocusSpecificDatabaseNameLinkPair> locusSpecificDatabaseNameLinkPairings;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_ENTREZ_GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId suppliedEntrezGeneId;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_OMIM_IDENTIFIER_FIELD_VALUE)
	private final Set<OmimID> suppliedOmimIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_REFSEQ_IDENTIFIER_FIELD_VALUE)
	private final RefSeqID suppliedRefseqId;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_UNIPROT_IDENTIFIER_FIELD_VALUE)
	private final Set<UniProtID> suppliedUniprotIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_ENSEMBL_IDENTIFIER_FIELD_VALUE)
	private final EnsemblGeneID suppliedEnsemblId;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_VEGA_IDENTIFIER_FIELD_VALUE)
	private final VegaID suppliedVegaId;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_UCSC_GENOME_BROWSER_IDENTIFIER_FIELD_VALUE)
	private final UcscGenomeBrowserId suppliedUcscId;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_MGI_IDENTIFIER_FIELD_VALUE)
	private final Set<MgiGeneID> suppliedMgiIds;
	@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___SUPPLIED_RGD_IDENTIFIER_FIELD_VALUE)
	private final Set<RgdID> suppliedRgdIds;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param hgncID
	 * @param hgncGeneSymbol
	 * @param hgncGeneName
	 * @param status
	 * @param locusType
	 * @param locusGroup
	 * @param previousSymbols
	 * @param previousNames
	 * @param synonyms
	 * @param nameSynonyms
	 * @param chromosome
	 * @param dateApproved
	 * @param dateModified
	 * @param dateSymbolChanged
	 * @param dateNameChanged
	 * @param accessionNumbers
	 * @param ecNumbers
	 * @param entrezGeneID
	 * @param ensemblGeneID
	 * @param mgiID
	 * @param specialistDatabaseIdLinkPairings
	 * @param pubmedIDs
	 * @param refseqIDs
	 * @param geneFamilyTagDescriptionPairings
	 * @param geneFamilyDescription
	 * @param recordType
	 * @param primaryIds
	 * @param secondaryIds
	 * @param ccdsIDs
	 * @param vegaIDs
	 * @param locusSpecificDatabaseNameLinkPairings
	 * @param suppliedEntrezGeneId
	 * @param suppliedOmimId
	 * @param suppliedRefseqId
	 * @param suppliedUniprotId
	 * @param suppliedEnsemblId
	 * @param suppliedUcscId
	 * @param suppliedMgiId
	 * @param suppliedRgdId
	 */
	public HgncDownloadFileData(HgncID hgncID, HgncGeneSymbolID hgncGeneSymbol, String hgncGeneName, String status,
			String locusType, String locusGroup, Set<String> previousSymbols, Set<String> previousNames,
			Set<String> synonyms, Set<String> nameSynonyms, String chromosome, String dateApproved,
			String dateModified, String dateSymbolChanged, String dateNameChanged,
			Set<DataSourceIdentifier<?>> accessionNumbers, Set<EnzymeCommissionID> ecNumbers,
			NcbiGeneId entrezGeneID, EnsemblGeneID ensemblGeneID, Set<MgiGeneID> mgiIDs,
			Set<SpecialistDbIdLinkPair> specialistDatabaseIdLinkPairings, Set<PubMedID> pubmedIDs,
			Set<RefSeqID> refseqIDs, Set<GeneFamilyTagDescriptionPair> geneFamilyTagDescriptionPairings,
			String recordType, Set<DataSourceIdentifier<?>> primaryIds, Set<DataSourceIdentifier<?>> secondaryIds,
			Set<CcdsId> ccdsIDs, Set<VegaID> vegaIDs,
			Set<LocusSpecificDatabaseNameLinkPair> locusSpecificDatabaseNameLinkPairings,
			NcbiGeneId suppliedEntrezGeneId, Set<OmimID> suppliedOmimIds, RefSeqID suppliedRefseqId,
			Set<UniProtID> suppliedUniprotIds, EnsemblGeneID suppliedEnsemblId, VegaID suppliedVegaId,
			UcscGenomeBrowserId suppliedUcscId, Set<MgiGeneID> suppliedMgiIds, Set<RgdID> suppliedRgdIds,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.hgncID = hgncID;
		this.hgncGeneSymbol = hgncGeneSymbol;
		this.hgncGeneName = hgncGeneName;
		this.status = status;
		this.locusType = locusType;
		this.locusGroup = locusGroup;
		this.previousSymbols = previousSymbols;
		this.previousNames = previousNames;
		this.synonyms = synonyms;
		this.nameSynonyms = nameSynonyms;
		this.chromosome = chromosome;
		this.dateApproved = dateApproved;
		this.dateModified = dateModified;
		this.dateSymbolChanged = dateSymbolChanged;
		this.dateNameChanged = dateNameChanged;
		this.accessionNumbers = accessionNumbers;
		this.ecNumbers = ecNumbers;
		this.entrezGeneID = entrezGeneID;
		this.ensemblGeneID = ensemblGeneID;
		this.mgiIDs = mgiIDs;
		this.specialistDatabaseIdLinkPairings = specialistDatabaseIdLinkPairings;
		this.pubmedIDs = pubmedIDs;
		this.refseqIDs = refseqIDs;
		this.geneFamilyTagDescriptionPairings = geneFamilyTagDescriptionPairings;
		this.recordType = recordType;
		this.primaryIds = primaryIds;
		this.secondaryIds = secondaryIds;
		this.ccdsIDs = ccdsIDs;
		this.vegaIDs = vegaIDs;
		this.locusSpecificDatabaseNameLinkPairings = locusSpecificDatabaseNameLinkPairings;
		this.suppliedEntrezGeneId = suppliedEntrezGeneId;
		this.suppliedOmimIds = suppliedOmimIds;
		this.suppliedRefseqId = suppliedRefseqId;
		this.suppliedUniprotIds = suppliedUniprotIds;
		this.suppliedEnsemblId = suppliedEnsemblId;
		this.suppliedVegaId = suppliedVegaId;
		this.suppliedUcscId = suppliedUcscId;
		this.suppliedMgiIds = suppliedMgiIds;
		this.suppliedRgdIds = suppliedRgdIds;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.HGNC_SPECIALIST_DATABASE_IDENTIFIER_LINK_PAIR_RECORD, dataSource = DataSource.HGNC)
	public static class SpecialistDbIdLinkPair {
		@RecordField(ontClass = CcpExtensionOntology.HGNC_SPECIALIST_DATABASE_LINK_PAIR_RECORD___DATA_SOURCE_IDENTIFIER_FIELD_VALUE)
		private final DataSourceIdentifier<?> specialistDbId;
		@RecordField(ontClass = CcpExtensionOntology.HGNC_SPECIALIST_DATABASE_LINK_PAIR_RECORD___SPECIALIST_DATABASE_URL_FIELD_VALUE)
		private final String specialistDbUrl;

		/**
		 * @param specialistDbId
		 * @param specialistDbUrl
		 */
		public SpecialistDbIdLinkPair(DataSourceIdentifier<?> specialistDbId, String specialistDbUrl) {
			super();
			this.specialistDbId = specialistDbId;
			this.specialistDbUrl = specialistDbUrl;
		}
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.HGNC_GENE_FAMILY_TAG_DESCRIPTION_PAIR_RECORD, dataSource = DataSource.HGNC)
	public static class GeneFamilyTagDescriptionPair {
		@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_FAMILY_TAG_DESCRIPTION_PAIR_RECORD___GENE_FAMILY_TAG_FIELD_VALUE)
		private final String geneFamilyTag;
		@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_FAMILY_TAG_DESCRIPTION_PAIR_RECORD___GENE_FAMILY_DESCRIPTION_FIELD_VALUE)
		private final String geneFamilyDescription;

		/**
		 * @param geneFamilyTag
		 * @param geneFamilyDescription
		 */
		public GeneFamilyTagDescriptionPair(String geneFamilyTag, String geneFamilyDescription) {
			super();
			this.geneFamilyTag = geneFamilyTag;
			this.geneFamilyDescription = geneFamilyDescription;
		}

	}

	@Data
	@Record(ontClass = CcpExtensionOntology.HGNC_LOCUS_SPECIFIC_DATABASE_NAME_LINK_PAIR_RECORD, dataSource = DataSource.HGNC)
	public static class LocusSpecificDatabaseNameLinkPair {
		@RecordField(ontClass = CcpExtensionOntology.HGNC_LOCUS_SPECIFIC_DATABASE_NAME_LINK_PAIR_RECORD___DATABASE_NAME_FIELD_VALUE)
		private final String databaseName;
		@RecordField(ontClass = CcpExtensionOntology.HGNC_LOCUS_SPECIFIC_DATABASE_NAME_LINK_PAIR_RECORD___LINK_FIELD_VALUE)
		private final String link;

		/**
		 * @param databaseName
		 * @param link
		 */
		public LocusSpecificDatabaseNameLinkPair(String databaseName, String link) {
			super();
			this.databaseName = databaseName;
			this.link = link;
		}

	}

}
