/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.irefweb;

import lombok.Getter;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.fileparsers.field.MiOntologyIdTermPair;

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.IREFWEB, label = "source db")
@Getter
public class IRefWebInteractionSourceDatabase extends MiOntologyIdTermPair {

	@RecordField
	private final MolecularInteractionOntologyTermID sourceDatabaseId;
	@RecordField
	private final String sourceDatabaseName;

	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebInteractionSourceDatabase(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.sourceDatabaseId = getId();
		this.sourceDatabaseName = getTermName();
	}

	public IRefWebInteractionSourceDatabase(MolecularInteractionOntologyTermID id) {
		super(id);
		this.sourceDatabaseId = getId();
		this.sourceDatabaseName = getTermName();
	}

}
