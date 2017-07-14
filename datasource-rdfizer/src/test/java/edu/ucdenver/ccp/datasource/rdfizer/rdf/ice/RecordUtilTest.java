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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.DCTERMS;
import org.openrdf.model.vocabulary.RDFS;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.ColumnOrder;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.LineOrder;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.LineTrim;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.ShowWhiteSpace;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.Identifier;
import edu.ucdenver.ccp.datasource.identifiers.IntegerDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.filter.NoOpDuplicateStatementFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class RecordUtilTest {

	@Test
	public void testGetSortedFieldsAndValues() {
		GeneID geneID = new GeneID(789);
		String geneName = new String("gene name");
		Set<GeneID> homologousGeneIDs = CollectionsUtil.createSet(new GeneID(2), new GeneID(10), new GeneID(100),
				new GeneID(1));
		long byteOffset1 = 10000000;
		long lineNumber1 = 999999;
		long byteOffset2 = 10000001;
		long lineNumber2 = 999991;
		DataRecord record1 = new GeneId2NameDatFileData(geneID, geneName, homologousGeneIDs, byteOffset1, lineNumber1);
		DataRecord record2 = new GeneId2NameDatFileData(geneID, geneName, homologousGeneIDs, byteOffset2, lineNumber2);

		String sha1 = RdfRecordUriFactory.sha1DigestForSortedFieldsAndValues(record1);
		String sha2 = RdfRecordUriFactory.sha1DigestForSortedFieldsAndValues(record2);
		assertEquals("only system fields have changed so these records should have the same sha1 digest", sha1, sha2);

		DataRecord record3 = new GeneId2NameDatFileData(new GeneID(456), geneName, homologousGeneIDs, byteOffset1,
				lineNumber1);
		String sha3 = RdfRecordUriFactory.sha1DigestForSortedFieldsAndValues(record3);
		assertFalse("gene ids changed so sha1 better also change", sha1.equals(sha3));
	}

	@Record(dataSource = DataSource.NCBI_GENE, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	@Data
	@EqualsAndHashCode(callSuper = false)
	private static class GeneId2NameDatFileData extends SingleLineFileRecord {
		@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE)
		private final GeneID geneID;
		@RecordField(ontClass = CcpExtensionOntology.NAME_FIELD_VALUE)
		private final String geneName;
		@RecordField(ontClass = CcpExtensionOntology.SYNONYMS_FIELD_VALUE)
		private final Set<GeneID> homologousGeneIDs;

		public GeneId2NameDatFileData(GeneID geneID, String geneName, Set<GeneID> homologousGeneIDs, long byteOffset,
				long lineNumber) {
			super(byteOffset, lineNumber);
			this.geneID = geneID;
			this.geneName = geneName;
			this.homologousGeneIDs = homologousGeneIDs;
		}

	}

	@Identifier(ontClass=CcpExtensionOntology.NCBI_GENE_IDENTIFIER)
	private static class GeneID extends IntegerDataSourceIdentifier {
		public GeneID(Integer resourceID) {
			super(resourceID, DataSource.NCBI_GENE);
		}
	}


	@Test
	public final void testGetRecordSetInstanceStatements() {
		// TODO everything myst have a parse key so that the record sets are unique within a datasource
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		String readerKey = "human";
		Collection<? extends Statement> statements = RdfRecordUtil.getRecordSetInstanceStatements(new TestDataRecord(),
				readerKey, createdTimeInMillis);
		Iterator<? extends Statement> it = statements.iterator();
		assertTrue(it.hasNext());
		assertEquals(
				"(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_HUMAN_20101221, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "
						+ CcpExtensionOntology.RECORD_SET.uri() + ")",
				it.next().toString());
		assertEquals("(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_HUMAN_20101221, " + DCTERMS.DATE + ", "
				+ RdfUtilTest.getExpectedTimeStamp(createdTimeInMillis) + ")", it.next().toString());
		assertFalse(it.hasNext());
	}

	@Test
	public final void testGetRecordInstanceStatements() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestExcludeFieldDataRecord();

		String fieldHash1 = "Y1hjcJI45sdEWqlDHGqW-ZTXRsA";
		String fieldHash2 = "Lz1H_luk2EuSt6cgRPmqsJrQM-c";
		String fieldHash3 = "LaDkkgFRguECzB9LF81DCE_b7es";
		String fieldHash4 = "oE49wz1Ki32ivuid120WR5iHQ9Q";

		/* @formatter:off */
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HPO_ANNOTATION_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", " + RDFS.LABEL + ", \"1\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYMBOL_FIELD_VALUE.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ", " + RDFS.LABEL + ", \"3\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ", " + RDFS.LABEL + ", \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ", " + RDFS.LABEL + ", \"4\"@en)");
		/* @formatter:on */

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());
		// Iterator<? extends Statement> it = statements.iterator();

		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));
	}

	@Test
	public final void testGetRecordInstanceStatements_WithSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestDataRecordWithSubrecord();

		String fieldHash1 = "Y1hjcJI45sdEWqlDHGqW-ZTXRsA";
		String fieldHash2 = "Lz1H_luk2EuSt6cgRPmqsJrQM-c";
		String fieldHash3 = "LaDkkgFRguECzB9LF81DCE_b7es";
		String fieldHash4 = "oE49wz1Ki32ivuid120WR5iHQ9Q";

		String subRecordHash = "15-ZNT-5KBJB5mCiGo6-wqnHfBU";
		String fieldHash_sub = "15-ZNT-5KBJB5mCiGo6-wqnHfBU";

		/* @formatter:off */
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HGNC_GENE_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", " + RDFS.LABEL + ", \"1\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYMBOL_FIELD_VALUE.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ", " + RDFS.LABEL + ", \"3\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ", " + RDFS.LABEL + ", \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ", " + RDFS.LABEL + ", \"4\"@en)",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.DATABASE_CROSS_REFERENCE_IDENTIFIER_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.GOA_GAF_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_sub + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_sub + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_NAME_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash_sub + ", " + RDFS.LABEL + ", \"sub\"@en)");
		/* @formatter:on */

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());

		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));
	}

	@Test
	public final void testGetRecordInstanceStatementsWithRecordKey() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestExcludeFieldDataRecord();

		String fieldHash1 = "Y1hjcJI45sdEWqlDHGqW-ZTXRsA";
		String fieldHash2 = "Lz1H_luk2EuSt6cgRPmqsJrQM-c";
		String fieldHash3 = "LaDkkgFRguECzB9LF81DCE_b7es";
		String fieldHash4 = "oE49wz1Ki32ivuid120WR5iHQ9Q";

		/* @formatter:off */
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_KEY_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HPO_ANNOTATION_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", " + RDFS.LABEL + ", \"1\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYMBOL_FIELD_VALUE.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ", " + RDFS.LABEL + ", \"3\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash3 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash2 + ", " + RDFS.LABEL + ", \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash4 + ", " + RDFS.LABEL + ", \"4\"@en)");
		/* @formatter:on */

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), null, "Key", new NoOpDuplicateStatementFilter());

		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));
	}

	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	public static class TestDataRecord implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.SYMBOL_FIELD_VALUE)
		private String stringField = "1";
		@RecordField(ontClass = CcpExtensionOntology.NAME_FIELD_VALUE)
		private int primitiveIntField = 2;
		@RecordField(ontClass = CcpExtensionOntology.SYNONYMS_FIELD_VALUE)
		private Collection<String> collectionField = Arrays.asList("3", "4");
	}

	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.HPO_ANNOTATION_RECORD)
	private static class TestExcludeFieldDataRecord extends TestDataRecord {
		@SuppressWarnings("unused")
		private int excludeField = 5;
	}

	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.GOA_GAF_RECORD)
	private static class TestDataSubRecord implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_NAME_FIELD_VALUE)
		private String subrecordStringField = "sub";
	}

	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	private static class TestDataRecordWithSubrecord extends TestDataRecord {
		@RecordField(ontClass = CcpExtensionOntology.DATABASE_CROSS_REFERENCE_IDENTIFIER_FIELD_VALUE)
		private TestDataSubRecord subRecord = new TestDataSubRecord();
	}

	@Record(dataSource = DataSource.HGNC, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	public static class TestDataRecordWithIdentifier implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE)
		private HgncGeneSymbolID hgncId = new HgncGeneSymbolID("ABBA");
	}

	@Test
	public final void testGetRecordInstanceStatementsWithIdentifierFieldValue() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		DataRecord r = new TestDataRecordWithIdentifier();

		String fieldHash1 = "rF6Eg4r4PGGWKfvpA-vucv_2HPc";

		/* @formatter:off */
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_HGNC_KEY_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HGNC_GENE_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", " + RDFS.LABEL + ", \"ABBA\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://ccp.ucdenver.edu/obo/ext/HGNC_ABBA)",
				"(http://ccp.ucdenver.edu/obo/ext/HGNC_ABBA, "+ RDFS.SUBCLASSOF.toString() + ", "+ CcpExtensionOntology.HGNC_GENE_SYMBOL_IDENTIFIER.uri() + ")");
		/* @formatter:on */

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), null, "Key", new NoOpDuplicateStatementFilter());

		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));
	}

	
	
	@Record(dataSource = DataSource.HGNC, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	public static class TestDataRecordWithOntologyIdentifier implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE)
		private GeneOntologyID goId = new GeneOntologyID("GO:0001234");
	}

	@Test
	public final void testGetRecordInstanceStatementsWithOntologyIdentifierFieldValue() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		DataRecord r = new TestDataRecordWithOntologyIdentifier();

		String fieldHash1 = "vg3uIdG5p0H_LaRo6eqp5IFQZHw";

		/* @formatter:off */
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_HGNC_KEY_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HGNC_GENE_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", " + RDFS.LABEL + ", \"GO:0001234\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.HGNC_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://ccp.ucdenver.edu/obo/ext/GO_0001234)",
				"(http://ccp.ucdenver.edu/obo/ext/GO_0001234, "+ RDFS.SUBCLASSOF.toString() + ", "+ CcpExtensionOntology.GENE_ONTOLOGY_CONCEPT_IDENTIFIER.uri() + ")");
		/* @formatter:on */

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), null, "Key", new NoOpDuplicateStatementFilter());

		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));
	}
	
}
