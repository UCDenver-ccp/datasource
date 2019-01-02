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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggPathwayID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;

/**
 * Representation of data from KEGG aaa_gene_map.tab file format.
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGeneMapTabFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(KeggGeneMapTabFileData.class);

	public static final String RECORD_NAME_PREFIX = "KEGG_GENE2PATHWAY_RECORD_";
	private final NcbiGeneId geneID;

	private final Set<KeggPathwayID> keggPathwayIDs;

	public KeggGeneMapTabFileData(NcbiGeneId geneID, Set<KeggPathwayID> keggPathwayIDs, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.geneID = geneID;
		this.keggPathwayIDs = keggPathwayIDs;
	}

	public NcbiGeneId getGeneID() {
		return geneID;
	}

	public Set<KeggPathwayID> getKeggPathwayIDs() {
		return keggPathwayIDs;
	}

	public static KeggGeneMapTabFileData parseKeggGeneMapTabLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			NcbiGeneId geneID = new NcbiGeneId(toks[0]);
			Set<KeggPathwayID> keggPathwayIDs = new HashSet<KeggPathwayID>();
			for (String tok : toks[1].split(" "))
				keggPathwayIDs.add(new KeggPathwayID(tok));
			return new KeggGeneMapTabFileData(geneID, keggPathwayIDs, line.getByteOffset(), line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
