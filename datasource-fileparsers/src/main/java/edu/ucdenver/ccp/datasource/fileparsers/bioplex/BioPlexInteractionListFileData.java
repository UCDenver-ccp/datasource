package edu.ucdenver.ccp.datasource.fileparsers.bioplex;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2018 Regents of the University of Colorado
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

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;
import lombok.Getter;
import lombok.ToString;

/**
 * Data representation of the contents of the BioPlex Interaction List file.
 * http://bioplex.hms.harvard.edu/data/BioPlex_interactionList_v4a.tsv
 * 
 */
@Record(ontClass = CcpExtensionOntology.BIOPLEX_RECORD, dataSource = DataSource.BIOPLEX)
@ToString
@Getter
public class BioPlexInteractionListFileData extends SingleLineFileRecord {

	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___GENE_IDENTIFIER_A_FIELD_VALUE)
	private final DataSourceIdentifier<?> geneIdA;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___GENE_IDENTIFIER_B_FIELD_VALUE)
	private final DataSourceIdentifier<?> geneIdB;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___UNIPROT_IDENTIFIER_A_FIELD_VALUE)
	private final DataSourceIdentifier<?> uniprotIdA;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___UNIPROT_IDENTIFIER_B_FIELD_VALUE)
	private final DataSourceIdentifier<?> uniprotIdB;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___SYMBOL_A_FIELD_VALUE)
	private final String symbolA;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___SYMBOL_B_FIELD_VALUE)
	private final String symbolB;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___PWRONG_FIELD_VALUE)
	private final float pWrong;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___PNO_INTERACTION_FIELD_VALUE)
	private final float pNoInteraction;
	@RecordField(ontClass = CcpExtensionOntology.BIOPLEX_RECORD___PINTERACTION_FIELD_VALUE)
	private final float pInteraction;

	public BioPlexInteractionListFileData(DataSourceIdentifier<?> geneIdA, DataSourceIdentifier<?> geneIdB,
			DataSourceIdentifier<?> uniprotIdA, DataSourceIdentifier<?> uniprotIdB, String symbolA, String symbolB,
			float pWrong, float pNoInteraction, float pInteraction, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.geneIdA = geneIdA;
		this.geneIdB = geneIdB;
		this.uniprotIdA = uniprotIdA;
		this.uniprotIdB = uniprotIdB;
		this.symbolA = symbolA;
		this.symbolB = symbolB;
		this.pWrong = pWrong;
		this.pNoInteraction = pNoInteraction;
		this.pInteraction = pInteraction;
	}

	public static BioPlexInteractionListFileData parseLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;
		DataSourceIdentifier<?> geneIdA = resolveId(DataSource.NCBI_GENE, toks[index++]);
		DataSourceIdentifier<?> geneIdB = resolveId(DataSource.NCBI_GENE, toks[index++]);
		DataSourceIdentifier<?> uniprotIdA = resolveId(DataSource.UNIPROT, toks[index++]);
		DataSourceIdentifier<?> uniprotIdB = resolveId(DataSource.UNIPROT, toks[index++]);
		String symbolA = toks[index++];
		String symbolB = toks[index++];
		float pWrong = Float.parseFloat(toks[index++]);
		float pNoInteraction = Float.parseFloat(toks[index++]);
		float pInteraction = Float.parseFloat(toks[index++]);

		return new BioPlexInteractionListFileData(geneIdA, geneIdB, uniprotIdA, uniprotIdB, symbolA, symbolB, pWrong,
				pNoInteraction, pInteraction, line.getByteOffset(), line.getLineNumber());
	}

	private static DataSourceIdentifier<?> resolveId(DataSource ds, String idStr) {
		try {
			switch (ds) {
			case NCBI_GENE:
				return new NcbiGeneId(idStr);
			case UNIPROT:
				if (idStr.contains("-")) {
					return new UniProtIsoformID(idStr);
				} else {
					return new UniProtID(idStr);
				}
			default:
				throw new IllegalStateException("Cannot create identifier. Unhandled data source: " + ds.name());
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, ds.name(), e.getMessage());
		}
	}

}
