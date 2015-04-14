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
@Record(dataSource = DataSource.IREFWEB, label = "interactor type")
@Getter
public class IRefWebInteractorType extends MiOntologyIdTermPair {

	@RecordField
	private final MolecularInteractionOntologyTermID interactorTypeId;
	@RecordField
	private final String interactorTypeName;

	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebInteractorType(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.interactorTypeId = getId();
		this.interactorTypeName = getTermName();
	}

	public IRefWebInteractorType(MolecularInteractionOntologyTermID id) {
		super(id);
		this.interactorTypeId = getId();
		this.interactorTypeName = getTermName();
	}

}
