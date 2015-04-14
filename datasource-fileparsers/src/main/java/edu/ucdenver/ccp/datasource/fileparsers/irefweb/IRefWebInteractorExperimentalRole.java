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
@Record(dataSource = DataSource.IREFWEB, label = "experimental role")
@Getter
public class IRefWebInteractorExperimentalRole extends MiOntologyIdTermPair {

	@RecordField
	private final MolecularInteractionOntologyTermID experimentalRoleId;
	@RecordField
	private final String experimentalRoleName;

	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebInteractorExperimentalRole(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.experimentalRoleId = getId();
		this.experimentalRoleName = getTermName();
	}

	public IRefWebInteractorExperimentalRole(MolecularInteractionOntologyTermID id) {
		super(id);
		this.experimentalRoleId = getId();
		this.experimentalRoleName = getTermName();
	}

}
