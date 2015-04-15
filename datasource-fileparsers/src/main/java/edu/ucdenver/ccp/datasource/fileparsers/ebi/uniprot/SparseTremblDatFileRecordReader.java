package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

public class SparseTremblDatFileRecordReader extends SparseUniProtDatFileRecordReader {

	public static final String FTP_FILE_NAME = "uniprot_trembl.dat.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.UNIPROT_KNOWLEDGEBASE_HOST, path = FtpHost.UNIPROT_KNOWLEDGEBASE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File tremblKbFile;

//	public SparseTremblDatFileRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean)
//			throws IOException {
//		super(workDirectory, encoding, clean, null);
//	}
//
//	public SparseTremblDatFileRecordReader(File uniprotDatFile, CharacterEncoding encoding) throws IOException {
//		super(uniprotDatFile, encoding, null);
//	}

	public SparseTremblDatFileRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean,
			Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(workDirectory, encoding, clean, taxonIds);
	}

	public SparseTremblDatFileRecordReader(File uniprotDatFile, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(uniprotDatFile, encoding, taxonIds);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(tremblKbFile)), encoding, skipLinePrefix);
	}

}
