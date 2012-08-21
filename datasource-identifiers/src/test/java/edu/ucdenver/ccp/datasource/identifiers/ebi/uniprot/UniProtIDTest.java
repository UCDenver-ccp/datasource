package edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot;

import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;

public class UniProtIDTest extends DefaultTestCase {

	@Test
	public void testWithValidInput() {
		try {
			new UniProtID("P12345");
			new UniProtID("P4A123");
			new UniProtID("Q1AAA9");
		} catch (IllegalArgumentException e) {
			fail(String.format("Should not have thrown an exception. %s", e.getMessage()));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput() {
		new UniProtID("A00AA0");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_NullInput() {
		new UniProtID(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidInput_EmptyStringInput() {
		new UniProtID("");
	}

}
