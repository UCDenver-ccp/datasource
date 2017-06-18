package edu.ucdenver.ccp.datasource.fileparsers.ncbi.homologene;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HomologeneGroupID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * This class is used to parse the EntrezGene gene_info file.
 * 
 * @author Bill Baumgartner
 * 
 */
public class HomoloGeneDataFileParser extends TaxonAwareSingleLineFileRecordReader<HomoloGeneDataFileData> {

	public static final String FTP_FILE_NAME = "homologene.data";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	private static Logger logger = Logger.getLogger(HomoloGeneDataFileParser.class);

	@FtpDownload(server = FtpHost.HOMOLOGENE_HOST, path = FtpHost.HOMOLOGENE_PATH, filename = FTP_FILE_NAME, filetype = FileType.ASCII)
	private File homologeneDataFile;

	public HomoloGeneDataFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	public HomoloGeneDataFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
	}

	public HomoloGeneDataFileParser(File dataFile, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIdsOfInterest)
			throws IOException {
		super(dataFile, encoding, taxonIdsOfInterest);
	}

	public HomoloGeneDataFileParser(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIdsOfInterest)
			throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIdsOfInterest);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(homologeneDataFile, encoding, skipLinePrefix);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		HomoloGeneDataFileData record = parseRecordFromLine(line);
		return record.getTaxonomyID();
	}

	@Override
	protected HomoloGeneDataFileData parseRecordFromLine(Line line) {
		return HomoloGeneDataFileData.parseHomologeneDataLine(line);
	}

	/**
	 * Returns a map from EntrezGene ID to Homologene Group ID.
	 * 
	 * @param homologeneDataFile
	 * @return
	 * @throws IOException
	 */
	public static Map<NcbiGeneId, HomologeneGroupID> getEntrezGeneID2HomologeneGroupIDMap(File homologeneDataFile,
			CharacterEncoding encoding) throws IOException {
		Map<NcbiGeneId, HomologeneGroupID> entrezGeneID2HomologeneGroupIDMap = new HashMap<NcbiGeneId, HomologeneGroupID>();
		HomoloGeneDataFileParser parser = new HomoloGeneDataFileParser(homologeneDataFile, encoding);

		while (parser.hasNext()) {
			HomoloGeneDataFileData dataRecord = parser.next();
			NcbiGeneId entrezGeneID = dataRecord.getEntrezGeneID();
			HomologeneGroupID homologeneGroupID = dataRecord.getHomologeneGroupID();
			if (entrezGeneID2HomologeneGroupIDMap.containsKey(entrezGeneID)) {
				logger.error("Multiple entries for single gene..  " + entrezGeneID);
			} else {
				entrezGeneID2HomologeneGroupIDMap.put(entrezGeneID, homologeneGroupID);
			}
		}

		parser.close();

		return entrezGeneID2HomologeneGroupIDMap;
	}

}
