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
import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

public abstract class TaxonAwareMultiLineFileRecordReader<T extends FileRecord> extends MultiLineFileRecordReader<T> {

	protected final Set<NcbiTaxonomyID> taxonsOfInterest;

	public TaxonAwareMultiLineFileRecordReader(File dataFile, CharacterEncoding encoding, String skipLinePrefix,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(dataFile, encoding, skipLinePrefix);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstRecordWithTaxonOfInterest();
	}

	public TaxonAwareMultiLineFileRecordReader(File workDirectory, CharacterEncoding encoding, String skipLinePrefix,
			String ftpUsername, String ftpPassword, boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest)
			throws IOException {
		super(workDirectory, encoding, skipLinePrefix, ftpUsername, ftpPassword, clean);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstRecordWithTaxonOfInterest();
	}

	public TaxonAwareMultiLineFileRecordReader(InputStream stream, CharacterEncoding encoding, String skipLinePrefix,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(stream, encoding, skipLinePrefix);
		this.taxonsOfInterest = taxonsOfInterest;
		initializeToFirstRecordWithTaxonOfInterest();
	}

	protected abstract NcbiTaxonomyID getRecordTaxon(MultiLineBuffer multiLineBuffer);

	/**
	 * @param line
	 * @return true if no taxons of interest have been specified (null or empty), or if the line is
	 *         associated with one of the taxons of interest
	 */
	protected boolean isRecordOfInterest(MultiLineBuffer multiLineBuffer) {
		return taxonsOfInterest == null || taxonsOfInterest.isEmpty()
				|| taxonsOfInterest.contains(getRecordTaxon(buffer));

	}

	protected void initializeToFirstRecordWithTaxonOfInterest() throws IOException {
		if (taxonsOfInterest != null && !taxonsOfInterest.isEmpty()) {
			while ((buffer != null) && !isRecordOfInterest(buffer)) {
				buffer = compileMultiLineBuffer();
				// cycling to the first record with appropriate taxon
			}
		}
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		T record = parseRecordFromMultipleLines(buffer);

		try {
			while (((buffer = compileMultiLineBuffer()) != null) && !isRecordOfInterest(buffer)) {
				// cycling to the first line with appropriate taxon
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return record;
	}

	protected abstract T parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer);

}
