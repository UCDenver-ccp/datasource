package edu.ucdenver.ccp.datasource.fileparsers.obo.impl;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboClassIterator;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil.ObsoleteTermHandling;

/**
 * This class iterates over the NCBI Taxonomy obo file and returns OBORecords for each class it
 * encounters.
 * 
 * @author bill
 * 
 */
public class NcbiTaxonomyClassIterator extends OboClassIterator {

	@HttpDownload(url = "http://www.berkeleybop.org/ontologies/obo-all/ncbi_taxonomy/ncbi_taxonomy.obo")
	private File ncbiTaxonomyOboFile;

	public static final String TAXON_ID_PREFIX = "NCBITaxon:";

	public NcbiTaxonomyClassIterator(File oboOntologyFile, ObsoleteTermHandling  obsoleteHandling) throws IOException, OBOParseException {
		super(oboOntologyFile, CharacterEncoding.UTF_8, obsoleteHandling);
	}

	public NcbiTaxonomyClassIterator(File workDirectory, boolean clean, ObsoleteTermHandling obsoleteHandling) throws IOException, OBOParseException {
		super(workDirectory, clean,obsoleteHandling);
	}

	@Override
	protected Set<String> getOntologyIdPrefixes() {
		return CollectionsUtil.createSet(TAXON_ID_PREFIX);
	}

	@Override
	protected OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException {
		return new OboUtil(ncbiTaxonomyOboFile, CharacterEncoding.UTF_8);
	}

}
