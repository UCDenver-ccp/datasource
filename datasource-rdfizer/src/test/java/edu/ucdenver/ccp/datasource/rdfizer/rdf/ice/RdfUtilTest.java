package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

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

import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.createRelativeFileUri;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.createStatement;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.createUri;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.createUriStatement;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.getCreationTimeStampStatement;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.getDateLiteral;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.getFileHasLocalPathStatement;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.getFileIsTypeRdfFileStatement;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.getRelativeFilePath;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.writeStatements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.CalendarLiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.KIAO;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.RDF;

public class RdfUtilTest extends DefaultTestCase {

	@Test
	public final void testCreateURI() {
		DataSource ns = DataSource.KABOB;
		assertEquals("http://kabob.ucdenver.edu/hasCreationDate", createUri(ns, "hasCreationDate").toString());
		assertEquals("http://kabob.ucdenver.edu/has_Creation_Date", createUri(ns, "has:Creation:Date").toString());
		assertEquals("http://kabob.ucdenver.edu/KABOB_0hasCreationDate", createUri(ns, "0hasCreationDate").toString());
		assertEquals("http://kabob.ucdenver.edu/hasCreationDate_", createUri(ns, "hasCreationDate_").toString());
	}

	@Test
	public final void testCreateUri() {
		assertEquals("http://kabob/hasCreationDate", createUri("http://kabob/", "hasCreationDate", null).toString());
		assertEquals("http://kabob/has_Creation_Date", createUri("http://kabob/", "has:Creation:Date", null).toString());
		assertEquals("http://kabob/EG_0hasCreationDate", createUri("http://kabob/", "0hasCreationDate", DataSource.EG)
				.toString());
		assertEquals("http://kabob/hasCreationDate_", createUri("http://kabob/", "hasCreationDate_", null).toString());
	}

	@Test
	public final void testCreateRelativeFileUriString() {
		assertEquals("file:///dir/file", createRelativeFileUri("dir\\file").toString());
		assertEquals("file:///dir/file", createRelativeFileUri("dir/file").toString());
	}

	@Test
	public final void testGetRelativeFilePathStringString() {
		assertEquals("parent/child", getRelativeFilePath("parent/child", "parent1").toString()); // no
																									// match
																									// on
																									// prefix
		assertEquals("child", getRelativeFilePath("parent/child", "parent").toString());
	}

	@Test
	public final void testGetRelativeFilePathFileString() {
		boolean isWindowsOs = System.getProperty("os.name").startsWith("Windows");
		assertEquals(isWindowsOs ? "C:\\parent\\child" : "/parent/child",
				getRelativeFilePath(new File("/parent/child"), "parent1").toString()); // no match
																						// on prefix
		assertEquals("child", getRelativeFilePath(new File("/parent/child"), "parent").toString());
		assertEquals(isWindowsOs ? "file:/C:/parent/child" : "file:/parent/child",
				getRelativeFilePath(new File("/parent/child"), null).toString());
	}

	@Test
	public final void testCreateRelativeFileUriFileString() {
		boolean isWindowsOs = System.getProperty("os.name").startsWith("Windows");
		assertEquals("file:///file", createRelativeFileUri(new File("dir\\file"), "dir").toString());
		assertEquals("file:///file", createRelativeFileUri(new File("dir/file"), "dir").toString());
		assertEquals(isWindowsOs ? "file:/C:/dir/file" : "file:/dir/file",
				createRelativeFileUri(new File("/dir/file"), null).toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testCreateUriStatementError() {
		// none are valid absolute URIs
		createUriStatement("subject", "predicate", "object");
	}

	@Test
	public final void testCreateUriStatement() {
		Statement s = new StatementImpl(new URIImpl("http://subject"), new URIImpl("http://predicate"), new URIImpl(
				"http://object"));
		validateStatement("http://subject", "http://predicate", "http://object", s);
	}

	@Test
	public final void testCreateStatement() {
		Statement s = createStatement(new URIImpl("http://subject"), new URIImpl("http://predicate"), new URIImpl(
				"http://object"));
		validateStatement("http://subject", "http://predicate", "http://object", s);
	}

	@Test
	public final void testGetDateLiteralLong() {
		long timeInMillis = 1292541019138L;
		Value value = getDateLiteral(timeInMillis);
		assertThat(value, Matchers.instanceOf(CalendarLiteralImpl.class));
		assertEquals("\"2010-12-16T16:10:19.138-07:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime>", value.toString());
	}

	@Test
	public final void testGetDateLiteralGregorianCalendar() {
		long timeInMillis = 1292541019138L;
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(timeInMillis);
		Value value = getDateLiteral(c);
		assertThat(value, Matchers.instanceOf(CalendarLiteralImpl.class));
		assertEquals("\"2010-12-16T16:10:19.138-07:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime>", value.toString());
	}

	@Test
	public final void testGetFileIsTypeRdfFileStatement() {
		Statement s = getFileIsTypeRdfFileStatement("http://myfile");
		validateStatement("http://myfile", RDF.TYPE.uri().toString(), RdfUtil.RDF_FILE_URI.toString(), s);
	}

	/**
	 * Validate statement subject, predicate and object values by comparing their
	 * {@link #toString()} to provided values
	 * 
	 * @param subject
	 *            value
	 * @param predicate
	 *            value
	 * @param object
	 *            value
	 * @param s
	 *            statement
	 * @throws AssertionError
	 *             if all aren't valid
	 */
	private void validateStatement(String subject, String predicate, String object, Statement s) {
		assertEquals("subject:", subject, s.getSubject().toString());
		assertEquals("predicte:", predicate, s.getPredicate().toString());
		assertEquals("object:", object, s.getObject().toString());
	}

	@Test
	public final void testGetCreationTimeStampStatement() {
		long timeInMillis = 1292541019138L;
		Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timeInMillis);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String expectedTime = "\""+df.format(cal.getTime())+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime>";
		Statement s = getCreationTimeStampStatement(new URIImpl("http://myfile"), timeInMillis);
		validateStatement("http://myfile", KIAO.HAS_CREATION_DATE.uri().toString(), expectedTime, s);
	}

	@Test
	public final void testGetFileHasLocalPathStatement() {
		Statement s = getFileHasLocalPathStatement("http://myfile", new File("/dir/file"), "dir");
		validateStatement("http://myfile", KIAO.HAS_LOCAL_FILE_PATH.uri().toString(), "\"file\"@en", s);
	}

	@Test
	public final void testCreateIaoUri() {
		assertEquals("http://kabob.ucdenver.edu/iao/kegg/hasCreationDate",
				RdfUtil.createKiaoUri( DataSource.KEGG, KIAO.HAS_CREATION_DATE.termName()).toString());
	}

	@Test
	public final void testCreateIaoUriAsString() {
		assertEquals("http://kabob.ucdenver.edu/iao/kegg/hasCreationDate",
				RdfUtil.createKiaoUri( DataSource.KEGG, KIAO.HAS_CREATION_DATE.termName()).toString());
	}

	

	@Test
	public final void testWriteStatements() {
		RDFWriter writer = Rio.createWriter(RDFFormat.NTRIPLES, new StringWriter());
		Statement s = new StatementImpl(new URIImpl("http://subject"), new URIImpl("http://predicate"), new URIImpl(
				"http://object"));
		writeStatements(Arrays.asList(s), writer);

		// now throw exception b/c writer was ended
		try {
			writer.handleStatement(s);
			fail("Exception should've been thrown b/c writer was closed");
		} catch (RDFHandlerException e) {
			// expected
		} catch (RuntimeException e) {
			// expected
		}
	}

	

	@Test
	public void testGetDataSource() {
		assertEquals(DataSource.DIP, RdfUtil.getDataSource(DataSource.DIP));
	}
}
