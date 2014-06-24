package edu.ucdenver.ccp.datasource.identifiers.ncbi.gene;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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

import org.junit.Test;

import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;

public class EntrezGeneIDTest extends DefaultTestCase {

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NotAnInteger() {
		new EntrezGeneID("this is not an integer");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NegativeInteger() {
		new EntrezGeneID(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NegativeIntegerString() {
		new EntrezGeneID("-12345");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NullIntegerInput() {
		new EntrezGeneID((Integer) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NullStringInput() {
		new EntrezGeneID((String) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_EmptyStringInput() {
		new EntrezGeneID("");
	}

}
