package edu.ucdenver.ccp.datasource.fileparsers.obo;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;
import org.geneontology.oboedit.datamodel.OBOClass;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;

/**
 * An abstract class to be used for iterating over the classes in an OBO file. 
 * 
 * @author bill
 * 
 */
public abstract class OboClassIterator extends RecordReader<OBOClassRecord> {

	private final Iterator<OBOClass> oboClassIterator;
	private OBOClassRecord nextRecord = null;

	public OboClassIterator(File oboOntologyFile, CharacterEncoding encoding) throws IOException, OBOParseException {
		FileUtil.validateFile(oboOntologyFile);
		OboUtil<?> oboUtil = new OboUtil(oboOntologyFile, encoding);
		oboClassIterator = oboUtil.getClassIterator();
	}

	public OboClassIterator(File workDirectory, boolean clean) throws IOException, OBOParseException {
		super();
		try {
			DownloadUtil.download(this, workDirectory, null, null, clean);
			OboUtil<?> oboUtil = initializeOboUtilFromDownload();
			oboClassIterator = oboUtil.getClassIterator();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		} catch (IllegalAccessException e) {
			throw new IOException(e);
		}

	}

	@Override
	public boolean hasNext() {
		if (nextRecord == null) {
			if (oboClassIterator.hasNext()) {
				OBOClass oboClass = oboClassIterator.next();
				if (!idStartsWithAllowedPrefix(oboClass.getID()))
					return hasNext();
				nextRecord = new OBOClassRecord(oboClass);
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public OBOClassRecord next() {
		if (!hasNext())
			throw new NoSuchElementException();

		OBOClassRecord recordToReturn = nextRecord;
		nextRecord = null;
		return recordToReturn;
	}

	
	private boolean idStartsWithAllowedPrefix(String id) {
		if (getOntologyIdPrefixes()== null) {
			return true;
		}
		for (String prefix : getOntologyIdPrefixes()) {
			if (id.startsWith(prefix))
				return true;
		}
		return false;
	}
	
	@Override
	public void close() throws IOException {
		// do nothing
	}

	protected abstract Set<String> getOntologyIdPrefixes();

	protected abstract OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException;

}
