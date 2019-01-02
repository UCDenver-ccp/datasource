package edu.ucdenver.ccp.datasource.fileparsers.mirbase;

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

import java.util.List;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblAssemblyInformation;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblDate;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblReferenceCitation;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblSequenceDatabaseFileDataBase;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.SequenceFeature;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InsdcProjectId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MiRBaseID;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
@Record(dataSource=DataSource.MIRBASE, ontClass=CcpExtensionOntology.MIRBASE_RECORD)
public class MirBaseMiRnaDatFileData extends EmblSequenceDatabaseFileDataBase<MiRBaseID> {

	/**
	 * @param primaryAccessionNumber
	 * @param sequenceVersionNumber
	 * @param sequenceTopology
	 * @param moleculeType
	 * @param dataClass
	 * @param taxonomicDivision
	 * @param sequenceLengthInBasePairs
	 * @param accessionNumbers
	 * @param projectId
	 * @param dates
	 * @param description
	 * @param keyWords
	 * @param organismSpeciesName
	 * @param organismClassification
	 * @param organelle
	 * @param referenceCitations
	 * @param proteinExistenceEvidence
	 * @param databaseCrossReferences
	 * @param comments
	 * @param sequenceFeatures
	 * @param sequenceLength
	 * @param numAs
	 * @param numCs
	 * @param numGs
	 * @param numTs
	 * @param numOthers
	 * @param sequence
	 * @param constructedSeqInfo
	 * @param assemblyInfo
	 * @param byteOffset
	 */
	public MirBaseMiRnaDatFileData(MiRBaseID primaryAccessionNumber, String sequenceVersionNumber,
			String sequenceTopology, String moleculeType, String dataClass, String taxonomicDivision,
			int sequenceLengthInBasePairs, List<MiRBaseID> accessionNumbers, InsdcProjectId projectId,
			Set<EmblDate> dates, String description, Set<String> keyWords, String organismSpeciesName,
			String organismClassification, String organelle, Set<EmblReferenceCitation> referenceCitations,
			//String proteinExistenceEvidence, 
			Set<DataSourceIdentifier<?>> databaseCrossReferences, String comments,
			Set<? extends SequenceFeature> sequenceFeatures, int sequenceLength, int numAs, int numCs, int numGs, int numTs,
			int numOthers, String sequence, String constructedSeqInfo, Set<EmblAssemblyInformation> assemblyInfo,
			long byteOffset) {
		super(primaryAccessionNumber, sequenceVersionNumber, sequenceTopology, moleculeType, dataClass, taxonomicDivision,
				sequenceLengthInBasePairs, accessionNumbers, projectId, dates, description, keyWords, organismSpeciesName,
				organismClassification, organelle, referenceCitations, databaseCrossReferences,
				comments, sequenceFeatures, sequenceLength, numAs, numCs, numGs, numTs, numOthers, sequence,
				constructedSeqInfo, assemblyInfo, byteOffset);
	}

}
