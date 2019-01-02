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
import java.util.Iterator;

import org.apache.tools.ant.util.StringUtils;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineIterator;
import edu.ucdenver.ccp.common.string.StringUtil;

/**
 * contains code to make OWL representations of identifiers that are
 * missing @Identifier annotations
 */
public class CcpOntologyIdentifierUtil {

	public enum IdentifierType {
		BIO("0000342"), ICE("0000341");
		private final String id;

		private IdentifierType(String id) {
			this.id = id;
		}

		public String id() {
			return id;
		}
	}

	private int nextIdNumber;

	public CcpOntologyIdentifierUtil(int nextIdNumber) {
		this.nextIdNumber = nextIdNumber;

	}

	private static final String getOwl(int idNumber, String label, IdentifierType idType) {
		return "<!-- http://ccp.ucdenver.edu/obo/ext/IAO_EXT_" + idNumber + " -->\n\n"
				+ "<owl:Class rdf:about=\"http://ccp.ucdenver.edu/obo/ext/IAO_EXT_" + idNumber + "\">\n"
				+ "    <rdfs:subClassOf rdf:resource=\"http://ccp.ucdenver.edu/obo/ext/IAO_EXT_" + idType.id()
				+ "\"/>\n" + "    <rdfs:label xml:lang=\"en\">" + label + "</rdfs:label>\n" + "</owl:Class>";
	}

	public void createOwlForMissingIdentifiers(File javaClassDirectory, IdentifierType idType, File owlOutputFile,
			File updatedIdClassOutputDirectory) throws IOException {

		try (BufferedWriter writer = FileWriterUtil.initBufferedWriter(owlOutputFile)) {

			for (Iterator<File> fileIter = FileUtil.getFileIterator(javaClassDirectory, false); fileIter.hasNext();) {
				File file = fileIter.next();

				if (!containsIdentifierAnnotation(file)) {
					System.out.println("Creating ID concept for file: " + file.getName());
					String label = getIdentifierConceptLabelFromFilename(file);
					String owlString = getOwl(nextIdNumber++, label, idType);
					writer.write(owlString + "\n\n\n");

					updateFileWithIdentifierAnnotation(file, label, updatedIdClassOutputDirectory);

				}

			}
		}

	}

	private void updateFileWithIdentifierAnnotation(File file, String label, File outputDirectory) throws IOException {

		try (BufferedWriter writer = FileWriterUtil.initBufferedWriter(new File(outputDirectory, file.getName()))) {

			String className = StringUtils.removeSuffix(file.getName(), ".java");

			for (StreamLineIterator lineIter = new StreamLineIterator(file, CharacterEncoding.UTF_8); lineIter
					.hasNext();) {
				Line line = lineIter.next();

				String lineText = line.getText();
				if (lineText.contains("public class " + className)) {
					String updatedLine = "@Identifier(ontClass=CcpExtensionOntology."
							+ CcpOntologyEnumBuilder.createEnumLabel(label) + ")\n" + lineText;
					writer.write(updatedLine + "\n");
				} else {
					writer.write(lineText + "\n");
				}

			}
		}

	}

	private boolean containsIdentifierAnnotation(File file) throws IOException {
		boolean containsIdentifierAnnot = false;
		for (StreamLineIterator lineIter = new StreamLineIterator(file, CharacterEncoding.UTF_8); lineIter.hasNext();) {
			Line line = lineIter.next();
			if (line.getText().contains("@Identifier")) {
				return true;
			}
		}
		return containsIdentifierAnnot;

	}

	private String getIdentifierConceptLabelFromFilename(File file) {
		String label = StringUtil.removeSuffix(file.getName(), ".java");
		if (label.endsWith("ID") || label.endsWith("Id")) {
			label = label.substring(0, label.length() - 2);
		}
		label = label + " identifier";
		return label;
	}

	public static void main(String[] args) {
		int nextIdNumber = Integer.parseInt(args[0]);
		File bioIdentifierClassDirectory = new File(args[1]);
		File iceIdentifierClassDirectory = new File(args[2]);
		File outputDirectory = new File(args[3]);

		File bioOwlOutputFile = new File(outputDirectory, "bio-identifiers.owl");
		File iceOwlOutputFile = new File(outputDirectory, "ice-identifiers.owl");

		File modifiedBioIdsDir = new File(bioIdentifierClassDirectory.getAbsolutePath() + "/tmp");
		File modifiedIceIdsDir = new File(iceIdentifierClassDirectory.getAbsolutePath() + "/tmp");
		FileUtil.mkdir(modifiedBioIdsDir);
		FileUtil.mkdir(modifiedIceIdsDir);

		System.out.println("MOD BIO DIR: " + modifiedBioIdsDir.getAbsolutePath());

		CcpOntologyIdentifierUtil util = new CcpOntologyIdentifierUtil(nextIdNumber);

		try {
			util.createOwlForMissingIdentifiers(bioIdentifierClassDirectory, IdentifierType.BIO, bioOwlOutputFile,
					modifiedBioIdsDir);
			util.createOwlForMissingIdentifiers(iceIdentifierClassDirectory, IdentifierType.ICE, iceOwlOutputFile,
					modifiedIceIdsDir);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
