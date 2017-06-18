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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;

/**
 * This class is used to parse the EntrezGene gene2accession file.
 * 
 * @author Bill Baumgartner
 * 
 */
public class NcbiGene2RefseqFileParser extends TaxonAwareSingleLineFileRecordReader<NcbiGene2RefseqFileData> {

	private static final String HEADER = "#tax_id\tGeneID\tstatus\tRNA_nucleotide_accession.version\tRNA_nucleotide_gi\tprotein_accession.version\tprotein_gi\tgenomic_nucleotide_accession.version\tgenomic_nucleotide_gi\tstart_position_on_the_genomic_accession\tend_position_on_the_genomic_accession\torientation\tassembly\tmature_peptide_accession.version\tmature_peptide_gi\tSymbol";

	public static final String FTP_FILE_NAME = "gene2refseq.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File gene2accessionFile;

	public NcbiGene2RefseqFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public NcbiGene2RefseqFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
	}
	public NcbiGene2RefseqFileParser(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(file, encoding, taxonIds);
	}
	
	public NcbiGene2RefseqFileParser(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(gene2accessionFile)), encoding, skipLinePrefix);
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
	protected NcbiGene2RefseqFileData parseRecordFromLine(Line line) {
		return NcbiGene2RefseqFileData.parseGene2AccessionLine(line);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		NcbiGene2RefseqFileData record = parseRecordFromLine(line);
		return record.getTaxonID();
	}

	
	/**
	 * 
	 * @param entrezGene2AccessionFile
	 * @return
	 * @throws IOException
	 */
	public static Map<GiNumberID, Set<NcbiGeneId>> getProteinGiID2EntrezGeneIDMap(File entrezGene2AccessionFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID) throws IOException {
		Map<GiNumberID, Set<NcbiGeneId>> proteinAccessionID2EntrezGeneIDMap = new HashMap<GiNumberID, Set<NcbiGeneId>>();
		NcbiGene2RefseqFileParser parser = null;
		try {
			parser = new NcbiGene2RefseqFileParser(entrezGene2AccessionFile, encoding);

			while (parser.hasNext()) {
				NcbiGene2RefseqFileData dataRecord = parser.next();
				if (dataRecord.getTaxonID().equals(taxonID)) {
					NcbiGeneId entrezGeneID = dataRecord.getGeneID();
					GiNumberID accessionID = dataRecord.getProtein_gi();

					if (accessionID != null) {
						if (proteinAccessionID2EntrezGeneIDMap.containsKey(accessionID)) {
							proteinAccessionID2EntrezGeneIDMap.get(accessionID).add(entrezGeneID);
						} else {
							Set<NcbiGeneId> entrezGeneIDs = new HashSet<NcbiGeneId>();
							entrezGeneIDs.add(entrezGeneID);
							proteinAccessionID2EntrezGeneIDMap.put(accessionID, entrezGeneIDs);
						}
					}
				}
			}
		} finally {
			if (parser != null)
				parser.close();
		}
		return proteinAccessionID2EntrezGeneIDMap;
	}

	/**
	 * 
	 * @param entrezGene2AccessionFile
	 * @return
	 * @throws IOException
	 */
	public static Map<RefSeqID, Set<NcbiGeneId>> getProteinAccessionID2EntrezGeneIDMap(
			File entrezGene2AccessionOrReseqFile, CharacterEncoding encoding, NcbiTaxonomyID taxonID)
			throws IOException {
		Map<RefSeqID, Set<NcbiGeneId>> proteinAccessionID2EntrezGeneIDMap = new HashMap<RefSeqID, Set<NcbiGeneId>>();
		NcbiGene2RefseqFileParser parser = null;
		try {
			parser = new NcbiGene2RefseqFileParser(entrezGene2AccessionOrReseqFile, encoding);

			while (parser.hasNext()) {
				NcbiGene2RefseqFileData dataRecord = parser.next();
				if (dataRecord.getTaxonID().equals(taxonID)) {
					NcbiGeneId entrezGeneID = dataRecord.getGeneID();
					RefSeqID accessionID = dataRecord.getProtein_accession_dot_version();

					if (accessionID != null) {

						String refseqIDStr = accessionID.getId();
						RefSeqID refseqIDForOutputMap = accessionID;
						if (refseqIDStr.contains(".")) {
							refseqIDForOutputMap = new RefSeqID(refseqIDStr.substring(0, refseqIDStr.lastIndexOf(".")));
						}

						if (proteinAccessionID2EntrezGeneIDMap.containsKey(refseqIDForOutputMap)) {
							proteinAccessionID2EntrezGeneIDMap.get(refseqIDForOutputMap).add(entrezGeneID);
						} else {
							Set<NcbiGeneId> entrezGeneIDs = new HashSet<NcbiGeneId>();
							entrezGeneIDs.add(entrezGeneID);
							proteinAccessionID2EntrezGeneIDMap.put(refseqIDForOutputMap, entrezGeneIDs);
						}
					}
				}
			}
		} finally {
			if (parser != null) {
				parser.close();
			}
		}

		return proteinAccessionID2EntrezGeneIDMap;
	}

	
}
