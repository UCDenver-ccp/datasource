package edu.ucdenver.ccp.datasource.identifiers.network.neo4j;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.string.StringBufferUtil;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

public abstract class NetworkBuilderTester extends DefaultTestCase {

	private static final String NEO4J_PROPERTIES_FILE_NAME = "neo4j.properties";
	private File dataDirectory;
	protected File baseDirectory;
	private File propertiesDirectory;
	private File neo4jPropertiesFile;
	private Neo4jNetworkSettings networkSettings;
	private File networkDirectory;

	@Before
	public void setUp() throws IOException  {
		baseDirectory = folder.newFolder("base");
		dataDirectory = folder.newFolder("data");
		networkDirectory = folder.newFolder("network");

		propertiesDirectory = folder.newFolder("props");
		neo4jPropertiesFile = createNeo4jPropertiesFile();

		networkSettings = new Neo4jNetworkSettings(networkDirectory, dataDirectory);
//		networkSettings.setDipFile(TestDataFileFactory.initDipDataFile(getDataDirectory()));
//		networkSettings.setHprdIdMappingsTxtFile(TestDataFileFactory.initHprdIdMappingsTxtFile(getDataDirectory()));
//		networkSettings.setKeggGeneMapTabFile(TestDataFileFactory.initKeggMmuGeneMapTabTabFile(getDataDirectory()));
		assertTrue(dataDirectory.exists());
	}

	/**
	 * @return the NCBI taxonomy ID to use when building this test network
	 */
	protected abstract NcbiTaxonomyID getNetworkTaxonomyId();

	private File createNeo4jPropertiesFile() throws IOException  {
		File neo4jPropertiesFile = FileUtil.appendPathElementsToDirectory(propertiesDirectory,
				NEO4J_PROPERTIES_FILE_NAME);
		/* @formatter:off */
		List<String> lines = CollectionsUtil.createList(
				"neostore.nodestore.db.mapped_memory=90M",
				"neostore.relationshipstore.db.mapped_memory=1G", 
				"neostore.propertystore.db.mapped_memory=50M",
				"neostore.propertystore.db.strings.mapped_memory=100M",
				"neostore.propertystore.db.arrays.mapped_memory=0M");
		/* @formatter:on */
		FileWriterUtil.printLines(lines, neo4jPropertiesFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);
		return neo4jPropertiesFile;
	}

	protected File getGraphDirectory() {
		return networkDirectory;
	}

	protected GraphDatabaseService getGraph() {
		return new EmbeddedGraphDatabase(getGraphDirectory().getAbsolutePath());
	}

	protected File getDataDirectory() {
		return dataDirectory;
	}

	protected Neo4jNetworkSettings getNetworkSettings() {
		return networkSettings;
	}

	protected String processErrorMessages(Collection<String> errorMessages) {
		String egIdPattern = "\\[.*?ENTREZ_GENE_ID -- (EG_\\d+),?.*?\\]";
		// Pattern egIdPattern = Pattern.compile(egIdPattern);
		Matcher m;
		StringBuffer sb = new StringBuffer();
		sb.append(StringConstants.NEW_LINE);
		for (String errorMessage : errorMessages) {
			errorMessage = errorMessage.replaceAll(egIdPattern, "$1");
			StringBufferUtil.appendLine(sb, errorMessage);
			// m = egIdPattern.matcher(errorMessage);
		}

		return sb.toString();
	}

}
