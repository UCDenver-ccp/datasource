package edu.ucdenver.ccp.datasource.fileparsers.cosmic;

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
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.cosmic.CosmicGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ec.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.other.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.other.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

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
@Record(dataSource = DataSource.COSMIC, schemaVersion = "2", comment = "Previous version of this record represented only a subset of the data in the HGNC download file. This version represents all data and includes the new \"gene family description\" column.", label = "COSMIC record")
public class CosmicCancerGeneCensusFileData extends SingleLineFileRecord {

	private static final Logger logger = Logger.getLogger(CosmicCancerGeneCensusFileData.class);

	// 0 Gene Symbol
	// 1 Name
	// 2 Entrez GeneId
	// 3 Genome Location
	// 4 Chr Band
	// 5 Somatic
	// 6 Germline
	// 7 Tumour Types (Somatic)
	// 8 Tumour Types (Germline)
	// 9 Cancer Syndrome
	// 10 Tissue Type
	// 11 Molecular Genetics
	// 12 Role in Cancer
	// 13 Mutation Types
	// 14 Translocation Partner
	// 15 Other Germline Mut
	// 16 Other Syndrome
	// 17 Synonyms

	@RecordField
	private final CosmicGeneSymbolID cosmicGeneSymbolID;
	@RecordField
	private final String cosmicGeneName;
	@RecordField
	private final EntrezGeneID entrezGeneID;
	@RecordField
	private final String genomeLocation;
	@RecordField
	private final String chromosomeBand;
	@RecordField
	private final String isSomatic;
	@RecordField
	private final String isGermline;
	@RecordField
	private final Set<String> somaticTumourTypes;
	@RecordField
	private final Set<String> germlineTumourTypes;
	@RecordField
	private final Set<String> cancerSyndrome;
	@RecordField
	private final Set<String> tissueTypes;
	@RecordField
	private final String molecularGenetics;
	@RecordField
	private final Set<String> roleInCancer;
	@RecordField
	private final Set<String> mutationTypes;
	@RecordField
	private final Set<String> translocationPartner;
	@RecordField
	private final Set<String> otherSyndromes;
	@RecordField
	private final Set<String> otherGermlineMutations;
	@RecordField
	private final Set<String> nameSynonyms;
	
	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param cosmicGeneSymbolID
	 * @param cosmicGeneName
	 * @param entrezGeneID
	 * @param genomeLocation
	 * @param chromosomeBand
	 * @param isSomatic
	 * @param isGermline
	 * @param somaticTumourTypes
	 * @param germlineTumourTypes
	 * @param cancerSyndrome
	 * @param tissueTypes
	 * @param molecularGenetics
	 * @param roleInCancer
	 * @param mutationTypes
	 * @param translocationPartner
	 * @param otherSyndromes
	 * @param otherGermlineMutations
	 * @param nameSynonyms
	 */
	
	public CosmicCancerGeneCensusFileData(CosmicGeneSymbolID cosmicGeneSymbolID, String cosmicGeneName, 
			EntrezGeneID entrezGeneID, String genomeLocation, String chromosomeBand, String isSomatic,
			String isGermline, Set<String> somaticTumourTypes, Set<String> germlineTumourTypes, 
			Set<String> cancerSyndrome, Set<String> tissueTypes, String molecularGenetics,
			Set<String> roleInCancer, Set<String> mutationTypes, Set<String> translocationPartner,
			Set<String> otherSyndromes, Set<String> otherGermlineMutations, Set<String> nameSynonyms,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.cosmicGeneSymbolID = cosmicGeneSymbolID;
		this.cosmicGeneName = cosmicGeneName;
		this.entrezGeneID = entrezGeneID;
		this.genomeLocation = genomeLocation;
		this.chromosomeBand = chromosomeBand;
		this.isSomatic = isSomatic;
		this.isGermline = isGermline;
		this.somaticTumourTypes = somaticTumourTypes;
		this.germlineTumourTypes = germlineTumourTypes;
		this.cancerSyndrome = cancerSyndrome;
		this.tissueTypes = tissueTypes;
		this.molecularGenetics = molecularGenetics;
		this.roleInCancer = roleInCancer;
		this.mutationTypes = mutationTypes;
		this.translocationPartner = translocationPartner;
		this.otherSyndromes = otherSyndromes;
		this.otherGermlineMutations = otherGermlineMutations;
		this.nameSynonyms = nameSynonyms;
		}

//	@Data
//	@Record(dataSource = DataSource.HGNC)
//	public static class SpecialistDbIdLinkPair {
//		@RecordField
//		private final DataSourceIdentifier<?> specialistDbId;
//		@RecordField
//		private final String specialistDbUrl;
//
//		/**
//		 * @param specialistDbId
//		 * @param specialistDbUrl
//		 */
//		public SpecialistDbIdLinkPair(DataSourceIdentifier<?> specialistDbId, String specialistDbUrl) {
//			super();
//			this.specialistDbId = specialistDbId;
//			this.specialistDbUrl = specialistDbUrl;
//		}
//	}
//
//	@Data
//	@Record(dataSource = DataSource.HGNC)
//	public static class GeneFamilyTagDescriptionPair {
//		@RecordField
//		private final String geneFamilyTag;
//		@RecordField
//		private final String geneFamilyDescription;
//
//		/**
//		 * @param geneFamilyTag
//		 * @param geneFamilyDescription
//		 */
//		public GeneFamilyTagDescriptionPair(String geneFamilyTag, String geneFamilyDescription) {
//			super();
//			this.geneFamilyTag = geneFamilyTag;
//			this.geneFamilyDescription = geneFamilyDescription;
//		}
//
//	}
//
//	@Data
//	@Record(dataSource = DataSource.HGNC)
//	public static class LocusSpecificDatabaseNameLinkPair {
//		@RecordField
//		private final String databaseName;
//		@RecordField
//		private final String link;
//
//		/**
//		 * @param databaseName
//		 * @param link
//		 */
//		public LocusSpecificDatabaseNameLinkPair(String databaseName, String link) {
//			super();
//			this.databaseName = databaseName;
//			this.link = link;
//		}
//
//	}

}
