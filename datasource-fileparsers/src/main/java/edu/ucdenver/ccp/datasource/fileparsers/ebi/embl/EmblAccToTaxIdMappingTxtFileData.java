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
package edu.ucdenver.ccp.datasource.fileparsers.ebi.embl;

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
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * Representation of data from the EMBL acc_to_taxid.mappings.txt.gz file,
 * a tab delimited text file of EMBL/INSDC accession numbers and NCBI taxid.
 * Useful for anyone who needs to derive lists of accession numbers for a particular taxid,
 * and also for users of the fasta embl files at
 * ftp://ftp.ebi.ac.uk/pub/databases/fastafiles/emblrelease.
 * 
 * ftp://ftp.ebi.ac.uk/pub/databases/embl/misc/acc_to_taxid.mapping.txt.gz
 * 
 * @author Bill Baumgartner
 * 
 */

@Record(dataSource=DataSource.EMBL, // TODO is this datasource correct?
		comment="Representation of data from the EMBL acc_to_taxid.mappings.txt.gz file, a tab delimited text file of EMBL/INSDC accession numbers and NCBI taxid. Useful for anyone who needs to derive lists of accession numbers for a particular taxid, and also for users of the fasta embl files at ftp://ftp.ebi.ac.uk/pub/databases/fastafiles/emblrelease. See ftp://ftp.ebi.ac.uk/pub/databases/embl/release/usrman.txt",
		citation="",
		license=License.EBI)
public class EmblAccToTaxIdMappingTxtFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(EmblAccToTaxIdMappingTxtFileData.class);

	@RecordField(comment="EMBL/INSDC accession number")
	private final EmblID emblId;

	@RecordField(comment="NCBI taxid")
	private final NcbiTaxonomyID taxId;

	public EmblAccToTaxIdMappingTxtFileData(EmblID emblId, NcbiTaxonomyID taxId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.emblId = emblId;
		this.taxId = taxId;
	}

	public EmblID getEmblId() {
		return emblId;
	}

	public NcbiTaxonomyID getTaxId() {
		return taxId;
	}

	public static EmblAccToTaxIdMappingTxtFileData parseEmblAccToTaxidMappingLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 2) {
			EmblID emblId = new EmblID(toks[0]);
			NcbiTaxonomyID taxId;
			if (StringUtil.isIntegerGreaterThanZero(toks[1]))
				taxId = new NcbiTaxonomyID(toks[1]);
			else {
				taxId = null;
				logger.info(String.format("Invalid taxonomy ID detected: %s on line: %d", toks[1], line.getLineNumber()));
			}
			return new EmblAccToTaxIdMappingTxtFileData(emblId, taxId, line.getByteOffset(), line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line);
		return null;
	}

}
