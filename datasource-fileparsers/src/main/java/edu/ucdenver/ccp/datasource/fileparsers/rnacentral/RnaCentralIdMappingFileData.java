package edu.ucdenver.ccp.datasource.fileparsers.rnacentral;

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
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DictyBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblTranscriptID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EuropeanNucleotideArchiveId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.FlyBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MiRBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PomBaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RfamId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RnaCentralId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TairID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.WormBaseID;
import lombok.Getter;
import lombok.ToString;

/**
 * Data representation of the contents of the RNACentral identifier mappings
 * file:
 * ftp://ftp.ebi.ac.uk/pub/databases/RNAcentral/current_release/id_mapping/id_mapping.tsv.gz
 * 
 */
@Record(ontClass = CcpExtensionOntology.RNACENTRAL_IDENTIFIER_MAPPING_RECORD, dataSource = DataSource.RNACENTRAL)
@ToString
@Getter
public class RnaCentralIdMappingFileData extends SingleLineFileRecord {

	// RNA Central ID
	// External ID
	// NCBI Taxonomy ID
	// RNA types
	// Gene names

	@RecordField(ontClass = CcpExtensionOntology.RNACENTRAL_IDENTIFIER_MAPPING_RECORD___RNACENTRAL_IDENTIFIER_FIELD_VALUE)
	private final RnaCentralId rnaCentralId;
	@RecordField(ontClass = CcpExtensionOntology.RNACENTRAL_IDENTIFIER_MAPPING_RECORD___EXTERNAL_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> externalIdentifier;
	@RecordField(ontClass = CcpExtensionOntology.RNACENTRAL_IDENTIFIER_MAPPING_RECORD___NCBI_TAXONOMY_IDENTIFIER_FIELD_VALUE)
	private final NcbiTaxonomyID ncbiTaxonomyId;
	@RecordField(ontClass = CcpExtensionOntology.RNACENTRAL_IDENTIFIER_MAPPING_RECORD___RNA_TYPE_FIELD_VALUE)
	private final String rnaType;
	@RecordField(ontClass = CcpExtensionOntology.RNACENTRAL_IDENTIFIER_MAPPING_RECORD___GENE_NAME_FIELD_VALUE)
	private final String geneName;

	public RnaCentralIdMappingFileData(RnaCentralId rnaCentralId, DataSourceIdentifier<?> externalIdentifier,
			NcbiTaxonomyID ncbiTaxonomyId, String rnaType, String geneName, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.rnaCentralId = rnaCentralId;
		this.externalIdentifier = externalIdentifier;
		this.ncbiTaxonomyId = ncbiTaxonomyId;
		this.rnaType = rnaType;
		this.geneName = geneName;
	}

	public static RnaCentralIdMappingFileData parseLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;
		RnaCentralId rnaCentralId = new RnaCentralId(toks[index++]);
		DataSourceIdentifier<?> externalId = resolveId(toks[index++], toks[index++]);
		NcbiTaxonomyID ncbiTaxonomyId = new NcbiTaxonomyID(toks[index++]);
		String rnaType = toks[index++];
		String geneName = toks[index++];

		return new RnaCentralIdMappingFileData(rnaCentralId, externalId, ncbiTaxonomyId, rnaType, geneName,
				line.getByteOffset(), line.getLineNumber());
	}

	private static DataSourceIdentifier<?> resolveId(String ds, String idStr) {
		try {
			switch (ds) {
			case "DICTYBASE":
				return new DictyBaseID(idStr);
			case "ENA":
				// DQ593091.1:1..31:ncRNA
				String id = (idStr.split(":")[0]).split("\\.")[0];
				return new EuropeanNucleotideArchiveId(id);
			case "ENSEMBL":
				if (idStr.startsWith("ENSG")) {
					return new EnsemblGeneID(idStr);
				} else if (idStr.startsWith("ENST")) {
					return new EnsemblTranscriptID(idStr);
				} else {
					return new UnknownDataSourceIdentifier(ds + "_" + idStr);
				}
			case "FLYBASE":
				return new FlyBaseID(idStr);
			case "GENCODE":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "GREENGENES":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "GTRNADB":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "HGNC":
				return new HgncID(idStr);
			case "LNCIPEDIA":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "LNCRNADB":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "MGI":
				return new MgiGeneID(idStr);
			case "MIRBASE":
				return new MiRBaseID(idStr);
			case "MODOMICS":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "NONCODE":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "PDB":
				return new PdbID(idStr);
			case "POMBASE":
				return new PomBaseId(idStr);
			case "RDP":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "REFSEQ":
				return new RefSeqID(idStr);
			case "RFAM":
				return new RfamId(idStr);
			case "RGD":
				return new RgdID(idStr);
			case "SGD":
				return new SgdID(idStr);
			case "SILVA":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "SNOPY":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "SRPDB":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "TAIR":
				return new TairID(idStr);
			case "TMRNA_WEB":
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			case "WORMBASE":
				return new WormBaseID(idStr);
			default:
				return new UnknownDataSourceIdentifier(ds + "_" + idStr);
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, ds, e.getMessage());
		}
	}

}
