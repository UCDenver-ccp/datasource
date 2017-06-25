package edu.ucdenver.ccp.datasource.fileparsers.pro;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ProteinOntologyId;

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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Center for Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
@Record(ontClass= CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD, dataSource = DataSource.PR, license = License.PIR, citation = "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3013777/?tool=pubmed", label = "id mapping record")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProMappingRecord extends SingleLineFileRecord {

	@RecordField(ontClass = CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___PRO_IDENTIFIER_FIELD_VALUE)
	private ProteinOntologyId proteinOntologyId;

	@RecordField(ontClass = CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___MAPPING_TYPE_FIELD_VALUE)
	private String mappingType;

	@RecordField( ontClass = CcpExtensionOntology.PRO_IDENTIFIER_MAPPING_RECORD___TARGET_RECORD_IDENTIFIER_FIELD_VALUE)
	private DataSourceIdentifier<?> targetRecordId;

	/**
	 * Default constructor
	 * 
	 * @param recordID
	 *            pro ontology id
	 * @param targetRecordId
	 *            target id
	 * @param mappingType
	 *            mapping type
	 * @param byteOffset
	 *            file byte offset
	 * @param lineNumber
	 */
	public ProMappingRecord(ProteinOntologyId recordID, DataSourceIdentifier<?> targetRecordId, String mappingType,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.proteinOntologyId = recordID;
		this.targetRecordId = targetRecordId;
		this.mappingType = mappingType;
	}

}
