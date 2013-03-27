package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PMAP_CutDBId extends StringDataSourceIdentifier {
		public PMAP_CutDBId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.PMAP_CUTDB;
		}
	}