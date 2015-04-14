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
package edu.ucdenver.ccp.fileparsers.reactome;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.reactome.ReactomeReactionID;
import edu.ucdenver.ccp.fileparsers.idlist.IdListFileFactory;

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
			Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(file, encoding, null, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, DataSource.UNIPROT, taxonIds,
				UniProtID.class);
		logger.info("Loaded " + ((taxonIds == null) ? "0" : taxonSpecificIds.size())
				+ " taxon specific ids for taxon(s): " + ((taxonIds == null) ? "none specified" : taxonIds.toString()));
	}

	public ReactomeUniprot2PathwayStidTxtFileParser(File workDirectory, boolean clean, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, DataSource.UNIPROT, taxonIds,
				UniProtID.class);
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

	/**
	 * Create a mapping between the KEGG pathway ID and the KEGG pathway name
	 */
	public static Map<ReactomeReactionID, String> createReactomeReactionID2NameMap(File uniprot2pathwaysStidTxtFile,
			CharacterEncoding encoding) throws IOException {
		Map<ReactomeReactionID, String> reactomeReactionID2NameMap = new HashMap<ReactomeReactionID, String>();

		ReactomeUniprot2PathwayStidTxtFileParser parser = null;
		try {
			parser = new ReactomeUniprot2PathwayStidTxtFileParser(uniprot2pathwaysStidTxtFile, encoding, null, null);
			while (parser.hasNext()) {
				ReactomeUniprot2PathwayStidTxtFileData dataRecord = parser.next();
				ReactomeReactionID reactionID = dataRecord.getReactionID();
				String reactionName = dataRecord.getReactionName();

				if (!reactomeReactionID2NameMap.containsKey(reactionID)) {
					reactomeReactionID2NameMap.put(reactionID, reactionName);
				} else {
					if (!reactomeReactionID2NameMap.get(reactionID).equals(reactionName)) {
						logger.error("Reaction ID (" + reactionID + ") with multiple reaction names discovered in: "
								+ uniprot2pathwaysStidTxtFile);
					}
				}
			}
			return reactomeReactionID2NameMap;
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		try {
			for (ReactomeUniprot2PathwayStidTxtFileParser parser = new ReactomeUniprot2PathwayStidTxtFileParser(
					new File("/Users/bill/Downloads/UniProt2Reactome_All_Levels.txt")); parser.hasNext();) {
				parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
