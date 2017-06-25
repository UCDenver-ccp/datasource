package edu.ucdenver.ccp.datasource.fileparsers.ncbi.refseq;

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

import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;

/**
 * Representation of data from the RefSeq RefSeq-release#.catalog.gz file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD, dataSource = DataSource.REFSEQ,
		comment="http://www.ncbi.nih.gov/RefSeq/, ftp://ftp.ncbi.nlm.nih.gov/refseq/release/release-catalog/README",
		license=License.NCBI,
		citation="NCBI Reference Sequences (RefSeq): current status, new features and genome annotation policy. Pruitt KD, Tatusova T, Brown GR, Maglott DR. Nucleic Acids Res. 2012 Jan;40(Database issue):D130-5., The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 18, The Reference Sequence (RefSeq) Project. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091 ",
		label="rel. catalog record")
public class RefSeqReleaseCatalogFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(RefSeqReleaseCatalogFileData.class);
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___TAXONOMY_IDENTIFIER_FIELD_VALUE)
	private final NcbiTaxonomyID taxId;
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___SPECIES_NAME_FIELD_VALUE)
	private final String speciesName;
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___REFSEQ_IDENTIFIER_FIELD_VALUE, isKeyField=true)
	private final RefSeqID refseqId;

	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___GENEINFO_IDENTIFIER_FIELD_VALUE)
	private final GiNumberID gi;
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___RELEASE_DIRECTORY_INCLUSION_FIELD_VALUE)
	private final String releaseDirectoryInclusion;
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___REFSEQ_STATUS_FIELD_VALUE)
	private final String refseqStatus;
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___LENGTH_FIELD_VALUE)
	private final int length;
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___MOLECULE_TYPE_FIELD_VALUE)
	private final String moleculeType;
	@RecordField(ontClass = CcpExtensionOntology.REFSEQ_CATALOG_RECORD___IS_PREDICTED_FIELD_VALUE)
	private final boolean isPredicted;

	public RefSeqReleaseCatalogFileData(NcbiTaxonomyID taxId, String speciesName, RefSeqID refseqId, GiNumberID gi,
			String releaseDirectoryInclusion, String refseqStatus, int length, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.taxId = taxId;
		this.speciesName = speciesName;
		this.refseqId = refseqId;
		this.gi = gi;
		this.releaseDirectoryInclusion = releaseDirectoryInclusion;
		this.refseqStatus = refseqStatus;
		this.length = length;
		this.moleculeType = getMoleculeType(refseqId);
		this.isPredicted = getIsPredicted(refseqId);
	}

	/**
	 * @param refseqId2
	 * @return
	 */
	private boolean getIsPredicted(RefSeqID refseqId) {
		Set<String> predictedPrefixes = CollectionsUtil.createSet("XM", "XR", "XP", "ZP");
		String prefix = refseqId.getId().substring(0, 2);
		return predictedPrefixes.contains(prefix);
	}

	/**
	 * @param refseqId2
	 * @return
	 */
	private String getMoleculeType(RefSeqID refseqId) {
		Set<String> genomicPrefixes = CollectionsUtil.createSet("AC", "NC", "NG", "NT", "NW", "NS", "NZ");
		Set<String> rnaPrefixes = CollectionsUtil.createSet("NR", "XR");
		Set<String> mrnaPrefixes = CollectionsUtil.createSet("NM", "XM");
		Set<String> proteinPrefixes = CollectionsUtil.createSet("AP", "NP", "YP", "XP", "ZP", "WP");
		String prefix = refseqId.getId().substring(0, 2);
		if (genomicPrefixes.contains(prefix))
			return "Genomic";
		if (rnaPrefixes.contains(prefix))
			return "RNA";
		if (mrnaPrefixes.contains(prefix))
			return "mRNA";
		if (proteinPrefixes.contains(prefix))
			return "Protein";
		throw new IllegalArgumentException("Unknown RefSeq prefix: " + refseqId.getId());
	}

	public NcbiTaxonomyID getTaxId() {
		return taxId;
	}

	public String getSpeciesName() {
		return speciesName;
	}

	public RefSeqID getRefseqId() {
		return refseqId;
	}

	public GiNumberID getGi() {
		return gi;
	}

	public String getReleaseDirectoryInclusion() {
		return releaseDirectoryInclusion;
	}

	public String getRefseqStatus() {
		return refseqStatus;
	}

	public int getLength() {
		return length;
	}

	public static RefSeqReleaseCatalogFileData parseRefSeqReleaseCatalogLine(Line line) {
		String[] toks = line.getText().split("\\t");
		if (toks.length == 7) {
			try {
				NcbiTaxonomyID taxId = new NcbiTaxonomyID(toks[0]);
				String speciesName = toks[1];
				RefSeqID refseqId = new RefSeqID(toks[2]);
				GiNumberID gi = new GiNumberID(toks[3]);
				String releaseDirectories = toks[4];
				String refseqStatus = toks[5];
				int length = Integer.parseInt(toks[6]);
				return new RefSeqReleaseCatalogFileData(taxId, speciesName, refseqId, gi, releaseDirectories,
						refseqStatus, length, line.getByteOffset(), line.getLineNumber());
			} catch (IllegalArgumentException e) {
				logger.warn("Error while parsing line: " + line.getText(), e);
				return null;
			}
		}

		logger.error("Unexpected number of tokens (" + toks.length + ") on line: " + line);
		return null;
	}

	/**
	 * @return the moleculeType
	 */
	public String getMoleculeType() {
		return moleculeType;
	}

	/**
	 * @return the isPredicted
	 */
	public boolean isPredicted() {
		return isPredicted;
	}

}
