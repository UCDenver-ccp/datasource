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
 * This class iterates over the gene ontology obo file and returns OBORecords for each class it
 * encounters.
 * 
 * @author bill
 * 
 */
public class GeneOntologyClassIterator extends OboClassIterator {

	public static final String FILE_URL = "http://geneontology.org/ontology/obo_format_1_2/gene_ontology_ext.obo";
	public static final String ENCODING = "ASCII";

	@HttpDownload(url = FILE_URL)
	private File geneOntologyOboFile;

	public static final String GO_ID_PREFIX = "GO:";

	public GeneOntologyClassIterator(File oboOntologyFile, ObsoleteTermHandling  obsoleteHandling) throws IOException, OBOParseException {
		super(oboOntologyFile, CharacterEncoding.UTF_8, obsoleteHandling);
	}

	public GeneOntologyClassIterator(File workDirectory, boolean clean, ObsoleteTermHandling obsoleteHandling) throws IOException, OBOParseException {
		super(workDirectory, clean,obsoleteHandling);
	}

	@Override
	protected Set<String> getOntologyIdPrefixes() {
		return CollectionsUtil.createSet(GO_ID_PREFIX);
	}

	@Override
	protected OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException {
		return new OboUtil(geneOntologyOboFile, CharacterEncoding.UTF_8);
	}

}
