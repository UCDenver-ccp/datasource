/**
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
package edu.ucdenver.ccp.datasource.fileparsers.custom;

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

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ProteinOntologyId;

public class CuratedTFRecordReader extends SingleLineFileRecordReader<CuratedTFRecord> {

	private static final String EXPECTED_HEADER = "ENSEMBL_ID\tHGNC_SYMBOL\tPRO_ID\tINTERPRO_IDS\tENTREZ_IDS\tGO_EVIDENCE\tPFAM_DOMAINS";

	public CuratedTFRecordReader(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return EXPECTED_HEADER;
	}

	@Override
	protected CuratedTFRecord parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		if (toks.length == EXPECTED_HEADER.split("\\t").length) {
			int index = 0;
			EnsemblGeneID ensemblId = new EnsemblGeneID(toks[index++]);
			HgncGeneSymbolID hgncSymbol = (toks[index++].trim().isEmpty()) ? null
					: new HgncGeneSymbolID(toks[index - 1]);
			ProteinOntologyId prId = (toks[index++].trim().isEmpty()) ? null : new ProteinOntologyId(toks[index - 1]);
			String interproIdStr = toks[index++];
			Set<InterProID> interproIds = new HashSet<InterProID>();
			if (!interproIdStr.trim().isEmpty() && !interproIdStr.equalsIgnoreCase("none")) {
				for (String idStr : interproIdStr.split(";")) {
					interproIds.add(new InterProID(idStr));
				}
			}
			NcbiGeneId ncbiGeneId = (toks[index++].trim().isEmpty()) ? null : new NcbiGeneId(toks[index - 1]);
			String goIdStr = toks[index++];
			Set<GeneOntologyID> goIds = new HashSet<GeneOntologyID>();
			if (!goIdStr.trim().isEmpty()) {
				Pattern p = Pattern.compile("#(GO:\\d+)#");
				Matcher m = p.matcher(goIdStr);
				while (m.find()) {
					goIds.add(new GeneOntologyID(m.group(1)));
				}
			}
			String pfamDomains = toks[index++];

			return new CuratedTFRecord(ensemblId, hgncSymbol, prId, interproIds, ncbiGeneId, goIds, pfamDomains,
					line.getByteOffset(), line.getLineNumber());
		}
		throw new IllegalStateException(
				"Encountered line with unexpected number of columns (" + toks.length + "): " + line.getText());
	}
	
}
