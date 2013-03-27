package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class EuPathDBId extends StringDataSourceIdentifier {
		public EuPathDBId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.EUPATHDB;
		}
	}