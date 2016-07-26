package edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.format.gaf2.Gaf2FileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

@Record(dataSource = DataSource.GOA)
public class GoaGaf2FileRecord extends Gaf2FileRecord {

	public GoaGaf2FileRecord(Gaf2FileRecord record) {
		super(record);
	}

}
