package edu.ucdenver.ccp.datasource.fileparsers.rnacentral;

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
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

public class RnaCentralIdMappingRecordReader extends TaxonAwareSingleLineFileRecordReader<RnaCentralIdMappingFileData> {

	@FtpDownload(server = "ftp.ebi.ac.uk", path = "pub/databases/RNAcentral/current_release/id_mapping", filename = "id_mapping.tsv.gz", filetype = FileType.BINARY)
	private File tsvFile;

	public RnaCentralIdMappingRecordReader(File dataFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(dataFile, encoding, taxonsOfInterest);
	}

	public RnaCentralIdMappingRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(workDirectory, encoding, null, clean, taxonsOfInterest);
	}

	public RnaCentralIdMappingRecordReader(File workDirectory, CharacterEncoding encoding, String ftpUsername,
			String ftpPassword, boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(workDirectory, encoding, null, ftpUsername, ftpPassword, clean, taxonsOfInterest);
	}

	public RnaCentralIdMappingRecordReader(InputStream stream, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(stream, encoding, null, taxonsOfInterest);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(tsvFile, encoding, null);
	}

	@Override
	protected RnaCentralIdMappingFileData parseRecordFromLine(Line line) {
		return RnaCentralIdMappingFileData.parseLine(line);
	}

	@Override
	protected Set<NcbiTaxonomyID> getLineTaxon(Line line) {
		RnaCentralIdMappingFileData record = RnaCentralIdMappingFileData.parseLine(line);
		return CollectionsUtil.createSet(record.getNcbiTaxonomyId());
	}

}
