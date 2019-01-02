package edu.ucdenver.ccp.datasource.fileparsers.biomart;

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
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblProteinID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblTranscriptID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import lombok.Getter;
import lombok.ToString;

/**
 * Data representation id mappings exported from BioMart using the following
 * command:
 * 
 * <pre>
 * wget -O /kabob_data/raw/biomart/biomart-protein-identifier-mappings.txt 'http://www.ensembl.org/biomart/martservice?query=<?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE Query><Query  virtualSchemaName = "default" formatter =
 "TSV" header = "0" uniqueRows = "0" count = "" datasetConfigVersion = "0.6" >	
 * <Dataset name = "hsapiens_gene_ensembl" interface = "default" >		
 * <Attribute name = "ensembl_gene_id" /> 
 * <Attribute name = "ensembl_peptide_id" /> 
 * <Attribute name = "uniprotswissprot" /> 
 * <Attribute name = "uniprotsptrembl" /> </Dataset></Query>'
 * </pre>
 */
@Record(ontClass = CcpExtensionOntology.BIOMART_PROTEIN_IDENTIFIER_MAPPING_RECORD, dataSource = DataSource.BIOMART)
@ToString
@Getter
public class BioMartProteinIdMappingFileData extends SingleLineFileRecord {

	// columns:
	// ensembl gene id
	// ensembl peptide id
	// uniprot swissprot id
	// uniprot trembl id

	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___ENSEMBL_GENE_IDENTIFIER_FIELD_VALUE)
	private final EnsemblGeneID ensemblGeneId;
	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___ENSEMBL_PEPTIDE_IDENTIFIER_FIELD_VALUE)
	private final EnsemblProteinID ensemblProteinId;
	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___UNIPROT_SWISSPROT_IDENTIFIER_FIELD_VALUE)
	private final UniProtID uniprotSwissProtId;
	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___UNIPROT_TREMBL_IDENTIFIER_FIELD_VALUE)
	private final UniProtID uniprotTremblId;

	public BioMartProteinIdMappingFileData(EnsemblGeneID ensemblGeneId, EnsemblProteinID ensemblProteinId,
			UniProtID uniprotSwissProtId, UniProtID uniprotTremblId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.ensemblGeneId = ensemblGeneId;
		this.ensemblProteinId = ensemblProteinId;
		this.uniprotSwissProtId = uniprotSwissProtId;
		this.uniprotTremblId = uniprotTremblId;
	}

	public static BioMartProteinIdMappingFileData parseLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;
		String tmpStr;
		EnsemblGeneID ensemblGeneId = new EnsemblGeneID(toks[index++]);
		EnsemblProteinID ensemblProteinId = new EnsemblProteinID(toks[index++]);
		tmpStr = toks[index++];
		UniProtID uniprotSwissProtId = (tmpStr.trim().isEmpty()) ? null : new UniProtID(tmpStr);
		tmpStr = toks[index++];
		UniProtID uniprotTremblId = (tmpStr.trim().isEmpty()) ? null : new UniProtID(tmpStr);

		return new BioMartProteinIdMappingFileData(ensemblGeneId, ensemblProteinId, uniprotSwissProtId, uniprotTremblId,
				line.getByteOffset(), line.getLineNumber());
	}

}
