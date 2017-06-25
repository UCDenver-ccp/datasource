package edu.ucdenver.ccp.datasource.fileparsers.vectorbase;

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
import java.util.zip.GZIPInputStream;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VectorBaseID;

public class VectorBaseFastaFileRecordReader extends MultiLineFileRecordReader<VectorBaseFastaFileRecord> {
	// write a test for this class, then generate rdf for the transcript file
	public VectorBaseFastaFileRecordReader(File file, CharacterEncoding encoding) throws IOException {
		super((file.getName().endsWith(".gz")) ? new GZIPInputStream(new FileInputStream(file)) : new FileInputStream(
				file), encoding, null);
	}

	public VectorBaseFastaFileRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean)
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
		} while (line != null && !line.getText().startsWith(">"));

		return multiLineBuffer;
	}

	@Override
	protected VectorBaseFastaFileRecord parseRecordFromMultipleLines(MultiLineBuffer multiLineBuffer) {
		try {
			BufferedReader br = new BufferedReader(new StringReader(multiLineBuffer.toString()));
			String line;
			VectorBaseID sequenceId = null;
			String sequenceName = null;
			String sequenceType = null;
			String contig = null;
			VectorBaseID geneId = null;
			String sequence = null;

			while ((line = br.readLine()) != null) {
				if (line.startsWith(">")) {
					String references = "";
					/*
					 * a minority of entries contain a bracketed expression
					 * containing external references, e.g.
					 * {ECO:0000303|PubMed:9887508} that may have a pipe within
					 * so we handle those bracketed expressions separately here
					 */
					if (line.contains("{")) {
						int referencesStart = line.indexOf("{");
						int referencesEnd = line.indexOf("}");
						references = line.substring(referencesStart, referencesEnd + 1);
						line = line.substring(0, referencesStart) + line.substring(referencesEnd + 1);
					}
					String[] toks = line.substring(1).split("\\|");
					int index = 0;
					int firstSpace = toks[0].indexOf(" ");
					sequenceId = new VectorBaseID(toks[0].substring(0, firstSpace));
					String nameStr = toks[index++].substring(firstSpace + 1).trim();
					if (!nameStr.isEmpty()) {
						sequenceName = nameStr + ((!references.isEmpty()) ? " " : "") + references;
					}
					sequenceType = toks[index++].trim();
					contig = toks[index++].trim();
					geneId = new VectorBaseID(StringUtil.removePrefix(toks[index++], "gene:"));
				} else {
					if (sequence == null) {
						sequence = "";
					}
					sequence += line.trim();
				}
			}

			return new VectorBaseFastaFileRecord(sequenceId, sequenceName, sequenceType, contig, geneId, sequence,
					multiLineBuffer.getByteOffset());

		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

}