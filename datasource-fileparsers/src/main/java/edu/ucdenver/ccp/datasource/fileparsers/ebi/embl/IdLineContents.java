/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public interface IdLineContents<T extends DataSourceIdentifier<?>> {
	public T getPrimaryAccessionNumber();
}
