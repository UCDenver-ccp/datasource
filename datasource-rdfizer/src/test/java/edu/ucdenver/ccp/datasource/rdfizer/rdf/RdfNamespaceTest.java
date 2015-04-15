package edu.ucdenver.ccp.datasource.rdfizer.rdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class RdfNamespaceTest {

	@Test
	public void testGetNamespaceFromLongName() {
		assertEquals(DataSource.MGI, DataSource.getNamespace("http://www.informatics.jax.org/"));
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamespaceFromLongName_InvalidInput() {
		DataSource.getNamespace("http://this.is.not.a.valid.long.name/");
	}
}
