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
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;

/**
 * Representation of data from the EntrezGene mim2gene file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.NCBI_GENE, comment = "", license = License.NCBI, citation = "The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091", label = "mim2gene record")
@Data
public class NcbiGeneMim2GeneFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(NcbiGeneMim2GeneFileData.class);

	public static final String GENE_ASSOCIATION_TYPE = "gene";
	public static final String PHENOTYPE_ASSOCIATION_TYPE = "phenotype";

	@RecordField(comment = "a MIM number associated with a GeneID")
	private final OmimID mimNumber;

	@RecordField(comment = "the current unique identifier for a gene")
	private final NcbiGeneId entrezGeneID;

	@RecordField(comment = "type of relationship between the MIM number and the GeneID.  current values are: 'gene' the MIM number associated with a Gene, or a GeneID that is assigned to a record where the molecular basis of the disease is not known, 'phenotype' the MIM number associated with a disease that is associate with a gene")
	private final String associationType;

	@RecordField(comment = "This value is provided only when there is a report of a relationship between a MIM number that is a phenotype, and a GeneID. The current expected values are GeneMap (from OMIM), GeneReviews, and NCBI.")
	private final Set<String> sources;

	@RecordField(comment = "The accession assigned by MedGen to this phenotype.  If the accession starts with a C followed by integers, the identifier is a concept ID (CUI) from UMLS. http://www.nlm.nih.gov/research/umls/ If it starts with a CN, no CUI in UMLS was identified, and NCBI created a placeholder.")
	private final MedGenId medGenId;

	@RecordField
	private final String comment;

	public NcbiGeneMim2GeneFileData(OmimID mimNumber, NcbiGeneId entrezGeneID, String associationType,
			Set<String> sources, MedGenId medGenId, String comment, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.mimNumber = mimNumber;
		this.entrezGeneID = entrezGeneID;
		this.associationType = associationType;
		this.sources = sources;
		this.medGenId = medGenId;
		this.comment = comment;
	}

	public static NcbiGeneMim2GeneFileData parseMim2GeneLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		if (toks.length == 6) {
			OmimID mimNumber = new OmimID(toks[0]);
			NcbiGeneId entrezGeneID = (toks[1].equals("-")) ? null : new NcbiGeneId(toks[1]);
			String associationType = toks[2];
			Set<String> sources = new HashSet<String>();
			if (!toks[3].equals("-")) {
				String[] sourceToks = toks[3].split(";");
				for (String sourceTok : sourceToks) {
					sources.add(sourceTok.trim());
				}
			}
			MedGenId medGenId = (toks[4].equals("-")) ? null : new MedGenId(toks[4].trim());
			String comment = (toks[5].equals("-")) ? null : toks[5].trim();
			return new NcbiGeneMim2GeneFileData(mimNumber, entrezGeneID, associationType, sources, medGenId, comment,
					line.getByteOffset(), line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line.toString());
		return null;
	}

}
