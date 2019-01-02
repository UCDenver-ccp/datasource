package edu.ucdenver.ccp.datasource.identifiers.impl.bio;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;

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

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.Identifier;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * This represents the ROGID identifier used by the IRefWeb resource.
 * 
 * From the IRefWeb documentation: The ROGID for proteins, consists of the base-64 version of the
 * SHA-1 key for the protein sequence concatenated with the taxonomy identifier for the protein. For
 * complex nodes, the ROGID is calculated as the SHA-1 digest of the ROGIDs of all the protein
 * participants (after first ordering them by ASCII-based lexicographical sorting in ascending order
 * and concatenating them) See the iRefIndex paper for details. The SHA-1 key is always 27
 * characters long. So the ROGID will be composed of 27 characters concatenated with a taxonomy
 * identifier for proteins.
 * 
 * @author 
 * 
 */
@Identifier(ontClass=CcpExtensionOntology.IREFWEBROG_IDENTIFIER)
public class IRefWebRogId extends StringDataSourceIdentifier {

	public IRefWebRogId(String resourceID) {
		super(resourceID, DataSource.IREFWEB);
}

}
