package edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary;

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

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil;

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
		return new URIImpl(RdfUtil.createUri(DataSource.KIAO, termName).toString());
	}

}
