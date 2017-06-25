package edu.ucdenver.ccp.datasource.fileparsers.mirbase;

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

import java.util.ArrayList;
import java.util.List;

import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.SequenceFeature;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.SequenceFeatureLocation;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.SequenceFeatureQualifierSet;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import lombok.Data;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Data
@Record(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_MODIFIED_BASE_SEQUENCE_FEATURE_RECORD, dataSource = DataSource.MIRBASE)
public class MirBaseModifiedBaseSequenceFeature implements SequenceFeature {

	@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_MODIFIED_BASE_SEQUENCE_FEATURE_RECORD___KEY_FIELD_VALUE)
	private final String key = "modified_base";
	@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_MODIFIED_BASE_SEQUENCE_FEATURE_RECORD___LOCATION_FIELD_VALUE)
	private final SequenceFeatureLocation location;
	@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_MODIFIED_BASE_SEQUENCE_FEATURE_RECORD___QUALIFIER_SET_FIELD_VALUE)
	private final MirBaseModifiedBaseSequenceFeatureQualifierSet qualifierSet;

	/**
	 * example:
	 * 
	 * <pre>
	 * 		FT   modified_base   48
	 *      FT                   /mod_base=i
	 * 
	 * </pre>
	 * 
	 * @param line
	 * @param br
	 * @return
	 */
	public static MirBaseModifiedBaseSequenceFeature extractFeature(List<String> featureTableLines) {
		String[] toks = featureTableLines.get(0).substring(2).trim().split("\\s+");
		if (!toks[0].equals("modified_base")) {
			throw new IllegalArgumentException("Cannot parse modified_base sequence feature from feature: " + toks[0]);
		}
		int position = Integer.parseInt(toks[1].trim());
		SequenceFeatureLocation location = new SequenceFeatureLocation(position, null);
		
		List<String> qualifierLines = new ArrayList<String>();
		String currentLine = null;
		for (int i = 1; i < featureTableLines.size(); i++) {
			String line = featureTableLines.get(i).substring(2).trim();
			if (line.startsWith("/")) {
				if (currentLine != null) {
					qualifierLines.add(currentLine);
				}
				currentLine = line;
			} else {
				currentLine += line;
			}
		}
		qualifierLines.add(currentLine);
		String modBase = null;
		for (String qualifierLine : qualifierLines) {
			qualifierLine = qualifierLine.substring(1); // remove leading slash
			String[] qToks = qualifierLine.split("=");
			if (qToks[0].equals("mod_base")) {
				modBase = removeQuotes(qToks[1]);
			}  else {
				throw new IllegalArgumentException("Unknown modified_base qualifier: " + qualifierLine);
			}
		}
		MirBaseModifiedBaseSequenceFeatureQualifierSet qualifierSet = new MirBaseModifiedBaseSequenceFeatureQualifierSet(modBase);
		return new MirBaseModifiedBaseSequenceFeature(location, qualifierSet);

	}

	/**
	 * @param string
	 * @return
	 */
	private static String removeQuotes(String input) {
		if (input.startsWith("\"") && input.endsWith("\"")) {
			return StringUtil.removeLastCharacter(input).substring(1);
		}
		return input;
	}

	@Record(ontClass = CcpExtensionOntology.MIRBASE_MODIFIED_BASE_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD, dataSource = DataSource.MIRBASE)
	@Data
	public static class MirBaseModifiedBaseSequenceFeatureQualifierSet implements SequenceFeatureQualifierSet {
		@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MODIFIED_BASE_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD___MODIFIED_BASE_FIELD_VALUE)
		private final String modBase;
	}

}
