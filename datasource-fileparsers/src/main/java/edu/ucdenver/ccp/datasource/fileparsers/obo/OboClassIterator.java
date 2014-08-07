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
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;
import org.geneontology.oboedit.datamodel.OBOClass;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil.ObsoleteTermHandling;

/**
 * An abstract class to be used for iterating over the classes in an OBO file. 
 * 
 * @author bill
 * 
 */
public abstract class OboClassIterator extends RecordReader<OBOClassRecord> {

	private final Iterator<OBOClass> oboClassIterator;
	private OBOClassRecord nextRecord = null;

	public OboClassIterator(File oboOntologyFile, CharacterEncoding encoding, ObsoleteTermHandling obsoleteHandling) throws IOException, OBOParseException {
		FileUtil.validateFile(oboOntologyFile);
		OboUtil<?> oboUtil = new OboUtil(oboOntologyFile, encoding);
		oboClassIterator = oboUtil.getClassIterator(obsoleteHandling);
	}

	public OboClassIterator(File workDirectory, boolean clean, ObsoleteTermHandling obsoleteHandling) throws IOException, OBOParseException {
		super();
		try {
			DownloadUtil.download(this, workDirectory, null, null, clean);
			OboUtil<?> oboUtil = initializeOboUtilFromDownload();
			oboClassIterator = oboUtil.getClassIterator(obsoleteHandling);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}

	}

	@Override
	public boolean hasNext() {
		if (nextRecord == null) {
			while (oboClassIterator.hasNext()) {
				OBOClass oboClass = oboClassIterator.next();
				if (idStartsWithAllowedPrefix(oboClass.getID())) {
					nextRecord = new OBOClassRecord(oboClass);
					return true;
				}
			}
//			if (oboClassIterator.hasNext()) {
//				OBOClass oboClass = oboClassIterator.next();
//				if (!idStartsWithAllowedPrefix(oboClass.getID())) {
//					return hasNext();
//				}
//				nextRecord = new OBOClassRecord(oboClass);
//				return true;
//			}
			return false;
		}
		return true;
	}

	@Override
	public OBOClassRecord next() {
		if (!hasNext())
			throw new NoSuchElementException();

		OBOClassRecord recordToReturn = nextRecord;
		nextRecord = null;
		return recordToReturn;
	}

	
	private boolean idStartsWithAllowedPrefix(String id) {
		if (getOntologyIdPrefixes()== null) {
			return true;
		}
		for (String prefix : getOntologyIdPrefixes()) {
			if (id.startsWith(prefix))
				return true;
		}
		return false;
	}
	
	@Override
	public void close() throws IOException {
		// do nothing
	}

	protected abstract Set<String> getOntologyIdPrefixes();

	protected abstract OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException;

}
