/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.snomed;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileComparisonUtil;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.ColumnOrder;
import edu.ucdenver.ccp.common.file.FileComparisonUtil.LineOrder;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class SnomedRfFileCleanerTest extends DefaultTestCase {

	private File snomedConceptFile;
	private File snomedRelationshipFile;

	@Before
	public void setUp() throws IOException {
		snomedConceptFile = folder.newFile("snomed_concept.utf8");
		populateSnomedConceptFile(snomedConceptFile);
		snomedRelationshipFile = folder.newFile("snomed_relationship.utf8");
		populateSnomedRelationshipFile(snomedRelationshipFile);
	}

	/**
	 * @param snomedRelationshipFile2
	 * @throws IOException
	 */
	private void populateSnomedRelationshipFile(File snomedRelationshipFile2) throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"id\teffectiveTime\tactive\tmoduleId\tsourceId\tdestinationId\trelationshipGroup\ttypeId\tcharacteristicTypeId\tmodifierId",
						"2688910022\t20080131\t1\t900000000000207008\t1734006\t269078005\t0\t116680003\t900000000000011006\t900000000000451002",
						"2688910022\t20110731\t0\t900000000000207008\t1734006\t269078005\t0\t116680003\t900000000000011006\t900000000000451002",
						"268891025\t20020131\t1\t900000000000207008\t74319002\t68406005\t0\t116680003\t900000000000011006\t900000000000451002",
						"1000131027\t20020131\t1\t900000000000207008\t10170007\t83386001\t3\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20030731\t1\t900000000000207008\t10170007\t83386001\t1\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20040731\t1\t900000000000207008\t10170007\t83386001\t2\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20050131\t1\t900000000000207008\t10170007\t83386001\t1\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20080731\t0\t900000000000207008\t10170007\t83386001\t1\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20090131\t1\t900000000000207008\t10170007\t83386001\t1\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20090731\t0\t900000000000207008\t10170007\t83386001\t1\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20100731\t1\t900000000000207008\t10170007\t83386001\t1\t116676008\t900000000000011006\t900000000000451002",
						"1000131027\t20110131\t0\t900000000000207008\t10170007\t83386001\t1\t116676008\t900000000000011006\t900000000000451002"

				);
		FileWriterUtil.printLines(lines, snomedRelationshipFile2, CharacterEncoding.UTF_8);
	}

	/**
	 * @param snomedConceptFile2
	 * @throws IOException
	 */
	private void populateSnomedConceptFile(File snomedConceptFile2) throws IOException {
		List<String> lines = CollectionsUtil.createList("id\teffectiveTime\tactive\tmoduleId\tdefinitionStatusId",
				"100000000\t20020131\t1\t900000000000207008\t900000000000074008",
				"100000000\t20090731\t0\t900000000000207008\t900000000000074008",
				"10000006\t20020131\t1\t900000000000207008\t900000000000074008",
				"1000004\t20020131\t1\t900000000000207008\t900000000000074008",
				"1000004\t20030131\t0\t900000000000207008\t900000000000074008",
				"100001001\t20020131\t1\t900000000000207008\t900000000000074008",
				"100001001\t20090731\t0\t900000000000207008\t900000000000074008",
				"100002008\t20020131\t1\t900000000000207008\t900000000000074008",
				"100002008\t20090731\t0\t900000000000207008\t900000000000074008",
				"100003003\t20020131\t1\t900000000000207008\t900000000000074008",
				"100003003\t20090731\t0\t900000000000207008\t900000000000074008",
				"100004009\t20020131\t1\t900000000000207008\t900000000000074008",
				"100004009\t20090731\t0\t900000000000207008\t900000000000074008",
				"100005\t20020131\t0\t900000000000207008\t900000000000074008",
				"100005005\t20020131\t1\t900000000000207008\t900000000000074008",
				"100005005\t20090731\t0\t900000000000207008\t900000000000074008",
				"100006006\t20020131\t1\t900000000000207008\t900000000000074008",
				"100006006\t20090731\t0\t900000000000207008\t900000000000074008",
				"100007002\t20020131\t1\t900000000000207008\t900000000000074008",
				"100007002\t20090731\t0\t900000000000207008\t900000000000074008",
				"100008007\t20020131\t1\t900000000000207008\t900000000000074008",
				"100008007\t20090731\t0\t900000000000207008\t900000000000074008",
				"100009004\t20020131\t1\t900000000000207008\t900000000000074008",
				"100009004\t20090731\t0\t900000000000207008\t900000000000074008",
				"100010009\t20020131\t1\t900000000000207008\t900000000000074008",
				"100010009\t20090731\t0\t900000000000207008\t900000000000074008",
				"10001005\t20020131\t1\t900000000000207008\t900000000000074008",
				"10001005\t20020731\t1\t900000000000207008\t900000000000073002");

		FileWriterUtil.printLines(lines, snomedConceptFile2, CharacterEncoding.UTF_8);

	}

	@Test
	public void testTimeComparison() {
		String timeBefore = "20080731";
		String timeAfter = "20110131";
		assertTrue(SnomedRf2ConceptFileCleaner.timeIsEqualToOrGreaterThan(timeAfter, timeBefore));
	}
	
	@Test
	public void testConceptClean() throws IOException {
		List<String> expectedLines = CollectionsUtil.createList(
				"id\teffectiveTime\tactive\tmoduleId\tdefinitionStatusId",
				"10000006\t20020131\t1\t900000000000207008\t900000000000074008",
				"10001005\t20020731\t1\t900000000000207008\t900000000000073002");
		File outputFile = folder.newFile("snomed_concepts.cleaned.utf8");
		SnomedRf2ConceptFileCleaner.cleanConceptFile(snomedConceptFile, outputFile);
		assertTrue(FileComparisonUtil.hasExpectedLines(outputFile, CharacterEncoding.UTF_8, expectedLines, null,
				LineOrder.AS_IN_FILE, ColumnOrder.AS_IN_FILE));
	}

	@Test
	public void testRelationshipClean() throws IOException {
		List<String> expectedLines = CollectionsUtil
				.createList(
						"id\teffectiveTime\tactive\tmoduleId\tsourceId\tdestinationId\trelationshipGroup\ttypeId\tcharacteristicTypeId\tmodifierId",
						"268891025\t20020131\t1\t900000000000207008\t74319002\t68406005\t0\t116680003\t900000000000011006\t900000000000451002");
		File outputFile = folder.newFile("snomed_relationships.cleaned.utf8");
		SnomedRf2ConceptFileCleaner.cleanRelationshipFile(snomedRelationshipFile, outputFile);
		assertTrue(FileComparisonUtil.hasExpectedLines(outputFile, CharacterEncoding.UTF_8, expectedLines, null,
				LineOrder.AS_IN_FILE, ColumnOrder.AS_IN_FILE));
	}
}
