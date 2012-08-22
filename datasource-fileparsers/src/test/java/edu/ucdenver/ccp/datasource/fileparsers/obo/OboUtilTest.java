/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.obo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.io.ClassPathUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class OboUtilTest extends DefaultTestCase {
	
	private static final String SAMPLE_OBO_FILE_NAME = "sample.obo";
	private OboUtil oboUtil;
	
	@Before
	public void setUp() throws IOException {
		File sampleOboFile = folder.newFile("sample.obo");
		ClassPathUtil.copyClasspathResourceToFile(getClass(), SAMPLE_OBO_FILE_NAME, sampleOboFile);
		oboUtil = new OboUtil(sampleOboFile, CharacterEncoding.UTF_8);
	}

	@Test
	public void testIsDescendent() {
		assertTrue(oboUtil.isDescendent("PR:000000101","PR:000000008"));
		assertTrue(oboUtil.isDescendent("PR:000000101","PR:000000046"));
		assertTrue(oboUtil.isDescendent("PR:000002012","PR:000000008"));
		assertFalse(oboUtil.isDescendent("PR:000002012","PR:000000046"));
		
	}

}
