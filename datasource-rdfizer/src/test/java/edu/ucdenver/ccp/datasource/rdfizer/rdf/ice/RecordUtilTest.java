/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.fileparsers.field.DataElementLiteral;
import edu.ucdenver.ccp.rdfizer.rdf.filter.NoOpDuplicateStatementFilter;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RecordUtilTest {

	@Test
	public void testGetSortedFieldsAndValues() {
		GeneID geneID = new GeneID(789);
		GeneName geneName = new GeneName("gene name");
		Set<GeneID> homologousGeneIDs = CollectionsUtil.createSet(new GeneID(2), new GeneID(10), new GeneID(100),
				new GeneID(1));
		long byteOffset1 = 10000000;
		long lineNumber1 = 999999;
		long byteOffset2 = 10000001;
		long lineNumber2 = 999991;
		DataRecord record1 = new GeneId2NameDatFileData(geneID, geneName, homologousGeneIDs, byteOffset1, lineNumber1);
		DataRecord record2 = new GeneId2NameDatFileData(geneID, geneName, homologousGeneIDs, byteOffset2, lineNumber2);

		long time = System.currentTimeMillis();
		String sha1 = RdfRecordUriFactory.sha1DigestForSortedFieldsAndValues(record1);
		String sha2 = RdfRecordUriFactory.sha1DigestForSortedFieldsAndValues(record2);
		assertEquals("only system fields have changed so these records should have the same sha1 digest", sha1, sha2);

		DataRecord record3 = new GeneId2NameDatFileData(new GeneID(456), geneName, homologousGeneIDs, byteOffset1,
				lineNumber1);
		String sha3 = RdfRecordUriFactory.sha1DigestForSortedFieldsAndValues(record3);
		assertFalse("gene ids changed so sha1 better also change", sha1.equals(sha3));
	}

	@Record(dataSource = DataSource.EG)
	private static class GeneId2NameDatFileData extends SingleLineFileRecord {

		@RecordField
		private final GeneID geneID;
		@RecordField
		private final GeneName geneName;
		@RecordField
		private final Set<GeneID> homologousGeneIDs;

		public GeneId2NameDatFileData(GeneID geneID, GeneName geneName, Set<GeneID> homologousGeneIDs, long byteOffset,
				long lineNumber) {
			super(byteOffset, lineNumber);
			this.geneID = geneID;
			this.geneName = geneName;
			this.homologousGeneIDs = homologousGeneIDs;
		}

		public GeneID getGeneID() {
			return geneID;
		}

		public GeneName getGeneName() {
			return geneName;
		}

		public Set<GeneID> getHomologousGeneIDs() {
			return homologousGeneIDs;
		}

		public static GeneId2NameDatFileData parseFromLine(Line line) {
			String[] toks = line.getText().split("\\t", -1);
			GeneID geneID = new GeneID(new Integer(toks[0]));
			GeneName geneName = new GeneName(toks[1]);
			Set<GeneID> homologousGeneIDs = new HashSet<GeneID>();
			if (toks[2].length() > 0)
				for (Integer id : CollectionsUtil.parseInts(Arrays.asList(toks[2].split("\\|")))) {
					homologousGeneIDs.add(new GeneID(id));
				}
			return new GeneId2NameDatFileData(geneID, geneName, homologousGeneIDs, line.getByteOffset(),
					line.getLineNumber());
		}

	}

	private static class GeneID extends DataSourceIdentifier<Integer> {

		public GeneID(Integer resourceID) {
			super(resourceID);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.EG;
		}

		@Override
		public Integer validate(Integer resourceID) throws IllegalArgumentException {
			return resourceID;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof GeneID) {
				GeneID geneID = (GeneID) obj;
				return geneID.getDataElement().equals(getDataElement());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return getDataElement().hashCode();
		}

	}

	private static class GeneName extends DataElementLiteral {

		public GeneName(String resourceName) {
			super(resourceName);
		}
	}

	// @Test
	// public final void testGetRecordSchemaDefinitionStatements() {
	// Set<String> expectedStatements = CollectionsUtil
	// .createSet(
	// "(http://kabob.ucdenver.edu/iao/kegg/keggSchema, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Schema)",
	// "(http://kabob.ucdenver.edu/iao/kegg/keggDataSource, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/DataSource)",
	// "(http://kabob.ucdenver.edu/iao/kegg/keggRecord, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Record)",
	// "(http://kabob.ucdenver.edu/iao/kegg/keggDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/DataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_collectionFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_collectionFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_collectionFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_primitiveIntFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_primitiveIntFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_primitiveIntFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_stringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_stringFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_stringFieldDataField1)");
	//
	// List<? extends Statement> statements =
	// RdfRecordUtil.getRecordSchemaDefinitionStatements(TestDataRecord.class);
	// Iterator<? extends Statement> it = statements.iterator();
	// while (it.hasNext()) {
	// String triple = it.next().toString();
	// // System.out.println(triple);
	// assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
	// }
	//
	// assertTrue(expectedStatements.isEmpty());
	// }

	// @Test
	// public final void testGetRecordSchemaStatements() {
	// Set<String> expectedStatements = CollectionsUtil
	// .createSet(
	// "(http://kabob.ucdenver.edu/iao/kegg/keggSchema, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Schema)",
	// "(http://kabob.ucdenver.edu/iao/kegg/keggDataSource, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/DataSource)",
	// "(http://kabob.ucdenver.edu/iao/kegg/keggRecord, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/Record)",
	// "(http://kabob.ucdenver.edu/iao/kegg/keggDataField, http://www.w3.org/2000/01/rdf-schema#subClassOf, http://kabob.ucdenver.edu/iao/DataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_collectionFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_collectionFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_collectionFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_primitiveIntFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_primitiveIntFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_primitiveIntFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_stringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_stringFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_stringFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecordSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecordSchema)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecordSchema1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecord_subrecordStringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecord_subrecordStringFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecord_subrecordStringFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_subRecordDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_subRecordDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_subRecordDataField1)");
	//
	// Collection<? extends Statement> statements = RdfRecordUtil
	// .getRecordSchemaStatements(TestDataRecordWithSubrecord.class, null, null, false);
	// Iterator<? extends Statement> it = statements.iterator();
	// while (it.hasNext()) {
	// String triple = it.next().toString();
	// System.out.println(triple);
	// //assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
	// }
	//
	// assertTrue(expectedStatements.isEmpty());
	// }

	// @Test
	// public final void testGetRecordFieldDeclarationStatements() {
	//
	// Set<String> expectedStatements = CollectionsUtil
	// .createSet(
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_collectionFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_collectionFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_collectionFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_primitiveIntFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_primitiveIntFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_primitiveIntFieldDataField1)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_stringFieldDataField1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_stringFieldDataField)",
	// "(http://kabob.ucdenver.edu/iao/kegg/TestDataRecordSchema1, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/TestDataRecord_stringFieldDataField1)");
	//
	// Collection<? extends Statement> statements =
	// RdfRecordUtil.getRecordFieldDeclarationStatements(
	// TestDataRecord.class, null);
	// Iterator<? extends Statement> it = statements.iterator();
	// while (it.hasNext()) {
	// String triple = it.next().toString();
	// // System.out.println(triple);
	// assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
	// }
	//
	// assertTrue(expectedStatements.isEmpty());
	// }

	@Test
	public final void testGetDataSourceInstanceStatements() {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		Collection<? extends Statement> statements = RdfRecordUtil.getDataSourceInstanceStatements(
				new TestDataRecord(), createdTimeInMillis);
		Iterator<? extends Statement> it = statements.iterator();
		assertTrue(it.hasNext());
		assertEquals(
				"(http://kabob.ucdenver.edu/iao/kegg/keggDataSource20101221, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/keggDataSource)",
				it.next().toString());
		assertEquals(
				"(http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordDataSet20101221, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordSchema1)",
				it.next().toString());
		assertEquals(
				"(http://kabob.ucdenver.edu/iao/kegg/keggDataSource20101221, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordDataSet20101221)",
				it.next().toString());
		assertEquals(
				"(http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordDataSet20101221, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/DataSet)",
				it.next().toString());
		assertEquals(
				"(http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordDataSet20101221, http://kabob.ucdenver.edu/iao/hasCreationDate, \"2010-12-21T00:00:00.000-07:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime>)",
				it.next().toString());
		assertFalse(it.hasNext());
	}

	@Test
	public final void testGetRecordInstanceStatements() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestExcludeFieldDataRecord();

		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/kegg/keggTestExcludeFieldDataRecordDataSet20101221, http://purl.obolibrary.org/obo/has_part, http://record.uri)",
						"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord)",
						"(http://record.uri, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecordSchema1)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());
		Iterator<? extends Statement> it = statements.iterator();

		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			assertTrue("expected triple not observed: " + triple, expectedStatements.remove(triple));
		}

		assertTrue(expectedStatements.isEmpty());
	}

	@Test
	public final void testGetRecordInstanceStatements_WithSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestDataRecordWithSubrecord();

		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordWithSubrecordDataSet20101221, http://purl.obolibrary.org/obo/has_part, http://record.uri)",
						"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord)",
						"(http://record.uri, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecordSchema1)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_DnIjgkTYdCorX3kHXYaWJhzcHJY)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_DnIjgkTYdCorX3kHXYaWJhzcHJY, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_DnIjgkTYdCorX3kHXYaWJhzcHJY, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_DnIjgkTYdCorX3kHXYaWJhzcHJY, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_L5wUWixEQDDNsXhJedMS4OBYRm0)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_L5wUWixEQDDNsXhJedMS4OBYRm0, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_L5wUWixEQDDNsXhJedMS4OBYRm0, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_collectionField_L5wUWixEQDDNsXhJedMS4OBYRm0, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_primitiveIntField_6OTcnDKpkfNEWVrrykNM-emmlhk)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_primitiveIntField_6OTcnDKpkfNEWVrrykNM-emmlhk, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_primitiveIntField_6OTcnDKpkfNEWVrrykNM-emmlhk, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_primitiveIntField_6OTcnDKpkfNEWVrrykNM-emmlhk, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_stringField_jw2tCkX01xySTT2rYV5FCpiqRw0)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_stringField_jw2tCkX01xySTT2rYV5FCpiqRw0, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubrecord_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_stringField_jw2tCkX01xySTT2rYV5FCpiqRw0, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubrecord_stringField_jw2tCkX01xySTT2rYV5FCpiqRw0, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/R_TestDataSubRecord_7VVwQkWKCGaD8uZQ2SMe2RFUNeU)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_TestDataSubRecord_7VVwQkWKCGaD8uZQ2SMe2RFUNeU, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecord)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_TestDataSubRecord_7VVwQkWKCGaD8uZQ2SMe2RFUNeU, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecordSchema1)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_TestDataSubRecord_7VVwQkWKCGaD8uZQ2SMe2RFUNeU, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestDataSubRecord_subrecordStringField_7VVwQkWKCGaD8uZQ2SMe2RFUNeU)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataSubRecord_subrecordStringField_7VVwQkWKCGaD8uZQ2SMe2RFUNeU, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataSubRecord_subrecordStringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataSubRecord_subrecordStringField_7VVwQkWKCGaD8uZQ2SMe2RFUNeU, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataSubRecord_subrecordStringField_7VVwQkWKCGaD8uZQ2SMe2RFUNeU, http://purl.obolibrary.org/obo/IAO_0000219, \"sub\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());
		Iterator<? extends Statement> it = statements.iterator();

		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			assertTrue("expected triple not observed: " + triple, expectedStatements.remove(triple));
		}

		assertTrue(expectedStatements.isEmpty());
	}

	@Test
	public final void testGetRecordInstanceStatementsWithRecordKey() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestExcludeFieldDataRecord();

		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/kegg/keggTestExcludeFieldDataRecordKeyDataSet20101221, http://purl.obolibrary.org/obo/has_part, http://record.uri)",
						"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord)",
						"(http://record.uri, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecordSchema1)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_O0waxDNfQT_lFeO4grbgwHJ_bxs, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_collectionField_oHpC_sn17AbL7y86SQEkK6oZqgA, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_primitiveIntField_K1E3g8ozXlcQiV3vtNaH7ikWY5I, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
						"(http://record.uri, http://purl.obolibrary.org/obo/has_part, http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestExcludeFieldDataRecord_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestExcludeFieldDataRecord_stringField_wM4OI6HAehQ_w0UDN6cjfEXbpXg, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), null, "Key", new NoOpDuplicateStatementFilter());
		Iterator<? extends Statement> it = statements.iterator();

		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
		}

		assertTrue(expectedStatements.isEmpty());

	}

	@Record(dataSource = DataSource.KEGG)
	public static class TestDataRecord implements DataRecord {
		@RecordField
		private String stringField = "1";
		@RecordField
		private int primitiveIntField = 2;
		@RecordField
		private Collection<String> collectionField = Arrays.asList("1", "2");
	}

	@Record(dataSource = DataSource.KEGG)
	private static class TestExcludeFieldDataRecord extends TestDataRecord {
		private int excludeField = 2;
	}

	@Record(dataSource = DataSource.KEGG)
	private static class TestDataSubRecord implements DataRecord {
		@RecordField
		private String subrecordStringField = "sub";
	}

	@Record(dataSource = DataSource.KEGG)
	private static class TestDataRecordWithSubrecord extends TestDataRecord {
		@RecordField
		private TestDataSubRecord subRecord = new TestDataSubRecord();
	}

}
