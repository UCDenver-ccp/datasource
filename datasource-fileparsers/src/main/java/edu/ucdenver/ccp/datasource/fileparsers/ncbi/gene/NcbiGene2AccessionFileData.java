package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

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

import lombok.Getter;

/**
 * This class represents data contained in the EntrezGene gene2accession file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Getter
@Record(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD, dataSource = DataSource.NCBI_GENE, comment = "", license = License.NCBI, citation = "The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091", label = "gene2accession record")
public class NcbiGene2AccessionFileData extends SingleLineFileRecord {
	/*
	 * #Format: tax_id GeneID status RNA_nucleotide_accession.version
	 * RNA_nucleotide_gi protein_accession.version protein_gi
	 * genomic_nucleotide_accession.version genomic_nucleotide_gi
	 * start_position_on_the_genomic_accession
	 * end_position_on_the_genomic_accession orientation assembly (tab is used
	 * as a separator, pound sign - start of a comment)
	 */

	/*
	 * #Format: tax_id GeneID status RNA_nucleotide_accession.version
	 * RNA_nucleotide_gi protein_accession.version protein_gi
	 * genomic_nucleotide_accession.version genomic_nucleotide_gi
	 * start_position_on_the_genomic_accession
	 * end_position_on_the_genomic_accession orientation assembly
	 * mature_peptide_accession.version mature_peptide_gi Symbol (tab is used as
	 * a separator, pound sign - start of a comment)
	 */

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___TAXON_IDENTIFIER_FIELD_VALUE)
	private final NcbiTaxonomyID taxonID;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId geneID;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___STATUS_FIELD_VALUE)
	private final String status;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___RNA_NUCLEOTIDE_ACCESSION_DOT_VERSION_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> RNA_nucleotide_accession_dot_version;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___RNA_NUCLEOTIDE_GENEINFO_IDENTIFIER_FIELD_VALUE)
	private final GiNumberID RNA_nucleotide_gi;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___PROTEIN_ACCESSION_DOT_VERSION_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> protein_accession_dot_version;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___PROTEIN_GENEINFO_IDENTIFIER_FIELD_VALUE)
	private final GiNumberID protein_gi;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___GENOMIC_NUCLEOTIDE_ACCESSION_DOT_VERSION_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> genomic_nucleotide_accession_dot_version;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___GENOMIC_NUCLEOTIDE_GENEINFO_IDENTIFIER_FIELD_VALUE)
	private final GiNumberID genomic_nucleotide_gi;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___START_POSITION_ON_THE_GENOMIC_ACCESSION_FIELD_VALUE)
	private final Integer start_position_on_the_genomic_accession;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___END_POSITION_ON_THE_GENOMIC_ACCESSION_FIELD_VALUE)
	private final Integer end_position_on_the_genomic_accession;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___ORIENTATION_FIELD_VALUE)
	private final char orientation;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___ASSEMBLY_FIELD_VALUE)
	private final String assembly;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___MATURE_PEPTIDE_ACCESSION_DOT_VERSION_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> mature_peptide_accession_dot_version;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___MATURE_PEPTIDE_GENEINFO_IDENTIFIER_FIELD_VALUE)
	private final GiNumberID mature_peptide_gi;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_2_ACCESSION_RECORD___SYMBOL_FIELD_VALUE)
	private final String symbol;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param taxonID
	 * @param geneID
	 * @param status
	 * @param rNA_nucleotide_accession_dot_version
	 * @param rNA_nucleotide_gi
	 * @param protein_accession_dot_version
	 * @param protein_gi
	 * @param genomic_nucleotide_accession_dot_version
	 * @param genomic_nucleotide_gi
	 * @param start_position_on_the_genomic_accession
	 * @param end_position_on_the_genomic_accession
	 * @param orientation
	 * @param assembly
	 * @param mature_peptide_accession_dot_version
	 * @param mature_peptide_gi
	 * @param symbol
	 */
	private NcbiGene2AccessionFileData(NcbiTaxonomyID taxonID, NcbiGeneId geneID, String status,
			DataSourceIdentifier<?> rNA_nucleotide_accession_dot_version, GiNumberID rNA_nucleotide_gi,
			DataSourceIdentifier<?> protein_accession_dot_version, GiNumberID protein_gi,
			DataSourceIdentifier<?> genomic_nucleotide_accession_dot_version, GiNumberID genomic_nucleotide_gi,
			Integer start_position_on_the_genomic_accession, Integer end_position_on_the_genomic_accession,
			char orientation, String assembly, DataSourceIdentifier<?> mature_peptide_accession_dot_version,
			GiNumberID mature_peptide_gi, String symbol, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.taxonID = taxonID;
		this.geneID = geneID;
		this.status = status;
		RNA_nucleotide_accession_dot_version = rNA_nucleotide_accession_dot_version;
		RNA_nucleotide_gi = rNA_nucleotide_gi;
		this.protein_accession_dot_version = protein_accession_dot_version;
		this.protein_gi = protein_gi;
		this.genomic_nucleotide_accession_dot_version = genomic_nucleotide_accession_dot_version;
		this.genomic_nucleotide_gi = genomic_nucleotide_gi;
		this.start_position_on_the_genomic_accession = start_position_on_the_genomic_accession;
		this.end_position_on_the_genomic_accession = end_position_on_the_genomic_accession;
		this.orientation = orientation;
		this.assembly = assembly;
		this.mature_peptide_accession_dot_version = mature_peptide_accession_dot_version;
		this.mature_peptide_gi = mature_peptide_gi;
		this.symbol = symbol;
	}

	/**
	 * Parse a line from the EntrezGene gene2accession file
	 * 
	 * @param line
	 * @return
	 */
	public static NcbiGene2AccessionFileData parseGene2AccessionLine(Line line) {
		if (!line.getText().startsWith("#")) {
			String[] toks = line.getText().split("\\t");
			Logger logger = Logger.getLogger(NcbiGene2AccessionFileData.class);

			if (toks.length != 16) {
				logger.error("Unexpected number of tokens (" + toks.length + ") on line:"
						+ line.getText().replaceAll("\\t", " [TAB] "));
				return null;
			}

			NcbiTaxonomyID taxonID = new NcbiTaxonomyID(toks[0]);
			NcbiGeneId geneID = new NcbiGeneId(toks[1]);

			String status = toks[2];
			if (status.equals("-")) {
				status = null;
			}

			DataSourceIdentifier<?> RNA_nucleotide_accession_dot_version = null;
			if (!toks[3].equals("-") && status != null) {
				RNA_nucleotide_accession_dot_version = NucleotideAccessionResolver.resolveNucleotideAccession(toks[3],
						toks[3]);
			}

			String intStr = toks[4];
			GiNumberID RNA_nucleotide_gi = null;
			if (!intStr.equals("-")) {
				RNA_nucleotide_gi = new GiNumberID(intStr);
			}

			DataSourceIdentifier<?> protein_accession_dot_version = null;
			if (!toks[5].equals("-") && status != null) {
				protein_accession_dot_version = ProteinAccessionResolver.resolveProteinAccession(toks[5], toks[5]);
			}

			intStr = toks[6];
			GiNumberID protein_gi = null;
			if (!intStr.equals("-")) {
				protein_gi = new GiNumberID(intStr);
			}

			DataSourceIdentifier<?> genomic_nucleotide_accession_dot_version = null;
			if (!toks[7].equals("-") && status != null) {
				genomic_nucleotide_accession_dot_version = NucleotideAccessionResolver.resolveNucleotideAccession(
						toks[7], toks[7]);
			}

			intStr = toks[8];
			GiNumberID genomic_nucleotide_gi = null;
			if (!intStr.equals("-")) {
				genomic_nucleotide_gi = new GiNumberID(intStr);
			}

			intStr = toks[9];
			Integer start_position_on_the_genomic_accession;
			if (intStr.equals("-")) {
				start_position_on_the_genomic_accession = null;
			} else {
				start_position_on_the_genomic_accession = new Integer(intStr);
			}

			intStr = toks[10];
			Integer end_position_on_the_genomic_accession;
			if (intStr.equals("-")) {
				end_position_on_the_genomic_accession = null;
			} else {
				end_position_on_the_genomic_accession = new Integer(intStr);
			}

			if (toks[11].trim().length() > 1) {
				logger.error("Expected a single character for the orientation on line: " + line);
				return null;
			}
			char orientation = toks[11].trim().charAt(0);

			String assembly = toks[12];
			if (assembly.equals("-")) {
				assembly = null;
			}

			DataSourceIdentifier<?> mature_peptide_accession_dot_version = null;
			if (!toks[13].equals("-")) {
				mature_peptide_accession_dot_version = ProteinAccessionResolver.resolveProteinAccession(toks[13],
						toks[13]);
			}

			intStr = toks[14];
			GiNumberID mature_peptide_gi = null;
			if (!intStr.equals("-")) {
				mature_peptide_gi = new GiNumberID(intStr);
			}

			String symbol = toks[15];
			if (symbol.equals("-")) {
				symbol = null;
			}

			return new NcbiGene2AccessionFileData(taxonID, geneID, status, RNA_nucleotide_accession_dot_version,
					RNA_nucleotide_gi, protein_accession_dot_version, protein_gi,
					genomic_nucleotide_accession_dot_version, genomic_nucleotide_gi,
					start_position_on_the_genomic_accession, end_position_on_the_genomic_accession, orientation,
					assembly, mature_peptide_accession_dot_version, mature_peptide_gi, symbol, line.getByteOffset(),
					line.getLineNumber());
		}

		return null;
	}

}
