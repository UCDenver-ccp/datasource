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
package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtFileRecord.DbReference;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtFileRecord.Organism;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareMultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;

/**
 * This class is used to parse the UniProt uniprot_sprot.dat and uniprot_trembl.dat files
 * 
 * @author Bill Baumgartner
 * 
 */
public class SparseUniProtDatFileRecordReader extends TaxonAwareMultiLineFileRecordReader<SparseUniProtFileRecord> {

	private static final Logger logger = Logger.getLogger(SparseUniProtDatFileRecordReader.class);

//	public SparseUniProtDatFileRecordReader(File file, CharacterEncoding encoding) throws IOException {
//		super((file.getName().endsWith(".gz")) ? new GZIPInputStream(new FileInputStream(file)) : new FileInputStream(
//				file), encoding, null, null);
//	}

	public SparseUniProtDatFileRecordReader(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super((file.getName().endsWith(".gz")) ? new GZIPInputStream(new FileInputStream(file)) : new FileInputStream(
				file), encoding, null, taxonIds);
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
//	public SparseUniProtDatFileRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean)
//			throws IOException {
//		super(workDirectory, encoding, null, null, null, clean, null);
//	}

	public SparseUniProtDatFileRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean,
			Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(workDirectory, encoding, null, null, null, clean, taxonIds);
	}

	@Override
	protected void initialize() throws IOException {
		line = readLine();
		super.initialize();
	}

	@Override
	protected MultiLineBuffer compileMultiLineBuffer() throws IOException {
		if (line == null)
			return null;
		MultiLineBuffer multiLineBuffer = new MultiLineBuffer();
		do {
			multiLineBuffer.add(line);
			line = readLine();
		} while (line != null && !line.getText().startsWith("//"));
		line = readLine();
		return multiLineBuffer;
	}

	@Override
	protected SparseUniProtFileRecord parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer) {
		try {
			BufferedReader br = new BufferedReader(new StringReader(multiLineBuffer.toString()));
			String line;
			UniProtEntryName uniprotEntryName = null;
			List<UniProtID> uniprotIDs = new ArrayList<UniProtID>();
			Organism organism = null;
			List<Organism> organismHosts = new ArrayList<Organism>();
			String canonicalGeneSymbol = null;
			Set<String> fullGeneNames = new HashSet<String>();
			Set<String> synonyms = new HashSet<String>();
			List<UniProtIsoformID> isoformIds = new ArrayList<UniProtIsoformID>();

			boolean hasGoAnnotation = false;
			while ((line = br.readLine()) != null) {
				// if (line.startsWith("//")) {
				// break;
				// } else
				if (line.startsWith("ID")) {
					uniprotEntryName = UniProtDatFileUtil.parseIDLine(line);
				} else if (line.startsWith("AC")) {
					uniprotIDs.addAll(UniProtDatFileUtil.parseACLine(line));
				} else if (line.startsWith("OX")) {
					organism = UniProtDatFileUtil.parseOXLine(line);
				} else if (line.startsWith("OH")) {
					Organism o = UniProtDatFileUtil.parseOXLine(line);
					if (o != null) {
						organismHosts.add(o);
					}
				} else if (line.startsWith("DE")) {
					String fullName = UniProtDatFileUtil.parseDELine(line, br);
					if (fullName != null) {
						fullGeneNames.add(fullName);
					}
				} else if (line.startsWith("CC")) {
					isoformIds.addAll(UniProtDatFileUtil.parseCCLine(line, br));
				} else if (line.startsWith("DR")) {
					if (line.substring(3).trim().startsWith("GO;")) {
						hasGoAnnotation = true;
					}
				}

				if (line.startsWith("GN")) {
					List<String> geneNameAndSynonyms = UniProtDatFileUtil.parseGNLineForNameAndSynonyms(line, br);
					if (geneNameAndSynonyms.size() > 0) {
						canonicalGeneSymbol = geneNameAndSynonyms.get(0);
						synonyms.addAll(geneNameAndSynonyms.subList(1, geneNameAndSynonyms.size()));
					}
				}
			}

			if (organism != null && uniprotIDs.size() > 0) {
				UniProtID primaryUniprotID = uniprotIDs.get(0);
				uniprotIDs.remove(0);
				List<String> allNames = new ArrayList<String>();
				allNames.add(canonicalGeneSymbol);
				allNames.addAll(fullGeneNames);
				allNames.addAll(synonyms);
				// sparse representation won't contain x-refs
				List<DbReference> dbReferences = new ArrayList<DbReference>();

				return new SparseUniProtFileRecord(primaryUniprotID, uniprotIDs, allNames, organism, organismHosts,
						dbReferences, multiLineBuffer.getByteOffset());
			}

			logger.warn("Could not create UniProtDatFileData. Missing either ncbiTaxonomyID or uniprotID(s): "
					+ uniprotIDs.toString());
			return null;

		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	@Override
	protected NcbiTaxonomyID getRecordTaxon(MultiLineBuffer buffer) {
		SparseUniProtFileRecord record = parseRecordFromMultipleLines(buffer);
		for (DbReference dbRef : record.getOrganism().getDbReference()) {
			DataSourceIdentifier<?> id = dbRef.getId();
			if (id instanceof NcbiTaxonomyID) {
				return (NcbiTaxonomyID) id;
			}
		}
		logger.warn("Unable to determine taxon id for record: " + record.getPrimaryAccession());
		return new NcbiTaxonomyID(0);
	}

}