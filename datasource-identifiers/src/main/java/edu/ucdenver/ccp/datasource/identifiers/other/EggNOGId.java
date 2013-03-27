package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class EggNOGId extends StringDataSourceIdentifier {
		public EggNOGId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.EGGNOG;
		}
	}
