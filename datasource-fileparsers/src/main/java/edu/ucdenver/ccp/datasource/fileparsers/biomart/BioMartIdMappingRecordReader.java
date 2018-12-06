package edu.ucdenver.ccp.datasource.fileparsers.biomart;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2018 Regents of the University of Colorado
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

public class BioMartIdMappingRecordReader extends SingleLineFileRecordReader<BioMartIdMappingFileData> {

	private static final String EXPECTED_HEADER = "Gene stable ID\tTranscript stable ID\tNCBI gene ID\tHGNC ID\tUniProtKB/Swiss-Prot ID";

	public BioMartIdMappingRecordReader(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding);
	}

	public BioMartIdMappingRecordReader(InputStream stream, CharacterEncoding encoding) throws IOException {
		super(stream, encoding, null);
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return EXPECTED_HEADER;
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected BioMartIdMappingFileData parseRecordFromLine(Line line) {
		return BioMartIdMappingFileData.parseLine(line);
	}

	public static void main(String[] args) {
		File f = new File("/Users/bill/Downloads/mart_export.txt.gz");
		try {
			int count = 0;
			for (BioMartIdMappingRecordReader rr = new BioMartIdMappingRecordReader(
					new GZIPInputStream(new FileInputStream(f)), CharacterEncoding.UTF_8); rr.hasNext();) {
				if (count++ % 1000 == 0) {
					System.out.println("progress: " + count);
				}
				rr.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
