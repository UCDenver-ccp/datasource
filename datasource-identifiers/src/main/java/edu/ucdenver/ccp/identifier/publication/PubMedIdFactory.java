/**
 * 
 */
package edu.ucdenver.ccp.identifier.publication;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class PubMedIdFactory {

	private static final Logger logger = Logger.getLogger(PubMedIdFactory.class);

	/**
	 * @param id
	 * @return an initialized {@link PubMedID} if the input string can be parsed, null otherwise.
	 */
	public static PubMedID createPubMedId(String id) {
		if (id == null)
			return null;
		String normId = id.trim().toLowerCase();
		if (normId.matches("pmid:-1") || normId.matches("pubmed:-1"))
			return null;
		if (normId.matches("pmid:\\d+"))
			return new PubMedID(StringUtil.removePrefix(normId, "pmid:"));
		if (normId.matches("pubmed:\\d+"))
			return new PubMedID(StringUtil.removePrefix(normId, "pubmed:"));
		if (normId.matches(RegExPatterns.HAS_NUMBERS_ONLY))
			return new PubMedID(normId);
		logger.warn("Unable to extract PubMed identifier from: " + id + ". Returning null.");
		return null;

	}

}
