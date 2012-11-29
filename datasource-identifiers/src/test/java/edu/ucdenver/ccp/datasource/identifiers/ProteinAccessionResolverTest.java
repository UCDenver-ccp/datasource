/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.other.DdbjId;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class ProteinAccessionResolverTest {

	@Test
	public void testProteinAccessionResolution() {
		assertEquals(new GenBankID("AAI00916"), ProteinAccessionResolver.resolveProteinAccession("AAI00916"));
		assertEquals(new GenBankID("AAI00916.2"), ProteinAccessionResolver.resolveProteinAccession("AAI00916.2"));
		assertEquals(new EmblID("CAI00916"), ProteinAccessionResolver.resolveProteinAccession("CAI00916"));
		assertEquals(new DdbjId("GAI00916"), ProteinAccessionResolver.resolveProteinAccession("GAI00916"));
		assertEquals(new RefSeqID("NP_795370"), ProteinAccessionResolver.resolveProteinAccession("NP_795370"));
	}

}
