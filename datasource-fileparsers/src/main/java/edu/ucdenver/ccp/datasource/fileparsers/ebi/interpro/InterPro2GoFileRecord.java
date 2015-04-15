/**
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.interpro;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;

/**
 * Record representation from InterPro file interpro2go.
 * 
 * @author Yuriy Malenkiy
 *
 */
@Record(dataSource = DataSource.INTERPRO, label="interpro2go record")
public class InterPro2GoFileRecord extends SingleLineFileRecord {

	@RecordField
	private final InterProID interProId;
	@RecordField
	private final GeneOntologyID goId;
	
	/**
	 * @param recordID
	 * @param byteOffset
	 */
	public InterPro2GoFileRecord(InterProID interProId, GeneOntologyID goId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.interProId = interProId;
		this.goId = goId;
	}

	public InterProID getInterProId() {
		return interProId;
	}

	public GeneOntologyID getGoId() {
		return goId;
	}

}
