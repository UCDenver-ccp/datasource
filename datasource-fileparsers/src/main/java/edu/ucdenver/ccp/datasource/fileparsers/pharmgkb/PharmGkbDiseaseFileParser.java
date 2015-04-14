package edu.ucdenver.ccp.fileparsers.pharmgkb;

import java.io.File;
import java.io.IOException;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

public class PharmGkbDiseaseFileParser extends SingleLineFileRecordReader<PharmGkbDiseaseFileRecord> {

	private static final String HEADER = "PharmGKB Accession Id\tName\tAlternate Names\tCross-references\tExternal Vocabulary";

	public static final String PHARMGKB_DISEASE_RECORD_ID_PREFIX = "PHARMGKB_DISEASE_RECORD_";

	private static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;
	@HttpDownload(url = "https://www.pharmgkb.org/download.do?objId=diseases.zip&dlCls=common", fileName = "diseases.zip", targetFileName = "diseases.tsv", decompress=true)
	private File pharmGkbDiseasesFile;

	public PharmGkbDiseaseFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	public PharmGkbDiseaseFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(pharmGkbDiseasesFile, encoding, skipLinePrefix);
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected PharmGkbDiseaseFileRecord parseRecordFromLine(Line line) {
		String[] toks = line.getText().split(RegExPatterns.TAB);
		String pharmGkbAccessionId = toks[0];
		String name = toks[1];
		String alternativeNames = toks.length > 2 ? toks[2] : "";
		String crossReferences = toks.length > 3 ? toks[3] : "";
		String externalVocabulary = toks.length > 4 ? toks[4] : "";
		return new PharmGkbDiseaseFileRecord(pharmGkbAccessionId, name, alternativeNames, crossReferences, externalVocabulary,
				line.getByteOffset(), line.getLineNumber());
	}

}
