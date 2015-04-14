package edu.ucdenver.ccp.fileparsers.hgnc;

import edu.ucdenver.ccp.fileparsers.field.DataElementLiteral;

/**
 * * Status - Indicates whether the gene is classified as: o Approved - these genes have
 * HGNC-approved gene symbols o Entry withdrawn - these previously approved genes are no longer
 * thought to exist o Symbol withdrawn - a previously approved record that has since been merged
 * into a another record
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public class HgncStatus extends DataElementLiteral<String> {

	public HgncStatus(String status) {
		super(status);
	}

}
