package edu.ucdenver.ccp.datasource.fileparsers.mirbase;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblAssemblyInformation;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblDate;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblReferenceCitation;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.EmblSequenceDatabaseFileParserBase;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.SequenceFeature;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AgricolaId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AsrpId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InsdcProjectId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MiRBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MirteId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PictarId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RfamId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.WormBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.DOI;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

public class MirBaseMiRnaDatFileParser extends
		EmblSequenceDatabaseFileParserBase<MirBaseMiRnaDatFileData, MiRBaseID, MirBaseIdLineContents> {

	private static final Logger logger = Logger.getLogger(MirBaseMiRnaDatFileParser.class);

	// ftp://mirbase.org/pub/mirbase/CURRENT/
	public static final String FTP_FILE_NAME = "miRNA.dat.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;

	@FtpDownload(server = "mirbase.org", path = "pub/mirbase/CURRENT", filename = FTP_FILE_NAME, filetype = FileType.BINARY)
	private File miRnaDatFile;

	public MirBaseMiRnaDatFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding);
	}

	public MirBaseMiRnaDatFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(new GZIPInputStream(new FileInputStream(miRnaDatFile)), encoding, skipLinePrefix);
	}

	@Override
	protected MirBaseMiRnaDatFileData invokeConstructor(MirBaseIdLineContents idLineContents,
			List<MiRBaseID> accessionNumbers, InsdcProjectId projectId, Set<EmblDate> dates, String description,
			Set<String> keyWords,
			String organismSpeciesName,
			String organismClassification,
			String organelle,
			Set<EmblReferenceCitation> referenceCitations, // String proteinExistenceEvidence,
			Set<DataSourceIdentifier<?>> databaseCrossReferences, String comments,
			Set<? extends SequenceFeature> sequenceFeatures, int sequenceLength, int numAs, int numCs, int numGs,
			int numTs, int numOthers, String sequence, String constructedSeqInfo,
			Set<EmblAssemblyInformation> assemblyInfo, long byteOffset) {
		return new MirBaseMiRnaDatFileData(idLineContents.getPrimaryAccessionNumber(),
				idLineContents.getSequenceVersionNumber(), idLineContents.getSequenceTopology(),
				idLineContents.getMoleculeType(), idLineContents.getDataClass(), idLineContents.getTaxonomicDivision(),
				idLineContents.getSequenceLengthInBasePairs(), accessionNumbers, projectId, dates, description,
				keyWords, organismSpeciesName, organismClassification, organelle, referenceCitations,
				databaseCrossReferences, comments, sequenceFeatures, sequenceLength, numAs, numCs, numGs, numTs,
				numOthers, sequence, constructedSeqInfo, assemblyInfo, byteOffset);
	}

	@Override
	protected MirBaseIdLineContents parseIDLine(String line) {
		MiRBaseID primaryAccessionNumber;

		Pattern p = Pattern.compile("ID\\s+([^\\s]+)");
		Matcher m = p.matcher(line);
		if (m.find()) {
			primaryAccessionNumber = initPrimaryAccessionNumberType(m.group(1));
		} else {
			throw new IllegalArgumentException("Unable to extract primary identifier from ID line: " + line);
		}

		String restOfLine = line.substring(m.end(1)).trim();
		String[] toks = restOfLine.split(";");
		String sequenceVersionNumber = null;
		String sequenceTopology = null;
		String moleculeType = toks[1].trim();
		String dataClass = toks[0].trim();
		String taxonomicDivision = toks[2].trim();
		int sequenceLengthInBasePairs = Integer.parseInt(StringUtil.removeSuffix(toks[3].trim(), " BP.").trim());

		return new MirBaseIdLineContents(primaryAccessionNumber, moleculeType, dataClass, taxonomicDivision,
				sequenceLengthInBasePairs);
	}

	/**
	 * @param line
	 * @return
	 */
	protected DataSourceIdentifier<?> parseRXLine(String line) {
		Pattern pmidPattern = Pattern.compile("PUBMED; (\\d+)\\.");
		Pattern doiPattern = Pattern.compile("DOI; (.*)\\.$");
		Pattern agricolaPattern = Pattern.compile("AGRICOLA; (.*)\\.$");

		Matcher m = pmidPattern.matcher(line);
		if (m.find()) {
			return new PubMedID(m.group(1));
		}
		m = doiPattern.matcher(line);
		if (m.find()) {
			return new DOI(m.group(1));
		}
		m = agricolaPattern.matcher(line);
		if (m.find()) {
			return new AgricolaId(m.group(1));
		}
		if (line.contains("MEDLINE")) {
			/*
			 * The mirBase miRNA.dat file contains one publication that has a MEDLINE ID and a
			 * PUBMED ID. The PUBMED ID is correct, not sure what the MEDLINE ID pertains to, so we
			 * ignore it here.
			 */
			logger.info("The mirBase miRNA.dat file contains one publication that has a MEDLINE ID "
					+ "and a PUBMED ID. The PUBMED ID is correct, not sure what the MEDLINE ID pertains "
					+ "to, so we ignore it here. Ignoring a MEDLINE ID on an RX line: " + line);
			return null;
		}
		throw new IllegalArgumentException("RX line failed to match: " + line);
	}

	@Override
	protected DataSourceIdentifier<?> parseDRLine(String line) {
		String[] toks = line.substring(2).split(";");
		String dbKey = toks[0].trim();
		String dbId = toks[1].trim();
		if (dbKey.equalsIgnoreCase("MGI")) {
			return new MgiGeneID("MGI:" + dbId);
		}
		if (dbKey.equalsIgnoreCase("RFAM")) {
			return new RfamId(dbId);
		}
		if (dbKey.equalsIgnoreCase("WORMBASE")) {
			if (dbId.indexOf("/") != -1) {
				return new WormBaseID(dbId.substring(0, dbId.indexOf("/")));
			} else {
				logger.warn("Unable to handle supposed WORMBASE ID: " + dbId);
				return null;
			}
		}
		if (dbKey.equalsIgnoreCase("HGNC")) {
			return new HgncID(dbId);
		}
		if (dbKey.equalsIgnoreCase("ENTREZGENE")) {
			return new NcbiGeneId(dbId);
		}
		if (dbKey.startsWith("TARGETS:PICTAR")) {
			return new PictarId(dbId);
		}
		if (dbKey.equalsIgnoreCase("MIR")) {
			return new MiRBaseID(dbId);
		}
		if (dbKey.equalsIgnoreCase("TARGETS:MIRTE") || dbKey.equalsIgnoreCase("MIRTE")) {
			return new MirteId(dbId);
		}
		if (dbKey.equalsIgnoreCase("ASRP")) {
			return new AsrpId(dbId);
		}

		throw new IllegalArgumentException("Unhandled database cross reference: " + line);
	}

	@Override
	protected MiRBaseID initPrimaryAccessionNumberType(String id) {
		return new MiRBaseID(id);
	}

	@Override
	protected Collection<? extends SequenceFeature> parseFeatureTable(String line, BufferedReader br)
			throws IOException {
		Collection<SequenceFeature> features = new HashSet<SequenceFeature>();
		List<String> sequenceFeatureLines = new ArrayList<String>();
		sequenceFeatureLines.add(line);
		String featureKey = getFeatureKey(line);
		while ((line = br.readLine()).startsWith("FT")) {
			if (!lineHasKey(line)) {
				sequenceFeatureLines.add(line);
			} else {
				addSequenceFeature(features, sequenceFeatureLines, featureKey);
				sequenceFeatureLines = new ArrayList<String>();
				sequenceFeatureLines.add(line);
				featureKey = getFeatureKey(line);
			}
		}
		addSequenceFeature(features, sequenceFeatureLines, featureKey);
		if (!line.startsWith("XX")) {
			throw new IllegalStateException("Observed record missing XX between FT's and SQ line");
		}
		return features;
	}

	/**
	 * @param line
	 * @return
	 */
	private String getFeatureKey(String line) {
		line = line.substring(2).trim();
		return line.substring(0, line.indexOf(" "));
	}

	/**
	 * @param features
	 * @param sequenceFeatureLines
	 * @param featureKey
	 */
	private void addSequenceFeature(Collection<SequenceFeature> features, List<String> sequenceFeatureLines,
			String featureKey) {
		if (featureKey.equals("miRNA")) {
			features.add(MirBaseMiRnaSequenceFeature.extractFeature(sequenceFeatureLines));
		} else if (featureKey.equals("modified_base")) {
			features.add(MirBaseModifiedBaseSequenceFeature.extractFeature(sequenceFeatureLines));
		} else {
			throw new IllegalArgumentException("Unhandled feature key: " + featureKey);
		}
	}

	/**
	 * @param line
	 * @return
	 */
	private boolean lineHasKey(String line) {
		return line.startsWith("FT") && line.charAt(5) != ' ';
	}

	@Override
	protected void parseGNLine(String line) {
		throw new IllegalStateException(
				"GN line should not exist in the EMBL/MirBase file format. It is only used by UniProt.");
	}

	@Override
	protected void parseOXLine(String line) {
		throw new IllegalStateException(
				"OX line should not exist in the EMBL/MirBase file format. It is only used by UniProt.");
	}

}
