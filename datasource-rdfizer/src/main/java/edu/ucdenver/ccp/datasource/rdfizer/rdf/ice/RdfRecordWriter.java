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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.DC;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;

import edu.ucdenver.ccp.common.download.DownloadMetadata;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.RecordUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.filter.DuplicateStatementFilter;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.filter.InMemoryDuplicateStatementFilter;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.ice.RdfUtil.RdfFormat;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.IAO;

/**
 * RDF generator that handles data provided by {@link RecordReader} instances in
 * combination with reader-specific config files (XML).
 * 
 * @param <T>
 *            reader type
 */
public class RdfRecordWriter<T extends RecordReader<?>> {

	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(RdfRecordWriter.class);

	/** output directory */
	private final File outputDirectory;

	/** file key (specified in config parser config files) to writer map */
	private Map<File, RdfWriterResource> file2RdfWriterMap;

	/**
	 * Part of output file name driven by reader.
	 * <p>
	 * This is useful in places where the same reader class is used to read
	 * several files (ex: multiple species).
	 */
	private String readerKey = StringConstants.BLANK;

	private long createdTime;

	/** indicator for whether to compress serialized rdf files */
	private boolean compress;

	/**
	 * maximum number of statements per output file ; -1 or 0 for unlimited -
	 * default
	 */
	private long maxStatementsPerFile = -1;

	/**
	 * tracks the number of statements written to file. This count is used to
	 * split files when the {@link #maxStatementsPerFile} variable is set to a
	 * number > 0
	 */
	private long writtenStatementCount;

	/**
	 * this number will be appended to the output file name to allow multiple
	 * stages to process a single file but output the RDF into distinct files.
	 */
	private final int batchNumber;

	/**
	 * The RDF format to use when writing out RDF
	 */
	private final RdfFormat rdfFormat;

	/**
	 * The default output encoding is UTF-8. This can be changed using the
	 * {@link #setEncoding(CharacterEncoding)} method.
	 */
	private CharacterEncoding encoding = CharacterEncoding.UTF_8;

	/**
	 * A reference to the {@link RecordReader} being processed is stored b/c it
	 * is used during the output file name creation process. See
	 * {@link #getOutputFileName()}
	 */
	private RecordReader<?> recordReader = null;

	/**
	 * Stores references to all RDF files generated during processing of a
	 * {@link RecordReader}
	 */
	private Collection<File> generatedRdfFiles = new ArrayList<File>();

	private final DuplicateStatementFilter filter;

	private final Set<String> rollingCacheSet = new HashSet<String>();
	private final LinkedList<String> rollingCacheList = new LinkedList<String>();

	private Set<String> primaryKeyFieldNames = null;
	private static final int ROLLING_CACHE_MAX_SIZE = 1000;

	/**
	 * @param outputDirectory
	 * @param rdfFormat
	 * @throws FileNotFoundException
	 */
	public RdfRecordWriter(File outputDirectory, RdfFormat rdfFormat) throws FileNotFoundException {
		this(outputDirectory, rdfFormat, 0, new InMemoryDuplicateStatementFilter());
	}

	/**
	 * @param outputDirectory
	 * @param rdfFormat
	 * @param batchNumber
	 * @throws FileNotFoundException
	 */
	public RdfRecordWriter(File outputDirectory, RdfFormat rdfFormat, int batchNumber, DuplicateStatementFilter filter)
			throws FileNotFoundException {
		this.batchNumber = batchNumber;
		this.outputDirectory = outputDirectory;
		this.rdfFormat = rdfFormat;
		this.filter = filter;

		if (!this.outputDirectory.exists() && !this.outputDirectory.mkdirs()) {
			throw new RuntimeException(
					String.format("Output directory %s does not exist and cannot be created", outputDirectory));
		}

		file2RdfWriterMap = new HashMap<File, RdfWriterResource>();
		writtenStatementCount = 0;
		generatedRdfFiles = new ArrayList<File>();
	}

	/**
	 * 
	 * @param outputDirectory
	 *            rdf output directory
	 * @param rdfFormat
	 * @param compress
	 *            whether to compress (gz) files or not
	 * @param maxStatementsPerFile
	 *            maximum number of statements per output file ; -1 for
	 *            unlimited
	 * @param batchNumber
	 * @throws FileNotFoundException
	 */
	public RdfRecordWriter(File outputDirectory, RdfFormat rdfFormat, boolean compress, long maxStatementsPerFile,
			int batchNumber, DuplicateStatementFilter filter) throws FileNotFoundException {
		this(outputDirectory, rdfFormat, batchNumber, filter);
		this.compress = compress;
		this.maxStatementsPerFile = maxStatementsPerFile > 0 ? maxStatementsPerFile : -1;
	}

	/**
	 * @param outputDirectory
	 * @param rdfFormat
	 * @param compress
	 * @throws FileNotFoundException
	 */
	public RdfRecordWriter(File outputDirectory, RdfFormat rdfFormat, boolean compress) throws FileNotFoundException {
		this(outputDirectory, rdfFormat, compress, -1, 0, new InMemoryDuplicateStatementFilter());
	}

	/**
	 * Process file data by converting each item to RDF format and writing it
	 * out to an RDF file.
	 * 
	 * @param <E>
	 * 
	 * @param recordReader
	 *            reader
	 * @param rdfSource
	 *            rdf source
	 * @throws IOException
	 */
	public <E extends DataRecord> void processRecordReader(RecordReader<E> recordReader, long createdTime,
			Set<DownloadMetadata> metadata) throws IOException {
		// -1 for the outputRecordLimit signifies that all records should be
		// output
		processRecordReader(recordReader, createdTime, 0, -1, metadata);
	}

	/**
	 * Process file data by converting each item to RDF format and writing it
	 * out to an RDF file.
	 * 
	 * @param dataRecordIterator
	 * @param dataSpecificFileSuffix
	 * @param outputRecordLimit
	 * @return
	 * @throws IOException
	 */
	private <E extends DataRecord> Collection<File> processRecordReader(RecordReader<E> dataRecordIterator,
			String dataSpecificFileSuffix, long createdTime, long outputRecordLimit, Set<DownloadMetadata> metadata)
			throws IOException {
		setReaderKey(dataSpecificFileSuffix);
		return processRecordReader(dataRecordIterator, createdTime, 0, outputRecordLimit, metadata);
	}

	/**
	 * Process file data by converting each item to RDF format and writing it
	 * out to an RDF file.
	 * 
	 * @param <E>
	 * 
	 * @param recordReader
	 *            reader
	 * @param rdfSource
	 *            data source
	 * @param recordSkipCount
	 *            the number of records to skip before processing
	 * @param outputRecordLimit
	 *            max records to process; -1 for all
	 * @return a collection of references to all generated RDF files
	 * @throws IOException
	 */
	public <E extends DataRecord> Collection<File> processRecordReader(RecordReader<E> recordReader, long createdTime,
			long recordSkipCount, long outputRecordLimit, Set<DownloadMetadata> metadata) throws IOException {

		this.recordReader = recordReader;

		setCreatedTime(createdTime);
		setReaderKey(recordReader.getDataSpecificKey());

		long instanceCount = 0;
		/* skip the recordSkipCount */
		while (recordReader.hasNext() && instanceCount < recordSkipCount) {
			DataRecord record = recordReader.next();
			if (record != null) {
				if (instanceCount % 100000 == 0) {
					logger.info("SKIP PROGRESS: " + instanceCount);
				}
				instanceCount++;
			}
		}
		while (recordReader.hasNext()) {
			DataRecord record = recordReader.next();
			if (record != null) {
				if ((instanceCount - recordSkipCount) % 100000 == 0) {
					logger.info("RDF GENERATION PROGRESS: " + (instanceCount - recordSkipCount));
				}
				instanceCount++;
				if (instanceCount == 1) {
					primaryKeyFieldNames = RecordUtil.getKeyFieldNames(record.getClass());
					DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(record.getClass()));
					writeDataSourceInstanceStatements(
							RdfRecordUtil.getRecordSetInstanceStatements(record, readerKey, createdTime), ns);
					writeDownloadMetadataStatements(metadata,
							RdfRecordUtil.getRecordSetInstanceUri(record, readerKey, createdTime), ns);
				}

				URIImpl recordUri = RdfRecordUriFactory.createRecordUri(record);
				processRecord(record, recordReader.getDataSpecificKey(), recordUri);
			}
			/*
			 * if the outputRecordLimit is > -1 then cap the number or records
			 * that are output to file
			 */
			if (outputRecordLimit > -1 && instanceCount >= (outputRecordLimit + recordSkipCount)) {
				break;
			}
		}
		closeFiles();
		filter.shutdown();
		logger.info("DUPLICATE TRIPLE FILTER WAS LEAK-PROOF = " + filter.isLeakProof());
		return generatedRdfFiles;
	}

	private void writeDownloadMetadataStatements(Set<DownloadMetadata> metadata, URIImpl dataSetInstanceUri,
			DataSource ns) throws IOException {
		for (DownloadMetadata md : metadata) {
			Collection<Statement> stmts = getDownloadMetadataStatements(md, dataSetInstanceUri);
			for (Statement s : stmts) {
				write(s, ns);
			}
		}
	}

	/**
	 * @param dmd
	 * @return a unique (reproducible) URI to represent the DownloadMetadata
	 */
	static URIImpl computeDownloadMetadataUri(DownloadMetadata dmd) {
		String s = dmd.getDownloadDate().getTimeInMillis() + dmd.getDownloadedFile().getName()
				+ ((dmd.getDownloadUrl() == null) ? "" : dmd.getDownloadUrl().toString()) + dmd.getFileSizeInBytes();

		String digest = DigestUtils.sha256Hex(s);
		return new URIImpl("http://ccp.ucdenver.edu/obo/ext/S_" + digest);
	}

	private Collection<Statement> getDownloadMetadataStatements(DownloadMetadata dmd, URIImpl dataSetInstanceUri)
			throws IOException {
		List<Statement> statements = new ArrayList<Statement>();
		URIImpl metadataUri = computeDownloadMetadataUri(dmd);

		statements.add(new StatementImpl(dataSetInstanceUri, DC.SOURCE, metadataUri));
		statements.add(new StatementImpl(metadataUri, RDF.TYPE, IAO.DOCUMENT.uri()));
		statements.add(new StatementImpl(metadataUri, new URIImpl(CcpExtensionOntology.DOWNLOAD_DATE.uri().toString()),
				RdfUtil.getDateLiteral(dmd.getDownloadDate().getTimeInMillis())));
		statements.add(
				new StatementImpl(metadataUri, new URIImpl(CcpExtensionOntology.LAST_MODIFIED_DATE.uri().toString()),
						RdfUtil.getDateLiteral(dmd.getFileLastModifiedDate().getTimeInMillis())));
		statements
				.add(new StatementImpl(metadataUri, new URIImpl(CcpExtensionOntology.FILE_SIZE_BYTES.uri().toString()),
						RdfUtil.createLiteral(dmd.getFileSizeInBytes())));
		if (dmd.getDownloadUrl() != null) {
			try {
				statements.add(new StatementImpl(metadataUri, DC.SOURCE,
						new URIImpl(dmd.getDownloadUrl().toURI().toString())));
			} catch (URISyntaxException e) {
				throw new IOException(e);
			}
		}

		return statements;
	}

	// /**
	// * @param recordClass
	// * @return a reference to the schema definition RDF file
	// */
	// private void writeSchemaDefinitionRdfFile(Class<? extends DataRecord>
	// recordClass) {
	// String schemaDefinitionFileName = recordClass.getSimpleName() +
	// ".schema." + rdfFormat.defaultFileExtension();
	// File schemaDefinitionRdfFile = new File(outputDirectory,
	// schemaDefinitionFileName);
	// Collection<? extends Statement> stmts =
	// RdfRecordUtil.getRecordSchemaStatements(recordClass, null, null, false);
	// RDFWriter rdfWriter = RdfUtil.openWriter(schemaDefinitionRdfFile,
	// encoding, rdfFormat);
	// for (Statement s : stmts) {
	// write(s, rdfWriter);
	// }
	// RdfUtil.closeWriter(rdfWriter);
	// generatedRdfFiles.add(schemaDefinitionRdfFile);
	// }

	// /**
	// * @param alreadyObservedFieldUris
	// * @param processedRecordsCount
	// * @return a mapping from field uris to observation counts
	// */
	// private static Map<String, ObservationCount> cullObservedFieldUris(
	// Map<String, ObservationCount> alreadyObservedFieldUris, long
	// processedRecordsCount) {
	// Map<String, ObservationCount> culledMap = new HashMap<String,
	// ObservationCount>();
	//
	// int beforeSize = alreadyObservedFieldUris.size();
	// for (Entry<String, ObservationCount> entry :
	// alreadyObservedFieldUris.entrySet()) {
	// ObservationCount oc = entry.getValue();
	// if (oc.getObservedCount() > 1) {
	// culledMap.put(entry.getKey(), entry.getValue());
	// }
	// if ((processedRecordsCount - oc.getLastObserved()) < 10000) {
	// culledMap.put(entry.getKey(), entry.getValue());
	// }
	// }
	// int afterSize = culledMap.size();
	// logger.info("Culled already observed field uris. Size before = " +
	// beforeSize +
	// " Size after = " + afterSize);
	// return culledMap;
	// }

	/**
	 * Set reader file suffix handling null and empty argument values
	 * 
	 * @param readerKey
	 *            to set ; if empty or null {@link #readerKey} is set to
	 *            {@link StringConstants#BLANK}.
	 */
	private void setReaderKey(String readerKey) {
		this.readerKey = readerKey == null || readerKey.trim().isEmpty() ? StringConstants.BLANK : readerKey;
	}

	/**
	 * Get reader-specifix file suffix
	 * 
	 * @return suffix
	 */
	private String getReaderKey() {
		return readerKey;
	}

	/**
	 * Write data source, and field instantiation statements that records and
	 * their fields will reference. These initialization statements are written
	 * out to every output file as specified in parser configuration file.
	 * 
	 * @param rdfFiles
	 * @param dataSourceInstanceStatements
	 */
	private void writeDataSourceInstanceStatements(Collection<? extends Statement> dataSourceInstanceStatements,
			DataSource ns) {
		for (Statement s : dataSourceInstanceStatements) {
			write(s, ns);
		}
	}

	/**
	 * Set rdf resource from which a lot of meta-data statements will be
	 * derived, including datasets, records and their fields.
	 * 
	 * @param rdfSource
	 */
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	// /**
	// * Process record reader
	// *
	// * @param <E>
	// * record type
	// * @param dataRecordIterator
	// * iterator
	// * @param rdfConfigFileStream
	// * input stream to config file iterator
	// * @return record
	// * @throws IOException
	// */
	// public <E extends DataRecord> void processRecordReader(RecordReader<E>
	// dataRecordIterator,
	// long createdTime) throws IOException {
	// processRecordReader(dataRecordIterator, StringConstants.BLANK,
	// createdTime, -1);
	// }

	/**
	 * Process record reader
	 * 
	 * @param <E>
	 *            record type
	 * @param dataRecordIterator
	 *            iterator
	 * @param rdfConfigFileStream
	 *            input stream to config file iterator
	 * @param outputRecordLimit
	 *            max number of records to process; -1 for all
	 * @return record
	 * @throws IOException
	 */
	public <E extends DataRecord> void processRecordReader(RecordReader<E> dataRecordIterator, long createdTime,
			long outputRecordLimit, Set<DownloadMetadata> metadata) throws IOException {
		processRecordReader(dataRecordIterator, StringConstants.BLANK, createdTime, outputRecordLimit, metadata);
	}

	/**
	 * Process file data by converting to RDF format and writing it out to an
	 * RDF file.
	 * 
	 * @param readerKey
	 *            label to differentiate records
	 * @param rdfFiles
	 *            output file definitions from parser config file
	 * @param alreadyObservedFieldUris
	 * @param recordIterator
	 *            iterator containing file data
	 */
	private void processRecord(DataRecord record, String readerKey, URIImpl recordUri) {
		setReaderKey(readerKey);
		Collection<? extends Statement> stmts = RdfRecordUtil.getRecordInstanceStatements(record, createdTime,
				recordUri, null, getReaderKey(), filter);
		DataSource ns = DataSource.getNamespace(RecordUtil.getRecordDataSource(record.getClass()));
		for (Statement stmt : stmts) {
			write(stmt, ns);
		}
	}

	// /**
	// * Constant is assumed to be a valid URI String
	// *
	// * @param tripleObj
	// * @param <E>
	// * @return
	// */
	// private <E extends DataRecord> Map<Class<?>, Collection<Value>>
	// getConstantValues(String value) {
	// Map<Class<?>, Collection<Value>> type2valuesMap = new HashMap<Class<?>,
	// Collection<Value>>();
	// Value constantValue = new URIImpl(value);
	// CollectionsUtil.addToOne2ManyMap(String.class, constantValue,
	// type2valuesMap);
	// return type2valuesMap;
	// }

	// /**
	// *
	// * @param <E>
	// * @param record
	// * @param tripleObj
	// * @return
	// */
	// private <E extends DataRecord> Map<Class<?>, Collection<Value>>
	// getLiteralValues(E record, String fieldName) {
	// Map<Class<?>, Collection<Value>> type2valuesMap = new HashMap<Class<?>,
	// Collection<Value>>();
	// Object fieldValue = PrivateAccessor.getFieldValue(record, fieldName);
	// if (fieldValue == null)
	// return type2valuesMap;
	// if (fieldValue instanceof DataSourceElement<?>) {
	// DataSourceElement<?> element = (DataSourceElement<?>) fieldValue;
	// Value literalValue = RdfUtil.createLiteral(element.getDataElement());
	// CollectionsUtil.addToOne2ManyMap(fieldValue.getClass(), literalValue,
	// type2valuesMap);
	// return type2valuesMap;
	// }
	// if (fieldValue instanceof Collection<?>) {
	// for (Object value : ((Collection<?>) fieldValue))
	// if (value instanceof DataSourceElement<?>) {
	// DataSourceElement<?> element = (DataSourceElement<?>) fieldValue;
	// Value literalValue = RdfUtil.createLiteral(element.getDataElement());
	// CollectionsUtil.addToOne2ManyMap(value.getClass(), literalValue,
	// type2valuesMap);
	// } else
	// throw new
	// RuntimeException(String.format("Unable to extract RDF object from field:
	// %s. "
	// +
	// "Expected Collection<ResourceComponent> but instead observed
	// Collection<%s>.",
	// fieldName,
	// value.getClass().getName()));
	// return type2valuesMap;
	// }
	// throw new
	// RuntimeException(String.format("Unable to extract RDF object from field:
	// %s (observedValue=%s)",
	// fieldName, fieldValue.toString()));
	// }

	// /**
	// * Get values for triple definition where value is specified to use ICE
	// formatting (ex:
	// * {@code <object use-ice-id="true">ensemblGeneId</object>})
	// *
	// * @param record
	// * @param tripleObj
	// * @return values
	// */
	// private Map<Class<?>, Collection<Value>>
	// getInformationContentEntityIDValues(DataRecord record, String fieldName)
	// {
	// Map<Class<?>, Collection<Value>> type2valuesMap = new HashMap<Class<?>,
	// Collection<Value>>();
	// Object fieldValue = PrivateAccessor.getFieldValue(record, fieldName);
	// if (fieldValue == null)
	// return type2valuesMap;
	//
	// if (fieldValue instanceof DataSourceIdentifier<?>) {
	// DataSourceIdentifier<?> id = (DataSourceIdentifier<?>) fieldValue;
	// RdfId rdfId = new RdfId(id);
	// Value iceIdValue = new
	// URIImpl(RdfUtil.createKiaoUri(rdfId.getNamespace(),
	// rdfId.getICE_ID()).toString());
	// CollectionsUtil.addToOne2ManyMap(fieldValue.getClass(), iceIdValue,
	// type2valuesMap);
	// return type2valuesMap;
	// }
	//
	// if (fieldValue instanceof Collection<?>) {
	// for (Object value : ((Collection<?>) fieldValue))
	// if (value instanceof DataSourceElement<?>) {
	// DataSourceIdentifier<?> id = (DataSourceIdentifier<?>) value;
	// RdfId rdfId = new RdfId(id);
	// Value iceIdValue = new
	// URIImpl(RdfUtil.createKiaoUri(rdfId.getNamespace(), rdfId.getICE_ID())
	// .toString());
	// CollectionsUtil.addToOne2ManyMap(value.getClass(), iceIdValue,
	// type2valuesMap);
	// } else
	// throw new
	// RuntimeException(String.format("Unable to extract RDF object from field:
	// %s. "
	// +
	// "Expected Collection<DataElementIdentifier<?>> but instead observed
	// Collection<%s>.",
	// fieldName, value.getClass().getName()));
	// return type2valuesMap;
	// }
	//
	// throw new
	// RuntimeException(String.format("Unable to extract RDF object from field:
	// %s (observedValue=%s)",
	// fieldName, fieldValue.toString()));
	// }

	// /**
	// * Parser {@link DataRecord} from field of record.
	// *
	// * @param <E>
	// * record type
	// * @param record
	// * instance
	// * @param fieldName
	// * field in record
	// * @return record
	// */
	// private <E extends DataRecord> Map<Class<?>, Collection<Value>>
	// getValues(E record, String fieldName) {
	// Map<Class<?>, Collection<Value>> type2valuesMap = new HashMap<Class<?>,
	// Collection<Value>>();
	// Object fieldValue = PrivateAccessor.getFieldValue(record, fieldName);
	// if (fieldValue == null)
	// return type2valuesMap;
	//
	// if (fieldValue instanceof DataSourceElement<?>) {
	// DataSourceElement<?> id = (DataSourceElement<?>) fieldValue;
	// Value rdfValue = null;
	//
	// if (id instanceof DataSourceIdentifier<?>) {
	// RdfId rdfId = new RdfId((DataSourceIdentifier<?>) id);
	// rdfValue = rdfId.getRdfValue();
	// } else
	// rdfValue = RdfUtil.createLiteral(id.getDataElement());
	//
	// CollectionsUtil.addToOne2ManyMap(fieldValue.getClass(), rdfValue,
	// type2valuesMap);
	// return type2valuesMap;
	// }
	//
	// if (fieldValue instanceof Collection<?>) {
	// for (Object value : ((Collection<?>) fieldValue)) {
	// if (value instanceof DataSourceElement<?>) {
	// DataSourceElement<?> id = (DataSourceElement<?>) value;
	// Value rdfValue = null;
	//
	// if (id instanceof DataSourceIdentifier<?>) {
	// RdfId rdfId = new RdfId((DataSourceIdentifier<?>) id);
	// rdfValue = rdfId.getRdfValue();
	// } else
	// rdfValue = RdfUtil.createLiteral(id.getDataElement());
	//
	// CollectionsUtil.addToOne2ManyMap(value.getClass(), rdfValue,
	// type2valuesMap);
	// } else {
	// throw new
	// RuntimeException(String.format("Unable to extract RDF object from field:
	// %s. "
	// +
	// "Expected Collection<ResourceComponent> but instead observed
	// Collection<%s>.",
	// fieldName,
	// value.getClass().getName()));
	// }
	// }
	//
	// return type2valuesMap;
	// }
	//
	// throw new
	// RuntimeException(String.format("Unable to extract RDF object from field:
	// %s (observedValue=%s)",
	// fieldName, fieldValue.toString()));
	// }
	//
	// /**
	// * Returns the subject Resource representation of the value of the field
	// with the given name
	// * contained in the input DataRecord. The field must be of type
	// ResourceIdentifier.
	// *
	// * @param record
	// * @param fieldName
	// * @return
	// *
	// */
	// private Collection<Resource> getSubjectResources(DataRecord record,
	// String fieldName) {
	// Collection<Resource> resources = new ArrayList<Resource>();
	// Object fieldValue = PrivateAccessor.getFieldValue(record, fieldName);
	//
	// if (fieldValue instanceof DataSourceIdentifier<?>) {
	// DataSourceIdentifier<?> id = (DataSourceIdentifier<?>) fieldValue;
	// RdfId rdfId = new RdfId(id);
	// resources.add(new URIImpl(RdfUtil.createKiaoUri(rdfId.getNamespace(),
	// id.toString()).toString()));
	// return resources;
	// }
	//
	// if (fieldValue instanceof Collection<?>) {
	// for (Object resource : ((Collection<?>) fieldValue))
	// if (resource instanceof DataSourceIdentifier<?>) {
	// DataSourceIdentifier<?> id = (DataSourceIdentifier<?>) resource;
	// RdfId rdfId = new RdfId(id);
	// resources.add(new URIImpl(RdfUtil.createKiaoUri(rdfId.getNamespace(),
	// id.toString()).toString()));
	// } else {
	// String message =
	// String.format("Unable to extract RDF subject from field: %s. "
	// +
	// "Expected Collection<ResourceIdentifier> but instead observed
	// Collection<%s>.",
	// fieldName, resource.getClass().getName());
	// throw new RuntimeException(message);
	// }
	//
	// return resources;
	// }
	//
	// throw new
	// RuntimeException(String.format("Unable to extract RDF subject from field:
	// %s (observedValue=%s)",
	// fieldName, fieldValue.toString()));
	// }

	/**
	 * Output RDF record to a file based on record's file key.
	 * 
	 * @param ns
	 * 
	 * @param rdfTriple
	 *            to output
	 */
	private void write(Statement stmt, DataSource ns) {
		RDFWriter rdfWriter = getWriter(ns);
		// primary key field values will always be printed, so there's no reason
		// to check and store
		// them in the filter. This saves some memory and also the time needed
		// to check for
		// something that is guaranteed to not be already observed
		boolean checkFilter = needToCheckFilter(stmt.getSubject());
		try {
			if (!checkFilter || (checkFilter && !filter.alreadyObservedStatement(stmt))) {
				if (!rollingCacheContains(stmt)) {
					write(stmt, rdfWriter);
					writtenStatementCount++;
				}
			}
		} catch (IllegalStateException e) {
			logger.error("Halting RDF Generation due to IllegalStateException.", e);
			try {
				closeFiles();
				System.exit(-1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * if the input subject matches one of the prefixes for a primary key field
	 * then we do not need to check the filter b/c there is no way that field
	 * has been previously observed. Aside from saving time, this will also save
	 * on memory b/c there's no point in caching this field since it should not
	 * be observed again given its "primary key" status
	 * 
	 * @param subject
	 * @param primaryKeyFieldValues
	 *            will contain things like:
	 *            F_SparseUniProtDatFileRecord_accession_
	 * @return true if the filter should be checked, false otherwise
	 */
	private boolean needToCheckFilter(Resource subject) {
		String sub = subject.toString();
		for (String key : primaryKeyFieldNames) {
			if (sub.contains(key)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * keeps track of the most recent 1000 statements and returns true if the
	 * input statement is in the cache
	 * 
	 * @param stmt
	 * @return
	 */
	private boolean rollingCacheContains(Statement stmt) {
		String stmtStr = stmt.toString();
		if (rollingCacheSet.contains(stmtStr)) {
			return true;
		}
		int currentSize = rollingCacheList.size();
		if (currentSize >= ROLLING_CACHE_MAX_SIZE) {
			String removed = rollingCacheList.removeLast();
			rollingCacheSet.remove(removed);
		}
		rollingCacheSet.add(stmtStr);
		rollingCacheList.addFirst(stmtStr);
		return false;
	}

	/**
	 * Write statement using provided writer.
	 * 
	 * @param s
	 *            statement
	 * @param rdfWriter
	 *            writer
	 */
	private static void write(Statement s, RDFWriter rdfWriter) {
		try {
			rdfWriter.handleStatement(s);
		} catch (RDFHandlerException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Initialize lazily and return rdf writer.
	 * 
	 * @param ns
	 * 
	 * @param rdfFile
	 *            key to use when generating and storing rdf writer
	 * @return rdf writer
	 * @throws FileNotFoundException
	 * @throws RDFHandlerException
	 */
	private RDFWriter getWriter(DataSource ns) {
		File outputFile = getOutputFile(ns);
		if (!file2RdfWriterMap.containsKey(outputFile)) {
			generatedRdfFiles.add(outputFile);
			Writer writer = null;
			try {
				logger.info(
						"Initializing new RDF writer. Compress flag = " + compress + " Output file = " + outputFile);
				OutputStream os = new FileOutputStream(outputFile);
				if (compress) {
					os = new GZIPOutputStream(os);
				}
				writer = new BufferedWriter(
						new OutputStreamWriter(os, Charset.forName(encoding.getCharacterSetName()).newEncoder()));
			} catch (IOException e) {
				throw new RuntimeException("Error occured while creating rdf writer to " + getOutputFile(ns), e);
			}

			// ensure calls to Rio.createWriter are synchronized on class
			// creating concurrent rdf
			// writers
			synchronized (RdfRecordWriter.class) {
				RDFWriter rdfWriter = rdfFormat.createWriter(writer);
				try {
					rdfWriter.startRDF();
				} catch (RDFHandlerException e) {
					throw new RuntimeException("Error occured while starting rdf writer to " + getOutputFile(ns), e);
				}
				file2RdfWriterMap.put(outputFile, new RdfWriterResource(rdfWriter, writer));
			}

		}
		return file2RdfWriterMap.get(outputFile).rw;
	}

	/**
	 * Get file index derived from total # of statements writtent to a file and
	 * {@link #maxStatementsPerFile} value.
	 * 
	 * @param rdfFile
	 *            file
	 * @return file index
	 */
	private long getOutFileIndex() {
		return maxStatementsPerFile > 0 ? writtenStatementCount / maxStatementsPerFile : 0;
	}

	// /**
	// * Opens an OutputStream based on the fileKey and {@link #baseRdfFileName}
	// *
	// * @param rdfFile
	// * @return
	// * @throws FileNotFoundException
	// */
	// private OutputStream getOutputStream() throws IOException {
	// File outputFile = getOutputFile();
	// generatedRdfFiles.add(outputFile);
	// if (compress) {
	// logger.info("RDF output stream will be a GZIPOutputStream");
	// return new GZIPOutputStream(new FileOutputStream(outputFile));
	// }
	// logger.info("RDF output stream will NOT be a compressed.");
	// return new FileOutputStream(outputFile);
	// }

	/**
	 * Creates a reference to the RDF output File
	 * 
	 * @param ns
	 * 
	 * @param rdfFile
	 *            to use in file name generation
	 * @return full rdf file path
	 */
	private File getOutputFile(DataSource ns) {
		return FileUtil.appendPathElementsToDirectory(outputDirectory, getOutputFileName(ns));
	}

	/**
	 * Compiles the name of the output file
	 * 
	 * @param ns
	 * 
	 * @return
	 */
	private String getOutputFileName(DataSource ns) {
		String recordReaderClass = recordReader.getClass().getSimpleName();
		recordReaderClass = (recordReader.getDataSpecificKey().isEmpty()) ? recordReaderClass
				: recordReaderClass + StringConstants.HYPHEN_MINUS + recordReader.getDataSpecificKey();
		String fileName = ns.name().toLowerCase() + StringConstants.HYPHEN_MINUS + recordReaderClass
				+ (getReaderKey().isEmpty() ? StringConstants.BLANK : StringConstants.HYPHEN_MINUS)
				+ getReaderKey().replaceAll(" ", "_") + StringConstants.PERIOD + getOutFileIndex()
				+ StringConstants.HYPHEN_MINUS + batchNumber + StringConstants.PERIOD
				+ rdfFormat.defaultFileExtension();
		fileName = (compress) ? fileName + ".gz" : fileName;
		return fileName;
	}

	/**
	 * Closes the RDFWriters
	 * 
	 * @param rdfWriters
	 * @throws RDFHandlerException
	 */
	private void closeFiles() throws IOException {
		try {
			for (RdfWriterResource rdfWriterResource : file2RdfWriterMap.values()) {
				rdfWriterResource.rw.endRDF();
				rdfWriterResource.w.close();
			}
		} catch (RDFHandlerException e) {
			throw new IOException("Exception while closing RDF Writers.", e);
		}
	}

	// /**
	// * By convention, the configuration file directing RDF output lives on the
	// classpath in the
	// same
	// * package as the parser to which it belongs. The name of the
	// configuration file is the name
	// of
	// * the parser lowercased with the .rdf-config.xml suffix.
	// *
	// * @param recordReader
	// * @return
	// * @throws IOException
	// */
	// private List<RdfFile> getRdfFilesToCreate(InputStream configStream)
	// throws IOException {
	// return RdfConfigFileReader.parseRdfConfig(configStream);
	// }
	//
	// /**
	// * Definition for rdf instance and target file key.
	// *
	// * @author bill
	// */
	// protected static class RdfTriple {
	// /** triple statement */
	// private final Statement statement;
	// /** config rdf file definition; contains output file key */
	// private final RdfFile rdfFile;
	//
	// /**
	// * Constructor
	// *
	// * @param statement
	// * triple
	// * @param rdfFile
	// * rdf file from which statement was derived
	// */
	// public RdfTriple(Statement statement, RdfFile rdfFile) {
	// super();
	// this.statement = statement;
	// this.rdfFile = rdfFile;
	// }
	//
	// /**
	// * Get statement.
	// *
	// * @return statement
	// */
	// public Statement getStatement() {
	// return statement;
	// }
	//
	// /**
	// * Get rdf file configuration.
	// *
	// * @return config
	// */
	// public RdfFile getRdfFile() {
	// return rdfFile;
	// }
	// }

	/**
	 * @return the encoding
	 */
	public CharacterEncoding getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(CharacterEncoding encoding) {
		this.encoding = encoding;
	}

	/**
	 * Wrapper that captures {@link RDFWriter} and underlying {@link Writer} for
	 * post processing.
	 */
	private static class RdfWriterResource {
		/** rdf writer */
		private final RDFWriter rw;

		/** underlying writer used by rdf writer */
		private final Writer w;

		/**
		 * Constructor .
		 * 
		 * @param rw
		 *            rdf writer
		 * @param w
		 *            java writer
		 */
		public RdfWriterResource(RDFWriter rw, Writer w) {
			this.rw = rw;
			this.w = w;
		}
	}

}
