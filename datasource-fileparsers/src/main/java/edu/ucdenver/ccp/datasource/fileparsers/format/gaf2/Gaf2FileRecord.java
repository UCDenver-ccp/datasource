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

	@RecordField(comment = "refers to the database from which the identifier in DB object ID (column 2) is drawn. This field is mandatory, cardinality 1.")
	private final String databaseDesignation;

	@RecordField(comment = "a unique identifier from the database in DB (column 1) for the item being annotated. This field is mandatory, cardinality 1.")
	private final DataSourceIdentifier<?> dbObjectId;

	@RecordField(comment = "a (unique and valid) symbol to which DB object ID is matched. This field is mandatory, cardinality 1.")
	private final String dbObjectSymbol;

	@RecordField(comment = "flags that modify the interpretation of an annotation. This field is not mandatory; cardinality 0, 1, >1.")
	private final String qualifier;

	@RecordField(comment = "the ontology term identifier for the term attributed to the DB object ID. This field is mandatory, cardinality 1.")
	private final DataSourceIdentifier<?> ontologyTermId;

	@RecordField(comment = "one or more unique identifiers for a single source cited as an authority for the attribution of the GO ID to the DB object ID. This may be a literature reference or a database record. This field is mandatory, cardinality 1, >1.")
	private final Set<DataSourceIdentifier<?>> referenceAccessionIds;

	@RecordField(comment = "see the GO evidence code guide (http://www.geneontology.org/GO.evidence.shtml) for the list of valid evidence codes. This field is mandatory, cardinality 1.")
	private final String evidenceCode;

	@RecordField(comment = "This field is used to hold an additional identifier for annotations using certain evidence codes (IC, IEA, IGI, IPI, ISS). For example, it can identify another gene product to which the annotated gene product is similar (ISS) or interacts with (IPI). More information on the meaning of with or from column entries is available in the evidence code documentation (http://www.geneontology.org/GO.evidence.shtml) entries for the relevant codes. This field is not mandatory overall, but is required for some evidence codes (see below and the evidence code documentation for details); cardinality 0, 1, >1.")
	private final Set<DataSourceIdentifier<?>> withOrFrom;

	@RecordField(comment = "refers to the namespace or ontology to which the Ontology Term ID (column 5) belongs. This field is mandatory; cardinality 1.")
	private final String aspect;

	@RecordField(comment = "name of gene or gene product. This field is not mandatory, cardinality 0, 1 [white space allowed].")
	private final String dbObjectName;

	@RecordField(comment = "Gene_symbol [or other text. This field is not mandatory, cardinality 0, 1, >1 [white space allowed].")
	private final Set<String> dbObjectSynonyms;

	@RecordField(comment = "A description of the type of gene product being annotated. This field is mandatory, cardinality 1.")
	private final String dbObjectType;

	@RecordField(comment = "the ID of the species encoding the gene product. This field is mandatory, cardinality 1.")
	private final NcbiTaxonomyID dbObjectTaxonId;

	@RecordField(comment = "to be used only in conjunction with terms that have the biological process term multi-organism process or the cellular component term host cell as an ancestor. The first taxon ID should be that of the organism encoding the gene or gene product, and the taxon ID after the pipe should be that of the other organism in the interaction. This field is optional.")
	private final NcbiTaxonomyID interactingTaxonId;

	@RecordField(comment = "date on which the annotation was made; format is YYYYMMDD. This field is mandatory, cardinality 1.")
	private final Calendar date;

	@RecordField(comment = "the database which made the annotation . This field is mandatory, cardinality 1.")
	private final String assignedBy;

	@RecordField(comment = "Contains cross references to other ontologies that can be used to qualify or enhance the annotation. The cross-reference is prefaced by an appropriate GO relationship; references to multiple ontologies can be entered. For example, if a gene product is localized to the mitochondria of lymphocytes, the GO ID (column 5) would be mitochondrion ; GO:0005439, and the annotation extension column would contain a cross-reference to the term lymphocyte from the Cell Type Ontology. This field is optional, cardinality 0 or greater.")
	private final Set<AnnotationExtension> annotationExtensions;

	@RecordField(comment = "As the DB Object ID (column 2) entry must be a canonical entity—a gene OR an abstract protein that has a 1:1 correspondence to a gene—this field allows the annotation of specific variants of that gene or gene product. Contents will frequently include protein sequence identifiers: for example, identifiers that specify distinct proteins produced by to differential splicing, alternative translational starts, post-translational cleavage or post-translational modification. Identifiers for functional RNAs can also be included in this column.")
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
	@Record(dataSource=DataSource.GOA)
	public static class AnnotationExtension {
		@RecordField
		private final String relation;
		@RecordField
		private final DataSourceIdentifier<?> ontologyTerm;
	}
	
}
