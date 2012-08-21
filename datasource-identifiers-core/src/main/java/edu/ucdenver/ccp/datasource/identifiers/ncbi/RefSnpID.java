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
 * Reference SNP cluster ID definition for NCBI Single Nucleotide Polymorphysm database.<p> 
 * From dbSNP Handbook:<p>
 * <i>
 * Once a new SNP is submitted to dbSNP, it is assigned a unique submitted SNP ID number (ss#). 
 * Once the ss number is assigned, we align the flanking sequence of each submitted SNP to its appropriate genomic contig. 
 * If several ss numbers map to the same position on the contig, we cluster them together, call the cluster a "reference SNP cluster", 
 * or "refSNP", and provide the cluster with a unique RefSNP ID number (rs#). If only one ss number maps to a specific position,
 * then that ss is assigned an rs number and is the only member of its RefSNP cluster until another submitted SNP is found 
 * that maps to the same position.
 * </i>
 * 
 * @author Yuriy Malenkiy
 *
 */
public class RefSnpID extends DataSourceIdentifier<String> {

	public RefSnpID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.REFSNP;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && resourceID.startsWith("rs") && NumberUtils.isDigits(resourceID.substring(2)))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid Reference SNP ID detected: %s", resourceID));
	}

}
