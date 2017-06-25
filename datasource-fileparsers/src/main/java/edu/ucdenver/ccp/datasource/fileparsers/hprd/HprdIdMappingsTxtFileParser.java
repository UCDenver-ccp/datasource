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
package edu.ucdenver.ccp.datasource.fileparsers.hprd;

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
import java.util.ArrayList;
import java.util.List;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * This class is used to parse the InterPro names.dat file
 * 
 * @author Bill Baumgartner
 * 
 */
public class HprdIdMappingsTxtFileParser extends SingleLineFileRecordReader<HprdIdMappingsTxtFileData> {

	public static final String HPRD_ID_MAPPINGS_TXT_FILE_NAME = "HPRD_ID_MAPPINGS.txt";

	public HprdIdMappingsTxtFileParser(File file, CharacterEncoding encoding) throws IOException {
		super(file, encoding, null);
	}

	@Override
	protected HprdIdMappingsTxtFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		if (toks.length == 8) {
			HprdID hprdID = new HprdID(toks[0]);
			String geneSymbol = null;
			if (!toks[1].equals("-")) {
				geneSymbol = toks[1];
			}
			DataSourceIdentifier<?> nucleotideAccession = resolveAccession(toks[2]);
			DataSourceIdentifier<?> proteinAccession = resolveAccession(toks[3]);
			NcbiGeneId entrezGeneID = null;
			if (!toks[4].equals("-")) {
				entrezGeneID = new NcbiGeneId(toks[4]);
			}
			OmimID omimID = null;
			if (!toks[5].equals("-") && !toks[5].equals("0")) {
				omimID = new OmimID(toks[5]);
			}
			List<UniProtID> swissProtIDs = new ArrayList<UniProtID>();
			if (!toks[6].equals("-")) {
				for (String spIDStr : toks[6].split(",")) {
					swissProtIDs.add(new UniProtID(spIDStr));
				}
			}
			String mainName = toks[7];
			return new HprdIdMappingsTxtFileData(hprdID, geneSymbol, nucleotideAccession, proteinAccession,
					entrezGeneID, omimID, swissProtIDs, mainName, line.getByteOffset(), line.getLineNumber());
		}
		throw new IllegalArgumentException("Unexpected number of tokens (" + toks.length + ") on line: " + line);
	}

	private DataSourceIdentifier<?> resolveAccession(String acc) {
		DataSourceIdentifier<String> nucAccId = NucleotideAccessionResolver.resolveNucleotideAccession(acc, acc);
		if (ProbableErrorDataSourceIdentifier.class.isInstance(nucAccId)) {
			return ProteinAccessionResolver.resolveProteinAccession(acc, acc);
		}
		return nucAccId;
	}

}
