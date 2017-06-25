/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.format.gaf2;

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

import java.util.Calendar;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A representation of data records in the GAF 2.0 file format as described here:
 * 
 * @see "http://www.geneontology.org/GO.format.gaf-2_0.shtml"
 * 
 *      File columns:<br>
 *      1) Database Designation<br>
 *      2) DB Object ID<br>
 *      3) DB Object Symbol<br>
 *      4) Qualifier<br>
 *      5) Ontology Term ID<br>
 *      6) DB Reference IDs<br>
 *      7) Evidence Code<br>
 *      8) With (or) From<br>
 *      9) Aspect <br>
 *      10) DB Object Name <br>
 *      11) DB Object Synonyms<br>
 *      12) DB Object Type <br>
 *      13) Taxon IDs <br>
 *      14) Modification Date <br>
 *      15) Assigned By 16) Annotation Extension 17) Gene Product Form ID (e.g. isoform)
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Gaf2FileRecord extends SingleLineFileRecord {
	private static Logger logger = Logger.getLogger(Gaf2FileRecord.class);

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_DESIGNATION_FIELD_VALUE)
	private final String databaseDesignation;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> dbObjectId;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_SYMBOL_FIELD_VALUE)
	private final String dbObjectSymbol;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___QUALIFIER_FIELD_VALUE)
	private final String qualifier;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___ONTOLOGY_TERM_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> ontologyTermId;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_REFERENCE_ACCESSION_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> referenceAccessionIds;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___EVIDENCE_CODE_FIELD_VALUE)
	private final String evidenceCode;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___WITH_OR_FROM_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> withOrFrom;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___ASPECT_FIELD_VALUE)
	private final String aspect;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_NAME_FIELD_VALUE)
	private final String dbObjectName;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_SYNONYM_FIELD_VALUE)
	private final Set<String> dbObjectSynonyms;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_TYPE_FIELD_VALUE)
	private final String dbObjectType;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_TAXON_IDENTIFIER_TAXON_FIELD_VALUE)
	private final NcbiTaxonomyID dbObjectTaxonId;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___INTERACTING_TAXON_IDENTIFIER_TAXON_FIELD_VALUE)
	private final NcbiTaxonomyID interactingTaxonId;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATE_FIELD_VALUE)
	private final Calendar date;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___ASSIGNED_BY_FIELD_VALUE)
	private final String assignedBy;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___ANNOTATION_EXTENSION_FIELD_VALUE)
	private final Set<AnnotationExtension> annotationExtensions;

	@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___GENE_PRODUCT_FORM_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> geneProductFormId;

	/**
	 * @param databaseDesignation
	 * @param dbObjectId
	 * @param dbObjectSymbol
	 * @param qualifier
	 * @param ontologyTermId
	 * @param referenceAccessionIDs
	 * @param evidenceCode
	 * @param withOrFrom
	 * @param aspect
	 * @param dbObjectName
	 * @param dbObjectSynonyms
	 * @param dbObjectType
	 * @param dbObjectTaxonId
	 * @param interactingTaxonId
	 * @param date
	 * @param assignedBy
	 * @param annotationExtension
	 * @param byteOffset
	 * @param lineNumber
	 */

	public Gaf2FileRecord(String databaseDesignation, DataSourceIdentifier<?> dbObjectId, String dbObjectSymbol,
			String qualifier, DataSourceIdentifier<?> ontologyTermId,
			Set<DataSourceIdentifier<?>> referenceAccessionIDs, String evidenceCode,
			Set<DataSourceIdentifier<?>> withOrFrom, String aspect, String dbObjectName, Set<String> dbObjectSynonyms,
			String dbObjectType, NcbiTaxonomyID dbObjectTaxonId, NcbiTaxonomyID interactingTaxonId, Calendar date,
			String assignedBy, Set<AnnotationExtension> annotationExtensions,
			DataSourceIdentifier<?> geneProductFormId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.databaseDesignation = databaseDesignation;
		this.dbObjectId = dbObjectId;
		this.dbObjectSymbol = dbObjectSymbol;
		this.qualifier = qualifier;
		this.ontologyTermId = ontologyTermId;
		this.referenceAccessionIds = referenceAccessionIDs;
		this.evidenceCode = evidenceCode;
		this.withOrFrom = withOrFrom;
		this.aspect = aspect;
		this.dbObjectName = dbObjectName;
		this.dbObjectSynonyms = dbObjectSynonyms;
		this.dbObjectType = dbObjectType;
		this.dbObjectTaxonId = dbObjectTaxonId;
		this.interactingTaxonId = interactingTaxonId;
		this.date = date;
		this.assignedBy = assignedBy;
		this.annotationExtensions = annotationExtensions;
		this.geneProductFormId = geneProductFormId;
	}

	public Gaf2FileRecord(Gaf2FileRecord record) {
		super(record.getByteOffset(), record.getLineNumber());
		this.databaseDesignation = record.getDatabaseDesignation();
		this.dbObjectId = record.getDbObjectId();
		this.dbObjectSymbol = record.getDbObjectSymbol();
		this.qualifier = record.getQualifier();
		this.ontologyTermId = record.getOntologyTermId();
		this.referenceAccessionIds = record.getReferenceAccessionIds();
		this.evidenceCode = record.getEvidenceCode();
		this.withOrFrom = record.getWithOrFrom();
		this.aspect = record.getAspect();
		this.dbObjectName = record.getDbObjectName();
		this.dbObjectSynonyms = record.getDbObjectSynonyms();
		this.dbObjectType = record.getDbObjectType();
		this.dbObjectTaxonId = record.getDbObjectTaxonId();
		this.interactingTaxonId = record.getInteractingTaxonId();
		this.date = record.getDate();
		this.assignedBy = record.getAssignedBy();
		this.annotationExtensions = record.getAnnotationExtensions();
		this.geneProductFormId = record.getGeneProductFormId();
	}

	@Data
	@Record(dataSource=DataSource.GOA, ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_EXTENSION_RECORD)
	public static class AnnotationExtension {
		@RecordField(ontClass = CcpExtensionOntology.GOA_ANNOTATION_EXTENSION_RECORD___RELATION_FIELD_VALUE)
		private final String relation;
		@RecordField(ontClass = CcpExtensionOntology.GOA_ANNOTATION_EXTENSION_RECORD___ONTOLOGY_TERM_FIELD_VALUE)
		private final DataSourceIdentifier<?> ontologyTerm;
	}
	
}
