package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

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

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.ntriples.NTriplesUtil;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.digest.DigestUtil;
import edu.ucdenver.ccp.common.reflection.PrivateAccessor;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.RecordUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.KIAO;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RdfRecordUriFactory {

	public enum IncludeVersion {
		YES,
		NO
	}

	/**
	 * @param record
	 * @return the URI for the input Record. The URI should take the form
	 *         [NAMESPACE]:R_[FILENAME]_[SHA1 DIGEST FOR SORTED FIELDS AND VALUE URIs]<br>
	 *         The string that is input into the sha1 algorithm should consist of field value URIs
	 *         sorted first by the field then by the values. The field-value URI pairs are then
	 *         added to the string with no spaces. Each URI is surrounded by angle brackets.
	 */
	public static URIImpl createRecordUri(Object record) {
		String inputFileType = record.getClass().getSimpleName();
		DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(record.getClass()));
		String sha1Str = RdfRecordUriFactory.sha1DigestForSortedFieldsAndValues(record);
		return RdfUtil.createKiaoUri(ns, "R_" + inputFileType + "_" + sha1Str);
	}

	public static URIImpl createRecordSchemaUri(Class<?> recordClass, IncludeVersion includeVersion) {
		String inputFileType = recordClass.getSimpleName();
		DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(recordClass));
		String localName = inputFileType + KIAO.SCHEMA.termName();
		if (includeVersion.equals(IncludeVersion.YES)) {
			localName += RecordUtil.getRecordSchemaVersion(recordClass);
		}
		return RdfUtil.createKiaoUri(ns, localName);
	}

	public static URIImpl createRecordTypeUri(Class<?> recordClass) {
		DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(recordClass));
		return RdfUtil.createKiaoUri(ns, recordClass.getSimpleName());
	}

	public static URIImpl createFieldUri(Object record, Field field, Object value) {
		if (value == null) {
			return null;
		}
		String sha1DigestForFieldValues = RdfRecordUriFactory.getSha1DigestForFieldValuePairing(record, field, value);
		if (sha1DigestForFieldValues == null) {
			return null;
		}
		DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(record.getClass()));
		return RdfUtil.createKiaoUri(ns, "F_" + record.getClass().getSimpleName() + "_" + field.getName() + "_"
				+ sha1DigestForFieldValues);
	}

	/**
	 * @param src
	 * @param inputFileType
	 * @param fieldName
	 * @return
	 */
	public static URIImpl createDataFieldTemplateUri(Class<?> recordClass, String fieldName,
			IncludeVersion includeVersion) {
		DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(recordClass));
		String localName = recordClass.getSimpleName() + "_" + fieldName + KIAO.DATAFIELD.termName();
		if (includeVersion.equals(IncludeVersion.YES)) {
			localName += RecordUtil.getRecordFieldVersion(recordClass, fieldName);
		}
		return RdfUtil.createKiaoUri(ns, localName);
	}

	/**
	 * @param record
	 * @param rdfSource
	 * @param inputFileType
	 * @return a sorted {@link LinkedHashMap} containing mappings from field names to field values
	 *         sorted as strings. Only record fields that are annotated with {@link RecordField} are
	 *         included in the mapping.
	 */
	private static LinkedHashMap<String, List<String>> getSortedFieldAndValueUriStrs(Object record) {
		LinkedHashMap<String, List<String>> sortedFieldsAndValues = new LinkedHashMap<String, List<String>>();
		Set<Field> fields = RecordUtil.getFieldToRecordFieldAnnotationsMap(record.getClass()).keySet();
		if (fields.isEmpty()) {
			throw new IllegalArgumentException(
					"Detected DataRecord with no fields annotated with RecordField annotations. Please add RecordField annotations to class: "
							+ record.getClass().getName());
		}
		List<Field> sortedFields = new ArrayList<Field>(fields);
		Collections.sort(sortedFields, new FieldNameComparator());
		for (Field field : sortedFields) {
			Collection<Object> fieldValues = getFieldValues(record, field);
			if (fieldValues != null) {
				URIImpl dataFileNameUri = createDataFieldTemplateUri(record.getClass(), field.getName(),
						IncludeVersion.YES);
				List<String> fieldValueUriStrs = getSortedFieldValueUriStrs(fieldValues);
				sortedFieldsAndValues.put("<" + dataFileNameUri.toString() + ">", fieldValueUriStrs);
			}
		}
		return sortedFieldsAndValues;
	}

	/**
	 * @param fieldValues
	 * @return
	 */
	private static List<String> getSortedFieldValueUriStrs(Collection<Object> fieldValues) {
		List<String> fieldValueUriStrs = new ArrayList<String>();
		for (Object fieldValue : fieldValues) {
			fieldValueUriStrs.add(getFieldValueUri(fieldValue));
		}
		Collections.sort(fieldValueUriStrs);
		return fieldValueUriStrs;
	}

	/**
	 * Generates the appropriate URI or constant value for the input field value. This method takes
	 * advantage of the {@link RdfUtil#getFieldDenotesValueStatement(URI, Object)} method by
	 * generating the complete statement then parsing out the Object field.
	 * 
	 * @param fieldValueUriStrs
	 * @param fieldValue
	 *            could be a collection, if so we return one string per value
	 */
	private static String getFieldValueUri(Object fieldValue) {
		/* address unknown and probable error data source identifiers here? */
		if (fieldValue instanceof UnknownDataSourceIdentifier) {
			UnknownDataSourceIdentifier id = (UnknownDataSourceIdentifier) fieldValue;
			NonNormalizedIdentifierRecord record = new NonNormalizedIdentifierRecord(id.getDataElement(), id.getDataSourceStr());
			URIImpl recordUri = RdfRecordUriFactory.createRecordUri(record);
			List<Statement> recordInstanceStatements = RdfRecordUtil.getRecordInstanceStatements(record, System.currentTimeMillis(),
					recordUri, null, null, null);
			recordInstanceStatements.remove(0);
			/* this is used to generate sha1 hashes, so it doesn't need to be a true uri */
			return CollectionsUtil.createDelimitedString(recordInstanceStatements, " ");
		} else if (fieldValue instanceof ProbableErrorDataSourceIdentifier) {
			ProbableErrorDataSourceIdentifier id = (ProbableErrorDataSourceIdentifier) fieldValue;
			ErroneousIdentifierRecord record = new ErroneousIdentifierRecord(id.getDataElement(),
					id.getDataSourceStr(), id.getErrorMessage());
			URIImpl recordUri = RdfRecordUriFactory.createRecordUri(record);
			List<Statement> recordInstanceStatements = RdfRecordUtil.getRecordInstanceStatements(record, System.currentTimeMillis(),
					recordUri, null, null, null);
			/*
			 * the first statement returned is a dataset has_part record triple
			 * which we do not need
			 */
			recordInstanceStatements.remove(0);
			return CollectionsUtil.createDelimitedString(recordInstanceStatements, " ");
		}
		Value value = RdfUtil.getValue(fieldValue);
		return NTriplesUtil.toNTriplesString(value);
	}

	private static String getSortedFieldsAndValuesStr(Object record) {
		LinkedHashMap<String, List<String>> sortedFieldsAndValues = getSortedFieldAndValueUriStrs(record);
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, List<String>> entry : sortedFieldsAndValues.entrySet()) {
			for (String value : entry.getValue()) {
				buffer.append(entry.getKey() + value);
			}
		}
		return buffer.toString();
	}

	static String sha1DigestForSortedFieldsAndValues(Object record) {
		String sortedStr = getSortedFieldsAndValuesStr(record);
		if (sortedStr == null) {
			return null;
		}
		return DigestUtil.getBase64Sha1Digest(sortedStr);
	}

	private static String getSha1DigestForFieldValuePairing(Object record, Field field, Object fieldValue) {
		if (fieldValue instanceof Collection) {
			throw new IllegalArgumentException("Collection input argument not allowed");
		}
		URIImpl dataFileNameUri = createDataFieldTemplateUri(record.getClass(), field.getName(), IncludeVersion.YES);
		String fieldValueUri = getFieldValueUri(fieldValue);
		if (fieldValueUri == null) {
			return null;
		}
		String fieldValueStr = "<" + dataFileNameUri + ">" + fieldValueUri;
		return DigestUtil.getBase64Sha1Digest(fieldValueStr);
	}

	/**
	 * @param field
	 * @return
	 */
	private static Collection<Object> getFieldValues(Object record, Field field) {
		Object fieldValue = PrivateAccessor.getFieldValue(record, field.getName());
		if (fieldValue == null) {
			return null;
		}

		Collection<Object> fieldValues = new ArrayList<Object>();

		if (!(fieldValue instanceof Collection)) {
			fieldValues.add(getNonCollectionFieldValues(fieldValue));
		} else {
			Collection<?> coll = (Collection<?>) fieldValue;
			for (Object object : coll) {
				if (object != null) {
					fieldValues.add(getNonCollectionFieldValues(object));
				}
			}
		}

		return fieldValues;
	}

	/**
	 * @param fieldValue
	 * @return
	 */
	private static Object getNonCollectionFieldValues(Object fieldValue) {
		if (fieldValue instanceof Collection) {
			throw new IllegalArgumentException("Collection fieldValue is not supported");
		}
		if (fieldValue == null) {
			throw new IllegalArgumentException("Null field values not supported");
		}
		return fieldValue;
	}

	/**
	 * Comparator that uses {@link Field#getName()} for comparison.
	 */
	private static class FieldNameComparator implements Comparator<Field> {
		/**
		 * Compare using fields' names
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Field o1, Field o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}

}
