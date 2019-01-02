package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This class represents data contained in the NCBI gene_info file.
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Record(dataSource = DataSource.NCBI_GENE, ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD, license = License.NCBI, citation = "The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091", label = "gene_info record")
public class NcbiGeneInfoFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "ENTREZ_GENEINFO_RECORD_";

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___TAXON_IDENTIFIER_FIELD_VALUE)
	private final NcbiTaxonomyID taxonID;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___GENE_IDENTIFIER_FIELD_VALUE)
	private final NcbiGeneId geneID;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___SYMBOL_FIELD_VALUE)
	private final String symbol;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___LOCUS_TAG_FIELD_VALUE)
	private final String locusTag;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___SYNONYM_FIELD_VALUE)
	private final Set<String> synonyms;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___DATABASE_CROSS_REFERENCE_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> dbXrefs;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___CHROMOSOME_FIELD_VALUE)
	private final String chromosome;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___MAP_LOCATION_FIELD_VALUE)
	private final String mapLocation;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___DESCRIPTION_FIELD_VALUE)
	private final String description;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___TYPE_OF_GENE_FIELD_VALUE)
	private final String typeOfGene;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___SYMBOL_FROM_NOMENCLATURE_AUTHORITY_FIELD_VALUE)
	private final String symbolFromNomenclatureAuthority;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___FULL_NAME_FROM_NOMENCLATURE_AUTHORITY_FIELD_VALUE)
	private final String fullNameFromNomenclatureAuthority;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___NOMENCLATURE_STATUS_FIELD_VALUE)
	private final String nomenclatureStatus;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___OTHER_DESIGNATIONS_FIELD_VALUE)
	private final Set<String> otherDesignations;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___MODIFICATION_DATE_FIELD_VALUE)
	private final String modificationDate;
	
	@RecordField(ontClass = CcpExtensionOntology.NCBI_GENE_INFO_RECORD___FEATURE_TYPE_FIELD_VALUE)
	private final Set<String> featureTypes;

	public NcbiGeneInfoFileData(NcbiTaxonomyID taxonID, NcbiGeneId geneID, String symbol, String locusTag,
			Set<String> synonyms, Set<DataSourceIdentifier<?>> dbXrefs, String chromosome, String mapLocation,
			String description, String typeOfGene, String symbolFromNomenclatureAuthority,
			String fullNameFromNomenclatureAuthority, String nomenclatureStatus, Set<String> otherDesignations,
			String modificationDate, Set<String> featureTypes, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.taxonID = taxonID;
		this.geneID = geneID;
		this.symbol = symbol;
		this.locusTag = locusTag;
		this.synonyms = synonyms;
		this.dbXrefs = dbXrefs;
		this.chromosome = chromosome;
		this.mapLocation = mapLocation;
		this.description = description;
		this.typeOfGene = typeOfGene;
		this.symbolFromNomenclatureAuthority = symbolFromNomenclatureAuthority;
		this.fullNameFromNomenclatureAuthority = fullNameFromNomenclatureAuthority;
		this.nomenclatureStatus = nomenclatureStatus;
		this.otherDesignations = otherDesignations;
		this.modificationDate = modificationDate;
		this.featureTypes = featureTypes;
	}

}
