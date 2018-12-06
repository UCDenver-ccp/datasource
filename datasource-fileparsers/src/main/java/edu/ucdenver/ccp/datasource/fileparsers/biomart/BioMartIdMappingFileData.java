package edu.ucdenver.ccp.datasource.fileparsers.biomart;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
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
 * wget -O ensembl-mappings.txt 'http://www.ensembl.org/biomart/martservice?query=
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE Query><Query  virtualSchemaName = "default" formatter =
 "TSV" header = "0" uniqueRows = "0" count = "" datasetConfigVersion = "0.6" >	
 * <Dataset name = "hsapiens_gene_ensembl" interface = "default" >		
 *    <Attribute name = "ensembl_gene_id" />		
 *    <Attribute name = "ensembl_transcript_id" />		
 *    <Attribute name = "entrezgene" /> 
 *    <Attribute name = "hgnc_id" />		
 *    <Attribute name = "uniprotswissprot" />	
 * </Dataset></Query>'
 * </pre>
 */
@Record(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD, dataSource = DataSource.BIOMART)
@ToString
@Getter
public class BioMartIdMappingFileData extends SingleLineFileRecord {

	// columns:
	// ensembl gene id
	// ensembl transcript id
	// ncbi gene id
	// hgnc gene id
	// uniprot swissprot id

	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___ENSEMBL_GENE_IDENTIFIER_FIELD_VALUE)
	private final EnsemblGeneID ensemblGeneId;
	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___ENSEMBL_TRANSCRIPT_IDENTIFIER_FIELD_VALUE)
	private final EnsemblTranscriptID ensemblTranscriptId;
	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___NCBI_GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId ncbiGeneId;
	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___HGNC_GENE_IDENTIFIER_FIELD_VALUE)
	private final HgncID hgncId;
	@RecordField(ontClass = CcpExtensionOntology.BIOMART_IDENTIFIER_MAPPING_RECORD___UNIPROT_IDENTIFIER_FIELD_VALUE)
	private final UniProtID uniprotId;

	public BioMartIdMappingFileData(EnsemblGeneID ensemblGeneId, EnsemblTranscriptID ensemblTranscriptId,
			NcbiGeneId ncbiGeneId, HgncID hgncId, UniProtID uniprotId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.ensemblGeneId = ensemblGeneId;
		this.ensemblTranscriptId = ensemblTranscriptId;
		this.ncbiGeneId = ncbiGeneId;
		this.hgncId = hgncId;
		this.uniprotId = uniprotId;
	}

	public static BioMartIdMappingFileData parseLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;
		String tmpStr;
		EnsemblGeneID ensemblGeneId = new EnsemblGeneID(toks[index++]);
		EnsemblTranscriptID ensemblTranscriptId = new EnsemblTranscriptID(toks[index++]);
		tmpStr = toks[index++];
		NcbiGeneId ncbiGeneId = (tmpStr.trim().isEmpty()) ? null : new NcbiGeneId(tmpStr);
		tmpStr = toks[index++];
		HgncID hgncId = (tmpStr.trim().isEmpty()) ? null : new HgncID(tmpStr);
		tmpStr = toks[index++];
		UniProtID uniprotId = (tmpStr.trim().isEmpty()) ? null : new UniProtID(tmpStr);

		return new BioMartIdMappingFileData(ensemblGeneId, ensemblTranscriptId, ncbiGeneId, hgncId, uniprotId,
				line.getByteOffset(), line.getLineNumber());
	}

}
