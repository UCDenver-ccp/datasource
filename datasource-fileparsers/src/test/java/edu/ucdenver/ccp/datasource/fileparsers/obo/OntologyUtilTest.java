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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.io.ClassPathUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OntologyUtil.SynonymType;

/**
 * @author Center for Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 *
 */
public class OntologyUtilTest extends DefaultTestCase {

	private static final String SAMPLE_OBO_FILE_NAME = "sample.obo";
	private OntologyUtil ontUtil;

	@Before
	public void setUp() throws IOException, OWLOntologyCreationException {
		File sampleOboFile = folder.newFile("sample.obo");
		ClassPathUtil.copyClasspathResourceToFile(getClass(), SAMPLE_OBO_FILE_NAME, sampleOboFile);
		ontUtil = new OntologyUtil(sampleOboFile);
	}

	@Test
	public void testIsDescendent() {
		assertTrue(ontUtil.isDescendent(ontUtil.getOWLClassFromId("PR:000000101"),
				ontUtil.getOWLClassFromId("PR:000000008")));
		assertTrue(ontUtil.isDescendent(ontUtil.getOWLClassFromId("PR:000000101"),
				ontUtil.getOWLClassFromId("PR:000000046")));
		assertTrue(ontUtil.isDescendent(ontUtil.getOWLClassFromId("PR:000002012"),
				ontUtil.getOWLClassFromId("PR:000000008")));
		assertFalse(ontUtil.isDescendent(ontUtil.getOWLClassFromId("PR:000002012"),
				ontUtil.getOWLClassFromId("PR:000000046")));
	}

	@Test
	public void testGetDescendents() {
		assertEquals(3, ontUtil.getDescendents(ontUtil.getOWLClassFromId("PR:000000008")).size());
	}

	@Test
	public void testGetLabel() {
		assertEquals("888888", ontUtil.getLabel(ontUtil.getOWLClassFromId("PR:000000008")));
	}

	@Test
	public void testGetNamespace() {
		assertEquals("protein", ontUtil.getNamespace(ontUtil.getOWLClassFromId("PR:000000008")));
	}

	@Test
	public void testGetSynonyms() {
		assertEquals(CollectionsUtil.createSet("transforming growth factor beta"),
				ontUtil.getSynonyms(ontUtil.getOWLClassFromId("PR:000000008"), SynonymType.RELATED));

		assertEquals(CollectionsUtil.createSet("transforming growth factor beta"),
				ontUtil.getSynonyms(ontUtil.getOWLClassFromId("PR:000000008"), SynonymType.ALL));

		assertEquals(CollectionsUtil.createSet(),
				ontUtil.getSynonyms(ontUtil.getOWLClassFromId("PR:000000008"), SynonymType.EXACT));

		assertEquals(CollectionsUtil.createSet("HLA-DP1A", "HLA-DPA1", "HLASB"),
				ontUtil.getSynonyms(ontUtil.getOWLClassFromId("PR:000002012"), SynonymType.RELATED));

		assertEquals(CollectionsUtil.createSet("HLADPA1", "DP(W3)", "DP(W4)", "HLA-SB alpha chain",
				"MHC class II DP3-alpha"), ontUtil.getSynonyms(ontUtil.getOWLClassFromId("PR:000002012"),
				SynonymType.EXACT));

		assertEquals(CollectionsUtil.createSet("HLADPA1", "DP(W3)", "DP(W4)", "HLA-SB alpha chain",
				"MHC class II DP3-alpha", "HLA-DP1A", "HLA-DPA1", "HLASB"), ontUtil.getSynonyms(
				ontUtil.getOWLClassFromId("PR:000002012"), SynonymType.ALL));
	}

}
