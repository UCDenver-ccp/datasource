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
package edu.ucdenver.ccp.fileparsers.ncbi.gene;

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
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.fileparsers.idlist.IdListFileFactory;
import edu.ucdenver.ccp.fileparsers.reactome.ReactomeUniprot2PathwayStidTxtFileData;

/**
 * This class is used to parse the InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class EntrezGeneRefSeqUniprotKbCollabFileParser extends
		TaxonAwareSingleLineFileRecordReader<EntrezGeneRefSeqUniprotKbCollabFileData> {

	private static final String HEADER = "#Format: NCBI_protein_accession UniProtKB_protein_accession (tab is used as a separator, pound sign - start of a comment)";

	public static final String FTP_FILE_NAME = "gene_refseq_uniprotkb_collab.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	private final Set<UniProtID> taxonSpecificIds;

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File geneRefseqUniprotKbCollabFile;

	public EntrezGeneRefSeqUniprotKbCollabFileParser(File gene2PubmedFile, CharacterEncoding encoding)
			throws IOException {
		super(gene2PubmedFile, encoding, null);
		taxonSpecificIds = null;
	}

	public EntrezGeneRefSeqUniprotKbCollabFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
		taxonSpecificIds = null;
	}

	public EntrezGeneRefSeqUniprotKbCollabFileParser(File gene2PubmedFile, CharacterEncoding encoding,
			File idListDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(gene2PubmedFile, encoding, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, DataSource.UNIPROT, taxonIds,
				UniProtID.class);
	}

	public EntrezGeneRefSeqUniprotKbCollabFileParser(File workDirectory, boolean clean, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, DataSource.UNIPROT, taxonIds,
				UniProtID.class);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(geneRefseqUniprotKbCollabFile)), encoding,
				skipLinePrefix);
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected EntrezGeneRefSeqUniprotKbCollabFileData parseRecordFromLine(Line line) {
		return EntrezGeneRefSeqUniprotKbCollabFileData.parseGeneRefseqUniprotKbCollabLine(line);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		EntrezGeneRefSeqUniprotKbCollabFileData record = parseRecordFromLine(line);
		if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty() && taxonSpecificIds.contains(record.getUniprotId())) {
			// here we have matched the record uniprot id as one of the ids of interest. We don't
			// know exactly what taxon it is however so we just return one (arbitrarily) of the
			// taxon ids of interest. this will ensure this record is returned.
			return taxonsOfInterest.iterator().next();
		}
		return new NcbiTaxonomyID(0);
	}

}
