package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PeroxiBaseId extends StringDataSourceIdentifier {
		public PeroxiBaseId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.PEROXIBASE;
		}
	}