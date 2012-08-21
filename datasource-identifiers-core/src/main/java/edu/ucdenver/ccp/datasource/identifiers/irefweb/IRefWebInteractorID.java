package edu.ucdenver.ccp.datasource.identifiers.irefweb;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.InteractorID;


public class IRefWebInteractorID extends InteractorID {

	private static final String ID_PREFIX = "IREFWEB_";

	public IRefWebInteractorID(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.IREFWEB;
	}

	@Override
	public String toString() {
		return ID_PREFIX + super.toString();
	}

}
