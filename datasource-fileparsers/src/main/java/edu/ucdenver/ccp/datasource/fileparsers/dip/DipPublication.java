/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.dip;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.DIP, label = "publication")
public class DipPublication {
	

	@RecordField(label = "PubMed ID")
	private final PubMedID pmid;

	@RecordField(comment="Identifier of the publication in which this interaction has been shown. Database name taken from the PSI-MI controlled vocabulary, represented as databaseName:identifier. Multiple identifiers separated by \"|\".", label = "publication ID")
	private final DipPublicationId dipPublicationId;

	@RecordField(comment = "First author surname(s) of the publication(s) in which this interaction has been shown, optionally followed by additional indicators, e.g. \"Doe-2005-a\". Separated by \"|\".  ", label = "first author")
	private final String publicationFirstAuthor;

	/**
	 * @param pmid
	 * @param dipPublicationId
	 * @param publicationFirstAuthor
	 */
	public DipPublication(PubMedID pmid, DipPublicationId dipPublicationId, String publicationFirstAuthor) {
		super();
		this.pmid = pmid;
		this.dipPublicationId = dipPublicationId;
		this.publicationFirstAuthor = publicationFirstAuthor;
	}

	/**
	 * @return the pmid
	 */
	public PubMedID getPmid() {
		return pmid;
	}

	/**
	 * @return the dipPublicationId
	 */
	public DipPublicationId getDipPublicationId() {
		return dipPublicationId;
	}

	/**
	 * @return the publicationFirstAuthor
	 */
	public String getPublicationFirstAuthor() {
		return publicationFirstAuthor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dipPublicationId == null) ? 0 : dipPublicationId.hashCode());
		result = prime * result + ((pmid == null) ? 0 : pmid.hashCode());
		result = prime * result + ((publicationFirstAuthor == null) ? 0 : publicationFirstAuthor.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DipPublication other = (DipPublication) obj;
		if (dipPublicationId == null) {
			if (other.dipPublicationId != null)
				return false;
		} else if (!dipPublicationId.equals(other.dipPublicationId))
			return false;
		if (pmid == null) {
			if (other.pmid != null)
				return false;
		} else if (!pmid.equals(other.pmid))
			return false;
		if (publicationFirstAuthor == null) {
			if (other.publicationFirstAuthor != null)
				return false;
		} else if (!publicationFirstAuthor.equals(other.publicationFirstAuthor))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DipPublication [pmid=" + pmid + ", dipPublicationId=" + dipPublicationId + ", publicationFirstAuthor="
				+ publicationFirstAuthor + "]";
	}

}
