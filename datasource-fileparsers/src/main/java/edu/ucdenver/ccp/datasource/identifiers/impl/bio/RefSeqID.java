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

import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.Identifier;

@Identifier(ontClass=CcpExtensionOntology.REFSEQ_IDENTIFIER)
public class RefSeqID extends DataSourceIdentifier<String> {

	public RefSeqID(String resourceID) {
		super(removeVersionIfPresent(resourceID),DataSource.REFSEQ);
	}

	/**
	 * RefSeq IDs are often listed as accession.version pairs, e.g. NP_047184.1. We currently ignore
	 * the version information because not all RefSeq IDs are listed with one. KABOB-265 addresses
	 * this issue. This method removes the version from the RefSeq ID if one is present.
	 * 
	 * @param resourceID
	 * @return
	 */
	private static String removeVersionIfPresent(String resourceID) {
		String versionRegex = "\\.\\d+";
		if (StringUtil.endsWithRegex(resourceID, versionRegex))
			return StringUtil.removeSuffixRegex(resourceID, versionRegex);
		if (resourceID.endsWith(".")) // this was seen at least once in the HGNC data file
			return StringUtil.removeLastCharacter(resourceID);
		return resourceID;
	}

	/**
	 * See: http://www.ncbi.nlm.nih.gov/refseq/key.html#accessions for proper validations
	 */
	@Override
	public String validate(String refseqID) throws IllegalArgumentException {
		String legalRefSeqRegex = "((AC_)|(AP_)|(NC_)|(NG_)|(NM_)|(NP_)|(NR_)|(NT_)|(NW_)|(NZ_[A-Z]{1,4})|(XM_)|(XP_)|(XR_)|(YP_)|(ZP_)|(NS_)|(WP_))\\d+";
		if (refseqID.matches(legalRefSeqRegex))
			return refseqID;
		throw new IllegalArgumentException("Illegal RefSeq identifier detected: " + refseqID);
	}

}
