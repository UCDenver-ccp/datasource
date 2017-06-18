/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.embl;

import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EmblID;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class EmblIdLineContents extends EmblIdLineContentsBase<EmblID> {

	/**
	 * @param primaryAccessionNumber
	 * @param sequenceVersionNumber
	 * @param sequenceTopology
	 * @param moleculeType
	 * @param dataClass
	 * @param taxonomicDivision
	 * @param sequenceLengthInBasePairs
	 */
	public EmblIdLineContents(EmblID primaryAccessionNumber, String sequenceVersionNumber, String sequenceTopology,
			String moleculeType, String dataClass, String taxonomicDivision, int sequenceLengthInBasePairs) {
		super(primaryAccessionNumber, sequenceVersionNumber, sequenceTopology, moleculeType, dataClass, taxonomicDivision,
				sequenceLengthInBasePairs);
	}

}
