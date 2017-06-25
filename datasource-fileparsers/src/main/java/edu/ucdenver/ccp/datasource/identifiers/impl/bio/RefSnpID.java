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

import org.apache.commons.lang.math.NumberUtils;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.Identifier;

/**
 * Reference SNP cluster ID definition for NCBI Single Nucleotide Polymorphysm database.<p> 
 * From dbSNP Handbook:<p>
 * <i>
 * Once a new SNP is submitted to dbSNP, it is assigned a unique submitted SNP ID number (ss#). 
 * Once the ss number is assigned, we align the flanking sequence of each submitted SNP to its appropriate genomic contig. 
 * If several ss numbers map to the same position on the contig, we cluster them together, call the cluster a "reference SNP cluster", 
 * or "refSNP", and provide the cluster with a unique RefSNP ID number (rs#). If only one ss number maps to a specific position,
 * then that ss is assigned an rs number and is the only member of its RefSNP cluster until another submitted SNP is found 
 * that maps to the same position.
 * </i>
 * 
 * @author Yuriy Malenkiy
 *
 */
@Identifier(ontClass=CcpExtensionOntology.REFSNP_IDENTIFIER)
public class RefSnpID extends DataSourceIdentifier<String> {

	public RefSnpID(String resourceID) {
		super(resourceID, DataSource.REFSNP);
}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && resourceID.startsWith("rs") && NumberUtils.isDigits(resourceID.substring(2)))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid Reference SNP ID detected: %s", resourceID));
	}

}
