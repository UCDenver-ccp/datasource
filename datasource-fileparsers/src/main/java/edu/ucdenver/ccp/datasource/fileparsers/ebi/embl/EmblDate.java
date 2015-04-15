/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.EMBL)
@Data
public class EmblDate {
	@RecordField
	private final Date date;
	@RecordField
	private final String createdOrLastUpdated;
	@RecordField
	private final Integer release;
	@RecordField
	private final Integer version;

	/**
	 * parses a date string formatted as DD-MON-YYYY
	 * 
	 * @param dateStr
	 * @return a date
	 * @throws ParseException 
	 */
	public static Date parseDate(String dateStr) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		return df.parse(dateStr);
	}

}
