package edu.ucdenver.ccp.fileparsers.hgnc;

import edu.ucdenver.ccp.fileparsers.field.DataElementLiteral;

/**
 * <pre>
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
 * </pre>
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public class HgncLocusType extends DataElementLiteral<String> {

	public HgncLocusType(String locusType) {
		super(locusType);
	}

}
