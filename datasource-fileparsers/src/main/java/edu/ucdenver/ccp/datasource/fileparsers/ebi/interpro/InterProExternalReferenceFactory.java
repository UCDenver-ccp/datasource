package edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro;

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

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.Gene3dID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HamapAnnotationRuleID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PantherID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PfamID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PirSfID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PrintsID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ProDomID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PrositeID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SmartID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SuperFamID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TigrFamsID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UncharacterizedPfamID;

public class InterProExternalReferenceFactory {

	private static final String PFAM_PREFIX = "PF";
	private static final String TIGRFAMS_PREFIX = "TIGR";
	private static final String GENE3D_PREFIX = "G3DSA:";
	private static final String SUPERFAM_PREFIX = "SSF";
	private static final String PROSITE_PREFIX = "PS";
	private static final String SMART_PREFIX = "SM";
	private static final String PRINTS_PREFIX = "PR";
	private static final String UPFAM_PREFIX = "UPF";
	private static final String PANTHER_PREFIX = "PTHR";
	private static final String PIR_PREFIX = "PIRSF";
	private static final String HAMAP_PREFIX = "MF_";
	private static final String PRODOM_PREFIX = "PD";


    public static DataSourceIdentifier<String> parseExternalReference(
			String databaseReferenceID)
    {
		if (databaseReferenceID.startsWith(PFAM_PREFIX))
			return new PfamID(databaseReferenceID);
		if (databaseReferenceID.startsWith(TIGRFAMS_PREFIX))
			return new TigrFamsID(databaseReferenceID);
		if (databaseReferenceID.startsWith(GENE3D_PREFIX))
			return new Gene3dID(databaseReferenceID);
		if (databaseReferenceID.startsWith(SUPERFAM_PREFIX))
			return new SuperFamID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PROSITE_PREFIX))
			return new PrositeID(databaseReferenceID);
		if (databaseReferenceID.startsWith(SMART_PREFIX))
			return new SmartID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PRINTS_PREFIX))
			return new PrintsID(databaseReferenceID);
		if (databaseReferenceID.startsWith(UPFAM_PREFIX))
			return new UncharacterizedPfamID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PANTHER_PREFIX))
			return new PantherID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PIR_PREFIX))
			return new PirSfID(databaseReferenceID);
		if (databaseReferenceID.startsWith(HAMAP_PREFIX))
			return new HamapAnnotationRuleID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PRODOM_PREFIX))
            return new ProDomID(databaseReferenceID);

		
        return new UnknownDataSourceIdentifier(databaseReferenceID);
	}

}
