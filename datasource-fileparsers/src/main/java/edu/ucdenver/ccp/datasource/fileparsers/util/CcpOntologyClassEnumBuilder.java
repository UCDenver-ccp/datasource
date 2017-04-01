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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.collections.CollectionsUtil.SortOrder;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntologyClass;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OntologyUtil;

/**
 * This class builds the {@link CcpExtensionOntologyClass} enum by processing
 * the CCP ontology file
 */
public class CcpOntologyClassEnumBuilder {

	private static final String FILE_PREFIX = "package edu.ucdenver.ccp.datasource.fileparsers;\n" + "\n" + "/**\n"
			+ "* This class is automatically generated from the CCP ontology.\n" + "*\n" + "*/\n"
			+ "public enum CcpExtensionOntologyClass {\n";

	private static final String FILE_SUFFIX = "\tprivate final String label;\n" + "\n"
			+ "\tprivate CcpExtensionOntologyClass(String label) {\n" + "\t\tthis.label = label;\n" + "\t}\n" + "\n"
			+ "\tpublic String label() {\n" + "\t\treturn this.label;\n" + "\t}\n" + "\n" + "}\n";

	private static final String CCP_NAMESPACE = "http://ccp.ucdenver.edu/obo/ext/";

	public static void buildEnum(File ontologyFile, File sourceFile) throws OWLOntologyCreationException, IOException {

		Map<String, String> label2idMap = new HashMap<String, String>();

		OntologyUtil ontUtil = new OntologyUtil(ontologyFile);
		for (Iterator<OWLClass> classIter = ontUtil.getClassIterator(); classIter.hasNext();) {
			OWLClass cls = classIter.next();
			String namespace = cls.getIRI().getNamespace();
			if (namespace != null && namespace.equals(CCP_NAMESPACE)) {
				String label = ontUtil.getLabel(cls);
				String id = cls.getIRI().getShortForm();
				label2idMap.put(createEnumLabel(label), id);
				System.out.println("ID: " + id + " label: " + label);
			}
		}

		writeClassFile(label2idMap, sourceFile);

	}

	private static String createEnumLabel(String label) {
		return removeLanguageTag(label).toUpperCase().replaceAll(" ", "_").replaceAll("-", "_").replaceAll("\\.", "")
				.replaceAll("\\(", "").replaceAll("\\)", "");
	}

	/**
	 * sorts the classes, first by the ID prefix, e.g. IAO_EXT_, then
	 * alphabetically by label
	 * 
	 * @param label2idMap
	 * @return
	 */
	private static void addSortedEnumClasses(Map<String, String> label2idMap, StringBuilder builder) {
		Map<String, Map<String, String>> prefix2label2idMap = new HashMap<String, Map<String, String>>();
		for (Entry<String, String> entry : label2idMap.entrySet()) {
			String idPrefix = entry.getValue().substring(0, entry.getValue().lastIndexOf("_"));
			if (!prefix2label2idMap.containsKey(idPrefix)) {
				prefix2label2idMap.put(idPrefix, new HashMap<String, String>());
			}
			prefix2label2idMap.get(idPrefix).put(entry.getKey(), entry.getValue());
		}

		List<String> prefixList = new ArrayList<String>(prefix2label2idMap.keySet());
		Collections.sort(prefixList);

		for (String prefix : prefixList) {
			Map<String, String> sortedMap = CollectionsUtil.sortMapByKeys(prefix2label2idMap.get(prefix),
					SortOrder.ASCENDING);
			// sortMapByValuesIgnoreCase(prefix2label2idMap.get(prefix),
			// SortOrder.ASCENDING);

			for (Entry<String, String> entry : sortedMap.entrySet()) {
				// builder.append("\t" + entry.getKey() + "(\"" +
				// removeLanguageTag(entry.getValue()) + "\"),\n");
				builder.append("\t" + entry.getKey() + "(\"" + entry.getValue() + "\"),\n");
			}
			builder.append("\n");
		}

	}

	/**
	 * @param map
	 * @return a map sorted by the values in the input map, however case is
	 *         ignored so the labels starting with capitals don't all appear
	 *         first but are instead mixed in with the labels that start with
	 *         lowercase letters
	 */
	private static Map<String, String> sortMapByValuesIgnoreCase(Map<String, String> inputMap,
			final SortOrder sortOrder) {
		ArrayList<Entry<String, String>> entryList = new ArrayList<Entry<String, String>>(inputMap.entrySet());
		Collections.sort(entryList, new Comparator<Entry<String, String>>() {

			@Override
			public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
				return entry1.getValue().toLowerCase().compareTo(entry2.getValue().toLowerCase())
						* sortOrder.modifier();
			}

		});
		Map<String, String> sortedMap = new LinkedHashMap<String, String>();
		for (Entry<String, String> entry : entryList) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	private static void writeClassFile(Map<String, String> id2labelMap, File sourceFile) throws IOException {
		StringBuilder builder = new StringBuilder(FILE_PREFIX);

		addSortedEnumClasses(id2labelMap, builder);

		/* delete the final comma and replace with a semi-colon */
		builder.deleteCharAt(builder.length() - 1);
		builder.append(";\n\n");
		builder.append(FILE_SUFFIX);

		try (BufferedWriter writer = FileWriterUtil.initBufferedWriter(sourceFile)) {
			writer.write(builder.toString());
		}

	}

	private static String removeLanguageTag(String s) {
		String suffix = "\"@en";
		if (s.endsWith(suffix)) {
			return StringUtil.removeSuffix(s, suffix);
		}
		return s;
	}

	/**
	 * This class takes a two arguments:
	 * 
	 * @param args
	 *            args[0] = the path to the CCP extension ontology file<br>
	 *            args[1] = the path to the source file to generate
	 * 
	 */
	public static void main(String[] args) {
		File ontologyFile = new File(args[0]);
		File sourceFile = new File(args[1]);
		try {
			FileUtil.validateFile(ontologyFile);
			CcpOntologyClassEnumBuilder.buildEnum(ontologyFile, sourceFile);
		} catch (OWLOntologyCreationException | IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
