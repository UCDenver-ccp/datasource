package edu.ucdenver.ccp.datasource.fileparsers.ncbi.omim;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader.MultiLineBuffer;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;

/**
 * Incomplete representation of data from the OMIM omim.txt file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.OMIM, 
		isComplete=false, 
		comment="This representation of the underlying OMIM data is incomplete.",
		license=License.OMIM,
		citation="http://omim.org/help/faq#1.8",
		label="OMIM record (INC)")
public class OmimTxtFileData extends MultiLineFileRecord {
	private static final Logger logger = Logger.getLogger(OmimTxtFileData.class);
	// public static final String RECORD_NAME_PREFIX = "OMIM_TXT_FILE_RECORD_";
	private static final String FIELD_TAG = "*FIELD*";

	private static final String MIM_NUMBER_TAG = "NO";
	private static final String MIM_TITLE_TAG = "TI";

	@RecordField(comment="", isKeyField=true)
	private final OmimID mimNumber;

	@RecordField(comment="")
	private final String title;

	@RecordField(comment="")
	private final Set<String> alternativeTitles;

	public OmimTxtFileData(OmimID mimNumber, String title, Set<String> alternativeTitles,
			long byteOffset) {
		super(byteOffset);
		this.mimNumber = mimNumber;
		this.title = title;
		this.alternativeTitles = alternativeTitles;
	}

	public OmimID getMimNumber() {
		return mimNumber;
	}

	public String getTitle() {
		return title;
	}

	public Set<String> getAlternativeTitles() {
		return alternativeTitles;
	}

	public static OmimTxtFileData parseOmimTxtFileRecord(MultiLineBuffer multiLineBuffer) {
		try {
			BufferedReader br = new BufferedReader(new StringReader(multiLineBuffer.toString()));
			String line;
			Map<String, String> fieldID2TextMap = new HashMap<String, String>();
			while ((line = br.readLine()) != null) {
				if (line.startsWith(OmimTxtFileParser.RECORD_TAG) || line.startsWith(OmimTxtFileParser.THE_END_TAG)) {
					logger.error("Encountered *RECORD* or *THEEND* tag within a record. This should not happen.");
					return null;
				} else if (line.startsWith(FIELD_TAG)) {
					String fieldID = line.substring(7).trim();
					getFieldText(br, fieldID, fieldID2TextMap);
				}
			}

			OmimID mimNumber = new OmimID(fieldID2TextMap.get(MIM_NUMBER_TAG));
			String title = new String(getMimTitle(fieldID2TextMap.get(MIM_TITLE_TAG)));
			Set<String> alternativeTitles = getMimAlternativeTitles(fieldID2TextMap.get(MIM_TITLE_TAG));
			return new OmimTxtFileData(mimNumber, title, alternativeTitles, multiLineBuffer.getByteOffset());
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private static Set<String> getMimAlternativeTitles(String titleText) {
		titleText = titleText.replaceAll("\\n", " ");
		if (titleText.contains(";;")) {
			String[] titles = titleText.split(";;");
			Set<String> alternativeTitles = new HashSet<String>();
			for (int i = 1; i < titles.length; i++) {
				alternativeTitles.add(new String(titles[i].trim()));
			}
			return alternativeTitles;
		}

		return new HashSet<String>();
	}

	private static String getMimTitle(String titleText) {
		titleText = titleText.replaceAll("\\n", " ");
		if (titleText.contains(";;")) {
			titleText = titleText.substring(0, titleText.indexOf(";;"));
		}
		titleText = titleText.substring(titleText.indexOf(" ")).trim();
		return titleText;
	}

	private static void getFieldText(BufferedReader br, String fieldID, Map<String, String> fieldID2TextMap)
			throws IOException {
		StringBuffer fieldText = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			if (line.startsWith(FIELD_TAG)) {
				br.reset();
				if (fieldID2TextMap.containsKey(fieldID)) {
					String newText = fieldID2TextMap.get(fieldID) + " " + fieldText.toString().trim();
					fieldID2TextMap.remove(fieldID);
					fieldID2TextMap.put(fieldID, newText);
				} else {
					fieldID2TextMap.put(fieldID, fieldText.toString().trim());
				}
				break;
			}

			fieldText.append(line + "\n");
			br.mark(1024);
		}
	}

}
