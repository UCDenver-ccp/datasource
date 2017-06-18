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
@Record(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_RECORD, dataSource = DataSource.MIRBASE)
public class MirBaseMiRnaSequenceFeature implements SequenceFeature {

	@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_RECORD___KEY_FIELD_VALUE)
	private final String key = "miRNA";
	@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_RECORD___LOCATION_FIELD_VALUE)
	private final SequenceFeatureLocation location;
	@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_RECORD___QUALIFIER_SET_FIELD_VALUE)
	private final MirBaseMiRnaSequenceFeatureQualifierSet qualifierSet;
	
	
	/**
	 * example:
	 * 
	 * <pre>
	 * 		FT   miRNA           17..38
	 * 			FT                   /accession="MIMAT0000001"
	 * 			FT                   /product="cel-let-7-5p"
	 * 			FT                   /evidence=experimental
	 * 			FT                   /experiment="cloned [1-3,5], Northern [1], PCR [4], Solexa
	 * 			FT                   [6], CLIPseq [7]"
	 * </pre>
	 * 
	 * @param line
	 * @param br
	 * @return
	 */
	public static MirBaseMiRnaSequenceFeature extractFeature(List<String> featureTableLines) {
		String[] toks = featureTableLines.get(0).substring(2).trim().split("\\s+");
		if (!toks[0].equals("miRNA")) {
			throw new IllegalArgumentException("Cannot parse miRNA sequence feature from feature: " + toks[0]);
		}
		String[] startEnd = toks[1].split("\\.\\.");
		SequenceFeatureLocation location = new SequenceFeatureLocation(Integer.parseInt(startEnd[0]),
				Integer.parseInt(startEnd[1]));

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
		
		String accession = null;
		String product = null;
		String evidence = null;
		String experiment = null;
		String similarity = null;
		for (String qualifierLine : qualifierLines) {
			qualifierLine = qualifierLine.substring(1); // remove leading slash
			String[] qToks = qualifierLine.split("=");
			if (qToks[0].equals("accession")) {
				accession = removeQuotes(qToks[1]);
			} else if (qToks[0].equals("product")) {
				product = removeQuotes(qToks[1]);
			} else if (qToks[0].equals("evidence")) {
				evidence = removeQuotes(qToks[1]);
			} else if (qToks[0].equals("experiment")) {
				experiment = removeQuotes(qToks[1]);
			} else if (qToks[0].equals("similarity")) {
				experiment = removeQuotes(qToks[1]);
			}
			else {
				throw new IllegalArgumentException("Unknown miRNA qualifier: " + qualifierLine);
			}
		}
		MirBaseMiRnaSequenceFeatureQualifierSet qualifierSet = new MirBaseMiRnaSequenceFeatureQualifierSet(accession,
				product, evidence, experiment, similarity);
		return new MirBaseMiRnaSequenceFeature(location, qualifierSet);

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

	@Record(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD, dataSource = DataSource.MIRBASE)
	@Data
	public static class MirBaseMiRnaSequenceFeatureQualifierSet implements SequenceFeatureQualifierSet {
		@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD___ACCESSION_FIELD_VALUE)
		private final String accession;
		@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD___PRODUCT_FIELD_VALUE)
		private final String product;
		@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD___EVIDENCE_FIELD_VALUE)
		private final String evidence;
		@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD___EXPERIMENT_FIELD_VALUE)
		private final String experiment;
		@RecordField(ontClass = CcpExtensionOntology.MIRBASE_MICRORNA_SEQUENCE_FEATURE_QUALIFIER_SET_RECORD___SIMILARITY_FIELD_VALUE)
		private final String similarity;
	}

}
