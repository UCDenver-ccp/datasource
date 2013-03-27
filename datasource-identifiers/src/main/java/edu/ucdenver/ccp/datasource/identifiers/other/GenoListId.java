package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class GenoListId extends StringDataSourceIdentifier {
		public GenoListId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.GENOLIST;
		}
	}