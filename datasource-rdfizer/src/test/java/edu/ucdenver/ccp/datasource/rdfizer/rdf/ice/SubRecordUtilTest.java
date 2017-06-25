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

import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Set;

import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
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
import edu.ucdenver.ccp.datasource.fileparsers.RecordUtil;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebInteractionSourceDatabase;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.filter.NoOpDuplicateStatementFilter;
import lombok.Data;

/**
 * A test for the {@link RecordUtil} class. Tests in this class are specific to
 * sub-record handling.
 * 
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class SubRecordUtilTest {

	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	public static class TestDataRecord implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.SYMBOL_FIELD_VALUE)
		private String stringField = "1";
		@RecordField(ontClass = CcpExtensionOntology.FILE_SIZE_BYTES)
		private int primitiveIntField = 2;
		@RecordField(ontClass = CcpExtensionOntology.SYNONYMS_FIELD_VALUE)
		private Collection<String> collectionField = Arrays.asList("3", "4");
	}

	/**
	 * This is a DataRecord class that has a field that is a sub-record
	 */
	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.GOA_GAF_RECORD)
	private static class TestDataRecordWithSubRecord extends TestDataRecord {
		@RecordField(ontClass = CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_NAME_FIELD_VALUE)
		private SubRecord subRecordField = new SubRecord("AABBCC", "XXYYZZ");
	}

	/**
	 * This class represents a Sub-record that will be a field in other record
	 * classes
	 * 
	 */
	@Record(dataSource = DataSource.KEGG, comment = "This is a sub-record class", ontClass = CcpExtensionOntology.HPO_ANNOTATION_RECORD)
	@Data
	private static class SubRecord implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.HPO_ANNOTATION_RECORD___HPO_TERM_NAME_FIELD_VALUE)
		private final String subStringField1;
		@RecordField(ontClass = CcpExtensionOntology.NAME_SYNONYMS_FIELD_VALUE)
		private final String subStringField2;

	}

	/**
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public final void testGetRecordInstanceStatementsWithSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestDataRecordWithSubRecord();

		String fieldValueHash1 = "Y1hjcJI45sdEWqlDHGqW-ZTXRsA";
		String fieldValueHash2 = "OmYYDRUb8J1Czc5oVJylN61vAZ8";
		String fieldValueHash3 = "LaDkkgFRguECzB9LF81DCE_b7es";
		String fieldValueHash4 = "oE49wz1Ki32ivuid120WR5iHQ9Q";
		String subRecordHash = "PyNURVmw6qiw-R0qkg86MSacIAM";
		String fieldValueHash5 = "UGT91mCJZqOPMEhgRQF0K5on24g";
		String fieldValueHash6 = "WzInWzqftc4j-WXTe1VkisDjgiE";

		/* @formatter:off*/
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.GOA_GAF_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ", " + RDFS.LABEL.toString() + ", \"1\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYMBOL_FIELD_VALUE.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ", " + RDFS.LABEL.toString() + ", \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.FILE_SIZE_BYTES.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ", " + RDFS.LABEL.toString() + ", \"3\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ", " + RDFS.LABEL.toString() + ", \"4\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.GOA_GAF_V20_ANNOTATION_RECORD___DATABASE_OBJECT_NAME_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HPO_ANNOTATION_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ", " + RDFS.LABEL.toString() + ", \"AABBCC\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.HPO_ANNOTATION_RECORD___HPO_TERM_NAME_FIELD_VALUE.uri() + ")",
			
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ", " + RDFS.LABEL.toString() + ", \"XXYYZZ\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_SYNONYMS_FIELD_VALUE.uri() + ")");
			/* @formatter:on*/

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());

		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));

	}

	/**
	 * This is a DataRecord class that has a field that is a collection of
	 * sub-records
	 */
	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	private static class TestDataRecordWithSubRecordCollection extends TestDataRecord {
		@RecordField(ontClass = CcpExtensionOntology.SUBRECORD)
		private Collection<SubRecord> subRecordCollectionField = Arrays.asList(new SubRecord("AABBCC", "XXYYZZ"),
				new SubRecord("ABABABABA", "778899"));
	}

	@Test
	public final void testGetRecordInstanceStatementsWithCollectionSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();

		String fieldValueHash1 = "Y1hjcJI45sdEWqlDHGqW-ZTXRsA";
		String fieldValueHash2 = "OmYYDRUb8J1Czc5oVJylN61vAZ8";
		String fieldValueHash3 = "LaDkkgFRguECzB9LF81DCE_b7es";
		String fieldValueHash4 = "oE49wz1Ki32ivuid120WR5iHQ9Q";
		String subRecordHash2 = "o5M4rxykXc8IiQ3jPbBEVas4POc";
		String fieldValueHash5 = "UGT91mCJZqOPMEhgRQF0K5on24g";
		String fieldValueHash6 = "WzInWzqftc4j-WXTe1VkisDjgiE";
		String subRecordHash1 = "PyNURVmw6qiw-R0qkg86MSacIAM";
		String fieldValueHash7 = "vjOXKAMRa4AA8jYMeNZqAirFXG0";
		String fieldValueHash8 = "REwB4v5-tgw3nELiwL23fWpR744";

		/* @formatter:off*/
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HGNC_GENE_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ", " + RDFS.LABEL.toString() + ", \"1\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYMBOL_FIELD_VALUE.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ", " + RDFS.LABEL.toString() + ", \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.FILE_SIZE_BYTES.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ", " + RDFS.LABEL.toString() + ", \"3\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ", " + RDFS.LABEL.toString() + ", \"4\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
			
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.SUBRECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HPO_ANNOTATION_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ", " + RDFS.LABEL.toString() + ", \"AABBCC\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.HPO_ANNOTATION_RECORD___HPO_TERM_NAME_FIELD_VALUE.uri() + ")",
			
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ", " + RDFS.LABEL.toString() + ", \"XXYYZZ\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_SYNONYMS_FIELD_VALUE.uri() + ")",
			
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.SUBRECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HPO_ANNOTATION_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash7 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash7 + ", " + RDFS.LABEL.toString() + ", \"ABABABABA\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash7 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.HPO_ANNOTATION_RECORD___HPO_TERM_NAME_FIELD_VALUE.uri() + ")",
			
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash8 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash8 + ", " + RDFS.LABEL.toString() + ", \"778899\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash8 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_SYNONYMS_FIELD_VALUE.uri() + ")");
			/* @formatter:on*/

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(
				new TestDataRecordWithSubRecordCollection(), createdTimeInMillis, new URIImpl("http://record.uri"),
				new NoOpDuplicateStatementFilter());
		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));

	}

	/**
	 * This class represents a Sub-record that will be a field in other record
	 * classes
	 * 
	 */
	@Record(dataSource = DataSource.KEGG, comment = "This is a sub-record class that has a subrecord itself", ontClass = CcpExtensionOntology.GOA_GAF_RECORD)
	private static class NestedSubRecord implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.SOURCE_DATABASE_NAME_FIELD_VALUE)
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
	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	private static class TestDataRecordWithNestedSubRecord extends TestDataRecord {
		@RecordField(ontClass = CcpExtensionOntology.SUBRECORD)
		private NestedSubRecord nestedSubRecordField = new NestedSubRecord("MI:0123", "miTerm123");
	}

	@Test
	public final void testGetRecordInstanceStatementsWithNestedSubRecord() throws URISyntaxException {
		long createdTimeInMillis = new GregorianCalendar(2010, 11, 21).getTimeInMillis();
		TestDataRecord r = new TestDataRecordWithNestedSubRecord();

		String fieldValueHash1 = "Y1hjcJI45sdEWqlDHGqW-ZTXRsA";
		String fieldValueHash2 = "OmYYDRUb8J1Czc5oVJylN61vAZ8";
		String fieldValueHash3 = "LaDkkgFRguECzB9LF81DCE_b7es";
		String fieldValueHash4 = "oE49wz1Ki32ivuid120WR5iHQ9Q";
		String subRecordHash2 = "0NXuy7cW1TS8M011RaFv00tMtU4";
		String subRecordHash1 = "U8jUIztV_hVS3iS5GPGiJoAlvrw";
		String fieldValueHash5 = "e4ZIzcl5h8DRcMO-1XgxuzOTed0";
		String fieldValueHash6 = "NWDo6g_-RTJBSS22ys_3rlGd-lY";

		/* @formatter:off */
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/obo/ext/RS_KEGG_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HGNC_GENE_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ", " + RDFS.LABEL.toString() + ", \"1\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYMBOL_FIELD_VALUE.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ", " + RDFS.LABEL.toString() + ", \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.FILE_SIZE_BYTES.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ", " + RDFS.LABEL.toString() + ", \"3\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash3 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
				
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ", " + RDFS.LABEL.toString() + ", \"4\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash4 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.SYNONYMS_FIELD_VALUE.uri() + ")",
			
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/R_"+subRecordHash1+")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.SUBRECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.GOA_GAF_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash1 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ")",

				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.SOURCE_DATABASE_NAME_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.IREFWEB_INTERACTION_SOURCE_DATABASE_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ", " + RDFS.LABEL.toString() + ", \"MI:0123\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.IREFWEB_INTERACTION_SOURCE_DATABASE_RECORD___SOURCE_DATABASE_IDENTIFIER_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash5 + ", " + RDF.TYPE.toString() + ", http://ccp.ucdenver.edu/obo/ext/MI_0123)",
				"(http://ccp.ucdenver.edu/obo/ext/MI_0123, "+ RDFS.SUBCLASSOF.toString() + ", "+ CcpExtensionOntology.MOLECULAR_INTERACTION_ONTOLOGY_CONCEPT_IDENTIFIER.uri() + ")",
//				
				"(http://ccp.ucdenver.edu/obo/ext/R_" + subRecordHash2 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ")",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ", " + RDFS.LABEL.toString() + ", \"miTerm123\"@en)",
				"(http://ccp.ucdenver.edu/obo/ext/F_" + fieldValueHash6 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.IREFWEB_INTERACTION_SOURCE_DATABASE_RECORD___SOURCE_DATABASE_NAME_FIELD_VALUE.uri() + ")");
				/* @formatter:on */

		
		//TODO fix the expected representation of an identifier as field value
		
		// "(http://ccp.ucdenver.edu/obo/ext/keggTestDataRecordWithNestedSubRecordDataSet20101221,
		// http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
		// "(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://ccp.ucdenver.edu/obo/ext/TestDataRecordWithNestedSubRecord)",
		// "(http://record.uri, http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://ccp.ucdenver.edu/obo/ext/TestDataRecordWithNestedSubRecordSchema1)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_collectionField_SG1QsGZwhJNaTdMnzgma3v5AB24,
		// http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_collectionField_SG1QsGZwhJNaTdMnzgma3v5AB24,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://ccp.ucdenver.edu/obo/ext/TestDataRecordWithNestedSubRecord_collectionFieldDataField1)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_collectionField_SG1QsGZwhJNaTdMnzgma3v5AB24,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://kabob.ucdenver.edu/iao/FieldValue)",
		// "(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051,
		// http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As,
		// http://purl.obolibrary.org/obo/IAO_0000219, \"2\"@en)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://ccp.ucdenver.edu/obo/ext/TestDataRecordWithNestedSubRecord_collectionFieldDataField1)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_collectionField_wICWjHJMs-mggQ_vE6Jc0mnb2As,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://kabob.ucdenver.edu/iao/FieldValue)",
		// "(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051,
		// http://ccp.ucdenver.edu/obo/ext/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU)",
		// "(http://ccp.ucdenver.edu/obo/ext/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://ccp.ucdenver.edu/obo/ext/NestedSubRecord)",
		// "(http://ccp.ucdenver.edu/obo/ext/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://ccp.ucdenver.edu/obo/ext/NestedSubRecordSchema1)",
		// "(http://ccp.ucdenver.edu/obo/ext/R_NestedSubRecord_QrM2DZEWgi_Lp-In-_vZnXBaPWU,
		// http://purl.obolibrary.org/obo/BFO_0000051,
		// http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_ngBtrmwImWPs5G7VIcb8LT0YmRQ)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_ngBtrmwImWPs5G7VIcb8LT0YmRQ,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabase)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_ngBtrmwImWPs5G7VIcb8LT0YmRQ,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabaseSchema1)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_ngBtrmwImWPs5G7VIcb8LT0YmRQ,
		// http://purl.obolibrary.org/obo/BFO_0000051,
		// http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_OQ6MB4TaBkNXN6mQ_jmDABOSbPM)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_OQ6MB4TaBkNXN6mQ_jmDABOSbPM,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabase_sourceDatabaseIdDataField1)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_OQ6MB4TaBkNXN6mQ_jmDABOSbPM,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://kabob.ucdenver.edu/iao/FieldValue)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseId_OQ6MB4TaBkNXN6mQ_jmDABOSbPM,
		// http://purl.obolibrary.org/obo/IAO_0000219,
		// http://kabob.ucdenver.edu/iao/mi/MI_0123_ICE)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/R_IRefWebInteractionSourceDatabase_ngBtrmwImWPs5G7VIcb8LT0YmRQ,
		// http://purl.obolibrary.org/obo/BFO_0000051,
		// http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://kabob.ucdenver.edu/iao/irefweb/IRefWebInteractionSourceDatabase_sourceDatabaseNameDataField1)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://kabob.ucdenver.edu/iao/FieldValue)",
		// "(http://kabob.ucdenver.edu/iao/irefweb/F_IRefWebInteractionSourceDatabase_sourceDatabaseName_8f_AI3EeN_eQ7M4Y6Ds14YrZmcU,
		// http://purl.obolibrary.org/obo/IAO_0000219, \"miTerm123\"@en)",
		// "(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051,
		// http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://ccp.ucdenver.edu/obo/ext/TestDataRecordWithNestedSubRecord_primitiveIntFieldDataField1)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://kabob.ucdenver.edu/iao/FieldValue)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_primitiveIntField_hzUY3pYkyLsIgSjagFn909vXijg,
		// http://purl.obolibrary.org/obo/IAO_0000219,
		// \"2\"^^<http://www.w3.org/2001/XMLSchema#integer>)",
		// "(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051,
		// http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8,
		// http://kabob.ucdenver.edu/iao/hasTemplate,
		// http://ccp.ucdenver.edu/obo/ext/TestDataRecordWithNestedSubRecord_stringFieldDataField1)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8,
		// http://www.w3.org/1999/02/22-rdf-syntax-ns#type,
		// http://kabob.ucdenver.edu/iao/FieldValue)",
		// "(http://ccp.ucdenver.edu/obo/ext/F_TestDataRecordWithNestedSubRecord_stringField_QEauZ8dFR2e9ZMcNoVMBLPRhFb8,
		// http://purl.obolibrary.org/obo/IAO_0000219, \"1\"@en)");

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());
		assertTrue(FileComparisonUtil.hasExpectedLines(new ArrayList<String>(CollectionsUtil.toString(statements)),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));

	}

}
