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
package edu.ucdenver.ccp.fileparsers.snomed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.reflection.ConstructorUtil;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ec.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.flybase.FlyBaseID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.sgd.SgdID;
import edu.ucdenver.ccp.datasource.identifiers.wormbase.WormBaseID;
import edu.ucdenver.ccp.fileparsers.snomed.SnomedRf2DescriptionFileRecord.DescriptionType;

public class SnomedRf2DescriptionFileRecordReader extends SingleLineFileRecordReader<SnomedRf2DescriptionFileRecord> {

	private static final Logger logger = Logger.getLogger(SnomedRf2DescriptionFileRecordReader.class);
	private static final String HEADER = "id\teffectiveTime\tactive\tmoduleId\tconceptId\tlanguageCode\ttypeId\tterm\tcaseSignificanceId";

	public static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;
	public static final String SKIP_LINE_PREFIX = null;

	public SnomedRf2DescriptionFileRecordReader(File file) throws IOException {
		super(file, ENCODING, SKIP_LINE_PREFIX);
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
	protected SnomedRf2DescriptionFileRecord parseRecordFromLine(Line line) {
		return parseSnomedRf2DescriptionFileLine(line);
	}

	/**
	 * @param line
	 * @return
	 */
	private SnomedRf2DescriptionFileRecord parseSnomedRf2DescriptionFileLine(Line line) {
		int index = 0;
		String[] columns = line.getText().split(RegExPatterns.TAB, -1);
		if (columns.length != 9) {
			logger.warn("Unexpected number of columns observed. Expected 9 but was " + columns.length + " on line: "
					+ line.getText());
			return null;
		}

		String descriptionId = columns[index++];
		String effectiveTime = columns[index++];
		boolean isActive = (columns[index++].equals("1")) ? true : false;
		String moduleId = columns[index++];
		String conceptId = columns[index++];
		String languageCode = columns[index++];
		DescriptionType type = DescriptionType.getType(columns[index++]);
		String term = columns[index++];
		String caseSignificanceId = columns[index++];

		return new SnomedRf2DescriptionFileRecord(descriptionId, effectiveTime, isActive, moduleId, conceptId,
				languageCode, type, term, caseSignificanceId, line.getByteOffset(), line.getLineNumber());
	}

}
