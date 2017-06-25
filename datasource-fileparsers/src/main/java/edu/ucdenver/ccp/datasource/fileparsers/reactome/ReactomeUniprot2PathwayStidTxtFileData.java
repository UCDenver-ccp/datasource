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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ReactomeReactionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
