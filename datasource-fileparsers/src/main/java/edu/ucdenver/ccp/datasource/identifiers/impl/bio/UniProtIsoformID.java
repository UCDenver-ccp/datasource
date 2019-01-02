package edu.ucdenver.ccp.datasource.identifiers.impl.bio;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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

import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.Identifier;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

@Identifier(ontClass=CcpExtensionOntology.UNIPROTISOFORM_IDENTIFIER)
public class UniProtIsoformID extends StringDataSourceIdentifier {

	public UniProtIsoformID(String resourceID) {
		super(resourceID, DataSource.UNIPROT);
}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		String[] toks = resourceID.split(StringConstants.HYPHEN_MINUS);
		IllegalArgumentException invalidUniProtException = null;
		if (toks.length == 2) {
			if (StringUtil.isNonNegativeInteger(toks[1]))
				try {
					new UniProtID(toks[0]);
				} catch (IllegalArgumentException iae) {
					invalidUniProtException = iae;
				}
			if (invalidUniProtException == null)
				return resourceID;
		}
		String errorMessage = String.format("Invalid UniProt Isoform ID: %s. This ID must consist of a "
				+ "valid UniProt ID followed by a hyphen and then an integer.", resourceID);
		if (invalidUniProtException == null)
			throw new IllegalArgumentException(errorMessage);
		throw new IllegalArgumentException(errorMessage, invalidUniProtException);

	}

}
