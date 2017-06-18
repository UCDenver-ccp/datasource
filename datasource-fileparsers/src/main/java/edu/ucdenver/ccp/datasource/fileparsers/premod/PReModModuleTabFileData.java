package edu.ucdenver.ccp.datasource.fileparsers.premod;

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
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PreModID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacMatrixID;

/**
 * A representation of a line of data from the PReMod mouse_module_tab.txt file available here:
 * http://genomequebec.mcgill.ca/PReMod/download.do?method=download&organism=Mouse&version=1
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.PREMOD, label="PReMod record")
public class PReModModuleTabFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(PReModModuleTabFileData.class);
	/*
	 * Name Chromosome Length Score Upstream Entrez Gene Id Upstream Gene Name Upstream Gene
	 * Position Downstream Entrez Gene Id Downstream Gene Name Downstream Gene Position Tag Matrices
	 */
	@RecordField
	private final PreModID premodID;
	@RecordField
	private final String chromosome;
	@RecordField
	private final int length;
	@RecordField
	private final float score;
	@RecordField
	private final NcbiGeneId upstreamEntrezGeneID;
	@RecordField
	private final String upstreamGeneName;
	@RecordField
	private final int upstreamGenePosition;
	@RecordField
	private final NcbiGeneId downstreamEntrezGeneID;
	@RecordField
	private final String downstreamGeneName;
	@RecordField
	private final int downstreamGenePosition;
	@RecordField
	private final Set<TransfacMatrixID> tagMatrices;

	public PReModModuleTabFileData(PreModID premodID, String chromosome, int length, float score,
			NcbiGeneId upstreamEntrezGeneID, String upstreamGeneName, int upstreamGenePosition,
			NcbiGeneId downstreamEntrezGeneID, String downstreamGeneName, int downstreamGenePosition,
			Set<TransfacMatrixID> tagMatrices, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.premodID = premodID;
		this.chromosome = chromosome;
		this.length = length;
		this.score = score;
		this.upstreamEntrezGeneID = upstreamEntrezGeneID;
		this.upstreamGeneName = upstreamGeneName;
		this.upstreamGenePosition = upstreamGenePosition;
		this.downstreamEntrezGeneID = downstreamEntrezGeneID;
		this.downstreamGeneName = downstreamGeneName;
		this.downstreamGenePosition = downstreamGenePosition;
		this.tagMatrices = tagMatrices;
	}

	public PreModID getPremodID() {
		return premodID;
	}

	public String getChromosome() {
		return chromosome;
	}

	public int getLength() {
		return length;
	}

	public float getScore() {
		return score;
	}

	public NcbiGeneId getUpstreamEntrezGeneID() {
		return upstreamEntrezGeneID;
	}

	public String getUpstreamGeneName() {
		return upstreamGeneName;
	}

	public int getUpstreamGenePosition() {
		return upstreamGenePosition;
	}

	public NcbiGeneId getDownstreamEntrezGeneID() {
		return downstreamEntrezGeneID;
	}

	public String getDownstreamGeneName() {
		return downstreamGeneName;
	}

	public int getDownstreamGenePosition() {
		return downstreamGenePosition;
	}

	public Set<TransfacMatrixID> getTagMatrices() {
		return tagMatrices;
	}

	public static PReModModuleTabFileData parseModuleTabLine(Line line) {
		if (line.getText().startsWith("mod")) {
			String[] toks = line.getText().split("\\t");
			PreModID premodID = new PreModID(toks[0]);
			String chromosome = new String(toks[1]);
			int length = Integer.parseInt(toks[2]);
			float score = Float.parseFloat(toks[3]);
			NcbiGeneId upstreamEntrezGeneID = new NcbiGeneId(toks[4]);
			String upstreamGeneName = new String(toks[5]);
			int upstreamGenePosition = Integer.parseInt(toks[6]);
			NcbiGeneId downstreamEntrezGeneID = new NcbiGeneId(toks[7]);
			String downstreamGeneName = new String(toks[8]);
			int downstreamGenePosition = Integer.parseInt(toks[9]);
			Set<TransfacMatrixID> tagMatrices = new HashSet<TransfacMatrixID>();
			for (int i = 10; i < toks.length; i++) {
				String matrixID = toks[i].substring(0, toks[i].indexOf(StringConstants.SPACE));
				tagMatrices.add(new TransfacMatrixID(matrixID));
			}

			return new PReModModuleTabFileData(premodID, chromosome, length, score, upstreamEntrezGeneID,
					upstreamGeneName, upstreamGenePosition, downstreamEntrezGeneID, downstreamGeneName,
					downstreamGenePosition, tagMatrices, line.getByteOffset(), line.getLineNumber());

		}

		logger.warn("There is no relevant information to parse on line: " + line);
		return null;
	}

}
