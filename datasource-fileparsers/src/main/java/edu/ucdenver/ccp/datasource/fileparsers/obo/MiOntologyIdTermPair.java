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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MolecularInteractionOntologyTermID;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Some databases used MI:0000 as a placeholder and attach a specific term name
 * to it, e.g. in IRefWeb there is both MI:0000(BIND_Translation) and
 * MI:0000(unspecified). This record allows for the ID and term name to be
 * paired.
 * 
 * @author Center for Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
// @Record(dataSource = DataSource.MI_ONTOLOGY)
@EqualsAndHashCode
@ToString
public abstract class MiOntologyIdTermPair {

	private static final Logger logger = Logger.getLogger(MiOntologyIdTermPair.class);

	private final MolecularInteractionOntologyTermID id;
	private final String termName;

	/**
	 * @param id
	 * @param termName
	 */
	public MiOntologyIdTermPair(MolecularInteractionOntologyTermID id, String termName) {
		super();
		this.id = id;
		this.termName = termName;
	}

	public MiOntologyIdTermPair(MolecularInteractionOntologyTermID id) {
		super();
		this.id = id;
		this.termName = null;
	}

	/**
	 * @return the id
	 */
	protected MolecularInteractionOntologyTermID getId() {
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
	 * @return parses Strings of the form "MI:0326(protein)" and returns a
	 *         {@link MiOntologyIdTermPair}
	 */
	public static <T extends MiOntologyIdTermPair> T parseString(Class<T> miIdTermPairClass, String input) {
		Pattern methodIDPattern = Pattern.compile("(MI:\\d+),?\\((.*?)\\)");
		Matcher m = methodIDPattern.matcher(input);
		if (m.find()) {
			return (T) ConstructorUtil.invokeConstructor(miIdTermPairClass.getName(),
					new MolecularInteractionOntologyTermID(m.group(1)), m.group(2));
			// return new MiOntologyIdTermPair(new
			// MolecularInteractionOntologyTermID(m.group(1)),
			// m.group(2));
		}
		methodIDPattern = Pattern.compile("psi-mi:\"(MI:\\d+)\",?\\((.*?)\\)");
		m = methodIDPattern.matcher(input);
		if (m.find()) {
			String miId = m.group(1);
			String name = (m.group(2).equals("-")) ? null : m.group(2);
			if (name != null) {
				return (T) ConstructorUtil.invokeConstructor(miIdTermPairClass.getName(),
						new MolecularInteractionOntologyTermID(miId), name);
			}
			return (T) ConstructorUtil.invokeConstructor(miIdTermPairClass.getName(),
					new MolecularInteractionOntologyTermID(miId));
		}

		logger.warn("Unable to extract MiOntologyIdTermPair from: " + input);
		return null;
	}

}
