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
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
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
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.filter.NoOpDuplicateStatementFilter;
import lombok.Data;

/**
 * Tests in this class are specific to sub-record handling where there are
 * duplicate subrecords assigned to different field values. Ensure that the
 * subrecords in different field values are not collapsed.
 * 
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class SubRecordUtil_DuplicatesTest {

	@Record(dataSource = DataSource.KEGG, ontClass = CcpExtensionOntology.HGNC_GENE_RECORD)
	public static class TestDataRecord implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.SYMBOL_FIELD_VALUE)
		private SubRecord idField = new SubRecord("hgnc", "12345");
		@RecordField(ontClass = CcpExtensionOntology.NAME_FIELD_VALUE)
		private SubRecord nameField = new SubRecord("hgnc", "12345");
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
		TestDataRecord r = new TestDataRecord();

		String subRecordHash1 = "06iRnTSfbV9vOAJ1laBG4i-Ko78";
		String sr1fvHash1 = "JHePhOoX45lWomvpR_Hn0NLO3co";
		String sr1fvHash2 = "yLHl5gQ-r7XdWjC5GVlW3KqajWg";

		String subRecordHash2 = "45aoWbM4xB8xQM8ZX5l_YVpR16I";
		String sr2fvHash1 = sr1fvHash1;
		String sr2fvHash2 = sr1fvHash2;

		/* @formatter:off*/
		Set<String> expectedStatements = CollectionsUtil.createSet(
				"(http://ccp.ucdenver.edu/kabob/ice/DS_KEGG_20101221, http://purl.obolibrary.org/obo/BFO_0000051, http://record.uri)",
				"(http://record.uri, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HGNC_GENE_RECORD.uri() + ")",
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash1 + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.SYMBOL_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HPO_ANNOTATION_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash1 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/kabob/ice/F_" + sr1fvHash1 + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr1fvHash1 + ", " + RDFS.LABEL.toString() + ", \"hgnc\"@en)",
				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr1fvHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.HPO_ANNOTATION_RECORD___HPO_TERM_NAME_FIELD_VALUE.uri() + ")",
			
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash1 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/kabob/ice/F_" + sr1fvHash2 + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr1fvHash2 + ", " + RDFS.LABEL.toString() + ", \"12345\"@en)",
				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr1fvHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_SYNONYMS_FIELD_VALUE.uri() + ")",
			
				"(http://record.uri, http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash2 + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.NAME_FIELD_VALUE.uri() + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, "	+ CcpExtensionOntology.HPO_ANNOTATION_RECORD.uri() + ")",
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash2 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/kabob/ice/F_" + sr2fvHash1 + ")",
//				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr2fvHash1 + ", " + RDFS.LABEL.toString() + ", \"hgnc\"@en)",
//				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr2fvHash1 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.HPO_ANNOTATION_RECORD___HPO_TERM_NAME_FIELD_VALUE.uri() + ")",
				
				"(http://ccp.ucdenver.edu/kabob/ice/R_" + subRecordHash2 + ", http://purl.obolibrary.org/obo/BFO_0000051, http://ccp.ucdenver.edu/kabob/ice/F_" + sr2fvHash2 + ")"
//				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr2fvHash2 + ", " + RDFS.LABEL.toString() + ", \"12345\"@en)",
//				"(http://ccp.ucdenver.edu/kabob/ice/F_" + sr2fvHash2 + ", http://www.w3.org/1999/02/22-rdf-syntax-ns#type, " + CcpExtensionOntology.NAME_SYNONYMS_FIELD_VALUE.uri() + ")"
				);
			/* @formatter:on*/

		Collection<? extends Statement> statements = RdfRecordUtil.getRecordInstanceStatements(r, createdTimeInMillis,
				new URIImpl("http://record.uri"), new NoOpDuplicateStatementFilter());

		assertTrue(FileComparisonUtil.hasExpectedLines(
				new ArrayList<String>(new HashSet<String>(CollectionsUtil.toString(statements))),
				new ArrayList<String>(expectedStatements), null, LineOrder.ANY_ORDER, ColumnOrder.AS_IN_FILE,
				LineTrim.OFF, ShowWhiteSpace.OFF));

	}

}
