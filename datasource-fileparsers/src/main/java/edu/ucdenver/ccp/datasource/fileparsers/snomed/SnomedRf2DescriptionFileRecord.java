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
public class SnomedRf2DescriptionFileRecord extends SingleLineFileRecord {

	public enum DescriptionType {
		FULLY_SPECIFIED_NAME("900000000000003001"),
		SYNONYM("900000000000013009");
		
		private final String typeId;
		private DescriptionType(String typeId) {
			this.typeId = typeId;
		}
		
		public String typeId() {
			return typeId;
		}
		
		public static DescriptionType getType(String typeId) {
			for (DescriptionType type : values()) {
				if (type.typeId().equals(typeId)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Invalid SnoMed description type id: " + typeId);
		}
	}
	
	/*
	 * id effectiveTime active moduleId conceptId languageCode typeId term caseSignificanceId
	 */

	private final String descriptionId;
	private final String effectiveTime;
	private final boolean active;
	private final String moduleId;
	private final String conceptId;
	private final String languageCode;
	private final DescriptionType type;
	private final String term;
	private final String caseSignificanceId;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param definitionId
	 * @param effectiveTime
	 * @param active
	 * @param moduleId
	 * @param conceptId
	 * @param languageCode
	 * @param typeId
	 * @param term
	 * @param caseSignificanceId
	 */
	public SnomedRf2DescriptionFileRecord(String descriptionId, String effectiveTime, boolean active, String moduleId,
			String conceptId, String languageCode, DescriptionType type, String term, String caseSignificanceId,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.descriptionId = descriptionId;
		this.effectiveTime = effectiveTime;
		this.active = active;
		this.moduleId = moduleId;
		this.conceptId = conceptId;
		this.languageCode = languageCode;
		this.type = type;
		this.term = term;
		this.caseSignificanceId = caseSignificanceId;
	}

}
