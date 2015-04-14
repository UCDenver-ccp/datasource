package edu.ucdenver.ccp.fileparsers.pharmgkb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceElement;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.MeshID;
import edu.ucdenver.ccp.datasource.identifiers.other.SnoMedCtId;
import edu.ucdenver.ccp.datasource.identifiers.other.UmlsId;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbID;
import edu.ucdenver.ccp.fileparsers.field.DataElementLiteral;

/**
 * File record capturing single line record from PharmGKB's diseases.tsv file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(dataSource = DataSource.PHARMGKB, schemaVersion = "2", license = License.PHARMGKB, licenseUri = "http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf", citation = "M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417", comment = "data from PharmGKB's disease.tsv file", label = "disease record")
public class PharmGkbDiseaseFileRecord extends SingleLineFileRecord {

	public static final Logger logger = Logger.getLogger(PharmGkbDiseaseFileRecord.class);

	private static final String MESH_PREFIX = "MeSH:";
	private static final String SNOMEDCT_PREFIX = "SnoMedCT:";
	private static final String UMLS_PREFIX = "UMLS:";

	@RecordField
	private PharmGkbID accessionId;
	@RecordField
	private final DataSourceElement<String> name;
	@RecordField
	private Collection<DataElementLiteral<String>> alternativeNames;
	@RecordField(comment = "This field appears to be empty for all records.")
	private Collection<DataSourceIdentifier<?>> crossReferences;
	@RecordField
	private Collection<DataSourceIdentifier<?>> externalVocabulary;

	public PharmGkbDiseaseFileRecord(String pharmGkbAccessionId, String name, String alternativeNames,
			String crossReferences, String externalVocabulary, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.name = new DataElementLiteral<String>(name) {
		};
		setAlternativeNames(StringUtil.delimitAndTrim(alternativeNames, StringConstants.COMMA,
				StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE));
		if (!crossReferences.isEmpty()) {
			logger.warn("The cross references field has been empty until now. Use this data to properly parse the cross references field: "
					+ crossReferences);
		}
		this.crossReferences = new ArrayList<DataSourceIdentifier<?>>();
		setExternalVocabulary(externalVocabulary);
		this.accessionId = new PharmGkbID(pharmGkbAccessionId);
	}

	private void setAlternativeNames(Collection<String> names) {
		alternativeNames = new ArrayList<DataElementLiteral<String>>();
		for (String name : names)
			alternativeNames.add(new DataElementLiteral<String>(name) {
			});
	}

	public PharmGkbID getAccessionId() {
		return accessionId;
	}

	public Collection<DataElementLiteral<String>> getAlternativeNames() {
		return alternativeNames;
	}

	public DataSourceElement<String> getName() {
		return name;
	}

	/**
	 * @return the crossReferences
	 */
	public Collection<DataSourceIdentifier<?>> getCrossReferences() {
		return crossReferences;
	}

	/**
	 * @param crossReferences
	 *            the crossReferences to set
	 */
	private void setCrossReferences(Collection<DataSourceIdentifier<?>> crossReferences) {
		this.crossReferences = crossReferences;
	}

	/**
	 * @return the externalVocabulary
	 */
	public Collection<DataSourceIdentifier<?>> getExternalVocabulary() {
		return externalVocabulary;
	}

	/**
	 * @param externalVocabulary
	 *            the externalVocabulary to set
	 */
	private void setExternalVocabulary(String externalVocabulary) {
		this.externalVocabulary = new ArrayList<DataSourceIdentifier<?>>();
		if (!externalVocabulary.isEmpty()) {
			Pattern p = Pattern.compile("([^,]+:.*?\\(.*?\\)),?");
			Matcher m = p.matcher(externalVocabulary);
			while (m.find()) {
				String idOnly = StringUtil.removeSuffixRegex(m.group(1), "\\(.*?\\)");
				if (idOnly.startsWith(MESH_PREFIX)) {
					this.externalVocabulary.add(new MeshID(StringUtil.removePrefix(idOnly, MESH_PREFIX)));
				} else if (idOnly.startsWith(SNOMEDCT_PREFIX)) {
					this.externalVocabulary.add(new SnoMedCtId(StringUtil.removePrefix(idOnly, SNOMEDCT_PREFIX)));
				} else if (idOnly.startsWith(UMLS_PREFIX)) {
					this.externalVocabulary.add(new UmlsId(StringUtil.removePrefix(idOnly, UMLS_PREFIX)));
				} else {
					logger.warn("Unhandled external reference: " + idOnly);
					// throw new IllegalArgumentException("Unknown external reference prefix: " +
					// idOnly);
				}
			}
		}
	}
}
