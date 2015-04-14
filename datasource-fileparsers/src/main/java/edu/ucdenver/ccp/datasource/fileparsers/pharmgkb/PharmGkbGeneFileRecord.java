package edu.ucdenver.ccp.fileparsers.pharmgkb;

import java.util.Collection;

import lombok.Data;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbID;

/**
 * File record capturing single line record from PharmGKB's genes.tsv file.
 * <p>
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(dataSource = DataSource.PHARMGKB, schemaVersion = "2", license = License.PHARMGKB, licenseUri = "http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf", comment = "data from PharmGKB's genes.tsv file", citation = "M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417", label = "gene record")
@Data
public class PharmGkbGeneFileRecord extends SingleLineFileRecord {

	private static final Logger logger = Logger.getLogger(PharmGkbGeneFileRecord.class);
	@RecordField
	private final PharmGkbID accessionId;
	@RecordField
	private final EntrezGeneID entrezGeneId;
	@RecordField
	private final EnsemblGeneID ensemblGeneId;
	@RecordField
	private final String name;
	@RecordField
	private final String symbol;
	@RecordField
	private final Collection<String> alternativeNames;
	@RecordField
	private final Collection<String> alternativeSymbols;
	@RecordField
	private final boolean isVip;
	@RecordField
	private final boolean hasVariantAnnotation;
	@RecordField(comment = "Note that many of the IDs listed as RefSeq_[something] are not RefSeq IDs. There are GenBank and UniProt IDs mixed in there among possibly others.")
	private final Collection<DataSourceIdentifier<?>> crossReferences;
	@RecordField
	private final boolean hasCpicDosingGuideline;
	@RecordField
	private final String chromosome;
	@RecordField
	private final Integer chromosomalStart;
	@RecordField
	private final Integer chromosomalEnd;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param accessionId
	 * @param entrezGeneId
	 * @param ensemblGeneId
	 * @param name
	 * @param symbol
	 * @param alternativeNames
	 * @param alternativeSymbols
	 * @param isVip
	 * @param hasVariantAnnotation
	 * @param crossReferences
	 */
	public PharmGkbGeneFileRecord(PharmGkbID accessionId, EntrezGeneID entrezGeneId, EnsemblGeneID ensemblGeneId,
			String name, String symbol, Collection<String> alternativeNames,
			Collection<String> alternativeSymbols, boolean isVip, boolean hasVariantAnnotation,
			Collection<DataSourceIdentifier<?>> crossReferences, boolean hasCpicDosingGuideline, String chromosome,
			Integer chromosomalStart, Integer chromosomalEnd, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.accessionId = accessionId;
		this.entrezGeneId = entrezGeneId;
		this.ensemblGeneId = ensemblGeneId;
		this.name = name;
		this.symbol = symbol;
		this.alternativeNames = alternativeNames;
		this.alternativeSymbols = alternativeSymbols;
		this.isVip = isVip;
		this.hasVariantAnnotation = hasVariantAnnotation;
		this.crossReferences = crossReferences;
		this.hasCpicDosingGuideline = hasCpicDosingGuideline;
		this.chromosome = chromosome;
		this.chromosomalStart = chromosomalStart;
		this.chromosomalEnd = chromosomalEnd;
	}

	public boolean hasVariantAnnotation() {
		return hasVariantAnnotation;
	}

	public boolean hasCpicDosingGuideline() {
		return hasCpicDosingGuideline;
	}

}
