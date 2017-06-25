package edu.ucdenver.ccp.datasource.fileparsers.irefweb;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

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
 * Data structure representing protein-protein interactions extracted from the PSI-MITAB 2.6 file
 * format (used by IRefWeb).
 * 
 * http://irefindex.uio.no/wiki/README_MITAB2.6_for_iRefIndex
 * ftp://ftp.no.embnet.org/irefindex/data/current/psimi_tab/MITAB2.6/
 * 
 * <pre>
 * Each line in this file represents a single source database record that supports either:
 *    1. an interaction between two proteins (binary interaction) or
 *    2. the membership of a protein in some complex (complex membership) or
 *    3. an interaction that involves only one protein type (multimer or self-interaction).
 * </pre>
 * 
 * <pre>
 *  Description of PSI-MITAB2.6 file
 *  Column number: 1 (uidA)
 *  Column number: 2 (uidB)
 *  Column number: 3 (altA)
 *  Column number: 4 (altB)
 *  Column number: 5 (aliasA)
 *  Column number: 6 (aliasB)
 *  Column number: 7 (Method)
 *  Column number: 8 (author)
 *  Column number: 9 (pmids)
 *  Column number: 10 (taxa)
 *  Column number: 11 (taxb)
 *  Column number: 12 (interactionType)
 *  Column number: 13 (sourcedb)
 *  Column number: 14 (interactionIdentifier)
 *  Column number: 15 (confidence)
 *  Column number: 16 (expansion)
 *  Column number: 17 (biological_role_A)
 *  Column number: 18 (biological_role_B)
 *  Column number: 19 (experimental_role_A)
 *  Column number: 20 (experimental_role_B)
 *  Column number: 21 (interactor_type_A)
 *  Column number: 22 (interactor_type_B)
 *  Column number: 23 (xrefs_A)
 *  Column number: 24 (xrefs_B)
 *  Column number: 25 (xrefs_Interaction)
 *  Column number: 26 (Annotations_A)
 *  Column number: 27 (Annotations_B)
 *  Column number: 28 (Annotations_Interaction)
 *  Column number: 29 (Host_organism_taxid)
 *  Column number: 30 (parameters_Interaction)
 *  Column number: 31 (Creation_date)
 *  Column number: 32 (Update_date)
 *  Column number: 33 (Checksum_A)
 *  Column number: 34 (Checksum_B)
 *  Column number: 35 (Checksum_Interaction)
 *  Column number: 36 (Negative)
 *  Column number: 37 (OriginalReferenceA)
 *  Column number: 38 (OriginalReferenceB)
 *  Column number: 39 (FinalReferenceA)
 *  Column number: 40 (FinalReferenceB)
 *  Column number: 41 (MappingScoreA)
 *  Column number: 42 (MappingScoreB)
 *  Column number: 43 (irogida)
 *  Column number: 44 (irogidb)
 *  Column number: 45 (irigid)
 *  Column number: 46 (crogida)
 *  Column number: 47 (crogidb)
 *  Column number: 48 (crigid)
 *  Column number: 49 (icrogida)
 *  Column number: 50 (icrogidb)
 *  Column number: 51 (icrigid)
 *  Column number: 52 (imex_id)
 *  Column number: 53 (edgetype)
 *  Column number: 54 (numParticipants)
 * </pre>
 * 
 * Note, many of comments in the code below are taken from
 * http://irefindex.uio.no/wiki/README_MITAB2.6_for_iRefIndex_8.0
 * 
 * @author
 * 
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Record(ontClass = CcpExtensionOntology.IREFWEB_PSI_MITAB_2_6_RECORD, dataSource = DataSource.IREFWEB, label="IRefWeb record")
public class IRefWebPsiMitab2_6FileData extends SingleLineFileRecord {

	/**
	 * Logger primarily used for outputting warning messages
	 */
	private static final Logger logger = Logger.getLogger(IRefWebPsiMitab2_6FileData.class);

	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_PSI_MITAB_2_6_RECORD___SOURCE_DATABASE_FIELD_VALUE)
	private final IRefWebInteractionSourceDatabase sourceDb;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_PSI_MITAB_2_6_RECORD___CREATION_DATE_FIELD_VALUE)
	private final String creationDate;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_PSI_MITAB_2_6_RECORD___UPDATE_DATE_FIELD_VALUE)
	private final String updateDate;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_PSI_MITAB_2_6_RECORD___INTERACTOR_A_FIELD_VALUE)
	private final IRefWebInteractor interactorA;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_PSI_MITAB_2_6_RECORD___INTERACTOR_B_FIELD_VALUE)
	private final IRefWebInteractor interactorB;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_PSI_MITAB_2_6_RECORD___INTERACTION_FIELD_VALUE)
	private final IRefWebInteraction interaction;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param sourceDb
	 * @param creationDate
	 * @param updateDate
	 * @param interactorA
	 * @param interactorB
	 * @param interaction
	 */
	public IRefWebPsiMitab2_6FileData(IRefWebInteractionSourceDatabase sourceDb, String creationDate, String updateDate,
			IRefWebInteractor interactorA, IRefWebInteractor interactorB, IRefWebInteraction interaction,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.sourceDb = sourceDb;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
		this.interactorA = interactorA;
		this.interactorB = interactorB;
		this.interaction = interaction;
	}

	
	
}
