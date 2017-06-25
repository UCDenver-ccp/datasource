package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * This parser for the UniProt idmapping_selected.tab file located at
 * ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/idmapping <br>
 * <br>
 * NOTE: The current implementation of UniProtIDMappingFileData is an incomplete representation of
 * the data located in the idmapping_selected.tab file (there are more columns than the three
 * extracted currently).
 * 
 * @author Bill Baumgartner
 * 
 */
public class UniProtIDMappingFileRecordReader extends TaxonAwareSingleLineFileRecordReader<UniProtIDMappingFileData> {

	public static final String FTP_FILE_NAME = "idmapping_selected.tab.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.UNIPROT_IDMAPPING_HOST, path = FtpHost.UNIPROT_IDMAPPING_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File idMappingSelectedTabFile;

//	public UniProtIDMappingFileRecordReader(File file, CharacterEncoding encoding) throws IOException {
//		super(file, encoding, null);
//	}
//
//	public UniProtIDMappingFileRecordReader(File workDirectory, boolean clean) throws IOException {
//		super(workDirectory, ENCODING, null, null, null, clean, null);
//	}

	public UniProtIDMappingFileRecordReader(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(file, encoding, null, taxonIds);
	}

	public UniProtIDMappingFileRecordReader(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(idMappingSelectedTabFile)), encoding,
				skipLinePrefix);
	}

	@Override
	protected UniProtIDMappingFileData parseRecordFromLine(Line line) {
		return UniProtIDMappingFileData.parseRecordFromLine(line);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		UniProtIDMappingFileData record = parseRecordFromLine(line);
		return record.getTaxonomyID();
	}

//	/**
//	 * Returns a mapping from uniprot ID to entrez gene ID(s)
//	 * 
//	 * @param uniprotIdMappingFile
//	 * @return
//	 * @throws IOException
//	 */
//	public static Map<UniProtID, Set<EntrezGeneID>> getUniProtIDToEntrezGeneIDsMap(File uniprotIdMappingFile,
//			CharacterEncoding encoding, NcbiTaxonomyID taxonomyID) throws IOException {
//		Map<UniProtID, Set<EntrezGeneID>> uniProtID2EntrezGeneIDsMap = new HashMap<UniProtID, Set<EntrezGeneID>>();
//		UniProtIDMappingFileRecordReader parser = new UniProtIDMappingFileRecordReader(uniprotIdMappingFile, encoding);
//
//		while (parser.hasNext()) {
//			UniProtIDMappingFileData dataRecord = parser.next();
//			Set<EntrezGeneID> entrezGeneIDs = dataRecord.getEntrezGeneIDs();
//			if (entrezGeneIDs != null) {
//				NcbiTaxonomyID recordTaxonomyID = dataRecord.getTaxonomyID();
//				if (recordTaxonomyID != null && taxonomyID.equals(recordTaxonomyID)) {
//					for (EntrezGeneID entrezGeneID : entrezGeneIDs) {
//						UniProtID uniProtID = dataRecord.getUniProtAccessionID();
//						if (uniProtID2EntrezGeneIDsMap.containsKey(uniProtID)) {
//							uniProtID2EntrezGeneIDsMap.get(uniProtID).add(entrezGeneID);
//						} else {
//							Set<EntrezGeneID> egIDs = new HashSet<EntrezGeneID>();
//							egIDs.add(entrezGeneID);
//							uniProtID2EntrezGeneIDsMap.put(uniProtID, egIDs);
//						}
//					}
//				}
//			}
//		}
//		parser.close();
//
//		return uniProtID2EntrezGeneIDsMap;
//	}
//
//	/**
//	 * Returns a mapping from entrez gene ID to uniProt ID(s)
//	 * 
//	 * @param uniprotIdMappingFile
//	 * @return
//	 * @throws IOException
//	 */
//	public static Map<EntrezGeneID, Set<UniProtID>> getEntrezGeneID2UniProtIDsMap(File uniprotIdMappingFile,
//			CharacterEncoding encoding, NcbiTaxonomyID taxonomyID) throws IOException {
//		Map<EntrezGeneID, Set<UniProtID>> entrezGeneID2UniProtIDsMap = new HashMap<EntrezGeneID, Set<UniProtID>>();
//		UniProtIDMappingFileRecordReader parser = new UniProtIDMappingFileRecordReader(uniprotIdMappingFile, encoding);
//
//		while (parser.hasNext()) {
//			UniProtIDMappingFileData dataRecord = parser.next();
//			Set<EntrezGeneID> entrezGeneIDs = dataRecord.getEntrezGeneIDs();
//			if (entrezGeneIDs != null) {
//				NcbiTaxonomyID recordTaxonomyID = dataRecord.getTaxonomyID();
//				if (taxonomyID == null || (recordTaxonomyID != null && taxonomyID.equals(recordTaxonomyID))) {
//					for (EntrezGeneID entrezGeneID : entrezGeneIDs) {
//						UniProtID uniProtID = dataRecord.getUniProtAccessionID();
//						if (entrezGeneID2UniProtIDsMap.containsKey(entrezGeneID)) {
//							entrezGeneID2UniProtIDsMap.get(entrezGeneID).add(uniProtID);
//						} else {
//							Set<UniProtID> uniprotIDs = new HashSet<UniProtID>();
//							uniprotIDs.add(uniProtID);
//							entrezGeneID2UniProtIDsMap.put(entrezGeneID, uniprotIDs);
//						}
//					}
//				}
//			}
//		}
//		parser.close();
//		return entrezGeneID2UniProtIDsMap;
//	}
//
//	/**
//	 * Returns a mapping from EMBL ID to EntrezGeneID(s)
//	 * 
//	 * @param uniprotIdMappingFile
//	 * @param taxonomyID
//	 * @return
//	 * @throws IOException
//	 */
//	public static Map<EmblID, Set<EntrezGeneID>> getEmblToEntrezGeneIDsMap(File uniprotIdMappingFile,
//			CharacterEncoding encoding, NcbiTaxonomyID taxonomyID) throws IOException {
//		Map<EmblID, Set<EntrezGeneID>> embl2EntrezGeneIDsMap = new HashMap<EmblID, Set<EntrezGeneID>>();
//		UniProtIDMappingFileRecordReader parser = new UniProtIDMappingFileRecordReader(uniprotIdMappingFile, encoding);
//
//		while (parser.hasNext()) {
//			UniProtIDMappingFileData dataRecord = parser.next();
//			Set<EntrezGeneID> entrezGeneIDs = dataRecord.getEntrezGeneIDs();
//			if (entrezGeneIDs != null) {
//				NcbiTaxonomyID recordTaxonomyID = dataRecord.getTaxonomyID();
//				if (recordTaxonomyID != null && taxonomyID.equals(recordTaxonomyID)) {
//					for (EntrezGeneID entrezGeneID : entrezGeneIDs) {
//						Set<EmblID> emblIDs = dataRecord.getEmblIDs();
//						for (EmblID emblID : emblIDs) {
//							if (embl2EntrezGeneIDsMap.containsKey(emblID)) {
//								embl2EntrezGeneIDsMap.get(emblID).add(entrezGeneID);
//							} else {
//								Set<EntrezGeneID> egIDs = new HashSet<EntrezGeneID>();
//								egIDs.add(entrezGeneID);
//								embl2EntrezGeneIDsMap.put(emblID, egIDs);
//							}
//						}
//					}
//				}
//			}
//		}
//
//		parser.close();
//		return embl2EntrezGeneIDsMap;
//	}
//
//	/**
//	 * Returns a mapping from emblID to uniprot id(s)
//	 * 
//	 * @param uniprotIdMappingFile
//	 * @param taxonomyID
//	 * @return
//	 * @throws IOException
//	 */
//	public static Map<EmblID, Set<UniProtID>> getEmbl2UniProtIDsMap(File uniprotIdMappingFile,
//			CharacterEncoding encoding, NcbiTaxonomyID taxonomyID) throws IOException {
//		Map<EmblID, Set<UniProtID>> embl2UniProtIDsMap = new HashMap<EmblID, Set<UniProtID>>();
//		UniProtIDMappingFileRecordReader parser = new UniProtIDMappingFileRecordReader(uniprotIdMappingFile, encoding);
//
//		while (parser.hasNext()) {
//			UniProtIDMappingFileData dataRecord = parser.next();
//			NcbiTaxonomyID recordTaxonomyID = dataRecord.getTaxonomyID();
//			if (recordTaxonomyID != null && taxonomyID.equals(recordTaxonomyID)) {
//				UniProtID uniProtID = dataRecord.getUniProtAccessionID();
//				Set<EmblID> emblIDs = dataRecord.getEmblIDs();
//				for (EmblID emblID : emblIDs) {
//					if (embl2UniProtIDsMap.containsKey(emblID)) {
//						embl2UniProtIDsMap.get(emblID).add(uniProtID);
//					} else {
//						Set<UniProtID> uniprotIDs = new HashSet<UniProtID>();
//						uniprotIDs.add(uniProtID);
//						embl2UniProtIDsMap.put(emblID, uniprotIDs);
//					}
//				}
//			}
//		}
//		parser.close();
//		return embl2UniProtIDsMap;
//	}

}
