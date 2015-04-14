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
package edu.ucdenver.ccp.fileparsers.kegg;

import java.io.File;
import java.io.IOException;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;

/**
 * This class is used to parse the Kegg genome file
 * 
 * @author Bill Baumgartner
 * 
 */
public class KeggGenesFileParser extends MultiLineFileRecordReader<KeggGenesFileData> {

	public KeggGenesFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	@Override
	protected void initialize() throws IOException {
		line = readLine();
		super.initialize();
	}

	@Override
	protected MultiLineBuffer compileMultiLineBuffer() throws IOException {
		if (line == null)
			return null;
		MultiLineBuffer multiLineBuffer = new MultiLineBuffer();
		do {
			multiLineBuffer.add(line);
			line = readLine();
		} while (line != null && !line.getText().startsWith("///"));
		line = readLine();
		return multiLineBuffer;
	}

	@Override
	protected KeggGenesFileData parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer) {
		return KeggGenesFileData.parseKeggGenesFileRecord(multiLineBuffer);
	}

}
