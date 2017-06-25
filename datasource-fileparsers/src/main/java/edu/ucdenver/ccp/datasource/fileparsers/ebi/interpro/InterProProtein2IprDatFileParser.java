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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.idlist.IdListFileFactory;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * This class is used to parse the InterPro protein2ipr.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class InterProProtein2IprDatFileParser extends
		TaxonAwareSingleLineFileRecordReader<InterProProtein2IprDatFileData> {

	public static final String FTP_SERVER = "ftp.ebi.ac.uk";
	public static final String FTP_PATH = "pub/databases/interpro";
	public static final String FTP_FILE_NAME = "protein2ipr.dat.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	private final Set<UniProtID> taxonSpecificIds;

	@FtpDownload(server = FTP_SERVER, path = FTP_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File interProProtein2IprDatFile;

	public InterProProtein2IprDatFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
		taxonSpecificIds = null;
	}

	public InterProProtein2IprDatFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
		taxonSpecificIds = null;
	}

	public InterProProtein2IprDatFileParser(File file, CharacterEncoding encoding, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		super(file, encoding, null, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory, DataSource.UNIPROT, taxonIds,
				UniProtID.class, cleanIdListFiles);
	}

	public InterProProtein2IprDatFileParser(File workDirectory, boolean clean, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds,File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory, DataSource.UNIPROT, taxonIds,
				UniProtID.class, cleanIdListFiles);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(interProProtein2IprDatFile)), encoding,
				skipLinePrefix);
	}

	@Override
	protected InterProProtein2IprDatFileData parseRecordFromLine(Line line) {
		return InterProProtein2IprDatFileData.parseInterProProtein2IprDatLine(line);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		InterProProtein2IprDatFileData record = parseRecordFromLine(line);
		if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty() && taxonSpecificIds.contains(record.getUniProtID())) {
			// here we have matched the record uniprot id as one of the ids of interest. We don't
			// know exactly what taxon it is however so we just return one (arbitrarily) of the
			// taxon ids of interest. this will ensure this record is returned.
			return taxonsOfInterest.iterator().next();
		}
		return new NcbiTaxonomyID(0);
	}

}
