/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.ncbi.homologene;

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
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.homologene.HomologeneGroupID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

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
	public static Map<EntrezGeneID, HomologeneGroupID> getEntrezGeneID2HomologeneGroupIDMap(File homologeneDataFile,
			CharacterEncoding encoding) throws IOException {
		Map<EntrezGeneID, HomologeneGroupID> entrezGeneID2HomologeneGroupIDMap = new HashMap<EntrezGeneID, HomologeneGroupID>();
		HomoloGeneDataFileParser parser = new HomoloGeneDataFileParser(homologeneDataFile, encoding);

		while (parser.hasNext()) {
			HomoloGeneDataFileData dataRecord = parser.next();
			EntrezGeneID entrezGeneID = dataRecord.getEntrezGeneID();
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
