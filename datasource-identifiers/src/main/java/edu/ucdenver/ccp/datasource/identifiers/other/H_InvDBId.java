package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class H_InvDBId extends StringDataSourceIdentifier {
		public H_InvDBId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.H_INVDB;
		}
	}
