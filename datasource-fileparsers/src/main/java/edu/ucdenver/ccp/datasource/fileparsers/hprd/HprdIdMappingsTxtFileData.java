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
package edu.ucdenver.ccp.datasource.fileparsers.hprd;

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

import java.util.List;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import lombok.Getter;

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
	private final NcbiGeneId entrezGeneID;
	@RecordField
	private final OmimID omimID;
	@RecordField
	private final List<UniProtID> swissProtIDs;
	@RecordField
	private final String mainName;

	public HprdIdMappingsTxtFileData(HprdID hprdID, String geneSymbol, DataSourceIdentifier<?> nucleotideAccession,
			DataSourceIdentifier<?> proteinAccession, NcbiGeneId entrezGeneID, OmimID omimID,
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
