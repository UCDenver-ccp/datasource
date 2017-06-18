/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DdbjId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTraceId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;

/**
 * Resolution of accession identifiers based on prefixes available here:
 * http://www.ncbi.nlm.nih.gov/Sequin/acc.html
 * 
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class NucleotideAccessionResolver {

	private static final Logger logger = Logger.getLogger(NucleotideAccessionResolver.class);

	private static final Pattern ACC_PATTERN = Pattern.compile("([A-Z]+)\\d+\\.?\\d*");

	private static final Set<String> GENBANK_ID_PREFIXES = CollectionsUtil.createSet("H", "N", "T", "R", "W", "AA",
			"AI", "AW", "BE", "BF", "BG", "BI", "BM", "BQ", "BU", "CA", "CB", "CD", "CF", "CK", "CN", "CO", "CV", "CX",
			"DN", "DR", "DT", "DV", "DW", "DY", "EB", "EC", "EE", "EG", "EH", "EL", "ES", "EV", "EW", "EX", "EY", "FC",
			"FD", "FE", "FF", "FG", "FK", "FL", "GD", "GE", "GH", "GO", "GR", "GT", "GW", "HO", "HS", "JG", "JK", "JZ",
			"U", "AF", "AY", "DQ", "EF", "EU", "FJ", "GQ", "GU", "HM", "HQ", "JF", "JN", "JQ", "JX", "KC", "KF", "KJ",
			"KM", "KP", "KR", "KT", "KU", "KX", "AE", "CP", "CY", "B", "AQ", "AZ", "BH", "BZ", "CC", "CE", "CG", "CL", "CW",
			"CZ", "DU", "DX", "ED", "EI", "EJ", "EK", "ER", "ET", "FH", "FI", "GS", "HN", "HR", "JJ", "JM", "JS", "JY",
			"KG", "KO", "KS", "AC", "DP", "I", "AR", "DZ", "EA", "GC", "GP", "GV", "GX", "GY", "GZ", "HJ", "HK", "HL",
			"G", "BV", "GF", "BK", "BL", "GJ", "GK", "EZ", "HP", "JI", "JL", "JO", "JP", "JR", "JT", "JU", "JV", "JW",
			"KA", "S", "AD", "AH", "AS", "BC", "BT", "J", "K", "L", "M", "N", "CH", "CM", "DS", "EM", "EN", "EP", "EQ",
			"FA", "GG", "GL", "JH", "KB", "KD", "KE", "KI", "KK", "KL", "KN", "KQ", "KV");
	private static final Set<String> EMBL_ID_PREFIXES = CollectionsUtil.createSet("AN", "F", "V", "X", "Y", "Z", "AJ",
			"AM", "FM", "FN", "HE", "HF", "HG", "FO", "LK", "LL", "LM", "LN", "LO", "LP", "LQ", "LR", "LS", "LT", "AL",
			"BX", "CR", "CT", "CU", "FP", "FQ", "FR", "A", "AX", "CQ", "CS", "FB", "GM", "GN", "HA", "HB", "HC", "HD",
			"HH", "HI", "JA", "JB", "JC", "JD", "JE", "BN");
	private static final Set<String> DDBJ_ID_PREFIXES = CollectionsUtil.createSet("BA", "DF", "DG", "LD", "C", "AT",
			"AU", "AV", "BB", "BJ", "BP", "BW", "BY", "CI", "CJ", "DA", "DB", "DC", "DK", "FS", "FY", "HX", "HY", "D",
			"AB", "LC", "AP", "BS", "AG", "DE", "DH", "FT", "GA", "LB", "AK", "E", "BD", "DD", "DI", "DJ", "DL", "DM",
			"FU", "FV", "FW", "FZ", "GB", "HV", "HW", "HZ", "LF", "LG", "BR", "HT", "HU", "FX", "LA", "LE", "LH", "LI",
			"LJ");

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

	/**
	 * @param acc
	 * @param idWithPrefix
	 *            - optional, is only used as part of the error message if the
	 *            acc cannot be resolved. Often the prefix is stripped prior to
	 *            id resolution, this parameter allows the prefix to be included
	 *            in the error message.
	 * @return
	 */
	public static DataSourceIdentifier<String> resolveNucleotideAccession(String acc) {
		return resolveNucleotideAccession(acc, null);
	}
	
	public static DataSourceIdentifier<String> resolveNucleotideAccession(String acc, String idWithPrefix) {
		acc = acc.toUpperCase().trim();
		if (acc.matches("[A-Z][A-Z]_\\d+\\.?\\d*")) {
			return new RefSeqID(acc);
		}
		if (acc.matches("TI\\d{8}")) {
			return new NcbiTraceId(acc);
		}
		Matcher m = ACC_PATTERN.matcher(acc);
		if (m.find()) {
			String prefix = m.group(1);
			if (prefix.length() == 5 && prefix.startsWith("A")) {
				return new DdbjId(acc);
			}
			if (prefix.length() == 4
					&& (prefix.startsWith("A") || prefix.startsWith("D") || prefix.startsWith("G")
							|| prefix.startsWith("J") || prefix.startsWith("L") || prefix.startsWith("M")
							|| prefix.startsWith("N") || prefix.startsWith("K") || prefix.startsWith("P")
							|| prefix.startsWith("Q") || prefix.startsWith("R"))) {
				return new GenBankID(acc);
			}
			if (prefix.length() == 4
					&& (prefix.startsWith("B") || prefix.startsWith("E") || prefix
							.startsWith("I"))) {
				return new DdbjId(acc);
			}
			if (prefix.length() == 4 && prefix.startsWith("C") || prefix.startsWith("F") || prefix.startsWith("O")
					|| prefix.startsWith("H")) {
				return new EmblID(acc);
			}
			Class<? extends DataSourceIdentifier<String>> idClass = prefixToIdClass.get(prefix);
			if (idClass != null) {
				try {
					return idClass.getConstructor(String.class).newInstance(acc);
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
		// logger.warn("Input is not a known nucleotide accession: " + acc);
		if (idWithPrefix == null) {
			idWithPrefix = acc;
		}
		return new ProbableErrorDataSourceIdentifier(idWithPrefix, null, "Input is not a known accession: "
				+ idWithPrefix);
	}

}
