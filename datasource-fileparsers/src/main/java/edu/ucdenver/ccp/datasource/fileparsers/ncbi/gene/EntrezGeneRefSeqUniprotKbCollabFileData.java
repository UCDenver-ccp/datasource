/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

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
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;

/**
 * Representation of data from the EntrezGene gene2pubmed file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.EG,
	comment="",
	license=License.NCBI,
	citation="The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091",
	label="refseq/uniprot mapping record")
public class EntrezGeneRefSeqUniprotKbCollabFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "REFSEQ_UNI_COLLAB_";
	private static final Logger logger = Logger.getLogger(EntrezGeneRefSeqUniprotKbCollabFileData.class);

	@RecordField(comment="", label="refseq protein id")
	private final RefSeqID refSeqProteinId;
	@RecordField(comment="")
	private final UniProtID uniprotId;

	public EntrezGeneRefSeqUniprotKbCollabFileData(RefSeqID refseqProteinId, UniProtID uniprotId, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.refSeqProteinId = refseqProteinId;
		this.uniprotId = uniprotId;
	}

	/**
	 * @return the refSeqProteinId
	 */
	public RefSeqID getRefSeqProteinId() {
		return refSeqProteinId;
	}

	/**
	 * @return the uniprotId
	 */
	public UniProtID getUniprotId() {
		return uniprotId;
	}

	public static EntrezGeneRefSeqUniprotKbCollabFileData parseGeneRefseqUniprotKbCollabLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			RefSeqID refseqId = new RefSeqID(toks[0]);
			UniProtID uniprotId = new UniProtID(toks[1]);
			return new EntrezGeneRefSeqUniprotKbCollabFileData(refseqId, uniprotId, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
