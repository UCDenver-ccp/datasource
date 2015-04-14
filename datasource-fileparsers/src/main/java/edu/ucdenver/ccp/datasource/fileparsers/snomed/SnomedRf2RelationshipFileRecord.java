/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.snomed;

import lombok.Getter;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Getter
public class SnomedRf2RelationshipFileRecord extends SingleLineFileRecord {

	/*
	 * id effectiveTime active moduleId sourceId destinationId relationshipGroup typeId
	 * characteristicTypeId modifierId
	 */

	private final String relationshipId;
	private final String effectiveTime;
	private final boolean active;
	private final String moduleId;
	private final String sourceConceptId;
	private final String destinationConceptId;
	private final String relationshipGroup;
	private final String typeId;
	private final String characteristicTypeId;
	private final String modifierId;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param relationshipId
	 * @param effectiveTime
	 * @param active
	 * @param moduleId
	 * @param sourceConceptId
	 * @param destinationConceptId
	 * @param relationshipGroup
	 * @param typeId
	 * @param characteristicTypeId
	 * @param modifierId
	 */
	public SnomedRf2RelationshipFileRecord(String relationshipId, String effectiveTime, boolean active,
			String moduleId, String sourceConceptId, String destinationConceptId, String relationshipGroup,
			String typeId, String characteristicTypeId, String modifierId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.relationshipId = relationshipId;
		this.effectiveTime = effectiveTime;
		this.active = active;
		this.moduleId = moduleId;
		this.sourceConceptId = sourceConceptId;
		this.destinationConceptId = destinationConceptId;
		this.relationshipGroup = relationshipGroup;
		this.typeId = typeId;
		this.characteristicTypeId = characteristicTypeId;
		this.modifierId = modifierId;
	}

}
