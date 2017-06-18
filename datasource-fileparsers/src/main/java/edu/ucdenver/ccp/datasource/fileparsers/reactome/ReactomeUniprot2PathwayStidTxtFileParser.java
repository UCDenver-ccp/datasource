package edu.ucdenver.ccp.datasource.fileparsers.reactome;

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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.datasource.fileparsers.idlist.IdListFileFactory;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * http://www.reactome.org/download/current/uniprot_2_pathways.stid.txt
 * 
 * @author Bill Baumgartner
 * 
 */
public class ReactomeUniprot2PathwayStidTxtFileParser extends
		TaxonAwareSingleLineFileRecordReader<ReactomeUniprot2PathwayStidTxtFileData> {
	private static final Logger logger = Logger.getLogger(ReactomeUniprot2PathwayStidTxtFileParser.class);

	public static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;

	private final Set<UniProtID> taxonSpecificIds;

	@HttpDownload(url = "http://www.reactome.org/download/current/UniProt2Reactome.txt")
	private File uniprot2pathwaysStidTxtFile;

	public ReactomeUniprot2PathwayStidTxtFileParser(File file) throws IOException {
		super(file, ENCODING, null);
		taxonSpecificIds = null;
	}

	//
	// public ReactomeUniprot2PathwayStidTxtFileParser(File workDirectory,
	// boolean clean) throws IOException {
	// super(workDirectory, ENCODING, null, null, null, clean, null);
	// taxonSpecificIds = null;
	// }

	public ReactomeUniprot2PathwayStidTxtFileParser(File file, CharacterEncoding encoding, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		super(file, encoding, null, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonIds, UniProtID.class, cleanIdListFiles);
		logger.info("Loaded " + ((taxonIds == null) ? "0" : taxonSpecificIds.size())
				+ " taxon specific ids for taxon(s): " + ((taxonIds == null) ? "none specified" : taxonIds.toString()));
	}

	public ReactomeUniprot2PathwayStidTxtFileParser(File workDirectory, boolean clean, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonIds, UniProtID.class, cleanIdListFiles);
		logger.info("Loaded " + ((taxonIds == null) ? "0" : taxonSpecificIds.size())
				+ " taxon specific ids for taxon(s): " + ((taxonIds == null) ? "none specified" : taxonIds.toString()));
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(uniprot2pathwaysStidTxtFile, encoding, skipLinePrefix);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		ReactomeUniprot2PathwayStidTxtFileData record = parseRecordFromLine(line);
		if (record == null) {
			return null;
		}
		if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty() && taxonSpecificIds.contains(record.getUniprotID())) {
			// here we have matched the record uniprot id as one of the ids of
			// interest. We don't
			// know exactly what taxon it is however so we just return one
			// (arbitrarily) of the
			// taxon ids of interest. this will ensure this record is returned.
			return taxonsOfInterest.iterator().next();
		}
		return new NcbiTaxonomyID(0);
	}

	@Override
	protected ReactomeUniprot2PathwayStidTxtFileData parseRecordFromLine(Line line) {
		try {
			return ReactomeUniprot2PathwayStidTxtFileData.parseDataLine(line);
		} catch (MalformedURLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			logger.error(e);
		}

		return null;
	}
	
}
