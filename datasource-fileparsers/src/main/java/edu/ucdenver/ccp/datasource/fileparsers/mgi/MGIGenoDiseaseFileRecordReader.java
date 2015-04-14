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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MammalianPhenotypeID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * This class is used to parse MGI_PhenoGenoMP.rpt files
 * 
 * @author Bill Baumgartner
 * 
 */
public class MGIGenoDiseaseFileRecordReader extends SingleLineFileRecordReader<MGIGenoDiseaseFileRecord> {

	public static final Logger logger = Logger.getLogger(MGIGenoDiseaseFileRecordReader.class);

	public static final String FTP_FILE_NAME = "MGI_Geno_Disease.rpt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File mgiGenoDiseaseRptFile;

	public MGIGenoDiseaseFileRecordReader(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public MGIGenoDiseaseFileRecordReader(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mgiGenoDiseaseRptFile, encoding, skipLinePrefix);
	}

	@Override
	protected MGIGenoDiseaseFileRecord parseRecordFromLine(Line line) {
		return parseMGIGenoDiseaseLine(line);
	}

	private static MGIGenoDiseaseFileRecord parseMGIGenoDiseaseLine(Line line) {
		String[] toks = line.getText().split("\\t");
		int index = 0;
		if (toks.length == 9) {
			// there are some instances where there appears to be an extra tab in between columns 5
			// and
			// 6
			throw new IllegalStateException("extra column?");
			// toks = new String[] { toks[0], toks[1], toks[2], toks[3], toks[4], toks[6] };
		}
		if (toks.length == 8) {
			String allelicComposition = toks[index++];
			String alleleSymbol = toks[index++];
			Set<MgiGeneID> mgiAlleleIds = getMgiIds(toks[index++]);
			String geneticBackground = toks[index++];
			MammalianPhenotypeID mammalianPhenotypeID = new MammalianPhenotypeID(toks[index++]);
			String[] pubmedIDToks = toks[index++].split(",");
			Set<PubMedID> pubmedIDs = new HashSet<PubMedID>();
			for (String pubmedID : pubmedIDToks) {
				if (pubmedID.trim().length() > 0) {
					pubmedIDs.add(new PubMedID(pubmedID));
				}
			}
			Set<MgiGeneID> mgiAccessionIDs = getMgiIds(toks[index++]);

			Set<OmimID> omimIds = getOmimIds(toks[index++]);
			return new MGIGenoDiseaseFileRecord(allelicComposition, alleleSymbol, mgiAlleleIds, geneticBackground,
					mammalianPhenotypeID, pubmedIDs, mgiAccessionIDs, omimIds, line.getByteOffset(),
					line.getLineNumber());
		}

		String errorMessage = "Unexpected number of tokens on line (" + toks.length + " != 6 expected): "
				+ line.getText().replaceAll("\\t", " [TAB] ");
		logger.error(errorMessage);
		return null;
	}

	/**
	 * @param mgiAccessionIDStr
	 * @return
	 */
	private static Set<MgiGeneID> getMgiIds(String mgiAccessionIDStr) {
		String[] mgiToks = mgiAccessionIDStr.split("[,\\|]");
		Set<MgiGeneID> mgiAccessionIDs = new HashSet<MgiGeneID>();
		for (String mgiID : mgiToks) {
			mgiAccessionIDs.add(new MgiGeneID(mgiID));
		}
		return mgiAccessionIDs;
	}

	private static Set<OmimID> getOmimIds(String inputStr) {
		String[] omimToks = inputStr.split(",");
		Set<OmimID> omimIds = new HashSet<OmimID>();
		for (String omimId : omimToks) {
			omimIds.add(new OmimID(omimId));
		}
		return omimIds;
	}

}
