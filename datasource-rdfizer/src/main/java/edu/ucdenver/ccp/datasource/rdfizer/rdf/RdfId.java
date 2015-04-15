package edu.ucdenver.ccp.datasource.rdfizer.rdf;

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

import java.net.URI;

import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil;

/**
 * RDF wrapper for {@link DataElementIdentifier}
 * 
 * @author Yuriy Malenkiy
 * 
 */
public class RdfId {

	public static final String ICE_URI_SUFFIX = "_ICE";

	private final DataSourceIdentifier<?> id;

	/**
	 * Default constructor
	 * 
	 * @param id
	 */
	public RdfId(DataSourceIdentifier<?> id) {
		this.id = id;
	}

	/**
	 * Provides a means to retrieve a URI representing this particular resource ID.
	 * 
	 * @return
	 */
	public URI getUri() {
		return RdfUtil.createUri(DataSource.getNamespace(id.getDataSource()), id.toString());
	}

	/**
	 * Returns a Resource to be used in an RDF Statement object
	 * 
	 * @return
	 */
	public Resource getRdfResource() {
		return new URIImpl(getUri().toString());
	}

	/**
	 * Get value based on {@link #getUri()}
	 */
	public Value getRdfValue() {
		return new URIImpl(getUri().toString());
	}

	/**
	 * Returns the resourceID
	 * 
	 * @return
	 */
	public Object getResourceID() {
		return id.getDataElement();
	}
	
	public URI getInformationContentEntityURI() {
		return RdfUtil.createUri(DataSource.getNamespace(id.getDataSource()), getICE_ID());
	}

	public String getICE_ID() {
		String idWithUnderscore = id.toString().replaceAll(":", "_");
		if (idWithUnderscore.startsWith(getNamespace().getLocalName() + "_")) {
			return idWithUnderscore + ICE_URI_SUFFIX;
		}
		return getNamespace().getLocalName() + "_" + idWithUnderscore + ICE_URI_SUFFIX;
	}

	public DataSource getNamespace() {
		return DataSource.getNamespace(id.getDataSource());
	}
}
