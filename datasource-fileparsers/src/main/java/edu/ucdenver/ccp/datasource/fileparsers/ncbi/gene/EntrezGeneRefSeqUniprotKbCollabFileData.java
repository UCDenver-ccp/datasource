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
package edu.ucdenver.ccp.fileparsers.ncbi.gene;

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
