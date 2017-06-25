/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.uniprot.DbReferenceType;
import org.uniprot.OrganismType;

import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtFileRecord.Organism;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class UniProtDatFileUtil {

	private static final Logger logger = Logger.getLogger(UniProtDatFileUtil.class);

	/**
	 * @param line
	 * @param br
	 * @return
	 */
	public static Collection<UniProtIsoformID> parseCCLine(String line, BufferedReader br) {
		Collection<UniProtIsoformID> isoFormIDs = new ArrayList<UniProtIsoformID>();
		if (line.contains("IsoId=")) {
			Pattern p = Pattern.compile("[A-Z].*?-\\d+");
			Matcher m = p.matcher(line.substring(line.indexOf("=")));
			while (m.find()) {
				// System.out.println("group0: " + m.group());// + " group1: " +m.group(1) +
				// " group2: " + m.group(2));
				// System.out.println("Found Isoform: " + new UniProtIsoformID(m.group()));
				isoFormIDs.add(new UniProtIsoformID(m.group()));
			}
		}
		return isoFormIDs;
	}

	/**
	 * Returns the canonical gene symbol if there is one, null otherwise
	 * 
	 * @param line
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public static List<String> parseGNLineForNameAndSynonyms(String line, BufferedReader br) throws IOException {
		String canonicalName = null;
		Set<String> synonyms = new HashSet<String>();

		canonicalName = lookForGeneNameOnLine(line);
		synonyms.addAll(lookForGeneSynonymsOnLine(line));

		while ((line = br.readLine()) != null && line.startsWith("GN")) {
			if (canonicalName == null) {
				canonicalName = lookForGeneNameOnLine(line);
			}
			synonyms.addAll(lookForGeneSynonymsOnLine(line));
		}

		List<String> returnList = new ArrayList<String>();
		if (canonicalName != null) {
			returnList.add(canonicalName);
			returnList.addAll(synonyms);
		}
		return returnList;
	}

	public static String lookForGeneNameOnLine(String line) {
		Pattern p = Pattern.compile("GN\\s+Name=(.*?);");
		Matcher m = p.matcher(line);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	public static Set<String> lookForGeneSynonymsOnLine(String line) {
		HashSet<String> synonyms = new HashSet<String>();
		Pattern p = Pattern.compile("Synonyms=(.*?);");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String synonymList = m.group(1);
			String toks[] = synonymList.split(",");
			for (String tok : toks)
				synonyms.add(tok);
		}
		return synonyms;
	}

	/**
	 * Returns the full gene name if there is one, null otherwise
	 * 
	 * @param line
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public static String parseDELine(String line, BufferedReader br) throws IOException {
		Pattern p = Pattern.compile("Name:\\s+Full=(.*?);");
		Matcher m = p.matcher(line);
		if (m.find()) {
			return m.group(1);
		}

		br.mark(1024);
		// while ((line = br.readLine()).startsWith("DE")) {
		while ((line = br.readLine()) != null && line.startsWith("DE")) {
			m = p.matcher(line);
			if (m.find()) {
				br.reset();
				return m.group(1);
			}
			br.mark(1024);
		}
		br.reset();

		return null;
	}

	/**
	 * Returns a list containing accession numbers (uniprot IDs) parsed from the AC line.
	 * 
	 * @param line
	 * @return
	 */
	public static List<UniProtID> parseACLine(String line) {
		List<UniProtID> ids = new ArrayList<UniProtID>();
		line = line.substring(2).trim();
		String[] toks = line.split(";");
		for (String tok : toks) {
			ids.add(new UniProtID(tok.trim()));
		}
		return ids;
	}

	/**
	 * Returns the NCBI taxonomy ID parsed from the OX line
	 * 
	 * @param line
	 * @return
	 */
	public static Organism parseOXLine(String line) {
		Pattern p = Pattern.compile("NCBI_TaxID=(\\d+)[^\\d]");
		Matcher m = p.matcher(line);
		if (m.find()) {
			DbReferenceType dbReferenceType = new DbReferenceType();
			dbReferenceType.setType("NCBI Taxonomy");
			dbReferenceType.setId(m.group(1));
			OrganismType ot = new OrganismType();
			ot.getDbReference().add(dbReferenceType);
			Organism o = new Organism(ot);
			return o;
		}

		logger.error("Expected but could not find NCBI Taxonomy ID on OX Line: " + line);
		return null;
	}

	public static UniProtEntryName parseIDLine(String line) {
		Pattern p = Pattern.compile("ID\\s+([^\\s]+)");
		Matcher m = p.matcher(line);
		if (m.find())
			return new UniProtEntryName(m.group(1));

		logger.error("Expected but could not find UniProt symbol on ID Line: " + line);
		return null;
	}

}
