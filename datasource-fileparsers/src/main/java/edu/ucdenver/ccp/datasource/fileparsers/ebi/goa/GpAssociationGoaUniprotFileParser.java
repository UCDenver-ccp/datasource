package edu.ucdenver.ccp.datasource.fileparsers.ebi.goa;

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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileReaderUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf.GoaGaf2FileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.idlist.IdListFileFactory;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IntActID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IpiID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ReactomeReactionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RnaCentralId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.DOI;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.GoRefID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.GoaRefID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * NOTE: This class has been deprecated as the file format that it parses has been
 * discontinued by UniProt and is no longer made available on their FTP site.
 * Please use {@link GoaGaf2FileRecordReader} as a replacement.
 * 
 * @author Bill Baumgartner
 * 
 */
@Deprecated
public class GpAssociationGoaUniprotFileParser extends
		TaxonAwareSingleLineFileRecordReader<GpAssociationGoaUniprotFileData> {

	private static final Logger logger = Logger.getLogger(GpAssociationGoaUniprotFileParser.class);

	private final Set<DataSourceIdentifier<?>> taxonSpecificIds;

	/* @formatter:off */
	private static final String HEADER =
			"!gpa-version: 1.1\n"+
			"!\n"+
			"!This file contains all GO annotations for proteins in the UniProt KnowledgeBase (UniProtKB).\n"+
			"!If a particular protein accession is not annotated with GO, then it will not appear in this file.\n"+
			"!\n"+
			"!Columns:\n"+
			"!\n"+
			"!   name                  required? cardinality   GAF column #\n"+
			"!   DB                    required  1             1\n"+
			"!   DB_Object_ID          required  1             2 / 17\n"+
			"!   Qualifier             required  1 or greater  4\n"+
			"!   GO ID                 required  1             5\n"+
			"!   DB:Reference(s)       required  1 or greater  6\n"+
			"!   ECO evidence code     required  1             7 (GO evidence code)\n"+
			"!   With                  optional  0 or greater  8\n"+
			"!   Interacting taxon ID  optional  0 or 1        13\n"+
			"!   Date                  required  1             14\n"+
			"!   Assigned_by           required  1             15\n"+
			"!   Annotation Extension  optional  0 or greater  16\n"+
			"!   Annotation Properties optional  0 or 1        n/a\n"+
			"!\n";

	/* @formatter:on */

	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	public static final String DELIMITER_REGEX = RegExPatterns.TAB;
	private static final int FILE_COLUMN_COUNT = 10;
	public static final String COMMENT_INDICATOR = null;// StringConstants.EXCLAMATION_MARK;

	// public GpAssociationGoaUniprotFileParser(File inputFile,
	// CharacterEncoding encoding) throws
	// IOException {
	// super(inputFile, encoding, COMMENT_INDICATOR, null);
	// taxonSpecificIds = null;
	// }
	//
	// public GpAssociationGoaUniprotFileParser(File workDirectory, boolean
	// clean) throws
	// IOException {
	// super(workDirectory, ENCODING, COMMENT_INDICATOR, null, null, clean,
	// null);
	// taxonSpecificIds = null;
	// }

	public GpAssociationGoaUniprotFileParser(File inputFile, CharacterEncoding encoding, File idListDirectory,
			Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		super(inputFile, encoding, COMMENT_INDICATOR, taxonIds);
		taxonSpecificIds = loadTaxonSpecificIds(idListDirectory, taxonIds, baseSourceFileDirectory, cleanIdListFiles);
		if (!isLineOfInterest(line)) {
			advanceToNextLineWithTaxonOfInterest();
		}
	}

	private Set<DataSourceIdentifier<?>> loadTaxonSpecificIds(File idListDirectory, Set<NcbiTaxonomyID> taxonIds,
			File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		Set<UniProtID> uniprotIdsForTaxon = IdListFileFactory.getIdListFromFile(idListDirectory,
				baseSourceFileDirectory, DataSource.UNIPROT, taxonIds, UniProtID.class, cleanIdListFiles);
		Set<IntActID> intactIdsForTaxon = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.INTACT, taxonIds, IntActID.class, cleanIdListFiles);
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		if (uniprotIdsForTaxon != null) {
			ids.addAll(uniprotIdsForTaxon);
		}
		if (intactIdsForTaxon != null) {
			ids.addAll(intactIdsForTaxon);
		}
		if (ids.isEmpty()) {
			return null;
		}
		return ids;
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected String getFileHeader() throws IOException {
		StringBuffer header = new StringBuffer();
		Line line = null;

		while ((line = readLine()).getText().startsWith(StringConstants.EXCLAMATION_MARK)) {
			System.out.println("header line: " + line.getText());
			header.append(line.getText() + "\n");
		}

		// make sure we don't skip the first real data line
		setNextLine(line);

		// chop off time stamp
		int timestampIndex = header.lastIndexOf("!Generated:");
		return header.substring(0, timestampIndex);
	}

	/**
	 * overriding b/c super.initialize() calls readLine() which increments the
	 * reader. We don't want to increment the reader b/c we have already found
	 * the first real data line while validating the header. See call to
	 * {@link #setNextLine(Line)} in {@link #getFileHeader()} above.
	 */
	@Override
	protected void initialize() throws IOException {
		String fileHeader = getFileHeader();
		validateFileHeader(fileHeader);
	}

	@Override
	protected GpAssociationGoaUniprotFileData parseRecordFromLine(Line line) {
		return parseGpAssociationGoaUniprotFileDataFromLine(line);
	}

	public static GpAssociationGoaUniprotFileData parseGpAssociationGoaUniprotFileDataFromLine(Line line) {
		String[] columns = FileReaderUtil.getColumnsFromLine(line.getText(), DELIMITER_REGEX);
		if (columns.length < FILE_COLUMN_COUNT) {
			String message = String.format(
					"Unable to initialize a new GpAssociationGoaUniprotFileData object. Expected %d columns in the input "
							+ "String[] but there were %d columns. Columns= %s LINE=%s", FILE_COLUMN_COUNT,
					columns.length, Arrays.toString(columns), line.getText());
			Logger.getLogger(GpAssociationGoaUniprotFileParser.class).warn(message);
			return null;
		}
		return initializeNewGpAssociationGoaUniprotFileData(columns, line.getByteOffset(), line.getLineNumber());
	}

	private static GpAssociationGoaUniprotFileData initializeNewGpAssociationGoaUniprotFileData(String[] columns,
			long byteOffset, long lineNumber) {
		String database = new String(columns[0]);
		String databaseObjectIDStr = columns[1];

		DataSourceIdentifier<?> databaseObjectID = createDatabaseObjectID(database, databaseObjectIDStr);

		if (databaseObjectID == null) {
			logger.warn("Skipping record (" + lineNumber + ") due to null database ID: " + Arrays.toString(columns));
			return null;
		}

		String qualifier = null;
		if (!columns[2].isEmpty()) {
			qualifier = columns[2];
		}
		GeneOntologyID goID = new GeneOntologyID(columns[3]);
		DataSourceIdentifier<?> dbReference = createDbReferenceIdentifier(columns[4]);

		if (dbReference == null) {
			Logger.getLogger(GpAssociationGoaUniprotFileParser.class).error(
					"Invalid Db reference value " + columns[4] + ". Skipping record " + Arrays.toString(columns));
			return null;
		}

		String evidenceCode = columns[5];
		String with = null;
		if (!columns[6].isEmpty()) {
			with = columns[6];
		}
		String taxonomyIDStr = columns[7];
		NcbiTaxonomyID extraTaxonID = null;
		if (!taxonomyIDStr.isEmpty()) {
			extraTaxonID = new NcbiTaxonomyID(taxonomyIDStr);
		}
		String date = columns[8];
		String assignedBy = columns[9];
		String annotationExtension = null;
		if (columns.length > 10 && !columns[10].isEmpty()) {
			annotationExtension = columns[10];
		}
		String annotationProperties = null;
		if (columns.length > 11 && !columns[11].isEmpty()) {
			annotationProperties = columns[11];
		}
		return new GpAssociationGoaUniprotFileData(database, databaseObjectID, qualifier, goID, dbReference,
				evidenceCode, with, extraTaxonID, date, assignedBy, annotationExtension, annotationProperties,
				byteOffset, lineNumber);
	}

	private static DataSourceIdentifier<?> createDbReferenceIdentifier(String dbReference) {
		String reactomePrefix = "Reactome:";
		if (dbReference.startsWith("PMID")) {
			PubMedID id = new PubMedID(dbReference);
			if (id.getId().intValue() <= 0) {
				return null;
			}
			return id;
		} else if (dbReference.startsWith("DOI")) {
			return new DOI(dbReference);
		} else if (dbReference.startsWith(reactomePrefix)) {
			return new ReactomeReactionID(dbReference.substring(reactomePrefix.length()));
		} else if (dbReference.startsWith("GO_REF")) {
			return new GoRefID(dbReference);
		} else if (dbReference.startsWith("GOA_REF")) {
			return new GoaRefID(dbReference);
		}
		logger.warn("Unhandled DB Reference ID type: " + dbReference);
		return null;
	}

	private static DataSourceIdentifier<?> createDatabaseObjectID(String database, String databaseObjectIDStr) {
		try {
			if (database.equals("IPI")) {
				return new IpiID(databaseObjectIDStr);
			}
			if (database.equals("UniProtKB")) {
				if (databaseObjectIDStr.contains("-")) {
					return new UniProtIsoformID(databaseObjectIDStr);
				} else if (databaseObjectIDStr.contains(":PRO_")) {
					// we are losing some information here (the protein chain
					// identifier)
					// TODO: this should be addressed again in the future.
					return new UniProtID(databaseObjectIDStr.substring(0, databaseObjectIDStr.indexOf(":")));
				}
				return new UniProtID(databaseObjectIDStr);
			}
			if (database.equals("IntAct")) {
				return new IntActID(databaseObjectIDStr);
			}
			if (database.equals("RNAcentral")) {
				return new RnaCentralId(databaseObjectIDStr);
			}
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
		}
		logger.warn("Unable to handle database/id pairing -- database: " + database.toString() + " id: "
				+ databaseObjectIDStr);
		return null;
		// throw new
		// IllegalArgumentException(String.format("Unable to handle database type: %s",
		// database));
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		if (line != null) {
			GpAssociationGoaUniprotFileData record = parseRecordFromLine(line);
			if (record != null) {
				DataSourceIdentifier<?> databaseObjectID = record.getDatabaseObjectID();
				if (databaseObjectID instanceof UniProtID) {
					UniProtID uniprotId = (UniProtID) databaseObjectID;
					if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty() && taxonSpecificIds.contains(uniprotId)) {
						/*
						 * here we have matched the record uniprot id as one of
						 * the ids of interest. We don't know exactly what taxon
						 * it is however so we just return one (arbitrarily) of
						 * the taxon ids of interest. this will ensure this
						 * record is returned.
						 */
						return taxonsOfInterest.iterator().next();
					}
				} else if (databaseObjectID instanceof UniProtIsoformID) {
					UniProtIsoformID isoformId = (UniProtIsoformID) databaseObjectID;
					String uniprotIdStr = StringUtil.removeSuffixRegex(isoformId.getId(), "-\\d+");
					if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty()
							&& taxonSpecificIds.contains(new UniProtID(uniprotIdStr))) {
						/*
						 * here we have matched the record uniprot id as one of
						 * the ids of interest. We don't know exactly what taxon
						 * it is however so we just return one (arbitrarily) of
						 * the taxon ids of interest. this will ensure this
						 * record is returned.
						 */
						return taxonsOfInterest.iterator().next();
					}
				} else if (databaseObjectID instanceof IntActID) {
					IntActID intactId = (IntActID) databaseObjectID;
					if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty() && taxonSpecificIds.contains(intactId)) {
						/*
						 * here we have matched the record intact id as one of
						 * the ids of interest. We don't know exactly what taxon
						 * it is however so we just return one (arbitrarily) of
						 * the taxon ids of interest. this will ensure this
						 * record is returned.
						 */
						return taxonsOfInterest.iterator().next();
					}
				} else {
					logger.warn("Unhandled non-UniProt id in GO data while trying to create a species specific subset: "
							+ databaseObjectID.getDataSource() + " -- " + databaseObjectID);
				}
			}
		}
		return new NcbiTaxonomyID(0);
	}
}
