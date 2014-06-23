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
package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * This class represents data contained in the EntrezGene gene_info file.
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.EG,
	comment="",
	license=License.NCBI,
	citation="The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 19 Gene: A Directory of Genes. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091",
	label="gene_info record")
public class EntrezGeneInfoFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "ENTREZ_GENEINFO_RECORD_";

	@RecordField(comment="the unique identifier provided by NCBI Taxonomy for the species or strain/isolate")
	private final NcbiTaxonomyID taxonID;

	@RecordField(comment="the unique identifier for a gene ASN1:  geneid")
	private final EntrezGeneID geneID;

	@RecordField(comment="the default symbol for the gene ASN1:  gene->locus")
	private final String symbol;

	@RecordField(comment="the LocusTag value ASN1:  gene->locus-tag")
	private final String locusTag;

	@RecordField(comment="set of unofficial symbols for the gene")
	private final Set<String> synonyms;

	@RecordField(comment="set of identifiers in other databases for this gene.  The unit of the set is database:value.")
	private final Set<DataSourceIdentifier<?>> dbXrefs;

	@RecordField(comment="the chromosome on which this gene is placed.  for mitochondrial genomes, the value 'MT' is used.")
	private final String chromosome;

	@RecordField(comment="the map location for this gene")
	private final String mapLocation;

	@RecordField(comment="a descriptive name for this gene")
	private final String description;

	@RecordField(comment="the type assigned to the gene according to the list of options provided in http://www.ncbi.nlm.nih.gov/IEB/ToolBox/CPP_DOC/lxr/source/src/objects/entrezgene/entrezgene.asn ")
	private final String typeOfGene;

	@RecordField(comment="when not '-', indicates that this symbol is from a a nomenclature authority", label="official symbol")
	private final String symbolFromNomenclatureAuthority;

	@RecordField(comment="when not '-', indicates that this full name is from a a nomenclature authority", label="official name")
	private final String fullNameFromNomenclatureAuthority;

	@RecordField(comment="when not '-', indicates the status of the name from the nomenclature authority (O for official, I for interim)")
	private final String nomenclatureStatus;

	@RecordField(comment="set of some alternate descriptions that have been assigned to a GeneID '-' indicates none is being reported.")
	private final Set<String> otherDesignations;

	@RecordField(comment="the last date a gene record was updated, in YYYYMMDD format")
	private final String modificationDate;

	public EntrezGeneInfoFileData(NcbiTaxonomyID taxonID, EntrezGeneID geneID, String symbol,
			String locusTag, Set<String> synonyms, Set<DataSourceIdentifier<?>> dbXrefs,
			String chromosome, String mapLocation, String description, String typeOfGene,
			String symbolFromNomenclatureAuthority, String fullNameFromNomenclatureAuthority,
			String nomenclatureStatus, Set<String> otherDesignations, String modificationDate,
			long byteOffset, long lineNumber) {
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

	public EntrezGeneID getGeneID() {
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
