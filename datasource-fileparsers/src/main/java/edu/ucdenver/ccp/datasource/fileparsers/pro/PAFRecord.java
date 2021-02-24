package edu.ucdenver.ccp.datasource.fileparsers.pro;

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
import java.util.HashSet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.obo.ProteinOntologyId;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.PR, comment = "The PRO mapping from PRO IDs to other ontologies", license = License.PIR, citation = "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3013777/?tool=pubmed", label = "id mapping record")
@Data
@EqualsAndHashCode(callSuper = false)
public class PAFRecord extends SingleLineFileRecord {

	@RecordField(comment = "PR id")
	private ProteinOntologyId proteinOntologyId;

	@RecordField(comment = "Name in Protein Ontology")
	private String proteinOntologyName;

	@RecordField(comment = "Other name synonyms")
	private Set<String> nameSynonyms;

	@RecordField(comment = "Modifier of described relation")
	private String modifier;

	@RecordField(comment = "Relation")
	private String relationName;
	
	@RecordField(comment = "Relation's target ontology ID")
	private DataSourceIdentifier<?> relationOntologyId;
	
	@RecordField(comment = "Relation's target ontology term")
	private String relationOntologyTerm;
	
	@RecordField(comment = "Relative to some reference protein (if modifier is increased, decreased, altered")
	private DataSourceIdentifier<?> referenceProtein;
	
	@RecordField(comment = "Interaction partner of protein in this relation")
	private Set<DataSourceIdentifier<?>> interactionPartners;
	
	@RecordField(comment = "Evidence source for this relation")
	private Set<DataSourceIdentifier<?>> evidenceSource;
	
	@RecordField(comment = "Evidence code for this relation")
	private String evidenceCode;
	
	@RecordField(comment = "Taxon for this relation")
	private String taxon;
	
	@RecordField(comment = "Source inferring this relation")
	private Set<DataSourceIdentifier<?>> inferredFrom;
	
	@RecordField(comment = "Database for source inferring this relation")
	private Set<DataSourceIdentifier<?>> inferredFromDB;
	
	@RecordField(comment = "Date of this relation")
	private String date;
	
	@RecordField(comment = "Annotator for this relation")
	private String annotatorInitials;
	
	@RecordField(comment = "Comments on this relation")
	private String comment;
	
	
	/**
	 * Default constructor
	 * 
	 * @param proteinOntologyID
	 *            pro ontology id
	 * @param proteinOntologyName
	 *            name
	 * @param nameSynonyms
	 *            other names
	 * @param modifier
	 *            modifies relation
	 * @param relationName
	 *            name of relation
	 * @param relationOntologyId
	 *            ontology of thing related
	 * @param relationOntologyTerm
	 *            name of thing related
	 * @param referenceProtein
	 *            comparison
	 * @param interactionPartners
	 *            binding partners
	 * @param evidenceSource
	 *            source of evidence
	 * @param evidenceCode
	 *            describes evidence type
	 * @param taxon
	 *            in which species
	 * @param inferredFrom
	 *            source of evidence
	 * @param inferredFromDB
	 *            database for evidence source
	 * @param date
	 *            date annotation was created
	 * @param annotatorInitials
	 *            provenance of annotation
	 * @param comment
	 *            optional comment
	 * @param lineNumber
	 */
	public PAFRecord(ProteinOntologyId proteinOntologyId, String proteinOntologyName, Set<String> nameSynonyms,
			String modifier, String relationName, DataSourceIdentifier<?> relationOntologyId, String relationOntologyTerm,
			DataSourceIdentifier<?> referenceProtein, Set<DataSourceIdentifier<?>> interactionPartners, 
			Set<DataSourceIdentifier<?>> evidenceSource, String evidenceCode, String taxon, 
			Set<DataSourceIdentifier<?>> inferredFrom, Set<DataSourceIdentifier<?>> inferredFromDB,
			String date, String annotatorInitials, String comment,			
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.proteinOntologyId = proteinOntologyId;
		this.proteinOntologyName = proteinOntologyName;
		this.nameSynonyms = nameSynonyms;
		this.modifier = modifier;
		this.relationName = relationName;
		this.relationOntologyId = relationOntologyId;
		this.relationOntologyTerm = relationOntologyTerm;
		this.referenceProtein = referenceProtein;
		this.interactionPartners = interactionPartners;
		this.evidenceSource = evidenceSource;
		this.evidenceCode = evidenceCode;
		this.taxon = taxon;
		this.date = date;
		this.annotatorInitials = annotatorInitials;
		this.comment = comment;
		
	}

}
