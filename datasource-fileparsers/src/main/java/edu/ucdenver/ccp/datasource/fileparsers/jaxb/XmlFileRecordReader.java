/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.FileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * @param <T>
 * 
 */
public abstract class XmlFileRecordReader<T extends FileRecord> extends FileRecordReader<T> {

	private XMLEventReader xmlfer;
	private XMLEventReader xmler;
	private InputStream is;
	private JAXBContext ctx;
	private Unmarshaller um;

	private T nextRecord = null;

	private final Set<NcbiTaxonomyID> taxonsOfInterest;
	private final Class<?> entryClass;

	// public XmlFileRecordReader(File workDirectory, boolean clean) throws
	// IOException {
	// this(workDirectory, clean, null);
	// }

	public XmlFileRecordReader(Class<?> entryClass, File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(workDirectory, null, null, null, null, clean);
		this.entryClass = entryClass;
		taxonsOfInterest = taxonIds;
		try {
			initialize(initializeInputStreamFromDownload());
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	// public XmlFileRecordReader(File dataFile) throws IOException {
	// this(dataFile, null);
	// }

	/**
	 * @param dataFile
	 * @param encoding
	 * @param skipLinePrefix
	 * @throws IOException
	 */
	public XmlFileRecordReader(Class<?> entryClass, File dataFile, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(dataFile, null, null);
		this.entryClass = entryClass;
		taxonsOfInterest = taxonIds;
		try {
			InputStream is;
			if (dataFile.getName().endsWith(".gz")) {
				is = new GZIPInputStream(new FileInputStream(dataFile));
			} else {
				is = new FileInputStream(dataFile);
			}
			initialize(is);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream initializeInputStreamFromDownload() throws IOException {
		throw new UnsupportedOperationException(
				"The initializeInputStreamFromDownload() method is designed to be used "
						+ "when a subclass of this class is automatically obtaining the input file. The subclass should initialize "
						+ "the InputStream that will serve the UniProt XML to the XML parsing code.");
	}

	/**
	 * @param dataFile
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 * @throws JAXBException
	 */
	private void initialize(InputStream is) throws FileNotFoundException, XMLStreamException, JAXBException {
		this.is = is;
		ctx = JAXBContext.newInstance(entryClass);
		um = ctx.createUnmarshaller();
		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		xmler = xmlif.createXMLEventReader(is);
		EventFilter filter = new EventFilter() {
			public boolean accept(XMLEvent event) {
				return event.isStartElement();
			}
		};
		xmlfer = xmlif.createFilteredReader(xmler, filter);
		// Jump to the first element in the document, the enclosing Uniprot in
		// the case of uniprot
		// xml
		StartElement e = (StartElement) xmlfer.nextEvent();
		advanceToRecordWithTaxonOfInterest();
	}

	private void advanceToRecordWithTaxonOfInterest() {
		if (hasNext()) {
			while (nextRecord != null && !hasTaxonOfInterest(nextRecord)) {
				next();
				hasNext();
			}
		}
	}

	/**
	 * @param nextRecord2
	 * @return
	 */
	protected abstract boolean hasTaxonOfInterest(T record);

	protected Set<NcbiTaxonomyID> getTaxonsOfInterest() {
		if (taxonsOfInterest == null) {
			return null;
		}
		return new HashSet<NcbiTaxonomyID>(taxonsOfInterest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		is.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (nextRecord == null) {
			try {
				while (xmlfer.peek() != null) {
					JAXBElement<?> unmarshalledElement = um.unmarshal(xmler,entryClass);
					Object o = unmarshalledElement.getValue();
					if (entryClass.isInstance(o)) {
						nextRecord = initializeNewRecord(entryClass.cast(o));
						if (hasTaxonOfInterest(nextRecord)) {
							return true;
						}
						nextRecord = null;
					}
				}
				return false;
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
		}
		return true;

	}

	/**
	 * @param entryXmlClassInstance
	 *            an instance of the class that is the "entry" into the XML, i.e. the thing you
	 *            want to iterate over
	 * @return
	 */
	protected abstract T initializeNewRecord(Object entryXmlClassInstance);

	// protected UniProtFileRecord initializeNewRecord(Entry entry) {
	// return new UniProtFileRecord(entry);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		T record = nextRecord;
		nextRecord = null;
		return record;
	}

}
