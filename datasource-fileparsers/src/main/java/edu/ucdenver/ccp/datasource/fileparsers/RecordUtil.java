/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.ucdenver.ccp.common.reflection.PrivateAccessor;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * A utility class for handling {@link Record} and {@link RecordField} annotations
 * 
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RecordUtil {

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
}
