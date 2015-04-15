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

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RdfRecordUriFactoryTest {

	@Record(dataSource = DataSource.KEGG)
	public static class TestDataRecord implements DataRecord {
		@RecordField
		private final String stringField;
		@RecordField
		private final Integer intField;
		@RecordField
		private final SubRecord subRecordField;

		/**
		 * @param stringField
		 * @param intField
		 * @param subRecordField
		 */
		public TestDataRecord(String stringField, Integer intField, SubRecord subRecordField) {
			super();
			this.stringField = stringField;
			this.intField = intField;
			this.subRecordField = subRecordField;
		}

	}

	@Record(dataSource = DataSource.KEGG)
	public static class SubRecord implements DataRecord {
		@RecordField
		private final String stringField;
		@RecordField
		private final Integer intField;

		/**
		 * @param stringField
		 * @param intField
		 */
		public SubRecord(String stringField, Integer intField) {
			super();
			this.stringField = stringField;
			this.intField = intField;
		}

	}

	/**
	 * A record with field values "A" and "B" should have the same URI as a record with a sub-record
	 * that has field values "A" and "B"
	 */
	@Ignore("Should this really be the case????")
	@Test
	public void testSubRecordUriGeneration() {
		URIImpl testRecordUri = RdfRecordUriFactory.createRecordUri(new TestDataRecord("string1", 2, null));
		URIImpl testRecordWithSubRecordUri = RdfRecordUriFactory.createRecordUri(new TestDataRecord(null, null,
				new SubRecord("string1", 2)));

		assertEquals(testRecordUri, testRecordWithSubRecordUri);
	}

}
