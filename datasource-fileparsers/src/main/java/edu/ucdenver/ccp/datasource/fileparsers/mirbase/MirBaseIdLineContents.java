/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.mirbase;

import edu.ucdenver.ccp.datasource.identifiers.other.MiRBaseID;
import edu.ucdenver.ccp.fileparsers.ebi.embl.EmblIdLineContentsBase;

/**
 * Very similar to the EmblIdLineContents, however the sequence version and sequence topology fields
 * are not used.
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class MirBaseIdLineContents extends EmblIdLineContentsBase<MiRBaseID> {

	/**
	 * @param primaryAccessionNumber
	 * @param moleculeType
	 * @param dataClass
	 * @param taxonomicDivision
	 * @param sequenceLengthInBasePairs
	 */
	public MirBaseIdLineContents(MiRBaseID primaryAccessionNumber, String moleculeType, String dataClass,
			String taxonomicDivision, int sequenceLengthInBasePairs) {
		super(primaryAccessionNumber, null, null, moleculeType, dataClass, taxonomicDivision, sequenceLengthInBasePairs);
	}

}
