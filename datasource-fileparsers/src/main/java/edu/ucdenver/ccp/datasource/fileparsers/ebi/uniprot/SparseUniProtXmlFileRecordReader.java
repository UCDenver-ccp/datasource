/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
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
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtFileRecord.DbReference;
import edu.ucdenver.ccp.datasource.fileparsers.jaxb.XmlFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class SparseUniProtXmlFileRecordReader extends XmlFileRecordReader<SparseUniProtFileRecord> {
	private static final Logger logger = Logger.getLogger(SparseUniProtDatFileRecordReader.class);

	// public SparseUniProtXmlFileRecordReader(File workDirectory, boolean
	// clean) throws IOException {
	// super(workDirectory, clean);
	// }
	//
	// public SparseUniProtXmlFileRecordReader(File dataFile) throws IOException
	// {
	// super(dataFile);
	// }

	public SparseUniProtXmlFileRecordReader(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(org.uniprot.Entry.class, workDirectory, clean, taxonIds);
	}

	public SparseUniProtXmlFileRecordReader(File dataFile, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(org.uniprot.Entry.class, dataFile, taxonIds);
	}

	protected InputStream initializeInputStreamFromDownload() throws IOException {
		throw new UnsupportedOperationException(
				"The initializeInputStreamFromDownload() method is designed to be used "
						+ "when a subclass of this class is automatically obtaining the input file. The subclass should initialize "
						+ "the InputStream that will serve the UniProt XML to the XML parsing code.");
	}

	@Override
	protected SparseUniProtFileRecord initializeNewRecord(Object entry) {
		if (org.uniprot.Entry.class.isInstance(entry)) {
			return new SparseUniProtFileRecord((org.uniprot.Entry) entry);
		}
		logger.warn("Expected org.uniprot.Entry, but observed " + entry.getClass().getName() + ". Skipping record...");
		return null;
	}

	@Override
	protected boolean hasTaxonOfInterest(SparseUniProtFileRecord record) {
		if (getTaxonsOfInterest() == null || getTaxonsOfInterest().isEmpty()) {
			return true;
		}
		for (DbReference dbRef : record.getOrganism().getDbReference()) {
			if (getTaxonsOfInterest().contains(dbRef.getId())) {
				System.out.println("has taxon of interest: " + dbRef.getId());
				return true;
			}
		}
		return false;
	}

}
