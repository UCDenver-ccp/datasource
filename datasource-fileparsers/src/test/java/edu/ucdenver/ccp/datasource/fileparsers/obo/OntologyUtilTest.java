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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
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
	private static final String SAMPLE_NCBITAXON_OBO_FILE_NAME = "sample.ncbitaxon.obo";
	private static final String SAMPLE_NCBITAXON_OWL_FILE_NAME = "sample.ncbitaxon.owl";
	private OntologyUtil ontUtil;
	private File sampleNcbiTaxonOboFile;
	private File sampleNcbiTaxonOwlFile;

	@Before
	public void setUp() throws IOException, OWLOntologyCreationException {
		File sampleOboFile = folder.newFile("sample.obo");
		ClassPathUtil.copyClasspathResourceToFile(getClass(), SAMPLE_OBO_FILE_NAME, sampleOboFile);
		ontUtil = new OntologyUtil(sampleOboFile);

		sampleNcbiTaxonOboFile = folder.newFile("sample.ncbitaxon.obo");
		ClassPathUtil.copyClasspathResourceToFile(getClass(), SAMPLE_NCBITAXON_OBO_FILE_NAME, sampleNcbiTaxonOboFile);

		sampleNcbiTaxonOwlFile = folder.newFile("sample.ncbitaxon.owl");
		ClassPathUtil.copyClasspathResourceToFile(getClass(), SAMPLE_NCBITAXON_OWL_FILE_NAME, sampleNcbiTaxonOwlFile);

	}

	@Test
	public void testGetComments() {
		OWLClass cls = ontUtil.getOWLClassFromId("PR:000000008");
		List<String> comments = ontUtil.getComments(cls);
		assertEquals("one comment expected", 1, comments.size());
		assertEquals("Category=family.", comments.iterator().next());

		cls = ontUtil.getOWLClassFromId("PR:000000101");
		comments = ontUtil.getComments(cls);
		assertEquals("one comment expected", 1, comments.size());
		assertEquals("Category=gene.", comments.iterator().next());

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

		assertEquals(
				CollectionsUtil.createSet("HLADPA1", "DP(W3)", "DP(W4)", "HLA-SB alpha chain",
						"MHC class II DP3-alpha"),
				ontUtil.getSynonyms(ontUtil.getOWLClassFromId("PR:000002012"), SynonymType.EXACT));

		assertEquals(
				CollectionsUtil.createSet("HLADPA1", "DP(W3)", "DP(W4)", "HLA-SB alpha chain", "MHC class II DP3-alpha",
						"HLA-DP1A", "HLA-DPA1", "HLASB"),
				ontUtil.getSynonyms(ontUtil.getOWLClassFromId("PR:000002012"), SynonymType.ALL));
	}

	@Test
	public void testNcbiTaxonOboFile() throws OWLOntologyCreationException, IOException {
		testSampleOntologyFile(sampleNcbiTaxonOboFile, 8, "ncbi_taxonomy");
	}

	@Test
	public void testNcbiTaxonOwlFile() throws OWLOntologyCreationException, IOException {
		testSampleOntologyFile(sampleNcbiTaxonOwlFile, 8, "ncbi_taxonomy");
	}

	/**
	 * This test was written in response to
	 * https://github.com/UCDenver-ccp/datasource/issues/5
	 * 
	 * The user reported an "unhandled synonym type" error when processing the NCBI
	 * Taxonomy ontology. This error stems from an inconsistency in the OWL API when
	 * processing OBO files vs. OWL files. Specifically, the oboInOwl namespace when
	 * parsing an OBO file is set to: http://www.geneontology.org/formats/oboInOWL#
	 * whereas in OWL files the following is used:
	 * http://www.geneontology.org/formats/oboInOwl#. Note the difference in
	 * capitalization, oboInOWL vs. oboInOwl. The error appears when retrieving
	 * synonyms for a concept and the oboInOwl namespace is used for the various
	 * synonym types (related, exact, broad, narrow, etc.) It also appears when
	 * retrieving the namespace of a concept.
	 *
	 * This test processes a sample ontology file and exercises the synonym
	 * retrieval code. If an "unhandled synonym type" error is logged, the test
	 * fails.
	 * 
	 * This test also checks to make sure the returned namespace is as expected.
	 * 
	 * @param ontFile
	 * @param expectedClassCount
	 * @throws OWLOntologyCreationException
	 * @throws IOException
	 */
	private static void testSampleOntologyFile(File ontFile, int expectedClassCount, String expectedNamespace)
			throws OWLOntologyCreationException, IOException {
		final TestAppender appender = new TestAppender();
		final Logger logger = Logger.getLogger(OntologyUtil.class);
		logger.addAppender(appender);

		OntologyUtil ontUtil = new OntologyUtil(ontFile);
		int count = 0;
		for (Iterator<OWLClass> classIterator = ontUtil.getClassIterator(); classIterator.hasNext();) {
			count++;
			OWLClass owlCls = classIterator.next();
			ontUtil.getSynonyms(owlCls, SynonymType.RELATED);
			assertEquals(expectedNamespace, ontUtil.getNamespace(owlCls));
		}
		ontUtil.close();
		assertEquals(expectedClassCount, count);

		/* ensure there were no errors logged */
		final List<LoggingEvent> logList = appender.getLog();
		for (LoggingEvent log : logList) {
			assertFalse("An error was logged: " + log.getMessage().toString(), log.getLevel().equals(Level.ERROR));
		}
	}

	/**
	 * from: http://stackoverflow.com/questions/1827677/how-to-do-a-junit-assert-
	 * on-a-message-in-a-logger
	 */
	private static class TestAppender extends AppenderSkeleton {
		private final List<LoggingEvent> log = new ArrayList<LoggingEvent>();

		@Override
		public boolean requiresLayout() {
			return false;
		}

		@Override
		protected void append(final LoggingEvent loggingEvent) {
			log.add(loggingEvent);
		}

		@Override
		public void close() {
		}

		public List<LoggingEvent> getLog() {
			return new ArrayList<LoggingEvent>(log);
		}
	}

}
