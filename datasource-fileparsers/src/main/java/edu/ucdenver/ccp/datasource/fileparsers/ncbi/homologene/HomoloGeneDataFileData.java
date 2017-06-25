package edu.ucdenver.ccp.datasource.fileparsers.ncbi.homologene;

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
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HomologeneGroupID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;

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
	private final NcbiGeneId entrezGeneID;

	@RecordField(comment="Gene Symbol")
	private final String geneSymbol;

	@RecordField(comment="Protein gi")
	private final GiNumberID proteinGI;

	@RecordField(comment="Protein accession")
	private final RefSeqID proteinAccession;

	public HomoloGeneDataFileData(HomologeneGroupID homologeneGroupID, NcbiTaxonomyID taxonomyID,
			NcbiGeneId entrezGeneID, String geneSymbol, GiNumberID proteinGI, RefSeqID proteinAccession,
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

	public NcbiGeneId getEntrezGeneID() {
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
		NcbiGeneId entrezGeneID = new NcbiGeneId(toks[2]);
		String geneSymbol = new String(toks[3]);
		GiNumberID proteinGI = new GiNumberID(toks[4]);
		RefSeqID proteinAccession = new RefSeqID(toks[5]);

		return new HomoloGeneDataFileData(homologeneGroupID, taxonomyID, entrezGeneID, geneSymbol, proteinGI,
				proteinAccession, line.getByteOffset(), line.getLineNumber());

	}

}
