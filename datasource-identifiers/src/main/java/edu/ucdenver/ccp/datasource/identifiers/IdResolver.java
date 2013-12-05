/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public interface IdResolver {

	public DataSourceIdentifier<?> resolveId(String idStr);
	
	public DataSourceIdentifier<?> resolveId(String db, String id);
		
}
