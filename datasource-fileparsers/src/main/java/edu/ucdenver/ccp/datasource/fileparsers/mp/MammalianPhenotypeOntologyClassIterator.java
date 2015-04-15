package edu.ucdenver.ccp.datasource.fileparsers.mp;

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

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboClassIterator;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil.ObsoleteTermHandling;

/**
 * This class iterates over the mammalian phenotype ontology obo file and returns OBORecords for
 * each class it encounters.
 * 
 * @author bill
 * 
 */
public class MammalianPhenotypeOntologyClassIterator extends OboClassIterator {

	public static final String FILE_URL = "http://obo.cvs.sourceforge.net/viewvc/obo/obo/ontology/phenotype/mammalian_phenotype.obo";

	@HttpDownload(url = FILE_URL)
	private File mpOntologyOboFile;

	public static final String MP_ID_PREFIX = "MP:";

	public MammalianPhenotypeOntologyClassIterator(File oboOntologyFile, ObsoleteTermHandling  obsoleteHandling) throws IOException, OBOParseException {
		super(oboOntologyFile, CharacterEncoding.UTF_8, obsoleteHandling);
	}

	public MammalianPhenotypeOntologyClassIterator(File workDirectory, boolean clean, ObsoleteTermHandling obsoleteHandling) throws IOException,
			OBOParseException {
		super(workDirectory, clean,obsoleteHandling);
	}

	@Override
	protected Set<String> getOntologyIdPrefixes() {
		return CollectionsUtil.createSet(MP_ID_PREFIX);
	}

	@Override
	protected OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException {
		return new OboUtil(mpOntologyOboFile, CharacterEncoding.UTF_8);
	}

}
