package edu.ucdenver.ccp.datasource.fileparsers.snomed;

import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;

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

import lombok.Getter;

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
