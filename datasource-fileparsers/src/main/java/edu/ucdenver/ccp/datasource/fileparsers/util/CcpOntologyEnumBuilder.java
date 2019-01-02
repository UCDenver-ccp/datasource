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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.collections.CollectionsUtil.SortOrder;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OntologyUtil;
import lombok.Data;

/**
 * This class builds the {@link CcpExtensionOntology} enum by processing the CCP
 * ontology file
 */
public class CcpOntologyEnumBuilder {

	private static final String FILE_PREFIX = "package edu.ucdenver.ccp.datasource.fileparsers;\n" + "\n"

			+ "import java.net.URI;\n" + "import java.net.URISyntaxException;\n" + "\n"
			+ "import edu.ucdenver.ccp.datasource.identifiers.DataSource;\n" + "\n"

			+ "/**\n" + "* This class is automatically generated from the CCP ontology.\n" + "*\n" + "*/\n"
			+ "public enum CcpExtensionOntology {\n";

	private static final String FILE_SUFFIX = "\tprivate final String id;\n" + "\tprivate final String prefix;\n" + "\n"
			+ "\tprivate CcpExtensionOntology(String id) {\n" + "\t\tthis.id = id;\n" + "\t\tthis.prefix = null;\n"
			+ "\t}\n" + "\n" + "\tprivate CcpExtensionOntology(String id, String prefix) {\n" + "\t\tthis.id = id;\n"
			+ "\t\tthis.prefix = prefix;\n" + "\t}\n" + "\n" + "\tpublic String prefix() {\n"
			+ "\t\treturn this.prefix;\n" + "\t}\n" + "\n" + "\tpublic String id() {\n" + "\t\treturn this.id;\n"
			+ "\t}\n" + "\n" + "\tpublic URI uri()  {\n" + "\t\ttry {\n"
			+ "\t\t\treturn new URI(DataSource.CCP + id);\n" + "\t\t} catch (URISyntaxException e) {\n"
			+ "\t\t\tthrow new IllegalStateException(e);\n" + "\t\t}\n" + "\t}\n" + "\n" + "}";

	private static final String CCP_NAMESPACE = "http://ccp.ucdenver.edu/obo/ext/";

	public static void buildEnum(File ontologyFile, File sourceFile) throws OWLOntologyCreationException, IOException {

		Map<String, IdWithPrefix> identifierClassLabel2idMap = new HashMap<String, IdWithPrefix>();
		Map<String, String> classLabel2idMap = new HashMap<String, String>();

		OntologyUtil ontUtil = new OntologyUtil(ontologyFile);
		OWLClass identifierSuperCls = ontUtil.getOWLClassFromId("http://purl.obolibrary.org/obo/IAO_0000578");
		for (Iterator<OWLClass> classIter = ontUtil.getClassIterator(); classIter.hasNext();) {
			OWLClass cls = classIter.next();
			if (ontUtil.isDescendent(cls, identifierSuperCls)) {
				Collection<OWLAnnotationValue> values = ontUtil.getAnnotationPropertyValues(cls,
						"http://purl.obolibrary.org/obo/IAO_0000599");
				// should be a single value here, just the prefix, or
				// potentially no value for the identifier upper-level classes
				if (values.size() > 1) {
					throw new IllegalStateException(
							"multiple ID prefixes found for concept: " + cls.getIRI().toString());
				}
				String prefix = null;
				if (values.size() == 1) {
					prefix = values.iterator().next().toString();
				}
				cacheId2LabelMapping(identifierClassLabel2idMap, ontUtil.getLabel(cls), cls.getIRI(), prefix);
			} else {
				cacheId2LabelMapping(classLabel2idMap, ontUtil.getLabel(cls), cls.getIRI());
			}
		}

		Map<String, String> annotPropertyLabel2idMap = new HashMap<String, String>();
		for (Iterator<OWLAnnotationProperty> annotPropIter = ontUtil.getAnnotationPropertyIterator(); annotPropIter
				.hasNext();) {
			OWLAnnotationProperty prop = annotPropIter.next();
			cacheId2LabelMapping(annotPropertyLabel2idMap, ontUtil.getLabel(prop), prop.getIRI());
		}

		Map<String, String> objectPropertyLabel2idMap = new HashMap<String, String>();
		for (Iterator<OWLObjectProperty> objectPropIter = ontUtil.getObjectPropertyIterator(); objectPropIter
				.hasNext();) {
			OWLObjectProperty prop = objectPropIter.next();
			cacheId2LabelMapping(objectPropertyLabel2idMap, ontUtil.getLabel(prop), prop.getIRI());
		}

		writeFile(classLabel2idMap, identifierClassLabel2idMap, annotPropertyLabel2idMap, objectPropertyLabel2idMap,
				sourceFile);

	}

	private static void cacheId2LabelMapping(Map<String, String> label2idMap, String label, IRI iri) {
		String namespace = iri.getNamespace();
		if (namespace != null && namespace.equals(CCP_NAMESPACE)) {
			String id = iri.getShortForm();
			String enumLabel = createEnumLabel(label);
			if (label2idMap.containsKey(enumLabel)) {
				System.err
						.println("WARNING -- duplicate prefix detected. Likely duplicate ontology concept exists for: "
								+ enumLabel);
			}
			label2idMap.put(enumLabel, id);
		}
	}

	private static void cacheId2LabelMapping(Map<String, IdWithPrefix> label2idMap, String label, IRI iri,
			String prefix) {
		String namespace = iri.getNamespace();
		if (namespace != null && namespace.equals(CCP_NAMESPACE)) {
			String id = iri.getShortForm();
			IdWithPrefix idWithPrefix = new IdWithPrefix(id, prefix);
			String enumLabel = createEnumLabel(label);
			if (label2idMap.containsKey(enumLabel)) {
				System.err
						.println("WARNING -- duplicate prefix detected. Likely duplicate ontology concept exists for: "
								+ enumLabel);
			}
			label2idMap.put(enumLabel, idWithPrefix);
		}
	}

	@Data
	private static class IdWithPrefix {
		private final String id;
		private final String prefix;
	}

	public static String createEnumLabel(String label) {
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

	private static void writeFile(Map<String, String> classLabel2IdMap,
			Map<String, IdWithPrefix> identifierClassLabel2idMap, Map<String, String> annotationPropertyLabel2IdMap,
			Map<String, String> objectPropertyLabel2idMap, File sourceFile) throws IOException {
		StringBuilder builder = new StringBuilder(FILE_PREFIX);

		builder.append("\t/* Annotation Properties */\n");
		addSortedEnumClasses(annotationPropertyLabel2IdMap, builder);

		builder.append("\t/* Object Properties */\n");
		addSortedEnumClasses(objectPropertyLabel2idMap, builder);

		builder.append("\t/* Record and Field Value Classes */\n");
		addSortedEnumClasses(classLabel2IdMap, builder);

		builder.append("\t/* Identifier Classes */\n");
		addSortedEnumClassesWithPrefix(identifierClassLabel2idMap, builder);

		/* delete the final comma and replace with a semi-colon */
		builder.deleteCharAt(builder.length() - 1);
		builder.append(";\n\n");
		builder.append(FILE_SUFFIX);

		try (BufferedWriter writer = FileWriterUtil.initBufferedWriter(sourceFile)) {
			writer.write(builder.toString());
		}

	}

	private static void addSortedEnumClassesWithPrefix(Map<String, IdWithPrefix> identifierClassLabel2idMap,
			StringBuilder builder) {
		Map<String, Map<String, IdWithPrefix>> prefix2label2idMap = new HashMap<String, Map<String, IdWithPrefix>>();
		for (Entry<String, IdWithPrefix> entry : identifierClassLabel2idMap.entrySet()) {
			String idPrefix = entry.getValue().getId().substring(0, entry.getValue().getId().lastIndexOf("_"));
			if (!prefix2label2idMap.containsKey(idPrefix)) {
				prefix2label2idMap.put(idPrefix, new HashMap<String, IdWithPrefix>());
			}
			prefix2label2idMap.get(idPrefix).put(entry.getKey(), entry.getValue());
		}

		List<String> prefixList = new ArrayList<String>(prefix2label2idMap.keySet());
		Collections.sort(prefixList);

		for (String prefix : prefixList) {
			Map<String, IdWithPrefix> sortedMap = CollectionsUtil.sortMapByKeys(prefix2label2idMap.get(prefix),
					SortOrder.ASCENDING);
			// sortMapByValuesIgnoreCase(prefix2label2idMap.get(prefix),
			// SortOrder.ASCENDING);

			for (Entry<String, IdWithPrefix> entry : sortedMap.entrySet()) {
				// builder.append("\t" + entry.getKey() + "(\"" +
				// removeLanguageTag(entry.getValue()) + "\"),\n");
				builder.append("\t" + entry.getKey() + "(\"" + entry.getValue().getId() + "\", "
						+ entry.getValue().getPrefix() + "),\n");
			}
			builder.append("\n");
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
			CcpOntologyEnumBuilder.buildEnum(ontologyFile, sourceFile);
		} catch (OWLOntologyCreationException | IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
