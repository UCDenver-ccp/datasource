/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.embl;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import lombok.Data;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.EMBL, ontClass = CcpExtensionOntology.EMBL_DATE_RECORD)
@Data
public class EmblDate {
	@RecordField(ontClass = CcpExtensionOntology.EMBL_DATE_RECORD___DATE_FIELD_VALUE)
	private final Date date;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_DATE_RECORD___CREATED_OR_LAST_UPDATED_FIELD_VALUE)
	private final String createdOrLastUpdated;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_DATE_RECORD___RELEASE_FIELD_VALUE)
	private final Integer release;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_DATE_RECORD___VERSION_FIELD_VALUE)
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
