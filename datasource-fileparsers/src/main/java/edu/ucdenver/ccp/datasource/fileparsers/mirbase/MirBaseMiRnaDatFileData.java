/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.mirbase;

import java.util.List;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.other.InsdcProjectId;
import edu.ucdenver.ccp.datasource.identifiers.other.MiRBaseID;
import edu.ucdenver.ccp.fileparsers.ebi.embl.EmblAssemblyInformation;
import edu.ucdenver.ccp.fileparsers.ebi.embl.EmblDate;
import edu.ucdenver.ccp.fileparsers.ebi.embl.EmblReferenceCitation;
import edu.ucdenver.ccp.fileparsers.ebi.embl.EmblSequenceDatabaseFileDataBase;
import edu.ucdenver.ccp.fileparsers.ebi.embl.SequenceFeature;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
@Record(dataSource=DataSource.MIRBASE, label="MiRBase record")
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
