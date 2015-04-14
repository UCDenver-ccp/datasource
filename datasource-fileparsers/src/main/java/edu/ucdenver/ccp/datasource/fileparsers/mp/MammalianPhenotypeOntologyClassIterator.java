package edu.ucdenver.ccp.fileparsers.mp;

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
 * This class iterates over the mammalian phenotype ontology obo file and returns OBORecords for
 * each class it encounters.
 * 
 * @author bill
 * 
 */
public class MammalianPhenotypeOntologyClassIterator extends OboClassIterator {

	public static final String FILE_URL = "http://obo.cvs.sourceforge.net/viewvc/obo/obo/ontology/phenotype/mammalian_phenotype.obo";

	@HttpDownload(url = FILE_URL)
	private File mpOntologyOboFile;

	public static final String MP_ID_PREFIX = "MP:";

	public MammalianPhenotypeOntologyClassIterator(File oboOntologyFile, ObsoleteTermHandling  obsoleteHandling) throws IOException, OBOParseException {
		super(oboOntologyFile, CharacterEncoding.UTF_8, obsoleteHandling);
	}

	public MammalianPhenotypeOntologyClassIterator(File workDirectory, boolean clean, ObsoleteTermHandling obsoleteHandling) throws IOException,
			OBOParseException {
		super(workDirectory, clean,obsoleteHandling);
	}

	@Override
	protected Set<String> getOntologyIdPrefixes() {
		return CollectionsUtil.createSet(MP_ID_PREFIX);
	}

	@Override
	protected OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException {
		return new OboUtil(mpOntologyOboFile, CharacterEncoding.UTF_8);
	}

}
