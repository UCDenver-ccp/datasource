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
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
@Record(dataSource=DataSource.IREFWEB, label="bio role")
@Getter
public class IRefWebInteractorBiologicalRole extends MiOntologyIdTermPair {

	@RecordField
	private final MolecularInteractionOntologyTermID biologicalRoleId;
	@RecordField
	private final String biologicalRoleName;
	
	/**
	 * @param id
	 * @param termName
	 */
	public IRefWebInteractorBiologicalRole(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.biologicalRoleId = getId();
		this.biologicalRoleName = getTermName();
	}
	public IRefWebInteractorBiologicalRole(MolecularInteractionOntologyTermID id) {
		super(id);
		this.biologicalRoleId = getId();
		this.biologicalRoleName = getTermName();
	}

}
