/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.dip;

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

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

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
