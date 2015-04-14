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

public class SnomedRf2ConceptFileRecordReader extends SingleLineFileRecordReader<SnomedRf2ConceptFileRecord> {

	private static final Logger logger = Logger.getLogger(SnomedRf2ConceptFileRecordReader.class);
	private static final String HEADER = "id\teffectiveTime\tactive\tmoduleId\tdefinitionStatusId";

	public static final CharacterEncoding ENCODING = CharacterEncoding.UTF_8;
	public static final String SKIP_LINE_PREFIX = null;

	public SnomedRf2ConceptFileRecordReader(File file) throws IOException {
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
	protected SnomedRf2ConceptFileRecord parseRecordFromLine(Line line) {
		return parseSnomedRf2ConceptFileLine(line);
	}

	/**
	 * @param line
	 * @return
	 */
	private SnomedRf2ConceptFileRecord parseSnomedRf2ConceptFileLine(Line line) {
		int index = 0;
		String[] columns = line.getText().split(RegExPatterns.TAB, -1);
		if (columns.length != 5) {
			logger.warn("Unexpected number of columns observed. Expected 5 but was " + columns.length + " on line: "
					+ line.getText());
			return null;
		}

		String conceptId = columns[index++];
		String effectiveTime = columns[index++];
		boolean isActive = (columns[index++].equals("1")) ? true : false;
		String moduleId = columns[index++];
		String definitionStatusId = columns[index++];

		return new SnomedRf2ConceptFileRecord(conceptId, effectiveTime, isActive, moduleId, definitionStatusId,
				line.getByteOffset(), line.getLineNumber());

	}

}
