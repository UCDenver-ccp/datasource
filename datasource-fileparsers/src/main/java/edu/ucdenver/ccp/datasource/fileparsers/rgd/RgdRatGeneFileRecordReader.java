package edu.ucdenver.ccp.fileparsers.rgd;

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
