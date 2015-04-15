/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.field;

import org.junit.Test;

import edu.ucdenver.ccp.fileparsers.irefweb.IRefWebHostOrganism;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class NcbiTaxonomyIdTermPairTest {

	@Test
	public void testIRefWebHostOrganismExtraction() {
		String input = "taxid:32644(unidentified)";
		IRefWebHostOrganism hostOrgTaxonomyId = NcbiTaxonomyIdTermPair.parseString(IRefWebHostOrganism.class, input);
		
		input = "taxid:-1(unidentified)";
		hostOrgTaxonomyId = NcbiTaxonomyIdTermPair.parseString(IRefWebHostOrganism.class, input);
	}

}
