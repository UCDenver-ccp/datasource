/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.rgd;

import java.io.File;
import java.io.IOException;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.fileparsers.format.gaf2.Gaf2FileRecordReader;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RgdRatGenePwAnnotationFileRecordReader extends Gaf2FileRecordReader<RgdAnnotationGaf2FileRecord> {

	@FtpDownload(server = "rgd.mcw.edu", path = "pub/data_release/annotated_rgd_objects_by_ontology/", filename = "rattus_genes_pw", filetype = FileType.ASCII)
	private File annotationFile;

	/**
	 * @param file
	 * @param encoding
	 * @param idResolverClass
	 * @throws IOException
	 */
	public RgdRatGenePwAnnotationFileRecordReader(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, RgdAnnotationFileIdResolver.class);
	}

	/**
	 * Default constructor
	 * 
	 * @param workDirectory
	 * @param clean
	 * @throws IOException
	 */
	public RgdRatGenePwAnnotationFileRecordReader(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, CharacterEncoding.US_ASCII, clean, RgdAnnotationFileIdResolver.class);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(annotationFile, encoding, skipLinePrefix);
	}

	@Override
	protected RgdAnnotationGaf2FileRecord parseRecordFromLine(Line line) {
		return new RgdAnnotationGaf2FileRecord(Gaf2FileRecordReader.parseGaf2FileLine(line));
	}

}
