package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class SABIO_RKId extends StringDataSourceIdentifier {
		public SABIO_RKId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.SABIO_RK;
		}
	}
