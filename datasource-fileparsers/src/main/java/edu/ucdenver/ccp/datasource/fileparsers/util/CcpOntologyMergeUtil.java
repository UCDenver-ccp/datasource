package edu.ucdenver.ccp.datasource.fileparsers.util;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2017 Regents of the University of Colorado
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineIterator;

/**
 * This class helps facilitate ontology merges (via git) by renumbering ontology
 * identifiers
 *
 */
public class CcpOntologyMergeUtil {

	private int nextOntologyId;
	private final String idPrefixToIncorporate;
	private final Pattern replacementPattern;

	private Map<String, String> originalToReplacedIdMap = new HashMap<String, String>();

	public CcpOntologyMergeUtil(int nextOntologyId, String idPrefixToIncorporate) {
		super();
		this.nextOntologyId = nextOntologyId;
		this.idPrefixToIncorporate = idPrefixToIncorporate;
		this.replacementPattern = Pattern.compile(idPrefixToIncorporate + "\\d+");
	}

	public void updateOntology(File inputOwlFile, File outputOwlFile, File outputMappingFile) throws IOException {
		try (BufferedWriter writer = FileWriterUtil.initBufferedWriter(outputOwlFile)) {

			for (StreamLineIterator lineIter = new StreamLineIterator(inputOwlFile, CharacterEncoding.UTF_8); lineIter
					.hasNext();) {
				Line line = lineIter.next();

				String lineText = line.getText();

				if (lineText.contains(idPrefixToIncorporate)) {
					String replacementLine = lineText;

//					System.out.println("line in need of replacement: " + lineText);
					
					Matcher matcher = replacementPattern.matcher(lineText);
					while (matcher.find()) {
						String idToReplace = matcher.group();
						System.out.println("found group to replace: " + idToReplace);
						String nextId = null;
						if (!originalToReplacedIdMap.containsKey(idToReplace)) {
							nextId = "IAO_EXT_" + getZeroPaddedId(nextOntologyId++);
							System.out.println("next id: " + nextId);
							originalToReplacedIdMap.put(idToReplace, nextId);
						} else {
							nextId = originalToReplacedIdMap.get(idToReplace);
						}
						replacementLine = replacementLine.replaceAll(idToReplace, nextId);
					}
					writer.write(replacementLine + "\n");
				} else {
					writer.write(lineText + "\n");
				}
			}
		}
		
		try (BufferedWriter writer = FileWriterUtil.initBufferedWriter(outputMappingFile)) {
			for (Entry<String, String> entry : originalToReplacedIdMap.entrySet()) {
				writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			}
		}
		
	}

	private String getZeroPaddedId(int i) {
		return String.format("%07d", i);
	}

	public static void main(String[] args) {
		int nextOntologyId = Integer.parseInt(args[0]);
		String idPrefixToIncorporate = args[1];
		File inputOwlFile = new File(args[2]);
		File outputOwlFile = new File(args[3]);
		File outputMappingFile = new File(args[4]);

		try {
			new CcpOntologyMergeUtil(nextOntologyId, idPrefixToIncorporate).updateOntology(inputOwlFile, outputOwlFile, outputMappingFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}
}
