package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class World_2DPAGEId extends StringDataSourceIdentifier {
	public World_2DPAGEId(String id) {
		super(id);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.WORLD_2DPAGE;
	}
}