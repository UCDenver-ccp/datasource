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
package edu.ucdenver.ccp.datasource.fileparsers.ebi.embl;

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
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AgricolaId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InsdcProjectId;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.DOI;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Data;

public abstract class EmblSequenceDatabaseFileParserBase<T extends EmblSequenceDatabaseFileDataBase, D extends DataSourceIdentifier<?>, E extends IdLineContents<?>>
		extends MultiLineFileRecordReader<T> {

	private static final Logger logger = Logger.getLogger(EmblSequenceDatabaseFileParserBase.class);

	/**
	 * // - termination line (ends each entry; 1 per entry)
	 */
	public static final String RECORD_SEPARATOR = "//";

	public enum LinePrefix {
		/**
		 * ID - identification (begins each entry; 1 per entry)
		 */
		ID,
		/**
		 * XX - spacer line (many per entry); only used in EMBL format (not used in UniProt)
		 */
		XX,
		/**
		 * AC - accession number (>=1 per entry)
		 */
		AC,
		/**
		 * PR - project identifier (0 or 1 per entry)
		 */
		PR,
		/**
		 * DT - date (2 per entry for EMBL, 3 per entry for UniProt)
		 */
		DT,
		/**
		 * DE - description (>=1 per entry)
		 */
		DE,
		/**
		 * GN - gene name (optional); only used in UniProt format (not used by EMBL) 
		 */
		GN,
		/**
		 * KW - keyword (>=1 per entry for EMBL; optional for UniProt)
		 */
		KW,
		/**
		 * OS - organism species (>=1 per entry)
		 */
		OS,
		/**
		 * OC - organism classification (>=1 per entry)
		 */
		OC,
		/**
		 * OG - organelle (0 or 1 per entry)
		 */
		OG,
		/**
		 * OX - organism taxonomy cross-reference; only used in UniProt format (not used by EMBL)
		 */
		OX,
		/**
		 * RN - reference number (>=1 per entry)
		 */
		RN,
		/**
		 * RC - reference comment (>=0 per entry)
		 */
		RC,
		/**
		 * RP - reference positions (>=1 per entry)
		 */
		RP,
		/**
		 * RX - reference cross-reference (>=0 per entry)
		 */
		RX,
		/**
		 * RG - reference group (>=0 per entry)
		 */
		RG,
		/**
		 * RA - reference author(s) (>=0 per entry)
		 */
		RA,
		/**
		 * RT - reference title (>=1 per entry)
		 */
		RT,
		/**
		 * RL - reference location (>=1 per entry)
		 */
		RL,
		/**
		 * DR - database cross-reference (>=0 per entry)
		 */
		DR,
		/**
		 * CC - comments or notes (>=0 per entry)
		 */
		CC,
		/**
		 * AH - assembly header (0 or 1 per entry)
		 */
		AH,
		/**
		 * AS - assembly information (0 or >=1 per entry)
		 */
		AS,
		/**
		 * FH - feature table header (2 per entry); only used in EMBL format (not used in UniProt)
		 */
		FH,
		/**
		 * FT - feature table data (>=2 per entry)
		 */
		FT,
		/**
		 * SQ - sequence header (1 per entry)
		 */
		SQ,
		/**
		 * blanks - sequence data (>=1 per entry)
		 */
		BLANK,
		/**
		 * CO - contig/construct line (0 or >=1 per entry)
		 */
		CO
	}

	public EmblSequenceDatabaseFileParserBase(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	/**
	 * This constructor is to only be used by subclasses of UniProtDatFileParser that allow for
	 * automatic download of the input data file.
	 * 
	 * @param workDirectory
	 * @param encoding
	 * @param clean
	 * @throws IOException
	 */
	protected EmblSequenceDatabaseFileParserBase(File workDirectory, CharacterEncoding encoding, boolean clean)
			throws IOException {
		super(workDirectory, encoding, null, null, null, clean);
	}

	@Override
	protected void initialize() throws IOException {
		line = readLine();
		super.initialize();
	}

	@Override
	protected MultiLineBuffer compileMultiLineBuffer() throws IOException {
		if (line == null) {
			return null;
		}
		MultiLineBuffer multiLineBuffer = new MultiLineBuffer();
		do {
			multiLineBuffer.add(line);
			line = readLine();
		} while (line != null && !line.getText().startsWith(RECORD_SEPARATOR));
		line = readLine();
		return multiLineBuffer;
	}

	@Override
	protected T parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer) {
		try {
			return parseEmblNucelotideSequenceDatabaseRecord(multiLineBuffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private T parseEmblNucelotideSequenceDatabaseRecord(MultiLineBuffer multiLineBuffer) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(multiLineBuffer.toString()));
		String line;

		E idLineContents = null;
		List<D> accessionNumbers = null;
		InsdcProjectId projectId = null;
		Set<EmblDate> dates = new HashSet<EmblDate>();
		String description = null;
		Set<String> keyWords = new HashSet<String>();
		String organismSpeciesName = null;
		String organismClassification = null;
		String organelle = null;
		Set<EmblReferenceCitation> referenceCitations = new HashSet<EmblReferenceCitation>();
//		String proteinExistenceEvidence = null;
		Set<DataSourceIdentifier<?>> databaseCrossReferences = new HashSet<DataSourceIdentifier<?>>();
		String comments = null;
		SqLineContents sqLineContents = null;
		String sequence = null;
		String constructedSeqInfo = null;
		Set<EmblAssemblyInformation> assemblyInfo = new HashSet<EmblAssemblyInformation>();
		Set<SequenceFeature> sequenceFeatures = new HashSet<SequenceFeature>();
		while ((line = br.readLine()) != null) {
			String prefix = line.substring(0, 2);
			LinePrefix lp = (prefix.trim().isEmpty()) ? LinePrefix.BLANK : LinePrefix.valueOf(prefix);
			switch (lp) {
			case ID:
				idLineContents = parseIDLine(line);
				break;
			case XX:
				continue;
			case AC:
				accessionNumbers = parseACLine(line);
				break;
			case PR:
				if (projectId != null) {
					throw new IllegalStateException("expected single project Id, but observed multiple: "
							+ idLineContents.getPrimaryAccessionNumber());
				}
				projectId = parsePRLine(line);
				break;
			case DT:
				dates.add(parseDTLine(line));
				break;
			case DE:
				description = parseDELine(line, description);
				break;
			case KW:
				String[] kws = line.substring(2).trim().split("[;\\.]");
				for (String kw : kws) {
					keyWords.add(kw.trim());
				}
				break;
			case OS:
				organismSpeciesName = line.substring(2).trim();
				break;
			case OC:
				organismClassification = parseOCLine(line, organismClassification);
				break;
			case OG:
				organelle = line.substring(2).trim();
				break;
			case RN:
				referenceCitations.add(parseReferenceCitation(line, br));
				break;
			case RC:
				throw new IllegalStateException(
						"Reference-related lines handled by parseReferenceCitation.. should not be here.");
			case RP:
				throw new IllegalStateException(
						"Reference-related lines handled by parseReferenceCitation.. should not be here.");
			case RX:
				throw new IllegalStateException(
						"Reference-related lines handled by parseReferenceCitation.. should not be here.");
			case RG:
				throw new IllegalStateException(
						"Reference-related lines handled by parseReferenceCitation.. should not be here.");
			case RA:
				throw new IllegalStateException(
						"Reference-related lines handled by parseReferenceCitation.. should not be here.");
			case RT:
				throw new IllegalStateException(
						"Reference-related lines handled by parseReferenceCitation.. should not be here.");
			case RL:
				throw new IllegalStateException(
						"Reference-related lines handled by parseReferenceCitation.. should not be here.");
			case DR:
				databaseCrossReferences.add(parseDRLine(line));
				break;
			case CC:
				comments = parseCCLine(line, comments);
				break;
			case AH:
				continue; // header line, contains no data
			case AS:
				assemblyInfo.add(parseASLine(line));
				break;
			case FH:
				continue; // header line, contains no data
			case FT:
				sequenceFeatures.addAll(parseFeatureTable(line, br));
				break;
			case SQ:
				sqLineContents = parseSqLine(line);
				break;
			case BLANK:
				String seqLine = line.substring(2).trim();
				seqLine = StringUtil.removeSuffixRegex(seqLine, "\\d+").trim().replaceAll("\\s", "");
				if (sequence == null) {
					sequence = seqLine;
				} else {
					sequence += seqLine;
				}
				break;
			case CO:
				String trimmedCoLine = line.substring(2).trim();
				if (constructedSeqInfo == null) {
					constructedSeqInfo = trimmedCoLine;
				} else {
					constructedSeqInfo += trimmedCoLine;
				}
				break;
			case OX:
				parseOXLine(line);
				break;
			case GN:
				parseGNLine(line);
				break;
			default:
				throw new IllegalStateException("Unhandled line type detected: " + lp.name());
			}
		}

		if (idLineContents == null) {
			return null;
		}

		return invokeConstructor(idLineContents, accessionNumbers,
				projectId, dates, description, keyWords, organismSpeciesName, organismClassification, organelle,
				referenceCitations, databaseCrossReferences, comments, sequenceFeatures,
				sqLineContents.getSequenceLength(), sqLineContents.getNumAs(), sqLineContents.getNumCs(),
				sqLineContents.getNumGs(), sqLineContents.getNumTs(), sqLineContents.getNumOthers(), sequence,
				constructedSeqInfo, assemblyInfo, multiLineBuffer.getByteOffset());

	}

	/**
	 * @param line
	 * @param comments
	 * @return
	 */
	protected String parseCCLine(String line, String comments) {
		String trimmedCcLine = line.substring(2).trim();
		if (comments == null) {
			comments = trimmedCcLine;
		} else {
			comments += (" " + trimmedCcLine);
		}
		return comments;
	}
	
	protected String parseOCLine(String line, String comments) {
		String trimmedCcLine = line.substring(2).trim();
		if (comments == null) {
			comments = trimmedCcLine;
		} else {
			comments += (" " + trimmedCcLine);
		}
		return comments;
	}
	
	protected String parseDELine(String line, String comments) {
		String trimmedCcLine = line.substring(2).trim();
		if (comments == null) {
			comments = trimmedCcLine;
		} else {
			comments += (" " + trimmedCcLine);
		}
		return comments;
	}

	/**
	 * To be implemented by UniProt file parsers only (not used in EMBL format)
	 * @param line
	 */
	protected abstract void parseGNLine(String line);

	/**
	 * To be implemented by UniProt file parsers only (not used in EMBL format)
	 * @param line
	 */
	protected abstract void parseOXLine(String line);

	/**
	 * @param line
	 * @param br
	 * @return
	 * @throws IOException 
	 */
	protected abstract Collection<? extends SequenceFeature> parseFeatureTable(String line, BufferedReader br) throws IOException;

	/**
	 * @param idLineContents 
	 * @param accessionNumbers
	 * @param projectId
	 * @param dates
	 * @param description
	 * @param keyWords
	 * @param organismSpeciesName
	 * @param organismClassification
	 * @param organelle
	 * @param referenceCitations
	 * @param proteinExistenceEvidence
	 * @param databaseCrossReferences
	 * @param comments
	 * @param sequenceFeatures
	 * @param sequenceLength
	 * @param numAs
	 * @param numCs
	 * @param numGs
	 * @param numTs
	 * @param numOthers
	 * @param sequence
	 * @param constructedSeqInfo
	 * @param assemblyInfo
	 * @param byteOffset
	 * @return
	 */
	protected abstract T invokeConstructor(E idLineContents, List<D> accessionNumbers,
			InsdcProjectId projectId, Set<EmblDate> dates, String description, Set<String> keyWords,
			String organismSpeciesName, String organismClassification, String organelle,
			Set<EmblReferenceCitation> referenceCitations, //String proteinExistenceEvidence,
			Set<DataSourceIdentifier<?>> databaseCrossReferences, String comments,
			Set<? extends SequenceFeature> sequenceFeatures, int sequenceLength, int numAs, int numCs, int numGs, int numTs,
			int numOthers, String sequence, String constructedSeqInfo, Set<EmblAssemblyInformation> assemblyInfo,
			long byteOffset);

	/**
	 * e.g. AS 427-526 AC001234.2 1-100 c
	 * 
	 * @param line
	 * @return
	 */
	private EmblAssemblyInformation parseASLine(String line) {
		String[] toks = line.split("\\s+");
		String localSpan = toks[1];
		DataSourceIdentifier<?> primaryIdentifier = NucleotideAccessionResolver.resolveNucleotideAccession(toks[2], toks[2]);
		String primarySpan = toks[3];
		boolean originatesFromComplementary = (toks.length == 5 && toks[4].trim().equalsIgnoreCase("c")) ? true : false;
		return new EmblAssemblyInformation(localSpan, primaryIdentifier, primarySpan, originatesFromComplementary);
	}

	/**
	 * e.g. SQ Sequence 1859 BP; 609 A; 314 C; 355 G; 581 T; 0 other;
	 * 
	 * @param line
	 * @return
	 */
	private SqLineContents parseSqLine(String line) {
		Pattern sqLinePattern = Pattern
				.compile("SQ\\s+Sequence\\s(\\d+)\\s?BP;\\s?(\\d+)\\s?A;\\s?(\\d+)\\s?C;\\s?(\\d+)\\s?G;\\s?(\\d+)\\s?T;\\s?(\\d+)\\s?other;");
		Matcher m = sqLinePattern.matcher(line);
		if (m.find()) {
			int sequenceLength = Integer.parseInt(m.group(1));
			int numAs = Integer.parseInt(m.group(2));
			int numCs = Integer.parseInt(m.group(3));
			int numGs = Integer.parseInt(m.group(4));
			int numTs = Integer.parseInt(m.group(5));
			int numOthers = Integer.parseInt(m.group(6));
			return new SqLineContents(sequenceLength, numAs, numTs, numCs, numGs, numOthers);
		}
		throw new IllegalArgumentException("Unable to extract sequence summary from SQ header line: " + line);
	}

	/**
	 * To be implemented by the implementing class since data sources and their abbreviations may
	 * not be consistent
	 * 
	 * @param line
	 * @return
	 */
	protected abstract DataSourceIdentifier<?> parseDRLine(String line);

	/**
	 * @param line
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private EmblReferenceCitation parseReferenceCitation(String rnLine, BufferedReader br) throws IOException {
		int referenceNumber = Integer.parseInt(StringUtil.removeSuffix(
				StringUtil.removePrefix(rnLine.substring(2).trim(), "["), "]"));
		String referenceComment = null;
		Set<String> referencePositions = new HashSet<String>();
		Set<DataSourceIdentifier<?>> referenceCrossReferences = new HashSet<DataSourceIdentifier<?>>();
		Set<String> referenceGroups = new HashSet<String>();
		Set<String> referenceAuthors = new HashSet<String>();
		String referenceTitle = null;
		String referenceLocation = null;
		boolean endReference = false;
		while (!endReference) {
			String line = br.readLine();
			if (line == null) {
				throw new IllegalStateException("File ended unexpectedly mid-reference.");
			}
			String prefix = line.substring(0, 2);
			LinePrefix lp = (prefix.trim().isEmpty()) ? LinePrefix.BLANK : LinePrefix.valueOf(prefix);
			switch (lp) {
			case RN:
				throw new IllegalStateException("Did not expect RN line. Must not be a break between references.");
			case RC:
				referenceComment = parseCCLine(line, referenceComment);
				break;
			case RP:
				String[] positions = line.substring(2).trim().split(",");
				for (String p : positions) {
					referencePositions.add(p.trim());
				}
				break;
			case RX:
				referenceCrossReferences.add(parseRXLine(line));
				break;
			case RG:
				referenceGroups.add(line.substring(2).trim());
				break;
			case RA:
				String[] authors = line.substring(2).trim().split("[,;]");
				for (String author : authors) {
					referenceAuthors.add(author.trim());
				}
				break;
			case RT:
				if (line.substring(2).trim().equals(";")) {
					referenceTitle = null;
				} else {
					referenceTitle = parseCCLine(line, referenceTitle);
				}
				break;
			case RL:
				referenceLocation = parseCCLine(line, referenceLocation);
				break;
			case XX:
				endReference = true;
				break;
			default:
				throw new IllegalStateException("Did not expect " + prefix + " line in the parseReference() method");
			}
		}

		if (referenceTitle != null && referenceTitle.endsWith(";")) {
			referenceTitle = StringUtil.removeLastCharacter(referenceTitle);
		}

		return new EmblReferenceCitation(referenceNumber, referenceComment, referencePositions,
				referenceCrossReferences, referenceGroups, referenceAuthors, referenceTitle, referenceLocation);
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
		throw new IllegalArgumentException("RX line failed to match: " + line);
	}

	/**
	 * @param line
	 * @return
	 */
	private EmblDate parseDTLine(String line) {
		Pattern dateLinePattern = Pattern
				.compile("(\\d{2}-[A-Z]{3}-\\d{4}) \\(Rel. (\\d+), ([^,\\)]+)(, Version (\\d+))?");
		Matcher m = dateLinePattern.matcher(line);
		if (m.find()) {
			String dateStr = m.group(1);
			Integer releaseNum = new Integer(m.group(2));
			String createOrUpdated = m.group(3);
			Integer version = (m.group(5) != null) ? new Integer(m.group(5)) : null;
			try {
				return new EmblDate(EmblDate.parseDate(dateStr), createOrUpdated, releaseNum, version);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Unparsable date in date line: " + line, e);
			}
		}
		throw new IllegalArgumentException("Unable to match date line: " + line);
	}

	/**
	 * @param line
	 * @return
	 */
	private InsdcProjectId parsePRLine(String line) {
		return new InsdcProjectId(StringUtil.removeSuffix(StringUtil.removePrefix(line, "PR   Project:"), ";"));
	}

	/**
	 * @param line
	 * @return contents of the ID line
	 */
	protected abstract E parseIDLine(String line);
	
	/**
	 * To be implemented by subclasses. This method allows flexibility in terms of what primary
	 * datasourceidentifier type to use for a particular data record parser.
	 * 
	 * @param group
	 * @return
	 */
	protected abstract D initPrimaryAccessionNumberType(String group);

	

	@Data
	private static class SqLineContents {
		private final int sequenceLength;
		private final int numAs;
		private final int numTs;
		private final int numCs;
		private final int numGs;
		private final int numOthers;
	}

	/**
	 * Process AC line
	 * 
	 * @param line
	 * @return
	 */
	private List<D> parseACLine(String line) {
		List<D> ids = new ArrayList<D>();
		String[] toks = line.substring(2).trim().split(";");
		for (String tok : toks) {
			ids.add(initPrimaryAccessionNumberType(tok.trim()));
		}
		return ids;
	}

	
	
}
