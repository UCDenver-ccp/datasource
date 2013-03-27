package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class InParanoidId extends StringDataSourceIdentifier {
		public InParanoidId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.INPARANOID;
		}
	}