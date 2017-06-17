package edu.ucdenver.ccp.datasource.identifiers;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2017 Regents of the University of Colorado
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

public class RepairedStringDataSourceIdentifier extends StringDataSourceIdentifier {
	@Getter
	private final String originalIdentifierStr;
	@Getter
	private final StringDataSourceIdentifier repairedId;

	public RepairedStringDataSourceIdentifier(StringDataSourceIdentifier repairedId, String originalIdentifierStr) {
		super(repairedId.getId(), repairedId.getDataSource());
		this.originalIdentifierStr = originalIdentifierStr;
		this.repairedId = repairedId;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		return repairedId.validate(resourceID);
	}

	@Override
	public boolean equals(Object o) {
		return repairedId.equals(o);
	}

	@Override
	public int hashCode() {
		return repairedId.hashCode();
	}

}
