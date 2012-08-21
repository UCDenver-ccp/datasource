package edu.ucdenver.ccp.fileparsers;

public abstract class SingleLineFileRecord extends FileRecord {

	private final long lineNumber;

	public SingleLineFileRecord(long byteOffset, long lineNumber) {
		super(byteOffset);
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the lineNumber
	 */
	public long getLineNumber() {
		return lineNumber;
	}

}
