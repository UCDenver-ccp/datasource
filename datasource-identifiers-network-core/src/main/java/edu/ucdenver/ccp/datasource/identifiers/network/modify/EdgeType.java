/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.modify;


/**
 * This interface is used to create an "extensible" enum representing network edge types by
 * overloading the {@link Enum#name()} method.
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public interface EdgeType {
	/**
	 * @return the name of the edge
	 */
	public String name();
}
