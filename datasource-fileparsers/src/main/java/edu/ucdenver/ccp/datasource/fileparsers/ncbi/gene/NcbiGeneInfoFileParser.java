package edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * This class is used to parse the EntrezGene gene_info file.
 * 
 * @author Bill Baumgartner
 * 
 */
public class NcbiGeneInfoFileParser extends TaxonAwareSingleLineFileRecordReader<NcbiGeneInfoFileData> {

	private static final String HEADER = "#tax_id\tGeneID\tSymbol\tLocusTag\tSynonyms\tdbXrefs\tchromosome\tmap_location\tdescription\ttype_of_gene\tSymbol_from_nomenclature_authority\tFull_name_from_nomenclature_authority\tNomenclature_status\tOther_designations\tModification_date\tFeature_type";

	private final static Logger logger = Logger.getLogger(NcbiGeneInfoFileParser.class);
	private static final String COMMENT_INDICATOR = null;// StringConstants.POUND_SIGN;

	public static final String FTP_FILE_NAME = "gene_info.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	public static final String FTP_HOST = "ftp.ncbi.nih.gov";
	public static final String FTP_PATH = "gene/DATA";

	@FtpDownload(server = FTP_HOST, path = FTP_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File gene2infoFile;

	public NcbiGeneInfoFileParser(File entrezGeneInfoFile, CharacterEncoding encoding) throws IOException {
		super(entrezGeneInfoFile, encoding, COMMENT_INDICATOR, null);
	}

	public NcbiGeneInfoFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, COMMENT_INDICATOR, null, null, clean, null);
	}

	public NcbiGeneInfoFileParser(File entrezGeneInfoFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(entrezGeneInfoFile, encoding, COMMENT_INDICATOR, taxonsOfInterest);
	}

	public NcbiGeneInfoFileParser(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest)
			throws IOException {
		super(workDirectory, ENCODING, COMMENT_INDICATOR, null, null, clean, taxonsOfInterest);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(gene2infoFile)), encoding, skipLinePrefix);
	}

	@Override
	protected NcbiGeneInfoFileData parseRecordFromLine(Line line) {
		return parseGeneInfoLine(line);
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
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		NcbiGeneInfoFileData record = parseGeneInfoLine(line);
		return record.getTaxonID();
	}

	/*
	 * #Format: tax_id GeneID Symbol LocusTag Synonyms dbXrefs chromosome
	 * map_location description type_of_gene Symbol_from_nomenclature_authority
	 * Full_name_from_nomenclature_authority Nomenclature_status
	 * Other_designations Modification_date (tab is used as a separator, pound
	 * sign - start of a comment)
	 */
	/**
	 * Parse a line from the EntrezGene gene_info file
	 * 
	 * @param line
	 * @return
	 */
	public static NcbiGeneInfoFileData parseGeneInfoLine(Line line) {
		String lineText = line.getText();
		// if (!lineText.startsWith(COMMENT_INDICATOR)) {
		String[] toks = lineText.split("\\t");
		if (toks.length != 16) {
			logger.error("Unexpected number of tokens (" + toks.length + ") on line:"
					+ lineText.replaceAll("\\t", " [TAB] "));
		}

		NcbiTaxonomyID taxonID = new NcbiTaxonomyID(toks[0]);
		NcbiGeneId geneID = new NcbiGeneId(toks[1]);
		String symbol = null;
		if (!toks[2].equals("-")) {
			symbol = new String(toks[2]);
		}
		String locusTag = toks[3];
		if (locusTag.equals("-")) {
			locusTag = null;
		}
		Set<String> synonyms = new HashSet<String>();
		if (!toks[4].equals("-")) {
			for (String syn : toks[4].split("\\|"))
				synonyms.add(new String(syn));
		}
		Set<DataSourceIdentifier<?>> dbXrefs = new HashSet<DataSourceIdentifier<?>>();
		if (!toks[5].equals("-")) {
			for (String id : toks[5].split("\\|")) {
				DataSourceIdentifier<?> resolveGeneID = DataSourceIdResolver.resolveId(id);
				if (resolveGeneID != null) {
					dbXrefs.add(resolveGeneID);
				}
			}
		}
		String chromosome = null;
		if (!toks[6].equals("-") && !toks[6].equals("Unknown")) {
			chromosome = new String(toks[6]);
		}
		String mapLocation = toks[7];
		if (mapLocation.equals("-")) {
			mapLocation = null;
		}
		String description = toks[8];
		if (description.equals("-")) {
			description = null;
		}
		String typeOfGene = toks[9];
		if (typeOfGene.equals("-")) {
			typeOfGene = null;
		}
		String symbolFromNomenclatureAuthority = null;
		if (!toks[10].equals("-")) {
			symbolFromNomenclatureAuthority = new String(toks[10]);
		}
		String fullNameFromNomenclatureAuthority = null;
		if (!toks[11].equals("-")) {
			fullNameFromNomenclatureAuthority = new String(toks[11]);
		}
		String nomenclatureStatus = toks[12];
		if (nomenclatureStatus.equals("-")) {
			nomenclatureStatus = null;
		}
		Set<String> otherDesignations;
		if (toks[13].equals("-")) {
			otherDesignations = new HashSet<String>();
		} else {
			otherDesignations = new HashSet<String>();
			for (String other : toks[13].split("\\|")) {
				otherDesignations.add(new String(other));
			}
		}
		String modificationDate = toks[14];
		if (modificationDate.equals("-")) {
			modificationDate = null;
		}

		String featureTypeStr = toks[15];
		Set<String> featureTypes = new HashSet<String>();
		if (!featureTypeStr.equals("-")) {
			for (String ft : featureTypeStr.split("\\|")) {
				featureTypes.add(ft);
			}
		}
		return new NcbiGeneInfoFileData(taxonID, geneID, symbol, locusTag, synonyms, dbXrefs, chromosome, mapLocation,
				description, typeOfGene, symbolFromNomenclatureAuthority, fullNameFromNomenclatureAuthority,
				nomenclatureStatus, otherDesignations, modificationDate, featureTypes, line.getByteOffset(),
				line.getLineNumber());

		// }
		//
		// logger.warn("No relevant data to parse on line: " + line);
		// return null;
	}

	// /**
	// * Returns a map from the gene symbol (3rd column in gene_info file) to
	// the entrez gene id.
	// *
	// * @param entrezGeneInfoFile
	// * @return
	// * @throws IOException
	// */
	// public static Map<String, Set<Integer>>
	// getGeneSymbol2EntrezGeneIDMap(File
	// entrezGeneInfoFile, int taxonID,
	// boolean toLowerCase) throws IOException {
	// FileInputStream fis = null;
	// try {
	// fis = new FileInputStream(entrezGeneInfoFile);
	// return getGeneSymbol2EntrezGeneIDMap(fis, taxonID, toLowerCase);
	// } finally {
	// fis.close();
	// }
	// }

	/**
	 * Returns a map from the gene symbol (3rd column in gene_info file) to the
	 * entrez gene id.
	 * 
	 * @param entrezGeneInfoFile
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Set<NcbiGeneId>> getGeneSymbol2EntrezGeneIDMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID, boolean toLowerCase) throws IOException {
		Map<String, Set<NcbiGeneId>> geneSymbol2EntrezGeneIDMap = new HashMap<String, Set<NcbiGeneId>>();
		NcbiGeneInfoFileParser parser = new NcbiGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			NcbiGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				String geneSymbol = dataRecord.getSymbol();
				if (toLowerCase) {
					geneSymbol = new String(geneSymbol.toLowerCase());
				}
				NcbiGeneId entrezGeneID = dataRecord.getGeneID();
				if (geneSymbol2EntrezGeneIDMap.containsKey(geneSymbol)) {
					geneSymbol2EntrezGeneIDMap.get(geneSymbol).add(entrezGeneID);
				} else {
					Set<NcbiGeneId> entrezGeneIDs = new HashSet<NcbiGeneId>();
					entrezGeneIDs.add(entrezGeneID);
					geneSymbol2EntrezGeneIDMap.put(geneSymbol, entrezGeneIDs);
				}
			}
		}

		return geneSymbol2EntrezGeneIDMap;
	}

	/**
	 * Returns a map from the gene symbol including synonyms and nomenclature
	 * symbol to the entrez gene id.
	 * 
	 * @param entrezGeneInfoFileStream
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Set<NcbiGeneId>> getGeneSymbol2EntrezGeneIDMap_withSynonyms(File entrezGeneInfoFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID, boolean toLowerCase) throws IOException {
		Map<String, Set<NcbiGeneId>> geneSymbol2EntrezGeneIDMap = new HashMap<String, Set<NcbiGeneId>>();
		NcbiGeneInfoFileParser parser = new NcbiGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			NcbiGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				NcbiGeneId entrezGeneID = dataRecord.getGeneID();
				Set<String> geneSymbols = new HashSet<String>();
				geneSymbols.add(dataRecord.getSymbol());
				if (dataRecord.getSymbolFromNomenclatureAuthority() != null) {
					geneSymbols.add(dataRecord.getSymbolFromNomenclatureAuthority());
				}
				geneSymbols.addAll(dataRecord.getSynonyms());

				for (String geneSymbol : geneSymbols) {
					if (geneSymbol == null) {
						System.err.println("Null geneSymbol for ID: " + entrezGeneID);
					}
					if (toLowerCase) {
						geneSymbol = new String(geneSymbol.toLowerCase());
					}
					if (geneSymbol2EntrezGeneIDMap.containsKey(geneSymbol)) {
						geneSymbol2EntrezGeneIDMap.get(geneSymbol).add(entrezGeneID);
					} else {
						Set<NcbiGeneId> entrezGeneIDs = new HashSet<NcbiGeneId>();
						entrezGeneIDs.add(entrezGeneID);
						geneSymbol2EntrezGeneIDMap.put(geneSymbol, entrezGeneIDs);
					}
				}
			}
		}

		return geneSymbol2EntrezGeneIDMap;
	}

	/**
	 * Returns a map from the EntrezGene ID to the gene symbol
	 * 
	 * @param entrezGeneInfoFileStream
	 * @return
	 * @throws IOException
	 */
	public static Map<NcbiGeneId, String> getEntrezGeneID2GeneSymbolMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID) throws IOException {
		Map<NcbiGeneId, String> entrezGeneID2GeneSymbolMap = new HashMap<NcbiGeneId, String>();
		NcbiGeneInfoFileParser parser = new NcbiGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			NcbiGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				String geneSymbol = dataRecord.getSymbol();
				NcbiGeneId entrezGeneID = dataRecord.getGeneID();
				if (entrezGeneID2GeneSymbolMap.containsKey(entrezGeneID)) {
					logger.error("Symbol for Entrez Gene ID has already been extracted!!!");
				} else {
					entrezGeneID2GeneSymbolMap.put(entrezGeneID, geneSymbol);
				}
			}
		}

		return entrezGeneID2GeneSymbolMap;
	}

	/**
	 * Returns a map from the EntrezGene ID to the gene symbol where the Entrez
	 * Gene ID is represented as a String
	 * 
	 * @param entrezGeneInfoFile
	 * @return
	 * @throws IOException
	 */
	public static Map<NcbiGeneId, String> getEntrezGeneID2GeneNameMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID) throws IOException {
		Map<NcbiGeneId, String> entrezGeneID2GeneSymbolMap = new HashMap<NcbiGeneId, String>();
		NcbiGeneInfoFileParser parser = new NcbiGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			NcbiGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				String geneName = dataRecord.getFullNameFromNomenclatureAuthority();

				NcbiGeneId entrezGeneID = dataRecord.getGeneID();
				if (entrezGeneID2GeneSymbolMap.containsKey(entrezGeneID)) {
					logger.error("Name for Entrez Gene ID has already been extracted!!!");
				} else {
					if (geneName != null) {
						entrezGeneID2GeneSymbolMap.put(entrezGeneID, geneName);
					}
				}
			}
		}

		return entrezGeneID2GeneSymbolMap;
	}

	public static Set<NcbiGeneId> getEntrezGeneIDsForTaxonomyID(File entrezGeneInfoFile, CharacterEncoding encoding,
			NcbiTaxonomyID taxonID) throws IOException {
		return getEntrezGeneID2GeneSymbolMap(entrezGeneInfoFile, encoding, taxonID).keySet();
	}

	/**
	 * Returns a mapping from EntrezGene ID to Taxonomy ID for the set of input
	 * EntrezGene IDs
	 * 
	 * @param entrezGeneInfoFileStream
	 * @param entrezGeneIDs
	 * @return
	 * @throws IOException
	 */
	public static Map<NcbiGeneId, NcbiTaxonomyID> getEntrezGeneID2TaxonomyIDMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, final Set<NcbiGeneId> entrezGeneIDs) throws IOException {
		Map<NcbiGeneId, NcbiTaxonomyID> entrezGeneID2TaxonomyIDMap = new HashMap<NcbiGeneId, NcbiTaxonomyID>();
		Set<NcbiGeneId> entrezGeneIDsToInclude = new HashSet<NcbiGeneId>(entrezGeneIDs);
		for (NcbiGeneInfoFileParser parser = new NcbiGeneInfoFileParser(entrezGeneInfoFile,
				encoding); !entrezGeneIDsToInclude.isEmpty() && parser.hasNext();) {
			NcbiGeneInfoFileData dataRecord = parser.next();
			if (entrezGeneIDsToInclude.contains(dataRecord.getGeneID())) {
				entrezGeneID2TaxonomyIDMap.put(dataRecord.getGeneID(), dataRecord.getTaxonID());
				entrezGeneIDsToInclude.remove(dataRecord.getGeneID());
			}
		}

		if (!entrezGeneIDsToInclude.isEmpty()) {
			throw new RuntimeException(
					String.format("Unable to map all gene IDs to a taxonomy ID. Missing mappings for gene IDs: %s",
							entrezGeneIDsToInclude.toString()));
		}
		return entrezGeneID2TaxonomyIDMap;
	}

}
