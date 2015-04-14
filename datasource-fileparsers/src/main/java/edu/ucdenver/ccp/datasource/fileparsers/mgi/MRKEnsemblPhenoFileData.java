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
package edu.ucdenver.ccp.fileparsers.mgi;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MammalianPhenotypeID;

/**
 * Deprecated b/c the MRK_Ensembl_Pheno.rpt file is no longer available on the MGI FTP site
 * 
 * Data representation of contents of MRK_Ensembl_Pheno.rpt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Deprecated
public class MRKEnsemblPhenoFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "MRK_ENSEMBL_PHENO_FILE_RECORD_";
	private static final Logger logger = Logger.getLogger(MRKEnsemblPhenoFileData.class);

	private final MgiGeneID mgiAccessionID;

	private final String markerSymbol;

	private final String markerName;

	private final EnsemblGeneID ensemblAccessionID;

	private final MammalianPhenotypeID highLevelMammalianPhenotypeID;

	private final MammalianPhenotypeTermName mammalianPhenotypeTermName;

	public MRKEnsemblPhenoFileData(MgiGeneID mgiAccessionID, String markerSymbol,
			String markerName, EnsemblGeneID ensemblAccessionID,
			MammalianPhenotypeID highLevelMammalianPhenotypeID, MammalianPhenotypeTermName mammalianPhenotypeTermName,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mgiAccessionID = mgiAccessionID;
		this.markerSymbol = markerSymbol;
		this.markerName = markerName;
		this.ensemblAccessionID = ensemblAccessionID;
		this.highLevelMammalianPhenotypeID = highLevelMammalianPhenotypeID;
		this.mammalianPhenotypeTermName = mammalianPhenotypeTermName;
	}

	public MgiGeneID getMgiAccessionID() {
		return mgiAccessionID;
	}

	public String getMarkerSymbol() {
		return markerSymbol;
	}

	public String getMarkerName() {
		return markerName;
	}

	public EnsemblGeneID getEnsemblAccessionID() {
		return ensemblAccessionID;
	}

	public MammalianPhenotypeID getHighLevelMammalianPhenotypeID() {
		return highLevelMammalianPhenotypeID;
	}

	public MammalianPhenotypeTermName getMammalianPhenotypeTermName() {
		return mammalianPhenotypeTermName;
	}

	public static MRKEnsemblPhenoFileData parseMRKEnsemblPhenoLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length != 5) {
			MgiGeneID mgiAccessionID = new MgiGeneID(toks[0]);
			String markerSymbol = new String(toks[1]);
			String markerName = new String(toks[2]);
			EnsemblGeneID ensemblAccessionID = new EnsemblGeneID(toks[3]);
			MammalianPhenotypeID highLevelMammalianPhenotypeID = new MammalianPhenotypeID(toks[4]);
			MammalianPhenotypeTermName mammalianPhenotypeTermName = new MammalianPhenotypeTermName(toks[5]);

			return new MRKEnsemblPhenoFileData(mgiAccessionID, markerSymbol, markerName, ensemblAccessionID,
					highLevelMammalianPhenotypeID, mammalianPhenotypeTermName, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens on line (" + toks.length + "): " + line);
		return null;

	}
}
