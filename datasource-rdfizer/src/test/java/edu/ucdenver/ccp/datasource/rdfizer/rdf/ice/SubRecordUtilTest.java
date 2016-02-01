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

import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.RecordUtil;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebInteractionSourceDatabase;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.filter.NoOpDuplicateStatementFilter;

/**
 * A test for the {@link RecordUtil} class. Tests in this class are specific to sub-record handling.
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SubRecordUtilTest {

	@Record(dataSource = DataSource.KEGG)
	public static class TestDataRecord implements DataRecord {
		@RecordField
		private String stringField = "1";
		@RecordField
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
		private final IRefWebInteractionSourceDatabase miIdTerm;

		/**
		 * @param subStringField
		 */
		public NestedSubRecord(String id, String term) {
			super();
			this.miIdTerm = new IRefWebInteractionSourceDatabase(new MolecularInteractionOntologyTermID(id), term);
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



	/**
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public final void testGetRecordInstanceStatementsWithSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestDataRecordWithSubRecord();

		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordWithSubRecordDataSet20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
						"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord)",
						"(http://record.uri, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordSchema1)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_bxwod_CldpkRuVLuKbP0T5IQ6JQ)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_bxwod_CldpkRuVLuKbP0T5IQ6JQ, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_bxwod_CldpkRuVLuKbP0T5IQ6JQ, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_bxwod_CldpkRuVLuKbP0T5IQ6JQ, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_Rft4iXphN4pTKZIY-174Yxb3mcA)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_Rft4iXphN4pTKZIY-174Yxb3mcA, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_Rft4iXphN4pTKZIY-174Yxb3mcA, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_collectionField_Rft4iXphN4pTKZIY-174Yxb3mcA, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_primitiveIntField_hmNPO2pDKLqbj8jYanuGe3fDEro)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_primitiveIntField_hmNPO2pDKLqbj8jYanuGe3fDEro, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_primitiveIntField_hmNPO2pDKLqbj8jYanuGe3fDEro, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_primitiveIntField_hmNPO2pDKLqbj8jYanuGe3fDEro, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_stringField_cK1-ZKY-VbQR72YjJQpeLtNdm34)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_stringField_cK1-ZKY-VbQR72YjJQpeLtNdm34, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecord_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_stringField_cK1-ZKY-VbQR72YjJQpeLtNdm34, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecord_stringField_cK1-ZKY-VbQR72YjJQpeLtNdm34, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/SubRecord)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws, http://purl.obolibrary.org/obo/IAO_0000219, \"XYZZZZZZZZ\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());
		Iterator<? extends Statement> it = statements.iterator();
		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
		}

		assertTrue(expectedStatements.isEmpty());

	}

	/**
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public final void testGetRecordInstanceStatementsWithCollectionSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();

		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordWithSubRecordCollectionDataSet20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
						"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection)",
						"(http://record.uri, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollectionSchema1)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_ActAF6W_MAU_W5CKGjJQDpVjRYs)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_ActAF6W_MAU_W5CKGjJQDpVjRYs, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_ActAF6W_MAU_W5CKGjJQDpVjRYs, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_ActAF6W_MAU_W5CKGjJQDpVjRYs, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_h0X8Bargx3OrweU69XvjQp6FneE)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_h0X8Bargx3OrweU69XvjQp6FneE, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_h0X8Bargx3OrweU69XvjQp6FneE, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_collectionField_h0X8Bargx3OrweU69XvjQp6FneE, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_primitiveIntField_ASovVMTKCova71bDzai_hBIv1Ek)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_primitiveIntField_ASovVMTKCova71bDzai_hBIv1Ek, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_primitiveIntField_ASovVMTKCova71bDzai_hBIv1Ek, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_primitiveIntField_ASovVMTKCova71bDzai_hBIv1Ek, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_stringField_fuBM1_QOxFmmz2fqYeEdeqb9eoo)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_stringField_fuBM1_QOxFmmz2fqYeEdeqb9eoo, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithSubRecordCollection_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_stringField_fuBM1_QOxFmmz2fqYeEdeqb9eoo, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithSubRecordCollection_stringField_fuBM1_QOxFmmz2fqYeEdeqb9eoo, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/SubRecord)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_h8DLlrIUz8gORKdbauuMlahhVws, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_h8DLlrIUz8gORKdbauuMlahhVws, http://purl.obolibrary.org/obo/IAO_0000219, \"XYZZZZZZZZ\"@en)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_YrTvhKIp4LTfUjgPWrmt54QkIPk)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_YrTvhKIp4LTfUjgPWrmt54QkIPk, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/SubRecord)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_YrTvhKIp4LTfUjgPWrmt54QkIPk, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/SubRecordSchema1)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_SubRecord_YrTvhKIp4LTfUjgPWrmt54QkIPk, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_YrTvhKIp4LTfUjgPWrmt54QkIPk)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_YrTvhKIp4LTfUjgPWrmt54QkIPk, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/SubRecord_subStringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_YrTvhKIp4LTfUjgPWrmt54QkIPk, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_SubRecord_subStringField_YrTvhKIp4LTfUjgPWrmt54QkIPk, http://purl.obolibrary.org/obo/IAO_0000219, \"ABABABABA\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(
				new TestDataRecordWithSubRecordCollection(), createdTimeInMillis, new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());
		Iterator<? extends Statement> it = statements.iterator();
		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
		}

		assertTrue(expectedStatements.isEmpty());

	}



	/**
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public final void testGetRecordInstanceStatementsWithNestedSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestDataRecordWithNestedSubRecord();

		Set<String> expectedStatements = CollectionsUtil
				.createSet(
						"(http://kabob.ucdenver.edu/iao/kegg/keggTestDataRecordWithNestedSubRecordDataSet20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
						"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithNestedSubRecord)",
						"(http://record.uri, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithNestedSubRecordSchema1)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_SG1QsGZwhJNaTdMnzgma3v5AB24)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_SG1QsGZwhJNaTdMnzgma3v5AB24, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_SG1QsGZwhJNaTdMnzgma3v5AB24, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithNestedSubRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_SG1QsGZwhJNaTdMnzgma3v5AB24, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"@en)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithNestedSubRecord_collectionFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/kegg/NestedSubRecord)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/NestedSubRecordSchema1)",
						"(http://kabob.ucdenver.edu/iao/kegg/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_IPNOgWa085q7R1Ww21fz-xD4MV0)",
						"(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_IPNOgWa085q7R1Ww21fz-xD4MV0, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabase)",
						"(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_IPNOgWa085q7R1Ww21fz-xD4MV0, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabaseSchema1)",
						"(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_IPNOgWa085q7R1Ww21fz-xD4MV0, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_hxLOHMRgT97VZ1vytl4H1dILsuc)",
						"(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_hxLOHMRgT97VZ1vytl4H1dILsuc, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabase_sourceDatabaseIdDataField1)",
						"(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_hxLOHMRgT97VZ1vytl4H1dILsuc, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_hxLOHMRgT97VZ1vytl4H1dILsuc, http://purl.obolibrary.org/obo/IAO_0000219, http://kabob.ucdenver.edu/iao/mi_ontology/MI_ONTOLOGY_MI_0123_ICE)",
						"(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_IPNOgWa085q7R1Ww21fz-xD4MV0, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU)",
						"(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabase_sourceDatabaseNameDataField1)",
						"(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU, http://purl.obolibrary.org/obo/IAO_0000219, \"miTerm123\"@en)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithNestedSubRecord_primitiveIntFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg, http://purl.obolibrary.org/obo/IAO_0000219, \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
						"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8, http://kabob.ucdenver.edu/iao/hasTemplate, http://kabob.ucdenver.edu/iao/kegg/TestDataRecordWithNestedSubRecord_stringFieldDataField1)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://kabob.ucdenver.edu/iao/FieldValue)",
						"(http://kabob.ucdenver.edu/iao/kegg/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8, http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());
		Iterator<? extends Statement> it = statements.iterator();
		while (it.hasNext()) {
			String triple = it.next().toString();
			// System.out.println(triple);
			assertTrue("Not in expected set: " + triple, expectedStatements.remove(triple));
		}

		assertTrue(expectedStatements.isEmpty());

	}

}
