/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.rgd;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

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
public class RgdRatGeneMpAnnotationFileRecordReader extends Gaf2FileRecordReader<RgdAnnotationGaf2FileRecord> {

	@FtpDownload(server = "rgd.mcw.edu", path = "pub/data_release/annotated_rgd_objects_by_ontology/", filename = "rattus_genes_mp", filetype = FileType.ASCII)
	private File annotationFile;

	/**
	 * @param file
	 * @param encoding
	 * @param idResolverClass
	 * @throws IOException
	 */
	public RgdRatGeneMpAnnotationFileRecordReader(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, RgdAnnotationFileIdResolver.class);
	}

	/**
	 * Default constructor
	 * 
	 * @param workDirectory
	 * @param clean
	 * @throws IOException
	 */
	public RgdRatGeneMpAnnotationFileRecordReader(File workDirectory, boolean clean) throws IOException {
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

	public static void main(String[] args) {
		BasicConfigurator.configure();
		try {
//			RgdRatGeneMpAnnotationFileRecordReader rr = new RgdRatGeneMpAnnotationFileRecordReader(new File("/tmp"), false);
//			RgdRatGeneRdoAnnotationFileRecordReader rr = new RgdRatGeneRdoAnnotationFileRecordReader(new File("/tmp"), false);
//			RgdRatGeneNboAnnotationFileRecordReader rr = new RgdRatGeneNboAnnotationFileRecordReader(new File("/tmp"), false);
			RgdRatGenePwAnnotationFileRecordReader rr = new RgdRatGenePwAnnotationFileRecordReader(new File("/tmp"), false);
			while (rr.hasNext()) {
				rr.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
