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
@Record(dataSource = DataSource.IREFWEB, label="host organism")
@Getter
public class IRefWebHostOrganism extends NcbiTaxonomyIdTermPair {

	@RecordField
	private final NcbiTaxonomyID hostOrganismId;
	@RecordField
	private final String hostOrganismName;

	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebHostOrganism(NcbiTaxonomyID id, String termName) {
		super(id, termName);
		this.hostOrganismId = getId();
		this.hostOrganismName = getTermName();
	}
	
	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebHostOrganism(String termName) {
		this(null, termName);
	}

}
