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

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbGeneId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * File record capturing single line record from PharmGKB's genes.tsv file.
 * <p>
 * 
 * @author Yuriy Malenkiy
 * 
 */
@Record(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD, dataSource = DataSource.PHARMGKB, schemaVersion = "2", license = License.PHARMGKB, licenseUri = "http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf", comment = "data from PharmGKB's genes.tsv file", citation = "M. Whirl-Carrillo, E.M. McDonagh, J. M. Hebert, L. Gong, K. Sangkuhl, C.F. Thorn, R.B. Altman and T.E. Klein. \"Pharmacogenomics Knowledge for Personalized Medicine\" Clinical Pharmacology & Therapeutics (2012) 92(4): 414-417", label = "gene record")
@EqualsAndHashCode(callSuper = false)
@Data
public class PharmGkbGeneFileRecord extends SingleLineFileRecord {

	private static final Logger logger = Logger.getLogger(PharmGkbGeneFileRecord.class);
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___ACCESSION_IDENTIFIER_FIELD_VALUE)
	private final PharmGkbGeneId accessionId;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___ENTREZ_GENE_IDENTIFIER_FIELD_VALUE)
	private final Set<NcbiGeneId> entrezGeneIds;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___HGNC_IDENTIFIER_FIELD_VALUE)
	private Set<HgncID> hgncIds;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___ENSEMBL_GENE_IDENTIFIER_FIELD_VALUE)
	private final EnsemblGeneID ensemblGeneId;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___NAME_FIELD_VALUE)
	private final String name;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___SYMBOL_FIELD_VALUE)
	private final String symbol;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___ALTERNATIVE_NAME_IDENTIFIER_FIELD_VALUE)
	private final Collection<String> alternativeNames;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___ALTERNATIVE_SYMBOL_IDENTIFIER_FIELD_VALUE)
	private final Collection<String> alternativeSymbols;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___IS_VIP_FIELD_VALUE)
	private final boolean isVip;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___HAS_VARIANT_ANNOTATION_FIELD_VALUE)
	private final boolean hasVariantAnnotation;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___CROSS_REFERENCE_FIELD_VALUE)
	private final Collection<DataSourceIdentifier<?>> crossReferences;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___HAS_CPIC_DOSING_GUIDELINE_FIELD_VALUE)
	private final boolean hasCpicDosingGuideline;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___CHROMOSOME_FIELD_VALUE)
	private final String chromosome;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___CHROMOSOMAL_START_FIELD_VALUE)
	private final Integer chromosomalStart;
	@RecordField(ontClass = CcpExtensionOntology.PHARMGKB_GENE_RECORD___CHROMOSOMAL_END_FIELD_VALUE)
	private final Integer chromosomalEnd;

	/**
	 * @param byteOffset
	 * @param lineNumber
	 * @param accessionId
	 * @param hgncIds
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
	public PharmGkbGeneFileRecord(PharmGkbGeneId accessionId, Set<NcbiGeneId> entrezGeneIds, Set<HgncID> hgncIds,
			EnsemblGeneID ensemblGeneId, String name, String symbol, Collection<String> alternativeNames,
			Collection<String> alternativeSymbols, boolean isVip, boolean hasVariantAnnotation,
			Collection<DataSourceIdentifier<?>> crossReferences, boolean hasCpicDosingGuideline, String chromosome,
			Integer chromosomalStart, Integer chromosomalEnd, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.accessionId = accessionId;
		this.entrezGeneIds = entrezGeneIds;
		this.hgncIds = hgncIds;
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
