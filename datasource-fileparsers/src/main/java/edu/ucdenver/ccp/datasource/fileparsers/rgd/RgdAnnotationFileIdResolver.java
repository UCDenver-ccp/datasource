package edu.ucdenver.ccp.datasource.fileparsers.rgd;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.IdResolver;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MammalianPhenotypeID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NboId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PwId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RdoId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * @author Center for Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class RgdAnnotationFileIdResolver implements IdResolver {

	private static final Logger logger = Logger.getLogger(RgdAnnotationFileIdResolver.class);

	@Override
	public DataSourceIdentifier<?> resolveId(String idStr) {
		if (idStr.equals("]")) {
			// this exists alone in one entry of the withOrFrom column
			return null;
		}
		if (idStr.matches("[Rr][Gg][Dd][:;]\\d+")) {
			// there are instances with mixed case, e.g. RGd: and with
			// semi-colons instead of colons
			return new RgdID(idStr.substring(4));
		}
		if (idStr.matches("[Rr][Gg][Dd]\\d+")) {
			// there is at least one instance missing the colon, e.g. RGD731056
			return new RgdID(idStr.substring(3));
		}
		if (idStr.matches("RGR:735727")) {
			// there is one instance of RGR: instead of RGD:
			return new RgdID(idStr.substring(4));
		}
		if (idStr.matches("RGD:\\s+\\d+")) {
			// there is one instance of RGD:[space]737465
			return new RgdID(idStr.substring(idStr.lastIndexOf(" ")));
		}
		if (idStr.matches("RGDG:\\d+")) {
			// there is one instance of RGDG:733289
			return new RgdID(idStr.substring(5));
		}
		if (idStr.matches("RDG:\\d+")) {
			// there are a few typos where RDG appears instead of RGD
			return new RgdID(idStr.substring(4));
		}
		if (idStr.matches("\\d+")) {
			// there are a few typos where the "RGD:" prefix is missing, e.g.
			// 1550157
			return new RgdID(idStr);
		}
		if (idStr.matches("MP:\\d+")) {
			return new MammalianPhenotypeID(idStr);
		}
		if (idStr.matches("PMID:\\d+")) {
			return new PubMedID(idStr.substring(5));
		}
		if (idStr.matches("RDO:\\d+")) {
			return new RdoId(idStr);
		}
		if (idStr.matches("OMIM:\\d+")) {
			return new OmimID(idStr.substring(5));
		}
		if (idStr.matches("NCBI GeneID:\\d+")) {
			return new NcbiGeneId(idStr.substring(12));
		}
		if (idStr.matches("NBO:\\d+")) {
			return new NboId(idStr);
		}
		if (idStr.matches("PW:\\d+")) {
			return new PwId(idStr);
		}
		if (idStr.startsWith("UniProtKB:")) {
			return new UniProtID(idStr.substring(10));
		}
		return new UnknownDataSourceIdentifier(idStr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.datasource.identifiers.IdResolver#resolveId(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public DataSourceIdentifier<?> resolveId(String db, String id, String originalIdString) {
		if (db.equals("RGD") && id.matches("\\d+")) {
			return new RgdID(id);
		}
		return new UnknownDataSourceIdentifier(originalIdString);
	}

}
