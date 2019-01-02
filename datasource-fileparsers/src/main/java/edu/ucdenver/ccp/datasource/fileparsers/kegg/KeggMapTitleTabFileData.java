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
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggPathwayID;

/**
 * Representation of data from KEGG map_title.tab file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource=DataSource.KEGG)
public class KeggMapTitleTabFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "KEGG_MAP_TITLE_RECORD_";
	private static final Logger logger = Logger.getLogger(KeggMapTitleTabFileData.class);

	@RecordField
	private final KeggPathwayID keggPathwayID;
	@RecordField
	private final String keggPathwayName;

	public KeggMapTitleTabFileData(KeggPathwayID keggPathwayID, String keggPathwayName, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.keggPathwayID = keggPathwayID;
		this.keggPathwayName = keggPathwayName;
	}

	public KeggPathwayID getKeggPathwayID() {
		return keggPathwayID;
	}

	public String getKeggPathwayName() {
		return keggPathwayName;
	}

	public static KeggMapTitleTabFileData parseKeggMapTitleTabLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			KeggPathwayID keggPathwayID = new KeggPathwayID(toks[0]);
			String keggPathwayName = new String(toks[1]);
			return new KeggMapTitleTabFileData(keggPathwayID, keggPathwayName, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
