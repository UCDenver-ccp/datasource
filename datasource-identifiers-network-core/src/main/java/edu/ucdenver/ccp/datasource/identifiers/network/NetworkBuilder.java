package edu.ucdenver.ccp.datasource.identifiers.network;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.FileUtil;

public abstract class NetworkBuilder {
	
	
	private final NetworkSettings networkSettings;
	
//	public NetworkBuilder(Properties networkProperties) {
//		this.networkSettings = new NetworkSettings(networkProperties);
//	}

	public NetworkBuilder(NetworkSettings networkSettings) {
		this.networkSettings = networkSettings;
	}

//	public void buildKnowledgeNetwork(File hprdIdMappingsTxtFile, File dipFile, File keggMmuGeneMapTabFile,
//			Float goPadScoreThreshold, File taggedGeneNameOutputFile) throws SecurityException, NoSuchMethodException,
//			IOException, OBOParseException {
//		populateNetworkWithGeneNodes(hprdIdMappingsTxtFile, dipFile);
//		populateNetworkWithTermNodes(taggedGeneNameOutputFile);
//		populateNetworkWithEdges(keggMmuGeneMapTabFile, dipFile);
//		createMasterEdges(goPadScoreThreshold);
//		computeResourceReliabilities();
//	}
//
//	private void populateNetworkWithGeneNodes(File hprdIdMappingsTxtFile, File dipFile) throws SecurityException,
//			NoSuchMethodException, IOException {
//		NetworkGeneNodeInserter gng = new NetworkGeneNodeInserter(networkSettings,
//				hprdIdMappingsTxtFile);
//		cleanNetwork(networkSettings);
//		gng.populateGraphWithEntrezGeneNodes();
//		gng.addGeneIdCrossReferences(dipFile);
//
//	}
//
//	private void populateNetworkWithTermNodes(File taggedGeneNameOutputFile) throws SecurityException, IOException,
//			NoSuchMethodException, OBOParseException {
//		NetworkTermNodeInserter tng = new NetworkTermNodeInserter(networkSettings);
//		tng.loadGeneOntologyNodes();
//		tng.loadInterProNodes();
//		tng.loadMammalianPhenotypeOntologyNodes();
//		tng.loadKeggPathwayNodes();
//		tng.loadPubmedNodes();
//		tng.loadPubmedNodes(taggedGeneNameOutputFile);
//	}
//
//	private void populateNetworkWithEdges(File keggMmuGeneMapTabFile, File dipFile) throws SecurityException,
//			NoSuchMethodException, IOException, OBOParseException {
//		NetworkEdgeInserter eg = new NetworkEdgeInserter(networkSettings);
//		eg.generateGeneOntology_IsA_Edges();
//		eg.generateMpOntology_IsA_Edges();
//		eg.generateMpOntology2GeneEdges();
//		eg.generateGeneOntology2GeneEdges();
//		eg.generateInterPro2GeneEdges();
//		eg.generateKegg2GeneEdges(keggMmuGeneMapTabFile);
//		eg.generateDipInteractionEdges(dipFile);
//	}
//
//	/**
//	 * Creates a "master" edge between each gene pair that is asserted as interacting by some data
//	 * source. The master edge will track the experts asserting a gene pair interaction as well as
//	 * the various edge scores that are produced during processing.
//	 * 
//	 * @param goPadScoreThreshold
//	 */
//	private void createMasterEdges(Float goPadScoreThreshold) {
//		NetworkMasterEdgeInserter meg = new NetworkMasterEdgeInserter(networkSettings);
//		meg.catalogGeneOntology2GeneEdgesOnMaster(goPadScoreThreshold);
//		meg.catalogInterPro2GeneEdgesOnMaster();
//		meg.catalogKegg2GeneEdgesOnMaster();
//		meg.catalogMpOntology2GeneEdgesOnMaster();
//		meg.catalogDipInteractionEdgesOnMaster();
//	}
//
//	private void computeResourceReliabilities() {
//		NetworkReliabilityScorer scorer = new NetworkReliabilityScorer(networkSettings);
//		scorer.computeResourceReliabilityScores();
//	}
	
	/**
	 * Makes a local copy of the Neo4j graph files to serve as a backup
	 * 
	 * @param networkProperties
	 */
	public abstract void backupGraph(String backupFileNamePrefix) throws IOException; 

	public abstract void cleanNetwork() throws IOException; 
//	{
//		File graphDirectory = networkSettings.getGraphDirectory();
//		logger.info(String.format("Cleaning graph directory: %s", graphDirectory.getAbsolutePath()));
//		FileUtil.cleanDirectory(graphDirectory);
//	}

	public NetworkSettings getNetworkSettings() {
		return networkSettings;
	}

}
