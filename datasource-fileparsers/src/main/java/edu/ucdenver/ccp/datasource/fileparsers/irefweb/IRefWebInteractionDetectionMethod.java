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
@Record(dataSource=DataSource.IREFWEB, label="detection method")
@Getter
public class IRefWebInteractionDetectionMethod extends MiOntologyIdTermPair {

		@RecordField
		private final MolecularInteractionOntologyTermID detectionMethodId;
		@RecordField
		private final String detectionMethodName;
		
		/**
		 * @param id
		 * @param termName
		 */
		public IRefWebInteractionDetectionMethod(MolecularInteractionOntologyTermID id, String termName) {
			super(id, termName);
			this.detectionMethodId = getId();
			this.detectionMethodName = getTermName();
		}
		
		public IRefWebInteractionDetectionMethod(MolecularInteractionOntologyTermID id) {
			super(id);
			this.detectionMethodId = getId();
			this.detectionMethodName = getTermName();
		}
	
}
