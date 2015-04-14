/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.dip;

import lombok.Getter;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.fileparsers.field.MiOntologyIdTermPair;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
@Record(dataSource = DataSource.DIP, label = "source database")
@Getter
public class DipInteractionSourceDatabase extends MiOntologyIdTermPair {

	@RecordField(comment = "Source database and identifiers, taken from the corresponding PSI-MI controlled vocabulary, and represented as databaseName:identifier(sourceName). Multiple source databases can be separated by \"|\".", label = "database id")
	private final MolecularInteractionOntologyTermID sourceDatabaseId;

	@RecordField(label = "database name")
	private final String sourceDatabaseName;

	/**
	 * @param id
	 * @param termName
	 */
	public DipInteractionSourceDatabase(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.sourceDatabaseId = getId();
		this.sourceDatabaseName = getTermName();
	}

}
