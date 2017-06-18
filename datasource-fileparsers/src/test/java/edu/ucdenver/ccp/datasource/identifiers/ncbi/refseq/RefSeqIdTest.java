package edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq;

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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;



public class RefSeqIdTest {

	
	@Test
	public void testVersionRemoval() {
		RefSeqID expectedId = new RefSeqID("NP_047184");
		assertEquals("Version should have been removed automatically", expectedId, new RefSeqID("NP_047184.1"));
	}
	
	
	
	@SuppressWarnings("unused")
	@Test
	public void testValidRefSeqIdValidation() {
		new RefSeqID("AC_123456");
		new RefSeqID("AP_123456");
		new RefSeqID("NC_123456");
		new RefSeqID("NG_123456");
		new RefSeqID("NM_123456");
		new RefSeqID("NP_123456");
		new RefSeqID("NR_123456");
		new RefSeqID("NT_123456");
		new RefSeqID("NW_123456");
		new RefSeqID("NZ_ABCD12345678");
		new RefSeqID("XM_123456");
		new RefSeqID("XP_123456");
		new RefSeqID("XR_123456");
		new RefSeqID("YP_123456");
		new RefSeqID("ZP_12345678");
		new RefSeqID("NS_123456");
		new RefSeqID("XR_016092.");
		new RefSeqID("NZ_CH959310");
	}
	
	
	@SuppressWarnings("unused")
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidRefSeqIdValidation() {
		new RefSeqID("AB_123456");
	}
	
}
