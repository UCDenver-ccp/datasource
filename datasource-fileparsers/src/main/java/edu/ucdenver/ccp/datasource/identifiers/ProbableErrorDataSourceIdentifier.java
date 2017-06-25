package edu.ucdenver.ccp.datasource.identifiers;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2016 Regents of the University of Colorado
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

@Data
@EqualsAndHashCode(callSuper=false)
@Identifier(ontClass=CcpExtensionOntology.INVALID_IDENTIFIER)
public class ProbableErrorDataSourceIdentifier extends DataSourceIdentifier<String> {
	private final String dataSourceStr;
	private final String errorMessage;
	// TODO: eventually it would be helpful to have the error id typed to what it was supposed to be (if known).
	// this will require some large switch statements in the IdResolver code in various places so for now
	// we will type things simply with 'invalid identifier' IAO_EXT_0000110 
//	private final CcpExtensionOntology identifierType;

	public ProbableErrorDataSourceIdentifier(String resourceID,
			String dataSourceStr, String errorMessage) {
		super(resourceID, DataSource.PROBABLE_ERROR);
//		this.identifierType = identifierType;
		this.dataSourceStr = dataSourceStr;
		this.errorMessage = errorMessage;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		return resourceID;
	}

}
