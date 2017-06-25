package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * Representation of data from the EntrezGene gene2pubmed file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(ontClass = CcpExtensionOntology.NCBI_GENE_2_PUBMED_RECORD,
		dataSource = DataSource.NCBI_GENE,
		comment="This file can be considered as the logical equivalent of what is reported as Gene/PubMed Links visible in Gene's and PubMed's Links menus. Although gene2pubmed is re-calculated daily, some of the source documents (GeneRIFs, for example) are not updated that frequently, so timing depends on the update frequency of the data source.",
		license=License.NCBI,
		citation="The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091",
		label="gene2pubmed record")
public class NcbiGene2PubmedFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "PUBMED_TO_ENTREZGENE_RECORD_";
	private static final Logger logger = Logger.getLogger(NcbiGene2PubmedFileData.class);

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_PUBMED_RECORD___TAXONOMY_IDENTIFIER_FIELD_VALUE)
	private final NcbiTaxonomyID taxonomyID;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_PUBMED_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
	private final PubMedID pmid;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_PUBMED_RECORD___ENTREZ_GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId entrezGeneID;

	public NcbiGene2PubmedFileData(NcbiTaxonomyID taxonomyID, NcbiGeneId entrezGeneID, 
			PubMedID pmid, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.taxonomyID = taxonomyID;
		this.pmid = pmid;
		this.entrezGeneID = entrezGeneID;
	}

	public PubMedID getPubmedID() {
		return pmid;
	}

	public NcbiGeneId getEntrezGeneID() {
		return entrezGeneID;
	}

	public NcbiTaxonomyID getTaxonomyID() {
		return taxonomyID;
	}

	public static NcbiGene2PubmedFileData parseGene2PubmedLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 3) {
			NcbiTaxonomyID taxonomyID = new NcbiTaxonomyID(toks[0]);
			NcbiGeneId entrezGeneID = new NcbiGeneId(toks[1]);
			PubMedID pmid = new PubMedID(toks[2]);
			return new NcbiGene2PubmedFileData(taxonomyID, entrezGeneID, pmid, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
