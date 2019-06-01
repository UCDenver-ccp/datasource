package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

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

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Collection;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AtcCode;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbDrugId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PubChemCompoundId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RxNormId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * File record capturing single line record from PharmGKB's drugs.tsv file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD, dataSource = DataSource.PHARMGKB, license = License.PHARMGKB, licenseUri = "http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf", comment = "data from PharmGKB's drugs.tsv file", citation = "M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417", label = "drug record")
@EqualsAndHashCode(callSuper = false)
@Data
public class PharmGkbDrugFileRecord extends SingleLineFileRecord {
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___ACCESSION_IDENTIFIER_FIELD_VALUE)
	private PharmGkbDrugId accessionId;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___NAME_FIELD_VALUE)
	private final String name;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___GENERIC_NAMES_FIELD_VALUE)
	private Collection<String> genericNames;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___TRADE_NAMES_FIELD_VALUE)
	private Collection<String> tradeNames;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___BRAND_MIXTURES_FIELD_VALUE)
	private Collection<String> brandMixtures;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___TYPE_FIELD_VALUE)
	private final String type;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___CROSS_REFERENCE_FIELD_VALUE)
	private Collection<DataSourceIdentifier<?>> crossReferences;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___URL_FIELD_VALUE)
	private final String url;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___SMILES_FIELD_VALUE)
	private final String smiles;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___INCHI_FIELD_VALUE)
	private final String inChI;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___DOSING_GUIDELINE_FIELD_VALUE)
	private final String dosingGuideline;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___EXTERNAL_VOCABULARY_FIELD_VALUE)
	private final String externalVocabulary;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___CLINICAL_ANNOTATION_COUNT_FIELD_VALUE)
	private final int clinicalAnnotationCount;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___VARIANT_ANNOTATION_COUNT_FIELD_VALUE)
	private final int variantAnnotationCount;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___PATHWAY_COUNT_FIELD_VALUE)
	private final int pathwayCount;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___VIP_COUNT_FIELD_VALUE)
	private final int vipCount;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___DOSING_GUIDELINE_SOURCES_FIELD_VALUE)
	private final Collection<String> dosingGuidelineSources;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___TOP_CLINICAL_ANNOTATION_LEVEL_FIELD_VALUE)
	private final String topClinicalAnnotationLevel;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___TOP_FDA_LABEL_TESTING_LEVEL_FIELD_VALUE)
	private final String topFdaLabelTestingLevel;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___TOP_ANY_DRUG_LABEL_TESTING_LEVEL_FIELD_VALUE)
	private final String topAnyDrugLabelTestingLevel;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___LABEL_HAS_DOSING_INFO_FIELD_VALUE)
	private final Boolean labelHasDosingInfo;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___HAS_RX_ANNOTATION_FIELD_VALUE)
	private final Boolean hasRxAnnotation;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___RXNORM_IDENTIFIERS_FIELD_VALUE)
	private final Set<RxNormId> rxNormIdentifiers;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___ATC_IDENTIFIERS_FIELD_VALUE)
	private final Set<AtcCode> atcIdentifiers;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DRUG_RECORD___PUBCHEM_COMPOUND_IDENTIFIERS_FIELD_VALUE)
	private final Set<PubChemCompoundId> pubchemCompoundIdentifiers;

	public PharmGkbDrugFileRecord(String pharmGkbAccessionId, String name, Collection<String> genericNames,
			Collection<String> tradeNames, Collection<String> brandMixtures, String type,
			Collection<DataSourceIdentifier<?>> crossReferences, String url, String smiles, String inChI,
			String dosingGuideline, String externalVocabulary, int clinicalAnnotationCount, int variantAnnotationCount,
			int pathwayCount, int vipCount, Collection<String> dosingGuidelineSources,
			String topClinicalAnnotationLevel, String topFdaLabelTestingLevel, String topAnyDrugLabelTestingLevel,
			String labelHasDosingInfo, String hasRxAnnotation, Set<RxNormId> rxNormIdentifiers,
			Set<AtcCode> atcIdentifiers, Set<PubChemCompoundId> pubchemCompoundIdentifiers, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.url = isNotBlank(url) ? url : null;
		this.accessionId = new PharmGkbDrugId(pharmGkbAccessionId);
		this.name = isNotBlank(name) ? name : null;
		this.genericNames = genericNames;
		this.tradeNames = tradeNames;
		this.brandMixtures = brandMixtures;
		this.type = type;
		this.crossReferences = crossReferences;
		this.smiles = isNotBlank(smiles) ? smiles : null;
		this.inChI = isNotBlank(inChI) ? inChI : null;
		this.dosingGuideline = isNotBlank(dosingGuideline) ? dosingGuideline : null;
		this.externalVocabulary = isNotBlank(externalVocabulary) ? externalVocabulary : null;
		this.clinicalAnnotationCount = clinicalAnnotationCount;
		this.variantAnnotationCount = variantAnnotationCount;
		this.pathwayCount = pathwayCount;
		this.vipCount = vipCount;
		this.dosingGuidelineSources = dosingGuidelineSources;
		this.topClinicalAnnotationLevel = isNotBlank(topClinicalAnnotationLevel) ? topClinicalAnnotationLevel : null;
		this.topFdaLabelTestingLevel = isNotBlank(topFdaLabelTestingLevel) ? topFdaLabelTestingLevel : null;
		this.topAnyDrugLabelTestingLevel = isNotBlank(topAnyDrugLabelTestingLevel) ? topAnyDrugLabelTestingLevel : null;
		this.labelHasDosingInfo = isNotBlank(labelHasDosingInfo) ? true : null;
		this.hasRxAnnotation = isNotBlank(hasRxAnnotation) ? true : null;
		this.rxNormIdentifiers = rxNormIdentifiers;
		this.atcIdentifiers = atcIdentifiers;
		this.pubchemCompoundIdentifiers = pubchemCompoundIdentifiers;

		if (isNotBlank(labelHasDosingInfo) && !labelHasDosingInfo.equals("Label Has Dosing Info")) {
			throw new IllegalArgumentException(
					"Unhandled value for 'Label Has Dosing Info' column: " + labelHasDosingInfo);
		}

		if (isNotBlank(hasRxAnnotation) && !hasRxAnnotation.equals("Has Rx Annotation")) {
			throw new IllegalArgumentException("Unhandled value for 'Has Rx Annotation' column: " + hasRxAnnotation);
		}
	}

}
