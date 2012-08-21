package edu.ucdenver.ccp.datasource.identifiers.dip;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.InteractionID;

/**
 * Example URL: http://dip.doe-mbi.ucla.edu/dip/DIPview.cgi?IK=45158
 * @author bill
 *
 */
public class DipInteractionID extends InteractionID {
	/**
	 * 
	 * @param resourceID
	 */
	public DipInteractionID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DIP;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.matches("DIP-\\d+E"))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid DIP Interaction ID detected %s", resourceID));
	}

}
