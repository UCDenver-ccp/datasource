/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.other.DdbjId;

/**
 * Resolution of accession identifiers based on prefixes available here:
 * http://www.ncbi.nlm.nih.gov/Sequin/acc.html
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class NucleotideAccessionResolver {

	private static final Pattern ACC_PATTERN = Pattern.compile("([A-Z]+)\\d+\\.?\\d*");

	private static final Set<String> GENBANK_ID_PREFIXES = CollectionsUtil.createSet("CH", "CM", "DS", "EM", "EN",
			"EP", "EQ", "FA", "GG", "GL", "JH", "KB", "H", "N", "T", "R", "W", "AA", "AI", "AW", "BE", "BF", "BG",
			"BI", "BM", "BQ", "BU", "CA", "CB", "CD", "CF", "CK", "CN", "CO", "CV", "CX", "DN", "DR", "DT", "DV", "DW",
			"DY", "EB", "EC", "EE", "EG", "EH", "EL", "ES", "EV", "EW", "EX", "EY", "FC", "FD", "FE", "FF", "FG", "FK",
			"FL", "GD", "GE", "GH", "GO", "GR", "GT", "GW", "HO", "HS", "JG", "JK", "JZ", "U", "AF", "AY", "DQ", "EF",
			"EU", "FJ", "GQ", "GU", "HM", "HQ", "JF", "JN", "JQ", "JX", "KC", "AE", "CP", "CY", "B", "AQ", "AZ", "BH",
			"BZ", "CC", "CE", "CG", "CL", "CW", "CZ", "DU", "DX", "ED", "EI", "EJ", "EK", "ER", "ET", "FH", "FI", "GS",
			"HN", "HR", "JJ", "JM", "JS", "JY", "AC", "DP", "I", "AR", "DZ", "EA", "GC", "GP", "GV", "GX", "GY", "GZ",
			"HJ", "HK", "HL", "G", "BV", "GF", "BK", "BL", "GJ", "GK", "EZ", "HP", "JI", "JL", "JO", "JP", "JR", "JT",
			"JU", "JV", "JW", "KA", "S", "AD", "AH", "AS", "BC", "BT", "J", "K", "L", "M", "N");
	private static final Set<String> EMBL_ID_PREFIXES = CollectionsUtil.createSet("AN", "F", "V", "X", "Y", "Z", "AJ",
			"AM", "FM", "FN", "HE", "HF", "HG", "FO", "AL", "BX", "CR", "CT", "CU", "FP", "FQ", "FR", "A", "AX", "CQ",
			"CS", "FB", "GM", "GN", "HA", "HB", "HC", "HD", "HH", "HI", "JA", "JB", "JC", "JD", "JE", "BN");
	private static final Set<String> DDBJ_ID_PREFIXES = CollectionsUtil.createSet("BA", "DF", "DG", "C", "AT", "AU",
			"AV", "BB", "BJ", "BP", "BW", "BY", "CI", "CJ", "DA", "DB", "DC", "DK", "FS", "FY", "HX", "HY", "D", "AB",
			"AP", "BS", "AG", "DE", "DH", "FT", "GA", "AK", "E", "BD", "DD", "DI", "DJ", "DL", "DM", "FU", "FV", "FW",
			"FZ", "GB", "HV", "HW", "BR", "HT", "HU", "FX");

	private static Map<String, Class<? extends DataSourceIdentifier<String>>> prefixToIdClass;

	static {
		prefixToIdClass = new HashMap<String, Class<? extends DataSourceIdentifier<String>>>();
		for (String prefix : GENBANK_ID_PREFIXES) {
			prefixToIdClass.put(prefix, GenBankID.class);
		}
		for (String prefix : EMBL_ID_PREFIXES) {
			prefixToIdClass.put(prefix, EmblID.class);
		}
		for (String prefix : DDBJ_ID_PREFIXES) {
			prefixToIdClass.put(prefix, DdbjId.class);
		}
	}

	public static DataSourceIdentifier<String> resolveNucleotideAccession(String acc) {
		acc = acc.toUpperCase();
		if (acc.matches("[A-Z][A-Z]_\\d+\\.?\\d*")) {
			return new RefSeqID(acc);
		}
		Matcher m = ACC_PATTERN.matcher(acc);
		if (m.find()) {
			String prefix = m.group(1);
			if (prefix.length() == 5 && prefix.startsWith("A")) {
				return new DdbjId(acc);
			}
			if (prefix.length() == 4 && (prefix.startsWith("A") || prefix.startsWith("D") || prefix.startsWith("G"))) {
				return new GenBankID(acc);
			}
			if (prefix.length() == 4 && (prefix.startsWith("B") || prefix.startsWith("E"))) {
				return new DdbjId(acc);
			}
			if (prefix.length() == 4 && prefix.startsWith("C")) {
				return new EmblID(acc);
			}
			Class<? extends DataSourceIdentifier<String>> idClass = prefixToIdClass.get(prefix);
			if (idClass != null) {
				try {
					return idClass.getConstructor(String.class).newInstance(prefix);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new IllegalArgumentException("Input is not a known nucleotide accession: " + acc);
	}

}
