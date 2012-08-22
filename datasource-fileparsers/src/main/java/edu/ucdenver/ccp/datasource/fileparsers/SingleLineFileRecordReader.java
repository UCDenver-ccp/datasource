package edu.ucdenver.ccp.datasource.fileparsers;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;

public abstract class SingleLineFileRecordReader<T extends SingleLineFileRecord> extends FileRecordReader<T> {

	private Line nextLine;

	public SingleLineFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		super(dataFile, encoding, skipLinePrefix);
	}

	public SingleLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			String ftpUsername, String ftpPassword, boolean clean) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
	}

	/**
	 * Overriding to determine initial status of {@link #hasNext()}
	 */
	@Override
	protected void initialize() throws IOException {
		super.initialize();
		nextLine = readLine();
	}

	@Override
	public boolean hasNext() {
		return nextLine != null;
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		T recordToReturn = parseRecordFromLine(nextLine);

		try {
			nextLine = readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return recordToReturn;
	}

	protected abstract T parseRecordFromLine(Line line);
}
