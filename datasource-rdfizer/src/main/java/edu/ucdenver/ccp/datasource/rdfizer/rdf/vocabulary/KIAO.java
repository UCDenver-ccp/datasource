/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.vocabulary;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.rdfizer.rdf.RdfNamespace;
import edu.ucdenver.ccp.rdfizer.rdf.RdfUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum KIAO {

	MENTIONS_PROTOCOL("mentionsProtocol"),
	MENTIONS_DISEASE("mentionsDisease"),
	MENTIONS_GEOGRAPHIC_REGION("mentionsGeographicRegion"),
	RDF_RESOURCE_ANNOTATION("rdfResourceAnnotation"),
	RESOURCE_ANNOTATION("resourceAnnotation"),
	ANNOTATES("annotates"),
	ANNOTATION("annotation"), 
	ANNOTATOR("annotator"),  // creator of the annotation
	TEXT_SPAN("textSpan"),   // the selector that references  has_start_position etc. below
	HAS_START_POSITION("hasStartPosition"),
	HAS_END_POSITION("hasEndPosition"),
	HAS_EXACT("hasExact"),

    HAS_CREATION_DATE("hasCreationDate"),
    HAS_LOCAL_FILE_PATH("hasLocalFilePath"),
	HAS_KEY_PART("hasKeyPart"),
    SCHEMA("Schema"),
    DATAFIELD("DataField"),
    DATASET("DataSet"), // TODO: this should be changed to RecordSet
    DATARECORD("Record"),
    DATASOURCE("DataSource"),
    FIELDVALUE("FieldValue"),
    FIELD("Field"),
    HAS_TEMPLATE("hasTemplate"),
    SPECIFIES_TARGET_GRAPH("specifiesTargetGraph"),
    SPECIFIES_SOURCE("specifiesSourceFile"),
    
    
    HAS_SCORE("has_score"),
    HAS_RELIABILITY_SCORE("has_reliability_score"),
    HAS_AVG_SCORE("has_avg_score"),
    HAS_HANISCH_SCORE("has_hanisch_score"),
    LINKS("links")
	;

	private final String termName;

	private KIAO(String termName) {
		this.termName = termName;
	}

	public String termName() { return termName; }

	public URI uri() {
		return new URIImpl(RdfUtil.createUri(RdfNamespace.KIAO, termName).toString());
	}

}
