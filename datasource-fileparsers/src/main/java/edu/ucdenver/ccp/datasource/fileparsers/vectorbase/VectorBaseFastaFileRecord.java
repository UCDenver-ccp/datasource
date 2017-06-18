package edu.ucdenver.ccp.datasource.fileparsers.vectorbase;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VectorBaseID;

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

import lombok.Getter;

@Getter
@Record(dataSource = DataSource.VECTORBASE, label = "vectorbase fasta record")
public class VectorBaseFastaFileRecord extends FileRecord {

	private static final Logger logger = Logger.getLogger(VectorBaseFastaFileRecord.class);

	@RecordField(isKeyField = true)
	private final VectorBaseID sequenceId;
	@RecordField
	private final String sequenceName;
	@RecordField
	private final String sequenceType;
	@RecordField
	private final String contig;
	@RecordField
	private final VectorBaseID geneId;
	@RecordField
	private final String sequence;

	/**
	 * @param byteOffset
	 * @param primaryAccession
	 * @param accession
	 * @param name
	 * @param organism
	 * @param organismHost
	 * @param dbReference
	 */
	public VectorBaseFastaFileRecord(VectorBaseID sequenceId, String sequenceName, String sequenceType, String contig,
			VectorBaseID geneId, String sequence, long byteOffset) {
		super(byteOffset);
		this.sequenceId = sequenceId;
		this.sequenceName = sequenceName;
		this.sequenceType = sequenceType;
		this.contig = contig;
		this.geneId = geneId;
		this.sequence = sequence;
	}

}
