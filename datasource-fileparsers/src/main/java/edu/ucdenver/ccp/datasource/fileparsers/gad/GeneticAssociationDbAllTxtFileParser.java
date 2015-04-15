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
package edu.ucdenver.ccp.datasource.fileparsers.gad;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;

/**
 * This class is used to parse the InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class GeneticAssociationDbAllTxtFileParser extends
		SingleLineFileRecordReader<GeneticAssociationDbAllTxtFileData> {

	private static final String HEADER = "Tab deliminated text file\n"
			+ "ID\tAssociation(Y/N)\tBroad Phenotype\tDisease Class\tDisease Class Code\tMeSH Disease Terms\tChromosom\tChr-Band\tGene\tDNA Start\tDNA End\tP Value\tReference\tPubmed ID\tAllele Author Description\tAllele Functional Effects\tPolymophism Class\tGene Name\tRefSeq\tPopulation\tMeSH Geolocation\tSubmitter\tLocus Number\tUnigene\t Narrow Phenotype\tMole. Phenotype\tJournal\tTitle\trs Number\tOMIM ID\tYear\tConclusion\tStudy Info\tEnv. Factor\tGI Gene A\tGI Allele of Gene A\tGI Gene B\tGI Allele of Gene B\tGI Gene C\tGI Allele of Gene C\tGI Association?\tGI combine Env. Factor\tGI relevant to Disease";

	public static final String GAD_ALL_TXT_FILE_NAME = "all.txt";

	private static Logger logger = Logger.getLogger(GeneticAssociationDbAllTxtFileParser.class);

	public GeneticAssociationDbAllTxtFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	// /**
	// * During the initialization we want to skip the header which consists of 3 lines
	// */
	// protected void initialize() throws IOException {
	// Line line;
	// for (int i = 0; i < 3; i++) {
	// line = readLine();
	// if (line.getText().matches("^\\d")) {
	// String errorMessage = "Expected header in mim2gene file, but instead observed a data line: "
	// + line;
	// logger.error(errorMessage);
	// throw new IOException(errorMessage);
	// }
	// }
	//
	// super.initialize();
	// }

	@Override
	protected String getFileHeader() throws IOException {
		// skip the first line of the header b/c it contains a timestamp
		readLine();
		return readLine().getText() + "\n" + readLine().getText();

	}

	@Override
	protected GeneticAssociationDbAllTxtFileData parseRecordFromLine(Line line) {
		try {
			return GeneticAssociationDbAllTxtFileData.parseGeneticAssociationDbAllTxtLine(line);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid line detected (" + e.getMessage() + "), returning null: " + line.getText());
			return null;
		}

	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

}
