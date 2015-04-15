package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.sql.rowset.spi.XmlWriter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
//import org.deri.any23.parser.NQuadsWriter;
//import org.deri.any23.parser.NQuadsParser;
import org.openrdf.model.impl.BooleanLiteralImpl;
import org.openrdf.model.impl.CalendarLiteralImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.reflection.PrivateAccessor;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceElement;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.RdfId;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.RdfPartition;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.KIAO;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.vocabulary.RDF;

/**
 * RDF generation utilities.
 */
public class RdfUtil {

	private static final Logger logger = Logger.getLogger(RdfUtil.class);

	public enum RdfFormat {

		NTRIPLES(RDFFormat.NTRIPLES.getDefaultFileExtension(), 		RDFFormat.NTRIPLES),
		NQUADS(  RDFFormat.NQUADS.getDefaultFileExtension(),		RDFFormat.NQUADS),
		N3(      RDFFormat.N3.getDefaultFileExtension(), 			RDFFormat.N3),
		RDFXML(  RDFFormat.RDFXML.getDefaultFileExtension(), 		RDFFormat.RDFXML),
		TRIG(    RDFFormat.TRIG.getDefaultFileExtension(), 			RDFFormat.TRIG),
		TRIX(    RDFFormat.TRIX.getDefaultFileExtension(), 			RDFFormat.TRIX),
		TURTLE(  RDFFormat.TURTLE.getDefaultFileExtension(), 		RDFFormat.TURTLE)
		;


		private final String defaultFileExtension;
		private final RDFFormat rioFormat;

		private RdfFormat(String defaultFileExtension, RDFFormat rioFormat) {
			this.defaultFileExtension = defaultFileExtension;
			this.rioFormat = rioFormat;
		}

		public String defaultFileExtension() {
			return defaultFileExtension;
		}

		public RDFFormat getRioRdfFormat() {
			return rioFormat;
		}
		
		public RDFWriter createWriter(Writer writer) {
			return Rio.createWriter(rioFormat, writer);

			/*
			if (this.equals(NQUADS)) {
				return new NQuadsWriter(writer);
			}
			else {
				return Rio.createWriter(rioFormat, writer);
			}
			*/
		}

		public RDFParser createParser(RDFFormat rioFormat) {
				return Rio.createParser(rioFormat);

			/*
			if (this.equals(NQUADS)) {
				return new NQuadsParser();
			}
			else {
				return Rio.createParser(rioFormat);
			}
			*/
		}
	}

	/** blank field value constant - used assertions' Subject position */
	private static final String FIELD_VALUE = "F";

	/** URI of predicate to indicate external rdf file */
	public static final URI RDF_FILE_URI = createUri(DataSource.KABOB, "RDFFile");

	/** delimiter to use when replacing illegal characters in namespaces */
	public static final String NAMESPACE_LOCALNAME_DELIMITER = StringConstants.UNDERSCORE;

	/** namespace uri delimiter */
	public static final String NAMESPACE_PARTITION_DELIMITER = StringConstants.FORWARD_SLASH;

	/** rdf string literal language */
	public static final String ENGLISH_LANGUAGE = "en";

	// /** field names to be excluded from serializing instances of {@link DataRecord} */
	// private static Set<String> systemFieldNames = Collections.unmodifiableSet(new
	// HashSet<String>(Arrays.asList(
	// "recordID", "byteOffset", "lineNumber")));

	/**
	 * Private default constructor to prevent instantiation.
	 */
	private RdfUtil() {
		// utility class - not meant for instantiation
	}

	public static DataSource getDataSource(DataSource ns) {
		DataSource ds = null;
		for (DataSource datasource : DataSource.values())
			if (datasource.name().equals(ns.name())) {
				ds = datasource;
				break;
			}

		// if (ds == null && !set.contains(ns.name())) {
		// logger.warn("Unable to retrieve data source for namespace: \"" + ns.name()
		// + "\". !!!This may be expected, i.e. in the case of the RDFS namespace");
		// }
		return ds;
	}

	/**
	 * Create a URIImpl in the specified namespace for a given localName.
	 * 
	 * @see createUri
	 */
	public static URIImpl createUriImpl(DataSource namespace, String localName) {
		String baseNameSpace = namespace.longName();
		return new URIImpl(createUri(baseNameSpace, localName, getDataSource(namespace)).toString());
	}

	/**
	 * Create URI in specified {@code namespace} for given {@code localName}. This method normalizes
	 * localName by using the {@link URLEncoder#encode(String, String)} method.
	 * 
	 * @param namespace
	 * @param localName
	 * @return URI
	 */
	public static URI createUri(DataSource namespace, String localName) {
		String baseNameSpace = namespace.longName();
		return createUri(baseNameSpace, localName, getDataSource(namespace));
	}

	/**
	 * Create URI in specified {@code baseNameSpace} for given {@code localName}. This method
	 * normalizes localName by using the {@link URLEncoder#encode(String, String)} method. In order
	 * 
	 * to maintain consistency with OBO we first replace colons with underscores. If the local name
	 * starts with a number then the specified {@link DataSource} name will be appended to the
	 * beginning of the local name with an underscore.
	 * 
	 * That is, obo names look like GO:123456 and get translated to GO_123456. If they look like
	 * 123456 they get translated to XX_123456, where XX is the datasource name.
	 * 
	 * @param baseNamespace
	 *            base namespace
	 * @param localName
	 *            local name
	 * @return URI
	 */
	public static URI createUri(String baseNamespace, String localName, DataSource ds) {
		String encodedName = localName.replaceAll(":", "_");
		try {
			encodedName = URLEncoder.encode(encodedName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		if (StringUtil.startsWithRegex(encodedName, "[0-9]")) {
			encodedName = ds.name() + NAMESPACE_LOCALNAME_DELIMITER + encodedName;
		}

		String uriStr = baseNamespace + encodedName;
		return URI.create(uriStr);
	}

	public static URIImpl createUriImpl(String baseNamespace, String localName, DataSource ns) {
		String encodedName = localName.replaceAll(":", "_");
		try {
			encodedName = URLEncoder.encode(encodedName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		if (StringUtil.startsWithRegex(encodedName, "[0-9]")) {
			encodedName = ns.getLocalName() + NAMESPACE_LOCALNAME_DELIMITER + encodedName;
		}
		String uriStr = baseNamespace + encodedName;
		return new URIImpl(uriStr);
	}

	/**
	 * Returns a unique URI with a human readable prefix that can be used to indicate what the URI
	 * represents
	 * 
	 * @param prefix
	 * @return
	 */
	public static Resource createUniqueBlankNodeUri(String prefix) {
		String normPrefix = prefix;
		if (normPrefix.contains("-")) {
			normPrefix = normPrefix.replaceAll("-", "");
			logger.warn("Illegal blank node prefix detected: '" + prefix
					+ "' cannot contain a hyphen. It has been removed.");
		}
		return new BNodeImpl(normPrefix + UUID.randomUUID().toString().replaceAll("-", ""));
	}

	/**
	 * Create URI using file: protocol to a relative path
	 * 
	 * @param relativePath
	 * @return URI
	 */
	public static URIImpl createRelativeFileUri(String relativePath) {
		String modRelativePath = relativePath.replaceAll("\\\\", NAMESPACE_PARTITION_DELIMITER); // windows
																									// path
																									// fix
		return new URIImpl("file:///" + modRelativePath);
	}

	/**
	 * Create statement with convenience of using {@link org.openrdf.model.URI} for all subject,
	 * predicate and object.
	 * 
	 * @param subject
	 * @param predicate
	 * @param object
	 * @return {@link Statement}
	 * @throws IllegalArgumentException
	 *             if one of arguments is not a valid absolute URI
	 */
	public static Statement createUriStatement(String subject, String predicate, String object) {
		return createStatement(new URIImpl(subject), new URIImpl(predicate), new URIImpl(object));
	}

	/**
	 * Wrapper to create a {@link Statement}
	 * 
	 * @param subject
	 * @param predicate
	 * @param object
	 * @return statement
	 */
	public static Statement createStatement(org.openrdf.model.Resource subject, org.openrdf.model.URI predicate,
			org.openrdf.model.Value object) {
		return new StatementImpl(subject, predicate, object);
	}

	/**
	 * Create calendar literal
	 * 
	 * @param timeInMillis
	 *            time value used to create {@link Calendar} instance
	 * @return calendar literal
	 */
	public static CalendarLiteralImpl getDateLiteral(long timeInMillis) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(timeInMillis);
		return getDateLiteral(c);
	}

	/**
	 * Create calendar literal.
	 * 
	 * @param c
	 *            calendar instance
	 * @return calendar literal
	 */
	public static CalendarLiteralImpl getDateLiteral(GregorianCalendar c) {
		XMLGregorianCalendar calendar = null;

		try {
			calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}

		CalendarLiteralImpl object = new CalendarLiteralImpl(calendar);
		return object;
	}

	/**
	 * Get relative path that follows {@code relativePathPrefix}. If {@code relativePathPrefix} is
	 * not found in {@code filePath}, then filePath is returned.
	 * 
	 * @param filePath
	 * @param relativePathPrefix
	 * @return relative path if relativePathPrefix is found; otherwise, absolutePath.
	 */
	public static String getRelativeFilePath(String filePath, String relativePathPrefix) {
		int prefixIndex = filePath.indexOf(relativePathPrefix);
		if (prefixIndex == -1)
			return filePath;

		int relativePathStartIndex = prefixIndex + relativePathPrefix.length() + 1;
		return filePath.substring(relativePathStartIndex);
	}

	/**
	 * Returns a relative path to the input file. This method assumes a certain directory structure
	 * and returns a relative path starting at the {@code relativePathPrefix} directory.
	 * 
	 * @param file
	 * @return
	 */
	public static String getRelativeFilePath(File file, String relativePathPrefix) {
		if (relativePathPrefix == null)
			return file.toURI().toString();

		return getRelativeFilePath(file.getAbsolutePath(), relativePathPrefix);
	}

	/**
	 * Returns a URI for the input file with relative prefix.
	 * 
	 * @param file
	 * @return
	 */
	public static URIImpl createRelativeFileUri(File file, String relativePathPrefix) {
		if (relativePathPrefix == null)
			return new URIImpl(file.toURI().toString());

		return createRelativeFileUri(getRelativeFilePath(file, relativePathPrefix));
	}

	/**
	 * Given the input File, this method produces an RDF statement that declares the input file a
	 * RdfFile
	 * 
	 * @param fileUri
	 *            properly formatted file URI (ex: file:///myfile)
	 * @return statement
	 * @throws URISyntaxException
	 */
	public static Statement getFileIsTypeRdfFileStatement(String fileUri) {
		return new StatementImpl(new URIImpl(fileUri), RDF.TYPE.uri(), new URIImpl(RDF_FILE_URI.toString()));
	}

	/**
	 * Given the input uri, this method produces and RDF statement that declares a timestamp for
	 * creation of the input uri.
	 * 
	 * @param uri
	 *            any URI
	 * @param timeInMillis
	 *            time value
	 * @return statement
	 */
	public static Statement getCreationTimeStampStatement(String uri, long timeInMillis) {
		return new StatementImpl(new URIImpl(uri), KIAO.HAS_CREATION_DATE.uri(), getDateLiteral(timeInMillis));
	}

	public static Statement getCreationTimeStampStatement(URIImpl uri, long timeInMillis) {
		return new StatementImpl(uri, KIAO.HAS_CREATION_DATE.uri(), getDateLiteral(timeInMillis));
	}

	/**
	 * Returns a statement linking the rdf file uri to a string literal representing the local path
	 * of the file
	 * 
	 * @param fileUri
	 * @param outputFile
	 * @return
	 * @throws URISyntaxException
	 */
	public static Statement getFileHasLocalPathStatement(String fileUri, File outputFile, String relativeDirPrefix) {
		Value object = createLiteral(getRelativeFilePath(outputFile, relativeDirPrefix));
		return new StatementImpl(new URIImpl(fileUri), KIAO.HAS_LOCAL_FILE_PATH.uri(), object);
	}

	/**
	 * 
	 * Create URI with local name. Example: for result
	 * {@code http://kabob.ucdenver.edu/iao/omim/OMIM_100050_ICE},
	 * 
	 * <pre>
	 * 'http://kabob.ucdenver.edu/' - derived form baseNameSpace
	 * 'omim' - derived form targetNameSpace
	 * 'OMIM_100050_ICE' - localName
	 * </pre>
	 * 
	 * @param baseNamespace
	 * @param targetNamespace
	 * @param localName
	 * @return URI
	 */
	public static URIImpl createKiaoUri(DataSource targetNamespace, String localName) {
		String baseUri = DataSource.KABOB.longName() + RdfPartition.IAO.toString().toLowerCase()
				+ NAMESPACE_PARTITION_DELIMITER + targetNamespace.lowerName() + NAMESPACE_PARTITION_DELIMITER;
		return createUriImpl(baseUri, localName, targetNamespace);

	}

	/**
	 * 
	 * Create URI with local name. Example: for result
	 * {@code http://kabob.ucdenver.edu/iao/omim/OMIM_100050_ICE},
	 * 
	 * <pre>
	 * 'http://kabob.ucdenver.edu/' - derived form baseNameSpace
	 * 'omim' - derived form targetNameSpace
	 * 'OMIM_100050_ICE' - localName
	 * </pre>
	 * 
	 * @param baseNamespace
	 * @param targetNamespace
	 * @param localName
	 * @return URI
	 */
	public static String createKiaoUriAsString(DataSource targetNamespace, String localName) {
		return createKiaoUri(targetNamespace, localName).toString();
	}

	/**
	 * Write statements with provided rdf writer. Method will start (open) and end (close) RDF
	 * writer automatically.
	 * 
	 * @param statements
	 *            to write
	 * @param rdfWriter
	 *            writer to use
	 * @throws RuntimeException
	 *             if rdf output errors occur
	 */
	public static void writeStatements(Collection<Statement> statements, RDFWriter rdfWriter) {
		try {
			rdfWriter.startRDF();
			for (Statement s : statements) {
				rdfWriter.handleStatement(s);
			}

			rdfWriter.endRDF();
		} catch (RDFHandlerException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a set of statements that are a copy of {@code sourceStatements} with Subject
	 * substituted by provided {@code subjectUri}.
	 * 
	 * @param subjectUri
	 * @param sourceStatements
	 * 
	 * @return copied statements
	 */
	public static Collection<Statement> copyStatementsFromPredicateValue(String subjectUri,
			Collection<Statement> sourceStatements) {
		Collection<Statement> statements = new ArrayList<Statement>();

		for (Statement statement : sourceStatements) {
			statements.add(createStatement(new URIImpl(subjectUri), statement.getPredicate(), statement.getObject()));
		}
		return statements;
	}

	/**
	 * Get literal value. If {@code fieldValue} type isn't recognized , literal defaults to default
	 * {@link LiteralImpl} String instance.
	 * <p>
	 * Numeric, Boolean and {@link Date} types are supported.
	 * 
	 * @param fieldValue
	 *            non-null value
	 * @return literal value.
	 */
	public static LiteralImpl createLiteral(Object fieldValue) {
		// literal
		LiteralImpl literal = new LiteralImpl(fieldValue.toString(), RdfUtil.ENGLISH_LANGUAGE);

		if (fieldValue instanceof Number)
			literal = createNumericLiteral((Number) fieldValue);
		else if (fieldValue instanceof Boolean)
			literal = new BooleanLiteralImpl((Boolean) fieldValue);
		else if (fieldValue instanceof java.util.Date) {
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime((Date) fieldValue);
			literal = getDateLiteral((GregorianCalendar) cal);
		}
		return literal;
	}

	/**
	 * Get numeric literal value. If Number type isn't recognized , literal defaults to default
	 * {@link LiteralImpl} String instance.
	 * 
	 * @param value
	 *            non-null value
	 * @return literal value.
	 */
	private static LiteralImpl createNumericLiteral(Number value) {
		LiteralImpl literal = null;

		if (value instanceof Double) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#decimal"));
		} else if (value instanceof Byte) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#integer"));
		} else if (value instanceof Float) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#decimal"));
		} else if (value instanceof Integer) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#integer"));
		} else if (value instanceof Long) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#integer"));
		} else if (value instanceof Short) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#integer"));
		} else if (value instanceof BigDecimal) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#decimal"));
		} else if (value instanceof BigInteger) {
			literal = new LiteralImpl(value.toString(), new URIImpl("http://www.w3.org/2001/XMLSchema#integer"));
		} else {
			literal = new LiteralImpl(value.toString(), RdfUtil.ENGLISH_LANGUAGE);
		}
		return literal;
	}

	/**
	 * @param fieldValue
	 *            should be either a {@link DataSourceIdentifier}, {@link DataSourceElement}, or a
	 *            literal
	 * @return a proper {@link Value} for the input object
	 */
	public static Value getValue(Object fieldValue) {
		if (fieldValue instanceof DataSourceIdentifier) {
			DataSourceIdentifier<?> id = (DataSourceIdentifier<?>) fieldValue;
			RdfId rdfId = new RdfId(id);
			return RdfUtil.createKiaoUri(rdfId.getNamespace(), rdfId.getICE_ID());
		} else if (fieldValue instanceof DataSourceElement) {
			DataSourceElement<?> dataSourceElement = (DataSourceElement<?>) fieldValue;
			return RdfUtil.createLiteral(dataSourceElement.getDataElement());
		} else if (fieldValue != null && !DataSourceElement.class.isAssignableFrom(fieldValue.getClass())) {
			return RdfUtil.createLiteral(fieldValue);
		} else {
			throw new IllegalArgumentException("Unhandled Value type: " + fieldValue.getClass().getName());
		}
	}

	/**
	 * Initializes a new {@link RDFWriter} to write the specified output file using the specified
	 * {@link RDFFormat} and {@link CharacterEncoding}
	 * 
	 * @param outputFile
	 * @param encoding
	 * @param format
	 * @return
	 * @throws IllegalStateException
	 *             if file is found or RDF writer cannot be initialized
	 */
	public static RDFWriter openWriter(File outputFile, CharacterEncoding encoding, RdfFormat format) {
		Writer writer = null;
		try {
			writer = FileWriterUtil.initBufferedWriter(outputFile, encoding, WriteMode.OVERWRITE,
					FileSuffixEnforcement.OFF);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
		RDFWriter rdfWriter = format.createWriter(writer);
		try {
			rdfWriter.startRDF();
		} catch (RDFHandlerException e) {
			throw new IllegalStateException(e);
		}

		return rdfWriter;
	}

	@Deprecated
	public static RDFWriter createWriter(RdfFormat format, Writer writer) {
		return format.createWriter(writer);
	}

	/**
	 * Close rdf writer by invoking {@link RDFWriter#endRDF()} but also close internal private
	 * Writer field. Unfortunately, {@link RDFWriter#endRDF()} only flushes, but does not close it.
	 * 
	 * @param r
	 *            writer
	 */
	public static void closeWriter(RDFWriter r) {
		if (r == null)
			return;

		try {
			r.endRDF();
		} catch (RDFHandlerException e) {
			throw new RuntimeException(e);
		}

		try {
			Field f = PrivateAccessor.getPrivateMemberField(r, "writer");
			if (f != null) {
				Object v = f.get(r);
				if (v instanceof Writer)
					((Writer) v).close();
			}
		} catch (NoSuchFieldException e) {
			closeXmlWriter(r);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The TRIX RDFWriter wraps its input Writer in an XMLWriter. This method attempts to close the
	 * wrapped Writer
	 * 
	 * @param writer
	 */
	private static void closeXmlWriter(RDFWriter writer) {
		try {
			Field xmlWriterField = PrivateAccessor.getPrivateMemberField(writer, "xmlWriter");
			if (xmlWriterField != null) {
				Object xmlWriter = xmlWriterField.get(writer);
				if (xmlWriter instanceof XmlWriter) {
					Field writerField = PrivateAccessor.getPrivateMemberField(writer, "_writer");
					if (writerField != null) {
						Object w = writerField.get(xmlWriter);
						if (w instanceof Writer)
							((Writer) w).close();
					}
				}
			}
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
