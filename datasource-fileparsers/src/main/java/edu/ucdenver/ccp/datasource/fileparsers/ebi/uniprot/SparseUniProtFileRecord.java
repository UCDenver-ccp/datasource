/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uniprot.DbReferenceType;
import org.uniprot.Entry;
import org.uniprot.OrganismType;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtFileRecord.DbReference;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtFileRecord.Organism;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import lombok.Getter;

/**
 * Useful for parsing trembl and ignoring most of it
 * ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/docs/dbxref.
 * txt
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Getter
@Record(ontClass = CcpExtensionOntology.SPARSE_UNIPROT_RECORD, dataSource = DataSource.UNIPROT, label = "uniprot record")
public class SparseUniProtFileRecord extends FileRecord {

	private static final Logger logger = Logger.getLogger(SparseUniProtFileRecord.class);

	@RecordField(ontClass = CcpExtensionOntology.SPARSE_UNIPROT_RECORD___PRIMARY_ACCESSION_FIELD_VALUE, isKeyField = true)
	private final UniProtID primaryAccession;
	@RecordField(ontClass = CcpExtensionOntology.SPARSE_UNIPROT_RECORD___ACCESSION_FIELD_VALUE)
	private final List<UniProtID> accession;
	@RecordField(ontClass = CcpExtensionOntology.SPARSE_UNIPROT_RECORD___NAME_FIELD_VALUE)
	private final List<String> name;
	// @RecordField
	// private final Protein protein;
	// @RecordField
	// private final List<Gene> gene;
	@RecordField(ontClass = CcpExtensionOntology.SPARSE_UNIPROT_RECORD___ORGANISM_FIELD_VALUE)
	private final Organism organism;
	@RecordField(ontClass = CcpExtensionOntology.SPARSE_UNIPROT_RECORD___ORGANISM_HOST_FIELD_VALUE)
	private final List<Organism> organismHost;
	// @RecordField
	// private final List<GeneLocation> geneLocation;
	// @RecordField
	// private final List<Reference> reference;
	// @RecordField
	// private final List<Comment> comment;
	@RecordField(ontClass = CcpExtensionOntology.SPARSE_UNIPROT_RECORD___DATABASE_REFERENCE_FIELD_VALUE)
	private final List<DbReference> dbReference;

	// @RecordField
	// private final ProteinExistence proteinExistence;
	// @RecordField
	// private final List<Keyword> keyword;
	// @RecordField
	// private final List<Feature> feature;
	// @RecordField
	// private final List<Evidence> evidence;
	// @RecordField
	// private final Sequence sequence;
	// @RecordField
	// private final String dataset;
	// @RecordField
	// private final XMLGregorianCalendar created;
	// @RecordField
	// private final XMLGregorianCalendar modified;
	// @RecordField
	// private final int version;

	/**
	 * @param byteOffset
	 */
	public SparseUniProtFileRecord(Entry xmlType) {
		super(-1); // b/c this data is coming from XML there's no easy way to track the byte offset
		this.accession = new ArrayList<UniProtID>();
		for (String idStr : xmlType.getAccession()) {
			this.accession.add(new UniProtID(idStr));
		}
		this.primaryAccession = this.accession.get(0);
		this.name = new ArrayList<String>(xmlType.getName());

		this.organism = new Organism(xmlType.getOrganism());
		this.organismHost = new ArrayList<Organism>();
		if (xmlType.getOrganismHost() != null) {
			for (OrganismType org : xmlType.getOrganismHost()) {
				this.organismHost.add(new Organism(org));
			}
		}

		this.dbReference = new ArrayList<DbReference>();
		if (xmlType.getDbReference() != null) {
			for (DbReferenceType ref : xmlType.getDbReference()) {
				this.dbReference.add(new DbReference(ref));
			}
		}
	}

	/**
	 * @param byteOffset
	 * @param primaryAccession
	 * @param accession
	 * @param name
	 * @param organism
	 * @param organismHost
	 * @param dbReference
	 */
	public SparseUniProtFileRecord(UniProtID primaryAccession, List<UniProtID> accession, List<String> name,
			Organism organism, List<Organism> organismHost, List<DbReference> dbReference, long byteOffset) {
		super(byteOffset);
		this.primaryAccession = primaryAccession;
		this.accession = accession;
		this.name = name;
		this.organism = organism;
		this.organismHost = organismHost;
		this.dbReference = dbReference;
	}

}
