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
package edu.ucdenver.ccp.fileparsers.ncbi.refseq;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * Representation of data from the RefSeq RefSeq-release#.catalog.gz file
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.REFSEQ,
		comment="http://www.ncbi.nih.gov/RefSeq/, ftp://ftp.ncbi.nlm.nih.gov/refseq/release/release-catalog/README",
		license=License.NCBI,
		citation="NCBI Reference Sequences (RefSeq): current status, new features and genome annotation policy. Pruitt KD, Tatusova T, Brown GR, Maglott DR. Nucleic Acids Res. 2012 Jan;40(Database issue):D130-5., The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 18, The Reference Sequence (RefSeq) Project. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091 ",
		label="rel. catalog record")
public class RefSeqReleaseCatalogFileData extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(RefSeqReleaseCatalogFileData.class);
	@RecordField
	private final NcbiTaxonomyID taxId;
	@RecordField
	private final String speciesName;
	@RecordField(comment="", isKeyField=true)
	private final RefSeqID refseqId;

	@RecordField(comment="")
	private final GiNumberID gi;
	@RecordField
	private final String releaseDirectoryInclusion;
	@RecordField(comment=" refseq status: na - not available; status codes are not applied to most genomic records, INFERRED, PREDICTED, PROVISIONAL, VALIDATED, REVIEWED, MODEL, UNKNOWN - status code not provided; however usually is provided for this type of record")
	private final String refseqStatus;
	@RecordField
	private final int length;
	@RecordField
	private final String moleculeType;
	@RecordField
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
		String prefix = refseqId.getDataElement().substring(0, 2);
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
		String prefix = refseqId.getDataElement().substring(0, 2);
		if (genomicPrefixes.contains(prefix))
			return "Genomic";
		if (rnaPrefixes.contains(prefix))
			return "RNA";
		if (mrnaPrefixes.contains(prefix))
			return "mRNA";
		if (proteinPrefixes.contains(prefix))
			return "Protein";
		throw new IllegalArgumentException("Unknown RefSeq prefix: " + refseqId.getDataElement());
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
