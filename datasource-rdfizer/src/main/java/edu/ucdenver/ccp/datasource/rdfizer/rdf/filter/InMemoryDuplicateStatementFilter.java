/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.rdfizer.rdf.filter.DuplicateStatementFilter;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class InMemoryDuplicateStatementFilter implements DuplicateStatementFilter {

	private static final Logger logger = Logger.getLogger(InMemoryDuplicateStatementFilter.class);

	private int CHECK_INTERVAL = 10000;
	private Map<Statement, Integer> stmtToObservationCountMap;
	private long stmtCount;
	private boolean heapFull = false;
	private boolean isLeakProof = true;

	public InMemoryDuplicateStatementFilter() {
		this.stmtToObservationCountMap = new HashMap<Statement, Integer>();
		stmtCount = 0;
		heapFull = false;

		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		logger.debug("MAX: " + maxMemory);
		logger.debug("FREE: " + freeMemory);
		logger.debug("TOTAL: " + totalMemory);

	}

	@Override
	public boolean alreadyObservedStatement(Statement stmt) {
		boolean alreadyObserved = stmtToObservationCountMap.containsKey(stmt);
		if (heapFull) {
			return alreadyObserved;
		}
		if (stmtCount++ % CHECK_INTERVAL == 0) {
			double free = percentageOfHeapFree();
			if (free < 0.05) {
				logger.info("Heap is at capacity. New statements may yield duplicates.");
				// the heap is almost full, no new statements will be added to the
				// stmtToObservationCountMap
				heapFull = true;
				return alreadyObserved;
			}
			// if only 10% of the heap remains then cull the map
			if (free < 0.1) {
				CHECK_INTERVAL = 10000;
				cullObservedStatements();
			}
		}
		CollectionsUtil.addToCountMap(stmt, stmtToObservationCountMap);
		return alreadyObserved;
	}

	private double percentageOfHeapFree() {
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long usedMemory = totalMemory - freeMemory;
		double percentFree = 1.0 - ((usedMemory * 1.0) / (maxMemory * 1.0));
		logger.debug("Max: " + maxMemory + " Used: " +usedMemory + " Free: " + freeMemory + " Total: " + totalMemory + " % free: "
				+ Double.toString(percentFree));
		return percentFree;
	}

	/**
	 * @param alreadyObservedFieldUris
	 * @param processedRecordsCount
	 * @return a mapping from field uris to observation counts
	 */
	private void cullObservedStatements() {
		// once you cull statements, it's no longer a guarantee that all duplicates will be filtered
		isLeakProof = false;
		Collection<Entry<Statement, Integer>> entriesToRemove = new ArrayList<Entry<Statement, Integer>>();
		int beforeSize = stmtToObservationCountMap.size();
		for (Entry<Statement, Integer> entry : stmtToObservationCountMap.entrySet()) {
			Integer count = entry.getValue();
			if (count > 1) {
				entriesToRemove.add(entry);
			}
		}
		for (Entry<Statement, Integer> entryToRemove : entriesToRemove) {
			stmtToObservationCountMap.remove(entryToRemove.getKey());
		}
		int afterSize = stmtToObservationCountMap.size();
		logger.debug("Culled already observed field uris. Size before = " + beforeSize + " Size after = " + afterSize);
	}

	@Override
	public boolean isLeakProof() {
		return isLeakProof;
	}

	/* (non-Javadoc)
	 * @see edu.ucdenver.ccp.rdfizer.rdf.filter.DuplicateStatementFilter#shutdown()
	 */
	@Override
	public void shutdown() throws IOException {
		// no need to do anything here
	}

	/* (non-Javadoc)
	 * @see edu.ucdenver.ccp.rdfizer.rdf.filter.DuplicateStatementFilter#alreadyObservedRecordUri(org.openrdf.model.impl.URIImpl)
	 */
	@Override
	public boolean alreadyObservedRecordUri(URIImpl recordUri) {
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.ucdenver.ccp.rdfizer.rdf.filter.DuplicateStatementFilter#logRecordUri(org.openrdf.model.impl.URIImpl)
	 */
	@Override
	public void logRecordUri(URIImpl subRecordUri) {
		// do nothing
	}

	
	// /**
	// * @param alreadyObservedFieldUris
	// * @param processedRecordsCount
	// * @return a mapping from field uris to observation counts
	// */
	// private static Map<String, ObservationCount> cullObservedFieldUris() {
	// Map<String, ObservationCount> culledMap = new HashMap<String, ObservationCount>();
	//
	// int beforeSize = alreadyObservedFieldUris.size();
	// for (Entry<String, ObservationCount> entry : alreadyObservedFieldUris.entrySet()) {
	// ObservationCount oc = entry.getValue();
	// if (oc.getObservedCount() > 1) {
	// culledMap.put(entry.getKey(), entry.getValue());
	// }
	// if ((processedRecordsCount - oc.getLastObserved()) < 10000) {
	// culledMap.put(entry.getKey(), entry.getValue());
	// }
	// }
	// int afterSize = culledMap.size();
	// logger.info("Culled already observed field uris. Size before = " + beforeSize +
	// " Size after = " + afterSize);
	// return culledMap;
	// }
}
