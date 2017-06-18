package edu.ucdenver.ccp.datasource.fileparsers.taxonaware;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

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
	}

	public TaxonAwareSingleLineFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(dataFile, encoding, skipLinePrefix);
		this.taxonsOfInterest = taxonsOfInterest;
	}

	public TaxonAwareSingleLineFileRecordReader(File dataFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(dataFile, encoding, null);
		this.taxonsOfInterest = taxonsOfInterest;
	}

	public TaxonAwareSingleLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			String ftpUsername, String ftpPassword, boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest)
			throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
		this.taxonsOfInterest = taxonsOfInterest;
	}

	public TaxonAwareSingleLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(workDirectory, encoding, skipLinePrefix, null, null, clean);
		this.taxonsOfInterest = taxonsOfInterest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader#initialize()
	 */
	@Override
	protected void initialize() throws IOException {
		String fileHeader = getFileHeader();
		validateFileHeader(fileHeader);
		// initializeToFirstLineWithTaxonOfInterest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader#parseRecordFromLine(edu
	 * .ucdenver.ccp.common.file.reader.Line)
	 */
	@Override
	protected T parseRecordFromLine(Line line) {
		// TODO Auto-generated method stub
		return null;
	}

	protected abstract NcbiTaxonomyID getLineTaxon(Line line);

	/**
	 * @param line
	 * @return true if no taxons of interest have been specified (null or empty), or if the line is
	 *         associated with one of the taxons of interest
	 */
	protected boolean isLineOfInterest(Line line) {
		if (line == null) {
			return false;
		}
		boolean is = taxonsOfInterest == null || taxonsOfInterest.isEmpty()
				|| taxonsOfInterest.contains(getLineTaxon(line));
		return is;

	}

	protected void advanceToNextLineWithTaxonOfInterest() throws IOException {
//		System.out.println("advancing");
		while ((line = readLine()) != null && !isLineOfInterest(line)) {
			// line = readLine();
//			System.out.println("skipping line");
			// cycling to the first line with appropriate taxon
		}
	}

	@Override
	public boolean hasNext() {
		try {
			if (line == null) {
				advanceToNextLineWithTaxonOfInterest();
			}
			return line != null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
