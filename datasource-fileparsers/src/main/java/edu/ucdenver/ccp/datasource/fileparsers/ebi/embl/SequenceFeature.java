/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import lombok.Data;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public interface SequenceFeature {

	public String getKey();
	public SequenceFeatureLocation getLocation();
	public SequenceFeatureQualifierSet getQualifierSet();
	
}


