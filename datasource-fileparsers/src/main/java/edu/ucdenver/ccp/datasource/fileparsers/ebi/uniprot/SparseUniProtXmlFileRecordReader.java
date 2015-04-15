/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.UniProtFileRecord.DbReference;
import edu.ucdenver.ccp.fileparsers.jaxb.XmlFileRecordReader;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class SparseUniProtXmlFileRecordReader extends XmlFileRecordReader<SparseUniProtFileRecord> {
	private static final Logger logger = Logger.getLogger(SparseUniProtDatFileRecordReader.class);

	// public SparseUniProtXmlFileRecordReader(File workDirectory, boolean
	// clean) throws IOException {
	// super(workDirectory, clean);
	// }
	//
	// public SparseUniProtXmlFileRecordReader(File dataFile) throws IOException
	// {
	// super(dataFile);
	// }

	public SparseUniProtXmlFileRecordReader(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(org.uniprot.Entry.class, workDirectory, clean, taxonIds);
	}

	public SparseUniProtXmlFileRecordReader(File dataFile, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		super(org.uniprot.Entry.class, dataFile, taxonIds);
	}

	protected InputStream initializeInputStreamFromDownload() throws IOException {
		throw new UnsupportedOperationException(
				"The initializeInputStreamFromDownload() method is designed to be used "
						+ "when a subclass of this class is automatically obtaining the input file. The subclass should initialize "
						+ "the InputStream that will serve the UniProt XML to the XML parsing code.");
	}

	@Override
	protected SparseUniProtFileRecord initializeNewRecord(Object entry) {
		if (org.uniprot.Entry.class.isInstance(entry)) {
			return new SparseUniProtFileRecord((org.uniprot.Entry) entry);
		}
		logger.warn("Expected org.uniprot.Entry, but observed " + entry.getClass().getName() + ". Skipping record...");
		return null;
	}

	@Override
	protected boolean hasTaxonOfInterest(SparseUniProtFileRecord record) {
		if (getTaxonsOfInterest() == null || getTaxonsOfInterest().isEmpty()) {
			return true;
		}
		for (DbReference dbRef : record.getOrganism().getDbReference()) {
			if (getTaxonsOfInterest().contains(dbRef.getId())) {
				System.out.println("has taxon of interest: " + dbRef.getId());
				return true;
			}
		}
		return false;
	}

}
