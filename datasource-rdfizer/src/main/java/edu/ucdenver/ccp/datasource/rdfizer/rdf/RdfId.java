/**
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
package edu.ucdenver.ccp.datasource.rdfizer.rdf;

import java.net.URI;

import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
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
		return RdfUtil.createUri(RdfNamespace.getNamespace(id.getDataSource()), id.toString());
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
		return RdfUtil.createUri(RdfNamespace.getNamespace(id.getDataSource()), getICE_ID());
	}

	public String getICE_ID() {
		String idWithUnderscore = id.toString().replaceAll(":", "_");
		if (idWithUnderscore.startsWith(getNamespace().getLocalName() + "_")) {
			return idWithUnderscore + ICE_URI_SUFFIX;
		}
		return getNamespace().getLocalName() + "_" + idWithUnderscore + ICE_URI_SUFFIX;
	}

	public RdfNamespace getNamespace() {
		return RdfNamespace.getNamespace(id.getDataSource());
	}
}
