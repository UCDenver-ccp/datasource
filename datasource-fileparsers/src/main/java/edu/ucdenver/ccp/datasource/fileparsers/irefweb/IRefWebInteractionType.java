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
@Record(dataSource = DataSource.IREFWEB, label = "interaction type")
@Getter
public class IRefWebInteractionType extends MiOntologyIdTermPair {
	@RecordField
	private final MolecularInteractionOntologyTermID interactionTypeId;
	@RecordField
	private final String interactionTypeName;

	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebInteractionType(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.interactionTypeId = getId();
		this.interactionTypeName = getTermName();
	}

	public IRefWebInteractionType(MolecularInteractionOntologyTermID id) {
		super(id);
		this.interactionTypeId = getId();
		this.interactionTypeName = getTermName();
	}
}
