package edu.ucdenver.ccp.datasource.fileparsers;

import java.net.URI;
import java.net.URISyntaxException;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
* This class is automatically generated from the CCP ontology.
*
*/
public enum CcpExtensionOntology {
	/* Annotation Properties */
	DOWNLOAD_DATE("DC_EXT_0000000"),
	FILE_SIZE_BYTES("DC_EXT_0000002"),
	LAST_MODIFIED_DATE("DC_EXT_0000001"),

	IDENTIFIER_OF_UNKNOWN_ORIGIN("IAO_EXT_0000111"),
	PROTEIN_ONTOLOGY_CONCEPT_IDENTIFIER("IAO_EXT_0000112"),
	
	/* Object Properties */
	/* Classes */
	ALTERNATE_ID_FIELD_VALUE("IAO_EXT_0000071"),
	CHROMOSOME_FIELD_VALUE("IAO_EXT_0000049"),
	CONCEPT_NAME_FIELD_VALUE("IAO_EXT_0000033"),
	DATABASE_CROSS_REFERENCE_FIELD_VALUE("IAO_EXT_0000090"),
	DATA_SOURCE_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000020"),
	DATE_APPROVED_FIELD_VALUE("IAO_EXT_0000051"),
	DATE_CREATED_FIELD_VALUE("IAO_EXT_0000066"),
	DATE_FIELD_VALUE("IAO_EXT_0000050"),
	DATE_MODIFIED_FIELD_VALUE("IAO_EXT_0000052"),
	DATE_NAME_CHANGED_FIELD_VALUE("IAO_EXT_0000054"),
	DATE_SYMBOL_CHANGED_FIELD_VALUE("IAO_EXT_0000053"),
	DESCRIPTION_FIELD_VALUE("IAO_EXT_0000091"),
	DNA_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000030"),
	FULL_NAME_FROM_NOMENCLATURE_AUTHORITY_FIELD_VALUE("IAO_EXT_0000094"),
	GAF_ANNOTATION_ASPECT_FIELD_VALUE("IAO_EXT_0000018"),
	GAF_ANNOTATION_EXTENSION_FIELD_VALUE("IAO_EXT_0000027"),
	GAF_ANNOTATION_WITH_OR_FROM_FIELD_VALUE("IAO_EXT_0000017"),
	GAF_ASSIGNED_BY_FIELD_VALUE("IAO_EXT_0000026"),
	GAF_DATABASE_NAME_FIELD_VALUE("IAO_EXT_0000009"),
	GAF_DATABASE_OBJECT_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000010"),
	GAF_DATABASE_OBJECT_NAME_FIELD_VALUE("IAO_EXT_0000022"),
	GAF_DATABASE_OBJECT_SYMBOL_FIELD_VALUE("IAO_EXT_0000011"),
	GAF_DATABASE_OBJECT_SYNONYM_FIELD_VALUE("IAO_EXT_0000021"),
	GAF_DATABASE_OBJECT_TYPE_FIELD_VALUE("IAO_EXT_0000023"),
	GAF_DATABASE_REFERENCE_FIELD_VALUE("IAO_EXT_0000015"),
	GAF_DATE_FIELD_VALUE("IAO_EXT_0000025"),
	GAF_EVIDENCE_CODE_FIELD_VALUE("IAO_EXT_0000016"),
	GAF_GENE_PRODUCT_FORM_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000028"),
	GAF_ONTOLOGY_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000014"),
	GAF_QUALIFIER_FIELD_VALUE("IAO_EXT_0000013"),
	GAF_RECORD("IAO_EXT_0000006"),
	GAF_RECORD_FIELD_VALUE("IAO_EXT_0000019"),
	GAF_RECORD_V20("IAO_EXT_0000007"),
	GAF_TAXON_FIELD_VALUE("IAO_EXT_0000024"),
	GENE_ONTOLOGY_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000035"),
	GENE_OR_GENE_PRODUCT_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000087"),
	GENE_OR_GENE_PRODUCT_OR_VARIANT_IDENTIFIER("IAO_EXT_0000083"),
	GENE_TYPE_FIELD_VALUE("IAO_EXT_0000092"),
	HGNC_GENE_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000041"),
	HGNC_GENE_NAME_FIELD_VALUE("IAO_EXT_0000040"),
	HGNC_GENE_RECORD("IAO_EXT_0000055"),
	HGNC_GENE_SYMBOL_FIELD_VALUE("IAO_EXT_0000042"),
	HPO_GENE_ANNOTATION_RECORD("IAO_EXT_0000029"),
	HUMAN_PHENOTYPE_ONTOLOGY_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000036"),
	INTERACTOR_FIELD_VALUE("IAO_EXT_0000076"),
	INTERACTOR_TYPE_FIELD_VALUE("IAO_EXT_0000073"),
	INTERACTOR_TYPE_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000075"),
	INTERACTOR_TYPE_NAME_FIELD_VALUE("IAO_EXT_0000074"),
	IREFWEB_INTERACTION_SOURCE_DATABASE_RECORD("IAO_EXT_0000078"),
	IREFWEB_INTERACTOR_RECORD("IAO_EXT_0000065"),
	IREFWEB_INTERACTOR_TYPE_RECORD("IAO_EXT_0000072"),
	IREFWEB_RECORD("IAO_EXT_0000064"),
	IREFWEB_SOURCE_DATABASE_RECORD("IAO_EXT_0000068"),
	IREFWEB_SUBRECORD("IAO_EXT_0000070"),
	LOCUS_GROUP_FIELD_VALUE("IAO_EXT_0000045"),
	LOCUS_TAG_FIELD_VALUE("IAO_EXT_0000100"),
	LOCUS_TYPE_FIELD_VALUE("IAO_EXT_0000044"),
	MAP_LOCATION_FIELD_VALUE("IAO_EXT_0000101"),
	NAME_FIELD_VALUE("IAO_EXT_0000039"),
	NAME_SYNONYMS_FIELD_VALUE("IAO_EXT_0000048"),
	NCBI_DNA_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000031"),
	NCBI_GENE_IDENTIFIER("IAO_EXT_0000084"),
	NCBI_GENE_RECORD("IAO_EXT_0000086"),
	NCBI_RECORD("IAO_EXT_0000085"),
	NCBI_TAXONOMY_IDENTIFIER("IAO_EXT_0000082"),
	NOMENCLATURE_AUTHORITY_STATUS("IAO_EXT_0000096"),
	ONTOLOGY_CONCEPT_IDENTIFIER("IAO_EXT_0000088"),
	ONTOLOGY_CONCEPT_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000034"),
	OTHER_DESIGNATIONS_FIELD_VALUE("IAO_EXT_0000098"),
	OTHER_FIELD_VALUE("IAO_EXT_0000097"),
	PREVIOUS_NAME_FIELD_VALUE("IAO_EXT_0000046"),
	PREVIOUS_SYMBOL_FIELD_VALUE("IAO_EXT_0000038"),
	RECORD("IAO_EXT_0000000"),
	RECORD_FIELD_VALUE("IAO_EXT_0000001"),
	RECORD_SET("IAO_EXT_0000012"),
	RECORD_STATUS_FIELD_VALUE("IAO_EXT_0000043"),
	SOURCE_DATABASE_FIELD_VALUE("IAO_EXT_0000067"),
	SOURCE_DATABASE_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000079"),
	SOURCE_DATABASE_NAME_FIELD_VALUE("IAO_EXT_0000080"),
	STATUS_FIELD_VALUE("IAO_EXT_0000095"),
	SUBRECORD("IAO_EXT_0000077"),
	SYMBOL_FIELD_VALUE("IAO_EXT_0000032"),
	SYMBOL_FROM_NOMENCLATURE_AUTHORITY_FIELD_VALUE("IAO_EXT_0000093"),
	SYNONYMS_FIELD_VALUE("IAO_EXT_0000047"),
	TAXONOMY_IDENTIFIER("IAO_EXT_0000081"),
	TAXONOMY_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000089"),
	UNIPROT_RECORD("IAO_EXT_0000061"),
	UNIQUE_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000069"), ONTOLOGY_CONCEPT_MAPPING_TYPE("IAO_EXT_0000113"),
	PROTEIN_ONTOLOGY_IDENTIFIER_MAPPING_RECORD("IAO_EXT_0000115"), INVALID_IDENTIFIER("IAO_EXT_0000110"), MAPPED_DATA_SOURCE_IDENTIFIER_FIELD_VALUE("IAO_EXT_0000116")
;

	private final String id;

	private CcpExtensionOntology(String id) {
		this.id = id;
	}

	public String id() {
		return this.id;
	}

	public URI uri()  {
		try {
			return new URI(DataSource.CCP + id);
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

}