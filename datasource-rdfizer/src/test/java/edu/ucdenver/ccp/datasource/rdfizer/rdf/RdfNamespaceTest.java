package edu.ucdenver.ccp.datasource.rdfizer.rdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;

public class RdfNamespaceTest {

	@Test
	public final void dataSourceSynchronization() {
		for (DataSource ds : DataSource.values()) {
			if (ds == DataSource.ANY)
				continue; 
			
			try {
				RdfNamespace.getNamespace(ds);
			} catch (IllegalStateException e) {
				fail(String.format("Data source %s does not have corresponding" +
						" rdf namespace definition. Error message: %s", ds, e.getMessage()));
			}
		}
	}
	
	
	@Test
	public void testGetNamespaceFromLongName() {
		assertEquals(RdfNamespace.MGI, RdfNamespace.getNamespace("http://www.informatics.jax.org/"));
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamespaceFromLongName_InvalidInput() {
		RdfNamespace.getNamespace("http://this.is.not.a.valid.long.name/");
	}
}
