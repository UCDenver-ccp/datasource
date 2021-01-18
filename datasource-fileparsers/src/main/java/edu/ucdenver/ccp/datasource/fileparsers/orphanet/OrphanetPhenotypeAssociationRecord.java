package edu.ucdenver.ccp.datasource.fileparsers.orphanet;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2020 Regents of the University of Colorado
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

import java.util.HashSet;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrphanetPhenotypeAssociationRecord extends FileRecord {

	public enum Frequency {
		OBLIGATE_100, VERY_FREQUENT_99_80, FREQUENT_79_30, OCCASIONAL_29_5, VERY_RARE_4_1, EXCLUDED_0
	}

	private final String disorderId;
	private String disorderType;
	private String disorderName;
	private String orphaId;
	private Set<AssociatedPhenotype> associatedPhenotypes;

	public OrphanetPhenotypeAssociationRecord(String disorderId, long byteOffset) {
		super(byteOffset);
		this.disorderId = disorderId;
		this.associatedPhenotypes = new HashSet<AssociatedPhenotype>();
	}
	
	public void addAssociatedPhenotype(AssociatedPhenotype ap) {
		this.associatedPhenotypes.add(ap);
	}

	@Data
	public static class AssociatedPhenotype {
		private  String phenotypeId;
		private  String phenotypeName;
		private  Frequency frequency;
	}

}
