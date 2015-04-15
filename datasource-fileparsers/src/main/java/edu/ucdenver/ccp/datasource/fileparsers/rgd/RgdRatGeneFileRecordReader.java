package edu.ucdenver.ccp.datasource.fileparsers.rgd;

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
import java.io.FileInputStream;
import java.io.IOException;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;

/**
 * 
 * @author bill
 * 
 */
public class RgdRatGeneFileRecordReader extends RgdGeneFileRecordReaderBase {
	public static final String FTP_FILE_NAME = "GENES_RAT.txt";
	private static final String HEADER = "GENE_RGD_ID\tSYMBOL\tNAME\tGENE_DESC\tCHROMOSOME_CELERA\tCHROMOSOME_3.1\tCHROMOSOME_3.4\tFISH_BAND\tSTART_POS_CELERA\tSTOP_POS_CELERA\tSTRAND_CELERA\tSTART_POS_3.1\tSTOP_POS_3.1\tSTRAND_3.1\tSTART_POS_3.4\tSTOP_POS_3.4\tSTRAND_3.4\tCURATED_REF_RGD_ID\tCURATED_REF_PUBMED_ID\tUNCURATED_PUBMED_ID\tENTREZ_GENE\tUNIPROT_ID\t(UNUSED)\tGENBANK_NUCLEOTIDE\tTIGR_ID\tGENBANK_PROTEIN\tUNIGENE_ID\tSSLP_RGD_ID\tSSLP_SYMBOL\tOLD_SYMBOL\tOLD_NAME\tQTL_RGD_ID\tQTL_SYMBOL\tNOMENCLATURE_STATUS\tSPLICE_RGD_ID\tSPLICE_SYMBOL\tGENE_TYPE\tENSEMBL_ID\tGENE_REFSEQ_STATUS\tCHROMOSOME_5.0\tSTART_POS_5.0\tSTOP_POS_5.0\tSTRAND_5.0\tCHROMOSOME_6.0\tSTART_POS_6.0\tSTOP_POS_6.0\tSTRAND_6.0";

	@FtpDownload(server = FTP_SERVER, path = FTP_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File geneFile;

	public RgdRatGeneFileRecordReader(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, clean);
	}

	public RgdRatGeneFileRecordReader(File rgdGeneFile) throws IOException {
		super(rgdGeneFile, ENCODING);
	}

	@Override
	public String getDataSpecificKey() {
		return "rat";
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new FileInputStream(geneFile), encoding, skipLinePrefix);
	}

}
