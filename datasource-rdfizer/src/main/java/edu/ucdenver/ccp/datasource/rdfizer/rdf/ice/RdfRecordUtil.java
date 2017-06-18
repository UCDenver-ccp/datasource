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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;

import edu.ucdenver.ccp.common.reflection.PrivateAccessor;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.filter.DuplicateStatementFilter;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.RO;

/**
 * Static utility functions for creating RDF
 * 
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 */
public class RdfRecordUtil {

	// private static final Logger logger =
	// Logger.getLogger(RdfRecordUtil.class);

	// /**
	// *
	// *
	// * @param recordClass
	// * @return Collection of created statements
	// */
	// public static Collection<? extends Statement>
	// getRecordSchemaStatements(Class<?> recordClass)
	// {
	// Collection<Statement> stmts = new LinkedHashSet<Statement>();
	// RdfNamespace ns =
	// RdfNamespace.getNamespace(RecordUtil.getRecordDataSource(recordClass));
	// String recordComment = RecordUtil.getRecordComment(recordClass);
	// String recordVersion = RecordUtil.getRecordSchemaVersion(recordClass);
	//
	// URIImpl recordClsUri = RdfUtil.createKiaoUri(ns,
	// recordClass.getSimpleName());
	// stmts.add(new StatementImpl(recordClsUri, RDFS.SUBCLASS_OF.uri(),
	// IAO.INFORMATION_CONTENT_ENITITY.uri()));
	// if (recordComment != null && !recordComment.isEmpty()) {
	// stmts.add(new StatementImpl(recordClsUri, RDFS.COMMENT.uri(),
	// RdfUtil.createLiteral(recordComment)));
	// }
	// stmts.add(new StatementImpl(recordClsUri, DC.HAS_VERSION.uri(),
	// RdfUtil.createLiteral(recordVersion)));
	//
	// Map<Field, RecordField> fieldToRecordFieldAnnotationMap = RecordUtil
	// .getFieldToRecordFieldAnnotationsMap(recordClass);
	// for (Entry<Field, RecordField> entry :
	// fieldToRecordFieldAnnotationMap.entrySet()) {
	// if (isFieldSubRecord(entry.getKey())) {
	// Field f = entry.getKey();
	// if (Collection.class.isAssignableFrom(f.getType())) {
	// Type gt = f.getGenericType();
	// if (gt instanceof ParameterizedType) {
	// Type[] genericTypes = ((ParameterizedType) gt).getActualTypeArguments();
	// if (genericTypes.length > 0 && (genericTypes[0] instanceof Class)) {
	// stmts.addAll(getRecordSchemaStatements((Class<?>) genericTypes[0]));
	// }
	// } else {
	// throw new
	// IllegalStateException("Non-parameterized collection detected in record
	// class: "
	// + recordClass.getName() + " Please parameterize.");
	// }
	// } else {
	// stmts.addAll(getRecordSchemaStatements(f.getType()));
	// }
	// }
	// String fieldName = entry.getKey().getName();
	// String fieldComment = RecordUtil.getRecordFieldComment(recordClass,
	// fieldName);
	// String fieldVersion = RecordUtil.getRecordFieldVersion(recordClass,
	// fieldName);
	// boolean isKeyField = RecordUtil.isKeyRecordField(recordClass, fieldName);
	//
	// URIImpl fieldTemplateUri =
	// RdfRecordUriFactory.createDataFieldTemplateUri(recordClass,
	// fieldName,
	// IncludeVersion.YES);
	// stmts.add(new StatementImpl(recordClsUri, RO.HAS_PART.uri(),
	// fieldTemplateUri));
	// if (fieldComment != null && !fieldComment.isEmpty()) {
	// stmts.add(new StatementImpl(fieldTemplateUri, RDFS.COMMENT.uri(),
	// RdfUtil.createLiteral(fieldComment)));
	// }
	// stmts.add(new StatementImpl(fieldTemplateUri, DC.HAS_VERSION.uri(),
	// RdfUtil.createLiteral(fieldVersion)));
	//
	// if (isKeyField) {
	// //stmts.add(new StatementImpl(fieldTemplateUri, DC.IDENTIFIER.uri(),
	// RdfUtil.createLiteral(isKeyField)));
	// stmts.add(new StatementImpl(recordClsUri, KIAO.HAS_KEY_FIELD.uri(),
	// fieldTemplateUri));
	// }
	// }
	//
	// return stmts;
	// }

	// /**
	// * Generate statements about datasets, records and their types for
	// specified namespace within
	// * KABOB namespace. Each class represents a dataset made up of its class
	// of records and their
	// * fields.
	// *
	// * @param recordTypes
	// * @param ns
	// * target namespace
	// * @return statements
	// */
	// public static List<? extends Statement>
	// getRecordSchemaDefinitionStatements(Class<? extends
	// DataRecord> recordClass) {
	// List<Statement> statements = new ArrayList<Statement>();
	//
	// RdfNamespace ns =
	// RdfNamespace.getNamespace(RecordUtil.getRecordDataSource(recordClass));
	//
	// URIImpl dataSourceUri = RdfUtil.createKiaoUri(ns, ns.lowerName() +
	// KIAO.KABOB_DATASOURCE.termName());
	// URIImpl dsSchemaUri = RdfUtil.createKiaoUri(ns, ns.lowerName() +
	// KIAO.KABOB_SCHEMA.termName());
	//
	// // schema subclass
	// statements.add(new StatementImpl(dsSchemaUri, RDFS.SUBCLASS_OF.uri(),
	// KIAO.KABOB_SCHEMA.uri()));
	//
	// // datasource subclass
	// statements.add(new StatementImpl(dataSourceUri, RDFS.SUBCLASS_OF.uri(),
	// KIAO.KABOB_DATASOURCE.uri()));
	//
	// // record subclass
	// URIImpl recordUri = RdfUtil.createKiaoUri(ns, ns.lowerName() +
	// KIAO.KABOB_DATARECORD.termName());
	// statements.add(new StatementImpl(recordUri, RDFS.SUBCLASS_OF.uri(),
	// KIAO.KABOB_DATARECORD.uri()));
	//
	// // field subclass
	// URIImpl fieldUri = RdfUtil.createKiaoUri(ns, ns.lowerName() +
	// KIAO.KABOB_DATAFIELD.termName());
	// statements.add(new StatementImpl(fieldUri, RDFS.SUBCLASS_OF.uri(),
	// KIAO.KABOB_DATAFIELD.uri()));
	//
	// statements.addAll(getRecordFieldDeclarationStatements(recordClass,
	// null));
	//
	// return statements;
	// }

	// /**
	// * Generate statements about class' fields specified namespace within
	// KABOB
	// * namespace. Each field is a subclass of generic field in namespace and
	// * part of dataset.
	// *
	// *
	// * @param recordClass
	// * source of fields
	// * @param ns
	// * target namespace
	// * @param version
	// * structural version label
	// * @param parentSchemaUri
	// * if not null, record schema is asserted to be
	// * {@link RdfPredicate#RO_PARTOF} parent schema.
	// * @param fieldComment
	// * the field comment is used to capture @RecordField comments on
	// * fields that are subrecords
	// * @param isKeyField
	// * @return statements about fields; empty result is returned for anonymous
	// * classes.
	// */
	// public static Collection<? extends Statement>
	// getRecordSchemaStatements(Class<?> recordClass,
	// URIImpl parentSchemaUri, String fieldComment, boolean isKeyField) {
	// DataSource ns =
	// DataSource.getNamespace(RecordUtil.getRecordDataSource(recordClass));
	// Collection<Statement> statements = new ArrayList<Statement>();
	//
	// /*
	// * The following two statements are meta statements that will be
	// * redundant if multiple record schemas are combined. Note that the
	// * first statement is supposed to be a self-loop.
	// */
	// statements.add(new StatementImpl(KIAO.SCHEMA.uri(), RO.HAS_PART.uri(),
	// KIAO.SCHEMA.uri()));
	// statements.add(new StatementImpl(KIAO.SCHEMA.uri(), RO.HAS_PART.uri(),
	// KIAO.FIELD.uri()));
	//
	// /*
	// * The following adds the kiaosource:Record rdfs:subClassOf
	// * iao:IAO_0000030 (information content entity) triple. This triple is
	// * not really part of the schema, however it only needs to be added one
	// * time so this seems like a good place to put it.
	// */
	// URIImpl recordClsUri = RdfUtil.createCcpUri(ns,
	// recordClass.getSimpleName());
	// statements.add(new StatementImpl(recordClsUri, RDFS.SUBCLASSOF,
	// IAO.INFORMATION_CONTENT_ENTITY.uri()));
	//
	// Set<Field> fields = PrivateAccessor.getAllFields(recordClass, new
	// HashSet<Field>());
	// List<Field> sortedFields = new ArrayList<Field>(fields);
	// Collections.sort(sortedFields, new FieldNameComparator());
	//
	// URIImpl recordSchemaUri =
	// RdfRecordUriFactory.createRecordSchemaUri(recordClass,
	// IncludeVersion.YES);
	// URIImpl recordSchemaTypeUri =
	// RdfRecordUriFactory.createRecordSchemaUri(recordClass,
	// IncludeVersion.NO);
	//
	// // record schema type data-source schema
	// statements.add(new StatementImpl(recordSchemaUri, RDF.TYPE,
	// recordSchemaTypeUri));
	// if (fieldComment != null && !fieldComment.isEmpty()) {
	// fieldComment = "FIELD_COMMENT: " + fieldComment;
	// statements.add(new StatementImpl(recordSchemaUri, RDFS.COMMENT,
	// RdfUtil.createLiteral(fieldComment)));
	// }
	//
	// String recordComment = RecordUtil.getRecordComment(recordClass);
	// String recordVersion = RecordUtil.getRecordSchemaVersion(recordClass);
	// String recordLabel = getRecordLabel(recordClass);
	//
	// if (recordComment != null && !recordComment.isEmpty()) {
	// if (fieldComment != null && !fieldComment.isEmpty()) {
	// recordComment = "RECORD_COMMENT: " + recordComment;
	// }
	// statements
	// .add(new StatementImpl(recordSchemaUri, RDFS.COMMENT,
	// RdfUtil.createLiteral(recordComment)));
	// }
	// statements.add(new StatementImpl(recordSchemaUri, DCTERMS.HAS_VERSION,
	// RdfUtil.createLiteral(recordVersion)));
	// statements.add(new StatementImpl(recordSchemaUri, RDFS.LABEL,
	// RdfUtil.createLiteral(recordLabel)));
	// statements.add(new StatementImpl(recordSchemaTypeUri, RDFS.SUBCLASSOF,
	// KIAO.SCHEMA.uri()));
	//
	// // record schema part-of parent schema
	// if (parentSchemaUri != null) {
	// statements.add(new StatementImpl(parentSchemaUri, RO.HAS_PART.uri(),
	// recordSchemaUri));
	// if (isKeyField) {
	// statements.add(new StatementImpl(parentSchemaUri,
	// KIAO.HAS_KEY_PART.uri(), recordSchemaUri));
	// }
	// }
	//
	// for (Field field : sortedFields) {
	// /*
	// * If the RecordField annotation is not present, then this field
	// * does not get serialized in the RDF, e.g. the logger field
	// */
	// if (field.isAnnotationPresent(RecordField.class)) {
	// String fComment = RecordUtil.getRecordFieldComment(recordClass,
	// field.getName());
	// boolean fieldIsKey = RecordUtil.isKeyRecordField(recordClass,
	// field.getName());
	// if (isFieldSubRecord(field)) {
	// Class<?> fieldIsARecordClass = getFieldType(field);
	// statements.addAll(getRecordSchemaStatements(fieldIsARecordClass,
	// recordSchemaUri, fComment,
	// fieldIsKey));
	// } else {
	//
	// URIImpl fieldClassUri =
	// RdfRecordUriFactory.createDataFieldTemplateUri(recordClass,
	// field.getName(), IncludeVersion.NO);
	// URIImpl fieldUri =
	// RdfRecordUriFactory.createDataFieldTemplateUri(recordClass,
	// field.getName(),
	// IncludeVersion.YES);
	//
	// statements.add(new StatementImpl(fieldUri, RDF.TYPE, fieldClassUri));
	// statements.add(new StatementImpl(fieldClassUri, RDFS.SUBCLASSOF,
	// KIAO.FIELD.uri()));
	// statements.add(new StatementImpl(recordSchemaUri, RO.HAS_PART.uri(),
	// fieldUri));
	// String fieldVersion = RecordUtil.getRecordFieldVersion(recordClass,
	// field.getName());
	// String fieldLabel = getFieldLabel(recordClass, field.getName());
	//
	// if (fComment != null && !fComment.isEmpty()) {
	// statements
	// .add(new StatementImpl(fieldUri, RDFS.COMMENT,
	// RdfUtil.createLiteral(fComment)));
	// }
	// statements.add(new StatementImpl(fieldUri, DCTERMS.HAS_VERSION, RdfUtil
	// .createLiteral(fieldVersion)));
	//
	// statements.add(new StatementImpl(fieldUri, RDFS.LABEL,
	// RdfUtil.createLiteral(fieldLabel)));
	//
	// if (fieldIsKey) {
	// statements.add(new StatementImpl(recordSchemaUri,
	// KIAO.HAS_KEY_PART.uri(), fieldUri));
	// }
	//
	// }
	// }
	// }
	//
	// return statements;
	// }
	//
	// /**
	// * @param recordClass
	// * @param name
	// * @return
	// */
	// private static String getFieldLabel(Class<?> recordClass, String
	// fieldName) {
	// String label = RecordUtil.getRecordFieldLabel(recordClass, fieldName);
	// if (label == null || label.isEmpty()) {
	// label = fieldName.replaceAll("([a-z])([A-Z])", "$1 " +
	// "$2").toLowerCase();
	// }
	// return label;
	// }
	//
	// /**
	// * @param recordClass
	// * @return a label for the record by first looking for an explicitly
	// defined
	// * label in the @Record annotation. If not present, a label is
	// * generated by adding spaces to replace camel-case in the Record
	// * name
	// */
	// private static String getRecordLabel(Class<?> recordClass) {
	// String label = RecordUtil.getRecordLabel(recordClass);
	// if (label == null || label.isEmpty()) {
	// String recordName = recordClass.getSimpleName();
	// label = recordName.replaceAll("([a-z])([A-Z])", "$1 " +
	// "$2").toLowerCase();
	// // Pattern lowUpPattern = Pattern.compile("([a-z])([A-Z])");
	// // Matcher m = lowUpPattern.matcher(recordName);
	// // while
	// }
	// return label;
	// }
	//
	// /**
	// * Get field type handling {@link Collection}s with generics.
	// *
	// * @param field
	// * to check
	// * @return field type, or generic type if field's type is a
	// * {@link Collection}
	// */
	// private static Class<?> getFieldType(Field field) {
	// Class<?> klass = field.getType();
	//
	// if (Collection.class.isAssignableFrom(klass)) {
	// Type gt = field.getGenericType();
	// if (gt instanceof ParameterizedType) {
	// Type[] genericTypes = ((ParameterizedType) gt).getActualTypeArguments();
	// if (genericTypes.length > 0 && (genericTypes[0] instanceof Class)) {
	// return (Class<?>) genericTypes[0];
	// }
	// }
	// }
	//
	// return field.getType();
	// }

	/**
	 * Determine whether class should be treated as sub-record definition. If
	 * field type is collection, it's generic type is used.
	 * 
	 * @param field
	 *            to check
	 * @return true if sub-record; otherwise, false.
	 */
	private static boolean isFieldSubRecord(Field field) {
		Class<?> klass = field.getType();

		if (Collection.class.isAssignableFrom(klass)) {
			Type gt = field.getGenericType();
			if (gt instanceof ParameterizedType) {
				Type[] genericTypes = ((ParameterizedType) gt).getActualTypeArguments();
				if (genericTypes.length > 0 && (genericTypes[0] instanceof Class)) {
					return isFieldSubRecord((Class<?>) genericTypes[0]);
				}
			}
		}

		return isFieldSubRecord(klass);
	}

	/**
	 * Determine whether class should be treated as sub-record definition.
	 * 
	 * @param klass
	 *            to check
	 * @return true if sub-record; otherwise, false.
	 */
	private static boolean isFieldSubRecord(Class<?> klass) {
		return klass.isAnnotationPresent(Record.class);
	}

	// /**
	// * Get collection of statements that instance datasource, records and
	// fields
	// * for given record.
	// *
	// * @param record
	// * @param src
	// * @return instantiation statements
	// */
	// public static List<? extends Statement>
	// getDataSourceInstanceStatements(DataRecord record, long createdTime) {
	// List<Statement> statements = new ArrayList<Statement>();
	// DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	// String yyyyMMdd = formatter.format(new Date(createdTime));
	//
	// DataSource ns =
	// DataSource.getNamespace(RecordUtil.getRecordDataSource(record.getClass()));
	//
	// URIImpl dataSourceClassUri = RdfUtil.createCcpUri(ns, ns.lowerName() +
	// KIAO.DATASOURCE.termName());
	// URIImpl dataSourceInstanceUri = RdfUtil.createCcpUri(ns,
	// ns.lowerName() + KIAO.DATASOURCE.termName() + yyyyMMdd);
	//
	// // datasource instance type of datasource class
	// statements.add(new StatementImpl(dataSourceInstanceUri, RDF.TYPE,
	// dataSourceClassUri));
	//
	// URIImpl dataSetInstanceUri = getDataSetInstanceUri(record, createdTime);
	// URIImpl recordSchemaUri = RdfUtil.createCcpUri(ns, ns.lowerName() +
	// record.getClass().getSimpleName()
	// + KIAO.SCHEMA.termName() +
	// RecordUtil.getRecordSchemaVersion(record.getClass()));
	//
	// // dataset instance has-template record schema
	// statements.add(new StatementImpl(dataSetInstanceUri,
	// KIAO.HAS_TEMPLATE.uri(), recordSchemaUri));
	//
	// // dataset instance part of datasource instance
	// statements.add(new StatementImpl(dataSourceInstanceUri,
	// RO.HAS_PART.uri(), dataSetInstanceUri));
	//
	// // dataset instance type of dataset class
	// statements.add(new StatementImpl(dataSetInstanceUri, RDF.TYPE,
	// KIAO.DATASET.uri()));
	//
	// // dataset has creation date
	// statements.add(RdfUtil.getCreationTimeStampStatement(dataSetInstanceUri,
	// createdTime));
	//
	// return statements;
	// }

	public static List<? extends Statement> getRecordSetInstanceStatements(DataRecord record, String readerKey,
			long createdTime) {
		List<Statement> statements = new ArrayList<Statement>();
		URIImpl recordSetInstanceUri = getRecordSetInstanceUri(record, readerKey, createdTime);
		statements.add(
				new StatementImpl(recordSetInstanceUri, RDF.TYPE, RdfUtil.getUri(CcpExtensionOntology.RECORD_SET)));
		statements.add(RdfUtil.getCreationTimeStampStatement(recordSetInstanceUri, createdTime));
		return statements;
	}

	public static URIImpl getRecordSetInstanceUri(DataRecord record, String readerKey, long createdTime) {
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String yyyyMMdd = formatter.format(new Date(createdTime));
		DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(record.getClass()));
		return RdfUtil.createUriImpl(DataSource.CCP,
				"RS_" + ns.name() + "_" + ((readerKey != null && !readerKey.isEmpty())
						? readerKey.replaceAll(" ", "_").toUpperCase() + "_" : "") + yyyyMMdd);
	}

	/**
	 * Generate instance statements about this particular instance of
	 * {@link DataRecord}. Statements include assertions about record and it's
	 * fields types and values. All record fields are included.
	 * 
	 * @param record
	 *            instance
	 * @param filter
	 * @param src
	 *            record source
	 * @param alreadyObservedFieldUris
	 * @param recordInstance
	 *            record instance index
	 * @return statements
	 */
	public static List<Statement> getRecordInstanceStatements(DataRecord record, long createdTime, URIImpl recordUri,
			DuplicateStatementFilter filter) {
		return getRecordInstanceStatements(record, createdTime, recordUri, null, StringConstants.BLANK, filter);
	}

	/**
	 * Generate instance statements about this particular instance of
	 * {@link DataRecord}. Statements include assertions about record and it's
	 * fields types and values. {@code rdfFields} will be used to determine
	 * record exclusion rules and output format.
	 * 
	 * @param record
	 *            instance
	 * @param src
	 *            record source
	 * @param recordInstance
	 *            record instance index
	 * @param rdfFields
	 *            configuration info for field export
	 * @param parentRecordUri
	 *            if not null, used to indicate that record is a subrecord
	 *            within record described by this value
	 * @param readerKey
	 *            label used in generating dataset instance URI; if null,
	 *            converted to {@link StringConstants#BLANK}
	 * @param alreadyObservedFieldUris
	 * @return statements ; empty result is returned for anonymous
	 *         {@code record} class.
	 */
	public static List<Statement> getRecordInstanceStatements(Object record, long createdTime, URIImpl recordUri,
			URIImpl parentRecordUri, String readerKey, DuplicateStatementFilter filter) {
		List<Statement> statements = new ArrayList<Statement>();
		readerKey = readerKey == null ? StringConstants.BLANK : readerKey;

		if (record.getClass().isAnonymousClass()) {
			Logger.getLogger(RdfUtil.class)
					.warn(String.format("Skipping anonymous record %s ; recordInstance=%s; parentRecordUri = %s",
							record, recordUri, parentRecordUri));
			return statements;
		}

		boolean isSubRecord = parentRecordUri != null;

		if (isSubRecord) {
			// record part of parent record
			statements.add(new StatementImpl(parentRecordUri, RO.HAS_PART.uri(), recordUri));
		} else {
			// record part of dataset
			URIImpl recordSetInstanceUri = getRecordSetInstanceUri((DataRecord) record, readerKey, createdTime);
			statements.add(new StatementImpl(recordSetInstanceUri, RO.HAS_PART.uri(), recordUri));
		}

		// record instance type record class
		URIImpl recordClassUri = RdfRecordUriFactory.getRecordTypeUri(record.getClass());
		statements.add(new StatementImpl(recordUri, RDF.TYPE, recordClassUri));

		Set<Field> fields = RecordUtil.getFieldToRecordFieldAnnotationsMap(record.getClass()).keySet();
		List<Field> sortedFields = new ArrayList<Field>(fields);
		Collections.sort(sortedFields, new FieldNameComparator());

		for (Field field : sortedFields) {
			if (isFieldSubRecord(field)) {
				Object subRecord = null;
				try {
					subRecord = field.get(record);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}

				if (subRecord == null) {
					continue;
				}

				if (subRecord instanceof Collection) {
					for (Object subrecord : (Collection<?>) subRecord) {
						statements.addAll(getSubrecordStatements(field, createdTime, recordUri, readerKey, filter,
								subrecord, record));
					}
				} else {
					statements.addAll(getSubrecordStatements(field, createdTime, recordUri, readerKey, filter,
							subRecord, record));
				}
			} else {
				Collection<Statement> fieldValueStmts = getRdfFieldValueStatements(recordUri, record, field,
						createdTime, filter);
				if (fieldValueStmts.isEmpty()) {
					continue;
				}

				statements.addAll(fieldValueStmts);
			}

		}

		return statements;
	}

	/**
	 * @param createdTime
	 * @param recordUri
	 * @param readerKey
	 * @param filter
	 * @param statements
	 * @param subRecord
	 */
	private static List<Statement> getSubrecordStatements(Field field, long createdTime, URIImpl recordUri,
			String readerKey, DuplicateStatementFilter filter, Object subRecord, Object parentRecord) {
		List<Statement> statements = new ArrayList<Statement>();
		URIImpl subRecordUri = RdfRecordUriFactory.createRecordUri(subRecord);
		List<Statement> subRecordStmts = getRecordInstanceStatements(subRecord, createdTime, subRecordUri, recordUri,
				readerKey, filter);
		if (!filter.alreadyObservedRecordUri(subRecordUri)) {
			statements.addAll(subRecordStmts);
			filter.logRecordUri(subRecordUri);
		} else {
			// logger.info("already seen subrecord");
			statements.add(subRecordStmts.get(0));
		}
		statements.add(new StatementImpl(subRecordUri, RDF.TYPE,
				RdfUtil.getUri(RecordUtil.getRecordFieldType(parentRecord.getClass(), field.getName()))));
		return statements;
	}

	// /**
	// * @param record
	// * @param recordUri
	// * @param fieldUri
	// * @param fieldName
	// * @return
	// */
	// private static Collection<Statement> createCommonFieldStatements(Object
	// record, URIImpl recordUri, URIImpl fieldUri,
	// String fieldName) {
	// Collection<Statement> commonFieldStatements = new ArrayList<Statement>();
	//
	// // field instance has template field schema
	// // URIImpl fieldSchemaUri =
	// // RdfRecordUriFactory.createDataFieldTemplateUri(record.getClass(),
	// // fieldName,
	// // IncludeVersion.YES);
	// // commonFieldStatements.add(new StatementImpl(fieldUri,
	// // KIAO.HAS_TEMPLATE.uri(), fieldSchemaUri));
	//
	// // field instance type kiao:FieldValue
	// commonFieldStatements.add(new StatementImpl(fieldUri, RDF.TYPE,
	// RdfUtil.getUri(RecordUtil.getRecordFieldType(record.getClass(),
	// fieldName))));
	//
	// return commonFieldStatements;
	// }
	//
	// /**
	// * @param recordUri
	// * @param fieldUri
	// * @param commonFieldStatements
	// */
	// private static Collection<Statement> linkFieldToRecord(URIImpl recordUri,
	// URIImpl fieldUri) {
	// Collection<Statement> commonFieldStatements = new ArrayList<Statement>();
	// // field instance part of record instance
	// commonFieldStatements.add(new StatementImpl(recordUri, RO.HAS_PART.uri(),
	// fieldUri));
	// return commonFieldStatements;
	// }

	/**
	 * Generate statements about record's field.
	 * 
	 * @param fieldInstanceUri
	 *            initial field instance URI; template re-used if field type is
	 *            a {@link Collection}
	 * @param record
	 *            instance with specified field
	 * @param filter
	 * @param fieldName
	 *            field name
	 * @param commonFieldStatements
	 *            shared template statements to be asserted about every field
	 * @return statements
	 */
	private static Collection<Statement> getRdfFieldValueStatements(URIImpl recordUri, Object record, Field field,
			long createdTime, DuplicateStatementFilter filter) {
		Object fieldValue = PrivateAccessor.getFieldValue(record, field.getName());
		if (fieldValue == null) {
			return new ArrayList<Statement>();
		}
		Collection<Statement> statements = new ArrayList<Statement>();

		if (!(fieldValue instanceof Collection)) {
			URIImpl fieldUri = RdfRecordUriFactory.createFieldUri(record, field, fieldValue);
			statements.add(new StatementImpl(recordUri, RO.HAS_PART.uri(), fieldUri));
			statements.add(new StatementImpl(fieldUri, RDF.TYPE,
					RdfUtil.getUri(RecordUtil.getRecordFieldType(record.getClass(), field.getName()))));
			statements.addAll(getFieldDenotesValueStatement(fieldUri, fieldValue, createdTime, filter));
		} else {
			/*
			 * for each element in the collection a new fieldInstanceUri is
			 * generated
			 */
			Collection<?> coll = (Collection<?>) fieldValue;
			for (Object object : coll) {
				URIImpl fieldUri = RdfRecordUriFactory.createFieldUri(record, field, object);
				if (fieldUri != null) {
					statements.add(new StatementImpl(recordUri, RO.HAS_PART.uri(), fieldUri));
					statements.add(new StatementImpl(fieldUri, RDF.TYPE,
							RdfUtil.getUri(RecordUtil.getRecordFieldType(record.getClass(), field.getName()))));
					statements.addAll(getFieldDenotesValueStatement(fieldUri, object, createdTime, filter));
				}
			}
		}

		return statements;
	}

	/**
	 * Generate statements about field (represented by {@code fieldInstanceUri},
	 * and also a Subject in RDF statement) and field's value. Statements
	 * generated:<br>
	 * 
	 * <pre>
	 * <http://www.ncbi.nlm.nih.gov/gene/F_RdfRecordWriterImplTest%24GeneId2NameDatFileData_geneID_bZWMJYAy_y1wpq1BHpoB2OFoLlc> <http://purl.obolibrary.org/obo/IAO_0000219> <http://kabob.ucdenver.edu/iao/eg/EG_111_ICE> .
	 * </pre>
	 * 
	 * @param fieldInstanceUri
	 *            rdf field instance URI (subject)
	 * @param fieldValue
	 *            value
	 * @param filter
	 * @throws IllegalArgumentException
	 *             if fieldValue's type is {@link Collection}
	 * @return statements
	 */
	public static List<Statement> getFieldDenotesValueStatement(URIImpl fieldInstanceUri, Object fieldValue,
			long createdTime, DuplicateStatementFilter filter) {
		if (fieldValue instanceof Collection) {
			throw new IllegalArgumentException("Collection fieldValue is not supported");
		}

		List<Statement> stmts = new ArrayList<Statement>();
		/*
		 * if we encounter a data source identifier that is declared either
		 * unknown or a probable error, we create a record to hold the
		 * identifier and optional data source string. The field then denotes
		 * this new record. Unknown data source identifiers occur when the file
		 * parsing code comes across an identifier for which it does not know
		 * how to generate an appropriate URI. Perhaps "unknown" is not the
		 * prefix to use here. Probably erroneous identifiers are identifiers
		 * that the parsing code has detected to be incorrect, e.g. an UniProt
		 * identifier that does not follow the regular expression pattern
		 * stipulated by UniProt.
		 */
		if (fieldValue instanceof UnknownDataSourceIdentifier) {
			UnknownDataSourceIdentifier id = (UnknownDataSourceIdentifier) fieldValue;
			LiteralImpl literalValue = RdfUtil.createLiteral(id.getId());
			stmts.add(new StatementImpl(fieldInstanceUri, RDF.TYPE, RdfUtil.getUri(CcpExtensionOntology.IDENTIFIER_OF_UNKNOWN_ORIGIN)));
			stmts.add(new StatementImpl(fieldInstanceUri, RDFS.LABEL, literalValue));
		} else if (fieldValue instanceof ProbableErrorDataSourceIdentifier) {
			ProbableErrorDataSourceIdentifier id = (ProbableErrorDataSourceIdentifier) fieldValue;
			LiteralImpl literalValue = RdfUtil.createLiteral(id.getId());
			stmts.add(new StatementImpl(fieldInstanceUri, RDF.TYPE, RdfUtil.getUri(CcpExtensionOntology.INVALID_IDENTIFIER)));
			CcpExtensionOntology identifierType = RdfIdentifierUtil.getIdentifierType(id.getClass());
			stmts.add(new StatementImpl(fieldInstanceUri, RDF.TYPE, RdfUtil.getUri(identifierType)));
			stmts.add(new StatementImpl(fieldInstanceUri, RDFS.LABEL, literalValue));
			if (id.getErrorMessage()!= null && !id.getErrorMessage().isEmpty()) {
				stmts.add(new StatementImpl(fieldInstanceUri, RDFS.COMMENT, RdfUtil.createLiteral(id.getErrorMessage())));
			}
		} else if (fieldValue instanceof DataSourceIdentifier) {
			Value value = RdfUtil.getValue(fieldValue);
			DataSourceIdentifier<?> id = (DataSourceIdentifier<?>) fieldValue;
			LiteralImpl literalValue = RdfUtil.createLiteral(id.getId());
			stmts.add(new StatementImpl(fieldInstanceUri, RDF.TYPE, value));
			stmts.add(new StatementImpl(fieldInstanceUri, RDFS.LABEL, literalValue));
			URIImpl idUri = RdfUtil.createCcpUri(id);
			CcpExtensionOntology identifierType = RdfIdentifierUtil.getIdentifierType(id.getClass());
			stmts.add(new StatementImpl(idUri, RDFS.SUBCLASSOF, RdfUtil.getUri(identifierType)));
		} else {
			Value value = RdfUtil.getValue(fieldValue);
			stmts.add(new StatementImpl(fieldInstanceUri, RDFS.LABEL, value));
		}
		return stmts;

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

	/**
	 * private constructor to prevent instantiation
	 */
	private RdfRecordUtil() {
		// utility class - not meant for instantiation
	}
}
