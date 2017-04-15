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
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * This class represents data contained in the EntrezGene gene_info file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.NCBI_GENE, ontClass=CcpExtensionOntology.NCBI_GENE_RECORD, comment = "", license = License.NCBI, citation = "The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091", label = "gene_info record")
public class EntrezGeneInfoFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "ENTREZ_GENEINFO_RECORD_";

	@RecordField(ontClass = CcpExtensionOntology.TAXONOMY_IDENTIFIER_FIELD_VALUE, comment = "the unique identifier provided by NCBI Taxonomy for the species or strain/isolate")
	private final NcbiTaxonomyID taxonID;

	@RecordField(ontClass = CcpExtensionOntology.NCBI_DNA_IDENTIFIER_FIELD_VALUE, comment = "the unique identifier for a gene ASN1:  geneid")
	private final NcbiGeneId geneID;

	@RecordField(ontClass = CcpExtensionOntology.SYMBOL_FIELD_VALUE, comment = "the default symbol for the gene ASN1:  gene->locus")
	private final String symbol;

	@RecordField(ontClass = CcpExtensionOntology.LOCUS_TAG_FIELD_VALUE, comment = "the LocusTag value ASN1:  gene->locus-tag")
	private final String locusTag;

	@RecordField(ontClass = CcpExtensionOntology.SYNONYMS_FIELD_VALUE, comment = "set of unofficial symbols for the gene")
	private final Set<String> synonyms;

	@RecordField(ontClass = CcpExtensionOntology.DATABASE_CROSS_REFERENCE_FIELD_VALUE, comment = "set of identifiers in other databases for this gene.  The unit of the set is database:value.")
	private final Set<DataSourceIdentifier<?>> dbXrefs;

	@RecordField(ontClass = CcpExtensionOntology.CHROMOSOME_FIELD_VALUE, comment = "the chromosome on which this gene is placed.  for mitochondrial genomes, the value 'MT' is used.")
	private final String chromosome;

	@RecordField(ontClass = CcpExtensionOntology.MAP_LOCATION_FIELD_VALUE, comment = "the map location for this gene")
	private final String mapLocation;

	@RecordField(ontClass = CcpExtensionOntology.DESCRIPTION_FIELD_VALUE, comment = "a descriptive name for this gene")
	private final String description;

	@RecordField(ontClass = CcpExtensionOntology.GENE_TYPE_FIELD_VALUE, comment = "the type assigned to the gene according to the list of options provided in http://www.ncbi.nlm.nih.gov/IEB/ToolBox/CPP_DOC/lxr/source/src/objects/entrezgene/entrezgene.asn ")
	private final String typeOfGene;

	@RecordField(ontClass = CcpExtensionOntology.SYMBOL_FROM_NOMENCLATURE_AUTHORITY_FIELD_VALUE, comment = "when not '-', indicates that this symbol is from a a nomenclature authority", label = "official symbol")
	private final String symbolFromNomenclatureAuthority;

	@RecordField(ontClass = CcpExtensionOntology.FULL_NAME_FROM_NOMENCLATURE_AUTHORITY_FIELD_VALUE, comment = "when not '-', indicates that this full name is from a a nomenclature authority", label = "official name")
	private final String fullNameFromNomenclatureAuthority;

	@RecordField(ontClass = CcpExtensionOntology.NOMENCLATURE_AUTHORITY_STATUS, comment = "when not '-', indicates the status of the name from the nomenclature authority (O for official, I for interim)")
	private final String nomenclatureStatus;

	@RecordField(ontClass = CcpExtensionOntology.OTHER_DESIGNATIONS_FIELD_VALUE, comment = "set of some alternate descriptions that have been assigned to a GeneID '-' indicates none is being reported.")
	private final Set<String> otherDesignations;

	@RecordField(ontClass = CcpExtensionOntology.DATE_MODIFIED_FIELD_VALUE, comment = "the last date a gene record was updated, in YYYYMMDD format")
	private final String modificationDate;

	public EntrezGeneInfoFileData(NcbiTaxonomyID taxonID, NcbiGeneId geneID, String symbol, String locusTag,
			Set<String> synonyms, Set<DataSourceIdentifier<?>> dbXrefs, String chromosome, String mapLocation,
			String description, String typeOfGene, String symbolFromNomenclatureAuthority,
			String fullNameFromNomenclatureAuthority, String nomenclatureStatus, Set<String> otherDesignations,
			String modificationDate, long byteOffset, long lineNumber) {
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
	}

	public NcbiTaxonomyID getTaxonID() {
		return taxonID;
	}

	public NcbiGeneId getGeneID() {
		return geneID;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getLocusTag() {
		return locusTag;
	}

	public Set<String> getSynonyms() {
		return synonyms;
	}

	public Set<DataSourceIdentifier<?>> getDbXrefs() {
		return dbXrefs;
	}

	public String getChromosome() {
		return chromosome;
	}

	public String getMapLocation() {
		return mapLocation;
	}

	public String getDescription() {
		return description;
	}

	public String getTypeOfGene() {
		return typeOfGene;
	}

	public String getSymbolFromNomenclatureAuthority() {
		return symbolFromNomenclatureAuthority;
	}

	public String getFullNameFromNomenclatureAuthority() {
		return fullNameFromNomenclatureAuthority;
	}

	public String getNomenclatureStatus() {
		return nomenclatureStatus;
	}

	public Set<String> getOtherDesignations() {
		return otherDesignations;
	}

	public String getModificationDate() {
		return modificationDate;
	}

}
