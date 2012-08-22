package edu.ucdenver.ccp.fileparsers.pro;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.geneontology.oboedit.dataadapter.OBOParseException;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboClassIterator;
import edu.ucdenver.ccp.datasource.fileparsers.obo.OboUtil;

/**
 * This class iterates over the gene ontology obo file and returns OBORecords for each class it
 * encounters.
 * 
 * @author bill
 * 
 */
public class ProOntologyClassIterator extends OboClassIterator {

	@FtpDownload(server = "ftp.pir.georgetown.edu", path = "databases/ontology/pro_obo/", filename = "pro.obo", filetype = FileType.ASCII)
	private File proOntologyOboFile;

	public static final String PR_ID_PREFIX = "PR:";
	public static final String CHEBI_ID_PREFIX = "CHEBI:";
	public static final String GO_ID_PREFIX = "GO:";
	public static final String MOD_ID_PREFIX = "MOD:";
	public static final String SO_ID_PREFIX = "SO:";

	public ProOntologyClassIterator(File oboOntologyFile) throws IOException, OBOParseException {
		super(oboOntologyFile, CharacterEncoding.UTF_8);
	}

	public ProOntologyClassIterator(File workDirectory, boolean clean) throws IOException, OBOParseException {
		super(workDirectory, clean);
	}

	@Override
	protected Set<String> getOntologyIdPrefixes() {
		return CollectionsUtil.createSet(PR_ID_PREFIX, CHEBI_ID_PREFIX, GO_ID_PREFIX, MOD_ID_PREFIX, SO_ID_PREFIX);
	}

	@Override
	protected OboUtil<?> initializeOboUtilFromDownload() throws IOException, OBOParseException {
		return new OboUtil(proOntologyOboFile, CharacterEncoding.UTF_8);
	}

}
