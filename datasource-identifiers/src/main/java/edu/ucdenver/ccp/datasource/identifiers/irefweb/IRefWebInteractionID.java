package edu.ucdenver.ccp.datasource.identifiers.irefweb;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.InteractionID;

public class IRefWebInteractionID extends InteractionID {

	private static final String ID_PREFIX = "IREFWEB_";

	public IRefWebInteractionID(String resourceID) {
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