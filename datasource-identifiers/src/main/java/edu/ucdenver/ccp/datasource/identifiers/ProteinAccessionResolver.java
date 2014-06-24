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
	private static final String VALID_UNIPROT_PATTERN_1 = "[A-NR-Z][0-9][A-Z][A-Z0-9][A-Z0-9][0-9]";
	private static final String VALID_UNIPROT_PATTERN_2 = "[OPQ][0-9][A-Z0-9][A-Z0-9][A-Z0-9][0-9]";

	public static DataSourceIdentifier<String> resolveProteinAccession(String acc) {
		acc = acc.toUpperCase();
		if (acc.matches("[A-Z][A-Z]_\\d+\\.?\\d*")) {
			return new RefSeqID(acc);
		}
		if (acc.matches(VALID_UNIPROT_PATTERN_1) || acc.matches(VALID_UNIPROT_PATTERN_2)) {
			return new UniProtID(acc);
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
		}
		throw new IllegalArgumentException("Input is not a known protein accession pattern: " + acc);
	}

}
