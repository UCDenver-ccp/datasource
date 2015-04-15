/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import lombok.Data;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Data
public abstract class EmblIdLineContentsBase<T extends DataSourceIdentifier<?>> implements IdLineContents<T> {
	private final T primaryAccessionNumber;
	private final String sequenceVersionNumber;
	private final String sequenceTopology;
	private final String moleculeType;
	private final String dataClass;
	private final String taxonomicDivision;
	private final int sequenceLengthInBasePairs;
}
