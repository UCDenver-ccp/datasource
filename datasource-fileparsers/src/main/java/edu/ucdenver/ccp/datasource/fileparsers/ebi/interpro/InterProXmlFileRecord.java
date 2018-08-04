package edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro;

import java.io.Serializable;
import java.util.ArrayList;

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

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.interpro.AbstractType;
import org.interpro.ChildListType;
import org.interpro.CiteType;
import org.interpro.ClassListType;
import org.interpro.ClassificationType;
import org.interpro.ContainsType;
import org.interpro.DbXrefType;
import org.interpro.ExternalDocListType;
import org.interpro.FoundInType;
import org.interpro.InterproType;
import org.interpro.LocationType;
import org.interpro.MemberListType;
import org.interpro.PType;
import org.interpro.ParentListType;
import org.interpro.PubListType;
import org.interpro.PublicationType;
import org.interpro.RelRefType;
import org.interpro.SecAcType;
import org.interpro.SecListType;
import org.interpro.StructureDbLinksType;
import org.interpro.TaxonDataType;
import org.interpro.TaxonType;
import org.interpro.TaxonomyDistributionType;
import org.interpro.UlType;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InterProID;
import lombok.Getter;
import lombok.ToString;

@Getter
@Record(ontClass = CcpExtensionOntology.INTERPRO_RECORD, dataSource = DataSource.INTERPRO, label = "interpro record")
public class InterProXmlFileRecord extends FileRecord {
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___INTERPRO_IDENTIFIER_FIELD_VALUE)
	private final InterProID interProId;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___PROTEIN_COUNT_FIELD_VALUE)
	private final Integer proteinCount;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___SHORT_NAME_FIELD_VALUE)
	private final String shortName;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___TYPE_FIELD_VALUE)
	private final String type;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___NAME_FIELD_VALUE)
	private String name;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___ABSTRACT_TEXT_FIELD_VALUE)
	private String abstractText;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___PUBLICATION_LIST_FIELD_VALUE)
	private List<Publication> pubList;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___CHILDREN_IDENTIFIER_FIELD_VALUE)
	private List<Reference> childReferences;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___FOUND_IN_IDENTIFIER_FIELD_VALUE)
	private List<Reference> foundInReferences;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___MEMBER_LIST_FIELD_VALUE)
	private List<DbXRef> memberList;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___EXTERNAL_DOCUMENT_LIST_FIELD_VALUE)
	private List<DbXRef> externalDocList;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___STRUCTURE_DATABASE_LINKS_FIELD_VALUE)
	private List<DbXRef> structureDbLinks;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___TAXON_DATA_FIELD_VALUE)
	private List<TaxonData> taxonData;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___SECONDARY_ACCESSION_FIELD_VALUE)
	private List<SecondaryAccession> secAcList;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___CLASSIFICATION_FIELD_VALUE)
	private List<Classification> classificationList;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___CONTAINS_IDENTIFIER_FIELD_VALUE)
	private List<Reference> containsList;
	@RecordField(ontClass = CcpExtensionOntology.INTERPRO_ENTRY_RECORD___PARENT_IDENTIFIER_FIELD_VALUE)
	private Reference parent;

	public InterProXmlFileRecord() {
		/*
		 * b/c this data is coming from XML there's no easy way to track the
		 * byte offset
		 */
		super(-1);

		this.interProId = null;
		this.proteinCount = null;
		this.shortName = null;
		this.type = null;
	}

	public InterProXmlFileRecord(InterproType entry) {
		/*
		 * b/c this data is coming from XML there's no easy way to track the
		 * byte offset
		 */
		super(-1);

		this.interProId = new InterProID(entry.getId());
		this.proteinCount = entry.getProteinCount();
		this.shortName = entry.getShortName();
		this.type = entry.getType();

		List<Object> nameOrAbstractOrClassList = entry.getNameOrAbstractOrClassList();
		for (Object obj : nameOrAbstractOrClassList) {
			if (obj instanceof String) {
				this.name = (String) obj;
			} else if (obj instanceof org.interpro.AbstractType) {
				Abstract theAbstract = new Abstract((AbstractType) obj);
				this.abstractText = theAbstract.getText().replaceAll("\\n", " ").trim();
			} else if (obj instanceof org.interpro.PubListType) {
				PubList pl = new PubList((PubListType) obj);
				this.pubList = pl.getPubList();
			} else if (obj instanceof org.interpro.ChildListType) {
				ChildList cl = new ChildList((ChildListType) obj);
				this.childReferences = cl.getRefs();
			} else if (obj instanceof org.interpro.FoundInType) {
				FoundInList fl = new FoundInList((FoundInType) obj);
				this.foundInReferences = fl.getRefs();
			} else if (obj instanceof org.interpro.MemberListType) {
				MemberList ml = new MemberList((MemberListType) obj);
				this.memberList = ml.getDbXRefs();
			} else if (obj instanceof org.interpro.ExternalDocListType) {
				ExternalDocList edl = new ExternalDocList((ExternalDocListType) obj);
				this.externalDocList = edl.getDbXRefs();
			} else if (obj instanceof org.interpro.StructureDbLinksType) {
				StructureDbLinksList sdl = new StructureDbLinksList((StructureDbLinksType) obj);
				this.structureDbLinks = sdl.getDbXRefs();
			} else if (obj instanceof org.interpro.TaxonomyDistributionType) {
				TaxonomyDistribution td = new TaxonomyDistribution((TaxonomyDistributionType) obj);
				this.taxonData = td.getTaxonData();
			} else if (obj instanceof org.interpro.SecListType) {
				SecList sl = new SecList((SecListType) obj);
				this.secAcList = sl.getSecAcList();
			} else if (obj instanceof org.interpro.ClassListType) {
				ClassList cl = new ClassList((ClassListType) obj);
				this.classificationList = cl.getClassificationList();
			} else if (obj instanceof org.interpro.ContainsType) {
				ContainsList c = new ContainsList((ContainsType) obj);
				this.containsList = c.getRefs();
			} else if (obj instanceof org.interpro.ParentListType) {
				ParentList p = new ParentList((ParentListType) obj);
				this.parent = p.getRef();
			} else {
				throw new IllegalStateException("Unhandled field: " + obj.getClass().getName());
			}
		}
	}

	@Getter
	public static class ChildList {
		private final List<Reference> refs;

		public ChildList(ChildListType xmlType) {
			List<Reference> refs = new ArrayList<Reference>();
			List<RelRefType> relRef = xmlType.getRelRef();
			for (RelRefType rrt : relRef) {
				refs.add(new Reference(rrt));
			}
			this.refs = refs;
		}
	}

	@Getter
	@Record(ontClass = CcpExtensionOntology.INTERPRO_REFERENCE_RECORD, dataSource = DataSource.INTERPRO)
	public static class Reference {
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_REFERENCE_RECORD___REFERENCE_IDENTIFIER_FIELD_VALUE)
		private final InterProID referenceId;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_REFERENCE_RECORD___VALUE_FIELD_VALUE)
		private final String value;

		public Reference(RelRefType xmlType) {
			this.referenceId = new InterProID(xmlType.getIprRef());
			this.value = xmlType.getValue();
		}
	}

	@Getter
	public static class PubList {
		private final List<Publication> pubList;

		public PubList(PubListType xmlType) {
			List<PublicationType> publications = xmlType.getPublication();
			List<Publication> pubList = new ArrayList<Publication>();
			for (PublicationType pt : publications) {
				pubList.add(new Publication(pt));
			}
			this.pubList = pubList;
		}
	}

	@Getter
	@Record(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD, dataSource = DataSource.INTERPRO)
	public static class Publication {
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD___AUTHOR_LIST_FIELD_VALUE)
		private final String authorList;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD___IDENTIFIER_FIELD_VALUE)
		private final String id;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD___JOURNAL_NAME_FIELD_VALUE)
		private final String journal;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD___JOURNAL_LOCATION_FIELD_VALUE)
		private final JournalLocation location;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD___TITLE_FIELD_VALUE)
		private final String title;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD___YEAR_FIELD_VALUE)
		private final int year;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_PUBLICATION_RECORD___DATABASE_CROSS_REFERENCE_FIELD_VALUE)
		private final DbXRef dbXRef;

		public Publication(PublicationType xmlType) {
			this.authorList = xmlType.getAuthorList();
			this.dbXRef = new DbXRef(xmlType.getDbXref());
			this.id = xmlType.getId();
			this.journal = xmlType.getJournal();
			this.location = (xmlType.getLocation() != null) ? new JournalLocation(xmlType.getLocation()) : null;
			this.title = xmlType.getTitle();
			this.year = xmlType.getYear();
		}
	}

	@Getter
	@Record(ontClass = CcpExtensionOntology.INTERPRO_JOURNAL_LOCATION_RECORD, dataSource = DataSource.INTERPRO)
	public static class JournalLocation {
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_JOURNAL_LOCATION_RECORD___ISSUE_FIELD_VALUE)
		private final String issue;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_JOURNAL_LOCATION_RECORD___PAGES_FIELD_VALUE)
		private final String pages;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_JOURNAL_LOCATION_RECORD___VOLUME_FIELD_VALUE)
		private final String volume;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_JOURNAL_LOCATION_RECORD___VALUE_FIELD_VALUE)
		private final String value;

		public JournalLocation(LocationType xmlType) {
			this.issue = xmlType.getIssue();
			this.pages = xmlType.getPages();
			this.volume = xmlType.getVolume();
			this.value = xmlType.getValue();
		}
	}

	@Getter
	public static class FoundInList {

		private final List<Reference> refs;

		public FoundInList(FoundInType xmlType) {
			List<Reference> refs = new ArrayList<Reference>();
			List<RelRefType> relRef = xmlType.getRelRef();
			for (RelRefType rrt : relRef) {
				refs.add(new Reference(rrt));
			}
			this.refs = refs;
		}
	}

	@Getter
	public static class MemberList {
		private final List<DbXRef> dbXRefs;

		public MemberList(MemberListType xmlType) {
			List<DbXRef> dbXRefs = new ArrayList<DbXRef>();
			List<DbXrefType> dbXref = xmlType.getDbXref();
			for (DbXrefType dbt : dbXref) {
				dbXRefs.add(new DbXRef(dbt));
			}
			this.dbXRefs = dbXRefs;
		}
	}

	@Getter
	public static class ExternalDocList {
		private final List<DbXRef> dbXRefs;

		public ExternalDocList(ExternalDocListType xmlType) {
			List<DbXRef> dbXRefs = new ArrayList<DbXRef>();
			List<DbXrefType> dbXref = xmlType.getDbXref();
			for (DbXrefType dbt : dbXref) {
				dbXRefs.add(new DbXRef(dbt));
			}
			this.dbXRefs = dbXRefs;
		}
	}

	@Getter
	public static class StructureDbLinksList {
		private final List<DbXRef> dbXRefs;

		public StructureDbLinksList(StructureDbLinksType xmlType) {
			List<DbXRef> dbXRefs = new ArrayList<DbXRef>();
			List<DbXrefType> dbXref = xmlType.getDbXref();
			for (DbXrefType dbt : dbXref) {
				dbXRefs.add(new DbXRef(dbt));
			}
			this.dbXRefs = dbXRefs;
		}
	}

	@Getter
	public static class TaxonomyDistribution {
		private final List<TaxonData> taxonData;

		public TaxonomyDistribution(TaxonomyDistributionType xmlType) {
			List<TaxonData> taxonData = new ArrayList<TaxonData>();
			List<TaxonDataType> taxonDataTypes = xmlType.getTaxonData();
			for (TaxonDataType tdt : taxonDataTypes) {
				taxonData.add(new TaxonData(tdt));
			}
			this.taxonData = taxonData;
		}
	}

	@Getter
	@Record(ontClass = CcpExtensionOntology.INTERPRO_TAXON_DATA_RECORD, dataSource = DataSource.INTERPRO)
	public static class TaxonData {

		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_TAXON_DATA_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_TAXON_DATA_RECORD___PROTEIN_COUNT_FIELD_VALUE)
		private final Integer proteinCount;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_TAXON_DATA_RECORD___VALUE_FIELD_VALUE)
		private final String value;

		public TaxonData(TaxonDataType xmlType) {
			this.name = xmlType.getName();
			this.proteinCount = xmlType.getProteinsCount();
			this.value = xmlType.getValue();
		}

	}

	@Getter
	public static class SecList {
		private final List<SecondaryAccession> secAcList;

		public SecList(SecListType xmlType) {
			List<SecondaryAccession> secAcList = new ArrayList<SecondaryAccession>();
			List<SecAcType> secAc = xmlType.getSecAc();
			for (SecAcType sat : secAc) {
				secAcList.add(new SecondaryAccession(sat));
			}
			this.secAcList = secAcList;
		}
	}

	@Getter
	@Record(ontClass = CcpExtensionOntology.INTERPRO_SECONDARY_ACCESSION_RECORD, dataSource = DataSource.INTERPRO)
	public static class SecondaryAccession {
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_SECONDARY_ACCESSION_RECORD___ACCESSION_FIELD_VALUE)
		private final InterProID accession;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_SECONDARY_ACCESSION_RECORD___VALUE_FIELD_VALUE)
		private final String value;

		public SecondaryAccession(SecAcType xmlType) {
			this.accession = new InterProID(xmlType.getAcc());
			this.value = xmlType.getValue();
		}
	}

	@Getter
	public static class ClassList {
		private final List<Classification> classificationList;

		public ClassList(ClassListType xmlType) {
			List<ClassificationType> classification = xmlType.getClassification();
			List<Classification> classificationList = new ArrayList<Classification>();
			for (ClassificationType ct : classification) {
				classificationList.add(new Classification(ct));
			}
			this.classificationList = classificationList;
		}
	}

	@Getter
	@Record(ontClass = CcpExtensionOntology.INTERPRO_CLASSIFICATION_RECORD, dataSource = DataSource.INTERPRO)
	public static class Classification {
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_CLASSIFICATION_RECORD___CATEGORY_FIELD_VALUE)
		private final String category;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_CLASSIFICATION_RECORD___CLASS_TYPE_FIELD_VALUE)
		private final String classType;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_CLASSIFICATION_RECORD___DESCRIPTION_FIELD_VALUE)
		private final String description;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_CLASSIFICATION_RECORD___IDENTIFIER_FIELD_VALUE)
		private final String id;

		public Classification(ClassificationType xmlType) {
			this.category = xmlType.getCategory();
			this.classType = xmlType.getClassType();
			this.description = xmlType.getDescription();
			this.id = xmlType.getId();
		}
	}

	@Getter
	public static class ContainsList {
		private final List<Reference> refs;

		public ContainsList(ContainsType xmlType) {
			List<Reference> refs = new ArrayList<Reference>();
			List<RelRefType> relRef = xmlType.getRelRef();
			for (RelRefType rrt : relRef) {
				refs.add(new Reference(rrt));
			}
			this.refs = refs;
		}

	}

	@Getter
	public static class ParentList {

		private final Reference ref;

		public ParentList(ParentListType xmlType) {
			RelRefType relRef = xmlType.getRelRef();
			this.ref = new Reference(relRef);
		}
	}

	public static String getString(List<Serializable> content) {
		StringBuilder sb = new StringBuilder();

		for (Serializable s : content) {
			if (s instanceof String) {
				sb.append(s);
			} else if (s instanceof JAXBElement) {
				JAXBElement<?> ele = (JAXBElement<?>) s;
				Object value = ele.getValue();
				if (value instanceof String) {
					sb.append(s);
				} else if (value instanceof CiteType) {
					Cite c = new Cite((CiteType) value);
					sb.append("<cite idref=\"" + c.getIdRef() + "\"/>");
				} else if (value instanceof PType) {
					Paragraph p = new Paragraph((PType) value);
					sb.append(p.getText());
				} else if (value instanceof TaxonType) {
					Taxon t = new Taxon((TaxonType) value);
					sb.append("<taxon tax_id=\"" + t.getTaxId() + "\">" + t.getValue() + "</taxon>");
				} else if (value instanceof UlType) {
					Ul ul = new Ul((UlType) value);
					sb.append(ul.getText());
				} else if (value instanceof DbXrefType) {
					DbXRef dbXRef = new DbXRef((DbXrefType) value);
					sb.append(dbXRef.toString());
				} else if (value instanceof PType.Sup) {
					Sup sup = new Sup((PType.Sup) value);
					sb.append("<sup>" + sup.getValue() + "</sup>");
				} else {
					throw new IllegalStateException(
							"Unhandled XML element in abstract type: " + value.getClass().getName());
				}
			} else {
				throw new IllegalStateException("Unhandled field in abstract type: " + s.getClass().getName());
			}
		}

		return sb.toString();
	}

	@Getter
	public static class Abstract {
		private final String text;

		public Abstract(AbstractType xmlType) {
			List<Serializable> content = xmlType.getContent();
			this.text = getString(content);
		}
	}

	@Getter
	public static class Cite {
		private final String idRef;

		public Cite(CiteType xmlType) {
			this.idRef = xmlType.getIdref();
		}
	}

	@Getter
	public static class Paragraph {
		private final String text;

		public Paragraph(PType xmlType) {
			List<Serializable> content = xmlType.getContent();
			this.text = getString(content);
		}
	}

	@Getter
	public static class Taxon {
		private final int taxId;
		private final String value;

		public Taxon(TaxonType xmlType) {
			this.taxId = xmlType.getTaxId();
			this.value = xmlType.getValue();
		}
	}

	@Getter
	public static class Ul {
		private final String text;

		public Ul(UlType xmlType) {
			List<Serializable> content = xmlType.getContent();
			text = getString(content);
		}
	}

	@Getter
	@ToString
	@Record(ontClass = CcpExtensionOntology.INTERPRO_DATABASE_CROSS_REFERENCE_RECORD, dataSource = DataSource.INTERPRO)
	public static class DbXRef {

		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_DATABASE_CROSS_REFERENCE_RECORD___DATABASE_NAME_FIELD_VALUE)
		private final String db;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_DATABASE_CROSS_REFERENCE_RECORD___DATABASE_INTERNAL_IDENTIFIER_FIELD_VALUE)
		private final String dbKey;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_DATABASE_CROSS_REFERENCE_RECORD___REFERENCE_IDENTIFIER_FIELD_VALUE)
		private final String idRef;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_DATABASE_CROSS_REFERENCE_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_DATABASE_CROSS_REFERENCE_RECORD___PROTEIN_COUNT_FIELD_VALUE)
		private final Integer proteinCount;
		@RecordField(ontClass = CcpExtensionOntology.INTERPRO_DATABASE_CROSS_REFERENCE_RECORD___VALUE_FIELD_VALUE)
		private final String value;

		public DbXRef(DbXrefType xmlType) {
			this.db = xmlType.getDb();
			this.dbKey = xmlType.getDbkey();
			this.idRef = xmlType.getIdref();
			this.name = xmlType.getName();
			this.proteinCount = xmlType.getProteinCount();
			this.value = xmlType.getValue();
		}
	}

	@Getter
	public static class Sup {
		private final String value;

		public Sup(PType.Sup xmlType) {
			this.value = xmlType.getValue();
		}
	}

}
