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
package edu.ucdenver.ccp.fileparsers.ncbi.refseq;

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
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse the RefSeq RefSeq-release47.catalog.gz file
 * 
 * @author Bill Baumgartner
 * 
 */
public class RefSeqReleaseCatalogFileParser extends TaxonAwareSingleLineFileRecordReader<RefSeqReleaseCatalogFileData> {

	// TODO: Modification to the FTPDownload annotation to allow for a regex for the file name will
	// make this more flexible, i.e. RefSeq-release\\d+.catalog.gz
	public static final String FTP_FILE_NAME = "RefSeq-release69.catalog.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.REFSEQ_HOST, path = FtpHost.REFSEQ_CATALOG_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File refseqReleaseCatalogFile;

	public RefSeqReleaseCatalogFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public RefSeqReleaseCatalogFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
	}

	public RefSeqReleaseCatalogFileParser(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(file, encoding, taxonIds);
	}

	public RefSeqReleaseCatalogFileParser(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(refseqReleaseCatalogFile)), encoding,
				skipLinePrefix);
	}

	@Override
	protected RefSeqReleaseCatalogFileData parseRecordFromLine(Line line) {
		return RefSeqReleaseCatalogFileData.parseRefSeqReleaseCatalogLine(line);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		RefSeqReleaseCatalogFileData record = parseRecordFromLine(line);
		return record.getTaxId();
	}

}
