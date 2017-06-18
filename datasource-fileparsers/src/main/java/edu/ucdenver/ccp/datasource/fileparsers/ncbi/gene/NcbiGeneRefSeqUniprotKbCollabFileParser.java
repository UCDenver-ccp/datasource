package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

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
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.fileparsers.idlist.IdListFileFactory;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * This class is used to parse the InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class NcbiGeneRefSeqUniprotKbCollabFileParser extends
		TaxonAwareSingleLineFileRecordReader<NcbiGeneRefSeqUniprotKbCollabFileData> {

	private static final String HEADER = "#NCBI_protein_accession\tUniProtKB_protein_accession";

	public static final String FTP_FILE_NAME = "gene_refseq_uniprotkb_collab.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	private final Set<UniProtID> taxonSpecificIds;

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File geneRefseqUniprotKbCollabFile;

	public NcbiGeneRefSeqUniprotKbCollabFileParser(File gene2PubmedFile, CharacterEncoding encoding)
			throws IOException {
		super(gene2PubmedFile, encoding, null);
		taxonSpecificIds = null;
	}

	public NcbiGeneRefSeqUniprotKbCollabFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
		taxonSpecificIds = null;
	}

	public NcbiGeneRefSeqUniprotKbCollabFileParser(File gene2PubmedFile, CharacterEncoding encoding,
			File idListDirectory, Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory, boolean cleanIdListFiles)
			throws IOException {
		super(gene2PubmedFile, encoding, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonIds, UniProtID.class, cleanIdListFiles);
	}

	public NcbiGeneRefSeqUniprotKbCollabFileParser(File workDirectory, boolean clean, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonIds, UniProtID.class, cleanIdListFiles);
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
	protected NcbiGeneRefSeqUniprotKbCollabFileData parseRecordFromLine(Line line) {
		return NcbiGeneRefSeqUniprotKbCollabFileData.parseGeneRefseqUniprotKbCollabLine(line);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		NcbiGeneRefSeqUniprotKbCollabFileData record = parseRecordFromLine(line);
		if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty() && taxonSpecificIds.contains(record.getUniprotId())) {
			// here we have matched the record uniprot id as one of the ids of
			// interest. We don't
			// know exactly what taxon it is however so we just return one
			// (arbitrarily) of the
			// taxon ids of interest. this will ensure this record is returned.
			return taxonsOfInterest.iterator().next();
		}
		return new NcbiTaxonomyID(0);
	}

}
