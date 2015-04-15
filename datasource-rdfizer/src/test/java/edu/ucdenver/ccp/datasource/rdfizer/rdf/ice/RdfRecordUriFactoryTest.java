/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

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
