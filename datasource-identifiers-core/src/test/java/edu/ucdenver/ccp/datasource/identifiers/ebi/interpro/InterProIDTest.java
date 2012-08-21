package edu.ucdenver.ccp.datasource.identifiers.ebi.interpro;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;


public class InterProIDTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_DoesNotStartWithIPR() {
		new InterProID("ABC00005");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_EndsInSomethingOtherThanDigits() {
		new InterProID("ABC00005A");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NullInput() {
		new InterProID(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_EmptyStringInput() {
		new InterProID("");
	}
	
	
}
