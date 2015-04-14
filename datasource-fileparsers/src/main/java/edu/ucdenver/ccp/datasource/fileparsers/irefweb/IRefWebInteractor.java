package edu.ucdenver.ccp.fileparsers.irefweb;

import java.util.Set;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.irefweb.CrogId;
import edu.ucdenver.ccp.datasource.identifiers.irefweb.IcrogId;
import edu.ucdenver.ccp.datasource.identifiers.irefweb.IrogId;
import edu.ucdenver.ccp.datasource.identifiers.irefweb.RogId;

/**
 * comments from: http://irefindex.uio.no/wiki/README_MITAB2.6_for_iRefIndex
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Data
@Record(dataSource = DataSource.IREFWEB, label="interactor")
public class IRefWebInteractor implements DataRecord {

	@RecordField(comment = "Description:	Unique identifier for interactor A.\nExample: uniprotkb:P23367\nNotes: This column contains an identifier, taken from a major database, for a protein representing the interactor A. A UniProt or a RefSeq accession is provided (in that order of preference) wherever possible. See column 3 for a list of prefixes that may be employed in this column in addition to the following:\ncomplex - If interactor A is being used to represent a complex, then the rogid for the complex will be listed here, such as the following: complex:xBr9cTXgzPLNxsaKiYyHcoEm/DM\nIn rare cases, a rogid may appear here if a protein interactor has a sequence but no known, valid database:accession pair.")
	private final DataSourceIdentifier<?> uniqueId;
	@RecordField(comment = "Description:	Alternative identifiers for interactor A\nExample: uniprotkb:P23367|refseq:NP_418591|entrezgene/locuslink:948691|rogid:hhZYhMtr5JC1lGIKtR1wxHAd3JY83333|irogid:12345\nNotes: All database:accession pairs listed in Column 3 point to protein records that describe the exact same sequence from the same taxon.\nEach pipe-delimited entry is a database_name:accession pair delimited by a colon. Database names are taken from the MI controlled vocabulary at the following location: http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI\nDatabase references listed in this column may include the following:\nuniprotkb - The accessions this protein is known by in UniProt (http://www.uniprot.org/). More information regarding this protein can be retrieved using this accession from UniProt. See the AC line in the flat file. http://au.expasy.org/sprot/userman.html#AC_line.\nrefseq - If a protein accession exists in the RefSeq data base (http://www.ncbi.nlm.nih.gov/RefSeq/) that reference is indicated here. More information about this protein can be obtained from RefSeq using this accession.\nentrezgene/locuslink - NCBI gene Identifiers for the gene encoding this protein. See ftp://ftp.ncbi.nlm.nih.gov/gene/DATA/gene2refseq column GeneID given protein's accession.version\nother - If none of the three identifier types are available then other databasename:accession pairs will be listed. These database names may not follow the MI controlled vocabulary. Example: emb:CAA44868.1|gb:AAA23715.1|gb:AAB02995.1|emb:CAA56736.1|uniprot:P24991\nrogid - Column 33 repeated here for convenience.\nirogid - Column 43 repeated here for convenience.\nNote: The rogid of a complex or a n-ary interaction is the rigid of that interaction. However, the irogid of the complex is not the irigid. The irogid for the complex is an integer and it is non-overlapping with any protein irogids")
	private final Set<DataSourceIdentifier<?>> alternateIds;
	@RecordField(comment = "The alias column contains both identifiers and gene symbols, so we represent the values from this column in two variables, one for the symbols and one for the identifiers. Description: Aliases for interactor A\nExample: uniprotkb:MUTL_ECOLI|entrezgene/locuslink:mutL|crogid:hhZYhMtr5JC1lGIKtR1wxHAd3JY83333|icrogid:12345\nNotes: Each pipe-delimited entry is a database name:alias pair delimited by a colon. Database names are taken from the PSI-MI controlled vocabulary at the following location: http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI\nDatabase names and sources listed in this column may include the following:\nuniprotkb:entry name - the entry name given by UniProt. See the description for \"Entry name\" in the section of http://au.expasy.org/sprot/userman.html#ID_line concerning the \"ID (IDentification)\" line of the flat file\nentrezgene/locuslink:symbol - the NCBI gene symbol for the gene encoding this protein. See the section in ftp://ftp.ncbi.nlm.nih.gov/gene/README for gene_info, specifically details for the Symbol column\ncrogid - Column 46 repeated here for convenience.\nicrogid - Column 49 repeated here for convenience.\nother db:accession pairs - Other db:accession pairs may be added (after icrogid) that all belong to the same canonical group. These are purely meant to facilitate look-up by PSICQUIC and other services - these sequences are related (but not identical) with interactor A sequence.\nNA - NA may be listed here if aliases are not available")
	private final Set<String> aliasSymbols;
	private final Set<DataSourceIdentifier<?>> aliasIds;
	@RecordField
	private final IRefWebInteractorOrganism ncbiTaxonomyId;
	@RecordField(comment = "Not used")
	private final Set<DataSourceIdentifier<?>> dbXReferenceIDs;
	@RecordField(comment = "Description: Biological role of interactor\nNotes:When provided by the source database, this includes single entries such as MI:0501(enzyme), MI:0502(enzyme target), MI:0580(electron acceptor), or MI:0499(unspecified role).\nSee http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI to browse possible values for biological role.\nFor complexes and when no role is explicitly specified this column will contain the following: MI:0000(unspecified)")
	private final IRefWebInteractorBiologicalRole biologicalRole;
	@RecordField(comment = "Description: Indicates the experimental role of the interactor (such as bait or prey).\nExample: MI:0496(bait)\nExample: MI:0498(prey)\nNotes: This column indicates the experimental role (if any was provided by the source database) that was played by this interactor.\nSee http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI to see definitions of bait and prey. as well as browse other possible values of experimental role that may appear in this column for other databases.\nFor complexes and when no role is explicitly specified this column will contain the following: MI:0000(unspecified)")
	private final IRefWebInteractorExperimentalRole experimentalRole;
	@RecordField(comment = "refers to the interactor's molecule type")
	private final IRefWebInteractorType interactorType;
	@RecordField(comment = "Not used")
	private final String annotations;
	@RecordField(comment = "Hash key for the interactor.\nExample: rogid:hhZYhMtr5JC1lGIKtR1wxHAd3JY83333\nNotes: This column contains a universal key for this interactor.\nThis column may be used to identify other interactors in this file that have the exact same amino acid sequence and taxon id.\nThis universal key listed here is the ROGID (redundant object group identifier) described in the original iRefIndex paper, PMID 18823568.\nColumn 3 lists database names and accessions that all have this same key.\nThe ROGID for proteins, consists of the base-64 version of the SHA-1 key for the protein sequence concatenated with the taxonomy identifier for the protein. For complex nodes, the ROGID is calculated as the SHA-1 digest of the ROGIDs of all the protein participants (after first ordering them by ASCII-based lexicographical sorting in ascending order and concatenating them) See the iRefIndex paper for details. The SHA-1 key is always 27 characters long. So the ROGID will be composed of 27 characters concatenated with a taxonomy identifier for proteins.")
	private final RogId checksum;
	@RecordField(comment = "Description: Database name and reference used in the original interaction record to describe interactor A\nExample: uniprotkb:P23367\nNotes:This is the protein reference that was found in the original interaction record to describe interactor A. It is a colon-delimited pair of database name and accession. It may be either the primary or secondary reference for the protein provided by the source database. For complexes this will be the ROGID of the complex.")
	private final DataSourceIdentifier<?> originalReference;
	@RecordField(comment = "Database name and reference used by iRefIndex to describe this interactor\nExample: uniprotkb:P23367\nNotes:\nColumn 37 (OriginalReferenceA) was used by the iRefIndex consolidation process to arrive at this FinalReferenceA. This database name and accession pair will usually be the same as that listed in column 37, unless the provided reference was malformed, had to be updated or was ambiguous.\nExamples:\nThe original reference is malformed. For example: RefSeq:NP 036076 instead of RefSeq:NP_036076.\nThe original reference is incomplete. For example: PDB:1KQ1| (missing chain information).\nThe original reference is deprecated. For example: UniProt:Q9H233 (the value of FinalReferenceA will be the latest available accession in this case).\nThe original reference is ambiguous. For example: a gene identifier is provided (the value of FinalReferenceA will be a protein product selected in a systematic way in this case).")
	private final DataSourceIdentifier<?> finalReference;
	@RecordField(comment = "String describing operations performed by iRefIndex procedure during mapping from original protein reference (columns 37) to the final protein reference (columns 39).")
	private final String mappingScore;
	@RecordField(comment = "Description: Integer ROGID for interactor A.\nNotes: This is an internal, integer-equivalent of the alphanumeric identifier in column 33 for interactor A. All interactors with the same sequence and taxon origin will have the same irogid. The identifier listed here is stable from one release of iRefIndex to another starting from release 6.0.")
	private final IrogId irogid;
	@RecordField(comment = "Description: Alphanumeric ROGID for the canonical group to which interactor A belongs.\nExample: hhZYhMtr5JC1lGIKtR1wxHAd3JY83333\nNotes: This column may be used to identify other interactors in this file that all belong to the same canonical group. Members of a canonical group may include splice isoform products from the same or related genes. Members of a canonical group do not all necessarily have the same sequence (although they all belong to the same taxon). One member of the canonical group is chosen to represent the entire group. The identifier for that canonical representative is listed in this column.\nSee http://irefindex.uio.no/wiki/Canonicalization for a description of canonicalization.")
	private final CrogId crogid;
	@RecordField(comment = "Integer ROGID for the canonical group to which interactor A belongs.\nNotes: This is an internal, integer-equivalent of the alphanumeric canonical ROGID in column 46 for interactor A. Interactors with the same icrogid may have different sequences but are related; e.g. different splice isoforms of the same gene. The identifier listed here is stable from one release of iRefIndex to another starting from release 6.0.")
	private final IcrogId icrogid;

}
