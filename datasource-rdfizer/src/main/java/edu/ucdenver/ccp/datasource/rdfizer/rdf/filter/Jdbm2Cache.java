/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf.filter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.FileUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Jdbm2Cache implements DiskBasedHash {

	private static final Logger logger = Logger.getLogger(Jdbm2Cache.class);

	private final RecordManager recMan;
	// private final PrimaryTreeMap<String, String> map;
	private final Map<String, PrimaryTreeMap<String, String>> maps;

	private long addCount;

	public Jdbm2Cache(File storageFile) throws IOException {
		File storageDir = storageFile.getParentFile();
		logger.info("JDBMS CACHE STORAGE: " + storageDir.getAbsolutePath());
		if (!storageDir.exists()) {
			logger.info("Creating directory to store duplicate filter cache files: " + storageDir);
			FileUtil.mkdir(storageDir);
		} else {
			FileUtil.cleanDirectory(storageDir);
		}

		recMan = RecordManagerFactory.createRecordManager(storageFile.getAbsolutePath());
		maps = new HashMap<String, PrimaryTreeMap<String, String>>();
		// map = recMan.treeMap("triples");
	}

	@Override
	public void add(Object o) throws IOException {
		addCount++;
		String[] nodeKeyValue = getNodeKeyValue(o.toString());
		String nodeKey = nodeKeyValue[0];
		String sha1 = nodeKeyValue[1];
		PrimaryTreeMap<String, String> map = getMap(nodeKey);
		map.put(sha1, "");
		if (addCount % 1000000 == 0) {
			commitToDisk();
		}

	}

	/**
	 * @throws IOException
	 */
	private void commitToDisk() throws IOException {
		logger.info("committing jdbm2 cache to disk...");
		recMan.commit();
		for (Entry<String, PrimaryTreeMap<String, String>> entry : maps.entrySet()) {
			logger.info("cache size (" + entry.getKey() + "): " + entry.getValue().size());
		}
	}

	/**
	 * @param nodeKey
	 * @return
	 */
	private PrimaryTreeMap<String, String> getMap(String nodeKey) {
		if (maps.containsKey(nodeKey)) {
			return maps.get(nodeKey);
		}
		PrimaryTreeMap<String, String> map = recMan.treeMap(nodeKey);
		maps.put(nodeKey, map);
		return map;
	}

	@Override
	public boolean contains(Object o) {
		String[] nodeKeyValue = getNodeKeyValue(o.toString());
		String nodeKey = nodeKeyValue[0];
		String sha1 = nodeKeyValue[1];
		PrimaryTreeMap<String, String> map = getMap(nodeKey);
		return map.containsKey(sha1);
		// return map.containsKey(o.toString());
	}

	@Override
	public void shutdown() throws IOException {
		logger.info("shutting down Jdbm2Cache...");
		commitToDisk();
		recMan.close();
	}

	/**
	 * @param string
	 * @return
	 */
	private String[] getNodeKeyValue(String s) {
		int underscoreIndex = s.indexOf("_");
		String sha1 = s.substring(underscoreIndex + 1);
		String nodeKey = s.substring(0, underscoreIndex);
		// logger.debug("NODEKEY: " + nodeKey + " SHA1: " + sha1);
		return new String[] { nodeKey, sha1 };
	}

}
