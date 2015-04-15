package edu.ucdenver.ccp.datasource.fileparsers.ebi.interpro;

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

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

public class InterProProtein2IprDatFileParserTestFn extends DefaultTestCase {

	/**
	 * This functional test downloads the real protein2ipr.dat file from the InterPro FTP site. It
	 * checks that the download succeeded and that it's been unzipped, then proceeds to parse the
	 * entire file to make sure the file format has not changed unexpected.
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void testDownloadAndParseOfRealFile() throws Exception {
		File workDirectory = folder.newFolder("work");
		InterProProtein2IprDatFileParser parser = new InterProProtein2IprDatFileParser(workDirectory, true);
		File expectedDownloadedFile = FileUtil.appendPathElementsToDirectory(workDirectory,
				StringUtil.removeSuffix(InterProProtein2IprDatFileParser.FTP_FILE_NAME,".gz"));
		assertTrue(String.format("file must exist"), expectedDownloadedFile.exists());
		assertTrue(String.format("file must not be empty"), expectedDownloadedFile.length() > 0);
		while (parser.hasNext())
			parser.next();
	}
}
