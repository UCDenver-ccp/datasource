/*
 Copyright (c) 2012, Regents of the University of Colorado
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
    list of conditions and the following disclaimer.
   
 * Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.
   
 * Neither the name of the University of Colorado nor the names of its 
    contributors may be used to endorse or promote products derived from this 
    software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.ucdenver.ccp.datasource.identifiers;

import java.util.EnumSet;

public enum DataSource implements IDataSource{
	/**
	 * Used to indicate "any" data source
	 */
	ANY,
	ANIMALQTLDB,
	APHIDBASE,
	APIDBCRYPTODB,
	BEEBASE,
	BEETLEBASE,
	BIND,
	BINDING_DB,
	BIOGRID,
	KABOB,
	CCP,
	CGNC,
	CHEMSPIDER,
	DAILYMED,
	DBJ,
	DDBJ,// DNA Data Bank of Japan
	DBSNP,
	DICTYBASE,
	DIP,
	DOI,
	/** drugs product database */
	DPD,
	DRUGBANK,
	/** drug code directory */
	DRUGCODEDIRECTORY,
	DRUG_PRODUCTS_DB,
	ECOCYC,
	ECOGENE,
	/** Entrez Gene */
	EG,
	ELSEVIER,
	EMB,
	EMBL,
	ENSEMBL,
	ENZYME_COMMISSION,
	FLYBASE,
	GAD,
	/**
	 * GenBank Nucleic Acid Sequence Database
	 */
	GENBANK,
	GENE3D,
	GO,
	GOA,
	GOA_REFERENCE,
	GO_EVIDENCE,
	GO_REFERENCE,
	GOPAD,
	HAMAP,
	HGNC,
	HOMOLOGENE,
	HPRD,
	IAO,
	IMGT,
	/**
	 * International Molecular Exchange Consortium (http://www.imexconsortium.org/)
	 */
	IMEX,
	INTACT,
	INTERPRO,
	IPI,
	IREFWEB,
	IUPHAR_LIGAND,
	KEGG,
	MAMMALIAN_PHENOTYPE,
	MAIZEGDB,
	MESH,
	MGI,
	MGI_REFERENCE,
	MI_ONTOLOGY,
	MINT,
	MIRBASE,
	MPHENO,
	NASONIABASE,
	NATIONAL_DRUG_CODE_DIRECTORY,
	NCBI_TAXON,
	OBO,
	OMIM,
	OWL,
	PANTHER,
	PATHEMA,
	PBR,
	PDB,
	PDB_LIGAND,
	PHARMGKB,
	PFAM,
	PII,
	PIR,
	PIRSF,
	PM,
	PMC,
	PREMOD,
	PRINTS,
	PRODOM,
	PROSITE,
	PSEUDOCAP,
	RATMAP,
	REACTOME,
	REFSEQ,
	REFSNP,
	RGD,
	SGD,
	SMART,
	SO,
	SUPERFAM,
	TAIR,
	TIGRFAMS,
	TRANSFAC,
	UNCHAR_PFAM,
	UNIGENE,
	UNIPARC,
	UNIPROT,
	VBRC,
	VECTORBASE,
	WIKIPEDIA,
	WORMBASE,
	XENBASE,
	ZFIN,
	/** relation ontology */
	RO,
	/** Protein Ontology - {@link http://pir.georgetown.edu/pro/} */ 
	PR,
	CHEBI,
	CL,
	MOD,
	/**
	 * 	Johns Hopkins University Genome Data Bank 
	 */
	GDB,
	CLINICAL_TRIALS_GOV,
	/**
	 * International Standard Randomised Controlled Trial Number
	 */
	ISRCTN,
	/**
	 * NLM's Gene Expression Omnibus
	 */
	GEO,
	PUBCHEM_SUBSTANCE,
	PUBCHEM_COMPOUND,
	PUBCHEM_BIOASSAY, CHEMICAL_ABSTRACTS_SERVICE, THERAPEUTIC_TARGETS_DB;

	public static EnumSet<DataSource> GENE_OR_GENE_PRODUCT_SOURCES = EnumSet.of(EG, UNIPROT, MGI, HGNC, HPRD, REFSEQ,
			DIP, IREFWEB, EMBL, PR, PHARMGKB);

	public static boolean isDataSource(String input) {
		try {
			DataSource.valueOf(input);
			return true;
		} 
		catch (NullPointerException e) {
			return false;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}
}
