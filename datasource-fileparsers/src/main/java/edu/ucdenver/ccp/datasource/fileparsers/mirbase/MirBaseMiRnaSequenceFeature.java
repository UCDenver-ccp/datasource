/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.mirbase;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.fileparsers.ebi.embl.SequenceFeature;
import edu.ucdenver.ccp.fileparsers.ebi.embl.SequenceFeatureLocation;
import edu.ucdenver.ccp.fileparsers.ebi.embl.SequenceFeatureQualifierSet;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Data
@Record(dataSource = DataSource.MIRBASE)
public class MirBaseMiRnaSequenceFeature implements SequenceFeature {

	@RecordField
	private final String key = "miRNA";
	@RecordField
	private final SequenceFeatureLocation location;
	@RecordField
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

	@Record(dataSource = DataSource.MIRBASE)
	@Data
	public static class MirBaseMiRnaSequenceFeatureQualifierSet implements SequenceFeatureQualifierSet {
		@RecordField
		private final String accession;
		@RecordField
		private final String product;
		@RecordField
		private final String evidence;
		@RecordField
		private final String experiment;
		@RecordField
		private final String similarity;
	}

}
