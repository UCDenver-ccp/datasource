/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro;

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
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.interpro.InterproType;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.fileparsers.jaxb.XmlFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class InterProXmlFileRecordReader extends XmlFileRecordReader<InterProXmlFileRecord> {

	private static final Logger logger = Logger.getLogger(InterProXmlFileRecordReader.class);

	public static final String FTP_FILE_NAME = "interpro.xml.gz";
	@FtpDownload(server = FtpHost.INTERPRO_HOST, path = FtpHost.INTERPRO_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File interProXmlFile;

	public InterProXmlFileRecordReader(File workDirectory, boolean clean)
			throws IOException {
		super(org.interpro.InterproType.class, workDirectory, clean, null);
	}

	public InterProXmlFileRecordReader(File dataFile) throws IOException {
		super(org.interpro.InterproType.class, dataFile, null);
	}

	protected InputStream initializeInputStreamFromDownload() throws IOException {
		return new GZIPInputStream(new FileInputStream(interProXmlFile));
	}

	@Override
	protected InterProXmlFileRecord initializeNewRecord(Object entry) {
		if (org.interpro.InterproType.class.isInstance(entry)) {
			InterproType it = (org.interpro.InterproType) entry;
			if (it.getId() != null) {
				return new InterProXmlFileRecord((org.interpro.InterproType) entry);
			} else {
				return new InterProXmlFileRecord();
			}
		} else if (org.interpro.DeletedEntriesType.class.isInstance(entry)) {
			return new InterProXmlFileRecord();
		} else if (org.interpro.DelRefType.class.isInstance(entry)) {
			return new InterProXmlFileRecord();
		}
		logger.warn("Expected org.interpro.InterproType, but observed " + entry.getClass().getName()
				+ ". Skipping record...");
		return null;
	}

	@Override
	protected boolean hasTaxonOfInterest(InterProXmlFileRecord record) {
		return true;
	}

}
