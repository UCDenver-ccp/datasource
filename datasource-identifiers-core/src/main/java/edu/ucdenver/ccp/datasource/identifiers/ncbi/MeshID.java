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
package edu.ucdenver.ccp.datasource.identifiers.ncbi;

import org.apache.commons.lang.math.NumberUtils;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * ID for Medical Subject Heading definition as described by www.nlm.nih.gov/mesh
 * 
 * @author Yuriy Malenkiy
 * 
 */
public class MeshID extends DataSourceIdentifier<String> {

	// private static final String ID_PREFIX = "MESH_";

	/**
	 * @param resourceID
	 */
	public MeshID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MESH;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && resourceID.toUpperCase().startsWith("D")
				&& NumberUtils.isDigits(resourceID.substring(1)))
			return resourceID;

		throw new IllegalArgumentException(String.format("Invalid Mesh ID : %s", resourceID));
	}

}
