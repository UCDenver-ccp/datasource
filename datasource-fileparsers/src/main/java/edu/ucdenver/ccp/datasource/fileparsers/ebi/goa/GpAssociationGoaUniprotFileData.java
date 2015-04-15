package edu.ucdenver.ccp.fileparsers.ebi.goa;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.fileparsers.field.DatabaseName;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * @see ftp://ftp.ebi.ac.uk/pub/databases/GO/goa/UNIPROT/gp_association_readme
 * 
 * @author bill
 * 
 */
@Record(dataSource = DataSource.GOA,
		comment="EBI-GOA,  see ftp://ftp.ebi.ac.uk/pub/databases/GO/goa/UNIPROT/gp_association_readme",
		citation="Dimmer EC , Huntley RP , Alam-Faruque Y , Sawford T , O'Donovan C , Martin MJ et al. (2012) The UniProt-GO Annotation database in 2011.  Nucleic Acids Research 2012 40: D565-D570. ",
		license=License.EBI,
		label="GOA record")
@Data
public class GpAssociationGoaUniprotFileData extends SingleLineFileRecord {

	@RecordField(comment="Database from which annotated entry has been taken. This file will contain one of either: UniProtKB or IPI. This field equates to column 1 in the GAF2.0 format.")
	private final DatabaseName database;

	@RecordField(comment="A unique identifier in the database for the item being annotated. Here: an accession number or identifier of the annotated protein a UniProtKB accession number or IPI identifier Example: O00165 This field equates to column 2 in the GAF2.0 format.")
	private final DataSourceIdentifier<?> databaseObjectID;

	@RecordField(comment="This column is used for flags that modify the interpretation of an annotation. If not null, then values in this field can equal: NOT, colocalizes_with, contributes_to, NOT | contributes_to, NOT | colocalizes_with Example: NOT This field equates to column 4 of the GAF2.0 format")
	private final String qualifier;

	@RecordField(comment="http://www.ebi.ac.uk/about/terms-of-use")
	private final GeneOntologyID goID;

	@RecordField(comment="A single reference cited to support an annotation. Where an annotation cannot reference a paper, this field will contain a GO_REF identifier. See: http://www.geneontology.org/doc/GO.references for an explanation of the reference types used.Examples: PMID:9058808 DOI:10.1046/j.1469-8137.2001.00150.x GO_REF:0000002  GO_REF:0000020 GO_REF:0000004 GO_REF:0000003 GO_REF:0000019 GO_REF:0000023  GO_REF:0000024  This field equates to column 6 in the GAF2.0 format.")
	private final DataSourceIdentifier<?> dbReference;

	@RecordField(comment="One of either EXP, IMP, IC, IGI, IPI, ISS, IDA, IEP, IEA, TAS, NAS, NR, ND or RCA. Example: TAS  This field equates to column 7 in the GAF2.0 format.")
	private final String evidenceCode;

	@RecordField(comment="An additional identifier to support annotations using certain evidence codes (including IEA, IPI, IGI, IC and ISS evidences). Examples: UniProtKB:O00341 InterPro:IPROO1878 Ensembl:ENSG00000136141 GO:0000001 EC:3.1.22.1 This field equates to column 8 in the GAF2.0 format")
	private final String with;

	@RecordField(comment="This taxon id should inform on the other organism involved in an multi-species interaction. An extra taxon id can only be used in conjunction with terms that have the biological process term 'GO:0051704; multi-organism process' or the cellular component term 'GO:0043657; host cell' as an ancestor. This taxon id should inform on the other organism involved in the interaction. For further information please see: http://www.geneontology.org/GO.annotation.conventions.shtml#interactions This field has been separated from the dual taxon id format allowed in the taxon (column 13 in GAF2.0 format).")
	private final NcbiTaxonomyID extraTaxonID;

	@RecordField(comment="The date of last annotation update in the format 'YYYYMMDD' Example: 20050101 This field equates to column 14 in the GAF2.0 format.")
	private final String date;

	@RecordField(comment="Attribute describing the source of the annotation. One of either UniProtKB, AgBase, BHF-UCL, CGD, DictyBase, EcoCyc, EcoWiki, Ensembl, FB, GDB, GeneDB, GR (Gramene), HGNC, Human Protein Atlas, LIFEdb, MGI, PAMGO_GAT, Reactome, RGD, Roslin Institute, SGD, TAIR, TIGR, ZFIN, IntAct, PINC (Proteome Inc.) or WormBase. Example: UniProtKB This field equates to column 15 in the GAF2.0 format. ")
	private final String assignedBy;

	@RecordField(comment="(N.B. Until annotation practices are finalised, this column will remain empty) Contains cross references to other ontologies/databases that can be used to qualify or enhance the annotation. The cross-reference is prefaced by an appropriate GO relationship; references to multiple ontologies can be entered. Example: part_of(CL:0000084) occurs_in(GO:0009536) has_input(CHEBI:15422) has_output(CHEBI:16761) has_participant(UniProtKB:Q08722) part_of(CL:0000017)|part_of(MA:0000415) This field equates to column 16 in the GAF2.0 format.")
	private final String annotationExtension;

	@RecordField(comment="The fully-qualified unique identifier of a specific spliceform of the protein described in column 2 (DB_Object_ID) Example:UniProtKB:O43526-1  This field equates to column 17 in the GAF2.0 format.")
	private final String annotationProperties;




	public GpAssociationGoaUniprotFileData(DatabaseName database, DataSourceIdentifier<?> databaseObjectID,
			String qualifier, GeneOntologyID goID, DataSourceIdentifier<?> dbReference,
			String evidenceCode, String with, NcbiTaxonomyID extraTaxonID, String date,
			String assignedBy, String annotationExtension, String annotationProperties,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.database = database;
		this.databaseObjectID = databaseObjectID;
		this.qualifier = qualifier;
		this.goID = goID;
		this.dbReference = dbReference;
		this.evidenceCode = evidenceCode;
		this.with = with;
		this.extraTaxonID = extraTaxonID;
		this.date = date;
		this.assignedBy = assignedBy;
		this.annotationExtension = annotationExtension;
		this.annotationProperties = annotationProperties;
	}

	
}
