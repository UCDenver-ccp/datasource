package edu.ucdenver.ccp.fileparsers.irefweb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

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
@Record(dataSource = DataSource.IREFWEB, label="IRefWeb record")
public class IRefWebPsiMitab2_6FileData extends SingleLineFileRecord {

	/**
	 * Logger primarily used for outputting warning messages
	 */
	private static final Logger logger = Logger.getLogger(IRefWebPsiMitab2_6FileData.class);

	@RecordField
	private final IRefWebInteractionSourceDatabase sourceDb;
	@RecordField
	private final String creationDate;
	@RecordField
	private final String updateDate;
	@RecordField
	private final IRefWebInteractor interactorA;
	@RecordField
	private final IRefWebInteractor interactorB;
	@RecordField
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
