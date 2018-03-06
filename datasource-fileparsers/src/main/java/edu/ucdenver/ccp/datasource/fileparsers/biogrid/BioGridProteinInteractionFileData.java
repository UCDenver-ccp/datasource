package edu.ucdenver.ccp.datasource.fileparsers.biogrid;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2018 Regents of the University of Colorado
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

import java.util.Arrays;
import java.util.List;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioGridInteractionId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioGridInteractorId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Getter;
import lombok.ToString;

/**
 * Data representation of the contents of the BioPlex Interaction List file.
 * http://bioplex.hms.harvard.edu/data/BioPlex_interactionList_v4a.tsv
 * 
 */
@Record(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD, dataSource = DataSource.BIOGRID)
@ToString
@Getter
public class BioGridProteinInteractionFileData extends SingleLineFileRecord {

	// #BioGRID Interaction ID
	// Entrez Gene Interactor A
	// Entrez Gene Interactor B
	// BioGRID ID Interactor A
	// BioGRID ID Interactor B
	// Systematic Name Interactor A
	// Systematic Name Interactor B
	// Official Symbol Interactor A
	// Official Symbol Interactor B
	// Synonyms Interactor A
	// Synonyms Interactor B
	// Experimental System
	// Experimental System Type
	// Author
	// Pubmed ID
	// Organism Interactor A
	// Organism Interactor B
	// Throughput
	// Score
	// Modification
	// Phenotypes
	// Qualifications
	// Tags
	// Source Database

	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___BIOGRID_INTERACTION_IDENTIFIER_FIELD_VALUE)
	private final BioGridInteractionId biogridInteractionId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_A_NCBI_GENE_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> ncbiGeneIdA;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_B_NCBI_GENE_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> ncbiGeneIdB;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_A_BIOGRID_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> biogridInteractorIdA;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_B_BIOGRID_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> biogridInteractorIdB;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_A_OFFICIAL_SYMBOL_FIELD_VALUE)
	private final String officialSymbolInteractorA;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_B_OFFICIAL_SYMBOL_FIELD_VALUE)
	private final String officialSymbolInteractorB;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_A_SYSTEMATIC_NAME_FIELD_VALUE)
	private final String systematicNameInteractorA;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_B_SYSTEMATIC_NAME_FIELD_VALUE)
	private final String systematicNameInteractorB;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_A_SYNONYMS_FIELD_VALUE)
	private final List<String> synonymsInteractorA;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_B_SYNONYMS_FIELD_VALUE)
	private final List<String> synonymsInteractorB;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___EXPERIMENTAL_SYSTEM_FIELD_VALUE)
	private final String experimentalSystem;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___EXPERIMENTAL_SYSTEM_TYPE_FIELD_VALUE)
	private final String experimentalSystemType;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___AUTHOR_FIELD_VALUE)
	private final String author;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> pubmedId;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_A_ORGANISM_FIELD_VALUE)
	private final DataSourceIdentifier<?> organismInteractorA;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___INTERACTOR_B_ORGANISM_FIELD_VALUE)
	private final DataSourceIdentifier<?> organismInteractorB;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___THROUGHPUT_FIELD_VALUE)
	private final String throughput;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___SCORE_FIELD_VALUE)
	private final Float score;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___MODIFICATION_FIELD_VALUE)
	private final String modification;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___PHENOTYPES_FIELD_VALUE)
	private final List<String> phenotypes;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___QUALIFICATIONS_FIELD_VALUE)
	private final List<String> qualifications;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___TAGS_FIELD_VALUE)
	private final List<String> tags;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___SOURCE_DATABASE_FIELD_VALUE)
	private final String sourceDatabase;
	@RecordField(ontClass = CcpExtensionOntology.BIOGRID_PROTEIN_INTERACTION_RECORD___MULTIVALIDATED_PHYSICAL_FLAG_FIELD_VALUE)
	private final boolean multiValidatedPhysicalFlag;

	public BioGridProteinInteractionFileData(BioGridInteractionId biogridInteractionId,
			DataSourceIdentifier<?> ncbiGeneIdA, DataSourceIdentifier<?> ncbiGeneIdB,
			DataSourceIdentifier<?> biogridInteractorIdA, DataSourceIdentifier<?> biogridInteractorIdB,
			String officialSymbolInteractorA, String officialSymbolInteractorB, String systematicNameInteractorA,
			String systematicNameInteractorB, List<String> synonymsInteractorA, List<String> synonymsInteractorB,
			String experimentalSystem, String experimentalSystemType, String author, DataSourceIdentifier<?> pubmedId,
			DataSourceIdentifier<?> organismInteractorA, DataSourceIdentifier<?> organismInteractorB, String throughput,
			Float score, String modification, List<String> phenotypes, List<String> qualifications, List<String> tags,
			String sourceDatabase, boolean multiValidatedPhysicalFlag, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.biogridInteractionId = biogridInteractionId;
		this.ncbiGeneIdA = ncbiGeneIdA;
		this.ncbiGeneIdB = ncbiGeneIdB;
		this.biogridInteractorIdA = biogridInteractorIdA;
		this.biogridInteractorIdB = biogridInteractorIdB;
		this.officialSymbolInteractorA = officialSymbolInteractorA;
		this.officialSymbolInteractorB = officialSymbolInteractorB;
		this.systematicNameInteractorA = systematicNameInteractorA;
		this.systematicNameInteractorB = systematicNameInteractorB;
		this.synonymsInteractorA = synonymsInteractorA;
		this.synonymsInteractorB = synonymsInteractorB;
		this.experimentalSystem = experimentalSystem;
		this.experimentalSystemType = experimentalSystemType;
		this.author = author;
		this.pubmedId = pubmedId;
		this.organismInteractorA = organismInteractorA;
		this.organismInteractorB = organismInteractorB;
		this.throughput = throughput;
		this.score = score;
		this.modification = modification;
		this.phenotypes = phenotypes;
		this.qualifications = qualifications;
		this.tags = tags;
		this.sourceDatabase = sourceDatabase;
		this.multiValidatedPhysicalFlag = multiValidatedPhysicalFlag;
	}

	public static BioGridProteinInteractionFileData parseLine(Line line, boolean multiValidatedPhysicalFlag) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;
		String tmpStr;
		BioGridInteractionId biogridInteractionId = new BioGridInteractionId(toks[index++]);
		DataSourceIdentifier<?> ncbiGeneIdA = resolveId(DataSource.NCBI_GENE, toks[index++]);
		DataSourceIdentifier<?> ncbiGeneIdB = resolveId(DataSource.NCBI_GENE, toks[index++]);
		DataSourceIdentifier<?> biogridInteractorIdA = resolveId(DataSource.BIOGRID, toks[index++]);
		DataSourceIdentifier<?> biogridInteractorIdB = resolveId(DataSource.BIOGRID, toks[index++]);
		tmpStr = toks[index++];
		String officialSymbolInteractorA = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String officialSymbolInteractorB = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String systematicNameInteractorA = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String systematicNameInteractorB = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		List<String> synonymsInteractorA = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		List<String> synonymsInteractorB = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		String experimentalSystem = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String experimentalSystemType = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		String author = (tmpStr.trim().equals("-")) ? null : tmpStr;
		DataSourceIdentifier<?> pubmedId = resolveId(DataSource.PM, toks[index++]);
		DataSourceIdentifier<?> organismInteractorA = resolveId(DataSource.NCBI_TAXON, toks[index++]);
		DataSourceIdentifier<?> organismInteractorB = resolveId(DataSource.BIOGRID, toks[index++]);
		tmpStr = toks[index++];
		String throughput = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		Float score = (tmpStr.trim().equals("-")) ? null : Float.parseFloat(tmpStr);
		tmpStr = toks[index++];
		String modification = (tmpStr.trim().equals("-")) ? null : tmpStr;
		tmpStr = toks[index++];
		List<String> phenotypes = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		List<String> qualifications = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		List<String> tags = (tmpStr.trim().equals("-")) ? null : Arrays.asList(tmpStr.split("\\|"));
		tmpStr = toks[index++];
		String sourceDatabase = (tmpStr.trim().equals("-")) ? null : tmpStr;

		return new BioGridProteinInteractionFileData(biogridInteractionId, ncbiGeneIdA, ncbiGeneIdB,
				biogridInteractorIdA, biogridInteractorIdB, officialSymbolInteractorA, officialSymbolInteractorB,
				systematicNameInteractorA, systematicNameInteractorB, synonymsInteractorA, synonymsInteractorB,
				experimentalSystem, experimentalSystemType, author, pubmedId, organismInteractorA, organismInteractorB,
				throughput, score, modification, phenotypes, qualifications, tags, sourceDatabase,
				multiValidatedPhysicalFlag, line.getByteOffset(), line.getLineNumber());
	}

	private static DataSourceIdentifier<?> resolveId(DataSource ds, String idStr) {
		try {
			switch (ds) {
			case NCBI_GENE:
				return new NcbiGeneId(idStr);
			case BIOGRID:
				return new BioGridInteractorId(idStr);
			case PM:
				return new PubMedID(idStr);
			case NCBI_TAXON:
				return new NcbiTaxonomyID(idStr);
			default:
				throw new IllegalStateException("Cannot create identifier. Unhandled data source: " + ds.name());
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, ds.name(), e.getMessage());
		}
	}

}
