package edu.ucdenver.ccp.datasource.fileparsers.obo;

import org.geneontology.oboedit.datamodel.OBOClass;

import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;

/**
 * Simple wrapper for the OBOEdit <code>OBOClass</code> class allowing it to be treated as a
 * <code>DataRecord</code>.
 * 
 * @author bill
 * 
 */
public class OBOClassRecord implements DataRecord {

	private final OBOClass oboClass;

	public OBOClassRecord(OBOClass oboClass) {
		this.oboClass = oboClass;
	}

	public OBOClass getOboClass() {
		return oboClass;
	}

}
