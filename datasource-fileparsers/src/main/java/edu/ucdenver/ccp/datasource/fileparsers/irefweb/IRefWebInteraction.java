package edu.ucdenver.ccp.datasource.fileparsers.irefweb;

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

import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebCrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIcrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebRigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ImexId;
import lombok.Data;

@Data
@Record(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD, dataSource = DataSource.IREFWEB)
public class IRefWebInteraction implements DataRecord {

	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___DETECTION_METHOD_FIELD_VALUE, comment = "Description: Interaction detection method\nExample: MI:0039(2h fragment pooling)\nNotes: Only a single method will appear in this column. Previously, multiple methods appeared.\nBoth the controlled vocabulary term identifier for the method (e.g. MI:0399) and the controlled vocabulary term short label in brackets (e.g. 2h fragment pooling) will appear in this column. See http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI to look up controlled vocabulary term identifiers.\nThe interaction detection method is from the original record. \nIf a controlled vocabulary term identifier was not provided by the source database then an attempt was made to use the supplied short label to find the correct term identifier. If a term identifier could not be found, then MI:0000 will appear before the shortLabels.\nNA or -1 may appear in place of a recognised shortLabel.\nFor example:\nMI:0000(-1)\nMI:0000(NA)")
	private final IRefWebInteractionDetectionMethod detectionMethod;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___AUTHOR_FIELD_VALUE, comment = "Notes: According to MITAB2.6 format this column should contain a pipe-delimited list of author surnames in which the interaction has been shown.\nThis column will usually include only one author name reference. However, some experimental evidences have secondary references which could be included here. This filed also includes references which are not author names as in the following examples:\nOPHID Predicted Protein Interaction\nHPRD Text Mining Confirmation\nMINT Text Mining Confirmation")
	private final String author;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE, comment = "Notes: This is a non-redundant list of PubMed identifiers pointing to literature that supports the interaction. According to MITAB2.6 format, this column should contain a pipe-delimited set of databaseName:identifier pairs such as pubmed:12345. The source database name is always pubmed.")
	private final Set<DataSourceIdentifier<?>> pmids;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___INTERACTION_TYPE_FIELD_VALUE)
	private final IRefWebInteractionType interactionType;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___INTERACTION_DATABASE_IDENTIFIER_FIELD_VALUE, comment = "source interaction-database and accessions.\nExample: intact:EBI-761694|rigid:3ERiFkUFsm7ZUHIRJTx8ZlHILRA|irigid:1234|edgetype:X\nNotes: Each reference is presented as a database name:identifier pair.\nChange: The source database is listed first. Additional information is pipe-delimited and presented here for the convenience of PSICQUIC web-service users (these services presently truncate this file at column 15 as they only support MITAB2.5). See columns 35,45,53.\nThe source database names that appear in this column are taken from the PSI-MI controlled vocabulary at the following location (where possible): http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI\nIf an interaction record identifier is not provided by the source database, this entry will appear as database-name:- with the identifier region replaced with a dash (-).")
	private final Set<DataSourceIdentifier<?>> interactionDbIds;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___CONFIDENCE_SCORE_FIELD_VALUE, comment = "Description: Confidence scores. \nExample: lpr:1|hpr:12|np:1|PSICQUIC entries are truncated here.  See irefindex.uio.no\nNotes: Each reference is presented as a scoreName:score pair. Three confidence scores are provided: lpr, hpr and np.\nPubMed Identifiers (PMIDs) point to literature references that support an interaction. A PMID may be used to support more than one interaction.\nThe lpr score (lowest PMID re-use) is the lowest number of distinct interactions (RIGIDs: see column 35) that any one PMID (supporting the interaction in this row) is used to support. A value of one indicates that at least one of the PMIDs supporting this interaction has never been used to support any other interaction. This likely indicates that only one interaction was described by that reference and that the present interaction is not derived from high throughput methods.\nThe hpr score (highest PMID re-use) is the highest number of interactions (RIGIDs: see column 35) that any one PMID (supporting the interaction in this row) is used to support. A high value (e.g. greater than 50) indicates that one PMID describes at least 50 other interactions and it is more likely that high-throughput methods were used.\nThe np score (number PMIDs) is the total number of unique PMIDs used to support the interaction described in this row.\n- may appear in the score field, indicating the absence of a score value.")
	private final Set<String> confidence;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___EXPANSION_FIELD_VALUE, comment = "Description: Model used to convert n-ary data into binary data for purpose of export in MITAB file\nNotes: For iRefIndex, this column will always contain either bipartite or none.")
	private final String expansion;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___CROSS_REFERENCE_INTERACTION_FIELD_VALUE, comment = "Not used")
	private final String xrefsInteraction;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___ANNOTATION_INTERACTION_FIELD_VALUE, comment = "Not used")
	private final String annotationsInteraction;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___HOST_ORGANISM_TAXONOMY_IDENTIFIER_FIELD_VALUE, comment = "Description: The taxonomy identifier of the host organism where the interaction was experimentally demonstrated. Example: taxid:10090(Mus musculus)\nNotes: This may differ from the taxonomy identifier associated with the interactors. Other possible entries are:\ntaxid:-1(in vitro)\ntaxid:-4(in vivo)\nA dash (-) will be used when no information about the host organism is available.\ntaxid:32644(unidentified) will be used when the source specifies the host organism taxonomy identifier as 32644.")
	private final IRefWebHostOrganism hostOrgTaxonomyId;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___PARAMETERS_INTERACTION_FIELD_VALUE, comment = "Not used")
	private final String parametersInteraction;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___CHECK_SUM_INTERACTION_FIELD_VALUE, comment = "Hash key for this interaction. Example: rigid:3ERiFkUFsm7ZUHIRJTx8ZlHILRA\nNotes: This column may be used to identify other rows (interaction records) in this file that describe interactions between the same set of proteins from the same taxon id. This universal key listed here is the RIGID (redundant interaction group identifier) described in the original iRefIndex paper, PMID 18823568. The RIGID consists of the ROG identifiers for each of the protein participants (see notes above) ordered by ASCII-based lexicographic sorting in ascending order, concatenated and then digested with the SHA-1 algorithm. See the iRefIndex paper for details. This identifier points to a set of redundant protein-protein interactions that involve the same set of proteins with the exact same primary sequences.")
	private final IRefWebRigId checksumInteraction;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___NEGATIVE_FIELD_VALUE, comment = "Does the interaction record provide evidence that some interaction does NOT occur.\nNotes: This value will be false for all lines in this file since iRefIndex does not include \"negative\" interactions from any of the source databases.")
	private final boolean negative;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___INTEGER_RIG_IDENTIFIER_FIELD_VALUE, comment = "Description:	Integer RIGID for this interaction. Example: 1234\nNotes: This is an internal, integer-equivalent of the alphanumeric identifier in column 35 for this interaction. All interactions involving the same interactors (same sequence and same taxon) will have the same irigid. The identifier listed here is stable from one release of iRefIndex to another starting from release 6.0.")
	private final IRefWebIrigId irigid;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___ALPHANUMERIC_RIG_IDENTIFIER_FIELD_VALUE, comment = "Alphanumeric RIGID for the canonical group to which this interaction belongs. Example: 3ERiFkUFsm7ZUHIRJTx8ZlHILRA\nNotes: This is the RIGID for this interaction calculated using the canonical ROGIDs (preceding two columns). This column may be used to identify other interactions in this file that all belong to the same canonical group.")
	private final IRefWebCrigId crigid;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___ICRIG_IDENTIFIER_FIELD_VALUE, comment = "Description: Integer RIGID for the canonical group to which this interaction belongs.\nNotes: This is an internal, integer-equivalent of the canonical RIGID. See column 48. This identifier serves to group together evidence for interactions that involve the same set (or a related set) of proteins.\nStarting with release 6.0, this canonical RIGID is stable from one release of iRefIndex to another.")
	private final IRefWebIcrigId icrigid;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___IMEX_IDENTIFIER_FIELD_VALUE)
	private final ImexId imexId;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___EDGE_TYPE_FIELD_VALUE, comment = "Description: Does the edge represent a binary interaction (X), member of complex (C) data, or a multimer (Y)?\nNotes: Edges can be labelled as either X, C or Y:\nX - a binary interaction with two protein participants\nC - denotes that this edge is a binary expansion of interaction record that had 3 or more interactors (so-called \"complex\" or \"n-ary\" data). The expansion type is described in column 16 (expansion). In the case of iRefIndex, the expansion is always \"bipartite\" meaning that Interactor A of this row represents the complex itself and Interactor B represents a protein that is a member of this group.\nY - for dimers and polymers. In case of dimers and polymers when the number of subunits is not described in the original interaction record, the edge is labelled with a Y. Interactor A will be identical to the Interactor B. The graphical representation of this will appear as a single node connected to itself (loop). The actual number of self-interacting subunits may be 2 (dimer) or more (say 5 for a pentamer). Refer to the original interaction record for more details and see column 54.")
	private final String edgeType;
	@RecordField(ontClass = CcpExtensionOntology.IREFWEB_INTERACTION_RECORD___NUMBER_OF_PARTICIPANTS_FIELD_VALUE, comment = "Description: Number of participants in the interaction\nNotes: For edges labelled X (see column 53) this value will be two.\nFor edges labelled C, this value will be equivalent to the number of protein interactors in the original n-ary interaction record.\nFor interactions labelled Y, this value will either be the number of self-interacting subunits (if present in the original interaction record) or 1 where the exact number of subunits is unknown or unspecified.\nImportant: The number of participants can be greater than the number of distinct proteins involved in an interaction because a single protein can participate more than once in an interaction. Such participation is enumerated and counted to produce the value in this column.")
	private final int numParticipants;

	

}
