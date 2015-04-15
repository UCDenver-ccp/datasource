/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.filter;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileArchiveUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.ColumnOrder;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.LineOrder;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class DuplicateFieldValueFilterTest extends DefaultTestCase {

	private static final List<String> rdf = CollectionsUtil
			.createList(
					"<http://kabob.ucdenver.edu/iao/hprd/hprdDataSource20130701> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/hprd/hprdDataSource> .",
					"<http://kabob.ucdenver.edu/iao/hprd/hprdHprdIdMappingsTxtFileDataDataSet20130701> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/hprdHprdIdMappingsTxtFileDataSchema1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/hprdDataSource20130701> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/hprd/hprdHprdIdMappingsTxtFileDataDataSet20130701> .",
					"<http://kabob.ucdenver.edu/iao/hprd/hprdHprdIdMappingsTxtFileDataDataSet20130701> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/DataSet> .",
					"<http://kabob.ucdenver.edu/iao/hprd/hprdHprdIdMappingsTxtFileDataDataSet20130701> <http://kabob.ucdenver.edu/iao/hasCreationDate> \"2013-07-01T00:00:00.000-06:00\"^^<http://www.w3.org/2001/XMLSchema#dateTime> .",
					"<http://kabob.ucdenver.edu/iao/hprd/hprdHprdIdMappingsTxtFileDataDataSet20130701> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_hDafTYVx4wPUw_w5Ck2SsiA7sgY> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_hDafTYVx4wPUw_w5Ck2SsiA7sgY> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileData> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_hDafTYVx4wPUw_w5Ck2SsiA7sgY> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileDataSchema1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_hDafTYVx4wPUw_w5Ck2SsiA7sgY> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_rgB00oPB7KpKAed3uhvN2yGcHSE> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_rgB00oPB7KpKAed3uhvN2yGcHSE> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileData_entrezGeneIDDataField1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_rgB00oPB7KpKAed3uhvN2yGcHSE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_rgB00oPB7KpKAed3uhvN2yGcHSE> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_216_ICE> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_hDafTYVx4wPUw_w5Ck2SsiA7sgY> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileData_geneSymbolDataField1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> <http://purl.obolibrary.org/obo/IAO_0000219> \"ALDH1A1\"@en .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_hDafTYVx4wPUw_w5Ck2SsiA7sgY> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_hprdID_h7CdKWFTsEl1XEnik4_j2-saVUE> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_hprdID_h7CdKWFTsEl1XEnik4_j2-saVUE> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileData_hprdIDDataField1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_hprdID_h7CdKWFTsEl1XEnik4_j2-saVUE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_hprdID_h7CdKWFTsEl1XEnik4_j2-saVUE> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/hprd/HPRD_00001_ICE> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_E6Iia3yiRLgdu6OeqHIuCaTYSeo> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileData> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_E6Iia3yiRLgdu6OeqHIuCaTYSeo> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileDataSchema1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_E6Iia3yiRLgdu6OeqHIuCaTYSeo> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_hFOPplrqOfFqxvQ_Jjd53tIuUx0> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_hFOPplrqOfFqxvQ_Jjd53tIuUx0> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileData_entrezGeneIDDataField1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_hFOPplrqOfFqxvQ_Jjd53tIuUx0> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_entrezGeneID_hFOPplrqOfFqxvQ_Jjd53tIuUx0> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_2197_ICE> .",
					"<http://kabob.ucdenver.edu/iao/hprd/R_HprdIdMappingsTxtFileData_E6Iia3yiRLgdu6OeqHIuCaTYSeo> <http://purl.obolibrary.org/obo/has_part> <http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> <http://kabob.ucdenver.edu/iao/hasTemplate> <http://kabob.ucdenver.edu/iao/hprd/HprdIdMappingsTxtFileData_geneSymbolDataField1> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/iao/FieldValue> .",
					"<http://kabob.ucdenver.edu/iao/hprd/F_HprdIdMappingsTxtFileData_geneSymbol_HcefK7kdtvPDQJtRbvEz1Q4Til0> <http://purl.obolibrary.org/obo/IAO_0000219> \"ALDH1A1\"@en .");

	@Test
	public void test() throws IOException {
		File dir1 = folder.newFolder("dir1");
		File dir2 = new File(dir1, "dir2");
		File fileWithDups = new File(dir2, "fileWithDups.nt");
		File fileWithDups2 = new File(dir2, "fileWithDups2.nt");

		FileWriterUtil.printLines(rdf, fileWithDups, CharacterEncoding.UTF_8, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);

		File fileWithDupsGz = FileArchiveUtil.gzipFile(fileWithDups);

		FileWriterUtil.printLines(rdf, fileWithDups2, CharacterEncoding.UTF_8, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);

		File fileWithDupsGz2 = FileArchiveUtil.gzipFile(fileWithDups2);

		// DuplicateTripleFilter filter = new DuplicateTripleFilter(new
		// Jdbm2Cache(folder.newFile("cacheFile")),
		// TrimLines.YES);

		// FileUtil.deleteDirectory(new File("/tmp/jcs_swap"));
		// DuplicateTripleFilter filter = new DuplicateTripleFilter(new JcsCache(), TrimLines.YES);
//		DiskBasedHash cache = new Neo4jCache(folder.newFolder("neo4jcache"), 1000);
		File cacheFilePrefix = new File(folder.newFolder("filter-cache"), "filter");

		DuplicateFieldValueFilter filter = new DefaultDuplicateStatementFilter(cacheFilePrefix);
		filter.createNonRedundantRdfFiles(dir2);
		filter.shutdown();

		File expectedNoDupsFileGz = new File(dir2, "fileWithDups.nodups.nt.gz");
		assertTrue(expectedNoDupsFileGz.exists());
		File expectedNoDupsFileGz2 = new File(dir2, "fileWithDups2.nodups.nt.gz");
		assertTrue(expectedNoDupsFileGz2.exists());

		List<String> expectedLines = new ArrayList<String>(rdf);
		expectedLines.remove(expectedLines.size() - 1);
		expectedLines.remove(expectedLines.size() - 1);
		expectedLines.remove(expectedLines.size() - 1);
		File expectedNoDupsFile = FileArchiveUtil.gunzipFile(expectedNoDupsFileGz);

		List<String> expectedLines2 = new ArrayList<String>(rdf);
		System.out.println("size: " + expectedLines2.size());
		expectedLines2.remove(expectedLines2.size() - 1);
		expectedLines2.remove(expectedLines2.size() - 1);
		expectedLines2.remove(expectedLines2.size() - 1);
		expectedLines2.remove(25);
		expectedLines2.remove(24);
		expectedLines2.remove(23);
		expectedLines2.remove(19);
		expectedLines2.remove(18);
		expectedLines2.remove(17);
		expectedLines2.remove(15);
		expectedLines2.remove(14);
		expectedLines2.remove(13);
		expectedLines2.remove(11);
		expectedLines2.remove(10);
		expectedLines2.remove(9);
		File expectedNoDupsFile2 = FileArchiveUtil.gunzipFile(expectedNoDupsFileGz2);

		boolean a11 = FileComparisonUtil.hasExpectedLines(expectedNoDupsFile, CharacterEncoding.UTF_8, expectedLines,
				null, LineOrder.AS_IN_FILE, ColumnOrder.AS_IN_FILE);
		boolean a22 = FileComparisonUtil.hasExpectedLines(expectedNoDupsFile2, CharacterEncoding.UTF_8, expectedLines2,
				null, LineOrder.AS_IN_FILE, ColumnOrder.AS_IN_FILE);
		boolean a21 = FileComparisonUtil.hasExpectedLines(expectedNoDupsFile2, CharacterEncoding.UTF_8, expectedLines,
				null, LineOrder.AS_IN_FILE, ColumnOrder.AS_IN_FILE);
		boolean a12 = FileComparisonUtil.hasExpectedLines(expectedNoDupsFile, CharacterEncoding.UTF_8, expectedLines2,
				null, LineOrder.AS_IN_FILE, ColumnOrder.AS_IN_FILE);

		assertTrue((a11 && a22) || (a21 && a12));
	}
}
