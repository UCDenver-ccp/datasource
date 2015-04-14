/**
 * Copyright (C) 2011 Center for Computational Pharmacology, University of Colorado School of Medicine
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
package edu.ucdenver.ccp.fileparsers.pro;

import lombok.Data;
import lombok.EqualsAndHashCode;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.obo.ProteinOntologyId;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.PR, comment = "The PRO mapping from PRO IDs to other ontologies", license = License.PIR, citation = "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3013777/?tool=pubmed", label = "id mapping record")
@Data
@EqualsAndHashCode(callSuper = false)
public class ProMappingRecord extends SingleLineFileRecord {

	@RecordField(comment = "PR id")
	private ProteinOntologyId proteinOntologyId;

	@RecordField(comment = "mapping type: is_a or  exact")
	private String mappingType;

	@RecordField(comment = "id in other ontology, one of HGNC, MGI, UniProtKB, UniProtKB_VAR, Reactome,PomBase, EcoCyc, possibly others")
	private DataSourceIdentifier<?> targetRecordId;

	/**
	 * Default constructor
	 * 
	 * @param recordID
	 *            pro ontology id
	 * @param targetRecordId
	 *            target id
	 * @param mappingType
	 *            mapping type
	 * @param byteOffset
	 *            file byte offset
	 * @param lineNumber
	 */
	public ProMappingRecord(ProteinOntologyId recordID, DataSourceIdentifier<?> targetRecordId, String mappingType,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.proteinOntologyId = recordID;
		this.targetRecordId = targetRecordId;
		this.mappingType = mappingType;
	}

}
