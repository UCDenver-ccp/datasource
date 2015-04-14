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
public class SnomedRf2ConceptFileRecord extends SingleLineFileRecord {

	/*
	 * id effectiveTime active moduleId definitionStatusId
	 */

	private final String conceptId;
	private final String effectiveTime;
	private final boolean active;
	private final String moduleId;
	private final String definitionStatusId;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param conceptId
	 * @param effectiveTime
	 * @param active
	 * @param moduleId
	 * @param definitionStatusId
	 */
	public SnomedRf2ConceptFileRecord(String conceptId, String effectiveTime, boolean active, String moduleId,
			String definitionStatusId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.conceptId = conceptId;
		this.effectiveTime = effectiveTime;
		this.active = active;
		this.moduleId = moduleId;
		this.definitionStatusId = definitionStatusId;
	}

}
