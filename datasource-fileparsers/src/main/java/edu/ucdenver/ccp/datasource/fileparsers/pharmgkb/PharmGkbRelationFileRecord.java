package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

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


import java.util.Collection;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Data;

/**
 * File record capturing single line record from PharmGKB's diseases.tsv file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD,
		dataSource = DataSource.PHARMGKB,
		schemaVersion = "2", 
		license=License.PHARMGKB,
		licenseUri="http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf",
		comment = "data from PharmGKB's disease.tsv file",
		citation="M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417",
		label="relation record")
@Data
public class PharmGkbRelationFileRecord extends SingleLineFileRecord {

	private static String PHARMGKB_ID_PREFIX = "PA";
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___ENTITY_1_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> entity1Id;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___ENTITY_1_NAME_FIELD_VALUE)
	private final String entity1Name;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___ENTITY_1_TYPE_FIELD_VALUE)
	private final String entity1Type;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___ENTITY_2_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> entity2Id;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___ENTITY_2_NAME_FIELD_VALUE)
	private final String entity2Name;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___ENTITY_2_TYPE_FIELD_VALUE)
	private final String entity2Type;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___EVIDENCE_FIELD_VALUE)
	private final Set<String> evidence;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___ASSOCIATION_FIELD_VALUE)
	private final String association;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___PHARMACOKINETICS_FIELD_VALUE)
	private final String pk;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___PHARMACODYNAMICS_FIELD_VALUE)
	private final String pd;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_RELATION_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
	private final Collection<PubMedID> pmids;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param entity1Id
	 * @param entity1Name
	 * @param entity1Type
	 * @param entity2Id
	 * @param entity2Name
	 * @param entity2Type
	 * @param evidence
	 * @param association
	 * @param pk
	 * @param pd
	 * @param pmids
	 */
	public PharmGkbRelationFileRecord(Set<DataSourceIdentifier<?>> entity1Id, String entity1Name, String entity1Type,
			Set<DataSourceIdentifier<?>> entity2Id, String entity2Name, String entity2Type, Set<String> evidence,
			String association, String pk, String pd, Collection<PubMedID> pmids, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.entity1Id = entity1Id;
		this.entity1Name = entity1Name;
		this.entity1Type = entity1Type;
		this.entity2Id = entity2Id;
		this.entity2Name = entity2Name;
		this.entity2Type = entity2Type;
		this.evidence = evidence;
		this.association = association;
		this.pk = pk;
		this.pd = pd;
		this.pmids = pmids;
	}

	
}
