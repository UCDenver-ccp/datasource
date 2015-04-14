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
@Record(dataSource = DataSource.DIP, label = "detection method")
@Getter
public class DipInteractionDetectionMethod extends MiOntologyIdTermPair {

	@RecordField(comment="Interaction detection methods, taken from the corresponding PSI-MI controlled Vocabulary, and represented as databaseName:identifier(methodName), separated by \"|\".", label = "method id")
	private final MolecularInteractionOntologyTermID detectionMethodId;

	@RecordField(label = "method name")
	private final String detectionMethodName;

	/**
	 * @param id
	 * @param termName
	 */
	public DipInteractionDetectionMethod(MolecularInteractionOntologyTermID id, String termName) {
		super(id, termName);
		this.detectionMethodId = getId();
		this.detectionMethodName = getTermName();
	}

}
