/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Getter;

import org.apache.log4j.Logger;
import org.uniprot.CitationType;
import org.uniprot.CommentType;
import org.uniprot.ConsortiumType;
import org.uniprot.DbReferenceType;
import org.uniprot.Entry;
import org.uniprot.EventType;
import org.uniprot.EvidenceType;
import org.uniprot.EvidencedStringType;
import org.uniprot.FeatureType;
import org.uniprot.GeneLocationType;
import org.uniprot.GeneNameType;
import org.uniprot.ImportedFromType;
import org.uniprot.InteractantType;
import org.uniprot.IsoformType;
import org.uniprot.KeywordType;
import org.uniprot.LocationType;
import org.uniprot.OrganismNameType;
import org.uniprot.OrganismType;
import org.uniprot.PersonType;
import org.uniprot.PositionType;
import org.uniprot.PropertyType;
import org.uniprot.ProteinExistenceType;
import org.uniprot.ProteinType;
import org.uniprot.ReferenceType;
import org.uniprot.SequenceType;
import org.uniprot.SourceDataType;
import org.uniprot.SourceType;
import org.uniprot.StatusType;
import org.uniprot.SubcellularLocationType;

import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.dip.DipInteractorID;
import edu.ucdenver.ccp.datasource.identifiers.drugbank.DrugBankID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.intact.IntActID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.Gene3dID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PantherID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PfamID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PirID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PirSfID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PrintsID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.ProDomID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PrositeID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.SmartID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.TigrFamsID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.ipi.IpiID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.PirnrId;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.PirsrId;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.RuleBaseId;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.SaasId;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtIsoformID;
import edu.ucdenver.ccp.datasource.identifiers.ec.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.flybase.FlyBaseID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggGeneID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.mint.MintID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.other.*;
import edu.ucdenver.ccp.datasource.identifiers.pdb.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbID;
import edu.ucdenver.ccp.datasource.identifiers.reactome.ReactomeReactionID;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.sgd.SgdID;
import edu.ucdenver.ccp.datasource.identifiers.wormbase.WormBaseID;
import edu.ucdenver.ccp.identifier.publication.DOI;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * 
 * ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/
 * complete/docs/dbxref. txt
 * 
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
@Getter
@Record(dataSource = DataSource.UNIPROT, label = "uniprot record")
public class UniProtFileRecord extends FileRecord {

	private static final Logger logger = Logger.getLogger(UniProtFileRecord.class);

	@RecordField
	private final UniProtID primaryAccession;
	@RecordField
	private final List<UniProtID> accession;
	@RecordField
	private final List<String> name;
	@RecordField
	private final Protein protein;
	@RecordField
	private final List<GeneType> gene;
	@RecordField
	private final Organism organism;
	@RecordField
	private final List<Organism> organismHost;
	@RecordField
	private final List<GeneLocation> geneLocation;
	@RecordField
	private final List<Reference> reference;
	@RecordField
	private final List<Comment> comment;
	@RecordField
	private final List<DbReference> dbReference;
	@RecordField
	private final ProteinExistence proteinExistence;
	@RecordField
	private final List<Keyword> keyword;
	@RecordField
	private final List<Feature> feature;
	@RecordField
	private final List<Evidence> evidence;
	@RecordField
	private final Sequence sequence;
	@RecordField
	private final String dataset;
	@RecordField
	private final XMLGregorianCalendar created;
	@RecordField
	private final XMLGregorianCalendar modified;
	@RecordField
	private final int version;

	/**
	 * @param byteOffset
	 */
	public UniProtFileRecord(Entry xmlType) {
		super(-1); // b/c this data is coming from XML there's no easy way to
					// track the byte offset
		this.accession = new ArrayList<UniProtID>();
		for (String idStr : xmlType.getAccession()) {
			this.accession.add(new UniProtID(idStr));
		}
		this.primaryAccession = this.accession.get(0);
		this.name = new ArrayList<String>(xmlType.getName());
		this.protein = (xmlType.getProtein() == null) ? null : new Protein(xmlType.getProtein());
		this.gene = new ArrayList<GeneType>();
		if (xmlType.getGene() != null) {
			for (org.uniprot.GeneType g : xmlType.getGene()) {
				this.gene.add(new GeneType(g));
			}
		}
		this.organism = new Organism(xmlType.getOrganism());
		this.organismHost = new ArrayList<Organism>();
		if (xmlType.getOrganismHost() != null) {
			for (OrganismType org : xmlType.getOrganismHost()) {
				this.organismHost.add(new Organism(org));
			}
		}
		this.geneLocation = new ArrayList<GeneLocation>();
		if (xmlType.getGeneLocation() != null) {
			for (GeneLocationType loc : xmlType.getGeneLocation()) {
				this.geneLocation.add(new GeneLocation(loc));
			}
		}
		this.reference = new ArrayList<Reference>();
		if (xmlType.getReference() != null) {
			for (ReferenceType ref : xmlType.getReference()) {
				this.reference.add(new Reference(ref));
			}
		}
		this.comment = new ArrayList<Comment>();
		if (xmlType.getComment() != null) {
			for (CommentType ct : xmlType.getComment()) {
				this.comment.add(new Comment(ct));
			}
		}
		this.dbReference = new ArrayList<DbReference>();
		if (xmlType.getDbReference() != null) {
			for (DbReferenceType ref : xmlType.getDbReference()) {
				this.dbReference.add(new DbReference(ref));
			}
		}
		this.proteinExistence = new ProteinExistence(xmlType.getProteinExistence());
		this.keyword = new ArrayList<Keyword>();
		if (xmlType.getKeyword() != null) {
			for (KeywordType kw : xmlType.getKeyword()) {
				this.keyword.add(new Keyword(kw));
			}
		}
		this.feature = new ArrayList<Feature>();
		if (xmlType.getFeature() != null) {
			for (FeatureType ft : xmlType.getFeature()) {
				this.feature.add(new Feature(ft));
			}
		}
		this.evidence = new ArrayList<Evidence>();
		if (xmlType.getEvidence() != null) {
			for (EvidenceType et : xmlType.getEvidence()) {
				this.evidence.add(new Evidence(et));
			}
		}
		this.sequence = (xmlType.getSequence() == null) ? null : new Sequence(xmlType.getSequence());
		this.dataset = xmlType.getDataset();
		this.created = xmlType.getCreated();
		this.modified = xmlType.getModified();
		this.version = xmlType.getVersion();
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Citation {

		@RecordField
		private final String title;
		@RecordField
		private final List<Name> editorList;
		@RecordField
		private final List<Name> authorList;
		@RecordField
		private final String locator;
		@RecordField
		private final List<DbReference> dbReference;
		@RecordField
		private final String type;
		@RecordField
		private final String date;
		@RecordField
		private final String name;
		@RecordField
		private final String volume;
		@RecordField
		private final String first;
		@RecordField
		private final String last;
		@RecordField
		private final String publisher;
		@RecordField
		private final String city;
		@RecordField
		private final String db;
		@RecordField
		private final String number;
		@RecordField
		private final String institute;
		@RecordField
		private final String country;

		public Citation(CitationType xmlType) {
			this.title = xmlType.getTitle();
			this.editorList = new ArrayList<Name>();
			if (xmlType.getEditorList() != null) {
				for (Object obj : xmlType.getEditorList().getConsortiumOrPerson()) {
					this.editorList.add(new Name(obj));
				}
			}
			this.authorList = new ArrayList<Name>();
			if (xmlType.getAuthorList() != null) {
				for (Object obj : xmlType.getAuthorList().getConsortiumOrPerson()) {
					this.authorList.add(new Name(obj));
				}
			}
			this.locator = xmlType.getLocator();
			this.dbReference = new ArrayList<DbReference>();
			if (xmlType.getDbReference() != null) {
				for (DbReferenceType type : xmlType.getDbReference()) {
					this.dbReference.add(new DbReference(type));
				}
			}
			this.type = xmlType.getType();
			this.date = xmlType.getDate();
			this.name = xmlType.getName();
			this.volume = xmlType.getVolume();
			this.first = xmlType.getFirst();
			this.last = xmlType.getLast();
			this.publisher = xmlType.getPublisher();
			this.city = xmlType.getCity();
			this.db = xmlType.getDb();
			this.number = xmlType.getNumber();
			this.institute = xmlType.getInstitute();
			this.country = xmlType.getCountry();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Comment {

		@RecordField
		private final Comment.Absorption absorption;
		@RecordField
		private final Comment.Kinetics kinetics;
		@RecordField
		private final EvidencedString phDependence;
		@RecordField
		private final EvidencedString redoxPotential;
		@RecordField
		private final EvidencedString temperatureDependence;
		@RecordField
		private final MoleculeType molecule;
		@RecordField
		private final List<SubcellularLocation> subcellularLocation;
		@RecordField
		private final Comment.Conflict conflict;
		@RecordField
		private final List<Comment.Link> link;
		@RecordField
		private final List<Event> event;
		@RecordField
		private final List<Isoform> isoform;
		@RecordField
		private final List<Interactant> interactant;
		@RecordField
		private final Boolean organismsDiffer;
		@RecordField
		private final Integer experiments;
		@RecordField
		private final List<Location> location;
		@RecordField
		private final EvidencedString text;
		@RecordField
		private final String type;
		@RecordField
		private final String locationType;
		@RecordField
		private final String name;
		@RecordField
		private final Float mass;
		@RecordField
		private final String error;
		@RecordField
		private final String method;
		@RecordField
		private final List<Integer> evidence;

		public Comment(CommentType xmlType) {
			this.absorption = (xmlType.getAbsorption() == null) ? null
					: new Comment.Absorption(xmlType.getAbsorption());
			this.kinetics = (xmlType.getKinetics() == null) ? null : new Comment.Kinetics(xmlType.getKinetics());
			this.phDependence = EvidencedString.getInstance(xmlType.getPhDependence());
			this.redoxPotential = EvidencedString.getInstance(xmlType.getRedoxPotential());
			this.temperatureDependence = EvidencedString.getInstance(xmlType.getTemperatureDependence());
			this.molecule = (xmlType.getMolecule() == null) ? null : new MoleculeType(xmlType.getMolecule());
			this.subcellularLocation = new ArrayList<SubcellularLocation>();
			if (xmlType.getSubcellularLocation() != null) {
				for (SubcellularLocationType type : xmlType.getSubcellularLocation()) {
					this.subcellularLocation.add(new SubcellularLocation(type));
				}
			}
			this.conflict = (xmlType.getConflict() == null) ? null : new Comment.Conflict(xmlType.getConflict());
			this.link = new ArrayList<Comment.Link>();
			if (xmlType.getLink() != null) {
				for (CommentType.Link type : xmlType.getLink()) {
					this.link.add(new Comment.Link(type));
				}
			}
			this.event = new ArrayList<Event>();
			if (xmlType.getEvent() != null) {
				for (EventType type : xmlType.getEvent()) {
					this.event.add(new Event(type));
				}
			}
			this.isoform = new ArrayList<Isoform>();
			if (xmlType.getIsoform() != null) {
				for (IsoformType type : xmlType.getIsoform()) {
					this.isoform.add(new Isoform(type));
				}
			}
			this.interactant = new ArrayList<Interactant>();
			if (xmlType.getInteractant() != null) {
				for (InteractantType type : xmlType.getInteractant()) {
					this.interactant.add(new Interactant(type));
				}
			}
			this.organismsDiffer = xmlType.isOrganismsDiffer();
			this.experiments = xmlType.getExperiments();
			this.location = new ArrayList<Location>();
			if (xmlType.getLocation() != null) {
				for (LocationType type : xmlType.getLocation()) {
					this.location.add(new Location(type));
				}
			}
			this.text = EvidencedString.getInstance(xmlType.getText());
			this.type = xmlType.getType();
			this.locationType = xmlType.getLocationType();
			this.name = xmlType.getName();
			this.mass = xmlType.getMass();
			this.error = xmlType.getError();
			this.method = xmlType.getMethod();
			this.evidence = (xmlType.getEvidence() == null) ? null : new ArrayList<Integer>(xmlType.getEvidence());
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Absorption {

			@RecordField
			private final EvidencedString max;
			@RecordField
			private final EvidencedString text;

			public Absorption(CommentType.Absorption xmlType) {
				this.max = EvidencedString.getInstance(xmlType.getMax());
				this.text = EvidencedString.getInstance(xmlType.getText());
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Conflict {
			@RecordField
			private final Comment.Conflict.Sequence sequence;
			@RecordField
			private final String type;
			@RecordField
			private final String ref;

			public Conflict(CommentType.Conflict xmlType) {
				this.sequence = (xmlType.getSequence() == null) ? null : new Conflict.Sequence(xmlType.getSequence());
				this.type = xmlType.getType();
				this.ref = xmlType.getRef();
			}

			@Getter
			@Record(dataSource = DataSource.UNIPROT)
			public static class Sequence {

				@RecordField
				private final String resource;
				@RecordField
				private final String id;
				@RecordField
				private final Integer version;

				public Sequence(CommentType.Conflict.Sequence xmlType) {
					this.resource = xmlType.getResource();
					this.id = xmlType.getId();
					this.version = xmlType.getVersion();
				}
			}

		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Kinetics {

			@RecordField
			private final List<EvidencedString> km;
			@RecordField
			private final List<EvidencedString> vmax;
			@RecordField
			private final EvidencedString text;

			public Kinetics(CommentType.Kinetics xmlType) {
				this.km = new ArrayList<EvidencedString>();
				if (xmlType.getKM() != null) {
					for (EvidencedStringType type : xmlType.getKM()) {
						this.km.add(EvidencedString.getInstance(type));
					}
				}
				this.vmax = new ArrayList<EvidencedString>();
				if (xmlType.getVmax() != null) {
					for (EvidencedStringType type : xmlType.getVmax()) {
						this.vmax.add(EvidencedString.getInstance(type));
					}
				}
				this.text = EvidencedString.getInstance(xmlType.getText());
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Link {
			@RecordField
			private final String uri;

			public Link(CommentType.Link xmlType) {
				this.uri = xmlType.getUri();
			}
		}

	}

	// @Getter
	// @Record(dataSource = DataSource.UNIPROT)
	// public static class Consortium {
	//
	// @RecordField
	// private final String name;
	//
	// public Consortium(ConsortiumType xmlType) {
	// }
	// }

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class DbReference {

		@RecordField
		private final List<Property> property;
		@RecordField
		private final String type;
		@RecordField
		private final DataSourceIdentifier<?> id;
		@RecordField
		private final List<Integer> evidence;

		public DbReference(DbReferenceType xmlType) {
			this.property = new ArrayList<Property>();
			if (xmlType.getProperty() != null) {
				for (PropertyType type : xmlType.getProperty()) {
					this.property.add(new Property(type));
				}
			}
			this.type = xmlType.getType();
			this.evidence = (xmlType.getEvidence() == null) ? null : new ArrayList<Integer>(xmlType.getEvidence());
			this.id = resolveDatabaseIdentifer(xmlType.getType(), xmlType.getId());
		}

		/**
		 * See ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/
		 * knowledgebase/complete/docs /dbxref.txt for a list of databased
		 * referenced by UniProt
		 * 
		 * @param type
		 * @param id
		 * @return
		 */
		private DataSourceIdentifier<?> resolveDatabaseIdentifer(String type, String idStr) {
			try {
				if (type.equalsIgnoreCase("pubmed")) {
					return new PubMedID(idStr);
				} else if (type.equalsIgnoreCase("doi")) {
					return new DOI(idStr);
				} else if (type.equalsIgnoreCase("NCBI Taxonomy")) {
					return new NcbiTaxonomyID(idStr);
				} else if (type.equalsIgnoreCase("Allergome")) {
					return new AllergomeId(idStr);
				} else if (type.equalsIgnoreCase("ArachnoServer")) {
					return new ArachnoServerId(idStr);
				} else if (type.equalsIgnoreCase("ArrayExpress")) {
					return new ArrayExpressId(idStr);
				} else if (type.equalsIgnoreCase("Bgee")) {
					return new BgeeId(idStr);
				} else if (type.equalsIgnoreCase("BindingDB")) {
					return new BindingDbId(idStr);
				} else if (type.equalsIgnoreCase("BioCyc")) {
					return new BioCycId(idStr);
				} else if (type.equalsIgnoreCase("BRENDA")) {
					return new BrendaId(idStr);
				} else if (type.equalsIgnoreCase("CAZy")) {
					return new CAZyId(idStr);
				} else if (type.equalsIgnoreCase("CGD")) {
					return new CGDId(idStr);
				} else if (type.equalsIgnoreCase("ChEMBL")) {
					return new ChemblId(idStr);
				} else if (type.equalsIgnoreCase("ChiTaRS")) {
					return new ChiTaRSId(idStr);
				} else if (type.equalsIgnoreCase("CleanEx")) {
					return new CleanExId(idStr);
				} else if (type.equalsIgnoreCase("COMPLUYEAST-2DPAGE")) {
					return new COMPLUYEAST_2DPAGEId(idStr);
				} else if (type.equalsIgnoreCase("ConoServer")) {
					return new ConoServerId(idStr);
				} else if (type.equalsIgnoreCase("CTD")) {
					return new CtdId(idStr);
				} else if (type.equalsIgnoreCase("CYGD")) {
					return new CygdId(idStr);
				} else if (type.equalsIgnoreCase("dbSNP")) {
					return new DbSnpId(idStr);
				} else if (type.equalsIgnoreCase("DDBJ")) {
					return new DdbjId(idStr);
				} else if (type.equalsIgnoreCase("dictyBase")) {
					return new DictyBaseID(idStr);
				} else if (type.equalsIgnoreCase("DIP")) {
					return new DipInteractorID(idStr);
				} else if (type.equalsIgnoreCase("DisProt")) {
					return new DisProtId(idStr);
				} else if (type.equalsIgnoreCase("DMDM")) {
					return new DmdmId(idStr);
				} else if (type.equalsIgnoreCase("DNASU")) {
					return new DnasuId(idStr);
				} else if (type.equalsIgnoreCase("DOSAC-COBS-2DPAGE")) {
					return new DOSAC_COBS_2DPAGEId(idStr);
				} else if (type.equalsIgnoreCase("DrugBank")) {
					return new DrugBankID(idStr);
				} else if (type.equalsIgnoreCase("EchoBASE")) {
					return new EchoBaseId(idStr);
				} else if (type.equalsIgnoreCase("EcoGene")) {
					return new EcoGeneID(idStr);
				} else if (type.equalsIgnoreCase("eggNOG")) {
					return new EggNOGId(idStr);
				} else if (type.equalsIgnoreCase("EMBL")) {
					return new EmblID(idStr);
				} else if (type.equalsIgnoreCase("Ensembl")) {
					return new EnsemblGeneID(idStr);
				} else if (type.equalsIgnoreCase("EnsemblBacteria")) {
					return new EnsemblBacteriaId(idStr);
				} else if (type.equalsIgnoreCase("EnsemblFungi")) {
					return new EnsemblFungiId(idStr);
				} else if (type.equalsIgnoreCase("EnsemblMetazoa")) {
					return new EnsemblMetazoaId(idStr);
				} else if (type.equalsIgnoreCase("EnsemblPlants")) {
					return new EnsemblPlantsId(idStr);
				} else if (type.equalsIgnoreCase("EnsemblProtists")) {
					return new EnsemblProtistsId(idStr);
				} else if (type.equalsIgnoreCase("ENZYME")) {
					return new ENZYMEId(idStr);
				} else if (type.equalsIgnoreCase("euHCVdb")) {
					return new EuHCVdbId(idStr);
				} else if (type.equalsIgnoreCase("EuPathDB")) {
					return new EuPathDBId(idStr);
				} else if (type.equalsIgnoreCase("EvolutionaryTrace")) {
					return new EvolutionaryTraceId(idStr);
				} else if (type.equalsIgnoreCase("FlyBase")) {
					return new FlyBaseID(idStr);
				} else if (type.equalsIgnoreCase("GenAtlas")) {
					return new GenAtlasId(idStr);
				} else if (type.equalsIgnoreCase("GenBank")) {
					return new GenBankID(idStr);
				} else if (type.equalsIgnoreCase("Gene3D")) {
					return new Gene3dID(idStr);
				} else if (type.equalsIgnoreCase("GeneCards")) {
					return new GeneCardId(idStr);
				} else if (type.equalsIgnoreCase("GeneFarm")) {
					return new GeneFarmId(idStr);
				} else if (type.equalsIgnoreCase("GeneID")) {
					return new EntrezGeneID(idStr);
				} else if (type.equalsIgnoreCase("GeneTree")) {
					return new GeneTreeId(idStr);
				} else if (type.equalsIgnoreCase("Genevestigator")) {
					return new GenevestigatorId(idStr);
				} else if (type.equalsIgnoreCase("GenoList")) {
					return new GenoListId(idStr);
				} else if (type.equalsIgnoreCase("GenomeReviews")) {
					return new GenomeReviewsId(idStr);
				} else if (type.equalsIgnoreCase("GenomeRNAi")) {
					return new GenomeRNAiId(idStr);
				} else if (type.equalsIgnoreCase("GermOnline")) {
					return new GermOnlineId(idStr);
				} else if (type.equalsIgnoreCase("GlycoSuiteDB")) {
					return new GlycoSuiteDBId(idStr);
				} else if (type.equalsIgnoreCase("GO")) {
					return new GeneOntologyID(idStr);
				} else if (type.equalsIgnoreCase("GPCRDB")) {
					return new GPCRDBId(idStr);
				} else if (type.equalsIgnoreCase("Gramene")) {
					return new GrameneId(idStr);
				} else if (type.equalsIgnoreCase("H-InvDB")) {
					return new H_InvDBId(idStr);
				} else if (type.equalsIgnoreCase("HAMAP")) {
					return new HAMAPId(idStr);
				} else if (type.equalsIgnoreCase("HGNC")) {
					return new HgncID(idStr);
				} else if (type.equalsIgnoreCase("HOGENOM")) {
					return new HOGENOMId(idStr);
				} else if (type.equalsIgnoreCase("HOVERGEN")) {
					return new HOVERGENId(idStr);
				} else if (type.equalsIgnoreCase("HPA")) {
					return new HPAId(idStr);
				} else if (type.equalsIgnoreCase("HSSP")) {
					return new HSSPId(idStr);
				} else if (type.equalsIgnoreCase("HUGE")) {
					return new HugeId(idStr);
				} else if (type.equalsIgnoreCase("IMGT")) {
					return new ImgtID(idStr);
				} else if (type.equalsIgnoreCase("InParanoid")) {
					return new InParanoidId(idStr);
				} else if (type.equalsIgnoreCase("IntAct")) {
					return new IntActID(idStr);
				} else if (type.equalsIgnoreCase("InterPro")) {
					return new InterProID(idStr);
				} else if (type.equalsIgnoreCase("IPI")) {
					return new IpiID(idStr);
				} else if (type.equalsIgnoreCase("KEGG")) {
					return new KeggGeneID(idStr);
				} else if (type.equalsIgnoreCase("KO")) {
					return new KeggGeneID(idStr);
				} else if (type.equalsIgnoreCase("LegioList")) {
					return new LegioListId(idStr);
				} else if (type.equalsIgnoreCase("Leproma")) {
					return new LepromaId(idStr);
				} else if (type.equalsIgnoreCase("MaizeGDB")) {
					return new MaizeGdbID(idStr);
				} else if (type.equalsIgnoreCase("MEROPS")) {
					return new MeropsId(idStr);
				} else if (type.equalsIgnoreCase("MGI")) {
					return new MgiGeneID(idStr);
				} else if (type.equalsIgnoreCase("Micado")) {
					return new MicadoId(idStr);
				} else if (type.equalsIgnoreCase("MIM")) {
					return new OmimID(idStr);
				} else if (type.equalsIgnoreCase("MINT")) {
					return new MintID(idStr);
				} else if (type.equalsIgnoreCase("ModBase")) {
					return new ModBaseId(idStr);
				} else if (type.equalsIgnoreCase("mycoCLAP")) {
					return new MycoCLAPId(idStr);
				} else if (type.equalsIgnoreCase("NextBio")) {
					return new NextBioId(idStr);
				} else if (type.equalsIgnoreCase("neXtProt")) {
					return new NeXtProtId(idStr);
				} else if (type.equalsIgnoreCase("OGP")) {
					return new OGPId(idStr);
				} else if (type.equalsIgnoreCase("OMA")) {
					return new OMAId(idStr);
				} else if (type.equalsIgnoreCase("Orphanet")) {
					return new OrphanetId(idStr);
				} else if (type.equalsIgnoreCase("OrthoDB")) {
					return new OrthoDBId(idStr);
				} else if (type.equalsIgnoreCase("PANTHER")) {
					return new PantherID(idStr);
				} else if (type.equalsIgnoreCase("Pathway_Interaction_DB")) {
					return new Pathway_Interaction_DBId(idStr);
				} else if (type.equalsIgnoreCase("PATRIC")) {
					return new PATRICId(idStr);
				} else if (type.equalsIgnoreCase("PaxDb")) {
					return new PaxDbId(idStr);
				} else if (type.equalsIgnoreCase("PDB")) {
					return new PdbEuropeId(idStr);
				} else if (type.equalsIgnoreCase("PDBj")) {
					return new PDB_JId(idStr);
				} else if (type.equalsIgnoreCase("PDBsum")) {
					return new PDB_sumId(idStr);
				} else if (type.equalsIgnoreCase("PeptideAtlas")) {
					return new PeptideAtlasId(idStr);
				} else if (type.equalsIgnoreCase("PeroxiBase")) {
					return new PeroxiBaseId(idStr);
				} else if (type.equalsIgnoreCase("Pfam")) {
					return new PfamID(idStr);
				} else if (type.equalsIgnoreCase("PharmGKB")) {
					return new PharmGkbID(idStr);
				} else if (type.equalsIgnoreCase("PhosphoSite")) {
					return new PhosphoSiteId(idStr);
				} else if (type.equalsIgnoreCase("PhosSite")) {
					return new PhosSiteId(idStr);
				} else if (type.equalsIgnoreCase("PhylomeDB")) {
					return new PhylomeDBId(idStr);
				} else if (type.equalsIgnoreCase("PIR")) {
					return new PirID(idStr);
				} else if (type.equalsIgnoreCase("PIRSF")) {
					return new PirSfID(idStr);
				} else if (type.equalsIgnoreCase("PMAP-CutDB")) {
					return new PMAP_CutDBId(idStr);
				} else if (type.equalsIgnoreCase("PomBase")) {
					return new PomBaseId(idStr);
				} else if (type.equalsIgnoreCase("PptaseDB")) {
					return new PptaseDBId(idStr);
				} else if (type.equalsIgnoreCase("PRIDE")) {
					return new PRIDEId(idStr);
				} else if (type.equalsIgnoreCase("PRINTS")) {
					return new PrintsID(idStr);
				} else if (type.equalsIgnoreCase("ProDom")) {
					return new ProDomID(idStr);
				} else if (type.equalsIgnoreCase("ProMEX")) {
					return new ProMEXId(idStr);
				} else if (type.equalsIgnoreCase("PROSITE")) {
					return new PrositeID(idStr);
				} else if (type.equalsIgnoreCase("ProtClustDB")) {
					return new ProtClustDBId(idStr);
				} else if (type.equalsIgnoreCase("ProteinModelPortal")) {
					return new ProteinModelPortalId(idStr);
				} else if (type.equalsIgnoreCase("ProtoNet")) {
					return new ProtoNetId(idStr);
				} else if (type.equalsIgnoreCase("PseudoCAP")) {
					return new PseudoCapID(idStr);
				} else if (type.equalsIgnoreCase("RCSB PDB")) {
					return new PdbID(idStr);
				} else if (type.equalsIgnoreCase("Reactome")) {
					return new ReactomeReactionID(idStr);
				} else if (type.equalsIgnoreCase("REBASE")) {
					return new REBASEId(idStr);
				} else if (type.equalsIgnoreCase("RefSeq")) {
					return new RefSeqID(idStr);
				} else if (type.equalsIgnoreCase("REPRODUCTION-2DPAGE")) {
					return new REPRODUCTION_2DPAGEId(idStr);
				} else if (type.equalsIgnoreCase("RGD")) {
					return new RgdID(idStr);
				} else if (type.equalsIgnoreCase("Rouge")) {
					return new RougeId(idStr);
				} else if (type.equalsIgnoreCase("SABIO-RK")) {
					return new SABIO_RKId(idStr);
				} else if (type.equalsIgnoreCase("SBKB")) {
					return new SBKBId(idStr);
				} else if (type.equalsIgnoreCase("SGD")) {
					return new SgdID(idStr);
				} else if (type.equalsIgnoreCase("SMART")) {
					return new SmartID(idStr);
				} else if (type.equalsIgnoreCase("SMR")) {
					return new SMRId(idStr);
				} else if (type.equalsIgnoreCase("SOURCE")) {
					return new SOURCEId(idStr);
				} else if (type.equalsIgnoreCase("STRING")) {
					return new STRINGId(idStr);
				} else if (type.equalsIgnoreCase("SUPFAM")) {
					return new SUPFAMId(idStr);
				} else if (type.equalsIgnoreCase("SWISS-2DPAGE")) {
					return new SWISS_2DPAGEId(idStr);
				} else if (type.equalsIgnoreCase("TAIR")) {
					return new TairID(idStr);
				} else if (type.equalsIgnoreCase("TCDB")) {
					return new TCDBId(idStr);
				} else if (type.equalsIgnoreCase("TIGRFAMs")) {
					return new TigrFamsID(idStr);
				} else if (type.equalsIgnoreCase("TubercuList")) {
					return new TubercuListId(idStr);
				} else if (type.equalsIgnoreCase("UCD-2DPAGE")) {
					return new UCD_2DPAGEId(idStr);
				} else if (type.equalsIgnoreCase("UCSC")) {
					return new UcscGenomeBrowserId(idStr);
				} else if (type.equalsIgnoreCase("UniGene")) {
					return new UniGeneID(idStr);
				} else if (type.equalsIgnoreCase("UniProtKB")) {
					return new UniProtID(idStr);
				} else if (type.equalsIgnoreCase("UniPathway")) {
					return new UniPathwayId(idStr);
				} else if (type.equalsIgnoreCase("VectorBase")) {
					return new VectorBaseID(idStr);
				} else if (type.equalsIgnoreCase("World-2DPAGE")) {
					return new World_2DPAGEId(idStr);
				} else if (type.equalsIgnoreCase("WormBase")) {
					return new WormBaseID(idStr);
				} else if (type.equalsIgnoreCase("Xenbase")) {
					return new XenBaseID(idStr);
				} else if (type.equalsIgnoreCase("ZFIN")) {
					return new ZfinID(idStr);
				} else if (type.equalsIgnoreCase("MEDLINE")) {
					return new MedlineId(idStr);
				} else if (type.equalsIgnoreCase("EC")) {
					return new EnzymeCommissionID(idStr);
				} else if (type.equalsIgnoreCase("HAMAP-Rule")) {
					return new HamapRuleId(idStr);
				} else if (type.equalsIgnoreCase("AGRICOLA")) {
					return new AgricolaId(idStr);
				} else if (type.equalsIgnoreCase("UniProt")) {
					if (idStr.endsWith("?")) {
						idStr = StringUtil.removeLastCharacter(idStr);
					}
					return new UniProtID(idStr);
				} else if (type.equalsIgnoreCase("RuleBase")) {
					return new RuleBaseId(idStr);
				} else if (type.equalsIgnoreCase("SAAS")) {
					return new SaasId(idStr);
				} else if (type.equalsIgnoreCase("PIRSR")) {
					return new PirsrId(idStr);
				} else if (type.equalsIgnoreCase("PIRNR")) {
					return new PirnrId(idStr);
				}
			} catch (IllegalArgumentException e) {
				logger.warn("Invalid identifier detected: " + e.getMessage());
				return null;
			}

			// throw new IllegalArgumentException("Unhandled identifier type: "
			// + type + " :: " + idStr);
			logger.warn("Unhandled identifier type: " + type + " :: " + idStr);
			return null;
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Event {

		@RecordField
		private final String type;

		public Event(EventType xmlType) {
			this.type = xmlType.getType();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class EvidencedString {

		@RecordField
		private final String value;
		@RecordField
		private final List<Integer> evidence;
		@RecordField
		private final String status;

		private EvidencedString(EvidencedStringType xmlType) {
			this.value = xmlType.getValue();
			this.status = xmlType.getStatus();
			this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
					xmlType.getEvidence());
		}

		public static EvidencedString getInstance(EvidencedStringType xmlType) {
			return (xmlType == null) ? null : new EvidencedString(xmlType);
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Evidence {

		@RecordField
		private final Source source;
		@RecordField
		private final ImportedFrom importedFrom;
		@RecordField
		private final String type;
		@RecordField
		private final Integer key;

		public Evidence(EvidenceType xmlType) {
			this.source = (xmlType.getSource() == null) ? null : new Source(xmlType.getSource());
			this.importedFrom = (xmlType.getImportedFrom() == null) ? null
					: new ImportedFrom(xmlType.getImportedFrom());
			this.type = xmlType.getType();
			this.key = xmlType.getKey().intValue();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Feature {

		@RecordField
		private final String original;
		@RecordField
		private final List<String> variation;
		@RecordField
		private final Location location;
		@RecordField
		private final String type;
		@RecordField
		private final String status;
		@RecordField
		private final String id;
		@RecordField
		private final String description;
		@RecordField
		private final List<Integer> evidence;
		@RecordField
		private final String ref;

		public Feature(FeatureType xmlType) {
			this.original = xmlType.getOriginal();
			this.variation = (xmlType.getVariation() == null) ? new ArrayList<String>() : new ArrayList<String>(
					xmlType.getVariation());
			this.location = (xmlType.getLocation() == null) ? null : new Location(xmlType.getLocation());
			this.type = xmlType.getType();
			this.status = xmlType.getStatus();
			this.id = xmlType.getId();
			this.description = xmlType.getDescription();
			this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
					xmlType.getEvidence());
			this.ref = xmlType.getRef();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class GeneType {
		@RecordField
		protected List<GeneName> name;

		public GeneType(org.uniprot.GeneType xmlType) {
			this.name = new ArrayList<GeneName>();
			if (xmlType.getName() != null) {
				for (GeneNameType type : xmlType.getName()) {
					this.name.add(new GeneName(type));
				}
			}
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class MoleculeType {
		@RecordField
		protected String id;
		@RecordField
		protected String value;

		public MoleculeType(org.uniprot.MoleculeType xmlType) {
			this.id = xmlType.getId();
			this.value = xmlType.getValue();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class GeneLocation {

		@RecordField
		private final List<Status> status;
		@RecordField
		private final String type;
		@RecordField
		private final List<Integer> evidence;

		public GeneLocation(GeneLocationType xmlType) {
			this.status = new ArrayList<Status>();
			if (xmlType.getName() != null) {
				for (StatusType type : xmlType.getName()) {
					this.status.add(new Status(type));
				}
			}
			this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
					xmlType.getEvidence());
			this.type = xmlType.getType();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class GeneName {

		@RecordField
		private final String value;
		@RecordField
		private final List<Integer> evidence;
		@RecordField
		private final String type;

		public GeneName(GeneNameType xmlType) {
			this.value = xmlType.getValue();
			this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
					xmlType.getEvidence());
			this.type = xmlType.getType();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class ImportedFrom {

		@RecordField
		private final DbReference dbReference;

		public ImportedFrom(ImportedFromType xmlType) {
			this.dbReference = (xmlType.getDbReference() == null) ? null : new DbReference(xmlType.getDbReference());
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Interactant {

		@RecordField
		private final DataSourceIdentifier<?> id;
		@RecordField
		private final String label;
		@RecordField
		private final IntActID intactId;

		public Interactant(InteractantType xmlType) {
			id = (xmlType.getId() == null) ? null : (xmlType.getId().contains("-")) ? new UniProtIsoformID(
					xmlType.getId()) : new UniProtID(xmlType.getId());
			this.label = xmlType.getLabel();
			intactId = (xmlType.getIntactId() == null) ? null : new IntActID(xmlType.getIntactId());
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Isoform {

		@RecordField
		private final List<UniProtIsoformID> id;
		@RecordField
		private final List<Isoform.Name> name;
		@RecordField
		private final Isoform.Sequence sequence;
		@RecordField
		private final Isoform.Note note;

		public Isoform(IsoformType xmlType) {
			this.id = new ArrayList<UniProtIsoformID>();
			if (xmlType.getId() != null) {
				for (String idStr : xmlType.getId()) {
					this.id.add(new UniProtIsoformID(idStr));
				}
			}
			this.name = new ArrayList<Isoform.Name>();
			if (xmlType.getName() != null) {
				for (IsoformType.Name isoName : xmlType.getName()) {
					this.name.add(new Isoform.Name(isoName));
				}
			}
			this.sequence = (xmlType.getSequence() == null) ? null : new Isoform.Sequence(xmlType.getSequence());
			this.note = (xmlType.getNote() == null) ? null : new Isoform.Note(xmlType.getNote());
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Name {

			@RecordField
			private final String value;
			@RecordField
			private final List<Integer> evidence;

			public Name(IsoformType.Name xmlType) {
				this.value = xmlType.getValue();
				this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
						xmlType.getEvidence());
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Note {

			@RecordField
			private final String value;
			@RecordField
			private final List<Integer> evidence;

			public Note(IsoformType.Note xmlType) {
				this.value = xmlType.getValue();
				this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
						xmlType.getEvidence());
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Sequence {

			@RecordField
			private final String type;
			@RecordField
			private final String ref;

			public Sequence(IsoformType.Sequence xmlType) {
				this.type = xmlType.getType();
				this.ref = xmlType.getRef();
			}
		}

	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Keyword {

		@RecordField
		private final String value;
		@RecordField
		private final List<Integer> evidence;
		@RecordField
		private final String id;

		public Keyword(KeywordType xmlType) {
			this.value = xmlType.getValue();
			this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
					xmlType.getEvidence());
			this.id = xmlType.getId();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Location {

		@RecordField
		private final Position begin;
		@RecordField
		private final Position end;
		@RecordField
		private final Position position;
		@RecordField
		private final String sequence;

		public Location(LocationType xmlType) {
			this.begin = (xmlType.getBegin() == null) ? null : new Position(xmlType.getBegin());
			this.end = (xmlType.getEnd() == null) ? null : new Position(xmlType.getEnd());
			this.position = (xmlType.getPosition() == null) ? null : new Position(xmlType.getPosition());
			this.sequence = xmlType.getSequence();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Name {

		@RecordField
		private final String consortiumOrPerson;
		@RecordField
		private final String name;

		public Name(Object xmlType) {
			if (xmlType instanceof PersonType) {
				this.consortiumOrPerson = "person";
				this.name = ((PersonType) xmlType).getName();
			} else if (xmlType instanceof ConsortiumType) {
				this.consortiumOrPerson = "consortium";
				this.name = ((ConsortiumType) xmlType).getName();
			} else {
				throw new IllegalArgumentException("Excepted person or consortium but observed: "
						+ xmlType.getClass().getName());
			}
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class OrganismName {

		@RecordField
		private final String value;
		@RecordField
		private final String type;

		public OrganismName(OrganismNameType xmlType) {
			this.value = xmlType.getValue();
			this.type = xmlType.getType();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Organism {

		@RecordField
		private final List<OrganismName> name;
		@RecordField
		private final List<DbReference> dbReference;
		@RecordField
		private final Organism.Lineage lineage;
		@RecordField
		private final List<Integer> evidence;

		public Organism(OrganismType xmlType) {
			this.name = new ArrayList<OrganismName>();
			if (xmlType.getName() != null) {
				for (OrganismNameType type : xmlType.getName()) {
					this.name.add(new OrganismName(type));
				}
			}
			this.dbReference = new ArrayList<DbReference>();
			if (xmlType.getDbReference() != null) {
				for (DbReferenceType type : xmlType.getDbReference()) {
					dbReference.add(new DbReference(type));
				}
			}
			this.lineage = (xmlType.getLineage() == null) ? null : new Organism.Lineage(xmlType.getLineage());
			this.evidence = (xmlType.getEvidence() == null) ? null : new ArrayList<Integer>(xmlType.getEvidence());
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Lineage {
			@RecordField
			private final List<String> taxon;

			public Lineage(OrganismType.Lineage xmlType) {
				this.taxon = (xmlType.getTaxon() == null) ? new ArrayList<String>() : new ArrayList<String>(
						xmlType.getTaxon());
			}
		}
	}

	// @Getter
	// @Record(dataSource = DataSource.UNIPROT)
	// public static class Person {
	// @RecordField
	// private final String name;
	//
	// public Person(PersonType xmlType) {
	// this.name = xmlType.getName();
	// }
	// }

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Position {

		@RecordField
		private final Integer position;
		@RecordField
		private final String status;
		@RecordField
		private final List<Integer> evidence;

		public Position(PositionType xmlType) {

			this.position = (xmlType.getPosition() == null) ? null : xmlType.getPosition().intValue();
			this.status = xmlType.getStatus();
			this.evidence = (xmlType.getEvidence() == null) ? null : new ArrayList<Integer>(xmlType.getEvidence());
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Property {

		@RecordField
		private final String type;
		@RecordField
		private final String value;

		public Property(PropertyType xmlType) {
			this.type = xmlType.getType();
			this.value = xmlType.getValue();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class ProteinExistence {

		@RecordField
		private final String type;

		public ProteinExistence(ProteinExistenceType xmlType) {
			this.type = xmlType.getType();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Protein {

		@RecordField
		private final Protein.RecommendedName recommendedName;
		@RecordField
		private final List<Protein.AlternativeName> alternativeName;
		@RecordField
		private final List<Protein.SubmittedName> submittedName;
		@RecordField
		private final EvidencedString allergenName;
		@RecordField
		private final EvidencedString biotechName;
		@RecordField
		private final List<EvidencedString> cdAntigenName;
		@RecordField
		private final List<EvidencedString> innName;
		@RecordField
		private final List<Protein.Domain> domain;
		@RecordField
		private final List<Protein.Component> component;

		public Protein(ProteinType xmlType) {
			this.recommendedName = (xmlType.getRecommendedName() == null) ? null : new Protein.RecommendedName(
					xmlType.getRecommendedName());
			this.alternativeName = new ArrayList<Protein.AlternativeName>();
			if (xmlType.getAlternativeName() != null) {
				for (ProteinType.AlternativeName name : xmlType.getAlternativeName()) {
					this.alternativeName.add(new Protein.AlternativeName(name));
				}
			}
			this.submittedName = new ArrayList<Protein.SubmittedName>();
			if (xmlType.getSubmittedName() != null) {
				for (ProteinType.SubmittedName name : xmlType.getSubmittedName()) {
					this.submittedName.add(new Protein.SubmittedName(name));
				}
			}
			allergenName = EvidencedString.getInstance(xmlType.getAllergenName());
			biotechName = EvidencedString.getInstance(xmlType.getBiotechName());
			this.cdAntigenName = new ArrayList<EvidencedString>();
			if (xmlType.getCdAntigenName() != null) {
				for (EvidencedStringType type : xmlType.getCdAntigenName()) {
					cdAntigenName.add(EvidencedString.getInstance(type));
				}
			}
			this.innName = new ArrayList<EvidencedString>();
			if (xmlType.getInnName() != null) {
				for (EvidencedStringType type : xmlType.getInnName()) {
					innName.add(EvidencedString.getInstance(type));
				}
			}
			this.domain = new ArrayList<Protein.Domain>();
			if (xmlType.getDomain() != null) {
				for (ProteinType.Domain name : xmlType.getDomain()) {
					this.domain.add(new Protein.Domain(name));
				}
			}
			this.component = new ArrayList<Protein.Component>();
			if (xmlType.getComponent() != null) {
				for (ProteinType.Component name : xmlType.getComponent()) {
					this.component.add(new Protein.Component(name));
				}
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class AlternativeName {

			@RecordField
			private final EvidencedString fullName;
			@RecordField
			private final List<EvidencedString> shortName;
			@RecordField
			private final List<EvidencedString> ecNumber;

			public AlternativeName(ProteinType.AlternativeName xmlType) {
				this.fullName = EvidencedString.getInstance(xmlType.getFullName());
				this.shortName = new ArrayList<EvidencedString>();
				if (xmlType.getShortName() != null) {
					for (EvidencedStringType type : xmlType.getShortName()) {
						shortName.add(EvidencedString.getInstance(type));
					}
				}
				this.ecNumber = new ArrayList<EvidencedString>();
				if (xmlType.getEcNumber() != null) {
					for (EvidencedStringType type : xmlType.getEcNumber()) {
						ecNumber.add(EvidencedString.getInstance(type));
					}
				}
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Component {

			@RecordField
			private final Protein.RecommendedName recommendedName;
			@RecordField
			private final List<Protein.AlternativeName> alternativeName;
			@RecordField
			private final List<Protein.SubmittedName> submittedName;
			@RecordField
			private final EvidencedString allergenName;
			@RecordField
			private final EvidencedString biotechName;
			@RecordField
			private final List<EvidencedString> cdAntigenName;
			@RecordField
			private final List<EvidencedString> innName;

			public Component(ProteinType.Component xmlType) {
				this.recommendedName = (xmlType.getRecommendedName() == null) ? null : new Protein.RecommendedName(
						xmlType.getRecommendedName());
				this.alternativeName = new ArrayList<Protein.AlternativeName>();
				if (xmlType.getAlternativeName() != null) {
					for (ProteinType.AlternativeName name : xmlType.getAlternativeName()) {
						this.alternativeName.add(new Protein.AlternativeName(name));
					}
				}
				this.submittedName = new ArrayList<Protein.SubmittedName>();
				if (xmlType.getSubmittedName() != null) {
					for (ProteinType.SubmittedName name : xmlType.getSubmittedName()) {
						this.submittedName.add(new Protein.SubmittedName(name));
					}
				}
				allergenName = EvidencedString.getInstance(xmlType.getAllergenName());
				biotechName = EvidencedString.getInstance(xmlType.getBiotechName());
				this.cdAntigenName = new ArrayList<EvidencedString>();
				if (xmlType.getCdAntigenName() != null) {
					for (EvidencedStringType type : xmlType.getCdAntigenName()) {
						cdAntigenName.add(EvidencedString.getInstance(type));
					}
				}
				this.innName = new ArrayList<EvidencedString>();
				if (xmlType.getInnName() != null) {
					for (EvidencedStringType type : xmlType.getInnName()) {
						innName.add(EvidencedString.getInstance(type));
					}
				}
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class Domain {

			@RecordField
			private final Protein.RecommendedName recommendedName;
			@RecordField
			private final List<Protein.AlternativeName> alternativeName;
			@RecordField
			private final List<Protein.SubmittedName> submittedName;
			@RecordField
			private final EvidencedString allergenName;
			@RecordField
			private final EvidencedString biotechName;
			@RecordField
			private final List<EvidencedString> cdAntigenName;
			@RecordField
			private final List<EvidencedString> innName;

			public Domain(ProteinType.Domain xmlType) {
				this.recommendedName = (xmlType.getRecommendedName() == null) ? null : new Protein.RecommendedName(
						xmlType.getRecommendedName());
				this.alternativeName = new ArrayList<Protein.AlternativeName>();
				if (xmlType.getAlternativeName() != null) {
					for (ProteinType.AlternativeName name : xmlType.getAlternativeName()) {
						this.alternativeName.add(new Protein.AlternativeName(name));
					}
				}
				this.submittedName = new ArrayList<Protein.SubmittedName>();
				if (xmlType.getSubmittedName() != null) {
					for (ProteinType.SubmittedName name : xmlType.getSubmittedName()) {
						this.submittedName.add(new Protein.SubmittedName(name));
					}
				}
				allergenName = EvidencedString.getInstance(xmlType.getAllergenName());
				biotechName = EvidencedString.getInstance(xmlType.getBiotechName());
				this.cdAntigenName = new ArrayList<EvidencedString>();
				if (xmlType.getCdAntigenName() != null) {
					for (EvidencedStringType type : xmlType.getCdAntigenName()) {
						cdAntigenName.add(EvidencedString.getInstance(type));
					}
				}
				this.innName = new ArrayList<EvidencedString>();
				if (xmlType.getInnName() != null) {
					for (EvidencedStringType type : xmlType.getInnName()) {
						innName.add(EvidencedString.getInstance(type));
					}
				}
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class RecommendedName {

			@RecordField
			private final EvidencedString fullName;
			@RecordField
			private final List<EvidencedString> shortName;
			@RecordField
			private final List<EvidencedString> ecNumber;

			public RecommendedName(ProteinType.RecommendedName xmlType) {
				this.fullName = EvidencedString.getInstance(xmlType.getFullName());
				this.shortName = new ArrayList<EvidencedString>();
				if (xmlType.getShortName() != null) {
					for (EvidencedStringType type : xmlType.getShortName()) {
						shortName.add(EvidencedString.getInstance(type));
					}
				}
				this.ecNumber = new ArrayList<EvidencedString>();
				if (xmlType.getEcNumber() != null) {
					for (EvidencedStringType type : xmlType.getEcNumber()) {
						ecNumber.add(EvidencedString.getInstance(type));
					}
				}
			}
		}

		@Getter
		@Record(dataSource = DataSource.UNIPROT)
		public static class SubmittedName {

			@RecordField
			private final EvidencedString fullName;
			@RecordField
			private final List<EvidencedString> ecNumber;

			public SubmittedName(ProteinType.SubmittedName xmlType) {
				this.fullName = EvidencedString.getInstance(xmlType.getFullName());
				this.ecNumber = new ArrayList<EvidencedString>();
				if (xmlType.getEcNumber() != null) {
					for (EvidencedStringType type : xmlType.getEcNumber()) {
						ecNumber.add(EvidencedString.getInstance(type));
					}
				}
			}
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Reference {

		@RecordField
		private final Citation citation;
		@RecordField
		private final List<String> scope;
		@RecordField
		private final List<SourceData> source;
		@RecordField
		private final List<Integer> evidence;
		@RecordField
		private final String key;

		public Reference(ReferenceType xmlType) {
			this.citation = (xmlType.getCitation() == null) ? null : new Citation(xmlType.getCitation());
			this.scope = (xmlType.getScope() == null) ? new ArrayList<String>() : new ArrayList<String>(
					xmlType.getScope());
			this.source = new ArrayList<SourceData>();
			if (xmlType.getSource() != null) {
				for (Object sdt : xmlType.getSource().getStrainOrPlasmidOrTransposon()) {
					source.add(new SourceData(sdt));
				}
			}
			this.evidence = (xmlType.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
					xmlType.getEvidence());
			this.key = xmlType.getKey();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Sequence {

		@RecordField
		private final String value;
		@RecordField
		private final int length;
		@RecordField
		private final int mass;
		@RecordField
		private final String checksum;
		@RecordField
		private final XMLGregorianCalendar modified;
		@RecordField
		private final int version;
		@RecordField
		private final Boolean precursor;
		@RecordField
		private final String fragment;

		public Sequence(SequenceType xmlType) {
			this.value = xmlType.getValue();
			this.length = xmlType.getLength();
			this.mass = xmlType.getMass();
			this.checksum = xmlType.getChecksum();
			this.modified = xmlType.getModified();
			this.version = xmlType.getVersion();
			this.precursor = xmlType.isPrecursor();
			this.fragment = xmlType.getFragment();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class SourceData {

		@RecordField
		private final String type;
		@RecordField
		private final String value;
		@RecordField
		private final List<Integer> evidence;

		public SourceData(Object xmlType) {
			if (xmlType instanceof SourceDataType.Tissue) {
				this.type = "tissue";
				SourceDataType.Tissue t = (SourceDataType.Tissue) xmlType;
				this.value = t.getValue();
				this.evidence = (t.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
						t.getEvidence());
			} else if (xmlType instanceof SourceDataType.Transposon) {
				this.type = "transposon";
				SourceDataType.Transposon t = (SourceDataType.Transposon) xmlType;
				this.value = t.getValue();
				this.evidence = (t.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
						t.getEvidence());
			} else if (xmlType instanceof SourceDataType.Plasmid) {
				this.type = "plasmid";
				SourceDataType.Plasmid p = (SourceDataType.Plasmid) xmlType;
				this.value = p.getValue();
				this.evidence = (p.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
						p.getEvidence());
			} else if (xmlType instanceof SourceDataType.Strain) {
				this.type = "strain";
				SourceDataType.Strain s = (SourceDataType.Strain) xmlType;
				this.value = s.getValue();
				this.evidence = (s.getEvidence() == null) ? new ArrayList<Integer>() : new ArrayList<Integer>(
						s.getEvidence());
			} else {
				throw new IllegalArgumentException("Unhandled source data type: " + xmlType.getClass().getName());
			}
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Source {

		@RecordField
		private final DbReference dbReference;
		@RecordField
		private final Integer ref;

		public Source(SourceType xmlType) {
			this.dbReference = (xmlType.getDbReference() == null) ? null : new DbReference(xmlType.getDbReference());
			this.ref = (xmlType.getRef() == null) ? null : xmlType.getRef().intValue();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class Status {

		@RecordField
		private final String value;
		@RecordField
		private final String status;

		public Status(StatusType xmlType) {
			this.value = xmlType.getValue();
			this.status = xmlType.getStatus();
		}
	}

	@Getter
	@Record(dataSource = DataSource.UNIPROT)
	public static class SubcellularLocation {

		@RecordField
		private final List<EvidencedString> location;
		@RecordField
		private final List<EvidencedString> topology;
		@RecordField
		private final List<EvidencedString> orientation;

		public SubcellularLocation(SubcellularLocationType xmlType) {
			this.location = new ArrayList<EvidencedString>();
			if (xmlType.getLocation() != null) {
				for (EvidencedStringType type : xmlType.getLocation()) {
					this.location.add(EvidencedString.getInstance(type));
				}
			}
			this.topology = new ArrayList<EvidencedString>();
			if (xmlType.getTopology() != null) {
				for (EvidencedStringType type : xmlType.getTopology()) {
					this.topology.add(EvidencedString.getInstance(type));
				}
			}
			this.orientation = new ArrayList<EvidencedString>();
			if (xmlType.getOrientation() != null) {
				for (EvidencedStringType type : xmlType.getOrientation()) {
					this.orientation.add(EvidencedString.getInstance(type));
				}
			}
		}
	}

}
