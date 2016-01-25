package edu.ucdenver.ccp.datasource.fileparsers.obo;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;

/**
 * An abstract class to be used for iterating over the classes in an OBO file.
 * 
 * @author bill
 * 
 */
public abstract class OntologyClassIterator extends RecordReader<OntologyClassRecord> {

	private final Iterator<OWLClass> owlClassIterator;
	private OntologyClassRecord nextRecord = null;
	private OntologyUtil ontUtil;

	public OntologyClassIterator(File ontologyFile) throws OWLOntologyCreationException, FileNotFoundException {
		FileUtil.validateFile(ontologyFile);
		OntologyUtil ontUtil = new OntologyUtil(ontologyFile);
		owlClassIterator = ontUtil.getClassIterator();
	}

	public OntologyClassIterator(File workDirectory, boolean clean) throws IOException, IllegalArgumentException,
			IllegalAccessException, OWLOntologyCreationException {
		super();
		DownloadUtil.download(this, workDirectory, null, null, clean);
		ontUtil = initializeOboUtilFromDownload();
		owlClassIterator = ontUtil.getClassIterator();
	}

	@Override
	public boolean hasNext() {
		if (nextRecord == null) {
			while (owlClassIterator.hasNext()) {
				OWLClass oboClass = owlClassIterator.next();
				nextRecord = new OntologyClassRecord(oboClass);
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public OntologyClassRecord next() {
		if (!hasNext())
			throw new NoSuchElementException();

		OntologyClassRecord recordToReturn = nextRecord;
		nextRecord = null;
		return recordToReturn;
	}

	@Override
	public void close() throws IOException {
		ontUtil.close();
	}

	protected abstract OntologyUtil initializeOboUtilFromDownload() throws IOException, OWLOntologyCreationException;

}
