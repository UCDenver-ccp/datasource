/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.field;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.reflection.ConstructorUtil;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;

/**
 * Some databases used negative taxonomy IDs as a placeholder and attach a specific term name to it,
 * e.g. in IRefWeb there is taxid:-4(in vivo). This record allows for the ID and term name to be
 * paired.
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
// @Data
// @Record(dataSource = DataSource.NCBI_TAXON)
@EqualsAndHashCode
@ToString
public abstract class NcbiTaxonomyIdTermPair {

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyIdTermPair.class);

	private final NcbiTaxonomyID id;
	private final String termName;

	protected NcbiTaxonomyIdTermPair(NcbiTaxonomyID id, String termName) {
		this.id = id;
		this.termName = termName;
	}
	
	protected NcbiTaxonomyIdTermPair(String termName) {
		this.id = null;
		this.termName = termName;
	}

	/**
	 * @return the id
	 */
	protected NcbiTaxonomyID getId() {
		return id;
	}

	/**
	 * @return the termName
	 */
	protected String getTermName() {
		return termName;
	}

	/**
	 * @param input
	 * @return parses Strings of the form "taxid:4932(Saccharomyces cerevisiae)" and returns a
	 *         {@link NcbiTaxonomyIdTermPair}
	 */
	public static <T extends NcbiTaxonomyIdTermPair> T parseString(Class<T> taxIdTermPairClass, String input) {
		if (input.startsWith("taxid:-")) {
			Pattern p = Pattern.compile("taxid:-\\d+\\((.*?)\\)");
			Matcher m = p.matcher(input);
			if (m.find()) {
				return (T) ConstructorUtil.invokeConstructor(taxIdTermPairClass.getName(), m.group(1));
				// return new NcbiTaxonomyIdTermPair(null, m.group(1));
			}
			logger.warn("Unable to extract taxon name from: " + input);
			return null;
		}
		Pattern methodIDPattern = Pattern.compile("(taxid:\\d+),?\\((.*?)\\)");
		Matcher m = methodIDPattern.matcher(input);
		if (m.find()) {
			return (T) ConstructorUtil.invokeConstructor(taxIdTermPairClass.getName(), new NcbiTaxonomyID(m.group(1)),
					m.group(2));
			// return new NcbiTaxonomyIdTermPair(new NcbiTaxonomyID(m.group(1)), m.group(2));
		}
		logger.warn("Unable to extract NcbiTaxonomyIdTermPair from: " + input);
		return null;
	}

}
