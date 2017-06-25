package edu.ucdenver.ccp.datasource.fileparsers.obo;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.reflection.ConstructorUtil;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Some databases used negative taxonomy IDs as a placeholder and attach a specific term name to it,
 * e.g. in IRefWeb there is taxid:-4(in vivo). This record allows for the ID and term name to be
 * paired.
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
// @Data
// @Record(dataSource = DataSource.NCBI_TAXON)
@EqualsAndHashCode
@ToString
public abstract class NcbiTaxonomyIdTermPair {

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyIdTermPair.class);

	private final NcbiTaxonomyID id;
	private final String termName;

	protected NcbiTaxonomyIdTermPair(NcbiTaxonomyID id, String termName) {
		this.id = id;
		this.termName = termName;
	}
	
	protected NcbiTaxonomyIdTermPair(String termName) {
		this.id = null;
		this.termName = termName;
	}

	/**
	 * @return the id
	 */
	protected NcbiTaxonomyID getId() {
		return id;
	}

	/**
	 * @return the termName
	 */
	protected String getTermName() {
		return termName;
	}

	/**
	 * @param input
	 * @return parses Strings of the form "taxid:4932(Saccharomyces cerevisiae)" and returns a
	 *         {@link NcbiTaxonomyIdTermPair}
	 */
	public static <T extends NcbiTaxonomyIdTermPair> T parseString(Class<T> taxIdTermPairClass, String input) {
		if (input.startsWith("taxid:-")) {
			Pattern p = Pattern.compile("taxid:-\\d+\\((.*?)\\)");
			Matcher m = p.matcher(input);
			if (m.find()) {
				return (T) ConstructorUtil.invokeConstructor(taxIdTermPairClass.getName(), m.group(1));
				// return new NcbiTaxonomyIdTermPair(null, m.group(1));
			}
			logger.warn("Unable to extract taxon name from: " + input);
			return null;
		}
		Pattern methodIDPattern = Pattern.compile("(taxid:\\d+),?\\((.*?)\\)");
		Matcher m = methodIDPattern.matcher(input);
		if (m.find()) {
			return (T) ConstructorUtil.invokeConstructor(taxIdTermPairClass.getName(), new NcbiTaxonomyID(m.group(1)),
					m.group(2));
			// return new NcbiTaxonomyIdTermPair(new NcbiTaxonomyID(m.group(1)), m.group(2));
		}
		logger.warn("Unable to extract NcbiTaxonomyIdTermPair from: " + input);
		return null;
	}

}
