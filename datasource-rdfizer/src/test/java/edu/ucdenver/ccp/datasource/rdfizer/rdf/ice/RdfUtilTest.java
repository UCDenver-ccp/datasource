package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2017 Regents of the University of Colorado
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

import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.getCreationTimeStampStatement;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.getDateLiteral;
import static edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.writeStatements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
import org.openrdf.model.vocabulary.DCTERMS;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

public class RdfUtilTest extends DefaultTestCase {

	@Test
	public final void testCreateUriStatement() {
		Statement s = new StatementImpl(new URIImpl("http://subject"), new URIImpl("http://predicate"),
				new URIImpl("http://object"));
		validateStatement("http://subject", "http://predicate", "http://object", s);
	}

	@Test
	public final void testGetDateLiteralLong() {
		long timeInMillis = 1292541019138L;
		Value value = getDateLiteral(timeInMillis);
		assertThat(value, Matchers.instanceOf(CalendarLiteralImpl.class));
		assertEquals(getExpectedTimeStamp(timeInMillis), value.toString());
	}

	@Test
	public final void testGetDateLiteralGregorianCalendar() {
		long timeInMillis = 1292541019138L;
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(timeInMillis);
		Value value = getDateLiteral(c);
		assertThat(value, Matchers.instanceOf(CalendarLiteralImpl.class));
		assertEquals(getExpectedTimeStamp(timeInMillis), value.toString());
	}

	/**
	 * Validate statement subject, predicate and object values by comparing
	 * their {@link #toString()} to provided values
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
		String expectedTime = getExpectedTimeStamp(timeInMillis);
		Statement s = getCreationTimeStampStatement(new URIImpl("http://myfile"), timeInMillis);
		validateStatement("http://myfile", DCTERMS.DATE.toString(), expectedTime, s);
	}

	/**
	 * @param timeInMillis
	 * @return a time stamp String formatted as if it were in an RDF statement
	 */
	static String getExpectedTimeStamp(long timeInMillis) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timeInMillis);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String expectedTime = "\"" + df.format(cal.getTime()) + "\"^^<http://www.w3.org/2001/XMLSchema#dateTime>";
		return expectedTime;
	}

	@Test
	public final void testCreateCcpUri_forDatasourceId() {
		assertEquals("http://ccp.ucdenver.edu/obo/ext/NCBI_GENE_123456", RdfUtil
				.createCcpUri(new NcbiGeneId(123456)).toString());
	}

	@Test
	public final void testCreateCcpUri_forDatasourceId_ncbitaxon() {
		assertEquals("http://ccp.ucdenver.edu/obo/ext/NCBITaxon_9606", RdfUtil
				.createCcpUri(NcbiTaxonomyID.HUMAN).toString());
	}
	
	@Test
	public final void testWriteStatements() {
		RDFWriter writer = Rio.createWriter(RDFFormat.NTRIPLES, new StringWriter());
		Statement s = new StatementImpl(new URIImpl("http://subject"), new URIImpl("http://predicate"),
				new URIImpl("http://object"));
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

}
