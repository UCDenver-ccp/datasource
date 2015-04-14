package edu.ucdenver.ccp.fileparsers.pharmgkb;

import java.util.Collection;
import java.util.Set;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * File record capturing single line record from PharmGKB's diseases.tsv file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(dataSource = DataSource.PHARMGKB,
		schemaVersion = "2", 
		license=License.PHARMGKB,
		licenseUri="http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf",
		comment = "data from PharmGKB's disease.tsv file",
		citation="M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417",
		label="relation record")
@Data
public class PharmGkbRelationFileRecord extends SingleLineFileRecord {

	private static String PHARMGKB_ID_PREFIX = "PA";
	@RecordField
	private final Set<DataSourceIdentifier<?>> entity1Id;
	@RecordField
	private final String entity1Name;
	@RecordField
	private final String entity1Type;
	@RecordField
	private final Set<DataSourceIdentifier<?>> entity2Id;
	@RecordField
	private final String entity2Name;
	@RecordField
	private final String entity2Type;
	@RecordField
	private final Set<String> evidence;
	@RecordField
	private final String association;
	@RecordField
	private final String pk;
	@RecordField
	private final String pd;
	@RecordField
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
