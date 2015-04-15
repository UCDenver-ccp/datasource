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
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

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
