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
package edu.ucdenver.ccp.fileparsers.ncbi.omim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader.MultiLineBuffer;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

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
	private final OmimRecordTitle title;

	@RecordField(comment="")
	private final Set<OmimRecordTitle> alternativeTitles;

	public OmimTxtFileData(OmimID mimNumber, OmimRecordTitle title, Set<OmimRecordTitle> alternativeTitles,
			long byteOffset) {
		super(byteOffset);
		this.mimNumber = mimNumber;
		this.title = title;
		this.alternativeTitles = alternativeTitles;
	}

	public OmimID getMimNumber() {
		return mimNumber;
	}

	public OmimRecordTitle getTitle() {
		return title;
	}

	public Set<OmimRecordTitle> getAlternativeTitles() {
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
			OmimRecordTitle title = new OmimRecordTitle(getMimTitle(fieldID2TextMap.get(MIM_TITLE_TAG)));
			Set<OmimRecordTitle> alternativeTitles = getMimAlternativeTitles(fieldID2TextMap.get(MIM_TITLE_TAG));
			return new OmimTxtFileData(mimNumber, title, alternativeTitles, multiLineBuffer.getByteOffset());
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private static Set<OmimRecordTitle> getMimAlternativeTitles(String titleText) {
		titleText = titleText.replaceAll("\\n", " ");
		if (titleText.contains(";;")) {
			String[] titles = titleText.split(";;");
			Set<OmimRecordTitle> alternativeTitles = new HashSet<OmimRecordTitle>();
			for (int i = 1; i < titles.length; i++) {
				alternativeTitles.add(new OmimRecordTitle(titles[i].trim()));
			}
			return alternativeTitles;
		}

		return new HashSet<OmimRecordTitle>();
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
