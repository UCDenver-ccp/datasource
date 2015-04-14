/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.rgd;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.fileparsers.format.gaf2.Gaf2FileRecord;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.RGD, comment = "Annotations of RGD identifiers to other ontologies", label = "rgd annotation record")
public class RgdAnnotationGaf2FileRecord extends Gaf2FileRecord {

	public RgdAnnotationGaf2FileRecord(Gaf2FileRecord record) {
		super(record);
	}

}
