package edu.ucdenver.ccp.datasource.fileparsers.hp;

import java.io.File;
import java.io.IOException;

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
		return new StreamLineReader(hpAnnotationFile, encoding, skipLinePrefix);
	}

}
