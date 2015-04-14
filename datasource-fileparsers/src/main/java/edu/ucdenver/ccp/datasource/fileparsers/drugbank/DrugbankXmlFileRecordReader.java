/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.drugbank;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.fileparsers.jaxb.XmlFileRecordReader;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class DrugbankXmlFileRecordReader extends XmlFileRecordReader<DrugBankDrugRecord> {

	private static final Logger logger = Logger.getLogger(DrugbankXmlFileRecordReader.class);

	@HttpDownload(url = "http://www.drugbank.ca/system/downloads/current/drugbank.xml.zip", decompress = true, targetFileName = "drugbank.xml")
	private File drugbankXmlFile;

	public DrugbankXmlFileRecordReader(File workDirectory, boolean clean) throws IOException {
		super(ca.drugbank.DrugType.class, workDirectory, clean, null);
	}

	public DrugbankXmlFileRecordReader(File dataFile) throws IOException {
		super(ca.drugbank.DrugType.class, dataFile, null);
	}

	@Override
	protected DrugBankDrugRecord initializeNewRecord(Object entry) {
		if (ca.drugbank.DrugType.class.isInstance(entry)) {
			return new DrugBankDrugRecord((ca.drugbank.DrugType) entry);
		}
		logger.warn("Expected ca.drugbank.DrugType, but observed " + entry.getClass().getName()
				+ ". Skipping record...");
		return null;
	}

	@Override
	protected boolean hasTaxonOfInterest(DrugBankDrugRecord record) {
		return true;
	}

	@Override
	protected InputStream initializeInputStreamFromDownload() throws IOException {
		if (drugbankXmlFile.getName().endsWith(".gz")) {
			return new GZIPInputStream(new FileInputStream(drugbankXmlFile));
		}
		return new FileInputStream(drugbankXmlFile);
	}

}
