/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.dip;

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
@Record(dataSource = DataSource.DIP, label = "organism")
@Getter
public class DipInteractorOrganism extends NcbiTaxonomyIdTermPair {

	@RecordField(comment="Database name for NCBI taxid taken from the PSI-MI controlled vocabulary, represented as databaseName:identifier (typicaly databaseName is set to 'taxid'). Multiple identifiers separated by \"|\". Note: In this column, the databaseName:identifier(speciesName) notation is only there for consistency. Currently no taxonomy identifiers other than NCBI taxid are anticipated, apart from the use of -1 to indicate \"in vitro\", -2 to indicate \"chemical synthesis\", -3 indicates \"unknown\", -4 indicates \"in vivo\" and -5 indicates \"in silico\".", label = "taxonomy ID")
	private final NcbiTaxonomyID taxonomyId;

	@RecordField(label = "taxonomy name")
	private final String taxonomyName;

	/**
	 * @param id
	 * @param termName
	 */
	public DipInteractorOrganism(NcbiTaxonomyID id, String termName) {
		super(id, termName);
		this.taxonomyId = getId();
		this.taxonomyName = getTermName();
	}
	
	
	public DipInteractorOrganism(String termName) {
		this(null, termName);
	}
}
