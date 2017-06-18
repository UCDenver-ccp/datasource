package edu.ucdenver.ccp.datasource.identifiers.ec;

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

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnzymeCommissionID;

public class EnzymeCommissionIDTest {

	@Test()
	public void testWithValidInput() {
		new EnzymeCommissionID("4.3.55.66");
	}

	@Test()
	public void testWithValidInput_HasECPrefix() {
		new EnzymeCommissionID("EC 4.3.55.66");
	}

	@Test()
	public void testWithValidInput_HasECPrefix_HasHyphen() {
		new EnzymeCommissionID("EC 1.-");
	}

	@Test()
	public void testWithValidInput_HyphenAtEnd() {
		new EnzymeCommissionID("4.3.55.-");
	}

	@Test()
	public void testWithValidInput_HyphenAt3() {
		new EnzymeCommissionID("4.3.-.66");
	}

	@Test
	public void testWithValidInput_HyphenAt2() {
		new EnzymeCommissionID("4.-.55.66");
	}

	@Ignore("ignore until we figure out what makes an EC number invalid")
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NotAnEcNumber() {
		new EnzymeCommissionID("this is not an ec number");
	}

	@Ignore("ignore until we figure out what makes an EC number invalid")
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_AllHyphens() {
		new EnzymeCommissionID("-.-.-.-");
	}

	@Test
	public void testWithValidInput_HyphenAt4_MissingPeriod() {
		new EnzymeCommissionID("EC 2.7.1 -");
	}

	@Test
	public void testWithValidInput2() {
		new EnzymeCommissionID("EC 2.7.1");
	}
}
