package edu.ucdenver.ccp.datasource.identifiers.ec;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.ec.EnzymeCommissionID;

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

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NotAnEcNumber() {
		new EnzymeCommissionID("this is not an ec number");
	}

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
