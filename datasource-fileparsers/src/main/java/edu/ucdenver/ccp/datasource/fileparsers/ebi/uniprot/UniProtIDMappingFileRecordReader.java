package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

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
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;

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
