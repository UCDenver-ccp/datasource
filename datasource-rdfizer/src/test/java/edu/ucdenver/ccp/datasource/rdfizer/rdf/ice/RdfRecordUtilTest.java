/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.openrdf.model.Statement;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.fileparsers.dip.DipInteractionDetectionMethod;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RdfRecordUtilTest {

	@Record(dataSource = DataSource.KEGG, comment = "test comment for datasource.kegg")
	public static class TestDataRecord implements DataRecord {
		@RecordField(comment = "test comment for TestDataRecord.stringField")
		private String stringField = "1";
		@RecordField(isKeyField = true)
		private int primitiveIntField = 2;
		@RecordField
		private Collection<String> collectionField = Arrays.asList("1", "2");
	}

	/**
	 * This class represents a Sub-record that will be a field in other record classes
	 * 
	 */
	@Record(dataSource = DataSource.KEGG, comment = "This is a sub-record class")
	private static class SubRecord implements DataRecord {
		@RecordField
		private final String subStringField;

		/**
		 * @param subStringField
		 */
		public SubRecord(String subStringField) {
			super();
			this.subStringField = subStringField;
		}

	}

	/**
	 * This class represents a Sub-record that will be a field in other record classes
	 * 
	 */
	@Record(dataSource = DataSource.KEGG, comment = "This is a sub-record class that has a subrecord itself")
	private static class NestedSubRecord implements DataRecord {
		@RecordField
		private final DipInteractionDetectionMethod miIdTerm;

		/**
		 * @param subStringField
		 */
		public NestedSubRecord(String id, String term) {
			super();
			this.miIdTerm = new DipInteractionDetectionMethod(new MolecularInteractionOntologyTermID(id), term);
		}

	}

	/**
	 * This is a DataRecord class that has a field that is a sub-record
	 */
	@Record(dataSource = DataSource.KEGG)
	private static class TestDataRecordWithSubRecord extends TestDataRecord {
		@RecordField
		private SubRecord subRecordField = new SubRecord("XYZZZZZZZZ");
	}

	/**
	 * This is a DataRecord class that has a field that is a collection of sub-records
	 */
	@Record(dataSource = DataSource.KEGG)
	private static class TestDataRecordWithSubRecordCollection extends TestDataRecord {
		@RecordField
		private Collection<SubRecord> subRecordCollectionField = Arrays.asList(new SubRecord("XYZZZZZZZZ"),
				new SubRecord("ABABABABA"));
	}

	/**
	 * This is a DataRecord class that has a field that is a sub-record
	 */
	@Record(dataSource = DataSource.KEGG)
	private static class TestDataRecordWithNestedSubRecord extends TestDataRecord {
		@RecordField
		private NestedSubRecord nestedSubRecordField = new NestedSubRecord("MI:0123", "miTerm123");
	}

	/**
	 * This is a DataRecord class that has a field that is a collection of sub-records
	 */
	@Record(dataSource = DataSource.KEGG)
	private static class TestDataRecordWithNestedSubRecordCollection extends TestDataRecord {
		@RecordField
		private Collection<NestedSubRecord> nestedSubRecordCollectionField = Arrays.asList(new NestedSubRecord(
				"MI:0123", "miTerm123"), new NestedSubRecord("MI:0789", "miTerm789"));
	}

	@Test
	public void testGetRecordSchemaStatements_WithSubRecordField() {
		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://purl.obolibrary.org/obo/IAO_0000030)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://www.w3.org/2000/01/rdf-schema#label, \"test data record with sub record\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"collection field\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"primitive int field\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://kabob.ucdenver.edu/iao/hasKeyPart, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField1, http://www.w3.org/2000/01/rdf-schema#comment, \"test comment for TestDataRecord.stringField\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"string field\"@en)",
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://purl.obolibrary.org/obo/IAO_0000030)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://www.w3.org/2000/01/rdf-schema#comment, \"This is a sub-record class\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://www.w3.org/2000/01/rdf-schema#label, \"sub record\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"sub string field\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordSchemaStatements(
				TestDataRecordWithSubRecord.class, null, null, false);
		Set<String> alreadyRemovedStmts = new HashSet<String>();
		Iterator<? extends Statement> it = statements.iterator();
		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			if (!alreadyRemovedStmts.contains(triple)) {
				assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
				alreadyRemovedStmts.add(triple);
			}
		}

		assertTrue(expectedStatements.isEmpty());
	}

	@Test
	public void testGetRecordSchemaStatements_WithSubRecordCollectionField() {
		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://purl.obolibrary.org/obo/IAO_0000030)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://www.w3.org/2000/01/rdf-schema#label, \"test data record with sub record collection\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"collection field\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"primitive int field\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://kabob.ucdenver.edu/iao/hasKeyPart, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField1, http://www.w3.org/2000/01/rdf-schema#comment, \"test comment for TestDataRecord.stringField\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"string field\"@en)",
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/Schema, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://purl.obolibrary.org/obo/IAO_0000030)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://www.w3.org/2000/01/rdf-schema#comment, \"This is a sub-record class\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://www.w3.org/2000/01/rdf-schema#label, \"sub record\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Schema)",
						"(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Field)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1, http://purl.org/dc/terms/hasVersion, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1, http://www.w3.org/2000/01/rdf-schema#label, \"sub string field\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordSchemaStatements(
				TestDataRecordWithSubRecordCollection.class, null, null, false);

		Set<String> alreadyRemovedStmts = new HashSet<String>();
		Iterator<? extends Statement> it = statements.iterator();
		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			if (!alreadyRemovedStmts.contains(triple)) {
				assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
				alreadyRemovedStmts.add(triple);
			}
		}

		assertTrue(expectedStatements.isEmpty());

	}
}
