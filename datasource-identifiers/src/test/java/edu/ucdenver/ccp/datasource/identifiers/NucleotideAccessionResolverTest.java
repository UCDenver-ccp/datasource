/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class NucleotideAccessionResolverTest {

	@Test
	public void testRefseqResolution() {
		assertEquals(new RefSeqID("NM_000518"), NucleotideAccessionResolver.resolveNucleotideAccession("NM_000518"));
		assertEquals(new RefSeqID("NM_000518"), NucleotideAccessionResolver.resolveNucleotideAccession("NM_000518.2"));
	}

}
