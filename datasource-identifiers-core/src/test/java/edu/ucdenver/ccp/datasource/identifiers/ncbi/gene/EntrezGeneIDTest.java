package edu.ucdenver.ccp.datasource.identifiers.ncbi.gene;

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
