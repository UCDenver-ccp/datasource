package edu.ucdenver.ccp.datasource.identifiers.dip;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.InteractorID;

public class DipInteractorID extends InteractorID {

	public DipInteractorID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.DIP;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		resourceID = super.validate(resourceID);
		if (resourceID.matches("DIP-\\d+N"))
			return resourceID;
		throw new IllegalArgumentException(String.format("Invalid DIP Interactor ID detected %s", resourceID));
	}

}
