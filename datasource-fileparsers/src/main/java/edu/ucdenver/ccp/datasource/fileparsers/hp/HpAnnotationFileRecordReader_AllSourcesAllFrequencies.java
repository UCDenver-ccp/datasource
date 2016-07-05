package edu.ucdenver.ccp.datasource.fileparsers.hp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;

public class HpAnnotationFileRecordReader_AllSourcesAllFrequencies extends HpAnnotationFileRecordReader {

	private static final String DOWNLOAD_URL = "http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt";

	@HttpDownload(url = DOWNLOAD_URL, decompress = false)
	private File hpAnnotationFile;

	public HpAnnotationFileRecordReader_AllSourcesAllFrequencies(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		/*
		 * Note: this file is downloaded in a compressed format even though it
		 * does not have a .gz suffix
		 */
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(hpAnnotationFile)), encoding,
				skipLinePrefix);
	}

}
