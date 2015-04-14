/*
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
package edu.ucdenver.ccp.fileparsers.hprd;

import java.util.List;

import lombok.Getter;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.hprd.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

/**
 * Representation of data from HPRD HPRD_ID_MAPPINGS.txt file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Getter
@Record(dataSource = DataSource.HPRD, label="idmappings record")
public class HprdIdMappingsTxtFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(HprdIdMappingsTxtFileData.class);

	@RecordField
	private final HprdID hprdID;
	@RecordField
	private final String geneSymbol;
	@RecordField
	private final DataSourceIdentifier<?> nucleotideAccession;
	@RecordField
	private final DataSourceIdentifier<?> proteinAccession;
	@RecordField
	private final EntrezGeneID entrezGeneID;
	@RecordField
	private final OmimID omimID;
	@RecordField
	private final List<UniProtID> swissProtIDs;
	@RecordField
	private final String mainName;

	public HprdIdMappingsTxtFileData(HprdID hprdID, String geneSymbol, DataSourceIdentifier<?> nucleotideAccession,
			DataSourceIdentifier<?> proteinAccession, EntrezGeneID entrezGeneID, OmimID omimID,
			List<UniProtID> swissProtID, String mainName, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.hprdID = hprdID;
		this.geneSymbol = geneSymbol;
		this.nucleotideAccession = nucleotideAccession;
		this.proteinAccession = proteinAccession;
		this.entrezGeneID = entrezGeneID;
		this.omimID = omimID;
		this.swissProtIDs = swissProtID;
		this.mainName = mainName;
	}
}
