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
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

/**
 * This class is used to parse the EntrezGene gene2accession file.
 * 
 * @author Bill Baumgartner
 * 
 */
public class EntrezGene2RefseqFileParser extends TaxonAwareSingleLineFileRecordReader<EntrezGene2RefseqFileData> {

	private static final String HEADER = "#Format: tax_id GeneID status RNA_nucleotide_accession.version RNA_nucleotide_gi protein_accession.version protein_gi genomic_nucleotide_accession.version genomic_nucleotide_gi start_position_on_the_genomic_accession end_position_on_the_genomic_accession orientation assembly mature_peptide_accession.version mature_peptide_gi Symbol (tab is used as a separator, pound sign - start of a comment)";

	public static final String FTP_FILE_NAME = "gene2refseq.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File gene2accessionFile;

	public EntrezGene2RefseqFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public EntrezGene2RefseqFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
	}
	public EntrezGene2RefseqFileParser(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(file, encoding, taxonIds);
	}
	
	public EntrezGene2RefseqFileParser(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds) throws IOException {
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
	protected EntrezGene2RefseqFileData parseRecordFromLine(Line line) {
		return EntrezGene2RefseqFileData.parseGene2AccessionLine(line);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		EntrezGene2RefseqFileData record = parseRecordFromLine(line);
		return record.getTaxonID();
	}

	
	/**
	 * 
	 * @param entrezGene2AccessionFile
	 * @return
	 * @throws IOException
	 */
	public static Map<GiNumberID, Set<EntrezGeneID>> getProteinGiID2EntrezGeneIDMap(File entrezGene2AccessionFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID) throws IOException {
		Map<GiNumberID, Set<EntrezGeneID>> proteinAccessionID2EntrezGeneIDMap = new HashMap<GiNumberID, Set<EntrezGeneID>>();
		EntrezGene2RefseqFileParser parser = null;
		try {
			parser = new EntrezGene2RefseqFileParser(entrezGene2AccessionFile, encoding);

			while (parser.hasNext()) {
				EntrezGene2RefseqFileData dataRecord = parser.next();
				if (dataRecord.getTaxonID().equals(taxonID)) {
					EntrezGeneID entrezGeneID = dataRecord.getGeneID();
					GiNumberID accessionID = dataRecord.getProtein_gi();

					if (accessionID != null) {
						if (proteinAccessionID2EntrezGeneIDMap.containsKey(accessionID)) {
							proteinAccessionID2EntrezGeneIDMap.get(accessionID).add(entrezGeneID);
						} else {
							Set<EntrezGeneID> entrezGeneIDs = new HashSet<EntrezGeneID>();
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
	public static Map<RefSeqID, Set<EntrezGeneID>> getProteinAccessionID2EntrezGeneIDMap(
			File entrezGene2AccessionOrReseqFile, CharacterEncoding encoding, NcbiTaxonomyID taxonID)
			throws IOException {
		Map<RefSeqID, Set<EntrezGeneID>> proteinAccessionID2EntrezGeneIDMap = new HashMap<RefSeqID, Set<EntrezGeneID>>();
		EntrezGene2RefseqFileParser parser = null;
		try {
			parser = new EntrezGene2RefseqFileParser(entrezGene2AccessionOrReseqFile, encoding);

			while (parser.hasNext()) {
				EntrezGene2RefseqFileData dataRecord = parser.next();
				if (dataRecord.getTaxonID().equals(taxonID)) {
					EntrezGeneID entrezGeneID = dataRecord.getGeneID();
					RefSeqID accessionID = dataRecord.getProtein_accession_dot_version();

					if (accessionID != null) {

						String refseqIDStr = accessionID.getDataElement();
						RefSeqID refseqIDForOutputMap = accessionID;
						if (refseqIDStr.contains(".")) {
							refseqIDForOutputMap = new RefSeqID(refseqIDStr.substring(0, refseqIDStr.lastIndexOf(".")));
						}

						if (proteinAccessionID2EntrezGeneIDMap.containsKey(refseqIDForOutputMap)) {
							proteinAccessionID2EntrezGeneIDMap.get(refseqIDForOutputMap).add(entrezGeneID);
						} else {
							Set<EntrezGeneID> entrezGeneIDs = new HashSet<EntrezGeneID>();
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
