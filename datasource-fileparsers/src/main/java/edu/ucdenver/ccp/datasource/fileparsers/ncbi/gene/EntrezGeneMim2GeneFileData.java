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

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.MedGenId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

/**
 * Representation of data from the EntrezGene mim2gene file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.EG, comment = "", license = License.NCBI, citation = "The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091", label = "mim2gene record")
@Data
public class EntrezGeneMim2GeneFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(EntrezGeneMim2GeneFileData.class);

	public static final String GENE_ASSOCIATION_TYPE = "gene";
	public static final String PHENOTYPE_ASSOCIATION_TYPE = "phenotype";

	@RecordField(comment = "a MIM number associated with a GeneID")
	private final OmimID mimNumber;

	@RecordField(comment = "the current unique identifier for a gene")
	private final EntrezGeneID entrezGeneID;

	@RecordField(comment = "type of relationship between the MIM number and the GeneID.  current values are: 'gene' the MIM number associated with a Gene, or a GeneID that is assigned to a record where the molecular basis of the disease is not known, 'phenotype' the MIM number associated with a disease that is associate with a gene")
	private final String associationType;

	@RecordField(comment = "This value is provided only when there is a report of a relationship between a MIM number that is a phenotype, and a GeneID. The current expected values are GeneMap (from OMIM), GeneReviews, and NCBI.")
	private final Set<String> sources;

	@RecordField(comment = "The accession assigned by MedGen to this phenotype.  If the accession starts with a C followed by integers, the identifier is a concept ID (CUI) from UMLS. http://www.nlm.nih.gov/research/umls/ If it starts with a CN, no CUI in UMLS was identified, and NCBI created a placeholder.")
	private final MedGenId medGenId;

	public EntrezGeneMim2GeneFileData(OmimID mimNumber, EntrezGeneID entrezGeneID, String associationType,
			Set<String> sources, MedGenId medGenId, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mimNumber = mimNumber;
		this.entrezGeneID = entrezGeneID;
		this.associationType = associationType;
		this.sources = sources;
		this.medGenId = medGenId;
	}

	public static EntrezGeneMim2GeneFileData parseMim2GeneLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 5) {
			OmimID mimNumber = new OmimID(toks[0]);
			EntrezGeneID entrezGeneID = (toks[1].equals("-")) ? null : new EntrezGeneID(toks[1]);
			String associationType = toks[2];
			Set<String> sources = new HashSet<String>();
			if (!toks[3].equals("-")) {
				String[] sourceToks = toks[3].split(";");
				for (String sourceTok : sourceToks) {
					sources.add(sourceTok.trim());
				}
			}
			MedGenId medGenId = (toks[4].equals("-")) ? null : new MedGenId(toks[4].trim());
			return new EntrezGeneMim2GeneFileData(mimNumber, entrezGeneID, associationType, sources, medGenId,
					line.getByteOffset(), line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
