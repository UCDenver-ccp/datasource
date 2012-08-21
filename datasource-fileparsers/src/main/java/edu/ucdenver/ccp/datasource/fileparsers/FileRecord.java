/*
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
package edu.ucdenver.ccp.fileparsers;

/**
 * Base file class for records captured from data files. Supports specification of location within
 * file.
 * 
 */
public abstract class FileRecord implements DataRecord {

	/**
	 * Specifies the start of the record within the file (offset from file beginning)
	 */
	private final long byteOffset;

	public FileRecord(long byteOffset) {
		this.byteOffset = byteOffset;
	}

	public long getByteOffset() {
		return byteOffset;
	}
}
