package edu.ucdenver.ccp.fileparsers.pharmgkb;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Collection;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbID;

/**
 * File record capturing single line record from PharmGKB's drugs.tsv file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(dataSource = DataSource.PHARMGKB,
		license=License.PHARMGKB,
		licenseUri="http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf",
		comment="data from PharmGKB's drugs.tsv file",
		citation="M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417",
		label="drug record")
@Data
public class PharmGkbDrugFileRecord extends SingleLineFileRecord {
	@RecordField
	private PharmGkbID accessionId;
	@RecordField
	private final String name;
	@RecordField
	private Collection<String> genericNames;
	@RecordField
	private Collection<String> tradeNames;
	@RecordField
	private Collection<String> brandMixtures;
	@RecordField
	private final String type;
	@RecordField
	private Collection<DataSourceIdentifier<?>> crossReferences;
	@RecordField
	private final String url;
	@RecordField
	private final String smiles;
	@RecordField
	private final String dosingGuideline;
	@RecordField
	private final String externalVocabulary;

	public PharmGkbDrugFileRecord(String pharmGkbAccessionId, String name, Collection<String> genericNames,
			Collection<String> tradeNames, Collection<String> brandMixtures, String type,
			Collection<DataSourceIdentifier<?>> crossReferences, String url, String smiles, String dosingGuideline,String externalVocabulary,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.url = isNotBlank(url) ? url : null;
		this.accessionId = new PharmGkbID(pharmGkbAccessionId);
		this.name = isNotBlank(name) ? name : null;
		this.genericNames = genericNames;
		this.tradeNames = tradeNames;
		this.brandMixtures = brandMixtures;
		this.type = type;
		this.crossReferences = crossReferences;
		this.smiles = isNotBlank(smiles) ? smiles : null;
		this.dosingGuideline = isNotBlank(dosingGuideline) ? dosingGuideline : null;
		this.externalVocabulary = isNotBlank(externalVocabulary) ? externalVocabulary : null;
	}

	

}
