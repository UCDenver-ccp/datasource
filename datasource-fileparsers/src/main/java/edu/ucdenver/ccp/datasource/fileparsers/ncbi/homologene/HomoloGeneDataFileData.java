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
package edu.ucdenver.ccp.fileparsers.ncbi.homologene;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.homologene.HomologeneGroupID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * This class represents data contained in the NCBI HomoloGene homologene.data file which can be
 * found here: ftp://ftp.ncbi.nih.gov/pub/HomoloGene/current/
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.HOMOLOGENE,
	comment="HomoloGene is a resource for exploring putative homology relationships among genes.",
	license=License.NCBI,
	citation="The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 21, UniGene: A Unified View of the Transcriptome. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091",
	label="homologene record")
public class HomoloGeneDataFileData extends SingleLineFileRecord {

	/**
	 * <pre>
	 * homologene.data is a tab delimited file containing the following columns:
	 * 
	 * 1) HID (HomoloGene group id)
	 * 2) Taxonomy ID
	 * 3) Gene ID
	 * 4) Gene Symbol
	 * 5) Protein gi
	 * 6) Protein accession
	 * 
	 * </pre>
	 */
	private static Logger logger = Logger.getLogger(HomoloGeneDataFileData.class);

	@RecordField(comment="HID (HomoloGene group id)")
	private final HomologeneGroupID homologeneGroupID;

	@RecordField(comment="Taxonomy ID")
	private final NcbiTaxonomyID taxonomyID;

	@RecordField(comment="Gene ID")
	private final EntrezGeneID entrezGeneID;

	@RecordField(comment="Gene Symbol")
	private final String geneSymbol;

	@RecordField(comment="Protein gi")
	private final GiNumberID proteinGI;

	@RecordField(comment="Protein accession")
	private final RefSeqID proteinAccession;

	public HomoloGeneDataFileData(HomologeneGroupID homologeneGroupID, NcbiTaxonomyID taxonomyID,
			EntrezGeneID entrezGeneID, String geneSymbol, GiNumberID proteinGI, RefSeqID proteinAccession,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.homologeneGroupID = homologeneGroupID;
		this.taxonomyID = taxonomyID;
		this.entrezGeneID = entrezGeneID;
		this.geneSymbol = geneSymbol;
		this.proteinGI = proteinGI;
		this.proteinAccession = proteinAccession;
	}

	public HomologeneGroupID getHomologeneGroupID() {
		return homologeneGroupID;
	}

	public NcbiTaxonomyID getTaxonomyID() {
		return taxonomyID;
	}

	public EntrezGeneID getEntrezGeneID() {
		return entrezGeneID;
	}

	public String getGeneSymbol() {
		return geneSymbol;
	}

	public GiNumberID getProteinGI() {
		return proteinGI;
	}

	public RefSeqID getProteinAccession() {
		return proteinAccession;
	}

	/**
	 * Parse a line from the EntrezGene gene_info file
	 * 
	 * @param line
	 * @return
	 */
	public static HomoloGeneDataFileData parseHomologeneDataLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length != 6) {
			logger.error("Unexpected number of tokens (" + toks.length + ") on line:"
					+ line.getText().replaceAll("\\t", " [TAB] "));
		}
		HomologeneGroupID homologeneGroupID = new HomologeneGroupID(toks[0]);
		NcbiTaxonomyID taxonomyID = new NcbiTaxonomyID(toks[1]);
		EntrezGeneID entrezGeneID = new EntrezGeneID(toks[2]);
		String geneSymbol = new String(toks[3]);
		GiNumberID proteinGI = new GiNumberID(toks[4]);
		RefSeqID proteinAccession = new RefSeqID(toks[5]);

		return new HomoloGeneDataFileData(homologeneGroupID, taxonomyID, entrezGeneID, geneSymbol, proteinGI,
				proteinAccession, line.getByteOffset(), line.getLineNumber());

	}

}
