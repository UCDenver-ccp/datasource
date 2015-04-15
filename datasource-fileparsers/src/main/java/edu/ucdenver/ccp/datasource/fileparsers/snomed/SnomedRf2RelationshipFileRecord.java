package edu.ucdenver.ccp.datasource.fileparsers.snomed;

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
