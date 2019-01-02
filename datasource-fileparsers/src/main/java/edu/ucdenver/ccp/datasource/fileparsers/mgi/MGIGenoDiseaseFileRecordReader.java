package edu.ucdenver.ccp.datasource.fileparsers.mgi;

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
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MammalianPhenotypeID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

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
