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
package edu.ucdenver.ccp.datasource.fileparsers.geneontology;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * A representation of data records contained in the gene_association.mgi file.<br>
 * File columns:<br>
 * 0) Database Designation<br>
 * 1) MGI Marker Accession ID<br>
 * 2) Mouse Marker Symbol<br>
 * 3) NOT Designation<br>
 * 4) GO Term ID<br>
 * 5) MGI Reference Accession ID<br>
 * 6) GO Evidence Code<br>
 * 7) Inferred From<br>
 * 8) Ontology P=Biological Process F=Molecular Function C=Cellular Component <br>
 * 9) Mouse Marker Name <br>
 * 10) Mouse Marker Synonyms (if any)<br>
 * 11) Mouse Marker Type gene transcript protein <br>
 * 12) Taxon <br>
 * 13) Modification Date <br>
 * 14) Assigned By
 *
 * @see ftp://ftp.ebi.ac.uk/pub/databases/GO/goa/UNIPROT/README
 * 
 * @author Bill Baumgartner
 * 
 */
// TODO: is the dataSource correct here?
@Record(dataSource=DataSource.GO,
		comment="ftp://ftp.ebi.ac.uk/pub/databases/GO/goa/UNIPROT/README", 
		license=License.EBI, 
		citation="Dimmer EC , Huntley RP , Alam-Faruque Y , Sawford T , O'Donovan C , Martin MJ et al. (2012) The UniProt-GO Annotation database in 2011.  Nucleic Acids Research 2012 40: D565-D570." )
public class GeneAssociationFileData extends SingleLineFileRecord {
	public static final String RECORD_NAME_PREFIX = "GENE_ASSOCIATION_RECORD_";
	private static Logger logger = Logger.getLogger(GeneAssociationFileData.class);

	@RecordField(comment="1.  DB Database from which annotated entry has been taken.  For the UniProtKB and UniProtKB Complete Proteomes gene associaton files: UniProtKB For the PDB association file:  PDB Example: UniProtKB")
	private final String databaseDesignation;

	@RecordField(comment="2.  DB_Object_ID A unique identifier in the database for the item being annotated.  Here: an accession number or identifier of the annotated protein (or PDB entry for the gene_association.goa_pdb file) For the UniProtKB and UniProtKB Complete Proteomes gene association files: a UniProtKB Accession.  Examples O00165")
	private final DataSourceIdentifier<?> geneID;

	@RecordField(comment="3.  DB_Object_Symbol A (unique and valid) symbol (gene name) that corresponds to the DB_Object_ID.  An officially approved gene symbol will be used in this field when available.  Alternatively, other gene symbols or locus names are applied.  If no symbols are available, the identifier applied in column 2 will be used.  Examples: G6PC CYB561 MGCQ309F3")
	private final String geneSymbol;

	@RecordField(comment="4.  Qualifier This column is used for flags that modify the interpretation of an annotation.  If not null, then values in this field can equal: NOT, colocalizes_with, contributes_to, NOT | contributes_to, NOT | colocalizes_with Example: NOT")
	private final String notDesignation;

	@RecordField(comment="5.  GO ID The GO identifier for the term attributed to the DB_Object_ID.  Example: GO:0005634")
	private final GeneOntologyID goTermID;

	@RecordField(comment="6.  DB:Reference A single reference cited to support an annotation.  Where an annotation cannot reference a paper, this field will contain a GO_REF identifier. See section 8 and http://www.geneontology.org/doc/GO.references for an explanation of the reference types used.  Examples: PMID:9058808 DOI:10.1046/j.1469-8137.2001.00150.x GO_REF:0000002 GO_REF:0000020 GO_REF:0000004 GO_REF:0000003 GO_REF:0000019 GO_REF:0000023 GO_REF:0000024 GO_REF:0000033")
	private final Set<MgiReferenceID> referenceAccessionIDs;

	@RecordField(comment="")
	private final Set<PubMedID> referencePMIDs;

	@RecordField(comment="7.  Evidence One of either EXP, IMP, IC, IGI, IPI, ISS, IDA, IEP, IEA, TAS, NAS, NR, ND or RCA.  Example: TAS")
	private final String goEvidenceCode;

	// TODO: correct?
	@RecordField(comment="8.  With An additional identifier to support annotations using certain evidence codes (including IEA, IPI, IGI, IMP, IC and ISS evidences).  Examples: UniProtKB:O00341 InterPro:IPROO1878 RGD:123456 CHEBI:12345 Ensembl:ENSG00000136141 GO:0000001 EC:3.1.22.1")
	private final String inferredFrom;

	@RecordField(comment="9.  Aspect One of the three ontologies, corresponding to the GO identifier applied.  P (biological process), F (molecular function) or C (cellular component).  Example: P")
	private final GeneOntologyDomain ontology;

	@RecordField(comment="")
	private static Map<String, GeneOntologyDomain> ontologySymbol2NameMap;
	static {
		ontologySymbol2NameMap = new HashMap<String, GeneOntologyDomain>();
		ontologySymbol2NameMap.put("P", GeneOntologyDomain.BIOLOGICAL_PROCESS);
		ontologySymbol2NameMap.put("F", GeneOntologyDomain.MOLECULAR_FUNCTION);
		ontologySymbol2NameMap.put("C", GeneOntologyDomain.CELLULAR_COMPONENT);
	}

	@RecordField(comment="10. DB_Object_Name Name of protein The full UniProt protein name will be present here, if available from UniProtKB. If a name cannot be added, this field will be left empty.  Examples: Glucose-6-phosphatase Cellular tumor antigen p53 Coatomer subunit beta")
	private final String markerName;

	@RecordField(comment="11. Synonym Gene_symbol [or other text] Alternative gene symbol(s), IPI identifier(s) and UniProtKB/Swiss-Prot identifiers are provided pipe-separated, if available from UniProtKB. If none of these identifiers have been supplied, the field will be left empty.  Example:  RNF20|BRE1A|IPI00690596|BRE1A_BOVIN IPI00706050 MMP-16|IPI00689864 ")
	private final Set<String> markerSynonyms;

	@RecordField(comment="12. DB_Object_Type What kind of entity is being annotated.  Here: protein (or protein_structure for the gene_association.goa_pdb file).  Example: protein")
	private final MarkerType markerType;

	@RecordField(comment="13. Taxon_ID Identifier for the species being annotated.  Example: taxon:9606")
	private final NcbiTaxonomyID taxonID;

	@RecordField(comment="14. Date The date of last annotation update in the format 'YYYYMMDD' Example: 20050101 ")
	private final String modificationDate;

	@RecordField(comment="15. Assigned_By Attribute describing the source of the annotation.  One of either UniProtKB, AgBase, BHF-UCL, CGD, DictyBase, EcoCyc, EcoWiki, Ensembl, FlyBase, GDB, GeneDB_Spombe,GeneDB_Pfal, GOC, GR (Gramene), HGNC, Human Protein Atlas, JCVI, IntAct, InterPro, LIFEdb, PAMGO_GAT, MGI, Reactome, RGD, Roslin Institute, SGD, TAIR, TIGR, ZFIN, PINC (Proteome Inc.) or WormBase.  Example: UniProtKB")
	private final String assignedBy;


	public GeneAssociationFileData(String databaseDesignation, 
			DataSourceIdentifier<?> geneID,
			String geneSymbol, 
			String notDesignation, 
			GeneOntologyID goTermID,
			Set<MgiReferenceID> referenceAccessionIDs, 
			Set<PubMedID> referencePMIDs,
			String goEvidenceCode, 
			String inferredFrom, 
			GeneOntologyDomain ontology,
			String markerName, 
			Set<String> markerSynonyms, 
			MarkerType markerType,
			NcbiTaxonomyID taxonID, 
			String modificationDate, 
			String assignedBy,
			long byteOffset, long lineNumber) {

		super(byteOffset, lineNumber);

		this.databaseDesignation = databaseDesignation;
		this.geneID = geneID;
		this.geneSymbol = geneSymbol;
		this.notDesignation = notDesignation;
		this.goTermID = goTermID;
		this.referenceAccessionIDs = referenceAccessionIDs;
		this.referencePMIDs = referencePMIDs;
		this.goEvidenceCode = goEvidenceCode;
		this.inferredFrom = inferredFrom;
		this.ontology = ontology;
		this.markerName = markerName;
		this.markerSynonyms = markerSynonyms;
		this.markerType = markerType;
		this.taxonID = taxonID;
		this.modificationDate = modificationDate;
		this.assignedBy = assignedBy;
	}

	public String getDatabaseDesignation() {
		return databaseDesignation;
	}

	public DataSourceIdentifier<?> getGeneID() {
		return geneID;
	}

	public String getGeneSymbol() {
		return geneSymbol;
	}

	public String getAnnotationQualifier() {
		return notDesignation;
	}

	public GeneOntologyID getGoTermID() {
		return goTermID;
	}

	public Set<MgiReferenceID> getReferenceAccessionIDs() {
		return referenceAccessionIDs;
	}

	public Set<PubMedID> getReferencePMIDs() {
		return referencePMIDs;
	}

	public String getGoEvidenceCode() {
		return goEvidenceCode;
	}

	public String getInferredFrom() {
		return inferredFrom;
	}

	public GeneOntologyDomain getOntology() {
		return ontology;
	}

	public String getMarkerName() {
		return markerName;
	}

	public Set<String> getMarkerSynonyms() {
		return markerSynonyms;
	}

	public MarkerType getMarkerType() {
		return markerType;
	}

	public NcbiTaxonomyID getTaxonID() {
		return taxonID;
	}

	public String getModificationDate() {
		return modificationDate;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public static GeneAssociationFileData parseLineFromFile(Line line) {
		if (line.getText().startsWith("!")) {
			logger.info("No relevant information to parse on line: " + line);
			return null;
		}

		String[] toks = line.getText().split("\\t");

		String databaseDesignation = new String(toks[0]);
		DataSourceIdentifier<?> geneID = DataSourceIdResolver.resolveId(databaseDesignation, toks[1], "Source: " + toks[0] + " ID: " + toks[1]);
		String markerSymbol = new String(toks[2]);

		String qualifier = null;
		if (!toks[3].isEmpty())
			qualifier = new String(toks[3]);

		GeneOntologyID goTermID = new GeneOntologyID(toks[4]);
		Set<MgiReferenceID> mgiReferenceAccessionIDs = extractMgiReferenceIDs(toks[5]);
		Set<PubMedID> pubmedIDs = extractPubMedIDs(toks[5], line.getText());

		String goEvidenceCode = new String(toks[6]);
		String inferredFrom = !toks[7].isEmpty() ? toks[7] : null;

		GeneOntologyDomain ontology = ontologySymbol2NameMap.get(toks[8].trim());
		if (ontology == null) {
			logger.error(String.format("Unexpected ontology symbol: %s in line: %s", toks[8], line.getText()));
			return null;
		}

		String markerName = new String(toks[9]);
		Set<String> markerSynonyms = new HashSet<String>();
		if (toks[10].trim().length() > 0) {
			String[] synonyms = toks[10].split("\\|");
			for (String synonym : synonyms) {
				markerSynonyms.add(new String(synonym));
			}
		}

		MarkerType markerType = MarkerType.valueOf(toks[11].toUpperCase());
		String taxonIDs = toks[12].substring(6);
		if (taxonIDs.contains("|")) {
			taxonIDs = taxonIDs.substring(0, taxonIDs.indexOf("|"));
		}
		NcbiTaxonomyID taxonID = new NcbiTaxonomyID(taxonIDs);
		String modificationDate = toks[13];
		String assignedBy = new String(toks[14]);

		return new GeneAssociationFileData(databaseDesignation, geneID, markerSymbol, 
		qualifier, goTermID, mgiReferenceAccessionIDs, pubmedIDs, goEvidenceCode, 
		inferredFrom, ontology, markerName, markerSynonyms, markerType, taxonID, 
		modificationDate, assignedBy, line.getByteOffset(), line.getLineNumber());

	}

	/**
	 * Parses the reference pmid from the column containing the MGI Reference Accession ID and
	 * PubMed IDs
	 * 
	 * @param referenceAccessionID
	 * @return
	 */
	private static Set<PubMedID> extractPubMedIDs(String referenceStr, String lineText) {
		Set<PubMedID> pmids = new HashSet<PubMedID>();
		Pattern p = Pattern.compile("PMID:\\d+");
		Matcher m = p.matcher(referenceStr);
		while (m.find()) {
			PubMedID pmid;
			try {
				pmid = new PubMedID(m.group());
				pmids.add(pmid);
			} catch (IllegalArgumentException iea) {
				logger.warn(String.format(
					"Detected Invalid PubMedID (%s) in gene association file line: %s",
					m.group(), lineText));
			}
		}
		return pmids;
	}

	/**
	 * Parses the mgi reference id from the column containing the MGI Reference Accession ID and
	 * PubMed IDs
	 * 
	 * @param referenceAccessionID
	 * @return
	 */
	private static Set<MgiReferenceID> extractMgiReferenceIDs(String referenceStr) {
		Set<MgiReferenceID> pmids = new HashSet<MgiReferenceID>();
		Pattern p = Pattern.compile("MGI:MGI:\\d+");
		Matcher m = p.matcher(referenceStr);
		while (m.find()) {
			pmids.add(new MgiReferenceID(m.group()));
		}
		return pmids;
	}

}
