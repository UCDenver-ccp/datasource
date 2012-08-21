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

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import edu.ucdenver.ccp.common.string.StringConstants;

/**
 * Abstract class for reading data records from a file.
 * 
 * @author Bill Baumgartner
 * 
 */
public abstract class RecordReader<T extends DataRecord> implements Closeable, Iterator<T> {

	/**
	 * Optional and overridable initialization callback.
	 * @throws IOException
	 */
	protected void initialize() throws IOException {}



	/**
	 * This method allows a record reader to introduce a dataset-specific string as a key to what
	 * kind of data is actually being processed. It is useful for record readers that correspond
	 * with multiple files, e.g. some of the KeggRecordReaders can be used on files from many
	 * different species so it is beneficial to add a species-specific string to the output file
	 * name to avoid overwriting. This key can be used to as part of such a file name (or for any
	 * other purpose). By default this method returns a blank string. It must be overriden by
	 * subclasses that need to use it.
	 * 
	 * @return
	 */
	public String getDataSpecificKey() {
		return StringConstants.BLANK;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("This operation is not supported.");
	}
	
}
