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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdResolver;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.fileparsers.field.ChromosomeNumber;

/**
 * This class is used to parse the EntrezGene gene_info file.
 * 
 * @author Bill Baumgartner
 * 
 */
public class EntrezGeneInfoFileParser extends TaxonAwareSingleLineFileRecordReader<EntrezGeneInfoFileData> {

	private static final String HEADER = "#Format: tax_id GeneID Symbol LocusTag Synonyms dbXrefs chromosome map_location description type_of_gene Symbol_from_nomenclature_authority Full_name_from_nomenclature_authority Nomenclature_status Other_designations Modification_date (tab is used as a separator, pound sign - start of a comment)";

	private final static Logger logger = Logger.getLogger(EntrezGeneInfoFileParser.class);
	private static final String COMMENT_INDICATOR = null;// StringConstants.POUND_SIGN;

	public static final String FTP_FILE_NAME = "gene_info.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File gene2infoFile;

	public EntrezGeneInfoFileParser(File entrezGeneInfoFile, CharacterEncoding encoding) throws IOException {
		super(entrezGeneInfoFile, encoding, COMMENT_INDICATOR, null);
	}

	public EntrezGeneInfoFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, COMMENT_INDICATOR, null, null, clean, null);
	}

	public EntrezGeneInfoFileParser(File entrezGeneInfoFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(entrezGeneInfoFile, encoding, COMMENT_INDICATOR, taxonsOfInterest);
	}

	public EntrezGeneInfoFileParser(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest)
			throws IOException {
		super(workDirectory, ENCODING, COMMENT_INDICATOR, null, null, clean, taxonsOfInterest);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(gene2infoFile)), encoding, skipLinePrefix);
	}

	@Override
	protected EntrezGeneInfoFileData parseRecordFromLine(Line line) {
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
		EntrezGeneInfoFileData record = parseGeneInfoLine(line);
		return record.getTaxonID();
	}

	/*
	 * #Format: tax_id GeneID Symbol LocusTag Synonyms dbXrefs chromosome map_location description
	 * type_of_gene Symbol_from_nomenclature_authority Full_name_from_nomenclature_authority
	 * Nomenclature_status Other_designations Modification_date (tab is used as a separator, pound
	 * sign - start of a comment)
	 */
	/**
	 * Parse a line from the EntrezGene gene_info file
	 * 
	 * @param line
	 * @return
	 */
	public static EntrezGeneInfoFileData parseGeneInfoLine(Line line) {
		String lineText = line.getText();
		// if (!lineText.startsWith(COMMENT_INDICATOR)) {
		String[] toks = lineText.split("\\t");
		if (toks.length != 15) {
			logger.error("Unexpected number of tokens (" + toks.length + ") on line:"
					+ lineText.replaceAll("\\t", " [TAB] "));
		}

		NcbiTaxonomyID taxonID = new NcbiTaxonomyID(toks[0]);
		EntrezGeneID geneID = new EntrezGeneID(toks[1]);
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
				DataSourceIdentifier<?> resolveGeneID = null;
				try {
					resolveGeneID = DataSourceIdResolver.resolveId(id);
				} catch (IllegalArgumentException e) {
					logger.warn("Exception during ID resolution for id: " + id);
					resolveGeneID = null;
				}
				if (resolveGeneID != null) {
					dbXrefs.add(resolveGeneID);
				}
			}
		}
		ChromosomeNumber chromosome = null;
		if (!toks[6].equals("-") && !toks[6].equals("Unknown")) {
			chromosome = new ChromosomeNumber(toks[6]);
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
			for (String other : toks[13].split("\\|"))
				otherDesignations.add(new String(other));
		}
		String modificationDate = toks[14];
		if (modificationDate.equals("-")) {
			modificationDate = null;
		}
		return new EntrezGeneInfoFileData(taxonID, geneID, symbol, locusTag, synonyms, dbXrefs, chromosome,
				mapLocation, description, typeOfGene, symbolFromNomenclatureAuthority,
				fullNameFromNomenclatureAuthority, nomenclatureStatus, otherDesignations, modificationDate,
				line.getByteOffset(), line.getLineNumber());

		// }
		//
		// logger.warn("No relevant data to parse on line: " + line);
		// return null;
	}

	// /**
	// * Returns a map from the gene symbol (3rd column in gene_info file) to the entrez gene id.
	// *
	// * @param entrezGeneInfoFile
	// * @return
	// * @throws IOException
	// */
	// public static Map<String, Set<Integer>> getGeneSymbol2EntrezGeneIDMap(File
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
	 * Returns a map from the gene symbol (3rd column in gene_info file) to the entrez gene id.
	 * 
	 * @param entrezGeneInfoFile
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Set<EntrezGeneID>> getGeneSymbol2EntrezGeneIDMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID, boolean toLowerCase) throws IOException {
		Map<String, Set<EntrezGeneID>> geneSymbol2EntrezGeneIDMap = new HashMap<String, Set<EntrezGeneID>>();
		EntrezGeneInfoFileParser parser = new EntrezGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			EntrezGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				String geneSymbol = dataRecord.getSymbol();
				if (toLowerCase) {
					geneSymbol = new String(geneSymbol.toLowerCase());
				}
				EntrezGeneID entrezGeneID = dataRecord.getGeneID();
				if (geneSymbol2EntrezGeneIDMap.containsKey(geneSymbol)) {
					geneSymbol2EntrezGeneIDMap.get(geneSymbol).add(entrezGeneID);
				} else {
					Set<EntrezGeneID> entrezGeneIDs = new HashSet<EntrezGeneID>();
					entrezGeneIDs.add(entrezGeneID);
					geneSymbol2EntrezGeneIDMap.put(geneSymbol, entrezGeneIDs);
				}
			}
		}

		return geneSymbol2EntrezGeneIDMap;
	}

	/**
	 * Returns a map from the gene symbol including synonyms and nomenclature symbol to the entrez
	 * gene id.
	 * 
	 * @param entrezGeneInfoFileStream
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Set<EntrezGeneID>> getGeneSymbol2EntrezGeneIDMap_withSynonyms(
			File entrezGeneInfoFile, CharacterEncoding encoding, NcbiTaxonomyID taxonID, boolean toLowerCase)
			throws IOException {
		Map<String, Set<EntrezGeneID>> geneSymbol2EntrezGeneIDMap = new HashMap<String, Set<EntrezGeneID>>();
		EntrezGeneInfoFileParser parser = new EntrezGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			EntrezGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				EntrezGeneID entrezGeneID = dataRecord.getGeneID();
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
						Set<EntrezGeneID> entrezGeneIDs = new HashSet<EntrezGeneID>();
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
	public static Map<EntrezGeneID, String> getEntrezGeneID2GeneSymbolMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID) throws IOException {
		Map<EntrezGeneID, String> entrezGeneID2GeneSymbolMap = new HashMap<EntrezGeneID, String>();
		EntrezGeneInfoFileParser parser = new EntrezGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			EntrezGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				String geneSymbol = dataRecord.getSymbol();
				EntrezGeneID entrezGeneID = dataRecord.getGeneID();
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
	 * Returns a map from the EntrezGene ID to the gene symbol where the Entrez Gene ID is
	 * represented as a String
	 * 
	 * @param entrezGeneInfoFile
	 * @return
	 * @throws IOException
	 */
	public static Map<EntrezGeneID, String> getEntrezGeneID2GeneNameMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, NcbiTaxonomyID taxonID) throws IOException {
		Map<EntrezGeneID, String> entrezGeneID2GeneSymbolMap = new HashMap<EntrezGeneID, String>();
		EntrezGeneInfoFileParser parser = new EntrezGeneInfoFileParser(entrezGeneInfoFile, encoding);

		while (parser.hasNext()) {
			EntrezGeneInfoFileData dataRecord = parser.next();
			if (dataRecord.getTaxonID().equals(taxonID)) {
				String geneName = dataRecord.getFullNameFromNomenclatureAuthority();

				EntrezGeneID entrezGeneID = dataRecord.getGeneID();
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

	public static Set<EntrezGeneID> getEntrezGeneIDsForTaxonomyID(File entrezGeneInfoFile, CharacterEncoding encoding,
			NcbiTaxonomyID taxonID) throws IOException {
		return getEntrezGeneID2GeneSymbolMap(entrezGeneInfoFile, encoding, taxonID).keySet();
	}

	/**
	 * Returns a mapping from EntrezGene ID to Taxonomy ID for the set of input EntrezGene IDs
	 * 
	 * @param entrezGeneInfoFileStream
	 * @param entrezGeneIDs
	 * @return
	 * @throws IOException
	 */
	public static Map<EntrezGeneID, NcbiTaxonomyID> getEntrezGeneID2TaxonomyIDMap(File entrezGeneInfoFile,
			CharacterEncoding encoding, final Set<EntrezGeneID> entrezGeneIDs) throws IOException {
		Map<EntrezGeneID, NcbiTaxonomyID> entrezGeneID2TaxonomyIDMap = new HashMap<EntrezGeneID, NcbiTaxonomyID>();
		Set<EntrezGeneID> entrezGeneIDsToInclude = new HashSet<EntrezGeneID>(entrezGeneIDs);
		for (EntrezGeneInfoFileParser parser = new EntrezGeneInfoFileParser(entrezGeneInfoFile, encoding); !entrezGeneIDsToInclude
				.isEmpty() && parser.hasNext();) {
			EntrezGeneInfoFileData dataRecord = parser.next();
			if (entrezGeneIDsToInclude.contains(dataRecord.getGeneID())) {
				entrezGeneID2TaxonomyIDMap.put(dataRecord.getGeneID(), dataRecord.getTaxonID());
				entrezGeneIDsToInclude.remove(dataRecord.getGeneID());
			}
		}

		if (!entrezGeneIDsToInclude.isEmpty()) {
			throw new RuntimeException(String.format(
					"Unable to map all gene IDs to a taxonomy ID. Missing mappings for gene IDs: %s",
					entrezGeneIDsToInclude.toString()));
		}
		return entrezGeneID2TaxonomyIDMap;
	}

}
