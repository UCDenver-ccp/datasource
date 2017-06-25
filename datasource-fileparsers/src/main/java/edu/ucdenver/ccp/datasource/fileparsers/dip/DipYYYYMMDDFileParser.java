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
package edu.ucdenver.ccp.datasource.fileparsers.dip;

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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.obo.MiOntologyIdTermPair;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DipInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DipInteractorID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * This class is used to parse DIPYYYMMDD files which can be downloaded from the
 * DIP website: http://dip.doe-mbi.ucla.edu/dip/Main.cgi
 * 
 * @author Bill Baumgartner
 * 
 */
public class DipYYYYMMDDFileParser extends TaxonAwareSingleLineFileRecordReader<DipYYYYMMDDFileData> {

	private static final Logger logger = Logger.getLogger(DipYYYYMMDDFileParser.class);

	private static final String HEADER = "ID interactor A\tID interactor B\tAlt. ID interactor A\tAlt. ID interactor B\tAlias(es) interactor A\tAlias(es) interactor B\tInteraction detection method(s)\tPublication 1st author(s)\tPublication Identifier(s)\tTaxid interactor A\tTaxid interactor B\tInteraction type(s)\tSource database(s)\tInteraction identifier(s)\tConfidence value(s)\tProcessing Status\t";

	public DipYYYYMMDDFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	public DipYYYYMMDDFileParser(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonsOfInterest)
			throws IOException {
		super(file, encoding, taxonsOfInterest);
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
		DipYYYYMMDDFileData record = parseRecordFromLine(line);
		// should probably return both tax id's here
		return record.getInteractor_A().getNcbiTaxonomyId().getTaxonomyId();
	}

	@Override
	protected DipYYYYMMDDFileData parseRecordFromLine(Line line) {
		if (line.getText().startsWith("ID interactor A")) {
			logger.warn("No relevant information to parse on line: " + line.toString());
			return null;
		}

		String[] toks = line.getText().split("\\t", -1);
		if (toks.length == 18) {
			DipInteractor interactorA = getInteractor(toks[0], toks[2], toks[4], toks[9], line.getText());
			DipInteractor interactorB = getInteractor(toks[1], toks[3], toks[5], toks[10], line.getText());

			Set<DipInteractionExperiment> interactionExperiments = getInteractionExperiments(toks[6], toks[7], toks[8],
					toks[11], toks[15], line.getText());

			DipInteractionSourceDatabase sourceDatabase = MiOntologyIdTermPair.parseString(
					DipInteractionSourceDatabase.class, toks[12]);

			DipInteractionID interactionID = new DipInteractionID(toks[13]);

			String qualityStatus = StringUtil.removePrefix(toks[14], "dip-quality-status:");
			if (qualityStatus.trim().isEmpty()) {
				qualityStatus = null;
			}

			if (!toks[16].isEmpty() && toks[17].equals("-")) {
				throw new IllegalStateException(
						"Unexpected values in trailing columns. Expected a space followed by a hyphen but observed: '"
								+ toks[16] + "' then '" + toks[17] + "'");
			}

			return new DipYYYYMMDDFileData(interactorA, interactorB, interactionExperiments, sourceDatabase,
					interactionID, qualityStatus, line.getByteOffset(), line.getLineNumber());

		}

		String errorMessage = "Unexpected number of tokens (" + toks.length + ") on line: " + line;
		logger.error(errorMessage);
		throw new IllegalArgumentException("DIP file format appears to have changed: " + errorMessage);
	}

	/**
	 * @return
	 */
	private Set<DipInteractionExperiment> getInteractionExperiments(String detectionMethodsStr,
			String pubFirstAuthorStr, String pubIdsStr, String interactionTypesStr, String processingStatusStr,
			String line) {
		Set<DipInteractionExperiment> experiments = new HashSet<DipInteractionExperiment>();

		if (!pubFirstAuthorStr.equals("-")) {
			throw new IllegalArgumentException(
					"Unexpected value observed for the 'Publication 1st author(s)' column. This column did not previously contain valid values. Code changes required: "
							+ pubFirstAuthorStr + " on line: " + line);
		}

		String[] detectionMethods = detectionMethodsStr.split(RegExPatterns.PIPE);
		String[] pmids = pubIdsStr.split(RegExPatterns.PIPE);
		String[] interactionTypes = interactionTypesStr.split(RegExPatterns.PIPE);
		String[] processingStatuses = processingStatusStr.split(RegExPatterns.PIPE);
		/*
		 * check to ensure there are equal numbers of values in each of the
		 * interaction-experiment-related columns since they are pipe-delimitied
		 */
		if (!(detectionMethods.length == pmids.length / 2 && detectionMethods.length == interactionTypes.length && detectionMethods.length == processingStatuses.length)) {
			throw new IllegalArgumentException(
					"Unbalanced pipes found in interaction-experiment-related columns. There should be equal value counts for detectionMethods("
							+ detectionMethods.length
							+ "), 2*pmids("
							+ pmids.length
							+ "),interactionTypes("
							+ interactionTypes.length
							+ "), and processingStatuses("
							+ processingStatuses.length
							+ ") on line: " + line);
		}

		for (int i = 0; i < detectionMethods.length; i++) {
			DipInteractionDetectionMethod detectionMethod = MiOntologyIdTermPair.parseString(
					DipInteractionDetectionMethod.class, detectionMethods[i]);
			DipInteractionType interactionType = MiOntologyIdTermPair.parseString(DipInteractionType.class,
					interactionTypes[i]);
			DipProcessingStatus processingStatus = getDipProcessingStatus(processingStatuses[i], line);
			String firstAuthorName = null; // change if the first author column
											// ever contains names
			DipPublication publication = getDipPublication(firstAuthorName, pmids[i * 2], pmids[i * 2 + 1]);

			experiments.add(new DipInteractionExperiment(publication, processingStatus, detectionMethod,
					interactionType));

		}

		return experiments;
	}

	/**
	 * @param string
	 * @param string2
	 * @param pmids
	 * @return {@link DipPublication} from first author name and conversions of
	 *         strings like "pubmed:9194558" and "pubmed:DIP-209S" into a
	 *         {@link PubMedID} and a {@link DipPublicationId}
	 */
	private DipPublication getDipPublication(String firstAuthorName, String pmidStr, String dipPubIdStr) {
		PubMedID pmid;
		try {
			pmid = new PubMedID(StringUtil.removePrefix(pmidStr, "pubmed:"));
		} catch (IllegalArgumentException e) {
			logger.warn("Detected invalid PubMed identifier: " + pmidStr + " for DIP publication ID: " + dipPubIdStr);
			pmid = null;
		}
		DipPublicationId dipPubId = new DipPublicationId(StringUtil.removePrefix(dipPubIdStr, "pubmed:"));
		return new DipPublication(pmid, dipPubId, firstAuthorName);
	}

	/**
	 * @param string
	 * @return {@link DipProcessingStatus} parsed from a string such as:
	 *         "dip:0002(small scale)"
	 */
	private DipProcessingStatus getDipProcessingStatus(String statusStr, String line) {
		Pattern p = Pattern.compile("(dip:\\d+)\\((.*?)\\)");
		Matcher m = p.matcher(statusStr);
		if (m.find()) {
			return new DipProcessingStatus(new DipProcessingStatusId(m.group(1)), m.group(2));
		}
		logger.warn("Invalid processing status detected: " + statusStr + ". On line: " + line);
		return null;
	}

	private DipInteractor getInteractor(String interactorStr, String alternateIdsStr, String aliasesStr,
			String taxIdStr, String line) {
		String[] idToks = interactorStr.split("\\|");
		if (idToks.length > 0) {
			if (idToks[0].startsWith("DIP")) {
				DipInteractorID id = new DipInteractorID(idToks[0]);
				Set<DataSourceIdentifier<?>> dbXRefIds = new HashSet<DataSourceIdentifier<?>>();
				for (int i = 1; i < idToks.length; i++) {
					String idStr = idToks[i].replace("RTD:", "");
					DataSourceIdentifier<?> xrefId = resolveId(idStr);
					if (xrefId != null) {
						dbXRefIds.add(xrefId);
					}
				}

				/*
				 * The columns for alternate IDs and aliases are always set to
				 * "-". If this is no longer the case then an exception will be
				 * thrown and code changes required.
				 */
				Set<DipInteractorID> alternateIds = null;
				if (!alternateIdsStr.trim().equals("-")) {
					alternateIds = getAlternateDipIds(alternateIdsStr, line);
				}

				Set<String> aliases = null;
				if (!aliasesStr.trim().equals("-")) {
					throw new IllegalArgumentException("non-null aliases: " + line);
				}

				// taxid:7227(Drosophila melanogaster)
				NcbiTaxonomyID taxId = null;
				String taxonomyDescription = null;
				if (taxIdStr.startsWith("taxid:-2")) {
					taxonomyDescription = StringUtil.removeLastCharacter(StringUtil.removePrefix(taxIdStr, "taxid:-2"));
				} else {
					Pattern p = Pattern.compile("taxid:(\\d+)\\((.*?)\\)");
					Matcher m = p.matcher(taxIdStr);
					if (m.find()) {
						taxId = new NcbiTaxonomyID(m.group(1));
						taxonomyDescription = m.group(2);
					} else {
						logger.warn("Invalid NCBI Taxonomy ID detected: " + taxIdStr);
					}
				}

				return new DipInteractor(id, new DipInteractorOrganism(taxId, taxonomyDescription), alternateIds,
						aliases, dbXRefIds);
			}
			logger.error("No DIP ID for record: " + line);
			return null;
		}
		logger.error("No DIP ID for record: " + line);
		return null;
	}

	/**
	 * @param alternateIdsStr
	 * @return
	 */
	private Set<DipInteractorID> getAlternateDipIds(String alternateIdsStr, String line) {
		Set<DipInteractorID> ids = new HashSet<DipInteractorID>();
		for (String altIdStr : alternateIdsStr.split(RegExPatterns.PIPE)) {
			ids.add(new DipInteractorID(altIdStr));
		}
		return ids;
	}

	/**
	 * @param idStr
	 * @return
	 */
	private DataSourceIdentifier<?> resolveId(String idStr) {
		if (idStr.startsWith("DIP-")) {
			return new DipInteractorID(idStr);
		}
		if (idStr.startsWith("refseq:")) {
			return ProteinAccessionResolver.resolveProteinAccession(StringUtil.removePrefix(idStr, "refseq:"), idStr);
		}
		if (idStr.startsWith("uniprotkb:")) {
			if (idStr.contains(StringConstants.HYPHEN_MINUS)) {
				return new UniProtIsoformID(StringUtil.removePrefix(idStr, "uniprotkb:"));
			}
			try {
				return new UniProtID(StringUtil.removePrefix(idStr, "uniprotkb:"));
			} catch (IllegalArgumentException e) {
				return new ProbableErrorDataSourceIdentifier(idStr, null, e.getMessage());
			}
		}
		throw new IllegalArgumentException("Unhandled identifier type: " + idStr);
	}

	// /**
	// * Extract Molecular Interaction Ontology Term ID from {@code inputStr}
	// *
	// * @param inputStr
	// * MI id
	// * @return id if recognized; otherwise, null
	// */
	// private static MolecularInteractionOntologyTermID extractMiId(String
	// inputStr) {
	// Pattern methodIDPattern = Pattern.compile("(MI:\\d+),?\\(");
	// Matcher m = methodIDPattern.matcher(inputStr);
	// if (m.find()) {
	// return new MolecularInteractionOntologyTermID(m.group(1));
	// }
	// logger.error("Unable to locate ExperimentalMethod MI ID in String: " +
	// inputStr);
	// return null;
	// }

}
