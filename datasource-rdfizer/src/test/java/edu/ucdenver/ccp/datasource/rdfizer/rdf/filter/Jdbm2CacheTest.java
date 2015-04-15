package edu.ucdenver.ccp.datasource.rdfizer.rdf.filter;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.test.DefaultTestCase;

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
