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
package edu.ucdenver.ccp.fileparsers.rgd;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

/**
 * This class is used to parse the PreMod mouse_module_tab.txt file
 * 
 * @author Bill Baumgartner
 * 
 */
public class RgdGeneFileRecordReaderBase extends SingleLineFileRecordReader<RgdGeneFileRecord> {

	public static final String FTP_SERVER = "rgd.mcw.edu";
	public static final String FTP_PATH = "pub/data_release";

	public static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;
	public static final String SKIP_LINE_PREFIX = StringConstants.POUND_SIGN;

	private static Logger logger = Logger.getLogger(RgdGeneFileRecordReaderBase.class);

	@Getter
	private String oldReferenceAssemblyNumber;
	@Getter
	private String currentReferenceAssemblyNumber;
	
	public RgdGeneFileRecordReaderBase(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, SKIP_LINE_PREFIX);
	}

	protected RgdGeneFileRecordReaderBase(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, SKIP_LINE_PREFIX, null, null, clean);
	}

	@Override
	protected String getFileHeader() throws IOException {
		String headerLine = readLine().getText();
		Pattern assemblyPattern = Pattern.compile("CHROMOSOME_(\\d\\.\\d)\\sCHROMOSOME_(\\d\\.\\d)");
		Matcher m = assemblyPattern.matcher(headerLine);
		if (m.find()) {
			oldReferenceAssemblyNumber = m.group(1);
			currentReferenceAssemblyNumber = m.group(2);
		} else {
			throw new IOException("Could not extract assembly numbers from file header: " + headerLine);
		}
		return headerLine;
	}

	@Override
	protected RgdGeneFileRecord parseRecordFromLine(Line line) {
		return RgdGeneFileRecord.parseGeneFileLine(line, oldReferenceAssemblyNumber, currentReferenceAssemblyNumber);
	}

}
