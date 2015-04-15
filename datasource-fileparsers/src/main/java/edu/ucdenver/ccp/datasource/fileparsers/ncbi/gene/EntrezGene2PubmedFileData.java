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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * Representation of data from the EntrezGene gene2pubmed file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.EG,
		comment="This file can be considered as the logical equivalent of what is reported as Gene/PubMed Links visible in Gene's and PubMed's Links menus. Although gene2pubmed is re-calculated daily, some of the source documents (GeneRIFs, for example) are not updated that frequently, so timing depends on the update frequency of the data source.",
		license=License.NCBI,
		citation="The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091",
		label="gene2pubmed record")
public class EntrezGene2PubmedFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "PUBMED_TO_ENTREZGENE_RECORD_";
	private static final Logger logger = Logger.getLogger(EntrezGene2PubmedFileData.class);

	@RecordField(comment="the unique identifier provided by NCBI Taxonomy for the species or strain/isolate")
	private final NcbiTaxonomyID taxonomyID;

	@RecordField(comment="the unique identifier in PubMed for a citation")
	private final PubMedID pmid;

	@RecordField(comment="the unique identifier for a gene")
	private final EntrezGeneID entrezGeneID;

	public EntrezGene2PubmedFileData(NcbiTaxonomyID taxonomyID, EntrezGeneID entrezGeneID, 
			PubMedID pmid, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.taxonomyID = taxonomyID;
		this.pmid = pmid;
		this.entrezGeneID = entrezGeneID;
	}

	public PubMedID getPubmedID() {
		return pmid;
	}

	public EntrezGeneID getEntrezGeneID() {
		return entrezGeneID;
	}

	public NcbiTaxonomyID getTaxonomyID() {
		return taxonomyID;
	}

	public static EntrezGene2PubmedFileData parseGene2PubmedLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 3) {
			NcbiTaxonomyID taxonomyID = new NcbiTaxonomyID(toks[0]);
			EntrezGeneID entrezGeneID = new EntrezGeneID(toks[1]);
			PubMedID pmid = new PubMedID(toks[2]);
			return new EntrezGene2PubmedFileData(taxonomyID, entrezGeneID, pmid, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
