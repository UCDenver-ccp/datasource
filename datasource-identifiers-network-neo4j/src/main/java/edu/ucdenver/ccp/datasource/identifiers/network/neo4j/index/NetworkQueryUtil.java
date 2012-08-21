/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.index;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public class NetworkQueryUtil {

	public static Query createIdentifierIndexQuery(String defaultFieldName, String searchStr) {
		String idStr = searchStr.replaceAll(":", "_");
		try {
			Query query = new QueryParser(Version.LUCENE_30, defaultFieldName, new IdIndexAnalyzer()).parse(idStr);
			// logger.info("Query: " + query);
			return query;
		} catch (ParseException e) {
			throw new RuntimeException(
					String.format("Lucene query parser exception while parsing query: %s", searchStr));
		}
	}
}
