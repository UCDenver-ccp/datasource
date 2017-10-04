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

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MeshID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbDiseaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SnoMedCtId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UmlsId;

/**
 * File record capturing single line record from PharmGKB's diseases.tsv file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(ontClass = CcpExtensionOntology.PHARMGKB_DISEASE_RECORD, dataSource = DataSource.PHARMGKB, schemaVersion = "2", license = License.PHARMGKB, licenseUri = "http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf", citation = "M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417", comment = "data from PharmGKB's disease.tsv file", label = "disease record")
public class PharmGkbDiseaseFileRecord extends SingleLineFileRecord {

	public static final Logger logger = Logger.getLogger(PharmGkbDiseaseFileRecord.class);

	private static final String MESH_PREFIX = "MeSH:";
	private static final String SNOMEDCT_PREFIX = "SnoMedCT:";
	private static final String UMLS_PREFIX = "UMLS:";

	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DISEASE_RECORD___ACCESSION_IDENTIFIER_FIELD_VALUE)
	private PharmGkbDiseaseId accessionId;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DISEASE_RECORD___NAME_FIELD_VALUE)
	private final String name;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DISEASE_RECORD___ALTERNATIVE_NAME_FIELD_VALUE)
	private Collection<String> alternativeNames;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DISEASE_RECORD___CROSS_REFERENCE_FIELD_VALUE)
	private Collection<DataSourceIdentifier<?>> crossReferences;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_DISEASE_RECORD___EXTERNAL_VOCABULARY_FIELD_VALUE)
	private Collection<DataSourceIdentifier<?>> externalVocabulary;

	public PharmGkbDiseaseFileRecord(String pharmGkbAccessionId, String name, String alternativeNames,
			String crossReferences, String externalVocabulary, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.name = name;
		setAlternativeNames(StringUtil.delimitAndTrim(alternativeNames, StringConstants.COMMA,
				StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE));
		if (!crossReferences.isEmpty()) {
			logger.warn(
					"The cross references field has been empty until now. Use this data to properly parse the cross references field: "
							+ crossReferences);
		}
		this.crossReferences = new ArrayList<DataSourceIdentifier<?>>();
		setExternalVocabulary(externalVocabulary);
		this.accessionId = new PharmGkbDiseaseId(pharmGkbAccessionId);
	}

	private void setAlternativeNames(Collection<String> names) {
		alternativeNames = new ArrayList<String>(names);
	}

	public PharmGkbDiseaseId getAccessionId() {
		return accessionId;
	}

	public Collection<String> getAlternativeNames() {
		return alternativeNames;
	}

	public String getName() {
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
				try {
					String idOnly = StringUtil.removeSuffixRegex(m.group(1), "\\(.*?\\)");
					if (idOnly.startsWith(MESH_PREFIX)) {
						this.externalVocabulary.add(new MeshID(StringUtil.removePrefix(idOnly, MESH_PREFIX)));
					} else if (idOnly.startsWith(SNOMEDCT_PREFIX)) {
						this.externalVocabulary.add(new SnoMedCtId(StringUtil.removePrefix(idOnly, SNOMEDCT_PREFIX)));
					} else if (idOnly.startsWith(UMLS_PREFIX)) {
						this.externalVocabulary.add(new UmlsId(StringUtil.removePrefix(idOnly, UMLS_PREFIX)));
					} else {
						logger.warn("Unhandled external reference: " + idOnly);
						// throw new IllegalArgumentException("Unknown external
						// reference prefix: " +
						// idOnly);
					}
				} catch (IllegalArgumentException e) {
					ProbableErrorDataSourceIdentifier id = new ProbableErrorDataSourceIdentifier(externalVocabulary,
							null, e.getMessage());
					this.externalVocabulary.add(id);
				}
			}
		}
	}
}
