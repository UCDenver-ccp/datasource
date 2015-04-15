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
package edu.ucdenver.ccp.fileparsers.ncbi.gene;

import lombok.Getter;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * This class represents data contained in the EntrezGene gene2accession file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Getter
@Record(dataSource = DataSource.EG, comment = "", license = License.NCBI, citation = "The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091", label = "gene2accession record")
public class EntrezGene2AccessionFileData extends SingleLineFileRecord {
	/*
	 * #Format: tax_id GeneID status RNA_nucleotide_accession.version RNA_nucleotide_gi
	 * protein_accession.version protein_gi genomic_nucleotide_accession.version
	 * genomic_nucleotide_gi start_position_on_the_genomic_accession
	 * end_position_on_the_genomic_accession orientation assembly (tab is used as a separator, pound
	 * sign - start of a comment)
	 */

	/*
	 * #Format: tax_id GeneID status RNA_nucleotide_accession.version RNA_nucleotide_gi
	 * protein_accession.version protein_gi genomic_nucleotide_accession.version
	 * genomic_nucleotide_gi start_position_on_the_genomic_accession
	 * end_position_on_the_genomic_accession orientation assembly mature_peptide_accession.version
	 * mature_peptide_gi Symbol (tab is used as a separator, pound sign - start of a comment)
	 */

	@RecordField(comment = "the unique identifier provided by NCBI Taxonomy for the species or strain/isolate")
	private final NcbiTaxonomyID taxonID;

	@RecordField(comment = "the unique identifier for a gene")
	private final EntrezGeneID geneID;

	@RecordField(comment = "status of the RefSeq if a refseq, else '-' RefSeq values are: INFERRED, MODEL, NA, PREDICTED, PROVISIONAL, REVIEWED, SUPPRESSED, VALIDATED")
	private final String status;

	@RecordField(comment = "may be null (-) for some genomes")
	private final DataSourceIdentifier<?> RNA_nucleotide_accession_dot_version;

	@RecordField(comment = "the gi for an RNA nucleotide accession, '-' if not applicable")
	private final GiNumberID RNA_nucleotide_gi;

	@RecordField(comment = "will be null (-) for RNA-coding genes")
	private final DataSourceIdentifier<?> protein_accession_dot_version;

	@RecordField(comment = "the gi for a protein accession, '-' if not applicable")
	private final GiNumberID protein_gi;

	@RecordField(comment = "may be null (-)")
	private final DataSourceIdentifier<?> genomic_nucleotide_accession_dot_version;

	@RecordField(comment = "the gi for a genomic nucleotide accession, '-' if not applicable")
	private final GiNumberID genomic_nucleotide_gi;

	@RecordField(comment = "position of the gene feature on the genomic accession, '-' if not applicable position 0-based.  NOTE: this file does not report the position of each exon for positions on RefSeq contigs and chromosomes, use the seq_gene.md file in the desired build directory.  For example, for human at the time this was written: /am/ftp-genomes/H_sapiens/maps/mapview/BUILD.35.1 WARNING: positions in these files are one-based, not 0-based NOTE: if genes are merged after an annotation is released, there may be more than one location reported on a genomic sequence per GeneID, each resulting from the annotation before the merge.")
	private final Integer start_position_on_the_genomic_accession;

	@RecordField(comment = "position of the gene feature on the genomic accession, '-' if not applicable position 0-based NOTE: this file does not report the position of each exon for positions on RefSeq contigs and chromosomes, use the seq_gene.md file in the desired build directory.  For example, for human at the time this was written: /am/ftp-genomes/H_sapiens/maps/mapview/BUILD.35.1 WARNING: positions in these files are one-based, not 0-based NOTE: if genes are merged after an annotation is released, there may be more than one location reported on a genomic sequence per GeneID, each resulting from the annotation before the merge.")
	private final Integer end_position_on_the_genomic_accession;

	@RecordField(comment = "orientation of the gene feature on the genomic accession, '?' if not applicable")
	private final char orientation;

	@RecordField(comment = "the name of the assembly '-' if not applicable")
	private final String assembly;

	@RecordField(comment = "will be null (-) if absent")
	private final DataSourceIdentifier<?> mature_peptide_accession_dot_version;

	@RecordField(comment = "the gi for a mature peptide accession, '-' if not applicable")
	private final GiNumberID mature_peptide_gi;

	@RecordField(comment = "the default symbol for the gene")
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
	private EntrezGene2AccessionFileData(NcbiTaxonomyID taxonID, EntrezGeneID geneID, String status,
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
	public static EntrezGene2AccessionFileData parseGene2AccessionLine(Line line) {
		if (!line.getText().startsWith("#")) {
			String[] toks = line.getText().split("\\t");
			Logger logger = Logger.getLogger(EntrezGene2AccessionFileData.class);

			if (toks.length != 16) {
				logger.error("Unexpected number of tokens (" + toks.length + ") on line:"
						+ line.getText().replaceAll("\\t", " [TAB] "));
				return null;
			}

			NcbiTaxonomyID taxonID = new NcbiTaxonomyID(toks[0]);
			EntrezGeneID geneID = new EntrezGeneID(toks[1]);

			String status = toks[2];
			if (status.equals("-")) {
				status = null;
			}

			DataSourceIdentifier<?> RNA_nucleotide_accession_dot_version = null;
			if (!toks[3].equals("-") && status != null) {
				RNA_nucleotide_accession_dot_version = NucleotideAccessionResolver.resolveNucleotideAccession(toks[3]);
			}

			String intStr = toks[4];
			GiNumberID RNA_nucleotide_gi = null;
			if (!intStr.equals("-")) {
				RNA_nucleotide_gi = new GiNumberID(intStr);
			}

			DataSourceIdentifier<?> protein_accession_dot_version = null;
			if (!toks[5].equals("-") && status != null) {
				protein_accession_dot_version = ProteinAccessionResolver.resolveProteinAccession(toks[5]);
			}

			intStr = toks[6];
			GiNumberID protein_gi = null;
			if (!intStr.equals("-")) {
				protein_gi = new GiNumberID(intStr);
			}

			DataSourceIdentifier<?> genomic_nucleotide_accession_dot_version = null;
			if (!toks[7].equals("-") && status != null) {
				genomic_nucleotide_accession_dot_version = NucleotideAccessionResolver
						.resolveNucleotideAccession(toks[7]);
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
				mature_peptide_accession_dot_version = ProteinAccessionResolver.resolveProteinAccession(toks[13]);
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

			return new EntrezGene2AccessionFileData(taxonID, geneID, status, RNA_nucleotide_accession_dot_version,
					RNA_nucleotide_gi, protein_accession_dot_version, protein_gi,
					genomic_nucleotide_accession_dot_version, genomic_nucleotide_gi,
					start_position_on_the_genomic_accession, end_position_on_the_genomic_accession, orientation,
					assembly, mature_peptide_accession_dot_version, mature_peptide_gi, symbol, line.getByteOffset(),
					line.getLineNumber());
		}

		return null;
	}

}
