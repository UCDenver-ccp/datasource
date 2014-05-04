/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.taxonaware;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public abstract class TaxonAwareSingleLineFileRecordReader<T extends SingleLineFileRecord> extends
		SingleLineFileRecordReader<T> {

	protected final Set<NcbiTaxonomyID> taxonsOfInterest;

	public TaxonAwareSingleLineFileRecordReader(InputStream stream, CharacterEncoding encoding, String skipLinePrefix,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(stream, encoding, skipLinePrefix);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstLineWithTaxonOfInterest();
	}

	public TaxonAwareSingleLineFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(dataFile, encoding, skipLinePrefix);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstLineWithTaxonOfInterest();
	}

	public TaxonAwareSingleLineFileRecordReader(File dataFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(dataFile, encoding, null);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstLineWithTaxonOfInterest();
	}

	public TaxonAwareSingleLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			String ftpUsername, String ftpPassword, boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest)
			throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstLineWithTaxonOfInterest();
	}

	public TaxonAwareSingleLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, null, null, clean);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstLineWithTaxonOfInterest();
	}

	protected abstract NcbiTaxonomyID getLineTaxon(Line line);

	/**
	 * @param line
	 * @return true if no taxons of interest have been specified (null or empty), or if the line is
	 *         associated with one of the taxons of interest
	 */
	protected boolean isLineOfInterest(Line line) {
		boolean is =   taxonsOfInterest == null || taxonsOfInterest.isEmpty() || taxonsOfInterest.contains(getLineTaxon(line));
		return is;
		
	}

	protected void initializeToFirstLineWithTaxonOfInterest() throws IOException {
		while ((line != null) && !isLineOfInterest(line)) {
			line = readLine();
			// cycling to the first line with appropriate taxon
		}
	}

	@Override
	public boolean hasNext() {
		return line != null;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		T recordToReturn = parseRecordFromLine(line);

		try {
			while (((line = readLine()) != null) && !isLineOfInterest(line)) {
				// cycling to the first line with appropriate taxon
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return recordToReturn;
	}

}
