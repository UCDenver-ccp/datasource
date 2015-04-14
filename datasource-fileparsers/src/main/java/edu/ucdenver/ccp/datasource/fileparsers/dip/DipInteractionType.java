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
@Record(dataSource = DataSource.DIP, label = "interaction type")
@Getter
public class DipInteractionType extends MiOntologyIdTermPair {

	@RecordField(comment = "Interaction types, taken from the corresponding PSI-MI controlled vocabulary, and represented as dataBaseName:identifier(interactionType), separated by \"|\".  ", label = "type id")
	private final MolecularInteractionOntologyTermID interactionTypeId;

	@RecordField(label = "type name")
	private final String interactionTypeName;

	/**
	 * @param id
	 * @param termName
	 */
	public DipInteractionType(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.interactionTypeId = getId();
		this.interactionTypeName = getTermName();
	}

}
