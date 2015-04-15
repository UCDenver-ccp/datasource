/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.other.InsdcProjectId;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class EmblNucleotideSequenceDatabaseFileParser extends
		EmblSequenceDatabaseFileParserBase<EmblSequenceDatabaseFileData, EmblID, EmblIdLineContents> {

	/**
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
	public EmblNucleotideSequenceDatabaseFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding);
	}

	@Override
	protected EmblID initPrimaryAccessionNumberType(String id) {
		return new EmblID(id);
	}

	/**
	 * @param line
	 * @return contents of the ID line
	 */
	@Override
	protected EmblIdLineContents parseIDLine(String line) {
		String restOfLine = line.substring(2).trim();
		String[] toks = restOfLine.split(";");
		EmblID primaryAccessionNumber = initPrimaryAccessionNumberType(toks[0].trim());
		String sequenceVersionNumber = toks[1].trim();
		String sequenceTopology = toks[2].trim();
		String moleculeType = toks[3].trim();
		String dataClass = toks[4].trim();
		String taxonomicDivision = toks[5].trim();
		int sequenceLengthInBasePairs = Integer.parseInt(StringUtil.removeSuffix(toks[6].trim(), " BP.").trim());

		return new EmblIdLineContents(primaryAccessionNumber, sequenceVersionNumber, sequenceTopology, moleculeType,
				dataClass, taxonomicDivision, sequenceLengthInBasePairs);
	}

	@Override
	protected DataSourceIdentifier<?> parseDRLine(String line) {
		String[] toks = line.substring(2).split(";");
		String dbKey = toks[0].trim();
		String dbId = toks[1].trim();
		if (dbKey.equalsIgnoreCase("MGI")) {
			return new MgiGeneID("MGI:" + dbId);
		}
		throw new IllegalArgumentException("Unhandled database cross reference: " + line);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.fileparsers.ebi.embl.EmblNucleotideSequenceDatabaseFileParserBase#
	 * parseFeatureTable(java.lang.String, java.io.BufferedReader)
	 */
	@Override
	protected Collection parseFeatureTable(String line, BufferedReader br) throws IOException {
		throw new UnsupportedOperationException("This feature has not been implemented, as parsing of generic EMBL files has not been used.");
	}

	@Override
	protected EmblSequenceDatabaseFileData invokeConstructor(EmblIdLineContents idLineContents, List accessionNumbers,
			InsdcProjectId projectId, Set dates, String description, Set keyWords, String organismSpeciesName,
			String organismClassification, String organelle, Set referenceCitations, Set databaseCrossReferences,
			String comments, Set sequenceFeatures, int sequenceLength, int numAs, int numCs, int numGs, int numTs,
			int numOthers, String sequence, String constructedSeqInfo, Set assemblyInfo, long byteOffset) {
		return new EmblSequenceDatabaseFileData(idLineContents.getPrimaryAccessionNumber(),
				idLineContents.getSequenceVersionNumber(), idLineContents.getSequenceTopology(),
				idLineContents.getMoleculeType(), idLineContents.getDataClass(), idLineContents.getTaxonomicDivision(),
				idLineContents.getSequenceLengthInBasePairs(), accessionNumbers, projectId, dates, description,
				keyWords, organismSpeciesName, organismClassification, organelle, referenceCitations,
				databaseCrossReferences, comments, sequenceFeatures, sequenceLength, numAs, numCs, numGs, numTs,
				numOthers, sequence, constructedSeqInfo, assemblyInfo, byteOffset);
	}

	@Override
	protected void parseGNLine(String line) {
		throw new IllegalStateException("GN line should not exist in the EMBL file format. It is only used by UniProt.");
	}

	@Override
	protected void parseOXLine(String line) {
		throw new IllegalStateException("OX line should not exist in the EMBL file format. It is only used by UniProt.");
	}

}
