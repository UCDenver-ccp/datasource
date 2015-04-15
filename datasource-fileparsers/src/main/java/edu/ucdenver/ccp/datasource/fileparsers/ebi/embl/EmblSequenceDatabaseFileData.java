/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import java.util.List;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.other.InsdcProjectId;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.EMBL, comment = "Field comments taken from ftp://ftp.ebi.ac.uk/pub/databases/embl/doc/usrman.txt")
public class EmblSequenceDatabaseFileData extends EmblSequenceDatabaseFileDataBase {

	
	
	
	
	
	
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
	public EmblSequenceDatabaseFileData(EmblID primaryAccessionNumber,
			String sequenceVersionNumber, String sequenceTopology, String moleculeType, String dataClass,
			String taxonomicDivision, int sequenceLengthInBasePairs, List<EmblID> accessionNumbers,
			InsdcProjectId projectId, Set<EmblDate> dates, String description, Set<String> keyWords,
			String organismSpeciesName, String organismClassification, String organelle,
			Set<EmblReferenceCitation> referenceCitations, //String proteinExistenceEvidence,
			Set<DataSourceIdentifier<?>> databaseCrossReferences, String comments,
			Set<SequenceFeature> sequenceFeatures, int sequenceLength, int numAs, int numCs, int numGs, int numTs,
			int numOthers, String sequence, String constructedSeqInfo, Set<EmblAssemblyInformation> assemblyInfo,
			long byteOffset) {
		super(primaryAccessionNumber, sequenceVersionNumber, sequenceTopology, moleculeType, dataClass,
				taxonomicDivision, sequenceLengthInBasePairs, accessionNumbers, projectId, dates, description,
				keyWords, organismSpeciesName, organismClassification, organelle, referenceCitations,
				databaseCrossReferences, comments, sequenceFeatures, sequenceLength, numAs,
				numCs, numGs, numTs, numOthers, sequence, constructedSeqInfo, assemblyInfo, byteOffset);
	}

}
