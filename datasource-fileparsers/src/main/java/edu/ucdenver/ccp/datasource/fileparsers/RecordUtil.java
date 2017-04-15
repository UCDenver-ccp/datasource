/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.reflection.PrivateAccessor;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * A utility class for handling {@link Record} and {@link RecordField} annotations
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RecordUtil {

	private static final Logger logger = Logger.getLogger(RecordUtil.class);

	/**
	 * @param record
	 * @return a mapping from {@link Field} to {@link RecordField} annotation for those fields that
	 *         have RecordField annotations in the input {@link DataRecord}
	 */
	public static Map<Field, RecordField> getFieldToRecordFieldAnnotationsMap(Class<?> recordClass) {
		Map<Field, RecordField> fieldNameToRecordFieldAnnotationMap = new HashMap<Field, RecordField>();
		Set<Field> fields = PrivateAccessor.getAllFields(recordClass, new HashSet<Field>());
		for (Field field : fields) {
			if (field.isAnnotationPresent(RecordField.class)) {
				fieldNameToRecordFieldAnnotationMap.put(field, field.getAnnotation(RecordField.class));
			}
		}
		return fieldNameToRecordFieldAnnotationMap;
	}

	public static Record getRecordAnnotation(Class<?> recordClass) {
		if (recordClass.isAnnotationPresent(Record.class)) {
			return recordClass.getAnnotation(Record.class);
		}
		throw new IllegalArgumentException("Detected missing Record annotation. Please add a Record annotation to the "
				+ recordClass.getName() + " class.");
	}

	public static DataSource getRecordDataSource(Class<?> recordClass) {
		return getRecordAnnotation(recordClass).dataSource();
	}
	
	public static CcpExtensionOntology getRecordType(Class<?> recordClass) {
		return getRecordAnnotation(recordClass).ontClass();
	}

	public static String getRecordComment(Class<?> recordClass) {
		return getRecordAnnotation(recordClass).comment();
	}

	public static String getRecordLabel(Class<?> recordClass) {
		return getRecordAnnotation(recordClass).label();
	}

	/**
	 * @param recordClass
	 * @return
	 */
	public static String getRecordSchemaVersion(Class<?> recordClass) {
		return getRecordAnnotation(recordClass).schemaVersion();
	}

	/**
	 * @param recordClass
	 * @param name
	 * @return
	 */
	public static RecordField getRecordFieldAnnotation(Class<?> recordClass, String fieldName) {
		Set<Field> fields = PrivateAccessor.getAllFields(recordClass, new HashSet<Field>());
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				if (field.isAnnotationPresent(RecordField.class)) {
					return field.getAnnotation(RecordField.class);
				}
				throw new IllegalArgumentException(
						"Detected missing RecordField annotation. Please add a RecordField annotation to the '"
								+ fieldName + "' field in the " + recordClass.getName() + " class.");
			}
		}
		throw new IllegalArgumentException("The '" + fieldName + "' field does not exist in class: "
				+ recordClass.getName());
	}

	public static String getRecordFieldVersion(Class<?> recordClass, String fieldName) {
		return getRecordFieldAnnotation(recordClass, fieldName).version();
	}
	
	public static CcpExtensionOntology getRecordFieldType(Class<?> recordClass, String fieldName) {
		return getRecordFieldAnnotation(recordClass, fieldName).ontClass();
	}

	public static String getRecordFieldComment(Class<?> recordClass, String fieldName) {
		return getRecordFieldAnnotation(recordClass, fieldName).comment();
	}

	public static String getRecordFieldLabel(Class<?> recordClass, String fieldName) {
		return getRecordFieldAnnotation(recordClass, fieldName).label();
	}

	public static boolean isKeyRecordField(Class<?> recordClass, String fieldName) {
		return getRecordFieldAnnotation(recordClass, fieldName).isKeyField();
	}

	private RecordUtil() {
		// this class should not be instantiated
	}

	/**
	 * @param class1
	 * @return
	 */
	public static Set<String> getKeyFieldNames(Class<? extends DataRecord> recordCls) {
		Set<String> keyFieldNames = new HashSet<String>();
		Map<Field, RecordField> fieldToRecordFieldMap = getFieldToRecordFieldAnnotationsMap(recordCls);
		for (Entry<Field, RecordField> entry : fieldToRecordFieldMap.entrySet()) {
			if (entry.getValue().isKeyField()) {
				keyFieldNames.add("F_" + recordCls.getSimpleName() + "_" + entry.getKey().getName() + "_");
			}
		}
		for (String name : keyFieldNames) {
			logger.info("Key field name to be ignored by duplicate cache: " + name);
		}
		return keyFieldNames;

	}
}
