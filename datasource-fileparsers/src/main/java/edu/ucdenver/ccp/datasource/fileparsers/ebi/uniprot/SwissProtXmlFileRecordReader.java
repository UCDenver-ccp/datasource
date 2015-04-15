package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

public class SwissProtXmlFileRecordReader extends UniProtXmlFileRecordReader {

	public static final String FTP_FILE_NAME = "uniprot_sprot.xml.gz";

	@FtpDownload(server = FtpHost.UNIPROT_KNOWLEDGEBASE_HOST, path = FtpHost.UNIPROT_KNOWLEDGEBASE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File sprotKbFile;

	// public SwissProtXmlFileRecordReader(File workDirectory, boolean clean)
	// throws IOException {
	// super(workDirectory, clean);
	// }
	//
	// public SwissProtXmlFileRecordReader(File uniprotXmlFile) throws
	// IOException {
	// super(uniprotXmlFile);
	// }
	public SwissProtXmlFileRecordReader(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(workDirectory, clean, taxonIds);
	}

	public SwissProtXmlFileRecordReader(File uniprotXmlFile, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(uniprotXmlFile, taxonIds);
	}

	@Override
	protected InputStream initializeInputStreamFromDownload() throws IOException {
		return new GZIPInputStream(new FileInputStream(sprotKbFile));
	}

}
