package edu.ucdenver.ccp.datasource.fileparsers.pro;

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
import java.util.Set;
import java.util.HashSet;



import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;

import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PirSfID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PfamID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PantherID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.ProteinOntologyId;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;

/**
 * File parser for Protein Ongology promapping.txt file.
 * 
 * @author Yuriy Malenkiy
 * 
 */
public class PAFFileParser extends SingleLineFileRecordReader<PAFRecord> {

	public static final String IS_A_RELATION = "is_a";
	public static final String EXACT_RELATION = "exact";

	private static final String HEADER = "PRO_ID\tObject_term\tObject_syny\tModifier\tRelation\tOntology_ID\tOntology_term\tRelative_to\tInteraction_with\tEvidence_source\tEvidence_code\tTaxon\tInferred_from	DB_ID\tAssigned_by\tComment";

	public static final CharacterEncoding FILE_ENCODING = CharacterEncoding.US_ASCII;

	@FtpDownload(server = "ftp.pir.georgetown.edu", path = "databases/ontology/pro_obo/old_files/tmp1/", filename = "PAF.txt", filetype = FileType.ASCII)
	private File mappingFile;

	/**
	 * Constructor.
	 * 
	 * @param dataFile
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public PAFFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding);
	}

	/**
	 * Default constructor
	 * 
	 * @param workDirectory
	 * @param clean
	 * @throws IOException
	 */
	public PAFFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, FILE_ENCODING, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(mappingFile, encoding, skipLinePrefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.fileparsers.SingleLineFileRecordReader#parseRecordFromLine
	 * (edu.ucdenver. ccp.common.file.reader.LineReader.Line)
	 */
	@Override
	protected PAFRecord parseRecordFromLine(Line line) {
		String text = line.getText();
		PAFRecord r = null;

		if (text.startsWith("PR:")) {
			
			String[] tokens = text.split("\t");
			int k = 0;
			ProteinOntologyId fromId = new ProteinOntologyId(tokens[0].trim());
			String modifier = "";
			String relationName = "";
			DataSourceIdentifier<?> relationOntologyId = null;
			String relationOntologyTerm = "";
			DataSourceIdentifier<?> referenceProtein = null;
			String evidenceCode = "";
			String taxon = "";
			String date = "";
			String annotatorInitials = "";
			String comment = "";
			
			String name = tokens[1].trim();
			
			Set<String> nameSynonyms = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(tokens[2], StringConstants.VERTICAL_LINE, null,
					RemoveFieldEnclosures.FALSE)) {
				nameSynonyms.add(new String(syn));
			}
			if (tokens[3].length() > 0) {
				modifier = new String(tokens[3].trim());
			}
			
			if (tokens[4].length() > 0) {
				relationName = new String(tokens[4].trim());
			}
			if (tokens[5].length() > 0) {
				relationOntologyId = resolveId(tokens[5].trim());
			}
			if (tokens[6].length() > 0) {
				relationOntologyTerm = new String(tokens[6].trim());
			}
			if (tokens[7].length() > 0) {
				referenceProtein = resolveId(tokens[7].trim());
			}
			Set<DataSourceIdentifier<?>> interactionPartners = new HashSet<DataSourceIdentifier<?>>();
			if (tokens[8].length() > 0) {
				for (String partner : StringUtil.delimitAndTrim(tokens[8], StringConstants.VERTICAL_LINE, null,
						RemoveFieldEnclosures.FALSE)) {
					DataSourceIdentifier<?> intPartner = resolveId(partner);
					interactionPartners.add(intPartner);
				}
			}
			Set<DataSourceIdentifier<?>> evidenceSource = new HashSet<DataSourceIdentifier<?>>();
			if (tokens[9].length() > 0) {
				for (String src : StringUtil.delimitAndTrim(tokens[9], StringConstants.VERTICAL_LINE, null,
						RemoveFieldEnclosures.FALSE)) {
					DataSourceIdentifier<?> evid = resolveId(src);
					evidenceSource.add(evid);
				}
			}
				
			
				
			if (tokens[10].length() > 0) {
				evidenceCode = tokens[10].trim();
			}
			if (tokens[11].length() > 0) {
				taxon = tokens[11].trim();
			}
			Set<DataSourceIdentifier<?>> inferredFrom = new HashSet<DataSourceIdentifier<?>>();
			if (tokens[12].length() > 0) {
				for (String src : StringUtil.delimitAndTrim(tokens[12], StringConstants.VERTICAL_LINE, null,
						RemoveFieldEnclosures.FALSE)) {
					DataSourceIdentifier<?> evid = resolveId(src);
					inferredFrom.add(evid);
				}
			}
			Set<DataSourceIdentifier<?>> inferredFromDB = new HashSet<DataSourceIdentifier<?>>();
			if (tokens[13].length() > 0) {
				for (String src : StringUtil.delimitAndTrim(tokens[13], StringConstants.VERTICAL_LINE, null,
						RemoveFieldEnclosures.FALSE)) {
					DataSourceIdentifier<?> evid = resolveId(src);
					inferredFromDB.add(evid);
				}
			}
			if (tokens[14].length() > 0) {
				date = tokens[14].trim();
			}
			
			if (tokens[15].length() > 0) {
				annotatorInitials = tokens[15].trim();
			}
			
			
			r = new PAFRecord(fromId, name, nameSynonyms, modifier, relationName, relationOntologyId, 
					relationOntologyTerm, referenceProtein, interactionPartners, evidenceSource,
					evidenceCode, taxon, inferredFrom, inferredFromDB, date, annotatorInitials, "",
					line.getByteOffset(), line.getLineNumber());
		}

		return r;
	}

	private DataSourceIdentifier<?> resolveId(String idStr) {
		try {
			if (idStr.startsWith("MGI:")) {
				return new MgiGeneID(idStr);
			}
			if (idStr.startsWith("Pfam:")) {
				return new PfamID(StringUtil.removePrefix(idStr, "Pfam:"));
			}
			if (idStr.startsWith("GO:")) {
				return new GeneOntologyID(idStr);
			}
			if (idStr.startsWith("RGD:")) {
				return new RgdID(StringUtil.removePrefix(idStr, "RGD:"));
			}
			if (idStr.startsWith("PIRSF:")) {
				return new PirSfID(StringUtil.removePrefix(idStr, "PIRSF:"));
			}
			if (idStr.startsWith("PANTHER:")) {
				return new PantherID(StringUtil.removePrefix(idStr, "PANTHER:"));
			}
			if (idStr.startsWith("HGNC:")) {
				return new HgncID(idStr);
			}
			if (idStr.startsWith("UniProtKB:")) {
				return new UniProtID(StringUtil.removePrefix(idStr, "UniProtKB:"));
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, null, e.getMessage());
		}
		return new UnknownDataSourceIdentifier(idStr, null);
	}
}
