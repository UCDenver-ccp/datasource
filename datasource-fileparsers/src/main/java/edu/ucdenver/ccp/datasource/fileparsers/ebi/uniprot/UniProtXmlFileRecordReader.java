/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.uniprot.Entry;

import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.UniProtFileRecord.DbReference;
import edu.ucdenver.ccp.fileparsers.jaxb.XmlFileRecordReader;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class UniProtXmlFileRecordReader extends XmlFileRecordReader<UniProtFileRecord> {

	private static final Logger logger = Logger.getLogger(UniProtXmlFileRecordReader.class);

	// public UniProtXmlFileRecordReader(File workDirectory, boolean clean)
	// throws IOException {
	// super(workDirectory, clean);
	// }
	//
	// public UniProtXmlFileRecordReader(File dataFile) throws IOException {
	// super(dataFile);
	// }

	public UniProtXmlFileRecordReader(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(org.uniprot.Entry.class, workDirectory, clean, taxonIds);
	}

	public UniProtXmlFileRecordReader(File dataFile, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(org.uniprot.Entry.class, dataFile, taxonIds);
	}

	protected InputStream initializeInputStreamFromDownload() throws IOException {
		throw new UnsupportedOperationException(
				"The initializeInputStreamFromDownload() method is designed to be used "
						+ "when a subclass of this class is automatically obtaining the input file. The subclass should initialize "
						+ "the InputStream that will serve the UniProt XML to the XML parsing code.");
	}

	@Override
	protected UniProtFileRecord initializeNewRecord(Object entry) {
		if (org.uniprot.Entry.class.isInstance(entry)) {
			return new UniProtFileRecord((org.uniprot.Entry) entry);
		}
		logger.warn("Expected org.uniprot.Entry, but observed " + entry.getClass().getName() + ". Skipping record...");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.fileparsers.jaxb.XmlFileRecordReader#hasTaxonOfInterest
	 * (edu.ucdenver.ccp .datasource.fileparsers.FileRecord)
	 */
	@Override
	protected boolean hasTaxonOfInterest(UniProtFileRecord record) {
		if (getTaxonsOfInterest() == null || getTaxonsOfInterest().isEmpty()) {
			return true;
		}
		for (DbReference dbRef : record.getOrganism().getDbReference()) {
			if (getTaxonsOfInterest().contains(dbRef.getId())) {
				return true;
			}
		}
		return false;
	}

}
