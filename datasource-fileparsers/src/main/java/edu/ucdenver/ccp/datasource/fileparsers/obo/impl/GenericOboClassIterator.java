/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.obo.impl;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboClassIterator;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil.ObsoleteTermHandling;

/**
 * Provides null implementations for the abstract functions in OboClassIterator,
 * hides the functionality of the second constructor in OboBlassIterator.
 * TODO: refactor a base class with this default behavior, then create a 
 * 2nd level base class to provide the functionality of OboClassIterator
 * that the rest of the classes can use.
 *
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class GenericOboClassIterator extends OboClassIterator {

	/**
	 * @param oboOntologyFile
	 * @param encoding
	 * @param  obsoleteHandling 
	 * @throws IOException
	 * @throws OBOParseException
	 */
	public GenericOboClassIterator(File oboOntologyFile, CharacterEncoding encoding, ObsoleteTermHandling  obsoleteHandling) throws IOException,
			OBOParseException {
		super(oboOntologyFile, encoding, obsoleteHandling);
	}

	/* (non-Javadoc)
	 * @see edu.ucdenver.ccp.fileparsers.obo.OboClassIterator#getOntologyIdPrefixes()
	 */
	@Override
	protected Set<String> getOntologyIdPrefixes() {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ucdenver.ccp.fileparsers.obo.OboClassIterator#initializeOboUtilFromDownload()
	 */
	@Override
	protected OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException {
		return null;
	}

}
