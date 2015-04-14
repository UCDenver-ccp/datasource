/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.irefweb;

import lombok.Getter;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.field.NcbiTaxonomyIdTermPair;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.IREFWEB, label="organism")
@Getter
public class IRefWebInteractorOrganism extends NcbiTaxonomyIdTermPair {

	@RecordField
	private final NcbiTaxonomyID taxonomyId;
	@RecordField
	private final String taxonomyName;

	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebInteractorOrganism(NcbiTaxonomyID id, String termName) {
		super(id, termName);
		this.taxonomyId = getId();
		this.taxonomyName = getTermName();
	}

	public IRefWebInteractorOrganism(String termName) {
		this(null, termName);
	}
}
