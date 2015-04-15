/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.apache.log4j.Logger;
import org.uniprot.DbReferenceType;
import org.uniprot.Entry;
import org.uniprot.OrganismType;

import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.UniProtFileRecord.DbReference;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.UniProtFileRecord.Organism;

/**
 * Useful for parsing trembl and ignoring most of it
 * ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/docs/dbxref.
 * txt
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Getter
@Record(dataSource = DataSource.UNIPROT, label = "uniprot record")
public class SparseUniProtFileRecord extends FileRecord {

	private static final Logger logger = Logger.getLogger(SparseUniProtFileRecord.class);

	@RecordField(isKeyField = true)
	private final UniProtID primaryAccession;
	@RecordField
	private final List<UniProtID> accession;
	@RecordField
	private final List<String> name;
	// @RecordField
	// private final Protein protein;
	// @RecordField
	// private final List<Gene> gene;
	@RecordField
	private final Organism organism;
	@RecordField
	private final List<Organism> organismHost;
	// @RecordField
	// private final List<GeneLocation> geneLocation;
	// @RecordField
	// private final List<Reference> reference;
	// @RecordField
	// private final List<Comment> comment;
	@RecordField
	private final List<DbReference> dbReference;

	// @RecordField
	// private final ProteinExistence proteinExistence;
	// @RecordField
	// private final List<Keyword> keyword;
	// @RecordField
	// private final List<Feature> feature;
	// @RecordField
	// private final List<Evidence> evidence;
	// @RecordField
	// private final Sequence sequence;
	// @RecordField
	// private final String dataset;
	// @RecordField
	// private final XMLGregorianCalendar created;
	// @RecordField
	// private final XMLGregorianCalendar modified;
	// @RecordField
	// private final int version;

	/**
	 * @param byteOffset
	 */
	public SparseUniProtFileRecord(Entry xmlType) {
		super(-1); // b/c this data is coming from XML there's no easy way to track the byte offset
		this.accession = new ArrayList<UniProtID>();
		for (String idStr : xmlType.getAccession()) {
			this.accession.add(new UniProtID(idStr));
		}
		this.primaryAccession = this.accession.get(0);
		this.name = new ArrayList<String>(xmlType.getName());

		this.organism = new Organism(xmlType.getOrganism());
		this.organismHost = new ArrayList<Organism>();
		if (xmlType.getOrganismHost() != null) {
			for (OrganismType org : xmlType.getOrganismHost()) {
				this.organismHost.add(new Organism(org));
			}
		}

		this.dbReference = new ArrayList<DbReference>();
		if (xmlType.getDbReference() != null) {
			for (DbReferenceType ref : xmlType.getDbReference()) {
				this.dbReference.add(new DbReference(ref));
			}
		}
	}

	/**
	 * @param byteOffset
	 * @param primaryAccession
	 * @param accession
	 * @param name
	 * @param organism
	 * @param organismHost
	 * @param dbReference
	 */
	public SparseUniProtFileRecord(UniProtID primaryAccession, List<UniProtID> accession, List<String> name,
			Organism organism, List<Organism> organismHost, List<DbReference> dbReference, long byteOffset) {
		super(byteOffset);
		this.primaryAccession = primaryAccession;
		this.accession = accession;
		this.name = name;
		this.organism = organism;
		this.organismHost = organismHost;
		this.dbReference = dbReference;
	}

}
