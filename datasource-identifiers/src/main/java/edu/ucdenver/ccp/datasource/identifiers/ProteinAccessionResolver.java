/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
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
public class ProteinAccessionResolver {

	private static final Pattern ACC_PATTERN = Pattern.compile("([A-Z]{3})\\d+\\.?\\d*");

	public static DataSourceIdentifier<String> resolveProteinAccession(String acc) {
		if (acc.matches("[A-Z][A-Z]_\\d+\\.?\\d*")) {
			return new RefSeqID(acc);
		}
		Matcher m = ACC_PATTERN.matcher(acc);
		if (m.find()) {
			String prefix = m.group(1);
			if (prefix.startsWith("A")) {
				return new GenBankID(acc);
			}
			if (prefix.startsWith("B")) {
				return new DdbjId(acc);
			}
			if (prefix.startsWith("C")) {
				return new EmblID(acc);
			}
			if (prefix.startsWith("D")) {
				return new GenBankID(acc);
			}
			if (prefix.startsWith("E")) {
				return new GenBankID(acc);
			}
			if (prefix.startsWith("F")) {
				return new DdbjId(acc);
			}
			if (prefix.startsWith("G")) {
				return new DdbjId(acc);
			}
			if (prefix.startsWith("H")) {
				return new GenBankID(acc);
			}
			if (prefix.startsWith("I")) {
				return new DdbjId(acc);
			}
			if (prefix.startsWith("J")) {
				return new GenBankID(acc);
			}
			if (prefix.startsWith("O")) {
				return new UniProtID(acc);
			}
			if (prefix.startsWith("P")) {
				return new UniProtID(acc);
			}
			if (prefix.startsWith("Q")) {
				return new UniProtID(acc);
			}
		}
		throw new IllegalArgumentException("Input is not a known protein accession pattern: " + acc);
	}

}
