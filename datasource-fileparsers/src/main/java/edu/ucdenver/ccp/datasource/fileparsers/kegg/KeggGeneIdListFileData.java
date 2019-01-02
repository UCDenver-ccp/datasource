package edu.ucdenver.ccp.datasource.fileparsers.kegg;

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

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggGeneID;

/**
 * Representation of data from KEGG AAA_geneIDType.list file
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGeneIdListFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(KeggGeneIdListFileData.class);
	public static final String RECORD_NAME_PREFIX = "KEGG_GENE_";
	private final KeggGeneID internalKeggGeneID;

	private final DataSourceIdentifier<?> externalGeneID;

	public KeggGeneIdListFileData(KeggGeneID internalKeggGeneID, DataSourceIdentifier<?> externalGeneID,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.internalKeggGeneID = internalKeggGeneID;
		this.externalGeneID = externalGeneID;
	}

	public KeggGeneID getKeggGeneID() {
		return internalKeggGeneID;
	}

	public DataSourceIdentifier<?> getExternalGeneID() {
		return externalGeneID;
	}

	public static KeggGeneIdListFileData parseKeggGeneIDListLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			String keggGeneIDStr = toks[0].substring(toks[0].indexOf(":") + 1);
			KeggGeneID keggInternalGeneID = new KeggGeneID(keggGeneIDStr);
			DataSourceIdentifier<?> externalGeneID = DataSourceIdResolver.resolveId(toks[1]);
			return new KeggGeneIdListFileData(keggInternalGeneID, externalGeneID, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
