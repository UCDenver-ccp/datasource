/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.drugbank;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.datasource.fileparsers.jaxb.XmlFileRecordReader;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class DrugbankXmlFileRecordReader extends XmlFileRecordReader<DrugBankDrugRecord> {

	private static final Logger logger = Logger.getLogger(DrugbankXmlFileRecordReader.class);

	@HttpDownload(url = "http://www.drugbank.ca/system/downloads/current/drugbank.xml.zip", decompress = true, targetFileName = "drugbank.xml")
	private File drugbankXmlFile;

	public DrugbankXmlFileRecordReader(File workDirectory, boolean clean) throws IOException {
		super(ca.drugbank.DrugType.class, workDirectory, clean, null);
	}

	public DrugbankXmlFileRecordReader(File dataFile) throws IOException {
		super(ca.drugbank.DrugType.class, dataFile, null);
	}

	@Override
	protected DrugBankDrugRecord initializeNewRecord(Object entry) {
		if (ca.drugbank.DrugType.class.isInstance(entry)) {
			return new DrugBankDrugRecord((ca.drugbank.DrugType) entry);
		}
		logger.warn("Expected ca.drugbank.DrugType, but observed " + entry.getClass().getName()
				+ ". Skipping record...");
		return null;
	}

	@Override
	protected boolean hasTaxonOfInterest(DrugBankDrugRecord record) {
		return true;
	}

	@Override
	protected InputStream initializeInputStreamFromDownload() throws IOException {
		if (drugbankXmlFile.getName().endsWith(".gz")) {
			return new GZIPInputStream(new FileInputStream(drugbankXmlFile));
		}
		return new FileInputStream(drugbankXmlFile);
	}

}
