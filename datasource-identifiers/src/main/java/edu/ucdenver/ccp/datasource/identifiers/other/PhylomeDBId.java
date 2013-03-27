package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PhylomeDBId extends StringDataSourceIdentifier {
		public PhylomeDBId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.PHYLOMEDB;
		}
	}