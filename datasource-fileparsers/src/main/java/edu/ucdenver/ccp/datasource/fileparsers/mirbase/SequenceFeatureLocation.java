/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Data
@Record(dataSource=DataSource.EMBL)
public class SequenceFeatureLocation {
	@RecordField
	public final Integer startOffset;
	@RecordField
	public final Integer endOffset;
}
