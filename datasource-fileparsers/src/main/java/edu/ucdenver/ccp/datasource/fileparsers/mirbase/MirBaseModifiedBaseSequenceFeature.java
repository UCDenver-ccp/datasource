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
public class MirBaseModifiedBaseSequenceFeature implements SequenceFeature {

	@RecordField
	private final String key = "modified_base";
	@RecordField
	private final SequenceFeatureLocation location;
	@RecordField
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

	@Record(dataSource = DataSource.MIRBASE)
	@Data
	public static class MirBaseModifiedBaseSequenceFeatureQualifierSet implements SequenceFeatureQualifierSet {
		@RecordField
		private final String modBase;
	}

}
