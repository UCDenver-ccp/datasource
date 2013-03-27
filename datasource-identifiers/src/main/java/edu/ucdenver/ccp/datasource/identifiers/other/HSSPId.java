package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class HSSPId extends StringDataSourceIdentifier {
		public HSSPId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.HSSP;
		}
	}