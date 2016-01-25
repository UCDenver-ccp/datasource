package edu.ucdenver.ccp.datasource.fileparsers.obo.impl;

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

import java.io.File;
import java.io.IOException;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OntologyClassIterator;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OntologyUtil;

/**
 * This class iterates over the gene ontology obo file and returns OBORecords
 * for each class it encounters.
 * 
 * @author bill
 * 
 */
public class UberonOntologyClassIterator extends OntologyClassIterator {

	public static final String FILE_URL = "http://purl.obolibrary.org/obo/uberon.obo";
	public static final String ENCODING = "ASCII";

	@HttpDownload(url = FILE_URL)
	private File oboFile;

	public UberonOntologyClassIterator(File oboOntologyFile) throws IOException, OWLOntologyCreationException {
		super(oboOntologyFile);
	}

	public UberonOntologyClassIterator(File workDirectory, boolean clean) throws IOException,
			IllegalArgumentException, IllegalAccessException, OWLOntologyCreationException {
		super(workDirectory, clean);
	}

	@Override
	protected OntologyUtil initializeOboUtilFromDownload() throws IOException, OWLOntologyCreationException {
		return new OntologyUtil(oboFile);
	}

	public File getOboFile() {
		return oboFile;
	}
}
