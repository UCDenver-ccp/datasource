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

import java.util.Arrays;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.Identifier;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;

@Identifier(ontClass=CcpExtensionOntology.NCBI_TAXONOMY_CONCEPT_IDENTIFIER)
public class NcbiTaxonomyID extends IntegerDataSourceIdentifier {

	public static final NcbiTaxonomyID ALL = new NcbiTaxonomyID(Integer.MAX_VALUE);
	public static final NcbiTaxonomyID HOMO_SAPIENS = new NcbiTaxonomyID(9606);
	public static final NcbiTaxonomyID HUMAN = new NcbiTaxonomyID(9606);
	public static final NcbiTaxonomyID MUS_MUSCULUS = new NcbiTaxonomyID(10090);
	public static final NcbiTaxonomyID MOUSE = new NcbiTaxonomyID(10090);
	public static final NcbiTaxonomyID RATTUS_NORVEGICUS = new NcbiTaxonomyID(10116);
	public static final NcbiTaxonomyID RAT = new NcbiTaxonomyID(10116);
	public static final NcbiTaxonomyID ARABIDOPSIS = new NcbiTaxonomyID(3702);
	public static final NcbiTaxonomyID CHICKEN = new NcbiTaxonomyID(9031);
	public static final NcbiTaxonomyID COW = new NcbiTaxonomyID(9913);
	public static final NcbiTaxonomyID DICTY = new NcbiTaxonomyID(44689);
	public static final NcbiTaxonomyID DOG = new NcbiTaxonomyID(9615);
	public static final NcbiTaxonomyID FLY = new NcbiTaxonomyID(7227);
	public static final NcbiTaxonomyID PIG = new NcbiTaxonomyID(9823);
	public static final NcbiTaxonomyID WORM = new NcbiTaxonomyID(6239);
	public static final NcbiTaxonomyID SACCHAROMYCES_CEREVISIAE_S288C = new NcbiTaxonomyID(559292);
	public static final NcbiTaxonomyID ZEBRAFISH = new NcbiTaxonomyID(7955);
	

	public NcbiTaxonomyID(Integer taxonomyID) {
		super(taxonomyID,DataSource.NCBI_TAXON);
	}

	public NcbiTaxonomyID(String taxonomyID) {
		super(taxonomyID, Arrays.asList("TAXID:", "TAXON:"), DataSource.NCBI_TAXON);
	}

}
