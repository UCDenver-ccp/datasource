/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class UniProtIsoformIdTest {

	@SuppressWarnings("unused")
	@Test
	public void testValid() {
		new UniProtIsoformID("P60953-2");
	}

}
