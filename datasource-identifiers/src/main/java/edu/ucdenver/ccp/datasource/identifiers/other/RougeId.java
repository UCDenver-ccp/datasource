package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class RougeId extends StringDataSourceIdentifier {
		public RougeId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.ROUGE;
		}
	}