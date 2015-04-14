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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtIsoformID;
import edu.ucdenver.ccp.datasource.identifiers.reactome.ReactomeReactionID;

/**
 * This class represents data contained in the Reactome
 * uniprot_2_pathways.stid.txt file which can be found here:
 * http://reactome.org/download/current/uniprot_2_pathways.stid.txt
 * 
 * @author Bill Baumgartner
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Record(dataSource = DataSource.REACTOME, label = "uniprot2pathway record")
public class ReactomeUniprot2PathwayStidTxtFileData extends SingleLineFileRecord {

	/**
	 * <pre>
	 * uniprot_2_pathways.stid.txt a tab delimited file containing the following columns:
	 * 
	 * 1) UniProt ID
	 * 2) Reactome Reaction ID
	 * 3) Reactome Pathway Name
	 * 4) Reactome Reaction URL
	 * 
	 * </pre>
	 */
	private static Logger logger = Logger.getLogger(ReactomeUniprot2PathwayStidTxtFileData.class);
	@RecordField
	// could also be a uniprot isoform ID
	private final DataSourceIdentifier<?> uniprotID;
	@RecordField
	private final ReactomeReactionID reactionID;
	@RecordField
	private final URL reactionURL;
	@RecordField
	private final String reactionName;
	@RecordField
	private final String evidenceCode;
	@RecordField
	private final String taxonName;

	public ReactomeUniprot2PathwayStidTxtFileData(DataSourceIdentifier<?> uniprotID, ReactomeReactionID reactionID, URL reactionURL,
			String reactionName, String evidenceCode, String taxonName, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.uniprotID = uniprotID;
		this.reactionID = reactionID;
		this.reactionName = reactionName;
		this.reactionURL = reactionURL;
		this.evidenceCode = evidenceCode;
		this.taxonName = taxonName;
	}

	/**
	 * Parse a line from the EntrezGene gene_info file
	 * 
	 * @param line
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public static ReactomeUniprot2PathwayStidTxtFileData parseDataLine(Line line) throws MalformedURLException,
			URISyntaxException {
		String[] toks = line.getText().split("\\t");
		if (toks.length != 6) {
			logger.error("Unexpected number of tokens (" + toks.length + ") on line:"
					+ line.getText().replaceAll("\\t", " [TAB] "));
		}
		DataSourceIdentifier<?> uniprotID = null;
		try {
			if (toks[0].contains("-")) {
				uniprotID = new UniProtIsoformID(toks[0]);
			} else {
				uniprotID = new UniProtID(toks[0]);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Invalid uniprot ID " + toks[0] + " . Skipping record : " + line.getText());
			return null;
		}
		ReactomeReactionID reactionID = new ReactomeReactionID(toks[1]);
		URL reactionURL = new URI(toks[2]).toURL();
		String reactionName = toks[3];
		String evidenceCode = toks[4];
		String taxonName = toks[5];
		
		return new ReactomeUniprot2PathwayStidTxtFileData(uniprotID, reactionID, reactionURL, reactionName,
				evidenceCode, taxonName, line.getByteOffset(), line.getLineNumber());

	}

}
