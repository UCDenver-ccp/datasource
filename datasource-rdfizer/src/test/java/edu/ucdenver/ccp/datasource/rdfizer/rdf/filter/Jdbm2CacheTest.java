/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.rdfizer.rdf.filter.Jdbm2Cache;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Jdbm2CacheTest extends DefaultTestCase {

	private static final Object INPUT_1 = "input 1";
	private static final Object INPUT_2 = "input 2";
	private static final Object INPUT_3 = "input 3";

	@Ignore ("the cache is now specific to the field value de-duplication effort. this test no longer applies. To change, the input objects must look like fieldname_sha1")
	@Test
	public void test() throws IOException {
		File cacheFile = folder.newFile("cache");
		Jdbm2Cache cache = new Jdbm2Cache(cacheFile);

		cache.add(INPUT_1);
		cache.add(INPUT_2);
		cache.add(INPUT_3);

		assertTrue(cache.contains(INPUT_1));
		assertTrue(cache.contains(INPUT_2));
		assertTrue(cache.contains(INPUT_3));
		assertFalse(cache.contains("blah"));

		cache.shutdown();

		cache = new Jdbm2Cache(cacheFile);

		assertTrue(cache.contains(INPUT_1));
		assertTrue(cache.contains(INPUT_2));
		assertTrue(cache.contains(INPUT_3));
		assertFalse(cache.contains("blah"));

	}

}
