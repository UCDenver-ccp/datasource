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
package edu.ucdenver.ccp.fileparsers.pro;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class ProMappingFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_PRO_MAPPINGS_FILE_NAME = "PRO_promapping.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_PRO_MAPPINGS_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new ProMappingFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Ignore("Test not yet implemented.. ")
	@Test
	public void testParser() {
		

	}

	
}
