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
	public static Map<Field, RecordField> getFieldToRecordFieldAnnotationsMap(DataRecord record) {
		Map<Field, RecordField> fieldNameToRecordFieldAnnotationMap = new HashMap<Field, RecordField>();
		Set<Field> fields = PrivateAccessor.getAllFields(record.getClass(), new HashSet<Field>());
		for (Field field : fields) {
			if (field.isAnnotationPresent(RecordField.class)) {
				fieldNameToRecordFieldAnnotationMap.put(field, field.getAnnotation(RecordField.class));
			}
		}
		return fieldNameToRecordFieldAnnotationMap;
	}

	public static Record getRecordAnnotation(Class<? extends DataRecord> recordClass) {
		if (recordClass.isAnnotationPresent(Record.class)) {
			return recordClass.getAnnotation(Record.class);
		}
		throw new IllegalArgumentException("Detected missing Record annotation. Please add a Record annotation to the "
				+ recordClass.getName() + " class.");
	}

	/**
	 * @param recordClass
	 * @return
	 */
	public static String getRecordSchemaVersion(Class<? extends DataRecord> recordClass) {
		return getRecordAnnotation(recordClass).schemaVersion();
	}

	/**
	 * @param recordClass
	 * @param name
	 * @return
	 */
	public static String getRecordFieldVersion(Class<?> recordClass, String fieldName) {
		Set<Field> fields = PrivateAccessor.getAllFields(recordClass, new HashSet<Field>());
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				if (field.isAnnotationPresent(RecordField.class)) {
					return field.getAnnotation(RecordField.class).version();
				}
				throw new IllegalArgumentException(
						"Detected missing RecordField annotation. Please add a RecordField annotation to the '"
								+ fieldName + "' field in the " + recordClass.getName() + " class.");
			}
		}
		throw new IllegalArgumentException("The '" + fieldName + "' field does not exist in class: "
				+ recordClass.getName());
	}

	private RecordUtil() {
		// this class should not be instantiated
	}
}
