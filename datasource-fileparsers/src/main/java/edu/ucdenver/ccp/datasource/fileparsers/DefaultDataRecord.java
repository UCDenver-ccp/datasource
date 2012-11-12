/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class DefaultDataRecord implements DataRecord {
	protected static final String DEFAULT_RECORD_SCHEMA = "1";

	@Override
	public String getRecordSchemaVersion() {
		return DEFAULT_RECORD_SCHEMA;
	}

}
