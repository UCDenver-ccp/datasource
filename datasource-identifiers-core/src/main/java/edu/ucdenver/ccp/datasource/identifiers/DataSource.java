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
	BIOGRID,
	KABOB,
	CCP,
	CGNC,
	DBJ,
	DBSNP,
	DICTYBASE,
	DIP,
	DOI,
	/** drugs product database */
	DPD,
	DRUGBANK,
	/** drug code directory */
	DRUGCODEDIRECTORY,
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
	NCBI_TAXON,
	OBO,
	OMIM,
	OWL,
	PANTHER,
	PATHEMA,
	PBR,
	PDB,
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
	PUBCHEM_BIOASSAY, CHEMICAL_ABSTRACTS_SERVICE;

	public static EnumSet<DataSource> GENE_OR_GENE_PRODUCT_SOURCES = EnumSet.of(EG, UNIPROT, MGI, HGNC, HPRD, REFSEQ,
			DIP, IREFWEB, EMBL, PR, PHARMGKB);

	public static boolean isDataSource(String input) {
		try {
			DataSource.valueOf(input);
			return true;
		} catch (NullPointerException npe) {
			return false;
		}
	}
}
