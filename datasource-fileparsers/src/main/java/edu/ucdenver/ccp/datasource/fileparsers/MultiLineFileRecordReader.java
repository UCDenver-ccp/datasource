package edu.ucdenver.ccp.datasource.fileparsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringConstants;

public abstract class MultiLineFileRecordReader<T extends MultiLineFileRecord> extends FileRecordReader<T> {

	protected Line line;
	private MultiLineBuffer buffer;

	public MultiLineFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		super(dataFile, encoding, skipLinePrefix);
	}

	public MultiLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			String ftpUsername, String ftpPassword, boolean clean) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
	}

	/**
	 * Overriding to determine initial status of {@link #hasNext()}
	 */
	@Override
	protected void initialize() throws IOException {
		super.initialize();
		buffer = compileMultiLineBuffer();
	}

	@Override
	public boolean hasNext() {
		return buffer != null;
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		T record = parseRecordFromMultipleLines(buffer);

		try {
			buffer = compileMultiLineBuffer();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return record;
	}

	protected abstract MultiLineBuffer compileMultiLineBuffer() throws IOException;

	protected abstract T parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer);

	public static class MultiLineBuffer {
		private List<Line> lines;

		public MultiLineBuffer() {
			lines = new ArrayList<Line>();
		}

		public void add(Line line) {
			lines.add(line);
		}

		public long getByteOffset() {
			if (lines.size() > 0)
				return lines.get(0).getByteOffset();
			throw new IllegalStateException("Cannot request byte offset from an empty MultiLineBuffer!");
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Line line : lines) {
				sb.append(line.getText() + StringConstants.NEW_LINE);
			}
			return sb.toString();
		}
	}

}
